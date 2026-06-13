package com.ecomarket.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Resultado de verificar si un usuario tiene acceso a un modulo")
public class VerificacionAccesoResponseDTO {

    @Schema(description = "ID del usuario verificado", example = "5")
    private Long idUsuario;

    @Schema(description = "Rol del usuario", example = "GERENTE")
    private String rol;

    @Schema(description = "Nivel de acceso del rol", example = "ALTO")
    private String nivelAcceso;

    @Schema(description = "Modulo consultado", example = "INVENTARIO")
    private String modulo;

    @Schema(description = "Indica si el acceso esta permitido", example = "true")
    private Boolean accesoPermitido;

    @Schema(description = "Mensaje explicativo", example = "Acceso permitido")
    private String mensaje;
}
