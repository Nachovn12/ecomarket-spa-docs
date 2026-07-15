# PASO C — Anotaciones Swagger / OpenAPI en el codigo Java

**Fecha:** 12-06-2026
**Rama:** develop
**Alcance:** 7 microservicios Spring Boot
**Tipo de cambio:** solo anotaciones + nueva dependencia + clase de configuracion. Sin cambios de logica de negocio.

---

## 1. Objetivo

Incorporar documentacion interactiva de la API REST mediante **OpenAPI 3 / Swagger UI**
usando el estandar de facto en el ecosistema Spring Boot: `springdoc-openapi-starter-webmvc-ui`.

Una vez iniciado cada microservicio se puede consultar:

- Swagger UI:   `http://localhost:<puerto>/doc/swagger-ui.html`
- OpenAPI JSON: `http://localhost:<puerto>/api-docs`
- OpenAPI YAML: `http://localhost:<puerto>/api-docs.yaml`

| Microservicio | Puerto |
|---|---|
| api-gateway (no anotado) | 8081 |
| ms-usuarios-identidad | 8083 |
| ms-catalogo | 8084 |
| ms-inventario-abastecimiento | 8085 |
| ms-pedidos-ventas | 8086 |
| ms-logistica-envios | 8087 |
| ms-administracion-soporte | 8088 |
| ms-reportes | 8089 |

---

## 2. Dependencia agregada

En los 7 `pom.xml` de los microservicios (no en el api-gateway) se agrego la dependencia:

```xml
<!-- OpenAPI / Swagger (springdoc-openapi) -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.7.0</version>
</dependency>
```

Compatible con Spring Boot 4.0.6 y Java 25 usados en el proyecto.

---

## 3. Clase de configuracion por microservicio

Se creo un archivo `OpenApiConfig.java` en el paquete `config/` de cada microservicio,
expuesto como un `@Bean` de tipo `OpenAPI` con metadata general (titulo, version, contacto, licencia y servidores).

Archivos creados:

- `ms-catalogo/src/main/java/com/ecomarket/catalogo/config/OpenApiConfig.java`
- `ms-usuarios-identidad/src/main/java/com/ecomarket/usuarios/config/OpenApiConfig.java`
- `ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/config/OpenApiConfig.java`
- `ms-inventario-abastecimiento/src/main/java/com/ecomarket/inventario/config/OpenApiConfig.java`
- `ms-logistica-envios/src/main/java/com/ecomarket/logistica/config/OpenApiConfig.java`
- `ms-administracion-soporte/src/main/java/com/ecomarket/admin/config/OpenApiConfig.java`
- `ms-reportes/src/main/java/com/ecomarket/reportes/config/OpenApiConfig.java`

---

## 4. Anotaciones aplicadas

Se siguieron tres niveles para mantener la documentacion util y mantenible:

### 4.1 Nivel controller (operaciones)

| Anotacion | Proposito |
|---|---|
| `@Tag` | Agrupa endpoints por dominio en Swagger UI. |
| `@Operation` | Resume que hace el endpoint. |
| `@ApiResponses` + `@ApiResponse` | Lista de respuestas HTTP posibles (200, 201, 400, 404, 409, etc.). |
| `@Parameter` | Describe un parametro de path / query / header. |

Controllers anotados (23 en total): 3 en ms-catalogo, 4 en ms-usuarios-identidad, 5 en ms-pedidos-ventas, 5 en ms-inventario-abastecimiento, 3 en ms-logistica-envios, 1 en ms-administracion-soporte, 2 en ms-reportes.

### 4.2 Nivel DTO y entidad (modelo de datos)

| Anotacion | Proposito |
|---|---|
| `@Schema` | Describe cada clase y cada campo, con `description`, `example`, `requiredMode`, `allowableValues`, `accessMode`, `minLength`, `maximum`, `minimum`. |

Se anoto todos los DTO (request/response) y todas las entidades + enums de cada microservicio.

### 4.3 Codigos HTTP documentados

