package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.dto.ProveedorDTO;
import com.ecomarket.logistica.model.Proveedor;
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
@RequestMapping("/api/envios/proveedores")
@Tag(name = "Proveedores Logisticos", description = "Administracion de proveedores de transporte y logistica")
public class ProveedorController {

    private final LogisticaService logisticaService;

    public ProveedorController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    private EntityModel<Proveedor> ensamblar(Proveedor prov) {
        return EntityModel.of(prov,
                linkTo(methodOn(ProveedorController.class).obtenerPorId(prov.getId())).withSelfRel(),
                linkTo(methodOn(ProveedorController.class).obtenerTodos()).withRel("proveedores")
        );
    }

    @Operation(summary = "Crear un proveedor logistico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proveedor creado",
                    content = @Content(schema = @Schema(implementation = Proveedor.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto al crear el proveedor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<Proveedor>> crear(@Valid @RequestBody ProveedorDTO dto) {
        Proveedor creado = logisticaService.crearProveedor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ensamblar(creado));
    }

    @Operation(summary = "Listar todos los proveedores logisticos")
    @ApiResponse(responseCode = "200", description = "Listado de proveedores",
            content = @Content(schema = @Schema(implementation = Proveedor.class)))
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Proveedor>>> obtenerTodos() {
        List<EntityModel<Proveedor>> proveedores = logisticaService.obtenerProveedores().stream()
                .map(this::ensamblar)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(proveedores, linkTo(methodOn(ProveedorController.class).obtenerTodos()).withSelfRel()));
    }

    @Operation(summary = "Obtener un proveedor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado",
                    content = @Content(schema = @Schema(implementation = Proveedor.class))),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Proveedor>> obtenerPorId(
            @Parameter(description = "ID del proveedor", example = "1", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(ensamblar(logisticaService.obtenerProveedorPorId(id)));
    }

    @Operation(summary = "Actualizar un proveedor logistico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado",
                    content = @Content(schema = @Schema(implementation = Proveedor.class))),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Proveedor>> actualizar(
            @Parameter(description = "ID del proveedor", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody ProveedorDTO dto) {
        return ResponseEntity.ok(ensamblar(logisticaService.actualizarProveedor(id, dto)));
    }

    @Operation(summary = "Activar un proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proveedor activado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activar(
            @Parameter(description = "ID del proveedor", example = "1", required = true) @PathVariable Long id) {
        logisticaService.activarProveedor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desactivar un proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proveedor desactivado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID del proveedor", example = "1", required = true) @PathVariable Long id) {
        logisticaService.desactivarProveedor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar proveedores activos")
    @ApiResponse(responseCode = "200", description = "Proveedores activos",
            content = @Content(schema = @Schema(implementation = Proveedor.class)))
    @GetMapping("/activos")
    public ResponseEntity<CollectionModel<EntityModel<Proveedor>>> obtenerActivos() {
        List<EntityModel<Proveedor>> activos = logisticaService.obtenerProveedoresActivos().stream()
                .map(this::ensamblar)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(activos));
    }

    @Operation(summary = "Buscar proveedores por tipo y cobertura")
    @ApiResponse(responseCode = "200", description = "Resultados de la busqueda",
            content = @Content(schema = @Schema(implementation = Proveedor.class)))
    @GetMapping("/buscar")
    public ResponseEntity<CollectionModel<EntityModel<Proveedor>>> buscar(
            @Parameter(description = "Tipo de proveedor", example = "TRANSPORTE", required = true)
            @RequestParam String tipoProveedor,
            @Parameter(description = "Cobertura geografica", example = "REGIONAL", required = true)
            @RequestParam String cobertura) {
        List<EntityModel<Proveedor>> resultados = logisticaService.buscarProveedores(tipoProveedor, cobertura).stream()
                .map(this::ensamblar)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(resultados));
    }
}
