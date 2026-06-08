package com.ecomarket.admin.model;

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
public class RespuestaSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespuesta;

    /**
     * Relación ManyToOne con TicketSoporte.
     * Reemplaza el campo Long idTicket anterior para garantizar
     * integridad referencial entre respuestas y tickets.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ticket", nullable = false)
    private TicketSoporte ticket;

    @Column(nullable = false, length = 1000)
    private String mensaje;

    @Column(nullable = false, length = 120)
    private String respondidoPor;

    @Column(nullable = false)
    private LocalDateTime fechaRespuesta;
}
