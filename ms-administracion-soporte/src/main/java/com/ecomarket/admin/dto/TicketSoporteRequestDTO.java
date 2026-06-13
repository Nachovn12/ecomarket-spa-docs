package com.ecomarket.admin.dto;

import com.ecomarket.admin.model.PrioridadTicket;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos para crear un ticket de soporte")
public class TicketSoporteRequestDTO {

    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 120, message = "El asunto no puede superar 120 caracteres")
    @Schema(description = "Asunto del ticket", example = "No puedo ver mi historial de pedidos", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String asunto;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 1000, message = "La descripcion no puede superar 1000 caracteres")
    @Schema(description = "Descripcion detallada del problema", example = "Al ingresar a mi perfil me aparece error 500", maxLength = 1000, requiredMode = Schema.RequiredMode.REQUIRED)
    private String descripcion;

    @NotBlank(message = "El nombre de contacto es obligatorio")
    @Size(max = 120, message = "El nombre de contacto no puede superar 120 caracteres")
    @Schema(description = "Nombre del contacto", example = "Juan Perez", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombreContacto;

    @NotBlank(message = "El correo de contacto es obligatorio")
    @Email(message = "El correo de contacto debe tener un formato valido")
    @Size(max = 120, message = "El correo de contacto no puede superar 120 caracteres")
    @Schema(description = "Correo de contacto", example = "juan.perez@correo.cl", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String correoContacto;

    @NotNull(message = "La prioridad del ticket es obligatoria")
    @Schema(description = "Prioridad del ticket", example = "MEDIA", allowableValues = {"BAJA", "MEDIA", "ALTA", "CRITICA"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private PrioridadTicket prioridad;
}
