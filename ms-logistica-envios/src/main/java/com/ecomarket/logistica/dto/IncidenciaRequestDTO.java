package com.ecomarket.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para registrar una incidencia en un envio.
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Solicitud para registrar una incidencia en un envio")
public class IncidenciaRequestDTO {

    @NotBlank(message = "El motivo de la incidencia es obligatorio")
    @Schema(description = "Motivo de la incidencia", example = "Direccion incorrecta", requiredMode = Schema.RequiredMode.REQUIRED)
    private String motivoIncidencia;

    @NotBlank(message = "El usuario que reporta es obligatorio")
    @Schema(description = "Usuario que reporta la incidencia", example = "repartidor1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String actualizadoPor;

    @Schema(description = "Observaciones adicionales", example = "Se intento contactar al cliente sin exito")
    private String observacion;
}
