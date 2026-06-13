package com.ecomarket.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "Datos de salida del registro de un cliente")
public class UsuarioResponseDTO {

    @Schema(description = "ID del cliente", example = "10")
    private Long id;

    @Schema(description = "Nombre del cliente", example = "Juan Perez")
    private String nombre;

    @Schema(description = "Correo del cliente", example = "juan.perez@correo.cl")
    private String correo;

    @Schema(description = "Rol del usuario", example = "CLIENTE")
    private String rol;

    @Schema(description = "Indica si esta activo", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de registro", example = "2026-06-12T10:00:00")
    private LocalDateTime fechaRegistro;
}
