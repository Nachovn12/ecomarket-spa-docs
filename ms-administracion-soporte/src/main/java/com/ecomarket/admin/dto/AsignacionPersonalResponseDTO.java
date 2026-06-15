package com.ecomarket.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "Datos de salida de una asignacion de personal")
public class AsignacionPersonalResponseDTO {

    @Schema(description = "ID de la asignacion", example = "1")
    private Long idAsignacion;

    @Schema(description = "ID del usuario interno asignado", example = "5")
    private Long idUsuarioInterno;

    @Schema(description = "ID de la tienda", example = "1")
    private Long idTienda;

    @Schema(description = "Cargo", example = "Vendedor")
    private String cargo;

    @Schema(description = "Indica si la asignacion esta activa", example = "true")
    private Boolean activa;

    @Schema(description = "Fecha de la asignacion", example = "2026-06-01T10:00:00")
    private LocalDateTime fechaAsignacion;
}
