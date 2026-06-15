package com.ecomarket.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Datos de salida de un proveedor logistico")
public class ProveedorResponse {

    @Schema(description = "ID del proveedor", example = "1")
    private Long id;

    @Schema(description = "Razon social del proveedor", example = "Transportes Eco SpA")
    private String razonSocial;

    @Schema(description = "RUT unico del proveedor", example = "76.123.456-7")
    private String rut;

    @Schema(description = "Persona de contacto", example = "Juan Perez")
    private String contacto;

    @Schema(description = "Email de contacto", example = "contacto@transporteseco.cl")
    private String email;

    @Schema(description = "Telefono de contacto", example = "+56 2 2345 6789")
    private String telefono;

    @Schema(description = "Tipo de proveedor", example = "TRANSPORTE",
            allowableValues = {"TRANSPORTE", "REPARTO", "ALMACENAJE"})
    private String tipoProveedor;

    @Schema(description = "Cobertura geografica del proveedor", example = "Region Metropolitana")
    private String cobertura;

    @Schema(description = "Indica si el proveedor esta activo", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de creacion del registro", example = "2026-06-01T10:00:00")
    private LocalDateTime fechaCreacion;
}