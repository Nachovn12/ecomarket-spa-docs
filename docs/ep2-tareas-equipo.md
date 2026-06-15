# EP2 - Checklist de Tareas para el Equipo

**Proyecto:** ecomarket-spa
**Fecha:** 2026-06-13
**Origen:** EP2_REPORTE.md (auditoria del 2026-06-12)

Este documento lista los items que quedaron PENDIENTES del EP2 y que se
asignan al equipo para que los aborden como parte del EP3. Cada tarea
indica el MS, los archivos clave, el peso en la nota final y el criterio
de aceptacion.

---

## Estado actual del EP2 (cerrado por Codex el 2026-06-13)

- IE 1.2.1 (peso 8%): Los 4 controllers de ms-pedidos-ventas ahora devuelven
  DTOs en vez de entidades JPA. Se crearon 8 DTOs Response.
- IE 2.3.1 (peso 8%): Los 7 GlobalExceptionHandler ahora manejan
  DataIntegrityViolationException y HttpMessageNotReadableException.
- Swagger/OpenAPI: operativo en los 7 MS (verificado en vivo).
- Compilacion: los 7 MS compilan con `mvn -DskipTests compile`.

---

## Tareas asignadas al equipo (EP3)

### ALTA prioridad (peso significativo en nota)

#### T1. IE 2.2.1 - Reglas de negocio reales (peso 20%)

Esta es la tarea mas importante. Cada MS debe tener validaciones de
dominio reales, no solo @NotNull/@NotBlank.

| MS | Reglas a implementar | Archivos a tocar |
|----|----------------------|------------------|
| ms-usuarios-identidad | Validacion de RUN chileno con digito verificador | service/UsuarioService.java, dto/UsuarioRequest.java |
| ms-usuarios-identidad | Politica de password (min 8, mayus, minus, numero) | dto/UsuarioRequest.java |
| ms-logistica-envios | Calculo de ETA basado en distancia y tipo de transporte | service/LogisticaService.java |
| ms-inventario-abastecimiento | Validar stock minimo antes de confirmar movimiento | service/InventarioService.java |
| ms-pedidos-ventas | Calculo de IVA (19%) y total con redondeo | service/PedidoService.java, service/VentaService.java |
| ms-pedidos-ventas | Validar que el carrito no este vacio al crear pedido | service/PedidoService.java |
| ms-reportes | KPIs reales (ventas del dia, ticket promedio, rotacion stock) | service/ReportesService.java |

Criterio de aceptacion: Existe un test unitario por cada regla que
demuestre el caso valido y el caso invalido.

#### T2. IE 2.4.1 - Comunicacion inter-MS robusta (peso 8%)

Ya existen 8 *ClientService.java pero les falta manejo de errores y
timeouts.

| MS | Archivos a tocar |
|----|------------------|
| ms-catalogo | config/RestTemplateConfig.java (nuevo) |
| ms-inventario-abastecimiento | config/RestTemplateConfig.java (nuevo) |
| ms-pedidos-ventas | service/CatalogoClientService.java, service/InventarioClientService.java |
| ms-logistica-envios | service/PedidosClientService.java (nuevo) |
| ms-administracion-soporte | service/UsuarioInternoClientService.java |
| ms-reportes | service/PedidosClientService.java, service/InventarioClientService.java |

Criterio de aceptacion: Cada llamada a otro MS tiene timeout (connect
2s, read 5s) y maneja 4xx/5xx con excepcion especifica de dominio.

#### T3. IE 2.1.1 - Migracion Double a BigDecimal (peso 7%)

Aplica en ms-pedidos-ventas y ms-catalogo.

