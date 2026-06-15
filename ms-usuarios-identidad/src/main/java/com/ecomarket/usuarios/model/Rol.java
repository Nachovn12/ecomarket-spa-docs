package com.ecomarket.usuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum que representa los roles disponibles en el sistema.
 * - CLIENTE: Usuario registrado en la tienda online.
 * - EMPLEADO: Personal operativo de tienda.
 * - GERENTE: Responsable de una tienda o area.
 * - ADMINISTRADOR: Acceso total al sistema.
 */
@Schema(description = "Roles disponibles en el sistema", allowableValues = {"CLIENTE", "EMPLEADO", "GERENTE", "ADMINISTRADOR"})
public enum Rol {
    CLIENTE,
    EMPLEADO,
    GERENTE,
    ADMINISTRADOR
}
