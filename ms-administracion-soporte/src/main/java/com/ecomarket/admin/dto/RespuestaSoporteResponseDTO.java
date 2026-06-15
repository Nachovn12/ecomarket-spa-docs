package com.ecomarket.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "Datos de salida de una respuesta de soporte")
public class RespuestaSoporteResponseDTO {

    @Schema(description = "ID de la respuesta", example = "1")
    private Long idRespuesta;

    @Schema(description = "ID del ticket asociado", example = "1")
    private Long idTicket;

    @Schema(description = "Mensaje", example = "Hemos verificado tu caso y procederemos a...")
    private String mensaje;

    @Schema(description = "Responsable de la respuesta", example = "operador1")
    private String respondidoPor;

    @Schema(description = "Fecha de la respuesta", example = "2026-06-11T10:30:00")
    private LocalDateTime fechaRespuesta;
}
