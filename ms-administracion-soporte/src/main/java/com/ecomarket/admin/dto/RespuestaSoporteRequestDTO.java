package com.ecomarket.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Respuesta agregada a un ticket de soporte")
public class RespuestaSoporteRequestDTO {

    @NotNull(message = "El id del ticket es obligatorio")
    @Schema(description = "ID del ticket al que se responde", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idTicket;

    @NotBlank(message = "El mensaje de respuesta es obligatorio")
    @Size(max = 1000, message = "El mensaje no puede superar 1000 caracteres")
    @Schema(description = "Mensaje de respuesta", example = "Hemos verificado tu caso y procederemos a...", maxLength = 1000, requiredMode = Schema.RequiredMode.REQUIRED)
    private String mensaje;

    @NotBlank(message = "El responsable de la respuesta es obligatorio")
    @Size(max = 120, message = "El responsable no puede superar 120 caracteres")
    @Schema(description = "Responsable que emite la respuesta", example = "operador1", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String respondidoPor;
}
