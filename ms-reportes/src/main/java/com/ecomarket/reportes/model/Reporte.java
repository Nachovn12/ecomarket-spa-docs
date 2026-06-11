package com.ecomarket.reportes.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA de Reporte generado.
 * Registra los reportes producidos con sus metadatos (tipo, tienda, rango de fechas).
 * Las validaciones de entrada se gestionan en ReporteFiltroRequestDTO.
 */
@Entity
@Table(name = "reportes")
@Getter
@Setter
@NoArgsConstructor
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoReporte tipo;

    private Long idTienda;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private FormatoExportacion formatoExportacion;

    @Column(length = 100)
    private String generadoPor;

    private LocalDateTime fechaGeneracion;

    @PrePersist
    public void prePersist() {
        this.fechaGeneracion = LocalDateTime.now();
    }
}
