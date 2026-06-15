package com.ecomarket.reportes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de salida para el reporte de inventario generado.
 */
@Getter
@Setter
@Schema(description = "Resultado del reporte de inventario de una tienda")
public class ReporteInventarioDTO {

    @Schema(description = "ID de la tienda consultada", example = "1")
    private Long idTienda;

    @Schema(description = "Cantidad de productos disponibles", example = "85")
    private Integer productosDisponibles;

    @Schema(description = "Cantidad de productos con stock bajo", example = "10")
    private Integer productosBajoStock;

    @Schema(description = "Cantidad de productos sin stock", example = "3")
    private Integer productosSinStock;
}
