package com.ecomarket.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "asignaciones_personal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad JPA que representa la asignacion de un usuario interno a una tienda")
public class AsignacionPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la asignacion", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idAsignacion;

    /**
     * ID del usuario interno (referencia externa al MS usuarios e identidad).
     */
    @Column(nullable = false)
    @Schema(description = "ID del usuario interno (referencia al MS Usuarios)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idUsuarioInterno;

    /**
     * Relacion manytoone con tienda.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tienda", nullable = false)
    @Schema(description = "Tienda a la que se asigna el personal")
    private Tienda tienda;

    @Column(nullable = false, length = 80)
    @Schema(description = "Cargo del personal en la tienda", example = "Vendedor", maxLength = 80, requiredMode = Schema.RequiredMode.REQUIRED)
    private String cargo;

    @Column(nullable = false)
    @Schema(description = "Indica si la asignacion esta activa", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean activa;

    @Column(nullable = false)
    @Schema(description = "Fecha de la asignacion", example = "2026-06-01T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaAsignacion;
}
