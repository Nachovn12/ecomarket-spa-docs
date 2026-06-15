package com.ecomarket.reportes.service;

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
 * Cliente REST para comunicacion con MS inventario y abastecimiento.
 * Implementa IE 2.4.1 (Comunicacion entre MS) con manejo de timeout y errores remotos.
 */
@Service
@RequiredArgsConstructor
public class InventarioClientService {

    private static final Logger log = LoggerFactory.getLogger(InventarioClientService.class);

    private final RestTemplate restTemplate;

    @Value("${ms.inventario.url}")
    private String msInventarioUrl;

    /**
     * Verifica la conectividad con el MS inventario y abastecimiento.
     * GET /api/inventario/productos
     */
    public Map<String, Object> ping() {
        String url = msInventarioUrl + "/api/inventario/productos";
        log.info("Llamando a MS Inventario y Abastecimiento. url={}", url);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Rol-Usuario", "SISTEMA");
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();
            log.info("MS Inventario y Abastecimiento respondio OK");
            return response;
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("MS Inventario y Abastecimiento respondio 404");
            return null;
        } catch (ResourceAccessException e) {
            log.error("MS Inventario y Abastecimiento no disponible. error={}", e.getMessage());
            throw new IllegalStateException("MS Inventario y Abastecimiento no esta disponible. Intente nuevamente en unos momentos.");
        } catch (Exception e) {
            log.error("Error inesperado al llamar a MS Inventario y Abastecimiento. error={}", e.getMessage());
            throw new IllegalStateException("No se pudo comunicar con MS Inventario y Abastecimiento.");
        }
    }
}
