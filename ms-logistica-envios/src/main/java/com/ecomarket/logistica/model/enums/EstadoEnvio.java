package com.ecomarket.logistica.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados posibles de un envio", allowableValues = {"PREPARADO", "EN_CAMINO", "ENTREGADO", "CON_INCIDENCIA"})
public enum EstadoEnvio {
    PREPARADO,
    EN_CAMINO,
    ENTREGADO,
    CON_INCIDENCIA
}
