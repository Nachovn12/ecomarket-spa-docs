package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoRuta;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Datos de salida de una ruta de entrega")
public class RutaEntregaResponse {

    @Schema(description = "ID de la ruta", example = "1")
    private Long id;

    @Schema(description = "Estado de la ruta", example = "PLANIFICADA",
            allowableValues = {"PLANIFICADA", "OPTIMIZADA", "EN_CURSO", "FINALIZADA"})
    private EstadoRuta estado;

    @Schema(description = "Fecha de creacion", example = "2026-06-15T08:00:00")
    private LocalDateTime fechaCreacion;
}