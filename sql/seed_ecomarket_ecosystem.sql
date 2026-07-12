-- =========================================================================================
-- SCRIPT MAESTRO DE CARGA DE DATOS MOCK (ECOMARKET SPA - POLYREPO)
-- Asignatura: Desarrollo Fullstack I / Arquitectura de Microservicios (DSY1103)
-- Descripción: Semillado completo y coherente para las 7 bases de datos MySQL.
-- Garantiza que IDs de clientes, productos, pedidos, envíos, tiendas y reportes coincidan.
-- =========================================================================================

SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------------------------------------
-- 1. BASE DE DATOS: bd_usuarios
-- -----------------------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_usuarios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bd_usuarios;

INSERT IGNORE INTO usuarios (id, activo, correo, direccion_envio, eliminado, fecha_modificacion_acceso, fecha_registro, medio_pago, modificado_por, nivel_acceso, nombre, password, rol, run, telefono)
VALUES
(1, 1, 'admin@ecomarket.cl', 'Casa Matriz Duoc UC, Santiago', 0, NULL, NOW(), 'TARJETA_CREDITO', NULL, 'TOTAL', 'Administrador EcoMarket', 'Password1', 'ADMINISTRADOR', '12345678-5', '+56 2 2345 6789'),
(2, 1, 'ignacio.valeria@ecomarket.cl', 'Av. Apoquindo 4500, Las Condes, Santiago', 0, NULL, NOW(), 'DEBITO', NULL, 'BASICO', 'Ignacio Valeria', 'Ignacio2026', 'CLIENTE', '19876543-K', '+56 9 8877 6655'),
(3, 1, 'gerente.valdivia@ecomarket.cl', 'Av. Alemania 890, Valdivia', 0, NULL, NOW(), 'TRANSFERENCIA', NULL, 'ALTO', 'Benjamin Palma', 'Gerente2026', 'GERENTE', '15678901-2', '+56 9 5566 7788'),
(4, 1, 'logistica.norte@ecomarket.cl', 'Calle Prat 456, Antofagasta', 0, NULL, NOW(), 'TARJETA_CREDITO', NULL, 'MEDIO', 'Benjamin Espinoza', 'Logistica123', 'EMPLEADO', '18901234-7', '+56 9 9988 7766');

INSERT IGNORE INTO usuario_permisos (usuario_id, permiso)
VALUES
(1, 'LEER_TODO'),
(1, 'ESCRIBIR_TODO'),
(1, 'GESTIONAR_USUARIOS'),
(1, 'GESTIONAR_TIENDAS'),
(2, 'LEER_CATALOGO'),
(2, 'CREAR_PEDIDO'),
(3, 'LEER_REPORTES'),
(3, 'GESTIONAR_INVENTARIO');

-- -----------------------------------------------------------------------------------------
-- 2. BASE DE DATOS: bd_catalogo
-- -----------------------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_catalogo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bd_catalogo;

INSERT IGNORE INTO categorias (id_categoria, descripcion, estado, nombre)
VALUES
(1, 'Embalajes biodegradables compostables', 'ACTIVA', 'Embalaje Sostenible'),
(2, 'Menaje reutilizable de bambú o acero', 'ACTIVA', 'Menaje Eco-friendly'),
(3, 'Productos de limpieza vegetales libres de fosfatos', 'ACTIVA', 'Limpieza Biodegradable');

INSERT IGNORE INTO productos (id_producto, descripcion, descripcion_ecologica, estado, fecha_actualizacion, fecha_creacion, nombre, precio, sku, id_categoria)
VALUES
(1, 'Bolsa de compostaje elaborada de almidón de maíz resistente (100 unidades)', '100% biodegradable en compost domiciliario en 180 días', 'PUBLICADO', NOW(), NOW(), 'Bolsa Biodegradable Mediana', 4500, 'ECO-BOL-001', 1),
(2, 'Set reutilizable incluye tenedor, cuchillo, cuchara y estuche de algodón natural', 'Fabricado de bambú orgánico chileno de crecimiento rápido sin pesticidas', 'PUBLICADO', NOW(), NOW(), 'Set de Cubiertos de Bambú', 6800, 'ECO-CUB-002', 2),
(3, 'Fórmula vegetal concentrada hipoalergénica con aroma a lavanda austral', 'Fórmula vegetal 100% biodegradable libre de fosfatos y microplásticos', 'PUBLICADO', NOW(), NOW(), 'Detergente Líquido Ecológico 3L', 12500, 'ECO-DET-003', 3),
(4, 'Bombillas metálicas lavables con cepillo limpiador incluido', 'Reemplaza miles de bombillas plásticas de un solo uso durante una vida útil infinita', 'PUBLICADO', NOW(), NOW(), 'Bombillas de Acero Inoxidable (Pack 4)', 3900, 'ECO-BOM-004', 2);

