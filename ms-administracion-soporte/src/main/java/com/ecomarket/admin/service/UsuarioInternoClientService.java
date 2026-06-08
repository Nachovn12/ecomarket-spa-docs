package com.ecomarket.admin.service;

import com.ecomarket.admin.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Servicio cliente REST que comunica este microservicio con MS Usuarios e Identidad.
 * Permite validar que un usuario interno existe y está activo antes de asignarlo
 * a una tienda (cumple IE 2.4.1 y IE 2.4.2).
 */
@Service
@RequiredArgsConstructor
public class UsuarioInternoClientService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioInternoClientService.class);

    private final RestTemplate restTemplate;

    @Value("${ms.usuarios.url}")
    private String msUsuariosUrl;

    /**
     * Valida que el usuario interno existe en MS Usuarios e Identidad.
     * Llama a: GET /api/usuarios/internos/{id} con cabecera X-Rol-Usuario: ADMINISTRADOR
     *
     * @param idUsuarioInterno ID del usuario a validar
     * @throws RecursoNoEncontradoException si el usuario no existe o no es interno
     * @throws IllegalArgumentException si el servicio remoto no está disponible
     */
    @SuppressWarnings("unchecked")
    public void validarUsuarioInternoExiste(Long idUsuarioInterno) {
        String url = msUsuariosUrl + "/api/usuarios/internos/" + idUsuarioInterno;
        log.info("Validando existencia de usuario interno. idUsuarioInterno={}, url={}", idUsuarioInterno, url);

        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("X-Rol-Usuario", "ADMINISTRADOR");
            org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);

            restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    Map.class
            );

            log.info("Usuario interno validado correctamente. idUsuarioInterno={}", idUsuarioInterno);

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Usuario interno no encontrado en MS Usuarios. idUsuarioInterno={}", idUsuarioInterno);
            throw new RecursoNoEncontradoException(
                    "El usuario interno con id " + idUsuarioInterno + " no existe en el sistema"
            );
        } catch (Exception e) {
            log.error("Error al comunicarse con MS Usuarios e Identidad. idUsuarioInterno={}, error={}", idUsuarioInterno, e.getMessage());
            throw new IllegalArgumentException(
                    "No se pudo validar el usuario interno. Verifique que MS Usuarios e Identidad esté disponible."
            );
        }
    }
}
