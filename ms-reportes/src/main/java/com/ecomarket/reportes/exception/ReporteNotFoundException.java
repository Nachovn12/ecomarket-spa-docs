package com.ecomarket.reportes.exception;

/**
 * Excepción lanzada cuando un reporte o kpi no es encontrado.
 * Se mapea a HTTP 404 not found en el globalexceptionhandler.
 */
public class ReporteNotFoundException extends RuntimeException {

    public ReporteNotFoundException(String message) {
        super(message);
    }
}
