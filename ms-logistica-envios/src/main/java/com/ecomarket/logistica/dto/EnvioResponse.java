package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Datos de salida de un envio")
public class EnvioResponse {

    @Schema(description = "ID del envio", example = "1")
    private Long id;

    @Schema(description = "ID del pedido que da origen al envio", example = "1")
    private Long idPedido;

    @Schema(description = "Direccion de origen", example = "Centro de distribucion Santiago")
    private String origen;

    @Schema(description = "Direccion de destino", example = "Av. Siempre Viva 742, Santiago")
    private String destino;

    @Schema(description = "Estado del envio", example = "PREPARADO",
            allowableValues = {"PREPARADO", "EN_CAMINO", "ENTREGADO", "CON_INCIDENCIA"})
    private EstadoEnvio estado;

    @Schema(description = "Ubicacion actual del envio", example = "Centro de distribucion Santiago")
    private String ubicacionActual;

    @Schema(description = "Observaciones del envio", example = "Entregar entre 18:00 y 21:00")
    private String observacion;

    @Schema(description = "Motivo de la ultima incidencia", example = "Cliente no se encontraba en domicilio")
    private String motivoIncidencia;

    @Schema(description = "Fecha estimada de entrega", example = "2026-06-20T18:00:00")
    private LocalDateTime fechaEstimadaEntrega;

    @Schema(description = "Fecha de creacion", example = "2026-06-15T10:00:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de ultima actualizacion", example = "2026-06-16T14:30:00")
    private LocalDateTime fechaActualizacion;

    @Schema(description = "ID del proveedor logistico asignado", example = "1")
    private Long proveedorId;

    @Schema(description = "ID de la ruta de entrega asignada", example = "1")
    private Long rutaEntregaId;
}