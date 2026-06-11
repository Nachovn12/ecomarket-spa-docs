package com.ecomarket.usuarios.service;

import com.ecomarket.usuarios.dto.LoginRequestDTO;
import com.ecomarket.usuarios.dto.LoginResponseDTO;
import com.ecomarket.usuarios.model.Usuario;
import com.ecomarket.usuarios.exception.CredencialesInvalidasException;
import com.ecomarket.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;

    public LoginResponseDTO iniciarSesion(LoginRequestDTO request) {
        String correoNormalizado = request.getCorreo().trim().toLowerCase();
        log.info("Intento de inicio de sesión. correo={}", correoNormalizado);

        Usuario usuario = usuarioRepository.findByCorreo(correoNormalizado)
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña inválidos"));

        if (!usuario.getPassword().equals(request.getPassword())) {
            log.warn("Contraseña incorrecta para correo: {}", correoNormalizado);
            throw new CredencialesInvalidasException("Correo o contraseña inválidos");
        }

        if (!Boolean.TRUE.equals(usuario.getActivo()) || Boolean.TRUE.equals(usuario.getEliminado())) {
            log.warn("Intento de login con cuenta inactiva o eliminada. correo={}", correoNormalizado);
            throw new CredencialesInvalidasException("Correo o contraseña inválidos");
        }

        log.info("Login exitoso. idUsuario={}, rol={}", usuario.getId(), usuario.getRol());

        return LoginResponseDTO.builder()
                .idUsuario(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().name())
                .tokenSesion(UUID.randomUUID().toString())
                .mensaje("Inicio de sesión exitoso")
                .funcionalidadesDisponibles(resolverFuncionalidades(usuario.getRol()))
                .build();
    }

    private List<String> resolverFuncionalidades(com.ecomarket.usuarios.model.Rol rol) {
        return switch (rol) {
            case CLIENTE -> List.of(
                    "gestionar perfil",
                    "gestionar pedidos",
                    "consultar historial de compras",
                    "gestionar carrito",
                    "dejar resenas",
                    "solicitar soporte"
            );
            case EMPLEADO -> List.of(
                    "registrar ventas",
                    "atender devoluciones",
                    "consultar inventario",
                    "generar facturas",
                    "atender reclamaciones"
            );
            case GERENTE -> List.of(
                    "gestionar inventario",
                    "generar reportes",
                    "gestionar tiendas",
                    "gestionar pedidos",
                    "supervisar ventas",
                    "gestionar personal"
            );
            case ADMINISTRADOR -> List.of(
                    "gestionar usuarios",
                    "configurar permisos",
                    "monitorizacion sistema",
                    "respaldar datos",
                    "restaurar datos",
                    "configuracion sistema"
            );
        };
    }
}
