package com.ecomarket.catalogo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para crear o actualizar un Producto en el catálogo.
 * Separa la validación de la entidad JPA.
 */
@Getter
@Setter
public class ProductoRequestDTO {

    @NotBlank(message = "El SKU es obligatorio")
    private String sku;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double precio;

    private String descripcion;

    private String descripcionEcologica;

    private String estado;

    /** ID de la categoría a la que pertenece el producto. */
    private Long idCategoria;
}
