package com.ecomarket.usuarios.controller;

import com.ecomarket.usuarios.dto.RolPermisosRequestDTO;
import com.ecomarket.usuarios.dto.RolPermisosResponseDTO;
import com.ecomarket.usuarios.dto.VerificacionAccesoResponseDTO;
import com.ecomarket.usuarios.service.RolPermisoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/internos")
@RequiredArgsConstructor
public class RolPermisoController {

    private final RolPermisoService rolPermisoService;

    @PutMapping("/{id}/roles-permisos")
    public ResponseEntity<RolPermisosResponseDTO> asignarRolesPermisos(
            @PathVariable Long id,
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante,
            @Valid @RequestBody RolPermisosRequestDTO request) {
        return ResponseEntity.ok(rolPermisoService.asignarRolesPermisos(id, request, rolSolicitante));
    }

    @GetMapping("/{id}/roles-permisos")
    public ResponseEntity<RolPermisosResponseDTO> obtenerRolesPermisos(
            @PathVariable Long id,
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante) {
        return ResponseEntity.ok(rolPermisoService.obtenerRolesPermisos(id, rolSolicitante));
    }

    @GetMapping("/{id}/verificar-acceso")
    public ResponseEntity<VerificacionAccesoResponseDTO> verificarAcceso(
            @PathVariable Long id,
            @RequestParam String modulo) {
        return ResponseEntity.ok(rolPermisoService.verificarAcceso(id, modulo));
    }
}
