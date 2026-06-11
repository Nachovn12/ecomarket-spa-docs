package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoRuta;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para cambiar el estado de una Ruta de Entrega.
 */
@Getter
@Setter
@NoArgsConstructor
public class CambioEstadoRutaRequestDTO {

    @NotNull(message = "El nuevo estado de la ruta es obligatorio")
    private EstadoRuta estado;
}
