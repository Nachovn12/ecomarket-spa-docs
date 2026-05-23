package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import java.time.LocalDateTime;

public class EnvioDTO {
    private Long id;
    private Long idPedido;
    private String origen;
    private String destino;
    private EstadoEnvio estado;
    private String ubicacionActual;
    private String observacion;
    private String motivoIncidencia;
    private LocalDateTime fechaEstimadaEntrega;
    private Long proveedorId;
    private Long rutaEntregaId;

    public EnvioDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }
    public String getUbicacionActual() { return ubicacionActual; }
    public void setUbicacionActual(String ubicacionActual) { this.ubicacionActual = ubicacionActual; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public String getMotivoIncidencia() { return motivoIncidencia; }
    public void setMotivoIncidencia(String motivoIncidencia) { this.motivoIncidencia = motivoIncidencia; }
    public LocalDateTime getFechaEstimadaEntrega() { return fechaEstimadaEntrega; }
    public void setFechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) { this.fechaEstimadaEntrega = fechaEstimadaEntrega; }
    public Long getProveedorId() { return proveedorId; }
    public void setProveedorId(Long proveedorId) { this.proveedorId = proveedorId; }
    public Long getRutaEntregaId() { return rutaEntregaId; }
    public void setRutaEntregaId(Long rutaEntregaId) { this.rutaEntregaId = rutaEntregaId; }
}