package com.ecomarket.reportes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO de salida para el reporte de ventas generado.
 */
@Getter
@Setter
@Schema(description = "Resultado del reporte de ventas de una tienda en un periodo")
public class ReporteVentasDTO {

    @Schema(description = "ID de la tienda", example = "1")
    private Long idTienda;

    @Schema(description = "Fecha de inicio del periodo", example = "2026-06-01")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin del periodo", example = "2026-06-30")
    private LocalDate fechaFin;

    @Schema(description = "Ventas totales en CLP", example = "523000.0")
    private Double ventasTotales;

    @Schema(description = "Cantidad de transacciones (ventas)", example = "82")
    private Integer totalTransacciones;

    @Schema(description = "Cantidad de unidades vendidas", example = "315")
    private Integer productosVendidos;
}
