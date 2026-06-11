package com.ecomarket.reportes.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO de salida para el reporte de ventas generado.
 */
@Getter
@Setter
public class ReporteVentasDTO {

    private Long idTienda;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double ventasTotales;
    private Integer totalTransacciones;
    private Integer productosVendidos;
}