| Archivo actual | Cambio |
|----------------|--------|
| ms-pedidos-ventas/model/Pedido.java | subtotal, descuento, total: Double -> BigDecimal |
| ms-pedidos-ventas/model/CarritoCompra.java | subtotal, descuentoAplicado, total: Double -> BigDecimal |
| ms-pedidos-ventas/model/ItemCarrito.java | precioUnitario, subtotal: Double -> BigDecimal |
| ms-pedidos-ventas/model/Venta.java | subtotal, descuento, total, iva: Double -> BigDecimal |
| ms-pedidos-ventas/model/Factura.java | subtotal, iva, total: Double -> BigDecimal |
| ms-pedidos-ventas/model/CuponDescuento.java | valorDescuento, montoMinimo: Double -> BigDecimal |
| ms-pedidos-ventas/dto/*Response.java | mismos campos: Double -> BigDecimal |
| ms-catalogo/model/Producto.java | precio: Double -> BigDecimal |

Criterio de aceptacion: Migracion completada, todos los calculos usan
BigDecimal con escala 2 y redondeo HALF_UP, no quedan Double en archivos
de modelo/dto de esos MS.

#### T4. IE 2.2.3 - Relaciones bidireccionales (peso 7%)

Actualmente el modelo esta "plano" (cero @OneToMany/@ManyToOne).

| Entidad | Relacion a agregar |
|---------|---------------------|
| Pedido | @OneToMany(mappedBy="pedido") List<DetallePedido> detalles |
| Pedido | @OneToMany(mappedBy="pedido") List<HistorialPedido> historial |
| CarritoCompra | ya tiene @OneToMany a ItemCarrito (verificar bidireccionalidad) |
| Categoria | @OneToMany(mappedBy="categoria") List<Producto> productos |
| Tienda | @OneToMany(mappedBy="tienda") List<AsignacionPersonal> personal |
| Pedido (Admin/Soporte) | @OneToMany(mappedBy="pedido") List<Reclamacion> |

Usar @JsonIgnore en el lado @ManyToOne para evitar ciclos en la
serializacion JSON. Considerar @JsonManagedReference/@JsonBackReference.

Criterio de aceptacion: Las 6 relaciones estan en la BD y la API
sigue respondiendo JSON sin ciclos infinitos (verificar con Swagger).

### MEDIA prioridad

#### T5. IE 2.1.2 - CRUD completo en ms-pedidos-ventas (peso 8%)

Los 4 controllers refactorizados ya tienen GET/POST/PUT/DELETE pero
verificar que las operaciones de actualizacion modifiquen todos los
campos esperados y que el DELETE haga soft-delete (campo activo).

#### T6. IE 2.4.2 - Convenciones REST (peso 7%)

| Inconsistencia | Accion |
|----------------|--------|
| Mezcla de PUT y PATCH para actualizacion | Estandarizar: PUT = reemplazo total, PATCH = parcial |
| Algunos paths en singular | Cambiar a plural: /api/venta -> /api/ventas |
| Verbo en path | Quitar: /api/ventas/presencial -> /api/ventas (registro presencial) |

#### T7. IE 1.2.1 - Mappers en service (peso 8%, resto)

En ms-logistica-envios los metodos `modelarXxx()` estan en los
controllers. Moverlos al service para que la conversion entidad->DTO
no quede en la capa de presentacion.

Archivos: service/LogisticaService.java, controller/EnvioController.java.

### BAJA prioridad (mejoras, no criticas)

- IE 2.1.1 (peso 7%, resto): Unificar entidad `Producto` entre
  ms-pedidos-ventas y ms-catalogo. Decidir si la fuente unica es
  catalogo y pedidos consume via REST.
- IE 1.2.1: Considerar extraer DTOs de salida a un modulo comun
  si se requiere reutilizacion.

---

## Como verificar progreso

Para cada tarea, antes de cerrar:

1. Compilar el MS: `mvn -f <ruta al pom>.xml -DskipTests compile`
2. Levantar el MS: `java -jar target/<ms>-0.0.1-SNAPSHOT.jar`
   (con `-Dspring.datasource.url=jdbc:h2:mem:testdb` para no depender de MySQL)
3. Probar en Swagger: `http://localhost:<puerto>/doc/swagger-ui.html`
4. Ejecutar tests: `mvn -f <ruta al pom>.xml test`

Puertos por MS:
- ms-usuarios-identidad: 8083
- ms-catalogo: 8084
- ms-inventario-abastecimiento: 8085
- ms-pedidos-ventas: 8086
- ms-logistica-envios: 8087
- ms-administracion-soporte: 8088
- ms-reportes: 8089
- api-gateway: 8081

---

## Estimacion de impacto en nota

| Tarea | Peso IE | Subida estimada |
|-------|---------|-----------------|
| T1 (reglas de negocio) | 20% | +8 a +12 puntos |
| T2 (comunicacion inter-MS) | 8% | +3 a +5 puntos |
| T3 (Double a BigDecimal) | 7% | +2 a +3 puntos |
| T4 (relaciones) | 7% | +2 a +3 puntos |
| T5 (CRUD completo) | 8% | +2 a +3 puntos |
| T6 (convenciones REST) | 7% | +2 a +3 puntos |
| T7 (mappers en service) | 8% | +1 a +2 puntos |

Si se completan todas, la nota podria subir de 53.7 a ~80-85.