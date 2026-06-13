package com.ecomarket.usuarios.controller;

import com.ecomarket.usuarios.dto.UsuarioInternoRequestDTO;
import com.ecomarket.usuarios.dto.UsuarioInternoResponseDTO;
import com.ecomarket.usuarios.dto.UsuarioInternoUpdateDTO;
import com.ecomarket.usuarios.service.UsuarioInternoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/internos")
@RequiredArgsConstructor
@Tag(name = "Usuarios Internos", description = "Administracion de usuarios con rol interno (ADMIN, OPERADOR, etc.)")
public class UsuarioInternoController {

    private final UsuarioInternoService usuarioInternoService;

    @Operation(summary = "Crear un usuario interno",
            description = "Requiere rol ADMIN en X-Rol-Usuario. Crea un usuario con rol interno (no cliente).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario interno creado",
                    content = @Content(schema = @Schema(implementation = UsuarioInternoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Requiere rol ADMIN", content = @Content),
            @ApiResponse(responseCode = "409", description = "Email o RUN duplicado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UsuarioInternoResponseDTO> crearUsuarioInterno(
            @Parameter(description = "Rol del usuario solicitante", example = "ADMIN")
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante,
            @Valid @RequestBody UsuarioInternoRequestDTO request) {
        UsuarioInternoResponseDTO response = usuarioInternoService.crearUsuarioInterno(request, rolSolicitante);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar usuarios internos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de usuarios internos",
                    content = @Content(schema = @Schema(implementation = UsuarioInternoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Requiere rol ADMIN", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<UsuarioInternoResponseDTO>> listarUsuariosInternos(
            @Parameter(description = "Rol del usuario solicitante", example = "ADMIN")
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante) {
        return ResponseEntity.ok(usuarioInternoService.listarUsuariosInternos(rolSolicitante));
    }

    @Operation(summary = "Obtener un usuario interno por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario interno encontrado",
                    content = @Content(schema = @Schema(implementation = UsuarioInternoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioInternoResponseDTO> obtenerUsuarioInternoPorId(
            @Parameter(description = "ID del usuario interno", example = "5", required = true) @PathVariable Long id,
            @Parameter(description = "Rol del usuario solicitante", example = "ADMIN")
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante) {
        return ResponseEntity.ok(usuarioInternoService.obtenerUsuarioInternoPorId(id, rolSolicitante));
    }

    @Operation(summary = "Actualizar un usuario interno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario interno actualizado",
                    content = @Content(schema = @Schema(implementation = UsuarioInternoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioInternoResponseDTO> actualizarUsuarioInterno(
            @Parameter(description = "ID del usuario interno", example = "5", required = true) @PathVariable Long id,
            @Parameter(description = "Rol del usuario solicitante", example = "ADMIN")
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante,
            @Valid @RequestBody UsuarioInternoUpdateDTO request) {
        return ResponseEntity.ok(usuarioInternoService.actualizarUsuarioInterno(id, request, rolSolicitante));
    }

    @Operation(summary = "Desactivar un usuario interno",
            description = "Cambia su estado activo a false sin eliminar el registro (soft delete).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario desactivado",
                    content = @Content(schema = @Schema(implementation = UsuarioInternoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioInternoResponseDTO> desactivarUsuarioInterno(
            @Parameter(description = "ID del usuario interno", example = "5", required = true) @PathVariable Long id,
            @Parameter(description = "Rol del usuario solicitante", example = "ADMIN")
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante) {
        return ResponseEntity.ok(usuarioInternoService.desactivarUsuarioInterno(id, rolSolicitante));
    }

    @Operation(summary = "Eliminar un usuario interno",
            description = "Borra el registro del usuario. Reservado a administradores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado",
                    content = @Content(schema = @Schema(implementation = UsuarioInternoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioInternoResponseDTO> eliminarUsuarioInterno(
            @Parameter(description = "ID del usuario interno", example = "5", required = true) @PathVariable Long id,
            @Parameter(description = "Rol del usuario solicitante", example = "ADMIN")
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante) {
        return ResponseEntity.ok(usuarioInternoService.eliminarUsuarioInterno(id, rolSolicitante));
    }
}
