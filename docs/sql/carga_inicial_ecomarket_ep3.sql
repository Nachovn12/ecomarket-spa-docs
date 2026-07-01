-- ============================================================================
-- SCRIPT DE CARGA INICIAL DE DATOS - ECOMARKET SPA (EP3 DEFENSA TÉCNICA)
-- ============================================================================
-- Descripción: Este script puebla las 7 bases de datos independientes del ecosistema
-- Polyrepo (patrón Database per Service) con datos transaccionales del mercado chileno,
-- permitiendo que la colección de pruebas E2E en Postman (HU-67) se ejecute
-- sin errores en cualquier computador de laboratorio tras el encendido inicial.
--
-- Instrucciones de ejecución en el laboratorio:
-- 1. Asegúrate de haber ejecutado el script de creación de bases de datos vacías.
-- 2. Inicia los microservicios con ".\mvnw.cmd spring-boot:run" (JPA creará las tablas).
-- 3. Copia y pega este contenido en la pestaña SQL de tu phpMyAdmin (localhost/phpmyadmin)
--    y presiona "Continuar" para inyectar los datos comerciales de EcoMarket SPA.
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 1. BASE DE DATOS: bd_usuarios (MS Usuarios e Identidad - Puerto 8083)
-- ----------------------------------------------------------------------------
USE bd_usuarios;

-- Insertar usuario Administrador y Cliente de prueba para la colección Postman
INSERT IGNORE INTO usuarios (id, nombre, correo, telefono, direccion_envio, medio_pago, password, run, rol, activo, eliminado, nivel_acceso) VALUES
(1, 'Ignacio Valeria', 'admin@ecomarket.cl', '+56 9 8765 4321', 'Av. Providencia 1234, Santiago', 'TARJETA_CREDITO', 'Password1', '12345678-5', 'ADMINISTRADOR', 1, 0, 'TOTAL'),
(2, 'Benjamin Flores', 'cliente.santiago@correo.cl', '+56 9 1122 3344', 'Calle Lastarria 45, Santiago', 'DEBITO', 'Cliente123!', '11223344-K', 'CLIENTE', 1, 0, 'BASICO'),
(3, 'Benjamin Palma', 'gerente.valdivia@ecomarket.cl', '+56 9 5566 7788', 'Av. Alemania 890, Valdivia', 'TRANSFERENCIA', 'Gerente2026', '15678901-2', 'GERENTE', 1, 0, 'ALTO'),
(4, 'Benjamin Espinoza', 'logistica.norte@ecomarket.cl', '+56 9 9988 7766', 'Calle Prat 456, Antofagasta', 'TARJETA_CREDITO', 'Logistica123', '18901234-7', 'EMPLEADO', 1, 0, 'MEDIO');

-- Insertar permisos asociados al Administrador (ID 1)
INSERT IGNORE INTO usuario_permisos (usuario_id, permisos) VALUES
(1, 'LEER_TODO'),
(1, 'ESCRIBIR_TODO'),
(1, 'GESTIONAR_USUARIOS'),
(1, 'GESTIONAR_TIENDAS'),
(2, 'LEER_CATALOGO'),
(2, 'CREAR_PEDIDO'),
(3, 'LEER_REPORTES'),
(3, 'GESTIONAR_INVENTARIO');


-- ----------------------------------------------------------------------------
-- 2. BASE DE DATOS: bd_catalogo (MS Catálogo - Puerto 8084)
-- ----------------------------------------------------------------------------
USE bd_catalogo;

-- Insertar Categorías ecológicas
INSERT IGNORE INTO categorias (id, nombre, descripcion, activo) VALUES
(1, 'Embalaje Sostenible', 'Bolsas y envases biodegradables de origen vegetal para comercio', 1),
(2, 'Menaje Eco-friendly', 'Cubiertos, platos y bombillas reutilizables de bambú y acero inoxidable', 1),
(3, 'Limpieza Biodegradable', 'Detergentes y jabones libres de fosfatos y quimicos contaminantes', 1);

-- Insertar Productos del caso de estudio
INSERT IGNORE INTO productos (id, sku, nombre, descripcion, precio, categoria_id, activo, destacado, ecologico) VALUES
(1, 'ECO-BOL-001', 'Bolsa Biodegradable Mediana', 'Bolsa de compostaje elaborada de almidón de maíz resistente (100 unidades)', 4500.00, 1, 1, 1, 1),
(2, 'ECO-CUB-002', 'Set de Cubiertos de Bambú', 'Set reutilizable incluye tenedor, cuchillo, cuchara y estuche de algodón natural', 6800.00, 2, 1, 1, 1),
(3, 'ECO-DET-003', 'Detergente Líquido Ecológico 3L', 'Fórmula vegetal concentrada hipoalergénica con aroma a lavanda austral', 12500.00, 3, 1, 0, 1),
(4, 'ECO-BOM-004', 'Bombillas de Acero Inoxidable (Pack 4)', 'Bombillas metálicas lavables con cepillo limpiador incluido', 3900.00, 2, 1, 0, 1);


-- ----------------------------------------------------------------------------
-- 3. BASE DE DATOS: bd_inventario (MS Inventario y Abastecimiento - Puerto 8085)
-- ----------------------------------------------------------------------------
USE bd_inventario;

-- Insertar Sucursales y Tiendas físicas del caso
INSERT IGNORE INTO sucursales (id, nombre, direccion, ciudad, region, activo) VALUES
(1, 'Tienda Barrio Lastarria', 'José Victorino Lastarria 120', 'Santiago', 'Región Metropolitana', 1),
(2, 'Tienda Eco Valdivia', 'Av. Los Lingues 450', 'Valdivia', 'Región de Los Ríos', 1),
(3, 'Tienda Antofagasta Puerto', 'Av. Balmaceda 2340', 'Antofagasta', 'Región de Antofagasta', 1);

