package com.ecomarket.admin.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets_soporte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad JPA que representa un ticket de soporte")
public class TicketSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del ticket", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idTicket;

    @Column(nullable = false, length = 120)
    @Schema(description = "Asunto del ticket", example = "No puedo ver mi historial de pedidos", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String asunto;

    @Column(nullable = false, length = 1000)
    @Schema(description = "Descripcion detallada del problema", example = "Al ingresar a mi perfil me aparece error 500", maxLength = 1000, requiredMode = Schema.RequiredMode.REQUIRED)
    private String descripcion;

    @Column(nullable = false, length = 120)
    @Schema(description = "Nombre de contacto", example = "Juan Perez", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombreContacto;

    @Column(nullable = false, length = 120)
    @Schema(description = "Correo de contacto", example = "juan.perez@correo.cl", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String correoContacto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "Prioridad del ticket", example = "MEDIA", allowableValues = {"BAJA", "MEDIA", "ALTA"})
    private PrioridadTicket prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Schema(description = "Estado del ticket", example = "ABIERTO", allowableValues = {"ABIERTO", "EN_ATENCION", "RESUELTO", "CERRADO"})
    private EstadoTicket estado;

    @Column(nullable = false)
    @Schema(description = "Fecha de creacion del ticket", example = "2026-06-10T09:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de ultima actualizacion", example = "2026-06-12T14:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "ticket")
    @JsonIgnore
    @Schema(hidden = true)
    private List<RespuestaSoporte> respuestas = new ArrayList<>();
}
