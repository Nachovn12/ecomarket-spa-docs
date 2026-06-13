package com.ecomarket.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "respuestas_soporte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad JPA que representa una respuesta dentro del hilo de un ticket")
public class RespuestaSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la respuesta", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idRespuesta;

    /**
     * Relacion manytoone con ticketsoporte.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ticket", nullable = false)
    @Schema(description = "Ticket al que pertenece la respuesta")
    private TicketSoporte ticket;

    @Column(nullable = false, length = 1000)
    @Schema(description = "Mensaje de la respuesta", example = "Hemos verificado tu caso y procederemos a...", maxLength = 1000, requiredMode = Schema.RequiredMode.REQUIRED)
    private String mensaje;

    @Column(nullable = false, length = 120)
    @Schema(description = "Responsable de la respuesta", example = "operador1", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String respondidoPor;

    @Column(nullable = false)
    @Schema(description = "Fecha de la respuesta", example = "2026-06-11T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaRespuesta;
}
