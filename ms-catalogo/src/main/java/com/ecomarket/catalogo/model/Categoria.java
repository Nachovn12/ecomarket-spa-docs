package com.ecomarket.catalogo.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Entidad JPA que representa una categoria del catalogo de productos ecologicos")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico de la categoria", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idCategoria;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre unico de la categoria", example = "Productos biodegradables", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(description = "Descripcion opcional de la categoria", example = "Productos fabricados con materiales biodegradables")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Estado actual de la categoria", example = "ACTIVA", allowableValues = {"ACTIVA", "INACTIVA"})
    private EstadoCategoria estado = EstadoCategoria.ACTIVA;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    @Schema(hidden = true)
    private List<Producto> productos = new ArrayList<>();
}
