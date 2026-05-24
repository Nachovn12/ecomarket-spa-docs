package com.ecomarket.logistica.dto;

import jakarta.validation.constraints.NotBlank;

public class IncidenciaRequestDTO {
    @NotBlank(message = "El motivo de la incidencia es obligatorio")
    private String motivoIncidencia;
    
    @NotBlank(message = "El usuario que reporta es obligatorio")
    private String actualizadoPor;
    
    private String observacion;

    public IncidenciaRequestDTO() {}

    public String getMotivoIncidencia() { return motivoIncidencia; }
    public void setMotivoIncidencia(String motivoIncidencia) { this.motivoIncidencia = motivoIncidencia; }
    public String getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(String actualizadoPor) { this.actualizadoPor = actualizadoPor; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}