INSERT IGNORE INTO resenas (id_resena, calificacion, comentario, estado, fecha_creacion, id_cliente, id_producto)
VALUES
(1, 5, 'Excelente calidad, las bolsas se compostaron sin problema en casa.', 'PUBLICADA', NOW(), 2, 1),
(2, 5, 'El set de cubiertos es muy práctico y estético. Lo llevo al trabajo siempre.', 'PUBLICADA', NOW(), 2, 2);

-- -----------------------------------------------------------------------------------------
-- 3. BASE DE DATOS: bd_inventario
-- -----------------------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_inventario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bd_inventario;

INSERT IGNORE INTO inventario (id, cantidad_disponible, cantidad_minima, categoria, nombre_producto)
VALUES
(1, 500, 50, 'Embalaje Sostenible', 'Bolsa Biodegradable Mediana'),
(2, 150, 20, 'Menaje Eco-friendly', 'Set de Cubiertos de Bambú'),
(3, 80, 15, 'Limpieza Biodegradable', 'Detergente Líquido Ecológico 3L'),
(4, 300, 40, 'Menaje Eco-friendly', 'Bombillas de Acero Inoxidable (Pack 4)');

INSERT IGNORE INTO productos (id, categoria, nombre, precio, sku, stock, stock_minimo, sucursal)
VALUES
(1, 'Embalaje Sostenible', 'Bolsa Biodegradable Mediana', 4500, 'ECO-BOL-001', 500, 50, 'Bodega Lastarria - Santiago'),
(2, 'Menaje Eco-friendly', 'Set de Cubiertos de Bambú', 6800, 'ECO-CUB-002', 150, 20, 'Bodega Valdivia'),
(3, 'Limpieza Biodegradable', 'Detergente Líquido Ecológico 3L', 12500, 'ECO-DET-003', 80, 15, 'Bodega Antofagasta'),
(4, 'Menaje Eco-friendly', 'Bombillas de Acero Inoxidable (Pack 4)', 3900, 'ECO-BOM-004', 300, 40, 'Bodega Lastarria - Santiago');

INSERT IGNORE INTO proveedores (id, contacto, email, nombre, telefono)
VALUES
(1, 'Carlos Mendoza', 'contacto@bioempaques.cl', 'BioEmpaques Chile S.A.', '+56 2 2888 9900'),
(2, 'Andrea Soto', 'ventas@ecosolutions.cl', 'EcoSolutions SpA', '+56 9 7788 9900');

INSERT IGNORE INTO ajustes_stock (id, cantidad_anterior, cantidad_nueva, fecha_ajuste, motivo, usuario_responsable, producto_id)
VALUES
(1, 480, 500, NOW(), 'Ajuste periódico por recuento en bodega central', 'Benjamin Palma', 1);

INSERT IGNORE INTO pedidos_reabastecimiento (id, cantidad, creado_por, estado, fecha_creacion, motivo_rechazo, producto_id)
VALUES
(1, 200, 'Benjamin Palma', 'RECIBIDO', NOW(), NULL, 2);

INSERT IGNORE INTO recepciones_mercancia (id, cantidad_danada, cantidad_recibida, diferencias, estado, fecha_recepcion, registrado_por, pedido_id)
VALUES
(1, 0, 200, 'Recepción completa sin novedades', 'CONFORME', NOW(), 'Benjamin Palma', 1);

-- -----------------------------------------------------------------------------------------
-- 4. BASE DE DATOS: bd_ventas
-- -----------------------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_ventas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bd_ventas;

INSERT IGNORE INTO cupones_descuento (id_cupon, activo, codigo, fecha_vencimiento, monto_minimo, tipo_descuento, valor_descuento)
VALUES
(1, 1, 'BIENVENIDA10', '2026-12-31', 5000, 'PORCENTAJE', 10.0),
(2, 1, 'ECOCYBER20', '2026-12-31', 10000, 'PORCENTAJE', 20.0);

INSERT IGNORE INTO carritos_compra (id_carrito, codigo_cupon_aplicado, descuento_aplicado, estado, fecha_actualizacion, fecha_creacion, id_cliente, subtotal, total)
VALUES
(1, NULL, 0, 'ACTIVO', NOW(), NOW(), 2, 11300, 11300);

INSERT IGNORE INTO items_carrito (id_item, cantidad, id_producto, nombre_producto, precio_unitario, subtotal, id_carrito)
VALUES
(1, 1, 1, 'Bolsa Biodegradable Mediana', 4500, 4500, 1),
(2, 1, 2, 'Set de Cubiertos de Bambú', 6800, 6800, 1);

