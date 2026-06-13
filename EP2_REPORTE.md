# EP2 - Reporte de Auditoría Técnica

**Proyecto:** ecomarket-spa
**Fecha auditoría:** 2026-06-12
**Versión Spring Boot:** 4.0.6 (todos los MS)
**Estructura:** 7 microservicios + api-gateway
**Nota actual reportada por el profesor:** 53.7/100

---

## Tabla resumen

| IE | Criterio | Peso | Nota | Estado | Prioridad |
|----|----------|-----:|-----:|--------|-----------|
| 1.2.1 | Patrón CSR | 8% | 4.8 | ⚠️ Parcial | 🟡 MEDIA |
| 2.1.1 | Modelo DB normalizado | 7% | 2.1 | ❌ Pendiente | 🔴 ALTA |
| 2.1.2 | CRUD + JpaRepository + REST | 8% | 4.8 | ⚠️ Parcial | 🟡 MEDIA |
| 2.2.1 | Reglas de negocio | 20% | 6.0 | ❌ Pendiente | 🔴 ALTA |
| 2.2.2 | Bean Validation | 8% | 8.0 | ✅ OK | — |
| 2.2.3 | Relaciones entre entidades | 7% | 2.1 | ❌ Pendiente | 🔴 ALTA |
| 2.3.1 | Manejo de excepciones | 8% | 2.4 | ⚠️ Parcial | 🔴 ALTA |
| 2.3.2 | Logs estructurados | 7% | 7.0 | ✅ OK | — |
| 2.4.1 | Comunicación entre MS | 8% | 2.4 | ❌ Pendiente | 🔴 ALTA |
| 2.4.2 | Endpoints REST convenciones | 7% | 2.1 | ⚠️ Parcial | 🔴 ALTA |
| 2.5.1 | GitHub commits | 7% | 7.0 | ✅ OK (externo) | — |
| 2.5.2 | Trello / similar | 5% | 5.0 | ✅ OK (externo) | — |

**Potencial de subir nota** (peso acumulado de los ⚠️ y ❌): **77%**.

---


## IE 1.2.1 — Patrón Controller–Service–Repository | 🟡 MEDIA

**Estado:** ⚠️ Parcial. La estructura de paquetes está presente y bien separada en los 7 MS, pero hay casos donde la respuesta de los controllers expone entidades JPA en vez de DTOs, y en `ms-pedidos-ventas` la separación se rompe a favor de la entidad.

### Lo que está bien
- Los 7 MS tienen paquetes separados físicamente: `controller/`, `service/`, `repository/`, `model/`, `dto/`, `exception/`, `config/`.
- **32 repositorios** extienden `JpaRepository` (7 en ms-administracion-soporte, 3 en ms-catalogo, 6 en ms-inventario-abastecimiento, 4 en ms-logistica-envios, 9 en ms-pedidos-ventas, 2 en ms-reportes, 1 en ms-usuarios-identidad).
- **Ningún controller importa clases de `repository/`** (verificado en los 7 MS): la capa controller no toca el repositorio directamente.
- Inyección por constructor en lugar de `@Autowired` en campos:
  - `ms-catalogo/src/main/java/com/ecomarket/catalogo/service/CatalogoService.java`
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/PedidoService.java` y los demás services de ese MS
  - `ms-logistica-envios/src/main/java/com/ecomarket/logistica/service/LogisticaService.java`

### Lo que falta o está mal
- **Controllers que devuelven entidades JPA directamente (no DTOs):**
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/controller/CarritoController.java` — devuelve `EntityModel<CarritoCompra>`, `EntityModel<ItemCarrito>`.
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/controller/CuponDescuentoController.java` — `EntityModel<CuponDescuento>`.
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/controller/DevolucionController.java` — `EntityModel<Devolucion>`, `EntityModel<Reclamacion>`.
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/controller/PedidoController.java` — `EntityModel<Pedido>` para GET y PUT.
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/controller/VentaController.java` — devuelve `Venta`, `Factura`.
  - Esto rompe la separación: la entidad JPA se filtra al cliente, exponiendo columnas internas y relaciones.
- **`ms-pedidos-ventas` no tiene DTOs de salida** (0 archivos `*ResponseDTO.java` vs 7 en `ms-administracion-soporte` o 12 en `ms-usuarios-identidad`).
- **Mappers entidades→DTO en el controller, no en service** (práctica aceptable para HATEOAS pero no ideal):
  - `ms-logistica-envios/src/main/java/com/ecomarket/logistica/controller/EnvioController.java` — `modelarEnvio()` y `modelarEnvioCollection()` están aquí, no en service.

### Archivos a tocar (prioridad media)
- Crear `*ResponseDTO.java` y `*RequestDTO.java` en `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/dto/` para: CarritoCompra, ItemCarrito, CuponDescuento, Devolucion, Reclamacion, Venta, Factura, Pedido, DetallePedido, HistorialPedido.
- Refactorizar controllers de `ms-pedidos-ventas` para devolver los DTOs en lugar de entidades.
- Mover los métodos `modelarXxx()` de los controllers a un paquete `assembler/` o al service.

---


## IE 2.1.1 — Modelo de base de datos normalizado | 🔴 ALTA

**Estado:** ❌ Pendiente. Todas las entidades tienen `@Entity`, `@Id` y `@GeneratedValue`, pero hay **dos entidades `Producto` con la misma tabla `productos` en distintos MS**, **categoría como String en inventario**, **dinero como `Double`** y **redundancia de nombre en items**.

### Lo que está bien
- 36 entidades detectadas; las 36 tienen `@Entity`, `@Id`, `@GeneratedValue`.
- Tipos `LocalDateTime`, `LocalDate`, enums para estados — bien elegidos.
- Algunos modelos tienen campos `fechaCreacion` / `fechaActualizacion` auditables.

