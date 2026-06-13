package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO de salida para exponer datos de seguimiento de envio sin exponer la entidad JPA.
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos de un evento de seguimiento de un envio")
public class SeguimientoEnvioDTO {

    @Schema(description = "ID del evento de seguimiento", example = "1")
    private Long id;

    @Schema(description = "ID del envio al que pertenece", example = "1")
    private Long envioId;

    @Schema(description = "Estado del envio en este evento", example = "EN_TRANSITO", allowableValues = {"PENDIENTE", "EN_TRANSITO", "ENTREGADO", "DEVUELTO", "CANCELADO"})
    private EstadoEnvio estado;

    @Schema(description = "Ubicacion registrada en el evento", example = "Centro de distribucion Santiago")
    private String ubicacion;

    @Schema(description = "Observaciones del evento", example = "Salida conforme a horario")
    private String observacion;

    @Schema(description = "Usuario que registro el evento", example = "operador1")
    private String actualizadoPor;

    @Schema(description = "Fecha y hora del evento", example = "2026-06-15T14:30:00")
    private LocalDateTime fechaRegistro;
}
