package com.ecomarket.catalogo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO de salida para exponer datos de Categoría sin exponer la entidad JPA.
 */
@Getter
@Setter
public class CategoriaResponseDTO {

    private Long idCategoria;
    private String nombre;
    private String descripcion;
    private String estado;
}
