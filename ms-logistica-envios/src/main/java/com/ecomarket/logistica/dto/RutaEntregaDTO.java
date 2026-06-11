package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoRuta;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada/salida para Ruta de Entrega.
 */
@Getter
@Setter
@NoArgsConstructor
public class RutaEntregaDTO {

    private Long id;
    private EstadoRuta estado;
}
