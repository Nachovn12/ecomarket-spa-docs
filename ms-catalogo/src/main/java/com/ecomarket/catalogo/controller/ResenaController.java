package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.dto.ResenaRequestDTO;
import com.ecomarket.catalogo.dto.ResenaResponseDTO;
import com.ecomarket.catalogo.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller de resenas del catalogo.
 * CRUD completo: GET /resenas, GET /resenas/{id}, POST /resenas, DELETE /resenas/{id}.
 * Patron CSR: delega toda la logica al service.
 */
@RestController
@RequestMapping("/api/resenas")
@Tag(name = "Resenas", description = "Operaciones sobre resenas y calificaciones de productos")
public class ResenaController {

    @Autowired
    private CatalogoService catalogoService;

    @Operation(
            summary = "Crear una nueva resena",
            description = "Registra una resena de un producto por parte de un cliente. Calificacion entre 1 y 5."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resena creada",
                    content = @Content(schema = @Schema(implementation = ResenaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o calificacion fuera de rango",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<ResenaResponseDTO>> crearResena(
            @Valid @RequestBody ResenaRequestDTO dto) {
        ResenaResponseDTO creada = catalogoService.crearResena(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ensamblarResource(creada));
    }

    @Operation(
            summary = "Listar todas las resenas",
            description = "Retorna la coleccion de resenas registradas con enlaces HATEOAS."
    )
    @ApiResponse(responseCode = "200", description = "Listado de resenas",
            content = @Content(schema = @Schema(implementation = ResenaResponseDTO.class)))
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ResenaResponseDTO>>> listarResenas() {
        List<EntityModel<ResenaResponseDTO>> resenas = catalogoService.obtenerTodasResenas().stream()
                .map(this::ensamblarResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(resenas,
                linkTo(methodOn(ResenaController.class).listarResenas()).withSelfRel()));
    }

    @Operation(
            summary = "Obtener una resena por ID",
            description = "Busca una resena especifica por su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resena encontrada",
                    content = @Content(schema = @Schema(implementation = ResenaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Resena no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ResenaResponseDTO>> obtenerResenaPorId(
            @Parameter(description = "ID de la resena", example = "1", required = true)
            @PathVariable Long id) {
        ResenaResponseDTO resena = catalogoService.obtenerResenaPorId(id);
        return ResponseEntity.ok(ensamblarResource(resena));
    }

    @Operation(
            summary = "Eliminar una resena",
            description = "Borra la resena identificada por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resena eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Resena no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(
            @Parameter(description = "ID de la resena", example = "1", required = true)
            @PathVariable Long id) {
        catalogoService.eliminarResena(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<ResenaResponseDTO> ensamblarResource(ResenaResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ResenaController.class).obtenerResenaPorId(dto.getIdResena())).withSelfRel(),
                linkTo(methodOn(ResenaController.class).listarResenas()).withRel("resenas"));
    }
}
