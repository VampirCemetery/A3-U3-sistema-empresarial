package com.empresa.sistema.service.impl;

import com.empresa.sistema.entity.MovimientoInventario;
import com.empresa.sistema.entity.Producto;
import com.empresa.sistema.entity.Usuario;
import com.empresa.sistema.repository.MovimientoInventarioRepository;
import com.empresa.sistema.repository.ProductoRepository;
import com.empresa.sistema.repository.UsuarioRepository;
import com.empresa.sistema.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movRepository;
    private final UsuarioRepository usuarioRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository,
            MovimientoInventarioRepository movRepository,
            UsuarioRepository usuarioRepository) {
        this.productoRepository = productoRepository;
        this.movRepository = movRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Producto> findAll() {
        return productoRepository.findByActivoTrue();
    }

    @Override
    public List<Producto> findAllIncludingInactive() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto save(Producto p) {
        return productoRepository.save(p);
    }

    @Override
    public void delete(Long id) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setActivo(false);
            productoRepository.save(p);
        });
    }

    @Override
    public void reactivar(Long id) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setActivo(true);
            productoRepository.save(p);
        });
    }

    @Override
    public void ajustarStock(Long productoId, int delta, String motivo, Long usuarioId) {
        Producto p = productoRepository.findById(productoId).orElseThrow();
        int anterior = p.getStockActual() == null ? 0 : p.getStockActual();
        int nuevo = anterior + delta;
        p.setStockActual(nuevo);
        productoRepository.save(p);

        MovimientoInventario mov = new MovimientoInventario();
        mov.setProducto(p);
        mov.setTipoMovimiento(
                delta >= 0 ? MovimientoInventario.TipoMovimiento.ENTRADA : MovimientoInventario.TipoMovimiento.SALIDA);
        mov.setCantidad(Math.abs(delta));
        mov.setStockAnterior(anterior);
        mov.setStockActual(nuevo);
        mov.setMotivo(motivo);
        if (usuarioId != null) {
            Usuario u = usuarioRepository.findById(usuarioId).orElse(null);
            mov.setUsuario(u);
        }
        movRepository.save(mov);
    }
}
