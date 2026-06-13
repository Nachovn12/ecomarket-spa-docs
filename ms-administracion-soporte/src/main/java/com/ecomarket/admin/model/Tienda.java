package com.ecomarket.admin.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tiendas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad JPA que representa una tienda fisica de EcoMarket")
public class Tienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la tienda", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idTienda;

    @Column(nullable = false, length = 120)
    @Schema(description = "Nombre de la tienda", example = "EcoMarket Santiago Centro", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(nullable = false, length = 80)
    @Schema(description = "Ciudad de la tienda", example = "Santiago", maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String ciudad;

    @Column(nullable = false)
    @Schema(description = "Horario de apertura", example = "09:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime horarioApertura;

    @Column(nullable = false)
    @Schema(description = "Horario de cierre", example = "20:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime horarioCierre;

    @Column(length = 500)
    @Schema(description = "Politicas locales", example = "No se aceptan devoluciones despues de 30 dias", maxLength = 500)
    private String politicasLocales;

    @Column(nullable = false)
    @Schema(description = "Indica si la tienda esta activa", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean activa;

    @Column(nullable = false)
    @Schema(description = "Fecha de creacion", example = "2026-01-15T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de ultima actualizacion", example = "2026-06-01T14:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "tienda")
    @JsonIgnore
    @Schema(hidden = true)
    private List<AsignacionPersonal> asignaciones = new ArrayList<>();
}
