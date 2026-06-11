package com.ecomarket.logistica.model;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Entidad JPA de Seguimiento de Envío.
 * Registra cada cambio de estado de un envío como historial trazable.
 * Las validaciones de entrada se gestionan en CambioEstadoRequestDTO.
 */
@Entity
@Table(name = "seguimientos_envio")
@Getter
@Setter
@NoArgsConstructor
public class SeguimientoEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "envio_id", nullable = false)
    private Envio envio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoEnvio estado;

    private String ubicacion;
    private String observacion;

    @Column(nullable = false, length = 120)
    private String actualizadoPor;

    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }
}
