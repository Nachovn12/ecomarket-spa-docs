package com.ecomarket.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
 * DTO de entrada/salida para proveedor logistico.
 * Incluye validaciones bean validation (JSR 380).
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos de un proveedor logistico")
public class ProveedorDTO {

    @Positive(message = "El ID debe ser positivo")
    @Schema(description = "ID del proveedor (solo para actualizacion)", example = "1")
    private Long id;

    @NotBlank(message = "La razon social es obligatoria")
    @Size(max = 120, message = "La razon social no puede superar 120 caracteres")
    @Schema(description = "Razon social del proveedor", example = "Transportes Eco SpA", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String razonSocial;

    @NotBlank(message = "El RUT es obligatorio")
    @Size(max = 15, message = "El RUT no puede superar 15 caracteres")
    @Schema(description = "RUT del proveedor", example = "76.123.456-7", maxLength = 15, requiredMode = Schema.RequiredMode.REQUIRED)
    private String rut;

    @Size(max = 100, message = "El contacto no puede superar 100 caracteres")
    @Schema(description = "Persona de contacto", example = "Juan Perez", maxLength = 100)
    private String contacto;

    @Email(message = "El email debe tener formato valido")
    @Schema(description = "Email de contacto", example = "contacto@transporteseco.cl")
    private String email;

    @Pattern(regexp = "^[+0-9\\s-]{8,20}$", message = "El telefono debe tener formato valido")
    @Schema(description = "Telefono de contacto", example = "+56 2 2345 6789")
    private String telefono;

    @NotBlank(message = "El tipo de proveedor es obligatorio")
    @Schema(description = "Tipo de proveedor", example = "TRANSPORTE", allowableValues = {"TRANSPORTE", "REPARTO", "ALMACENAJE"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoProveedor;

    @NotBlank(message = "La cobertura es obligatoria")
    @Schema(description = "Cobertura geografica", example = "REGIONAL", allowableValues = {"LOCAL", "REGIONAL", "NACIONAL"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String cobertura;

    @Schema(description = "Indica si el proveedor esta activo", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de registro del proveedor", example = "2026-01-15T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaRegistro;
}
