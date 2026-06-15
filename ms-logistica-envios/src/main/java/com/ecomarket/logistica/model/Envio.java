package com.ecomarket.logistica.model;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Entidad JPA de envio.
 * Las validaciones de entrada se gestionan en enviodto y cambioestadorequestdto.
 */
@Entity
@Table(name = "envios")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Entidad JPA que representa un envio de un pedido a un cliente")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del envio", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    @Schema(description = "ID del pedido origen del envio", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idPedido;

    @Column(nullable = false, length = 200)
    @Schema(description = "Direccion de origen del envio", example = "Centro de distribucion Santiago", maxLength = 200, requiredMode = Schema.RequiredMode.REQUIRED)
    private String origen;

    @Column(nullable = false, length = 200)
    @Schema(description = "Direccion de destino del envio", example = "Av. Siempre Viva 742, Santiago", maxLength = 200, requiredMode = Schema.RequiredMode.REQUIRED)
    private String destino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Schema(description = "Estado del envio", example = "PREPARADO", allowableValues = {"PREPARADO", "EN_CAMINO", "ENTREGADO", "CON_INCIDENCIA"})
    private EstadoEnvio estado = EstadoEnvio.PREPARADO;

    @Schema(description = "Ubicacion actual del envio", example = "Centro de distribucion Santiago")
    private String ubicacionActual;

    @Schema(description = "Observaciones del envio", example = "Entregar entre 18:00 y 21:00")
    private String observacion;

    @Schema(description = "Motivo de la ultima incidencia registrada", example = "Cliente no se encontraba en domicilio")
    private String motivoIncidencia;

    @Column(nullable = false)
    @Schema(description = "Fecha estimada de entrega", example = "2026-06-20T18:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime fechaEstimadaEntrega;

    @Schema(description = "Fecha de creacion del envio", example = "2026-06-15T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de ultima actualizacion", example = "2026-06-16T14:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    @Schema(description = "Proveedor logistico asignado al envio")
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruta_entrega_id")
    @Schema(description = "Ruta de entrega asignada")
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
