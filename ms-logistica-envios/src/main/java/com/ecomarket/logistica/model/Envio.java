package com.ecomarket.logistica.model;


import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import com.ecomarket.logistica.model.enums.EstadoEnvio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "envios")
@Getter
@Setter
@NoArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del pedido es obligatorio")
    @Column(nullable = false)
    private Long idPedido;

    @NotBlank(message = "El origen es obligatorio")
    @Column(nullable = false)
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    @Column(nullable = false)
    private String destino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estado = EstadoEnvio.PREPARADO;

    private String ubicacionActual;
    private String observacion;
    private String motivoIncidencia;

    private LocalDateTime fechaCreacion;

    @NotNull(message = "La fecha estimada de entrega es obligatoria")
    private LocalDateTime fechaEstimadaEntrega;

    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "ruta_entrega_id")
    private RutaEntrega rutaEntrega;
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoEnvio.PREPARADO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}