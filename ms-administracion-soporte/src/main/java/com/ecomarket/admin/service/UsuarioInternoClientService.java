package com.ecomarket.admin.service;

import com.ecomarket.admin.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Cliente REST para comunicación con MS Usuarios e Identidad.
 * Valida que un usuario interno exista antes de asignarlo a una tienda (IE 2.4.1).
 * Incluye manejo de timeout (configurado en RestTemplateConfig) y errores remotos.
 */
@Service
@RequiredArgsConstructor
public class UsuarioInternoClientService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioInternoClientService.class);

    private final RestTemplate restTemplate;

    @Value("${ms.usuarios.url}")
    private String msUsuariosUrl;

    /**
     * Valida que el usuario interno exista y esté activo en MS Usuarios e Identidad.
     * Llama a: GET /api/usuarios/internos/{id} con cabecera X-Rol-Usuario: ADMINISTRADOR
     *
     * @param idUsuarioInterno ID del usuario a validar
     * @throws RecursoNoEncontradoException  si el usuario no existe (HTTP 404)
     * @throws IllegalStateException         si MS Usuarios no está disponible (timeout/conexión)
     */
    public void validarUsuarioInternoExiste(Long idUsuarioInterno) {
        String url = msUsuariosUrl + "/api/usuarios/internos/" + idUsuarioInterno;
        log.info("Validando existencia de usuario interno. idUsuarioInterno={}", idUsuarioInterno);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Rol-Usuario", "ADMINISTRADOR");
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            log.info("Usuario interno validado correctamente. idUsuarioInterno={}", idUsuarioInterno);

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Usuario interno no encontrado en MS Usuarios. idUsuarioInterno={}", idUsuarioInterno);
            throw new RecursoNoEncontradoException(
                    "El usuario interno con id " + idUsuarioInterno + " no existe en el sistema");

        } catch (ResourceAccessException e) {
            // Timeout de conexión o lectura — el servicio no está disponible
            log.error("Timeout o conexión rechazada al contactar MS Usuarios. idUsuarioInterno={}, error={}",
                    idUsuarioInterno, e.getMessage());
            throw new IllegalStateException(
                    "MS Usuarios e Identidad no está disponible. Intente nuevamente en unos momentos.");

        } catch (Exception e) {
            log.error("Error inesperado al comunicarse con MS Usuarios. idUsuarioInterno={}, error={}",
                    idUsuarioInterno, e.getMessage());
            throw new IllegalStateException(
                    "No se pudo validar el usuario interno. Verifique que MS Usuarios e Identidad esté disponible.");
        }
    }
}
