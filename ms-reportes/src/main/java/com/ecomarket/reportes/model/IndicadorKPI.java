package com.ecomarket.reportes.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA de Indicador KPI.
 * Almacena los valores calculados por tipo de KPI para consulta en reportes.
 * Las validaciones de entrada se gestionan directamente en el controller con @Valid sobre la entidad.
 */
@Entity
@Table(name = "indicadores_kpi")
@Getter
@Setter
@NoArgsConstructor
public class IndicadorKPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoKPI tipo;

    @Column(nullable = false)
    private Double valor;

    @Column(length = 255)
    private String descripcion;

    private LocalDateTime fechaCalculo;

    @PrePersist
    public void prePersist() {
        this.fechaCalculo = LocalDateTime.now();
    }
}
