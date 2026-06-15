package com.ecomarket.reportes.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos de reporte generados por el sistema", allowableValues = {"VENTAS", "INVENTARIO", "RENDIMIENTO_TIENDA", "KPI"})
public enum TipoReporte {
    VENTAS, INVENTARIO, RENDIMIENTO_TIENDA, KPI
}
