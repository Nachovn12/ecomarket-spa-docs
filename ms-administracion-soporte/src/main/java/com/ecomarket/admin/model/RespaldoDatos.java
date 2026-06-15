package com.ecomarket.admin.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "respaldos_datos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad JPA que representa un respaldo de datos programado o ejecutado")
public class RespaldoDatos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del respaldo", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idRespaldo;

    @Column(nullable = false, length = 120)
    @Schema(description = "Origen de datos a respaldar", example = "bd_pedidos", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String origenDatos;

    @Column(nullable = false, length = 80)
    @Schema(description = "Frecuencia del respaldo", example = "DIARIA", maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String frecuencia;

    @Column(nullable = false, length = 120)
    @Schema(description = "Responsable del respaldo", example = "admin1", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String responsable;

    @Column(nullable = false, length = 30)
    @Schema(description = "Estado del respaldo", example = "PROGRAMADO", allowableValues = {"PROGRAMADO", "EN_EJECUCION", "EXITOSO", "FALLIDO"})
    private String estado;

    @Column(nullable = false, length = 500)
    @Schema(description = "Detalle del resultado de la ejecucion", example = "Respaldo completado sin errores", maxLength = 500)
    private String resultado;

    @Column(nullable = false)
    @Schema(description = "Fecha programada para el respaldo", example = "2026-06-20T02:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime fechaProgramada;

    @Schema(description = "Fecha en que se ejecuto el respaldo", example = "2026-06-20T02:05:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaEjecucion;

    @Schema(description = "Fecha de la ultima restauracion si aplica", example = "2026-06-21T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaRestauracion;
}
