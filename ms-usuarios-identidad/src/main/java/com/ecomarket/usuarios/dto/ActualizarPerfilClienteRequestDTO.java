package com.ecomarket.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos para actualizar el perfil de un cliente")
public class ActualizarPerfilClienteRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 120, message = "El nombre debe tener entre 3 y 120 caracteres")
    @Schema(description = "Nombre completo del cliente", example = "Juan Perez", minLength = 3, maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato valido")
    @Schema(description = "Email del cliente", example = "juan.perez@correo.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @NotBlank(message = "El telefono es obligatorio")
    @Size(min = 8, max = 30, message = "El telefono debe tener entre 8 y 30 caracteres")
    @Schema(description = "Telefono de contacto", example = "+56 9 8765 4321", requiredMode = Schema.RequiredMode.REQUIRED)
    private String telefono;

    @NotBlank(message = "La direccion de envio es obligatoria")
    @Size(max = 255, message = "La direccion de envio no puede superar 255 caracteres")
    @Schema(description = "Direccion de envio del cliente", example = "Av. Siempre Viva 742, Santiago", maxLength = 255, requiredMode = Schema.RequiredMode.REQUIRED)
    private String direccionEnvio;

    @Size(max = 80, message = "El medio de pago no puede superar 80 caracteres")
    @Schema(description = "Medio de pago preferido", example = "TARJETA_CREDITO", maxLength = 80)
    private String medioPago;
}
