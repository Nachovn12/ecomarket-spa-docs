package com.ecomarket.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados posibles de un ticket de soporte", allowableValues = {"ABIERTO", "EN_ATENCION", "RESUELTO", "CERRADO"})
public enum EstadoTicket {
    ABIERTO,
    EN_ATENCION,
    RESUELTO,
    CERRADO
}
