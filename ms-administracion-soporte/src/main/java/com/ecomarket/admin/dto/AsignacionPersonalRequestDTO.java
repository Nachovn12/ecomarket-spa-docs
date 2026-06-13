package com.ecomarket.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Asignacion de personal a una tienda")
public class AsignacionPersonalRequestDTO {

    @NotNull(message = "El id del usuario interno es obligatorio")
    @Schema(description = "ID del usuario interno (del MS Usuarios)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idUsuarioInterno;

    @NotNull(message = "El id de la tienda es obligatorio")
    @Schema(description = "ID de la tienda", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idTienda;

    @NotBlank(message = "El cargo es obligatorio")
    @Size(max = 80, message = "El cargo no puede superar 80 caracteres")
    @Schema(description = "Cargo del personal en la tienda", example = "Vendedor", maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String cargo;
}
