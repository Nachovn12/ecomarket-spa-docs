package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para cambiar el estado de un Envío.
 */
@Getter
@Setter
@NoArgsConstructor
public class CambioEstadoRequestDTO {

    @NotNull(message = "El estado es obligatorio")
    private EstadoEnvio estado;

    @NotBlank(message = "El usuario que actualiza es obligatorio")
    private String actualizadoPor;

    private String ubicacion;
    private String observacion;
}
