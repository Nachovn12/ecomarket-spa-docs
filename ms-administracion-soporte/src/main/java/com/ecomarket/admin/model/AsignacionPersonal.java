package com.ecomarket.admin.model;

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
public class AsignacionPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsignacion;

    /**
     * ID del usuario interno (referencia externa al MS Usuarios e Identidad).
     * Se mantiene como Long porque pertenece a otro microservicio.
     */
    @Column(nullable = false)
    private Long idUsuarioInterno;

    /**
     * Relación ManyToOne con Tienda.
     * Reemplaza el campo Long idTienda anterior para garantizar
     * integridad referencial dentro de este microservicio.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tienda", nullable = false)
    private Tienda tienda;

    @Column(nullable = false, length = 80)
    private String cargo;

    @Column(nullable = false)
    private Boolean activa;

    @Column(nullable = false)
    private LocalDateTime fechaAsignacion;
}
