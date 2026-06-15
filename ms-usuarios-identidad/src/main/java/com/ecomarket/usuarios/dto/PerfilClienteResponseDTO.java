package com.ecomarket.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "Perfil publico de un cliente")
public class PerfilClienteResponseDTO {

    @Schema(description = "ID del cliente", example = "10")
    private Long id;

    @Schema(description = "Nombre del cliente", example = "Juan Perez")
    private String nombre;

    @Schema(description = "Correo del cliente", example = "juan.perez@correo.cl")
    private String correo;

    @Schema(description = "Telefono del cliente", example = "+56 9 8765 4321")
    private String telefono;

    @Schema(description = "Direccion de envio", example = "Av. Siempre Viva 742, Santiago")
    private String direccionEnvio;

    @Schema(description = "Medio de pago preferido", example = "TARJETA_CREDITO")
    private String medioPago;

    @Schema(description = "Rol del usuario", example = "CLIENTE")
    private String rol;

    @Schema(description = "Indica si el cliente esta activo", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de registro del cliente", example = "2026-01-15T10:00:00")
    private LocalDateTime fechaRegistro;
}
