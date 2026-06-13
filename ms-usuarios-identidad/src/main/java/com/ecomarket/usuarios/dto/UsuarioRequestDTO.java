package com.ecomarket.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos para registrar un nuevo cliente")
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 120, message = "El nombre debe tener entre 3 y 120 caracteres")
    @Schema(description = "Nombre del cliente", example = "Juan Perez", minLength = 3, maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato valido")
    @Schema(description = "Email unico del cliente", example = "juan.perez@correo.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 6, max = 100, message = "La contrasena debe tener entre 6 y 100 caracteres")
    @Schema(description = "Contrasena del cliente", example = "P@ssw0rd!", minLength = 6, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    @Size(min = 8, max = 12, message = "El RUN debe tener entre 8 y 12 caracteres (ej: 12345678-9)")
    @Schema(description = "RUN/RUT del cliente (sin puntos, con guion y digito verificador)", example = "12345678-9", maxLength = 12)
    private String run;
}
