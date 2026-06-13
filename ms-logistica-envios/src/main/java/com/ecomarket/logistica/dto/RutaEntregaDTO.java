package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoRuta;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada/salida para ruta de entrega.
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos de una ruta de entrega")
public class RutaEntregaDTO {

    @Schema(description = "ID de la ruta", example = "1")
    private Long id;

    @Schema(description = "Estado de la ruta", example = "PLANIFICADA", allowableValues = {"PLANIFICADA", "EN_CURSO", "COMPLETADA", "CANCELADA"})
    private EstadoRuta estado;
}