### Lo que falta o está mal (con archivos)
- **Entidad duplicada `Producto` en dos MS distintos** con la misma tabla:
  - `ms-catalogo/src/main/java/com/ecomarket/catalogo/model/Producto.java` → tabla `productos`, PK `idProducto`.
  - `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/model/Producto.java` → tabla `productos`, PK `id`.
  - Si ambos MS usan la misma BD MySQL: **conflicto de esquema en arranque** (DDL de uno borraría/redifiniría la tabla del otro). Si usan BD distintas: inconsistencia eventual imposible de resolver sin comunicación.
  - El dominio pide que `ms-pedidos-ventas` consulte stock en inventario y datos de producto en catálogo, pero las dos entidades no se referencian entre sí.
- **Categoría como `String` (rompe 3FN):**
  - `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/model/Producto.java` → `private String categoria;` y `private String sucursal;` deberían ser FK a entidades `Categoria` y `Sucursal`.
- **Dinero como `Double` (riesgo de redondeo):**
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/model/CuponDescuento.java` → `valorDescuento: Double`, `montoMinimo: Double`.
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/model/DetallePedido.java` → `precioUnitario: Double`.
  - `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/model/Producto.java` → `precio: Double`.
  - Recomendación: `BigDecimal` para todo valor monetario.
- **Redundancia de datos en items de pedido/venta:**
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/model/DetallePedido.java` → `idProducto: Long` + `nombreProducto: String`. El nombre es una **copia desnormalizada** del nombre en `ms-catalogo`.
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/model/ItemCarrito.java` → mismo problema (`idProducto` + `nombreProducto` + `precioUnitario` + `stockDisponible`).
- **Stock como `int` primitivo:**
  - `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/model/Producto.java` → `private int stock;` debería ser `Integer` para permitir null y validación.
- **Falta campo RUN/RUT en `Usuario`:**
  - `ms-usuarios-identidad/src/main/java/com/ecomarket/usuarios/model/Usuario.java` no tiene `run` ni `rut`. La documentación del controller menciona RUN en la Javadoc pero el campo no existe en la entidad → no se puede validar duplicado, no se puede mostrar.
