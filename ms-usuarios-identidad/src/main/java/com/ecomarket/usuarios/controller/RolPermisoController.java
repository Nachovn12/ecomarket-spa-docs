package com.ecomarket.usuarios.controller;

import com.ecomarket.usuarios.dto.RolPermisosRequestDTO;
import com.ecomarket.usuarios.dto.RolPermisosResponseDTO;
import com.ecomarket.usuarios.dto.VerificacionAccesoResponseDTO;
import com.ecomarket.usuarios.service.RolPermisoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/internos")
@RequiredArgsConstructor
@Tag(name = "Roles y Permisos", description = "Asignacion y consulta de roles/permisos de usuarios internos")
public class RolPermisoController {

    private final RolPermisoService rolPermisoService;

    @Operation(summary = "Asignar roles y permisos a un usuario interno",
            description = "Requiere que el solicitante tenga un rol con permisos para asignar roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles asignados",
                    content = @Content(schema = @Schema(implementation = RolPermisosResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Rol sin permisos para asignar", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @PutMapping("/{id}/roles-permisos")
    public ResponseEntity<RolPermisosResponseDTO> asignarRolesPermisos(
            @Parameter(description = "ID del usuario interno", example = "5", required = true) @PathVariable Long id,
            @Parameter(description = "Rol del usuario que solicita la operacion", example = "ADMIN")
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante,
            @Valid @RequestBody RolPermisosRequestDTO request) {
        return ResponseEntity.ok(rolPermisoService.asignarRolesPermisos(id, request, rolSolicitante));
    }

    @Operation(summary = "Obtener los roles y permisos de un usuario interno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles y permisos",
                    content = @Content(schema = @Schema(implementation = RolPermisosResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @GetMapping("/{id}/roles-permisos")
    public ResponseEntity<RolPermisosResponseDTO> obtenerRolesPermisos(
            @Parameter(description = "ID del usuario interno", example = "5", required = true) @PathVariable Long id,
            @Parameter(description = "Rol del usuario solicitante", example = "ADMIN")
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante) {
        return ResponseEntity.ok(rolPermisoService.obtenerRolesPermisos(id, rolSolicitante));
    }

    @Operation(summary = "Verificar si un usuario tiene acceso a un modulo",
            description = "Consulta booleana: el usuario tiene o no tiene acceso al modulo indicado.")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificacion",
            content = @Content(schema = @Schema(implementation = VerificacionAccesoResponseDTO.class)))
    @GetMapping("/{id}/verificar-acceso")
    public ResponseEntity<VerificacionAccesoResponseDTO> verificarAcceso(
            @Parameter(description = "ID del usuario interno", example = "5", required = true) @PathVariable Long id,
            @Parameter(description = "Modulo a verificar", example = "INVENTARIO", required = true) @RequestParam String modulo) {
        return ResponseEntity.ok(rolPermisoService.verificarAcceso(id, modulo));
    }
}
