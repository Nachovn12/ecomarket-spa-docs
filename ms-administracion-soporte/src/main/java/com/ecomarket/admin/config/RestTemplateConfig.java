package com.ecomarket.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de RestTemplate para comunicación REST entre microservicios.
 * Incluye timeouts de conexión y lectura para evitar bloqueos de threads
 * si MS Usuarios e Identidad no responde.
 */
@Configuration
public class RestTemplateConfig {

    /** Timeout de conexión TCP en milisegundos. */
    private static final int CONNECT_TIMEOUT_MS = 3000;

    /** Timeout de lectura de respuesta HTTP en milisegundos. */
    private static final int READ_TIMEOUT_MS = 5000;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT_MS);
        factory.setReadTimeout(READ_TIMEOUT_MS);
        return new RestTemplate(factory);
    }
}
