package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.dto.ResenaRequestDTO;
import com.ecomarket.catalogo.dto.ResenaResponseDTO;
import com.ecomarket.catalogo.service.CatalogoService;
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
 * Controller de Reseñas del catálogo.
 * CRUD completo: GET /resenas, GET /resenas/{id}, POST /resenas, DELETE /resenas/{id}.
 * Patrón CSR: delega toda la lógica al Service.
 */
@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private CatalogoService catalogoService;

    @PostMapping
    public ResponseEntity<EntityModel<ResenaResponseDTO>> crearResena(
            @Valid @RequestBody ResenaRequestDTO dto) {
        ResenaResponseDTO creada = catalogoService.crearResena(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ensamblarResource(creada));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ResenaResponseDTO>>> listarResenas() {
        List<EntityModel<ResenaResponseDTO>> resenas = catalogoService.obtenerTodasResenas().stream()
                .map(this::ensamblarResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(resenas,
                linkTo(methodOn(ResenaController.class).listarResenas()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ResenaResponseDTO>> obtenerResenaPorId(@PathVariable Long id) {
        ResenaResponseDTO resena = catalogoService.obtenerResenaPorId(id);
        return ResponseEntity.ok(ensamblarResource(resena));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(@PathVariable Long id) {
        catalogoService.eliminarResena(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<ResenaResponseDTO> ensamblarResource(ResenaResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ResenaController.class).obtenerResenaPorId(dto.getIdResena())).withSelfRel(),
                linkTo(methodOn(ResenaController.class).listarResenas()).withRel("resenas"));
    }
}
