package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para cambiar el estado de un envio.
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Solicitud para cambiar el estado de un envio")
public class CambioEstadoRequestDTO {

    @NotNull(message = "El estado es obligatorio")
    @Schema(description = "Nuevo estado del envio", example = "EN_TRANSITO", requiredMode = Schema.RequiredMode.REQUIRED)
    private EstadoEnvio estado;

    @NotBlank(message = "El usuario que actualiza es obligatorio")
    @Schema(description = "Usuario que realiza el cambio", example = "operador1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String actualizadoPor;

    @Schema(description = "Ubicacion actual del envio al momento del cambio", example = "Centro de distribucion Santiago")
    private String ubicacion;

    @Schema(description = "Observaciones sobre el cambio de estado", example = "Salida conforme a horario")
    private String observacion;
}
