package com.ecomarket.logistica.model;


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
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La razón social es obligatoria")
    @Column(nullable = false)
    private String razonSocial;

    @NotBlank(message = "El RUT es obligatorio")
    @Column(nullable = false, unique = true)
    private String rut;

    private String contacto;

    @Email(message = "El email debe tener formato válido")
    private String email;

    private String telefono;

    @NotBlank(message = "El tipo de proveedor es obligatorio")
    @Column(nullable = false)
    private String tipoProveedor;

    @NotBlank(message = "La cobertura es obligatoria")
    @Column(nullable = false)
    private String cobertura;

    @Column(nullable = false)
    private Boolean activo = true;

    private LocalDateTime fechaRegistro;
    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.activo == null) {
            this.activo = true;
        }
    }
}