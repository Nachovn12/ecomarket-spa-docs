package com.ecomarket.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "Datos de salida de una metrica del sistema")
public class MetricaSistemaResponseDTO {

    @Schema(description = "ID de la metrica", example = "1")
    private Long idMetrica;

    @Schema(description = "Microservicio medido", example = "ms-pedidos-ventas")
    private String microservicio;

    @Schema(description = "Disponibilidad al medir", example = "true")
    private Boolean disponible;

    @Schema(description = "Tiempo de respuesta en ms", example = "120")
    private Long tiempoRespuestaMs;

    @Schema(description = "Errores detectados", example = "0")
    private Integer erroresDetectados;

    @Schema(description = "Fecha de registro", example = "2026-06-12T10:00:00")
    private LocalDateTime fechaRegistro;
}
