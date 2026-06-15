package com.ecomarket.logistica.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados posibles de una ruta de entrega", allowableValues = {"PLANIFICADA", "OPTIMIZADA", "EN_CURSO", "FINALIZADA"})
public enum EstadoRuta {
    PLANIFICADA,
    OPTIMIZADA,
    EN_CURSO,
    FINALIZADA
}
