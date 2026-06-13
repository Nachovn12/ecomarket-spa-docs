package com.ecomarket.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para crear o actualizar una categoria.
 */
@Getter
@Setter
@Schema(description = "Datos de entrada para crear o actualizar una categoria del catalogo")
public class CategoriaRequestDTO {

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Schema(description = "Nombre unico de la categoria", example = "Productos biodegradables", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Size(max = 500, message = "La descripcion no puede superar 500 caracteres")
    @Schema(description = "Descripcion opcional de la categoria", example = "Productos fabricados con materiales biodegradables", maxLength = 500)
    private String descripcion;

    @Schema(description = "Estado de la categoria. Si se omite, el service asigna ACTIVA por defecto", example = "ACTIVA", allowableValues = {"ACTIVA", "INACTIVA"})
    private String estado;
}