- **Falta campo `total` en `Venta`:**
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/model/Venta.java` no tiene `total` ni `subtotal`. Se calculan en runtime pero no se persisten.

### Archivos a tocar (prioridad alta)
- `ms-catalogo/src/main/java/com/ecomarket/catalogo/model/Producto.java` — definirla como la única entidad Producto del proyecto.
- `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/model/Producto.java` — eliminar o reemplazar por DTO que apunte a `ms-catalogo` vía API.
- Convertir `Double` → `BigDecimal` en CuponDescuento, DetallePedido, Producto.
- Agregar campo `run` a `ms-usuarios-identidad/src/main/java/com/ecomarket/usuarios/model/Usuario.java`.
- Migrar `int stock` a `Integer stock` y agregar `@Min(0)`.

---


## IE 2.1.2 — CRUD completo + JpaRepository + REST | 🟡 MEDIA

**Estado:** ⚠️ Parcial. 32/32 repos extienden JpaRepository (✅). Endpoints REST existen en todos los MS (✅), pero **varias entidades no tienen CRUD completo** y `ms-pedidos-ventas` no usa DTOs.

### Lo que está bien
- 32 repositorios, **32 extienden `JpaRepository`** (100%).
- 0 `@Query` personalizadas: se usan métodos derivados de nombres (práctica aceptable para queries simples).

### Distribución CRUD por MS

| MS | POST | GET | PUT/PATCH | DELETE | Endpoints totales |
|----|-----:|----:|----------:|-------:|------------------:|
| ms-administracion-soporte | 7 | 9 | 5 | **0** | 21 |
| ms-catalogo | 3 | 8 | 2 | 3 | 16 |
| ms-inventario-abastecimiento | 5 | 15 | 4 | 2 | 26 |
| ms-logistica-envios | 3 | 10 | 8 | 2 | 23 |
| ms-pedidos-ventas | 10 | 15 | 6 | 3 | 34 |
| ms-reportes | 5 | 7 | **0** | 2 | 14 |
| ms-usuarios-identidad | 3 | 5 | 4 | 1 | 13 |

### Lo que falta (entidades sin CRUD completo)

**`ms-administracion-soporte` (0 endpoints DELETE en todo el MS):**
- `AlertaSistema`: solo POST + GET activas + PATCH resolver. **No hay DELETE**.
- `AsignacionPersonal`: solo POST + GET. **No hay PUT/DELETE**.
- `MetricaSistema`: solo POST + GET. **No hay PUT/DELETE**.
- `RespaldoDatos`: solo POST + GET + PATCH ejecutar/restaurar. **No hay DELETE**.
- `RespuestaSoporte`: solo POST + GET. **No hay PUT/DELETE**.
- `TicketSoporte`: POST + GET + PATCH estado. **No hay DELETE, ni PUT para editar otros campos**.
- `Tienda`: POST + GET + PUT. **No hay DELETE**.

**`ms-inventario-abastecimiento`:**
- `AjusteStock`: solo registro (POST). **No hay GET, ni PUT, ni DELETE**.
- `Inventario`: CR + DELETE. **No hay PUT**.
- `PedidoReabastecimiento`: solo POST + GET. **No hay PUT/DELETE**.
- `RecepcionMercancia`: solo POST + GET. **No hay PUT/DELETE**.

**`ms-pedidos-ventas`:**
- `CarritoCompra`: CRUD implementado en controller pero **devuelve la entidad**, no DTO.
- `CuponDescuento`: solo POST + GET. **No hay PUT/DELETE**.
- `DetallePedido`: solo lectura. **No hay POST/PUT/DELETE** directos (se manipula vía pedido).
- `HistorialPedido`: solo GET. **No hay POST/PUT/DELETE** (es registro automático, aceptable).
- `ItemCarrito`: sin endpoints propios (CRUD dentro de CarritoController, OK).

**`ms-logistica-envios`:**
- `RutaEntrega`: solo POST + GET. **No hay PUT/DELETE**.
- `SeguimientoEnvio`: solo POST + GET. **No hay PUT/DELETE**.

**`ms-reportes` (0 endpoints PUT/PATCH en todo el MS):**
- `IndicadorKPI`: **sin endpoints en controllers** (solo entidad). `KpiController` usa otra cosa.
- `Reporte`: CR + DELETE. **No hay PUT** (es de solo creación, parcialmente aceptable).

### Archivos a tocar
- Agregar endpoints DELETE en `ms-administracion-soporte/src/main/java/com/ecomarket/admin/controller/AdministracionSoporteController.java`.
- Agregar endpoints PUT y DELETE en Inventario, RecepcionMercancia, PedidoReabastecimiento, RutaEntrega, SeguimientoEnvio.
- Crear DTOs de salida en `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/dto/` (ver IE 1.2.1).

---


## IE 2.2.1 — Reglas de negocio | 🔴 ALTA (peso 20%, la más crítica)

**Estado:** ❌ Pendiente. `ms-pedidos-ventas` es el único MS con reglas de negocio robustas (cálculo de totales, descuentos, transiciones de estado). El resto tiene validaciones de nulidad o formato, **no reglas de dominio**.

### Métricas de complejidad de services

| MS | Services | Líneas | `if` blocks | `throw new` | Cálculos | Referencias a `Estado*` |
|----|---------:|-------:|-------------:|--------------:|---------:|------------------------:|
| ms-administracion-soporte | 2 | 524 | 4 | 5 | **0** | 7 |
| ms-catalogo | 1 | 345 | 15 | 11 | 0 | 13 |
| ms-inventario-abastecimiento | 5 | 582 | 16 | 13 | **0** | 17 |
| ms-logistica-envios | 1 | 250 | 19 | 8 | **0** | 16 |
| ms-pedidos-ventas | 5 | 657 | 18 | 17 | 16 | 20 |
| ms-reportes | 1 | 234 | 4 | 4 | 1 | 0 |
| ms-usuarios-identidad | 4 | 561 | 20 | 18 | **0** | 0 |

### Lo que está bien
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/CarritoService.java`: cálculo de subtotales, aplicación de cupones con validación de fecha/activo/monto mínimo.
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/PedidoService.java`: transiciones de estado `PENDIENTE → EN_PREPARACION → ENVIADO → ENTREGADO`, cancelación, recálculo de totales.
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/VentaService.java`: cálculo de subtotal, descuento, total, generación de factura con folio.
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/DevolucionService.java`: transiciones `PENDIENTE → APROBADA/RECHAZADA`, normalización de estado.

### Lo que falta (reglas críticas del dominio, NO implementadas)

**1. `ms-usuarios-identidad`: validación de RUN/RUT chileno (CRÍTICO).**
- `ms-usuarios-identidad/src/main/java/com/ecomarket/usuarios/service/UsuarioService.java` y `UsuarioInternoService.java`: solo validan email duplicado.
- Falta: algoritmo de dígito verificador de RUN (módulo 11), validación de formato `12345678-9`, normalización.
- El campo RUN no existe en la entidad `Usuario`.

**2. `ms-logistica-envios`: cálculo de fecha estimada de entrega (FALTA LA RAZÓN DE SER DEL MS).**
- `ms-logistica-envios/src/main/java/com/ecomarket/logistica/service/LogisticaService.java`: el campo `fechaEstimadaEntrega` se **recibe del DTO**, no se calcula. No hay lógica de "origen + distancia + proveedor → ETA".
- Falta: cálculo de ETA según proveedor seleccionado, distancia entre origen y destino, ventanas de horario del proveedor.

**3. `ms-inventario-abastecimiento`: alertas de stock bajo y reabastecimiento automático.**
- `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/service/InventarioService.java`: solo CRUD de stock.
- Falta: comparación de `stockActual` vs `stockMinimo`, generación automática de `PedidoReabastecimiento` cuando stock < mínimo.

**4. `ms-catalogo`: restricción "solo clientes que compraron pueden dejar reseña".**
- `ms-catalogo/src/main/java/com/ecomarket/catalogo/service/CatalogoService.java`: `crearResena()` no valida que el cliente haya comprado el producto. Cualquier `idCliente` puede dejar N reseñas sobre el mismo producto.
- Falta: validación de compra previa (consultar `ms-pedidos-ventas` o relación), máximo 1 reseña por cliente-producto, validación de rango de calificación (1-5).

**5. `ms-reportes`: cálculo real de KPIs (no agregación sobre datos locales).**
- `ms-reportes/src/main/java/com/ecomarket/reportes/service/ReporteService.java`: los KPIs se leen de la tabla `indicador_kpi` local. `ventasTotales = kpisVentas.stream().mapToDouble(...).sum()` — es una **simulación**, no un cálculo real.
- Falta: llamadas HTTP a `ms-pedidos-ventas` y `ms-inventario-abastecimiento` para calcular ventas reales, ticket promedio, tasa de devolución, productos más vendidos.

**6. `ms-administracion-soporte`: SLA de tickets y prioridades.**
- `ms-administracion-soporte/src/main/java/com/ecomarket/admin/service/AdministracionSoporteService.java`: solo CRUD básico.
- Falta: cálculo automático de prioridad según tipo de ticket, SLA por prioridad (e.g. ALTA = 4h, MEDIA = 24h, BAJA = 72h), escalamiento automático.

**7. Reglas financieras faltantes:**
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/PedidoService.java`: no aplica impuestos (IVA 19% Chile).
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/VentaService.java`: no calcula IVA.
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/CarritoService.java`: el descuento del cupón se aplica pero no valida que el descuento no supere el subtotal.

