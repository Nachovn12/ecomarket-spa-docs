package com.ecomarket.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "Resultado de la consulta de roles y permisos")
public class RolPermisosResponseDTO {

    @Schema(description = "ID del usuario", example = "5")
    private Long idUsuario;

    @Schema(description = "Nombre del usuario", example = "Maria Gonzalez")
    private String nombre;

    @Schema(description = "Correo del usuario", example = "maria@ecomarket.cl")
    private String correo;

    @Schema(description = "Rol del usuario", example = "GERENTE")
    private String rol;

    @Schema(description = "Nivel de acceso derivado del rol", example = "ALTO")
    private String nivelAcceso;

    @Schema(description = "Permisos del usuario", example = "[\"GESTIONAR_INVENTARIO\", \"VER_REPORTES\"]")
    private List<String> permisos;

    @Schema(description = "Usuario que realizo la ultima modificacion", example = "admin1")
    private String modificadoPor;

    @Schema(description = "Fecha de la ultima modificacion de acceso", example = "2026-06-01T10:00:00")
    private LocalDateTime fechaModificacionAcceso;
}
