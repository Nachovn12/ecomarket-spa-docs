package com.ecomarket.reportes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO de entrada para filtrar la generación de reportes por tienda y rango de fechas.
 * Incluye validaciones Bean Validation (JSR 380).
 */
@Getter
@Setter
public class ReporteFiltroRequestDTO {

    @NotNull(message = "El ID de tienda es obligatorio")
    @Positive(message = "El ID de tienda debe ser positivo")
    private Long idTienda;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;
}
