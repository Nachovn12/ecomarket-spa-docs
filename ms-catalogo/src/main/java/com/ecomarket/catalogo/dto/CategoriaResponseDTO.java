package com.ecomarket.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de salida para exponer datos de categoria sin exponer la entidad JPA.
 */
@Getter
@Setter
@Schema(description = "Datos de salida de una categoria del catalogo")
public class CategoriaResponseDTO {

    @Schema(description = "Identificador unico de la categoria", example = "1")
    private Long idCategoria;

    @Schema(description = "Nombre de la categoria", example = "Productos biodegradables")
    private String nombre;

    @Schema(description = "Descripcion de la categoria", example = "Productos fabricados con materiales biodegradables")
    private String descripcion;

    @Schema(description = "Estado actual de la categoria", example = "ACTIVA", allowableValues = {"ACTIVA", "INACTIVA"})
    private String estado;
}
