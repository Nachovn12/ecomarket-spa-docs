package com.ecomarket.catalogo.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Entidad JPA que representa una resena de un producto")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico de la resena", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idResena;

    @Schema(description = "ID del cliente que escribio la resena (referencia logica al MS Usuarios)", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idCliente;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    @Schema(description = "Producto al que pertenece la resena")
    private Producto producto;

    @Schema(description = "Calificacion de la resena entre 1 y 5", example = "5", minimum = "1", maximum = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer calificacion;

    @Column(length = 1000)
    @Schema(description = "Comentario libre de la resena", example = "Excelente producto, se descompuso en 90 dias.", maxLength = 1000)
    private String comentario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Estado de moderacion de la resena", example = "PUBLICADA", allowableValues = {"PUBLICADA", "OCULTA", "PENDIENTE"})
    private EstadoResena estado = EstadoResena.PUBLICADA;

    @Schema(description = "Fecha y hora de creacion de la resena", example = "2026-05-15T12:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }
}
