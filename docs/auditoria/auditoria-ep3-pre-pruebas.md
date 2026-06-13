# Auditoria de Codigo - EcoMarket SPA (pre-EP3 / pruebas unitarias)

**Fecha:** 12-06-2026
**Rama auditada:** develop (HEAD @5ae79f)
**Alcance:** 7 microservicios + api-gateway. 232 archivos Java en total.
**Tipo de auditoria:** solo lectura - sin modificar, borrar ni refactorizar nada.
**Proposito:** confirmar/corregir los puntos debiles del EP2 antes de escribir las pruebas unitarias de EP3.

## Convenciones usadas

- [OK] Cumple / OK
- [WARN] Cumple parcialmente / riesgo latente
- [FAIL] No cumple / problema critico

---

## ms-catalogo (24 archivos Java)

### 1. Estado del patron CSR
- [OK] Cumple. Tres controllers (CategoriaController, ProductoController, ResenaController) delegan toda la logica a CatalogoService (ms-catalogo/src/main/java/com/ecomarket/catalogo/service/CatalogoService.java). Ningun controller accede directo a un repository. Las respuestas usan HATEOAS con EntityModel / CollectionModel y WebMvcLinkBuilder.

### 2. Modelo de datos (entidades JPA)
- Entidades encontradas: Categoria (model/Categoria.java), Producto (model/Producto.java), Resena (model/Resena.java).
- Enums: EstadoCategoria, EstadoProducto, EstadoResena (todos en model/).
- Relaciones:
  - Producto.categoria -> @ManyToOne a Categoria (FK id_categoria). [OK]
  - Resena.producto -> @ManyToOne a Producto (FK id_producto). [OK]
- [WARN] Observacion: la resena solo guarda idCliente como Long, no como @ManyToOne (es correcto porque el cliente vive en otro MS).
- Atributos / normalizacion: PKs por @GeneratedValue(IDENTITY), columnas declaradas con nullable, unique, length. Producto tiene FechaCreacion / FechaActualizacion manejados con @PrePersist / @PreUpdate. [OK]
- [WARN] Producto.descripcionEcologica es String simple. El repo expone FindByDescripcionEcologicaContainingIgnoreCase y el controller GET /api/productos/ecologicos la consume, lo cual esta bien, pero no hay una entidad AtributoEcologico que normalice el catalogo ecologico. No es bloqueante.

### 3. CRUD por entidad

| Entidad | POST | GET | GET/{id} | PUT/PATCH | DELETE | Conectado a BD real |
|---|---|---|---|---|---|---|
| Categoria | POST /api/categorias | GET /api/categorias | GET /api/categorias/{id} | PUT /api/categorias/{id} | DELETE /api/categorias/{id} | [OK] (JpaRepository) |
| Producto | POST /api/productos | GET /api/productos | GET /api/productos/{id} | PUT /api/productos/{id} | DELETE /api/productos/{id} | [OK] (JpaRepository) |
| Resena | POST /api/resenas | GET /api/resenas | GET /api/resenas/{id} | [FAIL] | DELETE /api/resenas/{id} | [OK] (JpaRepository) |

- [WARN] Resena no tiene PUT/PATCH; el controller no expone actualizacion de resena. Si el README/diagramas de casos de uso requieren edicion, esto es un faltante. Si la resena es inmutable, deberia documentarse asi.

### 4. Reglas de negocio
| Regla esperada (README / casos de uso) | Implementada? | Archivo:metodo | Observacion |
|---|---|---|---|
| No permitir SKU duplicado al crear | [OK] | CatalogoService.crearProducto | Lanza ConflictException -> 409. |
| No permitir SKU duplicado al actualizar | [OK] | CatalogoService.actualizarProducto | Valida solo si cambia. |
| Calificacion de resena entre 1 y 5 | [OK] | Resena con @Min(1) @Max(5) y DTO con mismas anotaciones | Bean Validation. |
| Estado por defecto de producto = PUBLICADO y de categoria = ACTIVA | [OK] | ResolverEstadoProducto / ResolverEstadoCategoria | Normaliza entrada. |
| Busqueda por palabra clave / categoria / rango de precio | [OK] | ProductoRepository + GET /api/productos/buscar | Cobertura completa. |
| Busqueda de productos ecologicos | [OK] | buscarEcologicos en service + GET /api/productos/ecologicos | Busqueda por texto libre. |

### 5. Validaciones Bean Validation
- [OK] Confirmado. Todos los DTOs de request (ProductoRequestDTO, CategoriaRequestDTO, ResenaRequestDTO) usan @NotBlank, @NotNull, @DecimalMin, @Size, @Min, @Max. Se aplican en controllers con @Valid.

### 6. Manejo de excepciones
- [OK] Existe GlobalExceptionHandler (exception/GlobalExceptionHandler.java) con @RestControllerAdvice.
- Cubre: ResourceNotFoundException -> 404, ConflictException -> 409, IllegalArgumentException -> 400, MethodArgumentNotValidException -> 400 con Validaciones, Exception -> 500.
- Excepciones personalizadas ResourceNotFoundException y ConflictException (en exception/) son lanzadas por el service. [OK]
- [WARN] ProductoController no expone HATEOAS en las busquedas (/buscar, /ecologicos) de forma completa; solo enlaza a la lista. Funcional pero mejorable.

### 7. Logs SLF4J
- [OK] Confirmado. CatalogoService usa LoggerFactory.getLogger(CatalogoService.class) y registra info en cada operacion y warn en duplicados.

### 8. Comunicacion entre microservicios
- [FAIL] No se encontraron llamadas salientes a otros MS. ms-catalogo no tiene RestTemplate, WebClient ni @FeignClient en sus archivos. La comunicacion es solo de entrada (los MS externos consumen su API).
- No hay validacion de stock contra ms-inventario-abastecimiento ni de cliente contra ms-usuarios-identidad. Si el flujo lo requiere, este MS esta aislado.

### 9. Archivos sobrantes / innecesarios detectados
- [OK] Nada detectado. Los 24 archivos se usan. El README.md interno y los enums son consistentes con el codigo.
- Sugerencia: la entidad Producto esta duplicada en ms-inventario-abastecimiento/model/Producto.java con campos muy similares (sku, nombre, precio, stock, categoria, sucursal). En una auditoria funcional se deberia resolver si ms-catalogo debe ser la fuente de verdad. Por ahora queda como observacion arquitectonica.

### 10. Resumen de prioridad de correccion
1. Agregar endpoint PUT/PATCH /api/resenas/{id} si la defensa lo exige.
2. Definir contrato de comunicacion con ms-pedidos-ventas para validar stock/cliente.
3. Documentar formalmente que Producto de catalogo NO es la misma entidad que Producto de inventario.

---


## ms-pedidos-ventas (53 archivos Java)

### 1. Estado del patron CSR
- [OK] Cumple. Cinco controllers (CarritoController, PedidoController, VentaController, CuponDescuentoController, DevolucionController) delegan en services. Sin acceso directo a repositorios desde controllers.
- [WARN] DevolucionController mapea /api raiz y mezcla rutas /devoluciones y /reclamaciones y /ventas/{id}/devoluciones. Funcional pero confuso de mantener.

