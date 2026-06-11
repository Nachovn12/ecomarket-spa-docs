package com.ecomarket.reportes.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO de salida para el reporte de inventario generado.
 */
@Getter
@Setter
public class ReporteInventarioDTO {

    private Long idTienda;
    private Integer productosDisponibles;
    private Integer productosBajoStock;
    private Integer productosSinStock;
}
