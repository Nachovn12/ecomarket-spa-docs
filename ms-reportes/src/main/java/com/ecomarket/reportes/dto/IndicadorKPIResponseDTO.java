package com.ecomarket.reportes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO de salida para exponer datos de un indicadorkpi sin exponer la entidad JPA.
 */
@Getter
@Setter
@Schema(description = "Datos de salida de un indicador KPI")
public class IndicadorKPIResponseDTO {

    @Schema(description = "ID del KPI", example = "1")
    private Long id;

    @Schema(description = "Tipo de KPI", example = "VENTAS", allowableValues = {"VENTAS", "INVENTARIO", "RENDIMIENTO"})
    private String tipo;

    @Schema(description = "Valor numerico del KPI", example = "153200.5")
    private Double valor;

    @Schema(description = "Descripcion del KPI", example = "Total de ventas del mes")
    private String descripcion;

    @Schema(description = "Fecha en que se calculo el KPI", example = "2026-06-12T10:00:00")
    private LocalDateTime fechaCalculo;
}