### 2. Modelo de datos (entidades JPA)
- Entidades en model/: CarritoCompra, CuponDescuento, DetallePedido, Devolucion, Factura, HistorialPedido, ItemCarrito, Pago, Pedido, Reclamacion, Venta. Mas los enums EstadoCarrito, EstadoPedido, MetodoPago, TipoDescuento.
- Relaciones:
  - CarritoCompra <-> ItemCarrito: @OneToMany(mappedBy="carrito", cascade=ALL, orphanRemoval=true) con @JsonManagedReference / @JsonBackReference. [OK]
  - Pedido <-> DetallePedido: @OneToMany(mappedBy="pedido", cascade=ALL, orphanRemoval=true). [OK]
- [WARN] Factura guarda idVenta y idCliente como Long (no FK), correcto porque pertenecen al mismo MS, pero podria ser una relacion @OneToOne para integridad referencial.
- [WARN] Reclamacion, Devolucion, Pago usan Long idPedido / Long idVenta en vez de FK JPA. No hay integridad referencial real en BD.
- [WARN] Venta.idPedido es Long nullable. La venta presencial puede o no estar ligada a un pedido. OK.
- [FAIL] **Entidad Pago y PagoRepository parecen huerfanos**: ningun service ni controller instancia Pago ni llama a PagoRepository.save/findByIdPedido. Solo Pedido.metodoPago y Venta.metodoPago (enums) se persisten. Ver punto 9.

### 3. CRUD por entidad

| Entidad | POST | GET | GET/{id} | PUT/PATCH | DELETE | Conectado a BD real |
|---|---|---|---|---|---|---|
| CarritoCompra | POST /api/pedidos/carritos | GET /api/pedidos/carritos | GET /api/pedidos/carritos/{id} | parcial (PUT items) | [FAIL] | [OK] |
| ItemCarrito (anidado) | POST .../items | a traves del carrito | a traves del carrito | PUT .../items/{idItem} | DELETE .../items/{idItem} | [OK] |
| Pedido | POST /api/pedidos/carritos/{idCarrito}/pedidos | GET /api/pedidos | GET /api/pedidos/{id} | PUT /api/pedidos/{id} | DELETE /api/pedidos/{id} | [OK] |
| Venta | POST /api/ventas/presencial | GET /api/ventas | GET /api/ventas/{id} | PUT /api/ventas/{id} | DELETE /api/ventas/{id} | [OK] |
| Factura | POST /api/ventas/{idVenta}/factura | GET /api/ventas/facturas/{id} | implicito | [FAIL] | [FAIL] | [OK] |
| CuponDescuento | POST /api/pedidos/cupones | [FAIL] | [FAIL] | [FAIL] | [FAIL] | [OK] |
| Devolucion | POST /api/ventas/{idVenta}/devoluciones | GET /api/devoluciones | GET /api/devoluciones/{id} | PATCH /api/devoluciones/{id}/estado | [FAIL] | [OK] |
| Reclamacion | POST /api/reclamaciones (tambien desde pedido) | GET /api/reclamaciones | GET /api/reclamaciones/{id} | PATCH /api/reclamaciones/{id}/estado | [FAIL] | [OK] |
| HistorialPedido | creacion interna desde PedidoService | GET /api/pedidos/{id}/historial | n/a | n/a | n/a | [OK] |
| Pago | [FAIL] | [FAIL] | [FAIL] | [FAIL] | [FAIL] | [WARN] (no se usa) |

- [WARN] CuponDescuento solo tiene POST. No hay GET para listar/consultar cupones. El flujo de "aplicar cupon al carrito" lo busca por FindByCodigoIgnoreCase pero el admin no puede administrarlos. Riesgo para pruebas.
- [WARN] Factura no expone GET listado ni DELETE (correcto a nivel negocio: no se deberia borrar una factura emitida).
- [WARN] Pago sin endpoints ni uso interno - ver punto 9.

### 4. Reglas de negocio

| Regla esperada | Implementada? | Archivo:metodo | Observacion |
|---|---|---|---|
| Cancelar pedido solo si esta PENDIENTE | [OK] | PedidoService.cancelarPedido | Lanza IllegalArgumentException (mapea a 400). El handler devuelve 400, no 409 como dice el README; **discrepancia con la documentacion**. |
| Factura duplicada -> 409 | [OK] parcial | VentaService.generarFactura | if (facturaRepository.existsByIdVenta(idVenta)) lanza IllegalStateException. El GlobalExceptionHandler lo mapea a **409 Conflict**. [OK] |
| Validacion de stock al agregar al carrito | [OK] parcial | CarritoService.validarStock | Valida cantidad > stockDisponible (viene del DTO). El stock es externo - no consulta a ms-inventario-abastecimiento. |
| Aplicar cupon con validacion de vigencia / monto minimo | [OK] | CuponDescuentoService.aplicarCupon y ValidarCupon | Cubre expiracion, activo, monto minimo. |
| Carrito solo modificable si esta ACTIVO | [OK] | CarritoService.validarCarritoActivo | [OK] |
| Limpiar cupon al cambiar carrito | [OK] | CarritoService.limpiarCuponAplicado | Correcto. |
| Recalcular totales al persistir | [OK] | CarritoCompra.recalcularTotales con @PrePersist/@PreUpdate | [OK] |
| No permitir venta con descuento > subtotal o negativo | [OK] | VentaService.registrarVentaPresencial | Lanza IllegalArgumentException -> 400. |
| Folio de factura consecutivo y persistente | [OK] | VentaService.generarFactura con FacturaRepository.findMaxFolio() | [OK] |
| Estados permitidos para devolucion / reclamacion | [OK] | DevolucionService.normalizarEstadoDevolucion /
ormalizarEstadoReclamacion | Valida con lista blanca. |
| Reglas de transicion de pedido (CONFIRMADO, EN_PREPARACION, ENVIADO, ENTREGADO) | [FAIL] | - | No hay endpoints para cambiar estado a esos valores. El unico cambio explicito es cancelarPedido. |
| Reserva de stock contra ms-inventario-abastecimiento | [FAIL] | - | **No hay RestTemplate / WebClient / FeignClient en este MS.** |
| Despacho a ms-logistica-envios al confirmar pedido | [FAIL] | - | **Tampoco existe.** |
| Pago persistido | [FAIL] | - | La entidad Pago existe pero no se usa. Solo se guarda el enum MetodoPago. |

### 5. Validaciones Bean Validation
- [OK] Confirmado. CrearPedidoRequest, CrearVentaRequest, ItemVentaRequest, CrearDevolucionRequest, CrearReclamacionRequest, CrearFacturaRequest, CrearCarritoRequest, AgregarItemCarritoRequest, ActualizarCantidadRequest, AplicarCuponRequest, ActualizarEstadoDevolucionRequest, ActualizarEstadoReclamacionRequest y CancelarPedidoRequest tienen anotaciones (@NotBlank, @NotNull, @Min, @DecimalMin, @Size).
- [WARN] CuponDescuentoController.crearCupon recibe la **entidad** CuponDescuento directamente como @RequestBody con @Valid (sin DTO). Funciona porque la entidad tiene @Column(nullable=false), pero deja la validacion a Hibernate. Aceptable, mejorable.

