package com.ecomarket.reportes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO de salida para el reporte de rendimiento de tienda generado.
 */
@Getter
@Setter
@Schema(description = "Resultado del reporte de rendimiento de una tienda en un periodo")
public class ReporteRendimientoDTO {

    @Schema(description = "ID de la tienda", example = "1")
    private Long idTienda;

    @Schema(description = "Fecha de inicio del periodo", example = "2026-06-01")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin del periodo", example = "2026-06-30")
    private LocalDate fechaFin;

    @Schema(description = "Ventas totales de la tienda en el periodo", example = "523000.0")
    private Double ventasPorTienda;

    @Schema(description = "Cantidad de pedidos entregados", example = "47")
    private Integer pedidosEntregados;

    @Schema(description = "Cantidad de SKUs con stock bajo", example = "8")
    private Integer stockBajo;

    @Schema(description = "Indice de rendimiento operativo (0.0 a 1.0)", example = "0.85")
    private Double rendimientoOperativo;
}
