package com.ecomarket.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "Datos de salida de un usuario interno")
public class UsuarioInternoResponseDTO {

    @Schema(description = "ID del usuario", example = "5")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Maria Gonzalez")
    private String nombre;

    @Schema(description = "Correo del usuario", example = "maria@ecomarket.cl")
    private String correo;

    @Schema(description = "Rol del usuario", example = "EMPLEADO")
    private String rol;

    @Schema(description = "Indica si esta activo", example = "true")
    private Boolean activo;

    @Schema(description = "Indica si fue eliminado logicamente", example = "false")
    private Boolean eliminado;

    @Schema(description = "Fecha de registro", example = "2026-01-15T10:00:00")
    private LocalDateTime fechaRegistro;
}
