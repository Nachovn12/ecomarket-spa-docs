package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO de salida para exponer datos de Seguimiento de Envío sin exponer la entidad JPA.
 */
@Getter
@Setter
@NoArgsConstructor
public class SeguimientoEnvioDTO {

    private Long id;
    private Long envioId;
    private EstadoEnvio estado;
    private String ubicacion;
    private String observacion;
    private String actualizadoPor;
    private LocalDateTime fechaRegistro;
}
