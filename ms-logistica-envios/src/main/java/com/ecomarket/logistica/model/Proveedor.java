package com.ecomarket.logistica.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "proveedores")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Entidad JPA que representa un proveedor logistico (transporte/reparto)")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del proveedor", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "La razon social es obligatoria")
    @Column(nullable = false)
    @Schema(description = "Razon social del proveedor", example = "Transportes Eco SpA", requiredMode = Schema.RequiredMode.REQUIRED)
    private String razonSocial;

    @NotBlank(message = "El RUT es obligatorio")
    @Column(nullable = false, unique = true)
    @Schema(description = "RUT unico del proveedor", example = "76.123.456-7", requiredMode = Schema.RequiredMode.REQUIRED)
    private String rut;

    @Schema(description = "Persona de contacto", example = "Juan Perez")
    private String contacto;

    @Email(message = "El email debe tener formato valido")
    @Schema(description = "Email de contacto", example = "contacto@transporteseco.cl")
    private String email;

    @Schema(description = "Telefono de contacto", example = "+56 2 2345 6789")
    private String telefono;

    @NotBlank(message = "El tipo de proveedor es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Tipo de proveedor", example = "TRANSPORTE", allowableValues = {"TRANSPORTE", "REPARTO", "ALMACENAJE"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoProveedor;

    @NotBlank(message = "La cobertura es obligatoria")
    @Column(nullable = false)
    @Schema(description = "Cobertura geografica", example = "REGIONAL", allowableValues = {"LOCAL", "REGIONAL", "NACIONAL"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String cobertura;

    @Column(nullable = false)
    @Schema(description = "Indica si el proveedor esta activo", example = "true")
    private Boolean activo = true;

    @Schema(description = "Fecha de registro del proveedor", example = "2026-01-15T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaRegistro;
    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.activo == null) {
            this.activo = true;
        }
    }
    @Schema(description = "Plazo de despacho del proveedor en horas", example = "24")
    private Integer plazoDespachoHoras;
}