package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.dto.ProductoRequestDTO;
import com.ecomarket.catalogo.dto.ProductoResponseDTO;
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
 * Controller de Productos del catálogo.
 * Patrón CSR: recibe la petición HTTP, delega al Service y retorna ResponseEntity.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private CatalogoService catalogoService;

    @PostMapping
    public ResponseEntity<EntityModel<ProductoResponseDTO>> crear(
            @Valid @RequestBody ProductoRequestDTO dto) {
        ProductoResponseDTO creado = catalogoService.crearProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ensamblarResource(creado));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> listarTodos() {
        List<EntityModel<ProductoResponseDTO>> productos = catalogoService.obtenerTodosProductos().stream()
                .map(this::ensamblarResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).listarTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoResponseDTO>> buscarPorId(@PathVariable Long id) {
        ProductoResponseDTO producto = catalogoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(ensamblarResource(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO dto) {
        ProductoResponseDTO actualizado = catalogoService.actualizarProducto(id, dto);
        return ResponseEntity.ok(ensamblarResource(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        catalogoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> buscar(
            @RequestParam(required = false) String palabraClave,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Double precioMinimo,
            @RequestParam(required = false) Double precioMaximo) {

        List<ProductoResponseDTO> resultados;
        if (palabraClave != null) {
            resultados = catalogoService.buscarPorPalabraClave(palabraClave);
        } else if (idCategoria != null) {
            resultados = catalogoService.buscarPorCategoria(idCategoria);
        } else if (precioMinimo != null && precioMaximo != null) {
            resultados = catalogoService.buscarPorPrecio(precioMinimo, precioMaximo);
        } else {
            resultados = catalogoService.obtenerTodosProductos();
        }

        List<EntityModel<ProductoResponseDTO>> recursos = resultados.stream()
                .map(this::ensamblarResource).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(recursos,
                linkTo(methodOn(ProductoController.class).listarTodos()).withRel("productos")));
    }

    @GetMapping("/ecologicos")
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> buscarEcologicos(
            @RequestParam(defaultValue = "biodegradable") String atributoEcologico) {
        List<EntityModel<ProductoResponseDTO>> recursos = catalogoService.buscarEcologicos(atributoEcologico)
                .stream().map(this::ensamblarResource).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(recursos,
                linkTo(methodOn(ProductoController.class).listarTodos()).withRel("productos")));
    }

    private EntityModel<ProductoResponseDTO> ensamblarResource(ProductoResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ProductoController.class).buscarPorId(dto.getIdProducto())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarTodos()).withRel("productos"));
    }
}
