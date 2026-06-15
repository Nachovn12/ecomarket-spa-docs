package com.ecomarket.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "Datos de salida de una alerta del sistema")
public class AlertaSistemaResponseDTO {

    @Schema(description = "ID de la alerta", example = "1")
    private Long idAlerta;

    @Schema(description = "Microservicio origen", example = "ms-pedidos-ventas")
    private String microservicio;

    @Schema(description = "Tipo de alerta", example = "CAIDA_SERVICIO")
    private String tipoAlerta;

    @Schema(description = "Descripcion", example = "El microservicio no responde desde las 10:00")
    private String descripcion;

    @Schema(description = "Indica si fue resuelta", example = "false")
    private Boolean resuelta;

    @Schema(description = "Fecha de generacion", example = "2026-06-12T10:00:00")
    private LocalDateTime fechaGeneracion;

    @Schema(description = "Fecha de resolucion si aplica", example = "2026-06-12T11:30:00")
    private LocalDateTime fechaResolucion;
}
