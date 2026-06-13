package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.dto.CambioEstadoRutaRequestDTO;
import com.ecomarket.logistica.dto.RutaEntregaDTO;
import com.ecomarket.logistica.model.RutaEntrega;
import com.ecomarket.logistica.service.LogisticaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rutas")
@Tag(name = "Rutas de Entrega", description = "Planificacion y trazabilidad de rutas de entrega")
public class RutaEntregaController {

    private final LogisticaService logisticaService;

    public RutaEntregaController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    private EntityModel<RutaEntrega> ensamblar(RutaEntrega ruta) {
        return EntityModel.of(ruta,
                linkTo(methodOn(RutaEntregaController.class).obtenerPorId(ruta.getId())).withSelfRel(),
                linkTo(methodOn(RutaEntregaController.class).obtenerTodas()).withRel("rutas")
        );
    }

    @Operation(summary = "Crear una nueva ruta de entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ruta creada",
                    content = @Content(schema = @Schema(implementation = RutaEntrega.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<RutaEntrega>> crear(@Valid @RequestBody RutaEntregaDTO dto) {
        RutaEntrega creada = logisticaService.crearRuta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ensamblar(creada));
    }

    @Operation(summary = "Listar todas las rutas de entrega")
    @ApiResponse(responseCode = "200", description = "Listado de rutas",
            content = @Content(schema = @Schema(implementation = RutaEntrega.class)))
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<RutaEntrega>>> obtenerTodas() {
        List<EntityModel<RutaEntrega>> rutas = logisticaService.obtenerRutas().stream()
                .map(this::ensamblar)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(rutas, linkTo(methodOn(RutaEntregaController.class).obtenerTodas()).withSelfRel()));
    }

    @Operation(summary = "Obtener una ruta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta encontrada",
                    content = @Content(schema = @Schema(implementation = RutaEntrega.class))),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<RutaEntrega>> obtenerPorId(
            @Parameter(description = "ID de la ruta", example = "1", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(ensamblar(logisticaService.obtenerRutaPorId(id)));
    }

    @Operation(summary = "Actualizar una ruta de entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta actualizada",
                    content = @Content(schema = @Schema(implementation = RutaEntrega.class))),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<RutaEntrega>> actualizar(
            @Parameter(description = "ID de la ruta", example = "1", required = true) @PathVariable Long id,
            @RequestBody RutaEntregaDTO dto) {
        return ResponseEntity.ok(ensamblar(logisticaService.actualizarRuta(id, dto)));
    }

    @Operation(summary = "Eliminar una ruta de entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ruta eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la ruta", example = "1", required = true) @PathVariable Long id) {
        logisticaService.eliminarRuta(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambiar el estado de una ruta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado",
                    content = @Content(schema = @Schema(implementation = RutaEntrega.class))),
            @ApiResponse(responseCode = "400", description = "Estado invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content)
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<EntityModel<RutaEntrega>> cambiarEstado(
            @Parameter(description = "ID de la ruta", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRutaRequestDTO request) {
        return ResponseEntity.ok(
                ensamblar(logisticaService.cambiarEstadoRuta(id, request.getEstado()))
        );
    }
}
