package com.ecomarket.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Credenciales para iniciar sesion")
public class LoginRequestDTO {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato valido")
    @Schema(description = "Email del usuario", example = "usuario@ecomarket.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @NotBlank(message = "La contrasena es obligatoria")
    @Schema(description = "Contrasena del usuario", example = "P@ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