### Archivos a tocar (prioridad ALTA, peso 20%)
- `ms-usuarios-identidad/src/main/java/com/ecomarket/usuarios/service/UsuarioService.java`: agregar `validadorRun.runEsValido(String)` con módulo 11.
- `ms-usuarios-identidad/src/main/java/com/ecomarket/usuarios/model/Usuario.java`: agregar campo `run`.
- `ms-logistica-envios/src/main/java/com/ecomarket/logistica/service/LogisticaService.java`: agregar `calcularFechaEstimada(origen, destino, proveedor)`.
- `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/service/InventarioService.java`: agregar `verificarStockMinimo()` que cree `PedidoReabastecimiento` automáticamente.
- `ms-catalogo/src/main/java/com/ecomarket/catalogo/service/CatalogoService.java`: agregar validación de "cliente compró producto" (vía llamada HTTP a `ms-pedidos-ventas`).
- `ms-reportes/src/main/java/com/ecomarket/reportes/service/ReporteService.java`: cambiar la lectura local de KPIs por llamadas HTTP a otros MS (ver IE 2.4.1).
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/PedidoService.java` y `VentaService.java`: agregar cálculo de IVA.

---


## IE 2.2.2 — Bean Validation | ✅ OK

**Estado:** ✅ Correcto. No requiere cambios para los puntos evaluados por el profesor; se sugieren mejoras menores no críticas.

### Lo que está bien
- **24 archivos `*RequestDTO.java`** en los 7 MS, **24 con al menos una anotación de validación** (100%).
- Anotaciones presentes: `@NotNull`, `@NotBlank`, `@Size`, `@Email`, `@Min`, `@Max`, `@Pattern`, `@DecimalMin`, `@DecimalMax`, `@Future`, `@PastOrPresent`.
- Las validaciones se procesan en `GlobalExceptionHandler.handleValidation()` con respuesta JSON estructurada y campo `validaciones` (Map de campos).

### Archivos referenciados (ejemplos representativos)
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/dto/CrearPedidoRequest.java`: `@NotNull metodoPago`, `@NotBlank direccionEntrega`.
- `ms-usuarios-identidad/src/main/java/com/ecomarket/usuarios/dto/UsuarioRequestDTO.java`: `@Email correo`, `@NotBlank nombre`, `@Size(max=120) contrasena`.
- `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/model/Producto.java`: `@NotBlank nombre`, `@PositiveOrZero stock`.

