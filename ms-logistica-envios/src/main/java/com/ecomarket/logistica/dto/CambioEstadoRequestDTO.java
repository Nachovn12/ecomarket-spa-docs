package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CambioEstadoRequestDTO {
    @NotNull(message = "El estado es obligatorio")
    private EstadoEnvio estado;
    
    @NotBlank(message = "El usuario que actualiza es obligatorio")
    private String actualizadoPor;
    
    private String ubicacion;
    private String observacion;

    public CambioEstadoRequestDTO() {}

    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }
    public String getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(String actualizadoPor) { this.actualizadoPor = actualizadoPor; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}