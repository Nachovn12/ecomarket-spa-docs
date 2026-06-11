package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO de entrada para crear o actualizar un Envío.
 * Incluye validaciones Bean Validation (JSR 380).
 */
@Getter
@Setter
@NoArgsConstructor
public class EnvioDTO {

    @Positive(message = "El ID debe ser positivo")
    private Long id;

    @NotNull(message = "El ID del pedido es obligatorio")
    @Positive(message = "El ID del pedido debe ser positivo")
    private Long idPedido;

    @NotBlank(message = "El origen es obligatorio")
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    private String destino;

    private EstadoEnvio estado;
    private String ubicacionActual;
    private String observacion;
    private String motivoIncidencia;

    @NotNull(message = "La fecha estimada de entrega es obligatoria")
    @FutureOrPresent(message = "La fecha estimada de entrega no puede estar en el pasado")
    private LocalDateTime fechaEstimadaEntrega;

    @Positive(message = "El ID del proveedor debe ser positivo")
    private Long proveedorId;

    @Positive(message = "El ID de la ruta debe ser positivo")
    private Long rutaEntregaId;
}