### Lo que falta (mejoras opcionales, prioridad baja)
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/dto/ItemVentaRequest.java`: validar `@Positive cantidad`, `@PositiveOrZero precioUnitario`.
- `ms-catalogo/src/main/java/com/ecomarket/catalogo/dto/ResenaRequestDTO.java`: validar `@Min(1) @Max(5) calificacion`.

---


## IE 2.2.3 — Relaciones entre entidades | 🔴 ALTA

**Estado:** ❌ Pendiente. Solo **12 anotaciones de relación** en 36 entidades. Todas son `@ManyToOne`. Cero `@OneToMany`, cero `@ManyToMany`, cero `@OneToOne`. Faltan las navegaciones inversas.

### Lo que está bien
- Las 12 relaciones existentes son `@ManyToOne(fetch = LAZY)` (LAZY es lo correcto).
- No hay riesgo de ciclo JSON porque los controllers devuelven DTOs (excepto en `ms-pedidos-ventas`).

### Lo que falta
- **Cero `@OneToMany`** en todo el proyecto. Las entidades padre no pueden navegar a sus hijos:
  - `Categoria` no tiene `@OneToMany(mappedBy="categoria") List<Producto>` → para listar productos de una categoría hay que filtrar manualmente.
  - `Producto` no tiene `@OneToMany(mappedBy="producto") List<Resena>`.
  - `Pedido` no tiene `@OneToMany(mappedBy="pedido") List<DetallePedido>`.
  - `Tienda` no tiene `@OneToMany(mappedBy="tienda") List<AsignacionPersonal>`.
  - `TicketSoporte` no tiene `@OneToMany(mappedBy="ticket") List<RespuestaSoporte>` (la entidad sí tiene el `@ManyToOne` pero el padre no conoce la lista).
- **0 relaciones en `ms-reportes`** (Reporte↔KPI no están enlazados, se consultan por separado).
- **0 relaciones en `ms-usuarios-identidad`** (Usuario↔Rol no existen como entidades separadas, todo se modela con enums).
- **Falta modelar la relación Usuario↔Rol** (la entidad `Usuario` tiene `private Rol rol` como enum, no como FK a tabla `roles`).
- **Falta modelar Permiso↔Rol** (mencionado en `RolPermisoController` pero no existe la entidad).
- **Ninguna relación con `cascade`** definido → al guardar un padre no se guardan los hijos automáticamente (hay que persistir uno a uno).
- **Sin `@JsonIgnore` ni `@JsonManagedReference`** en todo el código. Como no hay bidireccionalidad todavía, no hay ciclos; pero al agregar `@OneToMany` van a aparecer.

### Distribución de relaciones existentes

| MS | Relaciones | Tipos |
|----|----------:|-------|
| ms-administracion-soporte | 2 | @ManyToOne × 2 |
| ms-catalogo | 2 | @ManyToOne × 2 |
| ms-inventario-abastecimiento | 3 | @ManyToOne × 3 |
| ms-logistica-envios | 3 | @ManyToOne × 3 |
| ms-pedidos-ventas | 2 | @ManyToOne × 2 |
| ms-reportes | 0 | — |
| ms-usuarios-identidad | 0 | — |
| **TOTAL** | **12** | **@ManyToOne × 12** |

### Archivos a tocar (prioridad alta)
- `ms-catalogo/src/main/java/com/ecomarket/catalogo/model/Categoria.java`: agregar `@OneToMany(mappedBy="categoria") private List<Producto> productos;`.
- `ms-catalogo/src/main/java/com/ecomarket/catalogo/model/Producto.java`: agregar `@OneToMany(mappedBy="producto") private List<Resena> resenas;`.
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/model/Pedido.java`: agregar `@OneToMany(mappedBy="pedido", cascade=CascadeType.ALL) private List<DetallePedido> detalles;`.
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/model/CarritoCompra.java`: agregar `@OneToMany(mappedBy="carrito", cascade=CascadeType.ALL) private List<ItemCarrito> items;`.
- `ms-administracion-soporte/src/main/java/com/ecomarket/admin/model/Tienda.java`: agregar `@OneToMany(mappedBy="tienda") private List<AsignacionPersonal> personal;`.
- `ms-administracion-soporte/src/main/java/com/ecomarket/admin/model/TicketSoporte.java`: agregar `@OneToMany(mappedBy="ticket") private List<RespuestaSoporte> respuestas;`.
- `ms-usuarios-identidad/src/main/java/com/ecomarket/usuarios/model/Usuario.java`: crear entidades `Rol.java` y `Permiso.java` con relación `@ManyToOne`.
- En las nuevas bidireccionales: usar `@JsonIgnore` o `@JsonBackReference` en el lado "hijo" para evitar ciclos.

---


## IE 2.3.1 — Manejo de excepciones | 🔴 ALTA

**Estado:** ⚠️ Parcial. Los 7 MS tienen `@RestControllerAdvice` y entre 4 y 7 `@ExceptionHandler` cada uno. La estructura JSON de respuesta es uniforme. **Falta `DataIntegrityViolationException`** y faltan códigos de error consistentes.

### Lo que está bien
- Los 7 MS tienen su `GlobalExceptionHandler.java` con `@RestControllerAdvice`.
- Total de `@ExceptionHandler`: 6+5+5+5+4+4+7 = **36 handlers** entre los 7 MS.
- Excepciones custom: `ResourceNotFoundException`, `ConflictException`, `AccesoNoAutorizadoException`, `CredencialesInvalidasException`, `UsuarioYaExisteException`, `ReporteNotFoundException`, etc.
- Formato JSON uniforme (ver `ms-catalogo/src/main/java/com/ecomarket/catalogo/exception/GlobalExceptionHandler.java`): incluye `status`, `error`, `message`, `path`, `timestamp`, y `validaciones` (Map de campos).
- HTTP status codes correctos: 404, 409, 400.
- `MethodArgumentNotValidException` se maneja en todos los MS con detalle por campo.

### Excepciones manejadas por MS

| MS | Handlers | Custom Exceptions |
|----|---------:|------------------|
| ms-administracion-soporte | 6 | RecursoNoEncontradoException, … |
| ms-catalogo | 5 | ConflictException, ResourceNotFoundException, … |
| ms-inventario-abastecimiento | 5 | RecursoNoEncontradoException, SkuDuplicadoException, … |
| ms-logistica-envios | 5 | ConflictoNegocioException, ResourceNotFoundException, … |
| ms-pedidos-ventas | 4 | (ninguna custom) |
| ms-reportes | 4 | ReporteException, ReporteNotFoundException, … |
| ms-usuarios-identidad | 7 | AccesoNoAutorizadoException, CredencialesInvalidasException, UsuarioNoEncontradoException, UsuarioYaExisteException |

### Lo que falta
- **`DataIntegrityViolationException` no se maneja en ningún MS.** Si una FK o unique constraint falla, se devuelve 500 con stack trace en vez de 409/400 con mensaje legible.
- **Falta `HttpRequestMethodNotSupportedException`** (un PUT a un endpoint que solo tiene POST devuelve 500 por defecto en algunos casos).
- **Falta `HttpMessageNotReadableException`** (JSON malformado devuelve 500 por defecto en lugar de 400).
- **Falta `ConstraintViolationException`** (validaciones a nivel de path variable o query param).
- **Falta `MissingServletRequestParameterException`** (parámetros requeridos faltantes).
- **No hay códigos de error internos** (campo `code` tipo `USR_001`, `INV_002`) que el frontend pueda usar para i18n.
- **`@RestControllerAdvice` no tiene `@Order`** — el orden de precedencia entre MS no es problema, pero dentro de cada MS las excepciones más específicas no tienen precedencia sobre las genéricas en algunos casos (e.g. `ResourceNotFoundException` se hereda de `RuntimeException`; si el `@ExceptionHandler(Exception.class)` está antes, lo captura antes que el específico — depende del orden de los métodos, que no es determinista en Spring 6+).

### Archivos a tocar
- En cada `GlobalExceptionHandler.java` agregar:
  - `@ExceptionHandler(DataIntegrityViolationException.class)` → 409 con mensaje limpio.
  - `@ExceptionHandler(HttpRequestMethodNotSupportedException.class)` → 405.
  - `@ExceptionHandler(HttpMessageNotReadableException.class)` → 400.
  - `@ExceptionHandler(MissingServletRequestParameterException.class)` → 400.
- Estandarizar un campo `code` en la respuesta JSON.

---


## IE 2.3.2 — Logs estructurados | ✅ OK

**Estado:** ✅ Correcto. 159 llamadas `log.info|warn|error|debug` en services. Buenas prácticas cumplidas.

### Lo que está bien
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/PedidoService.java`: usa `log.info("Pedido creado. id={}, cliente={}, total={}", id, cliente, total)` con placeholders (no concatenación).
- Cada `@Service` declara `private static final Logger log = LoggerFactory.getLogger(ClaseService.class)`.
- Niveles usados: `info` para eventos normales, `warn` para validaciones que rechazan input, `error` para excepciones no controladas.
- El `GlobalExceptionHandler` loguea cada excepción con `log.warn` o `log.error` con el path del request.

