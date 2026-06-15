package com.ecomarket.reportes.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador centralizado de excepciones para ms-reportes.
 * ReporteNotFoundException → 404, reporteexception → 400, resto → 500.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ReporteNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ReporteNotFoundException ex, HttpServletRequest req) {
        log.warn("Reporte/KPI no encontrado: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ReporteException.class)
    public ResponseEntity<Map<String, Object>> handleReporteException(
            ReporteException ex, HttpServletRequest req) {
        log.warn("Error de validación de reporte: {} - path: {}", ex.getMessage(), req.getRequestURI());
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errores.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.warn("Error de validación en path: {} - campos: {}", req.getRequestURI(), errores);
        Map<String, Object> body = buildBodyMap(HttpStatus.BAD_REQUEST, "Validation Error",
                "Error de validación en los campos enviados", req.getRequestURI());
        body.put("validaciones", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex,
                                                                   HttpServletRequest req) {
        log.warn("Conflicto de integridad de datos - path: {}", req.getRequestURI());
        return buildResponse(HttpStatus.CONFLICT, "Conflict",
                "Violacion de integridad de datos: registro duplicado o referencia invalida", req.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpServletRequest req) {
        log.warn("Cuerpo de la peticion no legible o invalido - path: {}", req.getRequestURI());
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request",
                "El cuerpo de la peticion esta vacio, malformado o no es JSON valido", req.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(
            Exception ex, HttpServletRequest req) {
        log.error("Error interno no controlado - path: {}", req.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "Error interno en el servidor", req.getRequestURI());
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error,
                                                               String message, String path) {
        return ResponseEntity.status(status).body(buildBodyMap(status, error, message, path));
    }

    private Map<String, Object> buildBodyMap(HttpStatus status, String error,
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
