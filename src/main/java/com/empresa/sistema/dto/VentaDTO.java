package com.empresa.sistema.dto;

import lombok.Data;
import java.util.List;

@Data
public class VentaDTO {
    private Long clienteId;
    private List<DetalleVentaDTO> items;

    @Data
    public static class DetalleVentaDTO {
        private Long productoId;
        private Integer cantidad;
    }
}
