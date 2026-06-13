package com.ecomarket.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Asignacion de rol y permisos a un usuario interno")
public class RolPermisosRequestDTO {

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "EMPLEADO|GERENTE|ADMINISTRADOR", message = "El rol debe ser EMPLEADO, GERENTE o ADMINISTRADOR")
    @Schema(description = "Rol del usuario", example = "GERENTE", allowableValues = {"EMPLEADO", "GERENTE", "ADMINISTRADOR"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String rol;

    @NotEmpty(message = "Debe indicar al menos un permiso")
    @Schema(description = "Listado de permisos del usuario", example = "[\"GESTIONAR_INVENTARIO\", \"VER_REPORTES\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<@NotBlank(message = "El permiso no puede estar vacio") String> permisos;

    @NotBlank(message = "Debe indicar quien realiza la modificacion")
    @Schema(description = "Usuario que realiza la asignacion", example = "admin1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String modificadoPor;
}
