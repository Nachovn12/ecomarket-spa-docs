package com.ecomarket.logistica.model;


import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import com.ecomarket.logistica.model.enums.EstadoEnvio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "seguimientos_envio")
@Getter
@Setter
@NoArgsConstructor
public class SeguimientoEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "envio_id")
    private Envio envio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estado;

    private String ubicacion;
    private String observacion;

    @NotBlank(message = "El usuario que actualiza es obligatorio")
    private String actualizadoPor;

    private LocalDateTime fechaRegistro;
    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }
}