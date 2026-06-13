package com.ecomarket.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos para registrar una metrica del sistema")
public class MetricaSistemaRequestDTO {

    @NotBlank(message = "El microservicio es obligatorio")
    @Size(max = 80, message = "El microservicio no puede superar 80 caracteres")
    @Schema(description = "Nombre del microservicio medido", example = "ms-pedidos-ventas", maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String microservicio;

    @NotNull(message = "La disponibilidad es obligatoria")
    @Schema(description = "Indica si el microservicio estaba disponible al medir", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean disponible;

    @NotNull(message = "El tiempo de respuesta es obligatorio")
    @Min(value = 0, message = "El tiempo de respuesta no puede ser negativo")
    @Schema(description = "Tiempo de respuesta en milisegundos", example = "120", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tiempoRespuestaMs;

    @NotNull(message = "Los errores detectados son obligatorios")
    @Min(value = 0, message = "Los errores detectados no pueden ser negativos")
    @Schema(description = "Cantidad de errores detectados", example = "0", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer erroresDetectados;
}
