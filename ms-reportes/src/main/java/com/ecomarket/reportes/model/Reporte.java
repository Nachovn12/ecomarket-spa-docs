package com.ecomarket.reportes.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA de reporte generado.
 * Registra los reportes producidos con sus metadatos (tipo, tienda, rango de fechas).
 * Las validaciones de entrada se gestionan en reportefiltrorequestdto.
 */
@Entity
@Table(name = "reportes")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Entidad JPA que representa un reporte generado")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del reporte", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Schema(description = "Tipo de reporte", example = "VENTAS", allowableValues = {"VENTAS", "INVENTARIO", "RENDIMIENTO_TIENDA", "KPI"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoReporte tipo;

    @Schema(description = "ID de la tienda asociada al reporte", example = "1")
    private Long idTienda;

    @Schema(description = "Fecha de inicio del periodo del reporte", example = "2026-06-01")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin del periodo del reporte", example = "2026-06-30")
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    @Schema(description = "Formato de exportacion del reporte", example = "PDF", allowableValues = {"PDF", "EXCEL", "CSV"})
    private FormatoExportacion formatoExportacion;

    @Column(length = 100)
    @Schema(description = "Usuario que genero el reporte", example = "gerente1", maxLength = 100)
    private String generadoPor;

    @Schema(description = "Fecha de generacion del reporte", example = "2026-06-12T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaGeneracion;

    @PrePersist
    public void prePersist() {
        this.fechaGeneracion = LocalDateTime.now();
    }
}
