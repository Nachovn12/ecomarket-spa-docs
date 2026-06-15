package com.ecomarket.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de salida para exponer datos de resena sin exponer la entidad JPA.
 */
@Getter
@Setter
@Schema(description = "Datos de salida de una resena de producto")
public class ResenaResponseDTO {

    @Schema(description = "Identificador unico de la resena", example = "1")
    private Long idResena;

    @Schema(description = "ID del cliente que escribio la resena", example = "10")
    private Long idCliente;

    @Schema(description = "ID del producto resenado", example = "5")
    private Long idProducto;

    @Schema(description = "Nombre del producto resenado", example = "Bolsa biodegradable mediana")
    private String nombreProducto;

    @Schema(description = "Calificacion de la resena (1 a 5)", example = "5", minimum = "1", maximum = "5")
    private Integer calificacion;

    @Schema(description = "Comentario de la resena", example = "Excelente producto")
    private String comentario;

    @Schema(description = "Estado de moderacion de la resena", example = "PUBLICADA", allowableValues = {"PUBLICADA", "OCULTA", "PENDIENTE"})
    private String estado;

    @Schema(description = "Fecha y hora de creacion de la resena", example = "2026-05-15T12:00:00")
    private LocalDateTime fechaCreacion;
}
