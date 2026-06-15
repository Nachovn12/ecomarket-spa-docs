package com.ecomarket.reportes.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos de KPI calculados en el sistema", allowableValues = {"VENTAS_TOTALES", "STOCK_BAJO", "ROTACION_INVENTARIO", "PEDIDOS_ENTREGADOS", "RENDIMIENTO_TIENDA"})
public enum TipoKPI {
    VENTAS_TOTALES, STOCK_BAJO, ROTACION_INVENTARIO, PEDIDOS_ENTREGADOS, RENDIMIENTO_TIENDA
}