INSERT IGNORE INTO pedidos (id_pedido, descuento, direccion_entrega, estado, fecha_actualizacion, fecha_creacion, id_cliente, iva, metodo_pago, observaciones, subtotal, total)
VALUES
(101, 0, 'Av. Apoquindo 4500, Las Condes, Santiago', 'CONFIRMADO', NOW(), NOW(), 2, 2147, 'TARJETA', 'Entrega en portería', 11300, 13447),
(102, 0, 'Av. Apoquindo 4500, Las Condes, Santiago', 'ENTREGADO', NOW(), NOW(), 2, 2375, 'TRANSFERENCIA', 'Empaque sin plástico', 12500, 14875);

INSERT IGNORE INTO detalles_pedido (id_detalle, cantidad, id_producto, nombre_producto, precio_unitario, subtotal, id_pedido)
VALUES
(1, 1, 1, 'Bolsa Biodegradable Mediana', 4500, 4500, 101),
(2, 1, 2, 'Set de Cubiertos de Bambú', 6800, 6800, 101),
(3, 1, 3, 'Detergente Líquido Ecológico 3L', 12500, 12500, 102);

INSERT IGNORE INTO historial_pedidos (id_historial, descripcion, estado_anterior, estado_nuevo, fecha_cambio, id_pedido)
VALUES
(1, 'Pedido creado y confirmado', 'PENDIENTE', 'CONFIRMADO', NOW(), 101),
(2, 'Pedido despachado y entregado', 'ENVIADO', 'ENTREGADO', NOW(), 102);

INSERT IGNORE INTO ventas (id_venta, descuento, fecha_venta, id_cliente, id_pedido, iva, metodo_pago, observaciones, subtotal, total)
VALUES
(201, 0, NOW(), 2, 101, 2147, 'TARJETA', 'Venta confirmada online', 11300, 13447),
(202, 0, NOW(), 2, 102, 2375, 'TRANSFERENCIA', 'Venta confirmada online', 12500, 14875);

INSERT IGNORE INTO facturas (id_factura, estado, fecha_emision, folio, id_cliente, id_venta, iva, razon_social, rut_cliente, subtotal, total)
VALUES
(301, 'EMITIDA', NOW(), 10001, 2, 201, 2147, 'Ignacio Valeria', '19876543-K', 11300, 13447),
(302, 'EMITIDA', NOW(), 10002, 2, 202, 2375, 'Ignacio Valeria', '19876543-K', 12500, 14875);

INSERT IGNORE INTO devoluciones (id_devolucion, estado, fecha_creacion, id_cliente, id_pedido, id_venta, motivo)
VALUES
(1, 'COMPLETADA', NOW(), 2, 101, 201, 'Cambio de talla o formato solicitado en tienda');

INSERT IGNORE INTO reclamaciones (id_reclamacion, descripcion, estado, fecha_creacion, id_cliente, id_pedido, id_venta, motivo)
VALUES
(1, 'Solicitud de información sobre despacho ecológico', 'RESUELTO', NOW(), 2, 102, 202, 'Consulta logística');

-- -----------------------------------------------------------------------------------------
-- 5. BASE DE DATOS: bd_logistica
-- -----------------------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_logistica CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bd_logistica;

INSERT IGNORE INTO proveedores (id, activo, cobertura, contacto, email, fecha_registro, plazo_despacho_horas, razon_social, rut, telefono, tipo_proveedor)
VALUES
(1, 1, 'Región Metropolitana', 'Juan Pérez', 'despachos@biorutas.cl', NOW(), 24, 'BioRutas Express SpA', '76.999.888-7', '+56 9 8811 2233', 'EXPRESS'),
(2, 1, 'Nacional', 'Lorena Tapia', 'logistica@ecoenvios.cl', NOW(), 48, 'EcoEnvíos Chile S.A.', '77.888.777-6', '+56 2 2444 5566', 'ESTANDAR');

INSERT IGNORE INTO rutas_entrega (id, estado, fecha_creacion)
VALUES
(1, 'PLANIFICADA', NOW()),
(2, 'FINALIZADA', NOW());

INSERT IGNORE INTO envios (id, destino, estado, fecha_actualizacion, fecha_creacion, fecha_estimada_entrega, id_pedido, motivo_incidencia, observacion, origen, proveedor_id, ruta_entrega_id, ubicacion_actual)
VALUES
(501, 'Av. Apoquindo 4500, Las Condes, Santiago', 'EN_CAMINO', NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 101, NULL, 'Despacho en ruta ecológica', 'Bodega Central EcoMarket - Quilicura', 1, 1, 'En reparto por Las Condes'),
(502, 'Av. Apoquindo 4500, Las Condes, Santiago', 'ENTREGADO', NOW(), NOW(), NOW(), 102, NULL, 'Entregado a cliente en portería', 'Bodega Central EcoMarket - Quilicura', 1, 2, 'Entregado en destino');

