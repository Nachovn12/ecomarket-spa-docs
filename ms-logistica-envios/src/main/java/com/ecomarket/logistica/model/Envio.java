package com.ecomarket.logistica.model;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del pedido es obligatorio")
    @Column(nullable = false)
    private Long idPedido;

    @NotBlank(message = "El origen es obligatorio")
    @Column(nullable = false)
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    @Column(nullable = false)
    private String destino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estado = EstadoEnvio.PREPARADO;

    private String ubicacionActual;
    private String observacion;
    private String motivoIncidencia;

    private LocalDateTime fechaCreacion;

    @NotNull(message = "La fecha estimada de entrega es obligatoria")
    private LocalDateTime fechaEstimadaEntrega;

    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "ruta_entrega_id")
    private RutaEntrega rutaEntrega;

    public Envio() {}

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoEnvio.PREPARADO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

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
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaEstimadaEntrega() { return fechaEstimadaEntrega; }
    public void setFechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) { this.fechaEstimadaEntrega = fechaEstimadaEntrega; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
    public RutaEntrega getRutaEntrega() { return rutaEntrega; }
    public void setRutaEntrega(RutaEntrega rutaEntrega) { this.rutaEntrega = rutaEntrega; }
}