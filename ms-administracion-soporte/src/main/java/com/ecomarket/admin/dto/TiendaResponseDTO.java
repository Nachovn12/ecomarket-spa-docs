package com.ecomarket.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@Schema(description = "Datos de salida de una tienda")
public class TiendaResponseDTO {

    @Schema(description = "ID de la tienda", example = "1")
    private Long idTienda;

    @Schema(description = "Nombre de la tienda", example = "EcoMarket Santiago Centro")
    private String nombre;

    @Schema(description = "Ciudad", example = "Santiago")
    private String ciudad;

    @Schema(description = "Horario de apertura", example = "09:00:00")
    private LocalTime horarioApertura;

    @Schema(description = "Horario de cierre", example = "20:00:00")
    private LocalTime horarioCierre;

    @Schema(description = "Politicas locales", example = "No se aceptan devoluciones despues de 30 dias")
    private String politicasLocales;

    @Schema(description = "Indica si la tienda esta activa", example = "true")
    private Boolean activa;

    @Schema(description = "Fecha de creacion", example = "2026-01-15T10:00:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de ultima actualizacion", example = "2026-06-01T14:00:00")
    private LocalDateTime fechaActualizacion;
}
