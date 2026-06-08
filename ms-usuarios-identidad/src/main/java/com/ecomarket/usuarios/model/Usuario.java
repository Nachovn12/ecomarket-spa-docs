package com.ecomarket.usuarios.model;

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
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, unique = true, length = 120)
    private String correo;

    @Column(length = 30)
    private String telefono;

    @Column(length = 255)
    private String direccionEnvio;

    @Column(length = 80)
    private String medioPago;

    @Column(nullable = false)
    private String password;

    /**
     * Rol del usuario mapeado como enum JPA.
     * Reemplaza el campo String anterior para garantizar
     * consistencia de valores y seguridad en tiempo de compilación.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Rol rol;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false)
    private Boolean eliminado;

    @Column(length = 50)
    private String nivelAcceso;

    /**
     * Lista de permisos del usuario almacenada en tabla separada (usuario_permisos).
     * Reemplaza el campo String con valores separados por coma,
     * mejorando la normalización del modelo de datos.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "usuario_permisos",
            joinColumns = @JoinColumn(name = "usuario_id")
    )
    @Column(name = "permiso", length = 80)
    private List<String> permisos;

    @Column(length = 120)
    private String modificadoPor;

    private LocalDateTime fechaModificacionAcceso;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;
}
