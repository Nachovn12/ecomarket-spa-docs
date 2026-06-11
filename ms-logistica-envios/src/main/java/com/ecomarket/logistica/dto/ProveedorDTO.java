package com.ecomarket.logistica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO de entrada/salida para Proveedor logístico.
 * Incluye validaciones Bean Validation (JSR 380).
 */
@Getter
@Setter
@NoArgsConstructor
public class ProveedorDTO {

    @Positive(message = "El ID debe ser positivo")
    private Long id;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 120, message = "La razón social no puede superar 120 caracteres")
    private String razonSocial;

    @NotBlank(message = "El RUT es obligatorio")
    @Size(max = 15, message = "El RUT no puede superar 15 caracteres")
    private String rut;

    @Size(max = 100, message = "El contacto no puede superar 100 caracteres")
    private String contacto;

    @Email(message = "El email debe tener formato válido")
    private String email;

    @Pattern(regexp = "^[+0-9\\s-]{8,20}$", message = "El teléfono debe tener formato válido")
    private String telefono;

    @NotBlank(message = "El tipo de proveedor es obligatorio")
    private String tipoProveedor;

    @NotBlank(message = "La cobertura es obligatoria")
    private String cobertura;

    private Boolean activo;
    private LocalDateTime fechaRegistro;
}
