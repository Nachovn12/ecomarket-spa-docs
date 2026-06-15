package com.ecomarket.logistica.model;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Entidad JPA de seguimiento de envio.
 * Registra cada cambio de estado de un envio como historial trazable.
 * Las validaciones de entrada se gestionan en cambioestadorequestdto.
 */
@Entity
@Table(name = "seguimientos_envio")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Entidad JPA que registra un evento de seguimiento en un envio")
public class SeguimientoEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del evento de seguimiento", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "envio_id", nullable = false)
    @Schema(description = "Envio al que pertenece este evento")
    private Envio envio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Schema(description = "Estado registrado en este evento", example = "EN_CAMINO", allowableValues = {"PREPARADO", "EN_CAMINO", "ENTREGADO", "CON_INCIDENCIA"})
    private EstadoEnvio estado;

    @Schema(description = "Ubicacion registrada en el evento", example = "Centro de distribucion Santiago")
    private String ubicacion;

    @Schema(description = "Observaciones del evento", example = "Salida conforme a horario")
    private String observacion;

    @Column(nullable = false, length = 120)
    @Schema(description = "Usuario que registro el evento", example = "operador1", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String actualizadoPor;

    @Schema(description = "Fecha y hora del evento", example = "2026-06-15T14:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }
}
