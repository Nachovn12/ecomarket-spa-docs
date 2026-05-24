package com.ecomarket.logistica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "recepciones_mercaderia")
public class RecepcionMercaderia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de solicitud es obligatorio")
    private Long idSolicitudReabastecimiento;

    @NotNull(message = "La cantidad recibida es obligatoria")
    private Integer cantidadRecibida;

    private String observacion;
    private LocalDateTime fechaRecepcion;

    public RecepcionMercaderia() {}

    @PrePersist
    protected void onCreate() {
        this.fechaRecepcion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdSolicitudReabastecimiento() { return idSolicitudReabastecimiento; }
    public void setIdSolicitudReabastecimiento(Long idSolicitudReabastecimiento) { this.idSolicitudReabastecimiento = idSolicitudReabastecimiento; }
    public Integer getCantidadRecibida() { return cantidadRecibida; }
    public void setCantidadRecibida(Integer cantidadRecibida) { this.cantidadRecibida = cantidadRecibida; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public LocalDateTime getFechaRecepcion() { return fechaRecepcion; }
    public void setFechaRecepcion(LocalDateTime fechaRecepcion) { this.fechaRecepcion = fechaRecepcion; }
}