package com.ecomarket.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de salida para exponer datos de producto sin exponer la entidad JPA directamente.
 */
@Getter
@Setter
@Schema(description = "Datos de salida de un producto del catalogo")
public class ProductoResponseDTO {

    @Schema(description = "Identificador unico del producto", example = "1")
    private Long idProducto;

    @Schema(description = "Codigo SKU del producto", example = "ECO-001")
    private String sku;

    @Schema(description = "Nombre del producto", example = "Bolsa biodegradable mediana")
    private String nombre;

    @Schema(description = "Precio unitario en CLP", example = "1990.0")
    private Double precio;

    @Schema(description = "Descripcion general del producto", example = "Bolsa reutilizable hecha de almidon de maiz")
    private String descripcion;

    @Schema(description = "Descripcion del atributo ecologico", example = "100% biodegradable en 180 dias")
    private String descripcionEcologica;

    @Schema(description = "Estado del producto", example = "PUBLICADO", allowableValues = {"PUBLICADO", "PAUSADO", "AGOTADO", "DESCONTINUADO"})
    private String estado;

    @Schema(description = "ID de la categoria asociada", example = "2")
    private Long idCategoria;

    @Schema(description = "Nombre de la categoria asociada", example = "Productos biodegradables")
    private String nombreCategoria;

    @Schema(description = "Fecha y hora de creacion del producto", example = "2026-05-01T10:15:30")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha y hora de la ultima actualizacion", example = "2026-06-10T14:22:05")
    private LocalDateTime fechaActualizacion;
}
