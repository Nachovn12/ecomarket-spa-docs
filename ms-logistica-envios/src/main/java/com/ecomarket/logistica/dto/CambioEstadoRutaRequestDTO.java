package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoRuta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para cambiar el estado de una ruta de entrega.
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Solicitud para cambiar el estado de una ruta de entrega")
public class CambioEstadoRutaRequestDTO {

    @NotNull(message = "El nuevo estado de la ruta es obligatorio")
    @Schema(description = "Nuevo estado de la ruta", example = "EN_CURSO", requiredMode = Schema.RequiredMode.REQUIRED)
    private EstadoRuta estado;
}
