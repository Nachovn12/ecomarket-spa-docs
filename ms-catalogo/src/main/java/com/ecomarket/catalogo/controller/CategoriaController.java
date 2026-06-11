package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.dto.CategoriaRequestDTO;
import com.ecomarket.catalogo.dto.CategoriaResponseDTO;
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
 * Controller de Categorías del catálogo.
 * Patrón CSR: recibe petición HTTP, delega al Service, retorna ResponseEntity con DTO.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CatalogoService catalogoService;

    @PostMapping
    public ResponseEntity<EntityModel<CategoriaResponseDTO>> crearCategoria(
            @Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO creada = catalogoService.crearCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ensamblarResource(creada));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CategoriaResponseDTO>>> listarCategorias() {
        List<EntityModel<CategoriaResponseDTO>> categorias = catalogoService.obtenerTodasCategorias().stream()
                .map(this::ensamblarResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(categorias,
                linkTo(methodOn(CategoriaController.class).listarCategorias()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CategoriaResponseDTO>> obtenerCategoriaPorId(@PathVariable Long id) {
        CategoriaResponseDTO categoria = catalogoService.obtenerCategoriaPorId(id);
        return ResponseEntity.ok(ensamblarResource(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CategoriaResponseDTO>> actualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO actualizada = catalogoService.actualizarCategoria(id, dto);
        return ResponseEntity.ok(ensamblarResource(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        catalogoService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<CategoriaResponseDTO> ensamblarResource(CategoriaResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(CategoriaController.class).obtenerCategoriaPorId(dto.getIdCategoria())).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).listarCategorias()).withRel("categorias"));
    }
}