### 6. Manejo de excepciones
- [OK] Existe GlobalExceptionHandler (exception/GlobalExceptionHandler.java).
- Mapea: IllegalArgumentException -> 400, IllegalStateException -> **409** (este es el que usa VentaService.generarFactura para factura duplicada), MethodArgumentNotValidException -> 400, generico -> 500.
- [WARN] **Discrepancia con README/arquitectura**: cancelar pedido en estado no PENDIENTE lanza IllegalArgumentException -> 400, pero el README dice que deberia ser 409 Conflict (docs/arquitectura/arquitectura-microservicios.md).
- [WARN] DevolucionService y RolPermisoService lanzan ResponseStatusException con codigos especificos (404 / 400) en vez de excepciones personalizadas. Funciona pero no queda centralizado en el handler.

### 7. Logs SLF4J
- [OK] Confirmado en PedidoService, VentaService, CarritoService, CuponDescuentoService, DevolucionService. Cada operacion relevante tiene log.info y los errores log.warn.

### 8. Comunicacion entre microservicios
- [FAIL] **No hay cliente HTTP en este MS.** Busqueda de RestTemplate|WebClient|@FeignClient en ms-pedidos-ventas/** no devuelve resultados. Por lo tanto:
  - MS Pedidos -> MS Inventario (consultar/reservar stock): **no implementado**.
  - MS Pedidos -> MS Logistica (solicitar despacho): **no implementado**.
- El README de comunicacion REST describe estos flujos; el codigo no los implementa.

### 9. Archivos sobrantes / innecesarios detectados

- **ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/model/Pago.java** y **epository/PagoRepository.java**: entidades/repositorio creados pero sin uso real (ningun service ni controller instancia Pago ni llama a PagoRepository). Accion sugerida: ELIMINAR o REVISAR (si se pensaba modelar el pago como entidad aparte, no quedo integrado).
- **ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/repository/ItemCarritoRepository.java**: solo se usa para delete(item) en CarritoService.eliminarItem. Funcional, pero podria resolverse con orphanRemoval del cascade en CarritoCompra sin necesidad de repositorio propio. REVISAR.
- [WARN] **Duplicacion semantica**: existen dos entidades distintas llamadas Producto (una en ms-catalogo/model/Producto.java y otra en ms-inventario-abastecimiento/model/Producto.java). ms-pedidos-ventas guarda idProducto como Long en DetallePedido e ItemCarrito, sin integridad referencial. Documentar de que fuente se obtiene.
- [WARN] DTOs de respuesta: CarritoController, VentaController, PedidoController exponen directamente la entidad (EntityModel<CarritoCompra>, EntityModel<Venta>, EntityModel<PedidoResponse>). Consistente pero no hay DTOs de salida para Carrito/Venta.

### 10. Resumen de prioridad de correccion
1. Implementar transicion de estado del pedido (CONFIRMADO / EN_PREPARACION / ENVIADO / ENTREGADO) con @PatchMapping /{id}/estado validando transiciones.
2. Decidir y ejecutar el cliente HTTP al MS de Inventario para validar stock en tiempo real (hoy el stock llega en el request del carrito).
3. Decidir que hacer con Pago / PagoRepository (eliminar o usar).
4. Alinear codigos HTTP: cancelar pedido deberia responder 409 cuando no es PENDIENTE (lo espera la documentacion).
5. Agregar endpoints GET /api/pedidos/cupones para administrar cupones.

---


## ms-inventario-abastecimiento (38 archivos Java)

### 1. Estado del patron CSR
- [OK] Cumple. InventarioController, ProductoController, AjusteStockController, PedidoReabastecimientoController, RecepcionMercanciaController delegan en services.
- [WARN] Conviven dos modelos de "stock": el legacy Inventario (sin relacion al catalogo) y el moderno Producto (con stock propio). El CRUD legacy sigue accesible via /api/inventario (crear, listar, actualizar stock, eliminar) y el nuevo via /api/inventario/productos. Hay duplicacion operativa.

### 2. Modelo de datos (entidades JPA)
- Entidades: Inventario (model/Inventario.java), Producto (model/Producto.java), AjusteStock, PedidoReabastecimiento, RecepcionMercancia, Proveedor.
- Relaciones:
  - AjusteStock.producto -> @ManyToOne Producto. [OK]
  - PedidoReabastecimiento.producto -> @ManyToOne Producto. [OK]
  - RecepcionMercancia.pedido -> @ManyToOne PedidoReabastecimiento. [OK]
- [WARN] Producto de inventario NO esta relacionado con Producto de catalogo. Ambos comparten sku como unique, pero la FK referencial no existe. Riesgo de inconsistencia.
- [WARN] PedidoReabastecimiento define un enum Estado anidado en la clase (no en archivo aparte); funciona, pero se separa de la convencion de los demas enums.
- [WARN] RecepcionMercancia define EstadoRecepcion anidado tambien.
- [WARN] Producto tiene validaciones Bean Validation en la **entidad** (@NotBlank, @NotNull, @Min, @DecimalMin). Se duplica con ProductoRequestDTO. Aceptable, pero considerar mover validacion al DTO.

### 3. CRUD por entidad

| Entidad | POST | GET | GET/{id} | PUT/PATCH | DELETE | Conectado a BD real |
|---|---|---|---|---|---|---|
| Inventario (legacy) | POST /api/inventario | GET /api/inventario | GET /api/inventario/{id} | PUT /api/inventario/{id}/stock | DELETE /api/inventario/{id} | [OK] |
| Producto | POST /api/inventario/productos | GET /api/inventario/productos | GET /api/inventario/productos/{id} | PUT /api/inventario/productos/{id} | DELETE /api/inventario/productos/{id} | [OK] |
| AjusteStock | POST /api/inventario/ajustes-stock | GET /api/inventario/ajustes-stock y ?producto/{id} | [FAIL] | [FAIL] | [FAIL] | [OK] |
| PedidoReabastecimiento | POST /api/inventario/pedidos-reabastecimiento | GET listado y por id | GET /{id} | PUT /{id}/aprobar y PUT /{id}/rechazar | [FAIL] | [OK] |
| RecepcionMercancia | POST /api/inventario/recepciones-mercancia | GET listado y ?pedido/{id} | [FAIL] | [FAIL] | [FAIL] | [OK] |
| Proveedor | [FAIL] en controller | GET /api/inventario/proveedores | [FAIL] | [FAIL] | [FAIL] | [OK] |

- [WARN] AjusteStock y RecepcionMercancia no exponen GET /{id} ni DELETE. Para auditoria pueden requerirse; estan parcialmente ocultos.
- [WARN] Proveedor no tiene CRUD propio en el controller; se lista dentro de InventarioController (GET /api/inventario/proveedores) pero no hay forma de crear uno via API. Solo ms-logistica-envios tiene CRUD completo de proveedores. REVISAR (se duplica la responsabilidad?).

### 4. Reglas de negocio
| Regla esperada | Implementada? | Archivo:metodo | Observacion |
|---|---|---|---|
| Cantidad minima no puede superar la cantidad disponible | [OK] | InventarioService.crearInventario | IllegalArgumentException -> 400. |
| Stock no puede ser negativo | [OK] | InventarioService.actualizarStock / Producto.stock con @Min(0) | [OK] |
| SKU duplicado al crear/actualizar producto | [OK] | ProductoService.agregarProducto / ActualizarProducto lanza SkuDuplicadoException -> 409. | [OK] |
| Ajuste de stock actualiza el stock del producto | [OK] | AjusteStockService.ajustarStock | [OK] |
| Aprobar pedido de reabastecimiento solo si esta PENDIENTE | [OK] | PedidoReabastecimientoService.aprobarPedido | [OK] |
| Rechazar pedido requiere motivo obligatorio | [OK] | PedidoReabastecimientoService.rechazarPedido | [OK] |
| Recepcion de mercancia solo para pedido APROBADO | [OK] | RecepcionMercanciaService.registrarRecepcion | [OK] |
| Cantidad danada <= cantidad recibida | [OK] | RecepcionMercanciaService.registrarRecepcion | [OK] |
| Recepcion actualiza el stock del producto y cierra el pedido como RECIBIDO | [OK] | Mismo metodo, lineas con producto.setStock(stockAnterior + cantidadAceptada) | [OK] |
| Estado de recepcion segun diferencias: CONFORME / CON_DIFERENCIAS / CON_DANOS | [OK] | Mismo metodo | [OK] |
| Proveedor activo al crear envio (en logistica) | [OK] (en logistica) | - | OK porque vive en otro MS. |

### 5. Validaciones Bean Validation
- [OK] Confirmado. Todos los DTOs de request (InventarioRequestDTO, ProductoRequestDTO, AjusteStockRequestDTO, PedidoReabastecimientoRequestDTO, RecepcionMercanciaRequestDTO) tienen anotaciones. La entidad Producto tambien, lo cual es redundante con el DTO.

### 6. Manejo de excepciones
- [OK] Existe GlobalExceptionHandler (exception/GlobalExceptionHandler.java) con cobertura completa:
  - SkuDuplicadoException -> 409.
  - RecursoNoEncontradoException -> 404.
  - IllegalArgumentException -> 400.
  - MethodArgumentNotValidException -> 400 con Validaciones.
  - Exception -> 500.
- Excepciones personalizadas SkuDuplicadoException y RecursoNoEncontradoException estan en uso. [OK]

### 7. Logs SLF4J
- [OK] Confirmado en InventarioService, ProductoService (no tiene logger explicito - [WARN] menor), AjusteStockService, PedidoReabastecimientoService, RecepcionMercanciaService.
- [WARN] ProductoService no tiene Logger. El resto si.

### 8. Comunicacion entre microservicios
- [FAIL] No hay cliente HTTP. ms-pedidos-ventas deberia consultar stock aqui y **no lo hace** (ver seccion de pedidos).
- El README indica: "MS Logistica -> MS Inventario (gestionar recepcion o reabastecimiento)" - no implementado. La logistica tiene su propio Proveedor y Envio, pero no llama a Inventario.

### 9. Archivos sobrantes / innecesarios detectados
- **model/Inventario.java + epository/InventarioRepository.java + controller/InventarioController.java + service/InventarioService.java + dto/InventarioRequestDTO.java + dto/InventarioResponseDTO.java**: CRUD legacy que duplica la funcionalidad de Producto (que tambien tiene stock). Accion sugerida: REVISAR - confirmar si ambos se necesitan o se fusiona.
- **dto/ProveedorDTO.java**: usado unicamente por InventarioService.listarProveedores (que muestra los proveedores), pero no hay un CRUD que los cree. Si se mantiene Proveedor aqui, deberia existir un endpoint de creacion. FUSIONAR con la version de logistica o crear CRUD.
- [WARN] Enums anidados PedidoReabastecimiento.Estado y RecepcionMercancia.EstadoRecepcion: por convencion suelen ir en archivo separado. No es bloqueante.

### 10. Resumen de prioridad de correccion
1. Decidir si Inventario legacy sigue siendo necesario; de lo contrario, eliminar y consolidar stock en Producto.
2. Crear CRUD de Proveedor o eliminar la entidad.
3. Agregar Logger a ProductoService.
4. Documentar/definir contrato de comunicacion con ms-pedidos-ventas y ms-logistica-envios.

---


## ms-logistica-envios (26 archivos Java)

### 1. Estado del patron CSR
- [OK] Cumple. Tres controllers (EnvioController, RutaEntregaController, ProveedorController) delegan en LogisticaService. No hay acceso directo a repositorios desde controllers.

### 2. Modelo de datos (entidades JPA)
- Entidades: Envio, SeguimientoEnvio, RutaEntrega, Proveedor. Enums separados en model/enums/: EstadoEnvio, EstadoRuta.
- Relaciones:
  - Envio.proveedor -> @ManyToOne Proveedor. [OK]
  - Envio.rutaEntrega -> @ManyToOne RutaEntrega. [OK]
  - SeguimientoEnvio.envio -> @ManyToOne(fetch=LAZY, optional=false). [OK]
- [WARN] Envio no tiene @OneToMany a SeguimientoEnvio; la lista se obtiene por seguimientoEnvioRepository.findByEnvioIdOrderByFechaRegistroDesc(idEnvio). Funcional, pero rompe la simetria con el dominio (el seguimiento es parte del envio).
- [WARN] Envio.idPedido es Long (no FK), correcto porque el pedido vive en otro MS.

### 3. CRUD por entidad

| Entidad | POST | GET | GET/{id} | PUT/PATCH | DELETE | Conectado a BD real |
|---|---|---|---|---|---|---|
| Envio | POST /api/envios | GET /api/envios | GET /api/envios/{id} y ?pedido/{id} | PUT /api/envios/{id} y PATCH /estado, PATCH /incidencia | DELETE /api/envios/{id} | [OK] |
| SeguimientoEnvio | se crea automaticamente | GET /api/envios/{id}/seguimiento | n/a | n/a | n/a | [OK] |
| RutaEntrega | POST /api/rutas | GET /api/rutas | GET /api/rutas/{id} | PUT /api/rutas/{id} y PATCH /estado | DELETE /api/rutas/{id} | [OK] |
| Proveedor | POST /api/envios/proveedores | GET .../proveedores y /activos y /buscar | GET /api/envios/proveedores/{id} | PUT .../{id} y PATCH /{id}/activar, /desactivar | [FAIL] (borrado logico via desactivar) | [OK] |

- [OK] Cobertura completa. Unico faltante es DELETE fisico de proveedor (se hace por soft-delete con desactivar) - decision coherente con la regla "no se elimina fisicamente".

### 4. Reglas de negocio
| Regla esperada | Implementada? | Archivo:metodo | Observacion |
|---|---|---|---|
| Crear envio requiere idPedido, origen, destino y fecha estimada | [OK] | LogisticaService.crearEnvio | Cuatro IllegalArgumentException especificos. |
| Proveedor asociado debe estar activo | [OK] | Mismo metodo: if (!proveedor.getActivo()) throw new ConflictoNegocioException | [OK] |
| Cambio de estado no permitido si envio esta ENTREGADO | [OK] | LogisticaService.cambiarEstadoEnvio | ConflictoNegocioException -> 409. |
| Cambio de estado de ruta no permitido si FINALIZADA | [OK] | LogisticaService.cambiarEstadoRuta | Mismo manejo. |
| Cada cambio de estado crea un registro de seguimiento | [OK] | RegistrarSeguimiento privado | [OK] |
| Validar fecha estimada de entrega en pasado | [OK] | EnvioDTO con @FutureOrPresent | Bean Validation. |
| Email del proveedor con formato valido | [OK] | Proveedor.email con @Email | [OK] |
| RUT unico para proveedor | [OK] | Proveedor.rut con @Column(unique=true) | [OK] |

### 5. Validaciones Bean Validation
- [OK] Confirmado en EnvioDTO, CambioEstadoRequestDTO, CambioEstadoRutaRequestDTO, IncidenciaRequestDTO, ProveedorDTO, RutaEntregaDTO, SeguimientoEnvioDTO.
- [WARN] RutaEntregaDTO y SeguimientoEnvioDTO parecen casi vacios (no se ven campos validados en este review). REVISAR.

### 6. Manejo de excepciones
- [OK] Existe GlobalExceptionHandler (exception/GlobalExceptionHandler.java).
- Cubre: ResourceNotFoundException -> 404, ConflictoNegocioException -> 409, IllegalArgumentException -> 400, validaciones -> 400, generico -> 500.
- Excepciones personalizadas usadas: ResourceNotFoundException, ConflictoNegocioException. [OK]

### 7. Logs SLF4J
- [OK] Confirmado en LogisticaService. Cubre creacion, actualizacion, rechazo y cambios de estado.

### 8. Comunicacion entre microservicios
- [FAIL] No hay cliente HTTP. La logistica NO consulta a ms-inventario-abastecimiento ni a ms-pedidos-ventas. El flujo "MS Logistica -> MS Inventario (gestionar recepcion o reabastecimiento)" no esta implementado.

### 9. Archivos sobrantes / innecesarios detectados
- [WARN] dto/RutaEntregaDTO.java y dto/SeguimientoEnvioDTO.java parecen subutilizados (las entidades no tienen muchos campos; revisar si los DTOs son necesarios o deben fundirse).
- [WARN] dto/EnvioDTO.java: usado para crear/actualizar envio y como cuerpo del PATCH de incidencia; OK, pero contiene campos duplicados con la entidad.

### 10. Resumen de prioridad de correccion
1. Implementar el consumo de la API de ms-pedidos-ventas para recibir el "despacho solicitado" automaticamente.
2. Documentar formalmente la relacion Envio <-> SeguimientoEnvio (decidir si agregar @OneToMany en Envio).
3. Decidir si RutaEntregaDTO y SeguimientoEnvioDTO deben tener contenido real.

---


## ms-administracion-soporte (38 archivos Java)

### 1. Estado del patron CSR
- [OK] Cumple. Un solo AdministracionSoporteController cubre todas las rutas (/api/admin/** y /api/soporte/** y /api/admin/monitorizacion/** y /api/admin/respaldos/**). Toda la logica esta en AdministracionSoporteService.
- [WARN] El controller es muy grande (243 lineas) por gestionar siete areas. Es funcional y bien organizado, pero considerar split por dominio antes de EP3.

### 2. Modelo de datos (entidades JPA)
- Entidades: Tienda, AsignacionPersonal, TicketSoporte, RespuestaSoporte, MetricaSistema, AlertaSistema, RespaldoDatos. Enums: EstadoTicket, PrioridadTicket.
- Relaciones:
  - AsignacionPersonal.tienda -> @ManyToOne Tienda. [OK]
  - RespuestaSoporte.ticket -> @ManyToOne TicketSoporte. [OK]
- [WARN] AsignacionPersonal.idUsuarioInterno es Long (no FK), correcto porque el usuario vive en otro MS.
- [WARN] Tienda no expone operaciones de borrado en el controller (no hay DELETE /api/admin/tiendas/{id}). Decision: baja logica con campo Activa.
- [WARN] AlertaSistema y MetricaSistema no tienen relacion entre si pese a que la alerta suele generarse a partir de una metrica.

### 3. CRUD por entidad

| Entidad | POST | GET | GET/{id} | PUT/PATCH | DELETE | Conectado a BD real |
|---|---|---|---|---|---|---|
| Tienda | POST /api/admin/tiendas | GET /api/admin/tiendas | GET /api/admin/tiendas/{id} | PUT /api/admin/tiendas/{id} | [FAIL] (soft-delete via flag activa) | [OK] |
| AsignacionPersonal | POST /api/admin/tiendas/{id}/personal | GET /api/admin/tiendas/{id}/personal | [FAIL] (no por id) | [FAIL] | [FAIL] | [OK] |
| TicketSoporte | POST /api/soporte/tickets | GET /api/soporte/tickets | GET /api/soporte/tickets/{id} | PATCH /estado | [FAIL] | [OK] |
| RespuestaSoporte | POST /api/soporte/tickets/{id}/respuestas | GET .../respuestas | [FAIL] | [FAIL] | [FAIL] | [OK] |
| MetricaSistema | POST /api/admin/monitorizacion/metricas | GET .../metricas | [FAIL] | [FAIL] | [FAIL] | [OK] |
| AlertaSistema | POST /api/admin/monitorizacion/alertas | GET .../alertas (activas) | [FAIL] | POST /resolver | [FAIL] | [OK] |
| RespaldoDatos | POST /api/admin/respaldos | GET .../respaldos | [FAIL] | PATCH /ejecutar, PATCH /restaurar | [FAIL] | [OK] |

- [WARN] AsignacionPersonal, RespuestaSoporte, MetricaSistema, AlertaSistema, RespaldoDatos no exponen GET /{id} (deberian, para trazabilidad).
- [WARN] Tienda no expone DELETE fisico, pero el estado activo/inactivo no se puede cambiar por API. Si se da de baja una tienda, las asignaciones activas quedan huerfanas.

### 4. Reglas de negocio
| Regla esperada | Implementada? | Archivo:metodo | Observacion |
|---|---|---|---|
| Horario de apertura anterior al horario de cierre | [OK] | AdministracionSoporteService.validarHorarios | IllegalArgumentException -> 400. |
| Validar que el usuario interno existe en MS Usuarios antes de asignarlo | [OK] | AdministracionSoporteService.asignarPersonal -> UsuarioInternoClientService.validarUsuarioInternoExiste | **Unico cliente HTTP real del proyecto.** |
| Manejo de timeout / error al llamar a MS Usuarios | [OK] | UsuarioInternoClientService con catch de ResourceAccessException -> 503 IllegalStateException | [OK] |
| Resolucion de alerta con fecha de resolucion automatica | [OK] | AdministracionSoporteService.resolverAlerta | [OK] |
| Cambiar estado de ticket via enum | [OK] | AdministracionSoporteService.actualizarEstadoTicket | [OK] |

### 5. Validaciones Bean Validation
- [OK] Confirmado en todos los DTOs Request (TiendaRequestDTO, AsignacionPersonalRequestDTO, TicketSoporteRequestDTO, RespuestaSoporteRequestDTO, MetricaSistemaRequestDTO, AlertaSistemaRequestDTO, RespaldoDatosRequestDTO).

### 6. Manejo de excepciones
- [OK] Existe GlobalExceptionHandler (exception/GlobalExceptionHandler.java).
- Cubre: MethodArgumentNotValidException -> 400 con Validaciones, RecursoNoEncontradoException -> 404, IllegalArgumentException -> 400, IllegalStateException -> **503 Service Unavailable** (clave para cuando falla MS Usuarios), MethodArgumentTypeMismatchException -> 400, generico -> 500.
- Excepciones personalizadas usadas. [OK]

### 7. Logs SLF4J
- [OK] Confirmado en AdministracionSoporteService y UsuarioInternoClientService. El cliente REST loguea intentos, timeouts y errores inesperados.

### 8. Comunicacion entre microservicios
- [OK] **Es el unico MS con cliente HTTP real (UsuarioInternoClientService + RestTemplateConfig).** Usa RestTemplate con SimpleClientHttpRequestFactory y timeouts (3s connect, 5s read). Llama a GET /api/usuarios/internos/{id} en ms-usuarios con header X-Rol-Usuario: ADMINISTRADOR. Maneja 404, timeout y errores genericos.
- [WARN] El reporte "MS Reportes -> MS Administracion y Soporte (datos de tiendas/rendimiento)" no tiene contraparte: ms-reportes no llama a este MS.

### 9. Archivos sobrantes / innecesarios detectados
- [OK] Nada critico. El controller unico es grande pero cubre los siete dominios que el README declara.
- [WARN] model/PrioridadTicket se usa en TicketSoporte pero no aparece en los endpoints de filtro o creacion (no hay endpoint para filtrar por prioridad). Aceptable, pero REVISAR.

### 10. Resumen de prioridad de correccion
1. Documentar la decision arquitectonica de "un unico controller" para EP3.
2. Agregar GET /{id} para AsignacionPersonal, RespuestaSoporte, MetricaSistema, AlertaSistema, RespaldoDatos.
3. Considerar exponer un endpoint para activar/desactivar tienda (soft-delete).

---


## ms-usuarios-identidad (30 archivos Java)

### 1. Estado del patron CSR
- [OK] Cumple. Cuatro controllers (AuthController, UsuarioController, UsuarioInternoController, RolPermisoController) delegan en AuthService, UsuarioService, UsuarioInternoService, RolPermisoService. Sin acceso directo a repositorios.

### 2. Modelo de datos (entidades JPA)
- Una sola entidad: Usuario (model/Usuario.java) con enum Rol (model/Rol.java).
- [WARN] Solo existe **una** entidad para representar clientes, empleados, gerentes y administradores (diferenciados por ol). Esto simplifica el modelo pero limita la extensibilidad.
- [OK] Los permisos se almacenan en @ElementCollection (usuario_permisos table) en vez de un String con coma. Buena normalizacion.
- [WARN] Usuario.password se guarda en texto plano (no se observa ningun encoder). Es una brecha de seguridad. Accion sugerida: REVISAR antes de EP3.

### 3. CRUD por entidad

| Entidad | POST | GET | GET/{id} | PUT/PATCH | DELETE | Conectado a BD real |
|---|---|---|---|---|---|---|
| Usuario (cliente) | POST /api/usuarios/registro | implicito (perfil) | GET /api/usuarios/clientes/{id}/perfil | PUT /api/usuarios/clientes/{id}/perfil | [FAIL] | [OK] |
| Usuario (interno) | POST /api/usuarios/internos | GET /api/usuarios/internos | GET /api/usuarios/internos/{id} | PUT /{id} y PUT /{id}/desactivar | DELETE /{id} (logico: activo=false, eliminado=true) | [OK] |
| Rol/Permisos | PUT /api/usuarios/internos/{id}/roles-permisos | GET /api/usuarios/internos/{id}/roles-permisos | [FAIL] | [FAIL] | [FAIL] | [OK] |
| Verificacion | n/a | GET /api/usuarios/internos/{id}/verificar-acceso?modulo= | n/a | n/a | n/a | [OK] |
| Auth | POST /api/auth/login | n/a | n/a | n/a | n/a | [OK] |

- [WARN] No hay GET /api/usuarios (listado general) ni GET /api/usuarios/clientes (listado de clientes). El admin no puede listar todos los clientes; solo ve los usuarios internos.

### 4. Reglas de negocio
| Regla esperada | Implementada? | Archivo:metodo | Observacion |
|---|---|---|---|
| Login con credenciales validas (correo + password) | [OK] | AuthService.iniciarSesion | Lanza CredencialesInvalidasException -> 401. |
| Bloquear login si cuenta esta inactiva o eliminada | [OK] | Mismo metodo | [OK] |
| No permitir registro con correo duplicado | [OK] | UsuarioService.registrarCliente | UsuarioYaExisteException -> 409. |
| No permitir actualizar perfil con correo ya usado | [OK] | UsuarioService.actualizarPerfilCliente | [OK] |
| Solo administradores pueden gestionar usuarios internos | [OK] | UsuarioInternoService.validarPermisoAdministrador y header X-Rol-Usuario | AccesoNoAutorizadoException -> 403. |
| Normalizar y validar roles internos (EMPLEADO/GERENTE/ADMINISTRADOR) | [OK] | UsuarioInternoService.normalizarRolInterno y ROLES_INTERNOS set | [OK] |
| Verificar acceso a un modulo por permisos del usuario | [OK] | RolPermisoService.verificarAcceso | Lista blanca de MODULOS_VALIDOS. |
| AccesoNoAutorizadoException mapea a 403 | [OK] | GlobalExceptionHandler | [OK] |
| Token de sesion generado | [OK] (pero como UUID.randomUUID() sin firma) | AuthService.iniciarSesion retorna tokenSesion | [WARN] No es un JWT real; valido para EP2 pero debil para produccion. |

### 5. Validaciones Bean Validation
- [OK] Confirmado en LoginRequestDTO, UsuarioRequestDTO, ActualizarPerfilClienteRequestDTO, UsuarioInternoRequestDTO, UsuarioInternoUpdateDTO, RolPermisosRequestDTO.

### 6. Manejo de excepciones
- [OK] Existe GlobalExceptionHandler (exception/GlobalExceptionHandler.java).
- Mapea: UsuarioNoEncontradoException -> 404, UsuarioYaExisteException -> 409, CredencialesInvalidasException -> **401 Unauthorized**, AccesoNoAutorizadoException -> **403 Forbidden**, validaciones -> 400, generico -> 500. Cobertura completa.

### 7. Logs SLF4J
- [OK] Confirmado en AuthService y UsuarioService. AuthService loguea intentos de login, contrasenas incorrectas y cuentas inactivas.

### 8. Comunicacion entre microservicios
- [FAIL] Este MS no sale hacia otros (es el origen de identidad). Es consultado por ms-administracion-soporte via REST.
- [WARN] El X-Rol-Usuario: ADMINISTRADOR enviado por el cliente de admin no se valida criptograficamente. Solo se compara como String. Riesgo en produccion, aceptable para EP2.

### 9. Archivos sobrantes / innecesarios detectados
- [OK] Nada. Los 30 archivos se usan. Los DTOs de salida (UsuarioResponseDTO, PerfilClienteResponseDTO, LoginResponseDTO, UsuarioInternoResponseDTO, RolPermisosResponseDTO, VerificacionAccesoResponseDTO) tienen un proposito claro.
- [WARN] dto/VerificacionAccesoResponseDTO se usa solo en RolPermisoController y no se documenta en docs/integracion/comunicacion-rest-entre-servicios.md. REVISAR.

### 10. Resumen de prioridad de correccion
1. Documentar la decision de "una sola entidad Usuario para cliente e internos" o separarlas antes de EP3.
2. Codificar la contrasena (BCrypt) si no se ha hecho.
3. Considerar JWT firmado en lugar de UUID simple para el token de sesion.

---


## ms-reportes (21 archivos Java)

### 1. Estado del patron CSR
- [OK] Cumple. ReporteController y KpiController delegan en ReporteService. Sin acceso directo a repositorios.

### 2. Modelo de datos (entidades JPA)
- Entidades: Reporte (model/Reporte.java), IndicadorKPI (model/IndicadorKPI.java). Enums: TipoReporte, FormatoExportacion, TipoKPI.
- [WARN] Ninguna relacion FK entre Reporte y IndicadorKPI pese a que el reporte se construye a partir de los KPIs. Hoy se consultan KPIs por FindByTipo(...) y se agregan en memoria. No hay persistencia explicita de la asociacion.
- [WARN] Reporte.idTienda es Long (no FK), aceptable porque la tienda vive en otro MS.

### 3. CRUD por entidad

| Entidad | POST | GET | GET/{id} | PUT/PATCH | DELETE | Conectado a BD real |
|---|---|---|---|---|---|---|
| Reporte | POST /api/v1/reportes y POST /ventas//inventario//rendimiento | GET /api/v1/reportes y ?tipo/{tipo} y ?tienda/{id} | GET /api/v1/reportes/{id} | [FAIL] | DELETE /api/v1/reportes/{id} | [OK] |
| IndicadorKPI | POST /api/v1/kpis | GET /api/v1/kpis y ?tipo/{tipo} | GET /api/v1/kpis/{id} | [FAIL] | DELETE /api/v1/kpis/{id} | [OK] |

- [WARN] No hay PUT para Reporte o KPI. Decision de diseno (inmutables), pero documentar.

### 4. Reglas de negocio
| Regla esperada | Implementada? | Archivo:metodo | Observacion |
|---|---|---|---|
| Validar rango de fechas (inicio <= fin) | [OK] | ReporteService.validarRangoFechas | Lanza ReporteException -> 400. |
| Fechas obligatorias para reportes con filtro | [OK] | Mismo metodo | [OK] |
| Reporte de ventas agrega KPIs VENTAS_TOTALES | [OK] | ReporteService.generarReporteVentas | Suma de Valor de cada KPI. |
| Reporte de inventario usa STOCK_BAJO y ROTACION_INVENTARIO | [OK] | generarReporteInventario | [OK] |
| Reporte de rendimiento usa cuatro tipos de KPI | [OK] | generarReporteRendimiento | [OK] |
| Calcular totales en tiempo de generacion | [WARN] parcial | Ver observaciones | Los datos **provienen unicamente de la tabla local indicadores_kpi**. No hay job que alimente los KPIs desde otros MS. Si nadie crea KPIs, los reportes siempre devolveran 0. |

### 5. Validaciones Bean Validation
- [OK] En DTOs ReporteFiltroRequestDTO y IndicadorKPIResponseDTO (aunque IndicadorKPIResponseDTO es de salida, no deberia tener validaciones - REVISAR).
- [WARN] ReporteController.crearReporte y KpiController.crearKPI reciben la **entidad** como @RequestBody (mismo patron que en cupones). Funciona, pero deja la validacion a Hibernate.

### 6. Manejo de excepciones
- [OK] Existe GlobalExceptionHandler (exception/GlobalExceptionHandler.java).
- Cubre: ReporteNotFoundException -> 404, ReporteException -> 400, validaciones -> 400, generico -> 500.

### 7. Logs SLF4J
- [OK] Confirmado en ReporteService. Loguea cada generacion de reporte.

### 8. Comunicacion entre microservicios
- [FAIL] **No hay cliente HTTP.** El README describe cuatro integraciones (MS Reportes -> MS Pedidos, MS Reportes -> MS Inventario, MS Reportes -> MS Administracion), y **ninguna esta implementada**. Los reportes se calculan sobre indicadores_kpi local, que hoy parece poblarse manualmente.
- Accion sugerida: implementar los cuatro clientes REST o documentar que la alimentacion de KPIs es batch (futuro).

### 9. Archivos sobrantes / innecesarios detectados
- [WARN] dto/ExportacionRequestDTO.java no se referencia desde ningun controller ni service (busqueda de uso no encuentra consumidores). Probable DTO huerfano. ELIMINAR o REVISAR.
- [WARN] dto/IndicadorKPIResponseDTO es el unico DTO de salida de KPI; en KpiController se usa el toDTO del service que mapea IndicadorKPI -> IndicadorKPIResponseDTO. OK, pero considerar mover el mapper a un helper dedicado.
- [WARN] model/FormatoExportacion no se persiste en la entidad Reporte (el campo esta declarado pero no se setea en ReporteService.crearReporte). REVISAR.

### 10. Resumen de prioridad de correccion
1. Decidir y ejecutar la estrategia de alimentacion de indicadores_kpi (clientes REST, batch, o ambos).
2. Eliminar dto/ExportacionRequestDTO.java si no se va a usar.
3. Mover las validaciones de la entidad a su DTO de request dedicado.

---

## api-gateway (2 archivos Java)

### 1. Estado del patron CSR
- N/A. No es un MS de negocio, es el gateway.
- [OK] Define las 14 rutas esperadas en Application.properties (auth, usuarios, productos, categorias, inventario, stock, pedidos, ventas, envios, rutas, admin, soporte, reportes, kpis). Cobertura completa.

### 2. Archivos
- Solo ApiGatewayApplication.java (main) y Application.properties. Sin codigo adicional. Limpio.

### 3. Observaciones
- [WARN] Las rutas /api/stock estan en el gateway pero no se encontro un controller especifico para "stock" en ms-inventario-abastecimiento. Se enruta a ms-inventario igual que /api/inventario/**. REVISAR si es intencional o si se pensaba tener un controller /api/stock separado.

---

## RESUMEN EJECUTIVO GENERAL

### Tabla comparativa de microservicios

| Microservicio | CSR | Modelo JPA | CRUD | Reglas de negocio | Excepciones | Comunicacion | Archivos sobrantes |
|---|---|---|---|---|---|---|---|
| ms-catalogo | [OK] | [OK] (3 entidades, 3 enums, 2 FK) | [OK] (3/3 entidades, falta PUT en Resena) | [OK] (6/6 reglas verificadas) | [OK] (handler completo) | [FAIL] ninguna saliente | 0 |
| ms-pedidos-ventas | [OK] | [WARN] (muchas entidades, FKs por Long, Pago huerfano) | [WARN] (Cupon sin GET, Pago sin uso) | [WARN] (5/12, faltan transiciones, integracion con stock) | [WARN] (codigos 400 vs 409) | [FAIL] ninguna saliente | 3-4 (Pago, PagoRepository, ItemCarritoRepository, Cupon sin GET) |
| ms-inventario-abastecimiento | [OK] | [WARN] (Producto duplica dominio de catalogo, enum anidado) | [WARN] (Proveedor sin CRUD) | [OK] (10/10 reglas verificadas) | [OK] (handler completo) | [FAIL] ninguna saliente | 6 (Inventario legacy + Proveedor) |
| ms-logistica-envios | [OK] | [WARN] (Envio sin @OneToMany Seguimiento) | [OK] (cobertura completa) | [OK] (8/8 reglas verificadas) | [OK] (handler completo) | [FAIL] ninguna saliente | 0-2 (algunos DTOs subutilizados) |
| ms-administracion-soporte | [OK] (1 controller grande) | [WARN] (sin @OneToMany metricas/alertas, sin DELETE tienda) | [WARN] (faltan GET /{id} en 5 entidades) | [OK] (5/5 reglas) | [OK] (handler completo, 503 para MS caido) | [OK] **unico cliente HTTP real** (a MS Usuarios) | 0-1 (algunos DTOs podrian consolidarse) |
| ms-usuarios-identidad | [OK] | [WARN] (1 entidad para cliente+interno, password plano) | [WARN] (sin listado de clientes) | [OK] (9/9 reglas, UUID en vez de JWT) | [OK] (handler completo, 401/403 bien mapeados) | [FAIL] (es origen, no cliente) | 0 |
| ms-reportes | [OK] | [WARN] (sin relacion Reporte-KPI, FormatoExportacion no se setea) | [WARN] (Reporte/KPI inmutables) | [WARN] (5/5 reglas pero KPIs se calculan localmente) | [OK] (handler completo) | [FAIL] ninguna saliente | 1 (dto/ExportacionRequestDTO.java sin uso) |
| api-gateway | N/A | N/A | N/A | N/A | N/A | 14 rutas configuradas | 0 |

### Top 5 problemas mas criticos del proyecto (ordenados por impacto)

1. **Sin comunicacion real entre microservicios (IE 2.4.1)**: solo ms-administracion-soporte -> ms-usuarios esta implementado. El resto de los flujos documentados en docs/integracion/comunicacion-rest-entre-servicios.md no existen. Esto afecta a ms-pedidos-ventas -> ms-inventario, ms-pedidos-ventas -> ms-logistica, ms-reportes -> ms-pedidos/inventario/admin. Es la perdida mas grande para EP3 porque requiere 4 nuevos clientes REST (o un FeignClient por cada uno).
2. **Reglas de negocio incompletas en ms-pedidos-ventas (IE 2.2.1, 20% del peso)**: faltan transiciones de estado del pedido, persistencia real del pago, y la validacion de stock en tiempo real. Tambien el handler global devuelve 400 cuando la documentacion pide 409 para "cancelar pedido no PENDIENTE".
3. **Entidad Pago huerfana**: archivo creado, repositorio creado, sin uso. Es un buen candidato a eliminar antes de las pruebas para no inflar el codigo muerto.
4. **Doble modelo de stock**: Inventario (legacy) y Producto (nuevo) coexisten en ms-inventario-abastecimiento. Para EP3 se debe decidir cual es la fuente de verdad o se duplicaran pruebas y esfuerzo.
5. **Producto duplicado entre ms-catalogo y ms-inventario-abastecimiento**: misma palabra, dos entidades con campos similares pero sin relacion. Es una decision arquitectonica que debe documentarse formalmente para no generar confusion en la defensa.

### Recomendacion de orden de trabajo para el equipo antes de EP3 (pruebas con JUnit/Mockito/H2)

1. **Limpieza primero** (1-2 horas por equipo): eliminar Pago y PagoRepository en ms-pedidos-ventas; eliminar dto/ExportacionRequestDTO.java en ms-reportes; decidir y fusionar Inventario legacy vs Producto en inventario; fusionar DTOs subutilizados de logistica.
2. **Alinear codigos HTTP con la documentacion** (rapido): cambiar el IllegalArgumentException por IllegalStateException (409) en PedidoService.cancelarPedido cuando el estado no es PENDIENTE. Documentar explicitamente la semantica 400 vs 409.
3. **Agregar reglas de negocio pendientes de alto impacto** (en ms-pedidos-ventas): transiciones de estado del pedido (@PatchMapping /{id}/estado con validacion de transiciones legales) y persistencia de Pago real.
4. **Documentar decisiones arquitectonicas** que faltan: por que Usuario es una sola entidad, por que Producto esta duplicado entre catalogo e inventario, por que Admin-soporte usa un unico controller.
5. **Recien entonces escribir pruebas unitarias con JUnit/Mockito/H2**. Configurar H2 en TODOS los Application.properties de test (hoy solo ms-catalogo, ms-pedidos-ventas, ms-logistica-envios y ms-administracion-soporte lo tienen; ms-inventario-abastecimiento, ms-usuarios-identidad y ms-reportes aun no - ver ms-pedidos-ventas/src/test/resources/application.properties y ms-logistica-envios/src/test/resources/application.properties como plantillas).
6. **Implementar comunicacion entre microservicios** en paralelo (no bloqueante para pruebas del Service aislado): usar WebClient o RestTemplate con MockRestServiceServer en las pruebas, o WireMock para integracion. Esto se puede atacar despues de que el codigo este limpio.

> **Nota sobre la auditoria:** todos los hallazgos de archivos sobrantes se reportan como ELIMINAR o REVISAR o FUSIONAR, pero **no se elimino nada**. El reporte es la unica entrega.