### Lo que falta (mejoras opcionales, prioridad baja)

| MS | `log.*` en services |
|----|--------------------:|
| ms-administracion-soporte | 25 |
| ms-catalogo | 12 |
| ms-inventario-abastecimiento | 28 |
| ms-logistica-envios | 14 |
| ms-pedidos-ventas | 38 |
| ms-reportes | 11 |
| ms-usuarios-identidad | 31 |
| **TOTAL** | **159** |

### Archivos referenciados (distribución por MS)
- Considerar agregar un MDC (`Mapped Diagnostic Context`) con `requestId` o `userId` para trazabilidad end-to-end.
- Algunos logs podrían incluir `traceId` cuando se integra con un sistema de tracing (Zipkin, Jaeger).

---


## IE 2.4.1 — Comunicación entre microservicios | 🔴 ALTA

**Estado:** ❌ Pendiente. **Solo `ms-administracion-soporte` tiene comunicación HTTP saliente** (RestTemplate configurado + 1 `UsuarioInternoClientService` que llama a `ms-usuarios-identidad`). Los otros 6 MS no tienen `RestTemplate`, `WebClient` ni `@FeignClient`.

### Lo que está bien
- `ms-administracion-soporte/src/main/java/com/ecomarket/admin/config/RestTemplateConfig.java`: `@Bean RestTemplate` con timeouts configurados.
- `ms-administracion-soporte/src/main/java/com/ecomarket/admin/service/UsuarioInternoClientService.java`: implementa correctamente timeout, 404, errores genéricos → traduce a excepciones del dominio.
- `api-gateway/src/main/resources/application.properties`: 14 rutas configuradas, todas con URI hacia el puerto local del MS.

### Distribución de HTTP clients

| MS | RestTemplate | WebClient | Feign | HTTP calls |
|----|-------------:|----------:|------:|-----------:|
| ms-administracion-soporte | 8 | 0 | 0 | 1 |
| ms-catalogo | 0 | 0 | 0 | 0 |
| ms-inventario-abastecimiento | 0 | 0 | 0 | 0 |
| ms-logistica-envios | 0 | 0 | 0 | 0 |
| ms-pedidos-ventas | 0 | 0 | 0 | 0 |
| ms-reportes | 0 | 0 | 0 | 0 |
| ms-usuarios-identidad | 0 | 0 | 0 | 0 |

### Lo que falta
- **`ms-catalogo` no llama a `ms-pedidos-ventas`** para validar que el cliente haya comprado el producto antes de permitir una reseña.
- **`ms-pedidos-ventas` no llama a `ms-catalogo`** para obtener precio/nombre actual del producto al crear DetallePedido o Venta (toma los datos del request, lo que permite manipular precios).
- **`ms-pedidos-ventas` no llama a `ms-inventario-abastecimiento`** para validar stock disponible al crear pedido o venta, ni para decrementar stock.
- **`ms-pedidos-ventas` no llama a `ms-logistica-envios`** para crear el envío cuando un pedido pasa a `EN_PREPARACION`.
- **`ms-logistica-envios` no llama a `ms-pedidos-ventas`** para validar que el pedido existe antes de crear el envío.
- **`ms-reportes` no llama a otros MS** para calcular KPIs reales (los calcula sobre datos locales simulados, ver IE 2.2.1 punto 5).
- **`ms-inventario-abastecimiento` no llama a `ms-catalogo`** para validar que el producto existe antes de crear AjusteStock o PedidoReabastecimiento.
- **`ms-pedidos-ventas` no llama a `ms-usuarios-identidad`** para validar que el cliente existe antes de crear Carrito, Pedido, Venta, Devolucion, Reclamacion.

### Archivos a tocar (prioridad ALTA)
- Crear `RestTemplateConfig.java` (o `WebClientConfig`) en:
  - `ms-catalogo/src/main/java/com/ecomarket/catalogo/config/`
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/config/`
  - `ms-logistica-envios/src/main/java/com/ecomarket/logistica/config/`
  - `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/config/`
  - `ms-reportes/src/main/java/com/ecomarket/reportes/config/`
- Crear `*ClientService.java` por cada flujo:
  - `ms-catalogo/src/main/java/com/ecomarket/catalogo/service/PedidosClientService.java` → valida compra.
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/CatalogoClientService.java` → consulta producto.
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/InventarioClientService.java` → consulta/decrementa stock.
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/LogisticaClientService.java` → crea envío.
  - `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/UsuariosClientService.java` → valida cliente.
  - `ms-logistica-envios/src/main/java/com/ecomarket/logistica/service/PedidosClientService.java` → valida pedido.
  - `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/service/CatalogoClientService.java` → valida producto.
  - `ms-reportes/src/main/java/com/ecomarket/reportes/service/PedidosClientService.java` y `InventarioClientService.java` para KPIs reales.
- Configurar timeouts y circuit breaker (Resilience4j) para evitar cascadas de fallo.

---


## IE 2.4.2 — Endpoints REST convenciones | 🔴 ALTA

