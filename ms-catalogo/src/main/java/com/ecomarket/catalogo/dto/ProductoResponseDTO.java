package com.ecomarket.catalogo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de salida para exponer datos de Producto sin exponer la entidad JPA directamente.
 */
@Getter
@Setter
public class ProductoResponseDTO {

    private Long idProducto;
    private String sku;
    private String nombre;
    private Double precio;
    private String descripcion;
    private String descripcionEcologica;
    private String estado;
    private Long idCategoria;
    private String nombreCategoria;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
