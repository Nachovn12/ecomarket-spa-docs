package com.ecomarket.catalogo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de salida para exponer datos de Reseña sin exponer la entidad JPA.
 */
@Getter
@Setter
public class ResenaResponseDTO {

    private Long idResena;
    private Long idCliente;
    private Long idProducto;
    private String nombreProducto;
    private Integer calificacion;
    private String comentario;
    private String estado;
    private LocalDateTime fechaCreacion;
}
