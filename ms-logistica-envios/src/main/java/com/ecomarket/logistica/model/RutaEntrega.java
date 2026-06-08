package com.ecomarket.logistica.model;


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
public class RutaEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRuta estado = EstadoRuta.PLANIFICADA;

    private LocalDateTime fechaCreacion;
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoRuta.PLANIFICADA;
        }
    }
}