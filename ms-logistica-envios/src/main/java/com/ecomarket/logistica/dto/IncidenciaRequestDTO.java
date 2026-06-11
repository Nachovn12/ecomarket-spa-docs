package com.ecomarket.logistica.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para registrar una incidencia en un Envío.
 */
@Getter
@Setter
@NoArgsConstructor
public class IncidenciaRequestDTO {

    @NotBlank(message = "El motivo de la incidencia es obligatorio")
    private String motivoIncidencia;

    @NotBlank(message = "El usuario que reporta es obligatorio")
    private String actualizadoPor;

    private String observacion;
}
