package com.ecomarket.reportes.exception;

/**
 * Excepción lanzada cuando un Reporte o KPI no es encontrado.
 * Se mapea a HTTP 404 Not Found en el GlobalExceptionHandler.
 */
public class ReporteNotFoundException extends RuntimeException {

    public ReporteNotFoundException(String message) {
        super(message);
    }
}
