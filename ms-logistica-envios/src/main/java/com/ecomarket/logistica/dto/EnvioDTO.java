package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO de entrada para crear o actualizar un envio.
 * Incluye validaciones bean validation (JSR 380).
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos para crear o actualizar un envio")
public class EnvioDTO {

    @Positive(message = "El ID debe ser positivo")
    @Schema(description = "ID del envio (solo para actualizacion)", example = "1")
    private Long id;

    @NotNull(message = "El ID del pedido es obligatorio")
    @Positive(message = "El ID del pedido debe ser positivo")
    @Schema(description = "ID del pedido que da origen al envio", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idPedido;

    @NotBlank(message = "El origen es obligatorio")
    @Schema(description = "Direccion de origen del envio", example = "Centro de distribucion Santiago", requiredMode = Schema.RequiredMode.REQUIRED)
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    @Schema(description = "Direccion de destino del envio", example = "Av. Siempre Viva 742, Santiago", requiredMode = Schema.RequiredMode.REQUIRED)
    private String destino;

    @Schema(description = "Estado actual del envio", example = "PENDIENTE", allowableValues = {"PENDIENTE", "EN_TRANSITO", "ENTREGADO", "DEVUELTO", "CANCELADO"})
    private EstadoEnvio estado;

    @Schema(description = "Ubicacion actual del envio", example = "Centro de distribucion Santiago")
    private String ubicacionActual;

    @Schema(description = "Observaciones del envio", example = "Entregar entre 18:00 y 21:00")
    private String observacion;

    @Schema(description = "Motivo de incidencia si existe", example = "Cliente no se encontraba en domicilio")
    private String motivoIncidencia;

    @Schema(description = "Fecha estimada de entrega (opcional; si no se envia, el servicio la calcula con EtaCalculator)", example = "2026-06-20T18:00:00")
    private LocalDateTime fechaEstimadaEntrega;

    @Positive(message = "El ID del proveedor debe ser positivo")
    @Schema(description = "ID del proveedor logistico asignado", example = "1")
    private Long proveedorId;

    @Positive(message = "El ID de la ruta debe ser positivo")
    @Schema(description = "ID de la ruta de entrega asignada", example = "1")
    private Long rutaEntregaId;
}

