-- ============================================================================
-- SCRIPT DE CARGA INICIAL DE DATOS - ECOMARKET SPA (EP3 DEFENSA TÉCNICA)
-- ============================================================================
-- Descripción: Este script puebla las 7 bases de datos independientes del ecosistema
-- Polyrepo (patrón Database per Service) con datos 100% compatibles y verificados
-- contra las entidades JPA (@Table y @Column) de cada microservicio, permitiendo
-- que todas las tablas tengan registros comerciales para mostrar a la comisión.
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 1. BASE DE DATOS: bd_usuarios (MS Usuarios e Identidad - Puerto 8083)
-- ----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_usuarios;
USE bd_usuarios;

-- Insertar Usuarios y Personal de la Empresa
INSERT IGNORE INTO usuarios (id, nombre, correo, telefono, direccion_envio, medio_pago, password, run, rol, activo, eliminado, nivel_acceso, fecha_registro) VALUES
(1, 'Administrador EcoMarket', 'admin@ecomarket.cl', '+56 2 2345 6789', 'Casa Matriz Duoc UC, Santiago', 'TARJETA_CREDITO', 'Password1', '12345678-5', 'ADMINISTRADOR', 1, 0, 'TOTAL', NOW()),
(2, 'Ignacio Valeria', 'ignacio.valeria@ecomarket.cl', '+56 9 8877 6655', 'Av. Apoquindo 4500, Las Condes, Santiago', 'DEBITO', 'Ignacio2026', '19876543-K', 'CLIENTE', 1, 0, 'BASICO', NOW()),
(3, 'Benjamin Palma', 'gerente.valdivia@ecomarket.cl', '+56 9 5566 7788', 'Av. Alemania 890, Valdivia', 'TRANSFERENCIA', 'Gerente2026', '15678901-2', 'GERENTE', 1, 0, 'ALTO', NOW()),
(4, 'Benjamin Espinoza', 'logistica.norte@ecomarket.cl', '+56 9 9988 7766', 'Calle Prat 456, Antofagasta', 'TARJETA_CREDITO', 'Logistica123', '18901234-7', 'EMPLEADO', 1, 0, 'MEDIO', NOW());

-- Insertar permisos asociados a los usuarios
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
CREATE DATABASE IF NOT EXISTS bd_catalogo;
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


-- ----------------------------------------------------------------------------
-- 3. BASE DE DATOS: bd_inventario (MS Inventario y Abastecimiento - Puerto 8085)
-- ----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_inventario;
USE bd_inventario;

-- Insertar Proveedores de Abastecimiento
INSERT IGNORE INTO proveedores (id, nombre, contacto, email, telefono) VALUES
(1, 'EcoDistribuidora SpA', 'Maria Lopez', 'ventas@ecodistribuidora.cl', '+56 2 2345 6789'),
(2, 'Bambú Austral Chile', 'Carlos Silva', 'contacto@bambuaustral.cl', '+56 63 221 3344');

-- Insertar registros de Inventario por producto
INSERT IGNORE INTO inventario (id, nombre_producto, cantidad_disponible, cantidad_minima, categoria) VALUES
(1, 'Bolsa Biodegradable Mediana', 500, 50, 'Embalaje Sostenible'),
(2, 'Set de Cubiertos de Bambú', 150, 20, 'Menaje Eco-friendly'),
(3, 'Detergente Líquido Ecológico 3L', 80, 15, 'Limpieza Biodegradable'),
(4, 'Bombillas de Acero Inoxidable (Pack 4)', 300, 40, 'Menaje Eco-friendly');


-- ----------------------------------------------------------------------------
-- 4. BASE DE DATOS: bd_ventas (MS Pedidos y Ventas - Puerto 8086)
-- ----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_ventas;
USE bd_ventas;

-- Insertar Cupones de Descuento
INSERT IGNORE INTO cupones_descuento (id_cupon, codigo, tipo_descuento, valor_descuento, monto_minimo, fecha_vencimiento, activo) VALUES
(1, 'ECO10', 'PORCENTAJE', 10.0, 5000.0, '2026-12-31', 1),
(2, 'BIENVENIDA20', 'PORCENTAJE', 20.0, 10000.0, '2026-12-31', 1),
(3, 'ENVIO5000', 'MONTO_FIJO', 5000.0, 15000.0, '2026-12-31', 1);


-- ----------------------------------------------------------------------------
-- 5. BASE DE DATOS: bd_logistica (MS Logística de Envíos - Puerto 8087)
-- ----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_logistica;
USE bd_logistica;

-- Insertar Proveedores Logísticos
INSERT IGNORE INTO proveedores (id, razon_social, rut, contacto, email, telefono, tipo_proveedor, cobertura, activo, fecha_registro, plazo_despacho_horas) VALUES
(1, 'Transportes Eco SpA', '76.123.456-7', 'Juan Perez', 'contacto@transporteseco.cl', '+56 2 2345 6789', 'TRANSPORTE', 'NACIONAL', 1, NOW(), 24),
(2, 'Repartos Sostenibles Chile', '78.901.234-5', 'Maria Silva', 'ayuda@repartos.cl', '+56 2 2987 6543', 'REPARTO', 'REGIONAL', 1, NOW(), 12);


-- ----------------------------------------------------------------------------
-- 6. BASE DE DATOS: bd_admin (MS Administración y Soporte - Puerto 8088)
-- ----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_admin;
USE bd_admin;

-- Insertar Tiendas Físicas
INSERT IGNORE INTO tiendas (id_tienda, nombre, ciudad, horario_apertura, horario_cierre, politicas_locales, activa, fecha_creacion) VALUES
(1, 'Tienda Barrio Lastarria', 'Santiago', '09:00:00', '20:00:00', 'Garantía legal 6 meses, reciclaje en punto verde.', 1, NOW()),
(2, 'Tienda Eco Valdivia', 'Valdivia', '10:00:00', '19:30:00', 'Atención continuada, despacho gratis sobre $20.000.', 1, NOW()),
(3, 'Tienda Antofagasta Puerto', 'Antofagasta', '09:30:00', '20:30:00', 'Punto de retiro Pick-up express 2 horas.', 1, NOW());


-- ----------------------------------------------------------------------------
-- 7. BASE DE DATOS: bd_reportes (MS Reportes y KPIs - Puerto 8090)
-- ----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_reportes;
USE bd_reportes;

-- Insertar Indicadores KPI del Negocio
INSERT IGNORE INTO indicadores_kpi (id, tipo, valor, descripcion, fecha_calculo) VALUES
(1, 'VENTAS_TOTALES', 153200.0, 'Total de ventas consolidadas Q3 2026', NOW()),
(2, 'STOCK_BAJO', 4.0, 'Cantidad de productos en nivel crítico de reposición', NOW()),
(3, 'PEDIDOS_ENTREGADOS', 128.0, 'Total de despachos exitosos en el período', NOW()),
(4, 'RENDIMIENTO_TIENDA', 96.5, 'NPS de satisfacción general en sucursal Lastarria', NOW());
