package com.ecomarket.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos para generar una alerta del sistema")
public class AlertaSistemaRequestDTO {

    @NotBlank(message = "El microservicio es obligatorio")
    @Size(max = 80, message = "El microservicio no puede superar 80 caracteres")
    @Schema(description = "Microservicio que origina la alerta", example = "ms-pedidos-ventas", maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String microservicio;

    @NotBlank(message = "El tipo de alerta es obligatorio")
    @Size(max = 80, message = "El tipo de alerta no puede superar 80 caracteres")
    @Schema(description = "Tipo de alerta", example = "CAIDA_SERVICIO", maxLength = 80, allowableValues = {"CAIDA_SERVICIO", "LATENCIA_ALTA", "ERROR_5XX", "OTRO"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoAlerta;

    @NotBlank(message = "La descripcion de la alerta es obligatoria")
    @Size(max = 500, message = "La descripcion no puede superar 500 caracteres")
    @Schema(description = "Descripcion de la alerta", example = "El microservicio no responde desde las 10:00", maxLength = 500, requiredMode = Schema.RequiredMode.REQUIRED)
    private String descripcion;
}
