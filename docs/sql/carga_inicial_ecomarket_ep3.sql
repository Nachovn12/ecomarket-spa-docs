-- ============================================================================
-- SCRIPT DE CARGA INICIAL DE DATOS - ECOMARKET SPA (EP3 DEFENSA TÉCNICA)
-- ============================================================================
-- Descripción: Este script puebla las bases de datos principales del ecosistema
-- Polyrepo (patrón Database per Service) con los usuarios, permisos, categorías
-- y productos comerciales de EcoMarket SPA, permitiendo que la colección de
-- pruebas E2E en Postman (HU-67) y el inicio de sesión se ejecuten en verde
-- sin errores de datos faltantes tras el encendido en el laboratorio.
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 1. BASE DE DATOS: bd_usuarios (MS Usuarios e Identidad - Puerto 8083)
-- ----------------------------------------------------------------------------
USE bd_usuarios;

-- Insertar Usuarios y Personal de la Empresa
INSERT IGNORE INTO usuarios (id, nombre, correo, telefono, direccion_envio, medio_pago, password, run, rol, activo, eliminado, nivel_acceso) VALUES
(1, 'Administrador EcoMarket', 'admin@ecomarket.cl', '+56 2 2345 6789', 'Casa Matriz Duoc UC, Santiago', 'TARJETA_CREDITO', 'Password1', '12345678-5', 'ADMINISTRADOR', 1, 0, 'TOTAL'),
(2, 'Ignacio Valeria', 'ignacio.valeria@ecomarket.cl', '+56 9 8877 6655', 'Av. Apoquindo 4500, Las Condes, Santiago', 'DEBITO', 'Ignacio2026', '19876543-K', 'CLIENTE', 1, 0, 'BASICO'),
(3, 'Benjamin Palma', 'gerente.valdivia@ecomarket.cl', '+56 9 5566 7788', 'Av. Alemania 890, Valdivia', 'TRANSFERENCIA', 'Gerente2026', '15678901-2', 'GERENTE', 1, 0, 'ALTO'),
(4, 'Benjamin Espinoza', 'logistica.norte@ecomarket.cl', '+56 9 9988 7766', 'Calle Prat 456, Antofagasta', 'TARJETA_CREDITO', 'Logistica123', '18901234-7', 'EMPLEADO', 1, 0, 'MEDIO');

-- Insertar permisos asociados a los usuarios (ElementCollection)
INSERT IGNORE INTO usuario_permisos (usuario_id, permiso) VALUES
(1, 'LEER_TODO'),
(1, 'ESCRIBIR_TODO'),
(1, 'GESTIONAR_USUARIOS'),
(1, 'GESTIONAR_TIENDAS'),
(2, 'LEER_CATALOGO'),
(2, 'CREAR_PEDIDO'),
(3, 'LEER_REPORTES'),
(3, 'GESTIONAR_INVENTARIO');


-- ----------------------------------------------------------------------------
-- 2. BASE DE DATOS: bd_catalogo (MS Catálogo de Productos - Puerto 8084)
-- ----------------------------------------------------------------------------
USE bd_catalogo;

-- Insertar Categorías ecológicas
INSERT IGNORE INTO categorias (id_categoria, nombre, descripcion, estado) VALUES
(1, 'Embalaje Sostenible', 'Bolsas y envases biodegradables de origen vegetal para comercio', 'ACTIVA'),
(2, 'Menaje Eco-friendly', 'Cubiertos, platos y bombillas reutilizables de bambú y acero inoxidable', 'ACTIVA'),
(3, 'Limpieza Biodegradable', 'Detergentes y jabones libres de fosfatos y quimicos contaminantes', 'ACTIVA');

-- Insertar Productos ecológicos chilenos
INSERT IGNORE INTO productos (id_producto, sku, nombre, precio, descripcion, descripcion_ecologica, estado, id_categoria) VALUES
(1, 'ECO-BOL-001', 'Bolsa Biodegradable Mediana', 4500.00, 'Bolsa de compostaje elaborada de almidón de maíz resistente (100 unidades)', '100% biodegradable en compost domiciliario en 180 días', 'PUBLICADO', 1),
(2, 'ECO-CUB-002', 'Set de Cubiertos de Bambú', 6800.00, 'Set reutilizable incluye tenedor, cuchillo, cuchara y estuche de algodón natural', 'Fabricado de bambú orgánico chileno de crecimiento rápido sin pesticidas', 'PUBLICADO', 2),
(3, 'ECO-DET-003', 'Detergente Líquido Ecológico 3L', 12500.00, 'Fórmula vegetal concentrada hipoalergénica con aroma a lavanda austral', 'Fórmula vegetal 100% biodegradable libre de fosfatos y microplásticos', 'PUBLICADO', 3),
(4, 'ECO-BOM-004', 'Bombillas de Acero Inoxidable (Pack 4)', 3900.00, 'Bombillas metálicas lavables con cepillo limpiador incluido', 'Reemplaza miles de bombillas plásticas de un solo uso durante una vida útil infinita', 'PUBLICADO', 2);
