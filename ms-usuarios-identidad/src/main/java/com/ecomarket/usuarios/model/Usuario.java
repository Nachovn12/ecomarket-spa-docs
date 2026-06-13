package com.ecomarket.usuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad JPA unica que representa cualquier usuario del sistema (cliente o interno)")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del usuario", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, length = 120)
    @Schema(description = "Nombre completo del usuario", example = "Juan Perez", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(nullable = false, unique = true, length = 120)
    @Schema(description = "Correo unico del usuario", example = "juan.perez@correo.cl", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @Column(length = 30)
    @Schema(description = "Telefono del cliente (no aplica a internos)", example = "+56 9 8765 4321", maxLength = 30)
    private String telefono;

    @Column(length = 255)
    @Schema(description = "Direccion de envio (cliente)", example = "Av. Siempre Viva 742, Santiago", maxLength = 255)
    private String direccionEnvio;

    @Column(length = 80)
    @Schema(description = "Medio de pago preferido (cliente)", example = "TARJETA_CREDITO", maxLength = 80)
    private String medioPago;

    @Column(nullable = false)
    @Schema(description = "Contrasena del usuario (almacenada en texto plano en este proyecto academico)", example = "P@ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    @Column(length = 12, unique = true)
    @Schema(description = "RUN/RUT del usuario sin puntos, con guion y digito verificador (ej: 12345678-9)", example = "12345678-9", maxLength = 12)
    private String run;

    /**
     * Rol del usuario mapeado como enum JPA.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Schema(description = "Rol del usuario", example = "CLIENTE", allowableValues = {"CLIENTE", "EMPLEADO", "GERENTE", "ADMINISTRADOR"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private Rol rol;

    @Column(nullable = false)
    @Schema(description = "Indica si el usuario esta activo", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean activo;

    @Column(nullable = false)
    @Schema(description = "Indica si fue eliminado logicamente (soft delete)", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean eliminado;

    @Column(length = 50)
    @Schema(description = "Nivel de acceso derivado del rol", example = "ALTO", maxLength = 50)
    private String nivelAcceso;

    /**
     * Lista de permisos del usuario almacenada en tabla separada (usuario_permisos).
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "usuario_permisos",
            joinColumns = @JoinColumn(name = "usuario_id")
    )
    @Column(name = "permiso", length = 80)
    @Schema(description = "Permisos del usuario (solo usuarios internos)", example = "[\"GESTIONAR_INVENTARIO\"]")
    private List<String> permisos;

    @Column(length = 120)
    @Schema(description = "Usuario que realizo la ultima modificacion de acceso", example = "admin1", maxLength = 120)
    private String modificadoPor;

    @Schema(description = "Fecha de la ultima modificacion de acceso", example = "2026-06-01T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaModificacionAcceso;

    @Column(nullable = false)
    @Schema(description = "Fecha de registro del usuario", example = "2026-01-15T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaRegistro;
}