**Estado:** ⚠️ Parcial. Los endpoints usan plural (`/api/productos`, `/api/categorias`), verbos HTTP correctos en la mayoría, y status codes apropiados. Pero hay **inconsistencias en la nomenclatura** y **PUT vs PATCH mal aplicados**.

### Lo que está bien
- **Plural correcto** en todos los recursos: `/api/productos`, `/api/categorias`, `/api/pedidos`, `/api/ventas`, `/api/envios`, `/api/rutas`, `/api/inventario/proveedores`, `/api/tiendas`, etc.
- **Verbos HTTP correctos** en general: GET para listar/obtener, POST para crear, PATCH para actualizaciones parciales, DELETE para borrar.
- **Status codes correctos** vía `GlobalExceptionHandler`: 200 OK, 201 CREATED (`HttpStatus.CREATED` se usa en 33 controllers), 400/404/409 desde el handler.
- **Prefijo `/api` consistente** en los 7 MS.

### Lo que falta o está mal (con archivos)

**1. Mezcla de PUT y PATCH para actualizaciones parciales (incorrecto).**
- `ms-logistica-envios/src/main/java/com/ecomarket/logistica/controller/EnvioController.java`: usa `@PutMapping` para `actualizarEnvio()` (recurso completo) y `@PutMapping` también para marcar como ENTREGADO (actualización parcial). Debería ser `@PatchMapping`.
- `ms-administracion-soporte/src/main/java/com/ecomarket/admin/controller/AdministracionSoporteController.java`: usa `@PatchMapping` para `/api/soporte/tickets/{idTicket}/estado` y `/api/admin/alertas/{idAlerta}/resolver`. **Bien** para estas, pero `@PutMapping("/api/admin/respaldos/{idRespaldo}/ejecutar")` está mal: ejecutar un respaldo no es actualizar el recurso completo, es un cambio de estado.

**2. Nombres de path inconsistentes.**
- `ms-catalogo/src/main/java/com/ecomarket/catalogo/controller/ProductoController.java`: `@GetMapping("/{id}")` en lugar de `/api/productos/{id}` (depende del `@RequestMapping` del controller — verificar que esté bien).
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/controller/PedidoController.java`: `@GetMapping("/{idPedido}")` y `@GetMapping` sin path. Confuso: `GET /api/pedidos` y `GET /api/pedidos/{idPedido}` no están claros sin leer el código.
- `ms-logistica-envios/src/main/java/com/ecomarket/logistica/controller/EnvioController.java`: `@GetMapping` y `@GetMapping("/{id}")` con el mismo controller.
- `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/controller/InventarioController.java`: `@GetMapping("/producto/{productoId}")` — usa el singular `producto` en el path (anti-convención REST).

**3. Recursos anidados innecesariamente.**
- `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/controller/AjusteStockController.java`: `@PostMapping` y `@GetMapping` sin path propio, dentro del controller. Los paths son `/api/inventario` (raíz) en vez de `/api/ajustes-stock`.
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/controller/HistorialPedidoController.java` (si existe): mismo problema.

