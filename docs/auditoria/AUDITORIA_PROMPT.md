# PROMPT PARA CODEX — AUDITORÍA DE CÓDIGO (SOLO REPORTE, SIN CAMBIOS)

## ROL

Actúa como Auditor Técnico Senior de arquitecturas Spring Boot con microservicios.
Tu única tarea es **generar un informe de diagnóstico**.
**NO modifiques, refactorices, borres ni crees ningún archivo.** Solo lectura y análisis.

## CONTEXTO DEL PROYECTO

Proyecto "EcoMarket SPA" — Backend de microservicios (Java 21, Spring Boot, Maven, JPA/Hibernate, MySQL, HATEOAS, REST).
Microservicios: ms-usuarios-identidad, ms-catalogo, ms-inventario-abastecimiento, ms-pedidos-ventas, ms-logistica-envios, ms-administracion-soporte, ms-reportes, api-gateway.

Arquitectura esperada por microservicio (patrón CSR):

```
src/main/java/com/ecomarket/<dominio>/
├── controller/
├── service/
├── repository/
├── model/ (entidades JPA)
├── dto/
├── exception/
└── config/
```

## RESULTADOS DEL EP2 QUE DEBES VERIFICAR (puntos débiles confirmados por el profesor)

La rúbrica EP2 detectó estas falencias específicas — tu auditoría debe centrarse en confirmar si siguen presentes:

1. **IE 1.2.1 (CSR — 4,8/8, "aceptable")**: errores de implementación en uno o dos paquetes (controller/service/repository). Revisa si hay lógica de negocio filtrada en controllers, o controllers que acceden directo a repositories saltándose el service.

2. **IE 2.1.1 (Modelado JPA — 2,1/7, "incipiente")**: relaciones incompletas, mala normalización, incoherencia en atributos. Revisa cada entidad @Entity: tipos de datos, nombres de columnas, claves foráneas, anotaciones @OneToMany/@ManyToOne/@OneToOne mal definidas o ausentes donde el dominio las requiere.

3. **IE 2.1.2 (CRUD — 4,8/8, "aceptable")**: errores menores o entidad faltante. Verifica que TODAS las entidades de cada microservicio tengan CRUD completo (POST, GET, GET/{id}, PUT/PATCH, DELETE donde corresponda) y que los repositories estén realmente conectados (no simulados con listas en memoria).

4. **IE 2.2.1 (Reglas de negocio — 6/20, "incipiente")**: ESTE ES EL PESO MÁS ALTO (20%). Reglas básicas implementadas pero con omisiones importantes. Revisa contra el README y la guía de defensa qué reglas de negocio se DOCUMENTARON como implementadas (cancelación solo si PENDIENTE, factura duplicada → 409, validación de stock en carrito, cupones, etc.) y CONFIRMA cuáles realmente existen en el código del Service.

5. **IE 2.2.3 (Relaciones entre entidades — 2,1/7, "incipiente")**: relaciones incompletas, inconsistentes o mal aplicadas. Revisa integridad referencial real entre entidades de un mismo microservicio (ej. Pedido-HistorialPedido, Venta-Factura, Envio-SeguimientoEnvio, Carrito-ItemCarrito).

6. **IE 2.3.1 (Excepciones — 2,4/8, "incipiente")**: manejo limitado o poco claro. Verifica si existe `@ControllerAdvice`/`GlobalExceptionHandler` en CADA microservicio y si CADA endpoint está realmente cubierto (no solo algunos). Revisa que los códigos HTTP devueltos (400, 404, 409, 500) coincidan con lo que documenta el README/guía.

7. **IE 2.4.1 (Comunicación entre microservicios — 2,4/8, "incipiente")**: comunicación con errores o incoherencias. Según el README, los flujos esperados son:
   - MS Pedidos y Ventas → MS Inventario (consultar/reservar stock)
   - MS Pedidos y Ventas → MS Logística (solicitar despacho)
   - MS Reportes → MS Pedidos y Ventas (datos de ventas)
   - MS Reportes → MS Inventario (datos de stock)
   - MS Reportes → MS Administración y Soporte (datos de tiendas/rendimiento)

   Verifica si existen implementaciones reales con WebClient o Feign Client para estos flujos, si manejan timeouts/errores, y si coinciden con lo declarado.

8. **IE 2.4.2 (Endpoints REST remotos — 2,1/7, "incipiente")**: endpoints incorrectos o incompletos. Revisa que los endpoints que exponen/consumen datos remotos sigan convenciones REST (verbos, rutas semánticas, ResponseEntity).