INSERT IGNORE INTO seguimientos_envio (id, actualizado_por, estado, fecha_registro, observacion, ubicacion, envio_id)
VALUES
(1, 'Chofer BioRutas #12', 'EN_CAMINO', NOW(), 'Saliendo del centro de distribución', 'Quilicura', 501),
(2, 'Chofer BioRutas #12', 'ENTREGADO', NOW(), 'Recepción firmada por cliente', 'Las Condes', 502);

-- -----------------------------------------------------------------------------------------
-- 6. BASE DE DATOS: bd_admin
-- -----------------------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_admin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bd_admin;

INSERT IGNORE INTO tiendas (id_tienda, activa, ciudad, fecha_actualizacion, fecha_creacion, horario_apertura, horario_cierre, nombre, politicas_locales)
VALUES
(1, 1, 'Santiago', NOW(), NOW(), '09:00:00', '20:00:00', 'Tienda Flagship EcoMarket Lastarria', 'Punto verde central'),
(2, 1, 'Valdivia', NOW(), NOW(), '10:00:00', '19:30:00', 'Tienda EcoMarket Valdivia', 'Compostaje comunitario y devolución local'),
(3, 1, 'Antofagasta', NOW(), NOW(), '10:00:00', '21:00:00', 'Tienda EcoMarket Antofagasta', 'Políticas locales de reciclaje de agua');

INSERT IGNORE INTO tickets_soporte (id_ticket, asunto, correo_contacto, descripcion, estado, fecha_actualizacion, fecha_creacion, nombre_contacto, prioridad)
VALUES
(1, 'Consulta sobre abono orgánico', 'ignacio.valeria@ecomarket.cl', 'Duda respecto a las instrucciones de compostaje del producto ECO-BOL-001', 'ABIERTO', NOW(), NOW(), 'Ignacio Valeria', 'MEDIA');

INSERT IGNORE INTO respuestas_soporte (id_respuesta, fecha_respuesta, mensaje, respondido_por, id_ticket)
VALUES
(1, NOW(), 'Estimado Ignacio, la bolsa de compostaje es apta para residuos vegetales domiciliarios y se degrada en 180 días.', 'Administrador EcoMarket', 1);

INSERT IGNORE INTO alertas_sistema (id_alerta, descripcion, fecha_generacion, fecha_resolucion, microservicio, resuelta, tipo_alerta)
VALUES
(1, 'Uso de CPU nominal dentro de parámetros seguros (18%)', NOW(), NOW(), 'ms-pedidos-ventas', 1, 'MONITORING');

INSERT IGNORE INTO asignaciones_personal (id_asignacion, activa, cargo, fecha_asignacion, id_usuario_interno, id_tienda)
VALUES
(1, 1, 'Jefe de Tienda', NOW(), 1, 1);

INSERT IGNORE INTO metricas_sistema (id_metrica, disponible, errores_detectados, fecha_registro, microservicio, tiempo_respuesta_ms)
VALUES
(1, 1, 0, NOW(), 'ms-usuarios-identidad', 18),
(2, 1, 0, NOW(), 'ms-catalogo', 22);

INSERT IGNORE INTO respaldos_datos (id_respaldo, estado, fecha_ejecucion, fecha_programada, fecha_restauracion, frecuencia, origen_datos, responsable, resultado)
VALUES
(1, 'EXITOSO', NOW(), NOW(), NULL, 'DIARIO', '7 bases de datos MySQL EcoMarket', 'Administrador EcoMarket', 'Respaldo consolidado completado satisfactoriamente');

-- -----------------------------------------------------------------------------------------
-- 7. BASE DE DATOS: bd_reportes
-- -----------------------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS bd_reportes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bd_reportes;

INSERT IGNORE INTO indicadores_kpi (id, descripcion, fecha_calculo, tipo, valor)
VALUES
(1, 'Ventas Totales Consolidadas del Mes', NOW(), 'VENTAS_TOTALES', 28322.0),
(2, 'Tasa de Pedidos Entregados A Tiempo', NOW(), 'PEDIDOS_ENTREGADOS', 98.5),
(3, 'Índice de Rotación de Inventario Ecológico', NOW(), 'ROTACION_INVENTARIO', 4.2);

INSERT IGNORE INTO reportes (id, fecha_fin, fecha_generacion, fecha_inicio, formato_exportacion, generado_por, id_tienda, tipo)
VALUES
(1, CURDATE(), NOW(), DATE_SUB(CURDATE(), INTERVAL 30 DAY), 'PDF', 'Administrador EcoMarket', 1, 'VENTAS'),
(2, CURDATE(), NOW(), DATE_SUB(CURDATE(), INTERVAL 30 DAY), 'EXCEL', 'Administrador EcoMarket', 1, 'INVENTARIO');

SET FOREIGN_KEY_CHECKS = 1;
