package com.ecomarket.admin.dto;

import com.ecomarket.admin.model.EstadoTicket;
import com.ecomarket.admin.model.PrioridadTicket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "Datos de salida de un ticket de soporte")
public class TicketSoporteResponseDTO {

    @Schema(description = "ID del ticket", example = "1")
    private Long idTicket;

    @Schema(description = "Asunto", example = "No puedo ver mi historial de pedidos")
    private String asunto;

    @Schema(description = "Descripcion", example = "Al ingresar a mi perfil me aparece error 500")
    private String descripcion;

    @Schema(description = "Nombre de contacto", example = "Juan Perez")
    private String nombreContacto;

    @Schema(description = "Correo de contacto", example = "juan.perez@correo.cl")
    private String correoContacto;

    @Schema(description = "Prioridad", example = "MEDIA", allowableValues = {"BAJA", "MEDIA", "ALTA", "CRITICA"})
    private PrioridadTicket prioridad;

    @Schema(description = "Estado", example = "ABIERTO", allowableValues = {"ABIERTO", "EN_PROCESO", "RESUELTO", "CERRADO"})
    private EstadoTicket estado;

    @Schema(description = "Fecha de creacion", example = "2026-06-10T09:00:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de ultima actualizacion", example = "2026-06-12T14:30:00")
    private LocalDateTime fechaActualizacion;
}