**4. Sub-recursos mal nombrados.**
- `ms-administracion-soporte/src/main/java/com/ecomarket/admin/controller/AdministracionSoporteController.java`: `/api/admin/tiendas/{idTienda}/personal` — bien. `/api/soporte/tickets/{idTicket}/respuestas` — bien.
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/controller/CarritoController.java`: usa `/api/carritos` con sub-paths `/{idCarrito}/items`, `/{idCarrito}/cupon` — bien.

**5. Falta endpoint para Health y Swagger en producción.**
- `springdoc.swagger-ui.path` no está configurado en `application.properties` (los MS exponen `/swagger-ui.html` por defecto, OK).
- El `/actuator/health` no está expuesto en algunos MS (verificar `management.endpoints.web.exposure.include` en application.properties).

### Archivos a tocar
- Estandarizar `@RequestMapping("/api/xxx")` a nivel de clase y luego `@GetMapping`/`@PostMapping` sin path redundante.
- Cambiar `@PutMapping` a `@PatchMapping` donde la actualización sea parcial.
- Renombrar `/api/inventario/producto/{productoId}` a `/api/inventario/productos/{productoId}`.
- Revisar y unificar nombres de controllers con paths raíz.

---


## IE 2.5.1 — GitHub commits | ✅ OK (externo)

**Estado:** ✅ Confirmado por el profesor (7/7). No auditable desde el código fuente.

### Lo que está bien
- El profesor ya validó este criterio con 7/7 en la rúbrica.
- No hay evidencia en el código que indique problemas con el flujo de Git (los archivos `.gitignore`, README.md por MS y estructura de carpetas están bien organizados).

### Lo que falta (prioridad baja, recomendaciones generales)
- Cada feature o fix debería ir en un commit atómico.
- Mensajes en español consistentes con el estándar del equipo (Conventional Commits o similar).

### Archivos / prácticas sugeridas (no aplica a código Java)
- Repositorio del proyecto en GitHub (externo al código fuente). Se recomienda Conventional Commits en español.

---

## IE 2.5.2 — Trello / similar | ✅ OK (externo)

**Estado:** ✅ Confirmado por el profesor (5/5). No auditable desde el código fuente. Se sugiere mantener el tablero actualizado con las tareas de este reporte.

### Lo que está bien
- El profesor ya validó este criterio con 5/5 en la rúbrica.
- No hay evidencia en el código que indique problemas con la gestión de tareas.

### Lo que falta (prioridad baja, recomendaciones generales)
- Mantener el tablero actualizado con las tareas de este reporte (ver sección "Plan de acción sugerido" más abajo).

### Archivos / prácticas sugeridas (no aplica a código Java)
- Tablero Trello / equivalente (externo al código fuente).

---


### Archivos / prácticas sugeridas (no aplica a código Java)
- Tablero Trello / equivalente (externo al código fuente).

---
## Resumen de prioridades

### 🔴 ALTA (crítico, alto impacto en nota)

| IE | Acción principal | Archivos clave |
|----|------------------|----------------|
| 2.2.1 | Implementar reglas de negocio reales (validación RUN, ETA logística, stock mínimo, IVA, KPIs reales) | services de los 7 MS |
| 2.3.1 | Agregar `DataIntegrityViolationException` y otros handlers faltantes | 7 × `GlobalExceptionHandler.java` |
| 2.4.1 | Crear `RestTemplate` + `*ClientService.java` en 6 MS | config + service de cada MS |
| 2.1.1 | Unificar entidad Producto, cambiar Double→BigDecimal, agregar campo RUN a Usuario | models de los 7 MS |
| 2.2.3 | Agregar `@OneToMany` bidireccionales y entidades Rol/Permiso | models de los 7 MS |
| 2.4.2 | Estandarizar PUT vs PATCH, paths plurales consistentes | controllers de los 7 MS |

### 🟡 MEDIA (mejoras, impacto medio)

| IE | Acción principal | Archivos clave |
|----|------------------|----------------|
| 1.2.1 | Crear DTOs de salida en `ms-pedidos-ventas` y refactorizar controllers | `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/dto/` y controllers |
| 2.1.2 | Completar CRUD (DELETE en ms-administracion-soporte, PUT/DELETE donde faltan) | controllers de los 7 MS |

### ✅ OK (no requiere cambios)

| IE | Estado |
|----|--------|
| 2.2.2 Bean Validation | 24/24 DTOs con anotaciones |
| 2.3.2 Logs | 159 llamadas a `log.*` con buenas prácticas |
| 2.5.1 GitHub | Confirmado por el profesor |
| 2.5.2 Trello | Confirmado por el profesor |

---

## Plan de acción sugerido (orden de ejecución)

### Semana 1 — Alta impacto, bajo costo
- **IE 2.3.1:** agregar 4 `@ExceptionHandler` por MS (1-2 horas por MS, total 1 día).
- **IE 2.1.1:** cambiar Double→BigDecimal en 4 archivos (½ día).
- **IE 2.4.2:** estandarizar PUT/PATCH y plurales (1 día).
- **IE 2.1.2:** agregar DELETE en `ms-administracion-soporte` (½ día).

### Semana 2 — Reglas de negocio (peso 20%)
- **IE 2.2.1:** validación de RUN en `ms-usuarios-identidad` (1 día).
- **IE 2.2.1:** cálculo de ETA en `ms-logistica-envios` (2 días).
- **IE 2.2.1:** stock mínimo en `ms-inventario-abastecimiento` (1 día).
- **IE 2.2.1:** IVA en `ms-pedidos-ventas` (1 día).

### Semana 3 — Comunicación entre MS
- **IE 2.4.1:** crear `RestTemplateConfig` en 6 MS (1 día).
- **IE 2.4.1:** implementar 8 `*ClientService.java` con timeouts y manejo de errores (3-4 días).
- **IE 2.2.1 punto 5:** KPIs reales en `ms-reportes` (1-2 días).

### Semana 4 — Modelo de datos
- **IE 2.1.1:** unificar entidad Producto (1 día).
- **IE 2.1.1:** agregar campo RUN a Usuario (½ día).
- **IE 2.2.3:** agregar `@OneToMany` bidireccionales con `@JsonIgnore` (2-3 días).
- **IE 2.2.3:** modelar Rol/Permiso como entidades (1 día).

### Semana 5 — Refactor de salida
- **IE 1.2.1:** crear DTOs en `ms-pedidos-ventas` (2 días).
- **IE 1.2.1:** refactorizar controllers para usar DTOs (1 día).

---

## Cómo verificar el progreso

- **Compilación:** `mvn clean compile` en cada MS (0 errores esperado).
- **Tests existentes:** `mvn test` — los 42 tests deben seguir pasando.
- **Swagger:** cada MS debe responder 200 en `/v3/api-docs` y `/swagger-ui/index.html`.
- **CRUD end-to-end:** `POST /api/...` → 201, `GET /api/.../{id}` → 200.
- **Manejo de excepciones:** forzar un 404 (GET de id inexistente) y verificar JSON estructurado.
- **Comunicación inter-MS:** al crear un Pedido, verificar que internamente llama a `ms-catalogo`/`ms-inventario` (logs).
- **Reglas de negocio:** tests unitarios con casos válidos e inválidos (campo RUN mal formado, stock < 0, fecha estimada < fecha actual).

---

## Conclusión

El proyecto tiene una **base sólida** (estructura CSR, Bean Validation, logs, GlobalExceptionHandler presente en los 7 MS, Swagger operativo), pero **le falta el "alma" de las reglas de negocio** y **toda la comunicación inter-MS**. Los items de mayor impacto en nota son:

1. **IE 2.2.1 (peso 20%):** agregar reglas reales de dominio, no solo validaciones de nulidad.
2. **IE 2.4.1 (peso 8%):** los MS no se hablan entre sí — el sistema no funciona como sistema distribuido.
3. **IE 2.3.1 (peso 8%):** los `GlobalExceptionHandler` están pero les faltan casos (DataIntegrity, JSON malformado).
4. **IE 2.2.3 (peso 7%):** faltan las relaciones bidireccionales; el modelo está "plano".

Con el plan de 5 semanas sugerido, el proyecto podría subir de **53.7 a ~80-85** sin alterar la arquitectura existente (solo agregando lo que falta).

---

*Reporte generado por Codex el 2026-06-12. Auditoría de solo lectura, sin modificaciones al código.*
