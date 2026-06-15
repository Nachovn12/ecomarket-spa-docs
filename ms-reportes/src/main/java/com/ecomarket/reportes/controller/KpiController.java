package com.ecomarket.reportes.controller;

import com.ecomarket.reportes.dto.IndicadorKPIResponseDTO;
import com.ecomarket.reportes.model.IndicadorKPI;
import com.ecomarket.reportes.model.TipoKPI;
import com.ecomarket.reportes.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/kpis")
@Tag(name = "KPIs", description = "Indicadores clave de desempeno (calculados o registrados)")
public class KpiController {

    private final ReporteService reporteService;

    public KpiController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(summary = "Listar todos los KPIs")
    @ApiResponse(responseCode = "200", description = "Listado de KPIs",
            content = @Content(schema = @Schema(implementation = IndicadorKPIResponseDTO.class)))
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<IndicadorKPIResponseDTO>>> listarKPIs() {
        List<EntityModel<IndicadorKPIResponseDTO>> kpis = reporteService.listarKPIs().stream()
                .map(k -> {
                    IndicadorKPIResponseDTO dto = reporteService.toDTO(k);
                    return EntityModel.of(dto,
                            linkTo(methodOn(KpiController.class).obtenerKPIPorId(dto.getId())).withSelfRel(),
                            linkTo(methodOn(KpiController.class).listarKPIs()).withRel("kpis"));
                })
                .toList();
        CollectionModel<EntityModel<IndicadorKPIResponseDTO>> collection = CollectionModel.of(kpis,
                linkTo(methodOn(KpiController.class).listarKPIs()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Obtener un KPI por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "KPI encontrado",
                    content = @Content(schema = @Schema(implementation = IndicadorKPIResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "KPI no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<IndicadorKPIResponseDTO>> obtenerKPIPorId(
            @Parameter(description = "ID del KPI", example = "1", required = true) @PathVariable Long id) {
        IndicadorKPI kpi = reporteService.obtenerKPIPorId(id);
        IndicadorKPIResponseDTO dto = reporteService.toDTO(kpi);
        EntityModel<IndicadorKPIResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(KpiController.class).obtenerKPIPorId(id)).withSelfRel(),
                linkTo(methodOn(KpiController.class).listarKPIs()).withRel("kpis"),
                linkTo(methodOn(KpiController.class).listarPorTipo(kpi.getTipo())).withRel("por-tipo"));
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Registrar un nuevo KPI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "KPI registrado",
                    content = @Content(schema = @Schema(implementation = IndicadorKPIResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<IndicadorKPIResponseDTO>> crearKPI(@Valid @RequestBody IndicadorKPI kpi) {
        IndicadorKPI creado = reporteService.crearKPI(kpi);
        IndicadorKPIResponseDTO dto = reporteService.toDTO(creado);
        EntityModel<IndicadorKPIResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(KpiController.class).obtenerKPIPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(KpiController.class).listarKPIs()).withRel("kpis"));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @Operation(summary = "Eliminar un KPI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "KPI eliminado", content = @Content),
            @ApiResponse(responseCode = "404", description = "KPI no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarKPI(
            @Parameter(description = "ID del KPI", example = "1", required = true) @PathVariable Long id) {
        reporteService.eliminarKPI(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar KPIs por tipo",
            description = "Filtra los KPIs por su tipo (ej: VENTAS, INVENTARIO, RENDIMIENTO).")
    @ApiResponse(responseCode = "200", description = "Listado filtrado por tipo",
            content = @Content(schema = @Schema(implementation = IndicadorKPIResponseDTO.class)))
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<CollectionModel<EntityModel<IndicadorKPIResponseDTO>>> listarPorTipo(
            @Parameter(description = "Tipo de KPI", example = "VENTAS", required = true) @PathVariable TipoKPI tipo) {
        List<EntityModel<IndicadorKPIResponseDTO>> kpis = reporteService.listarKPIsPorTipo(tipo).stream()
                .map(k -> {
                    IndicadorKPIResponseDTO dto = reporteService.toDTO(k);
                    return EntityModel.of(dto,
                            linkTo(methodOn(KpiController.class).obtenerKPIPorId(dto.getId())).withSelfRel());
                })
                .toList();
        CollectionModel<EntityModel<IndicadorKPIResponseDTO>> collection = CollectionModel.of(kpis,
                linkTo(methodOn(KpiController.class).listarPorTipo(tipo)).withSelfRel(),
                linkTo(methodOn(KpiController.class).listarKPIs()).withRel("kpis"));
        return ResponseEntity.ok(collection);
    }
}
