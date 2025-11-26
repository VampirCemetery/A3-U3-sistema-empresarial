package com.empresa.sistema.dto;

import lombok.Data;
import java.util.List;

@Data
public class CompraDTO {
    private Long proveedorId;
    private List<DetalleCompraDTO> items;

    @Data
    public static class DetalleCompraDTO {
        private Long productoId;
        private Integer cantidad;
        private Double costoUnitario; // Optional: if cost changes
    }
}
