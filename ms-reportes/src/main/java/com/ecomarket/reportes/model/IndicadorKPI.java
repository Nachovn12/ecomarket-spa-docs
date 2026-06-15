package com.ecomarket.reportes.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA de indicador kpi.
 * Almacena los valores calculados por tipo de kpi para consulta en reportes.
 * Las validaciones de entrada se gestionan directamente en el controller con @Valid sobre la entidad.
 */
@Entity
@Table(name = "indicadores_kpi")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Entidad JPA que representa un indicador KPI calculado o registrado")
public class IndicadorKPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del KPI", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Schema(description = "Tipo de KPI", example = "VENTAS_TOTALES", allowableValues = {"VENTAS_TOTALES", "STOCK_BAJO", "ROTACION_INVENTARIO", "PEDIDOS_ENTREGADOS", "RENDIMIENTO_TIENDA"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoKPI tipo;

    @Column(nullable = false)
    @Schema(description = "Valor numerico del KPI", example = "153200.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double valor;

    @Column(length = 255)
    @Schema(description = "Descripcion del KPI", example = "Total de ventas del mes", maxLength = 255)
    private String descripcion;

    @Schema(description = "Fecha de calculo del KPI", example = "2026-06-12T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCalculo;

    @PrePersist
    public void prePersist() {
        this.fechaCalculo = LocalDateTime.now();
    }
}
