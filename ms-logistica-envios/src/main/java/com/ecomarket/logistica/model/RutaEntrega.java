package com.ecomarket.logistica.model;

import com.ecomarket.logistica.model.enums.EstadoRuta;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rutas_entrega")
public class RutaEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRuta estado = EstadoRuta.PLANIFICADA;

    private LocalDateTime fechaCreacion;

    public RutaEntrega() {}

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoRuta.PLANIFICADA;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EstadoRuta getEstado() { return estado; }
    public void setEstado(EstadoRuta estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}