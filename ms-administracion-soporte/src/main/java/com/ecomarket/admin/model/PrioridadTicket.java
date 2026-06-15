package com.ecomarket.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Prioridades posibles de un ticket de soporte", allowableValues = {"BAJA", "MEDIA", "ALTA"})
public enum PrioridadTicket {
    BAJA,
    MEDIA,
    ALTA
}
