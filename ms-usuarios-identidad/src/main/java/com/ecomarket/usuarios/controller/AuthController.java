package com.ecomarket.usuarios.controller;

import com.ecomarket.usuarios.dto.LoginRequestDTO;
import com.ecomarket.usuarios.dto.LoginResponseDTO;
import com.ecomarket.usuarios.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacion", description = "Login de usuarios al sistema")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Iniciar sesion",
            description = "Autentica al usuario con email y password y retorna un token de sesion UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciales invalidas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> iniciarSesion(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.iniciarSesion(request);
        return ResponseEntity.ok(response);
    }
}
