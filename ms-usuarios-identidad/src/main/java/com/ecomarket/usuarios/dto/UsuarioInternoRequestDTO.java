package com.ecomarket.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos para crear un usuario interno (empleado/gerente/administrador)")
public class UsuarioInternoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 120, message = "El nombre debe tener entre 3 y 120 caracteres")
    @Schema(description = "Nombre del usuario", example = "Maria Gonzalez", minLength = 3, maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato valido")
    @Schema(description = "Email unico del usuario", example = "maria@ecomarket.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 6, max = 100, message = "La contrasena debe tener entre 6 y 100 caracteres")
    @Schema(description = "Contrasena del usuario", example = "P@ssw0rd!", minLength = 6, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "EMPLEADO|GERENTE|ADMINISTRADOR", message = "El rol debe ser EMPLEADO, GERENTE o ADMINISTRADOR")
    @Schema(description = "Rol del usuario", example = "EMPLEADO", allowableValues = {"EMPLEADO", "GERENTE", "ADMINISTRADOR"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String rol;
}
