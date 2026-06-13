package com.ecomarket.reportes.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Formatos de exportacion disponibles para los reportes", allowableValues = {"PDF", "EXCEL", "CSV"})
public enum FormatoExportacion {
    PDF, EXCEL, CSV
}
