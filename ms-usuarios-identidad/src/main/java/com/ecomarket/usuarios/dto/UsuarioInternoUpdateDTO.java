package com.ecomarket.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos opcionales para actualizar un usuario interno")
public class UsuarioInternoUpdateDTO {

    @Size(min = 3, max = 120, message = "El nombre debe tener entre 3 y 120 caracteres")
    @Schema(description = "Nuevo nombre", example = "Maria Gonzalez", maxLength = 120)
    private String nombre;

    @Email(message = "El correo debe tener un formato valido")
    @Schema(description = "Nuevo correo", example = "maria.g@ecomarket.cl")
    private String correo;

    @Size(min = 6, max = 100, message = "La contrasena debe tener entre 6 y 100 caracteres")
    @Schema(description = "Nueva contrasena", example = "N3wP@ss!", minLength = 6, maxLength = 100)
    private String password;

    @Pattern(regexp = "EMPLEADO|GERENTE|ADMINISTRADOR", message = "El rol debe ser EMPLEADO, GERENTE o ADMINISTRADOR")
    @Schema(description = "Nuevo rol", example = "GERENTE", allowableValues = {"EMPLEADO", "GERENTE", "ADMINISTRADOR"})
    private String rol;
}
