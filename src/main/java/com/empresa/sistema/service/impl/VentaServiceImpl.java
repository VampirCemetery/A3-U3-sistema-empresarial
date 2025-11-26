package com.empresa.sistema.service.impl;

import com.empresa.sistema.dto.VentaDTO;
import com.empresa.sistema.entity.*;
import com.empresa.sistema.repository.*;
import com.empresa.sistema.service.VentaService;
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
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoInventarioRepository movimientoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> findById(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    @Transactional
    public Venta crearVenta(VentaDTO dto, Long usuarioId) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setUsuario(usuario);
        venta.setFechaVenta(LocalDate.now());
        venta.setNumeroFactura("FAC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        venta.setEstado(Venta.Estado.COMPLETADA);

        List<DetalleVenta> detalles = new ArrayList<>();
        double subtotal = 0;

        for (VentaDTO.DetalleVentaDTO item : dto.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + item.getProductoId()));

            if (producto.getStockActual() < item.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para: " + producto.getNombre());
            }

            // Decrement Stock
            int stockAnterior = producto.getStockActual();
            producto.setStockActual(stockAnterior - item.getCantidad());
            productoRepository.save(producto);

            // Record Movement
            MovimientoInventario mov = MovimientoInventario.builder()
                    .producto(producto)
                    .tipoMovimiento(MovimientoInventario.TipoMovimiento.SALIDA)
                    .cantidad(item.getCantidad())
                    .stockAnterior(stockAnterior)
                    .stockActual(producto.getStockActual())
                    .motivo("Venta " + venta.getNumeroFactura())
                    .usuario(usuario)
                    .build();
            movimientoRepository.save(mov);

            // Create Detail
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            detalle.setSubtotal(producto.getPrecioVenta() * item.getCantidad());

            detalles.add(detalle);
            subtotal += detalle.getSubtotal();
        }

        venta.setDetalles(detalles);
        venta.setSubtotal(subtotal);
        venta.setIva(subtotal * 0.15); // Assuming 15% IVA, configurable?
        venta.setTotal(subtotal + venta.getIva());

        return ventaRepository.save(venta);
    }
}
