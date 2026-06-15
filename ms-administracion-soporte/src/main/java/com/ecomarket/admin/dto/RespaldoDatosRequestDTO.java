package com.ecomarket.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Datos para programar un respaldo de datos")
public class RespaldoDatosRequestDTO {

    @NotBlank(message = "El origen de datos es obligatorio")
    @Size(max = 120, message = "El origen de datos no puede superar 120 caracteres")
    @Schema(description = "Origen de datos a respaldar", example = "bd_pedidos", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String origenDatos;

    @NotBlank(message = "La frecuencia es obligatoria")
    @Size(max = 80, message = "La frecuencia no puede superar 80 caracteres")
    @Schema(description = "Frecuencia del respaldo", example = "DIARIA", maxLength = 80, allowableValues = {"DIARIA", "SEMANAL", "MENSUAL"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String frecuencia;

    @NotBlank(message = "El responsable es obligatorio")
    @Size(max = 120, message = "El responsable no puede superar 120 caracteres")
    @Schema(description = "Responsable del respaldo", example = "admin1", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String responsable;

    @NotNull(message = "La fecha programada es obligatoria")
    @FutureOrPresent(message = "La fecha programada debe ser actual o futura")
    @Schema(description = "Fecha y hora programada para el respaldo", example = "2026-06-20T02:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime fechaProgramada;
}
