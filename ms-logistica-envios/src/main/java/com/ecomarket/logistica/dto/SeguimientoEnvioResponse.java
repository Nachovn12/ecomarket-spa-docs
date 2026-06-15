package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Datos de salida de un seguimiento de envio")
public class SeguimientoEnvioResponse {

    @Schema(description = "ID del seguimiento", example = "1")
    private Long id;

    @Schema(description = "ID del envio asociado", example = "1")
    private Long idEnvio;

    @Schema(description = "Estado registrado en este seguimiento", example = "EN_CAMINO",
            allowableValues = {"PREPARADO", "EN_CAMINO", "ENTREGADO", "CON_INCIDENCIA"})
    private EstadoEnvio estado;

    @Schema(description = "Ubicacion al momento del registro", example = "Providencia, Santiago")
    private String ubicacion;

    @Schema(description = "Observacion del seguimiento", example = "Salida del centro de distribucion")
    private String observacion;

    @Schema(description = "Persona que registro el cambio", example = "operador1@ecomarket.cl")
    private String actualizadoPor;

    @Schema(description = "Fecha y hora del registro", example = "2026-06-16T09:00:00")
    private LocalDateTime fecha;
}