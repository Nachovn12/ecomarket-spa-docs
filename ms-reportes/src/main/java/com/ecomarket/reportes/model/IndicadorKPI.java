package com.ecomarket.reportes.model;


import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "indicadores_kpi")
@Getter
@Setter
@NoArgsConstructor
public class IndicadorKPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El tipo de KPI es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoKPI tipo;

    @NotNull(message = "El valor del KPI es obligatorio")
    @PositiveOrZero(message = "El valor del KPI no puede ser negativo")
    @Column(nullable = false)
    private Double valor;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    private LocalDateTime fechaCalculo;

    @PrePersist
    public void prePersist() {
        this.fechaCalculo = LocalDateTime.now();
    }
}