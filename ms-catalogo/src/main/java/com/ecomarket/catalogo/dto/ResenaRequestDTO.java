package com.ecomarket.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para crear una resena de producto.
 */
@Getter
@Setter
@Schema(description = "Datos de entrada para registrar una resena sobre un producto")
public class ResenaRequestDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    @Schema(description = "Identificador del cliente que emite la resena (provisto por MS Usuarios)", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idCliente;

    @NotNull(message = "El ID del producto es obligatorio")
    @Schema(description = "Identificador del producto resenado", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idProducto;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion minima es 1")
    @Max(value = 5, message = "La calificacion maxima es 5")
    @Schema(description = "Calificacion del producto entre 1 y 5", example = "5", minimum = "1", maximum = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer calificacion;

    @Size(max = 1000, message = "El comentario no puede superar 1000 caracteres")
    @Schema(description = "Comentario libre de la resena", example = "Excelente producto, se descompuso en 90 dias.", maxLength = 1000)
    private String comentario;
}
