package com.ecomarket.logistica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "proveedores")
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

    public Proveedor() {}

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.activo == null) {
            this.activo = true;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getTipoProveedor() { return tipoProveedor; }
    public void setTipoProveedor(String tipoProveedor) { this.tipoProveedor = tipoProveedor; }
    public String getCobertura() { return cobertura; }
    public void setCobertura(String cobertura) { this.cobertura = cobertura; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}