| Codigo | Significado en EcoMarket |
|---|---|
| 200 | OK |
| 201 | Recurso creado |
| 204 | Eliminado sin contenido |
| 400 | Datos invalidos (Bean Validation) |
| 401 | No autenticado |
| 403 | Sin permisos para la operacion |
| 404 | Recurso no encontrado |
| 409 | Conflicto (duplicado, transicion invalida, etc.) |
| 500 | Error interno |

---

## 5. Resultado por microservicio

| Microservicio | Controllers anotados | DTOs anotados | Entidades anotadas | Config creado |
|---|---|---|---|---|
| ms-catalogo | 3 / 3 | 6 / 6 | 6 / 6 (incluye 3 enums) | Si |
| ms-usuarios-identidad | 4 / 4 | 11 / 11 | 2 / 2 (incluye enum Rol) | Si |
| ms-pedidos-ventas | 5 / 5 | 17 / 17 | 14 / 14 (incluye 4 enums) | Si |
| ms-inventario-abastecimiento | 5 / 5 | 11 / 11 | 6 / 6 | Si |
| ms-logistica-envios | 3 / 3 | 7 / 7 | 4 / 4 (incluye 2 enums) | Si |
| ms-administracion-soporte | 1 / 1 | 11 / 11 | 7 / 7 (incluye 2 enums) | Si |
| ms-reportes | 2 / 2 | 5 / 5 | 5 / 5 (incluye 3 enums) | Si |
| **Total** | **23 / 23** | **68 / 68** | **44 / 44** | **7 / 7** |

---

## 6. Validacion de compilacion y tests

Se ejecuto `mvn -f <ms>/pom.xml test` para los 7 microservicios. Resultado:

| Microservicio | Build | Tests |
|---|---|---|
| ms-catalogo | BUILD SUCCESS | 1/1 OK |
| ms-usuarios-identidad | BUILD SUCCESS | 1/1 OK |
| ms-pedidos-ventas | BUILD SUCCESS | 15/15 OK |
| ms-inventario-abastecimiento | BUILD SUCCESS | 5/5 OK |
| ms-logistica-envios | BUILD SUCCESS | 12/12 OK |
| ms-administracion-soporte | BUILD SUCCESS | 1/1 OK |
| ms-reportes | BUILD SUCCESS | 7/7 OK |
| **Total** | **7/7 OK** | **42/42 OK** |

Sin errores de compilacion ni de tests en ningun microservicio.

---

## 7. Como probar la documentacion Swagger

1. Levantar la base de datos MySQL (XAMPP u otro).
2. Iniciar el microservicio deseado, por ejemplo:
   ```bash
   mvn -f ms-catalogo/pom.xml spring-boot:run
   ```
3. Abrir en el navegador:
   ```
   http://localhost:8084/doc/swagger-ui.html
   ```
4. Probar los endpoints directamente desde Swagger UI (boton "Try it out").

Endpoints utiles para validar:

- `GET /api-docs` -> JSON OpenAPI puro.
- `GET /api-docs.yaml` -> YAML OpenAPI.
- `GET /doc/swagger-ui.html` -> UI interactiva.

---

## 8. Notas y decisiones

- La anotacion `@Schema(requiredMode = REQUIRED)` se uso solo en campos que tienen
  `@NotBlank` / `@NotNull` en sus DTOs, manteniendo la documentacion consistente con la
  validacion real.
- En las entidades, los campos autogenerados o de salida llevan
  `accessMode = READ_ONLY` para que Swagger UI no los muestre como editables.
- En los enums, `@Schema(allowableValues = {...})` permite que Swagger genere un combo
  cerrado con los valores validos.
- No se modifico ninguna clase `service/`, `repository/` ni `exception/` para mantener
  el alcance del PASO C limitado a documentacion.
- El api-gateway no requiere Swagger propio (solo rutea), por lo que no se le agrego
  la dependencia ni la configuracion.

---

## 9. Archivos modificados / creados (resumen)

- Archivos nuevos: 7 (los `OpenApiConfig.java`).
- Archivos modificados: 7 `pom.xml` + 23 controllers + 68 DTOs + 44 entidades/enums
  = **142 archivos Java actualizados** + 7 archivos `pom.xml`.
- **Total: 149 archivos tocados**.
