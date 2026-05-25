package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import java.time.LocalDateTime;

public class SeguimientoEnvioDTO {
    private Long id;
    private Long envioId;
    private EstadoEnvio estado;
    private String ubicacion;
    private String observacion;
    private String actualizadoPor;
    private LocalDateTime fechaRegistro;

    public SeguimientoEnvioDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEnvioId() { return envioId; }
    public void setEnvioId(Long envioId) { this.envioId = envioId; }
    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public String getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(String actualizadoPor) { this.actualizadoPor = actualizadoPor; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}