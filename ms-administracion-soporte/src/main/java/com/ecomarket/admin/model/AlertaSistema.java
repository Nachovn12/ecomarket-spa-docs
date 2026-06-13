package com.ecomarket.admin.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alertas_sistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad JPA que representa una alerta del sistema")
public class AlertaSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la alerta", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idAlerta;

    @Column(nullable = false, length = 80)
    @Schema(description = "Microservicio que origina la alerta", example = "ms-pedidos-ventas", maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String microservicio;

    @Column(nullable = false, length = 80)
    @Schema(description = "Tipo de alerta", example = "CAIDA_SERVICIO", maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoAlerta;

    @Column(nullable = false, length = 500)
    @Schema(description = "Descripcion de la alerta", example = "El microservicio no responde desde las 10:00", maxLength = 500, requiredMode = Schema.RequiredMode.REQUIRED)
    private String descripcion;

    @Column(nullable = false)
    @Schema(description = "Indica si fue resuelta", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean resuelta;

    @Column(nullable = false)
    @Schema(description = "Fecha de generacion", example = "2026-06-12T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaGeneracion;

    @Schema(description = "Fecha de resolucion si aplica", example = "2026-06-12T11:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaResolucion;
}
