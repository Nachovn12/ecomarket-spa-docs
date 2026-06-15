package com.ecomarket.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para crear o actualizar un producto en el catalogo.
 * Separa la validacion de la entidad JPA.
 */
@Getter
@Setter
@Schema(description = "Datos de entrada para crear o actualizar un producto del catalogo")
public class ProductoRequestDTO {

    @NotBlank(message = "El SKU es obligatorio")
    @Schema(description = "Codigo SKU unico del producto", example = "ECO-001", maxLength = 50, requiredMode = Schema.RequiredMode.REQUIRED)
    private String sku;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del producto", example = "Bolsa biodegradable mediana", maxLength = 200, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Schema(description = "Precio unitario del producto en CLP", example = "1990.0", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double precio;

    @Schema(description = "Descripcion general del producto", example = "Bolsa reutilizable hecha de almidon de maiz", maxLength = 1000)
    private String descripcion;

    @Schema(description = "Descripcion del atributo ecologico (biodegradable, compostable, reciclable, etc.)",
            example = "100% biodegradable en 180 dias", maxLength = 1000)
    private String descripcionEcologica;

    @Schema(description = "Estado del producto. Si se omite, el service asigna PUBLICADO por defecto",
            example = "PUBLICADO", allowableValues = {"PUBLICADO", "PAUSADO", "AGOTADO", "DESCONTINUADO"})
    private String estado;

    /** ID de la categoria a la que pertenece el producto. */
    @Schema(description = "Identificador de la categoria a la que pertenece el producto", example = "2")
    private Long idCategoria;
}