-- Insertar Inventario y existencias por producto y sucursal
INSERT IGNORE INTO inventarios (id, producto_id, sucursal_id, stock, stock_minimo, precio_unitario) VALUES
(1, 1, 1, 500, 50, 4500.00),
(2, 2, 1, 150, 20, 6800.00),
(3, 3, 1, 80, 15, 12500.00),
(4, 1, 2, 300, 40, 4500.00),
(5, 2, 2, 120, 20, 6800.00),
(6, 1, 3, 250, 30, 4500.00);


-- ----------------------------------------------------------------------------
-- 4. BASE DE DATOS: bd_ventas (MS Pedidos y Ventas - Puerto 8086)
-- ----------------------------------------------------------------------------
USE bd_ventas;

-- Insertar Carrito de prueba inicial
INSERT IGNORE INTO carritos (id, cliente_id, run_cliente, fecha_creacion, total, estado) VALUES
(1, 2, '11223344-K', NOW(), 11300.00, 'ACTIVO');

-- Insertar Pedido histórico de demostración
INSERT IGNORE INTO pedidos (id, cliente_id, run_cliente, fecha_pedido, total, estado, direccion_despacho) VALUES
(1, 2, '11223344-K', NOW(), 15800.00, 'CONFIRMADO', 'Calle Lastarria 45, Santiago');


-- ----------------------------------------------------------------------------
-- 5. BASE DE DATOS: bd_logistica (MS Logística de Envíos - Puerto 8087)
-- ----------------------------------------------------------------------------
USE bd_logistica;

-- Insertar Proveedores de Transporte
INSERT IGNORE INTO proveedores (id, nombre, rut, telefono, activo) VALUES
(1, 'EcoDespachos Chile Express', '76543210-8', '+56 2 2345 6789', 1),
(2, 'Logística Verde Starken', '78901234-5', '+56 2 2987 6543', 1);

-- Insertar Despacho en curso para consultar seguimiento
INSERT IGNORE INTO envios (id, pedido_id, proveedor_id, codigo_tracking, estado, fecha_despacho, direccion_destino, ciudad_destino, costo) VALUES
(1, 1, 1, 'TRK-ECO-889900', 'EN_TRANSITO', NOW(), 'Calle Lastarria 45', 'Santiago', 3500.00);


-- ----------------------------------------------------------------------------
-- 6. BASE DE DATOS: bd_admin (MS Administración y Soporte - Puerto 8088)
-- ----------------------------------------------------------------------------
USE bd_admin;

-- Insertar Tiendas administrativas del sistema
INSERT IGNORE INTO tiendas (id, nombre, direccion, ciudad, region, telefono, activo, gerente) VALUES
(1, 'Tienda Barrio Lastarria', 'José Victorino Lastarria 120', 'Santiago', 'Región Metropolitana', '+56 2 2633 4455', 1, 'Ignacio Valeria'),
(2, 'Tienda Eco Valdivia', 'Av. Los Lingues 450', 'Valdivia', 'Región de Los Ríos', '+56 63 221 3344', 1, 'Benjamin Palma'),
(3, 'Tienda Antofagasta Puerto', 'Av. Balmaceda 2340', 'Antofagasta', 'Región de Antofagasta', '+56 55 234 5566', 1, 'Benjamin Espinoza');

-- Insertar Ticket de Soporte Técnico inicial
INSERT IGNORE INTO tickets_soporte (id, asunto, descripcion, prioridad, estado, fecha_creacion, usuario_creador, tienda_id) VALUES
(1, 'Solicitud urgente reabastecimiento bolsas', 'Se requiere reposicion urgente de bolsas biodegradables medianas en sucursal Lastarria', 'ALTA', 'ABIERTO', NOW(), 'admin@ecomarket.cl', 1);

-- Insertar Métricas de monitoreo
INSERT IGNORE INTO metricas (id, nombre_metrica, valor, fecha_registro, categoria) VALUES
(1, 'TIEMPO_RESPUESTA_GATEWAY_MS', '45.2', NOW(), 'RENDIMIENTO'),
(2, 'TRANSACCIONES_EXITOSAS_HORA', '128', NOW(), 'VENTAS');


-- ----------------------------------------------------------------------------
-- 7. BASE DE DATOS: bd_reportes (MS Reportes y KPIs - Puerto 8089)
-- ----------------------------------------------------------------------------
USE bd_reportes;

-- Insertar Indicadores KPI estratégicos de EcoMarket
INSERT IGNORE INTO kpis (id, nombre_kpi, valor_actual, meta, unidad, periodo) VALUES
(1, 'Tasa de Satisfaccion al Cliente (NPS)', 88.50, 85.00, '%', '2026-Q3'),
(2, 'Tiempo Promedio Despacho Urbano', 22.40, 24.00, 'Horas', '2026-Q3'),
(3, 'Reduccion Huella de Carbono Embalaje', 42.00, 35.00, '%', '2026-Q3'),
(4, 'Disponibilidad de Stock en Tiendas', 96.20, 95.00, '%', '2026-Q3');

-- Insertar Reporte consolidado
INSERT IGNORE INTO reportes (id, titulo, tipo, fecha_generacion, resumen) VALUES
(1, 'Informe de Ventas Sostenibles Q3', 'VENTAS_TRIMESTRAL', NOW(), 'Crecimiento del 18% en productos de embalaje biodegradable en sucursales Santiago y Valdivia.');
