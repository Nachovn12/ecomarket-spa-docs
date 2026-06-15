package com.ecomarket.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "Datos de salida de un respaldo")
public class RespaldoDatosResponseDTO {

    @Schema(description = "ID del respaldo", example = "1")
    private Long idRespaldo;

    @Schema(description = "Origen de datos", example = "bd_pedidos")
    private String origenDatos;

    @Schema(description = "Frecuencia", example = "DIARIA")
    private String frecuencia;

    @Schema(description = "Responsable", example = "admin1")
    private String responsable;

    @Schema(description = "Estado del respaldo", example = "EXITOSO", allowableValues = {"PROGRAMADO", "EN_EJECUCION", "EXITOSO", "FALLIDO"})
    private String estado;

    @Schema(description = "Detalle del resultado de la ejecucion", example = "Respaldo completado sin errores")
    private String resultado;

    @Schema(description = "Fecha programada", example = "2026-06-20T02:00:00")
    private LocalDateTime fechaProgramada;

    @Schema(description = "Fecha de ejecucion", example = "2026-06-20T02:05:00")
    private LocalDateTime fechaEjecucion;

    @Schema(description = "Fecha de la ultima restauracion si aplica", example = "2026-06-21T10:00:00")
    private LocalDateTime fechaRestauracion;
}
