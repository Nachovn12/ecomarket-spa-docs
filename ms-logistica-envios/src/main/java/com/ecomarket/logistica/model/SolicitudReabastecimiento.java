package com.ecomarket.logistica.model;


import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_reabastecimiento")
@Getter
@Setter
@NoArgsConstructor
public class SolicitudReabastecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de tienda es obligatorio")
    private Long idTienda;

    @NotNull(message = "El ID de producto es obligatorio")
    private Long idProducto;

    @NotNull(message = "La cantidad solicitada es obligatoria")
    private Integer cantidadSolicitada;

    private String estado;
    private LocalDateTime fechaSolicitud;
    @PrePersist
    protected void onCreate() {
        this.fechaSolicitud = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "PENDIENTE";
        }
    }
}