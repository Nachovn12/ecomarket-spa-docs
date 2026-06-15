package com.ecomarket.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Schema(description = "Datos para crear o actualizar una tienda")
public class TiendaRequestDTO {

    @NotBlank(message = "El nombre de la tienda es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar 120 caracteres")
    @Schema(description = "Nombre de la tienda", example = "EcoMarket Santiago Centro", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 80, message = "La ciudad no puede superar 80 caracteres")
    @Schema(description = "Ciudad de la tienda", example = "Santiago", maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String ciudad;

    @NotNull(message = "El horario de apertura es obligatorio")
    @Schema(description = "Hora de apertura", example = "09:00:00", type = "string", format = "time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime horarioApertura;

    @NotNull(message = "El horario de cierre es obligatorio")
    @Schema(description = "Hora de cierre", example = "20:00:00", type = "string", format = "time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime horarioCierre;

    @Size(max = 500, message = "Las politicas locales no pueden superar 500 caracteres")
    @Schema(description = "Politicas locales de la tienda", example = "No se aceptan devoluciones despues de 30 dias", maxLength = 500)
    private String politicasLocales;
}
