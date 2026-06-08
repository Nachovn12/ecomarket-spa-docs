package com.ecomarket.usuarios.service;

import com.ecomarket.usuarios.dto.RolPermisosRequestDTO;
import com.ecomarket.usuarios.dto.RolPermisosResponseDTO;
import com.ecomarket.usuarios.dto.VerificacionAccesoResponseDTO;
import com.ecomarket.usuarios.model.Rol;
import com.ecomarket.usuarios.model.Usuario;
import com.ecomarket.usuarios.exception.AccesoNoAutorizadoException;
import com.ecomarket.usuarios.exception.UsuarioNoEncontradoException;
import com.ecomarket.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RolPermisoService {

    private static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";

    private static final Set<Rol> ROLES_INTERNOS = Set.of(
            Rol.EMPLEADO,
            Rol.GERENTE,
            Rol.ADMINISTRADOR
    );

    private static final Set<String> MODULOS_VALIDOS = Set.of(
            "USUARIOS",
            "ROLES_PERMISOS",
            "INVENTARIO",
            "INVENTARIO_CONSULTA",
            "PEDIDOS",
            "VENTAS",
            "REPORTES",
            "LOGISTICA",
            "SOPORTE",
            "FACTURACION",
            "DEVOLUCIONES",
            "TIENDAS",
            "ABASTECIMIENTO",
            "CONFIGURACION"
    );

    private static final Map<Rol, String> NIVELES_ACCESO = Map.of(
            Rol.EMPLEADO, "OPERATIVO",
            Rol.GERENTE, "GESTION",
            Rol.ADMINISTRADOR, "TOTAL"
    );

    private final UsuarioRepository usuarioRepository;

    public RolPermisosResponseDTO asignarRolesPermisos(Long id, RolPermisosRequestDTO request, String rolSolicitante) {
        validarPermisoAdministrador(rolSolicitante);

        Usuario usuario = buscarUsuarioInternoVigente(id);

        Rol rolNormalizado = normalizarRol(request.getRol());
        List<String> permisosNormalizados = normalizarPermisos(request.getPermisos());

        usuario.setRol(rolNormalizado);
        usuario.setNivelAcceso(NIVELES_ACCESO.get(rolNormalizado));
        usuario.setPermisos(permisosNormalizados);
        usuario.setModificadoPor(request.getModificadoPor().trim());
        usuario.setFechaModificacionAcceso(LocalDateTime.now());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return mapearRespuesta(usuarioActualizado);
    }

    public RolPermisosResponseDTO obtenerRolesPermisos(Long id, String rolSolicitante) {
        validarPermisoAdministrador(rolSolicitante);

        Usuario usuario = buscarUsuarioInternoVigente(id);

        return mapearRespuesta(usuario);
    }

    public VerificacionAccesoResponseDTO verificarAcceso(Long id, String modulo) {
        Usuario usuario = buscarUsuarioInternoVigente(id);
        String moduloNormalizado = normalizarModulo(modulo);

        List<String> permisos = usuario.getPermisos() != null ? usuario.getPermisos() : List.of();

        boolean tieneAcceso = Boolean.TRUE.equals(usuario.getActivo())
                && !Boolean.TRUE.equals(usuario.getEliminado())
                && permisos.contains(moduloNormalizado);

        String mensaje = tieneAcceso
                ? "Acceso permitido al módulo solicitado"
                : "Acceso bloqueado: el usuario no tiene permiso para este módulo";

        return VerificacionAccesoResponseDTO.builder()
                .idUsuario(usuario.getId())
                .rol(usuario.getRol().name())
                .nivelAcceso(usuario.getNivelAcceso())
                .modulo(moduloNormalizado)
                .accesoPermitido(tieneAcceso)
                .mensaje(mensaje)
                .build();
    }

    private Usuario buscarUsuarioInternoVigente(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario interno no encontrado"));

        if (Rol.CLIENTE.equals(usuario.getRol()) || Boolean.TRUE.equals(usuario.getEliminado())) {
            throw new UsuarioNoEncontradoException("Usuario interno no encontrado");
        }

        return usuario;
    }

    private void validarPermisoAdministrador(String rolSolicitante) {
        if (rolSolicitante == null || !ROL_ADMINISTRADOR.equals(rolSolicitante.trim().toUpperCase())) {
            throw new AccesoNoAutorizadoException("No tiene permisos para asignar roles y permisos");
        }
    }

    private Rol normalizarRol(String rol) {
        String rolNormalizado = rol.trim().toUpperCase();
        Rol rolEnum;
        try {
            rolEnum = Rol.valueOf(rolNormalizado);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol interno no válido: " + rol);
        }
        if (!ROLES_INTERNOS.contains(rolEnum)) {
            throw new IllegalArgumentException("Rol interno no válido: " + rol);
        }
        return rolEnum;
    }

    private List<String> normalizarPermisos(List<String> permisos) {
        return permisos.stream()
                .map(permiso -> permiso.trim().toUpperCase())
                .peek(this::validarModulo)
                .distinct()
                .toList();
    }

    private String normalizarModulo(String modulo) {
        if (modulo == null || modulo.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el módulo a validar");
        }

        String moduloNormalizado = modulo.trim().toUpperCase();
        validarModulo(moduloNormalizado);

        return moduloNormalizado;
    }

    private void validarModulo(String modulo) {
        if (!MODULOS_VALIDOS.contains(modulo)) {
            throw new IllegalArgumentException("Módulo o permiso no válido: " + modulo);
        }
    }

    private RolPermisosResponseDTO mapearRespuesta(Usuario usuario) {
        List<String> permisos = usuario.getPermisos() != null ? usuario.getPermisos() : List.of();

        return RolPermisosResponseDTO.builder()
                .idUsuario(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().name())
                .nivelAcceso(usuario.getNivelAcceso())
                .permisos(permisos)
                .modificadoPor(usuario.getModificadoPor())
                .fechaModificacionAcceso(usuario.getFechaModificacionAcceso())
                .build();
    }
}