## TAREA ADICIONAL — DETECCIÓN DE ARCHIVOS INNECESARIOS / SOBRANTES

Dado que cada microservicio acumula muchos archivos Java por capa, identifica:

- **Clases huérfanas**: archivos en `model/`, `dto/`, `service/`, `repository/`, `controller/` que NO son referenciados/usados por ningún otro archivo (verificar imports y usos reales).
- **DTOs duplicados o redundantes**: DTOs con estructura casi idéntica que podrían unificarse, o DTOs no usados por ningún controller/service.
- **Entidades sin repository o sin controller asociado** (entidad "fantasma" que no se usa en ningún CRUD).
- **Código muerto**: métodos no llamados, clases de configuración duplicadas, controllers/services vacíos o con métodos sin implementación (`return null;`, `// TODO`, métodos comentados).
- **Restos del EP2 que ya no aplican**: clases de prueba temporales, controllers de ejemplo (`HelloController`, `TestController`, etc.), application.properties duplicados, archivos `.java.bak`, clases con sufijo `Old`, `Copy`, `V2`, `Temp`.
- **Excepciones personalizadas no usadas**: clases en `exception/` que no son lanzadas por ningún service ni capturadas por el `GlobalExceptionHandler`.

## FORMATO DEL REPORTE FINAL

Genera un único reporte estructurado así, **por cada microservicio**:

```
## [Nombre del microservicio]

### 1. Estado del patrón CSR
- ✅/⚠️/❌ + descripción concreta con nombres de archivo y línea aproximada

### 2. Modelo de datos (entidades JPA)
- Lista de entidades encontradas
- Relaciones detectadas vs relaciones esperadas según el dominio
- Problemas de normalización/atributos

### 3. CRUD por entidad
- Tabla: Entidad | POST | GET | GET/{id} | PUT/PATCH | DELETE | Conectado a BD real (Sí/No)

### 4. Reglas de negocio
- Tabla: Regla esperada (según README/guía) | ¿Implementada? | Archivo:método | Observación

### 5. Validaciones Bean Validation
- Resumen rápido (este punto salió bien en EP2, solo confirmar que sigue así)

### 6. Manejo de excepciones
- ¿Existe GlobalExceptionHandler? Sí/No
- Endpoints cubiertos vs no cubiertos
- Códigos HTTP usados vs esperados

### 7. Logs SLF4J
- Resumen rápido (este punto salió bien en EP2, solo confirmar que sigue así)

### 8. Comunicación entre microservicios
- Llamadas remotas detectadas (WebClient/Feign) con origen→destino
- Comparación con flujos esperados del README
- Manejo de errores/timeouts: Sí/No

### 9. Archivos sobrantes / innecesarios detectados
- Lista de archivos con ruta completa + razón por la que se consideran innecesarios
- Clasificación: ELIMINAR / REVISAR / FUSIONAR

### 10. Resumen de prioridad de corrección
- Lista ordenada (1, 2, 3...) de qué corregir primero pensando en:
  a) Recuperar puntaje en los indicadores con peor desempeño en EP2 (especialmente IE 2.2.1 que pesa 20%)
  b) Dejar una base de código limpia (sin archivos sobrantes) ANTES de escribir pruebas unitarias para EP3
```

## AL FINAL DEL REPORTE, AGREGA UNA SECCIÓN GLOBAL:

```
## RESUMEN EJECUTIVO GENERAL

- Tabla comparativa de los 7 microservicios: Microservicio | CSR | Modelo JPA | CRUD | Reglas de negocio | Excepciones | Comunicación | Total archivos sobrantes
- Top 5 problemas más críticos del proyecto completo (ordenados por impacto en la nota EP2/EP3)
- Recomendación de orden de trabajo para el equipo antes de iniciar EP3 (pruebas unitarias con JUnit/Mockito/H2)
```

## REGLAS FINALES

- NO ejecutes `mvn`, NO compiles, NO modifiques `pom.xml`.
- NO crees archivos nuevos.
- NO borres nada — solo señala qué se debería borrar/fusionar/revisar.
- Si un microservicio no existe o está vacío, indícalo explícitamente en el reporte en lugar de omitirlo.
- Sé específico: cita rutas de archivo reales (`ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/service/CarritoService.java`), no descripciones genéricas.
- El reporte debe quedar guardado como `docs/auditoria/auditoria-ep3-pre-pruebas.md`.
