package com.ecomarket.reportes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO de entrada para filtrar la generacion de reportes por tienda y rango de fechas.
 * Incluye validaciones bean validation (JSR 380).
 */
@Getter
@Setter
@Schema(description = "Filtro para la generacion de un reporte")
public class ReporteFiltroRequestDTO {

    @NotNull(message = "El ID de tienda es obligatorio")
    @Positive(message = "El ID de tienda debe ser positivo")
    @Schema(description = "ID de la tienda", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idTienda;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Schema(description = "Fecha de inicio del rango", example = "2026-06-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Schema(description = "Fecha de fin del rango", example = "2026-06-30", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaFin;
}
