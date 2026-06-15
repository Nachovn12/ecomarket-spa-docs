package com.ecomarket.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "Resultado de un login exitoso")
public class LoginResponseDTO {

    @Schema(description = "ID del usuario autenticado", example = "10")
    private Long idUsuario;

    @Schema(description = "Nombre del usuario", example = "Juan Perez")
    private String nombre;

    @Schema(description = "Correo del usuario", example = "usuario@ecomarket.cl")
    private String correo;

    @Schema(description = "Rol del usuario", example = "CLIENTE", allowableValues = {"CLIENTE", "EMPLEADO", "GERENTE", "ADMINISTRADOR"})
    private String rol;

    @Schema(description = "Token de sesion (UUID). El cliente debe enviarlo en X-Token-Usuario en peticiones autenticadas", example = "550e8400-e29b-41d4-a716-446655440000")
    private String tokenSesion;

    @Schema(description = "Mensaje informativo del login", example = "Bienvenido")
    private String mensaje;

    @Schema(description = "Funcionalidades disponibles para el usuario", example = "[\"VER_CATALOGO\", \"REALIZAR_PEDIDO\"]")
    private List<String> funcionalidadesDisponibles;
}
