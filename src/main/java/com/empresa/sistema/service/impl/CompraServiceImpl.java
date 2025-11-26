package com.empresa.sistema.service.impl;

import com.empresa.sistema.dto.CompraDTO;
import com.empresa.sistema.entity.*;
import com.empresa.sistema.repository.*;
import com.empresa.sistema.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoInventarioRepository movimientoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Compra> findAll() {
        return compraRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Compra> findById(Long id) {
        return compraRepository.findById(id);
    }

    @Override
    @Transactional
    public Compra crearCompra(CompraDTO dto, Long usuarioId) {
        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        Compra compra = new Compra();
        compra.setProveedor(proveedor);
        compra.setUsuario(usuario);
        compra.setFechaCompra(LocalDate.now());
        compra.setNumeroFactura("COM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        compra.setEstado(Compra.Estado.COMPLETADA);

        List<DetalleCompra> detalles = new ArrayList<>();
        double subtotal = 0;

        for (CompraDTO.DetalleCompraDTO item : dto.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + item.getProductoId()));

            // Increment Stock
            int stockAnterior = producto.getStockActual();
            producto.setStockActual(stockAnterior + item.getCantidad());

            // Update cost if provided? For now, we keep existing logic or update if needed.
            // Let's assume we might update purchase price if provided, but for simplicity,
            // we use current or item cost.
            // In DTO I added costoUnitario. If null, use product.precioCompra.
            Double costo = item.getCostoUnitario() != null ? item.getCostoUnitario() : producto.getPrecioCompra();
            if (costo == null)
                costo = 0.0;

            // Update product purchase price? Maybe.
            producto.setPrecioCompra(costo);
            productoRepository.save(producto);

            // Record Movement
            MovimientoInventario mov = MovimientoInventario.builder()
                    .producto(producto)
                    .tipoMovimiento(MovimientoInventario.TipoMovimiento.ENTRADA)
                    .cantidad(item.getCantidad())
                    .stockAnterior(stockAnterior)
                    .stockActual(producto.getStockActual())
                    .motivo("Compra " + compra.getNumeroFactura())
                    .usuario(usuario)
                    .build();
            movimientoRepository.save(mov);

            // Create Detail
            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(compra);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(costo);
            detalle.setSubtotal(costo * item.getCantidad());

            detalles.add(detalle);
            subtotal += detalle.getSubtotal();
        }

        compra.setDetalles(detalles);
        compra.setSubtotal(subtotal);
        compra.setIva(subtotal * 0.15);
        compra.setTotal(subtotal + compra.getIva());

        return compraRepository.save(compra);
    }
}
