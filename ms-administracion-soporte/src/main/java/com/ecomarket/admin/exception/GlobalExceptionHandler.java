package com.ecomarket.admin.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador centralizado de excepciones para ms-administracion-soporte.
 * Retorna respuestas JSON estructuradas con timestamp, status, error, message y path.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> validaciones = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                validaciones.put(error.getField(), error.getDefaultMessage()));

        log.warn("Error de validación en path: {} - campos: {}", req.getRequestURI(), validaciones);

        Map<String, Object> body = crearBody(HttpStatus.BAD_REQUEST, "Error de validación",
                "Existen campos inválidos o incompletos", req.getRequestURI());
        body.put("validaciones", validaciones);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException ex, HttpServletRequest req) {
        log.warn("Recurso no encontrado: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(crearBody(HttpStatus.NOT_FOUND, "Recurso no encontrado",
                        ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarArgumentoInvalido(
            IllegalArgumentException ex, HttpServletRequest req) {
        log.warn("Argumento inválido: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearBody(HttpStatus.BAD_REQUEST, "Solicitud inválida",
                        ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> manejarServicioNoDisponible(
            IllegalStateException ex, HttpServletRequest req) {
        log.error("Servicio externo no disponible: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(crearBody(HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable",
                        ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> manejarTipoParametroInvalido(
            MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String mensaje = "El parámetro '" + ex.getName() + "' tiene un valor inválido";
        log.warn("Tipo de parámetro inválido: {} - path: {}", mensaje, req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearBody(HttpStatus.BAD_REQUEST, "Parámetro inválido",
                        mensaje, req.getRequestURI()));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex,
                                                                   HttpServletRequest req) {
        log.warn("Conflicto de integridad de datos - path: {}", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(crearBody(HttpStatus.CONFLICT, "Conflict",
                "Violacion de integridad de datos: registro duplicado o referencia invalida", req.getRequestURI()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpServletRequest req) {
        log.warn("Cuerpo de la peticion no legible o invalido - path: {}", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(crearBody(HttpStatus.BAD_REQUEST, "Bad Request",
                "El cuerpo de la peticion esta vacio, malformado o no es JSON valido", req.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(
            Exception ex, HttpServletRequest req) {
        log.error("Error interno no controlado - path: {}", req.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearBody(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor",
                        "Ocurrió un error inesperado en el MS Administración y Soporte",
                        req.getRequestURI()));
    }

    private Map<String, Object> crearBody(HttpStatus status, String error,
                                           String message, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        return body;
    }
}
