package com.ecomarket.reportes.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO de salida para exponer datos de un IndicadorKPI sin exponer la entidad JPA.
 */
@Getter
@Setter
public class IndicadorKPIResponseDTO {

    private Long id;
    private String tipo;
    private Double valor;
    private String descripcion;
    private LocalDateTime fechaCalculo;
}
