package com.ecomarket.logistica.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import com.ecomarket.logistica.model.enums.EstadoRuta;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rutas_entrega")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Entidad JPA que representa una ruta de entrega planificada")
public class RutaEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la ruta", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Estado de la ruta", example = "PLANIFICADA", allowableValues = {"PLANIFICADA", "OPTIMIZADA", "EN_CURSO", "FINALIZADA"})
    private EstadoRuta estado = EstadoRuta.PLANIFICADA;

    @Schema(description = "Fecha de creacion de la ruta", example = "2026-06-15T08:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoRuta.PLANIFICADA;
        }
    }
}
