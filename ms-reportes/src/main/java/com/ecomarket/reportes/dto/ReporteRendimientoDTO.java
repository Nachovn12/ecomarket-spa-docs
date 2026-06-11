package com.ecomarket.reportes.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO de salida para el reporte de rendimiento de tienda generado.
 */
@Getter
@Setter
public class ReporteRendimientoDTO {

    private Long idTienda;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double ventasPorTienda;
    private Integer pedidosEntregados;
    private Integer stockBajo;
    private Double rendimientoOperativo;
}
