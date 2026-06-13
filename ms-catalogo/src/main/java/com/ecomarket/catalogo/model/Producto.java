package com.ecomarket.catalogo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA del producto del catalogo. OWNER: ms-catalogo (PK = idProducto).
 * Existe una entidad homonima en ms-inventario-abastecimiento (PK = id) que
 * Almacena datos de stock por sucursal; ambos MS no comparten BD por diseno
 * (cada MS tiene su propia base mysql). La sincronizacion entre ambos
 * Modelos ocurre via comunicacion REST (ver CatalogoClientService / inventarioclientservice).
 * Las validaciones de entrada se gestionan en productorequestdto.
 */
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Entidad JPA que representa un producto del catalogo ecologico")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico del producto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idProducto;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "Codigo SKU unico del producto", example = "ECO-001", maxLength = 50, requiredMode = Schema.RequiredMode.REQUIRED)
    private String sku;

    @Column(nullable = false, length = 200)
    @Schema(description = "Nombre del producto", example = "Bolsa biodegradable mediana", maxLength = 200, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Precio unitario en CLP", example = "1990.0", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double precio;

    @Column(length = 1000)
    @Schema(description = "Descripcion general del producto", example = "Bolsa reutilizable hecha de almidon de maiz", maxLength = 1000)
    private String descripcion;

    @Column(length = 1000)
    @Schema(description = "Descripcion del atributo ecologico", example = "100% biodegradable en 180 dias", maxLength = 1000)
    private String descripcionEcologica;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "Estado del producto", example = "PUBLICADO", allowableValues = {"PUBLICADO", "PAUSADO", "AGOTADO", "DESCONTINUADO"})
    private EstadoProducto estado = EstadoProducto.PUBLICADO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    @Schema(description = "Categoria a la que pertenece el producto")
    private Categoria categoria;

    @Schema(description = "Fecha y hora de creacion del producto", example = "2026-05-01T10:15:30", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha y hora de la ultima actualizacion", example = "2026-06-10T14:22:05", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "producto")
    @JsonIgnore
    @Schema(hidden = true)
    private List<Resena> resenas = new ArrayList<>();
}
