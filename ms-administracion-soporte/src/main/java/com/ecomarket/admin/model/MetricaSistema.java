package com.ecomarket.admin.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "metricas_sistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad JPA que representa una metrica registrada del sistema")
public class MetricaSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la metrica", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idMetrica;

    @Column(nullable = false, length = 80)
    @Schema(description = "Nombre del microservicio medido", example = "ms-pedidos-ventas", maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String microservicio;

    @Column(nullable = false)
    @Schema(description = "Disponibilidad al medir", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean disponible;

    @Column(nullable = false)
    @Schema(description = "Tiempo de respuesta en milisegundos", example = "120", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tiempoRespuestaMs;

    @Column(nullable = false)
    @Schema(description = "Cantidad de errores detectados", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer erroresDetectados;

    @Column(nullable = false)
    @Schema(description = "Fecha de registro de la metrica", example = "2026-06-12T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaRegistro;
}
