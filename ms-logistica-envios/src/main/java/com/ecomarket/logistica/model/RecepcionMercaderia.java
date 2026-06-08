package com.ecomarket.logistica.model;


import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "recepciones_mercaderia")
@Getter
@Setter
@NoArgsConstructor
public class RecepcionMercaderia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de solicitud es obligatorio")
    private Long idSolicitudReabastecimiento;

    @NotNull(message = "La cantidad recibida es obligatoria")
    private Integer cantidadRecibida;

    private String observacion;
    private LocalDateTime fechaRecepcion;
    @PrePersist
    protected void onCreate() {
        this.fechaRecepcion = LocalDateTime.now();
    }
}