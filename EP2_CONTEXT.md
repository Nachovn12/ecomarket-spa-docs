# EP2 - Contexto para Codex

## Objetivo de esta auditoria
Codex debe leer este archivo + el codigo fuente del proyecto EP2, y luego generar un **reporte detallado** indicando:
- Que esta corregido y bien implementado
- Que falta o esta mal implementado segun la rubrica
- Prioridad de cada pendiente (ALTA/MEDIA/BAJA)
- NO debe generar codigo, solo diagnosticar y reportar

---

## Mi nota actual: 53.7/100

---

## Rubrica completa con mis errores

### IE 1.2.1 — Patron CSR (Controller–Service–Repository) | Peso: 8% | Mi nota: 4.8/8
**Profe dice:** "Estructura el proyecto por paquetes pero con algunos errores en implementacion de funcionalidades propias de uno o dos paquetes."

**Revisar:**
- [ ] Controller solo debe recibir request y delegar a Service
- [ ] Service solo debe tener logica de negocio
- [ ] Repository solo debe tener consultas a DB (JpaRepository)
- [ ] Ninguna capa debe hacer trabajo de otra capa
- [ ] Paquetes separados fisicamente: `controller/`, `service/`, `repository/`, `model/` o `entity/`

---

### IE 2.1.1 — Modelo de base de datos normalizado | Peso: 7% | Mi nota: 2.1/7
**Profe dice:** "Modela entidades basicas, pero con relaciones incompletas, mala normalizacion o incoherencia en atributos."

**Revisar:**
- [ ] Tablas en 3FN (Tercera Forma Normal)
- [ ] Atributos coherentes con el dominio del proyecto
- [ ] Tipos de datos correctos (Long, String, BigDecimal, LocalDate, etc.)
- [ ] Sin datos redundantes entre tablas
- [ ] Cada entidad tiene su `@Entity`, `@Id`, `@GeneratedValue`

---

### IE 2.1.2 — CRUD completo + JpaRepository + REST | Peso: 8% | Mi nota: 4.8/8
**Profe dice:** "Implementa CRUD en la mayoria de las entidades, con algunos errores menores o una entidad faltante."

**Revisar:**
- [ ] Todas las entidades tienen CRUD completo (Create, Read, Update, Delete)
- [ ] Repositorios extienden JpaRepository
- [ ] Endpoints REST devuelven JSON coherentes (usa DTOs, no entidades)
- [ ] Ninguna entidad se quedo sin CRUD

---

### IE 2.2.1 — Reglas de negocio | Peso: 20% | Mi nota: 6/20
**Profe dice:** "Implementa reglas basicas, pero omite elementos importantes del dominio o las aplica incorrectamente."

**ESTO ES LO QUE MAS PESA. Revisar a fondo:**
- [ ] Listar todas las reglas de negocio del dominio
- [ ] Cada regla implementada en la capa Service
- [ ] Reglas complejas (no solo validar que un campo no sea nulo)
- [ ] Ejemplos: calcular impuestos, validar stock, estados de pedido, restricciones de fechas, etc.

---

### IE 2.2.2 — Bean Validation | Peso: 8% | Mi nota: 8/8
**Profe dice:** "Muy buen desempeno."
- [ ] Confirmar que sigue correcto, no romperlo con otros cambios

---

### IE 2.2.3 — Relaciones entre entidades | Peso: 7% | Mi nota: 2.1/7
**Profe dice:** "Relaciones incompletas, inconsistentes o mal aplicadas."

**Revisar:**
- [ ] `@OneToMany`, `@ManyToOne`, `@JoinColumn` correctamente configurados
- [ ] `cascade` y `fetch` apropiados (LAZY vs EAGER)
- [ ] Integridad referencial: las FK existen y apuntan a tablas correctas
- [ ] Sin relaciones circulares que causen JSON infinito (usar `@JsonIgnore` o DTOs)

---

### IE 2.3.1 — Manejo de excepciones | Peso: 8% | Mi nota: 2.4/8
**Profe dice:** "Manejo limitado o poco claro."

**Revisar:**
- [ ] `@RestControllerAdvice` global implementado
- [ ] `@ExceptionHandler` para: EntityNotFoundException, BadRequestException, MethodArgumentNotValidException, DataIntegrityViolationException, generic Exception
- [ ] Respuestas uniformes en formato JSON (`{ "status": 404, "error": "Not found", "message": "...", "timestamp": "..." }`)
- [ ] Todos los endpoints cubiertos, no solo algunos

---

### IE 2.3.2 — Logs estructurados | Peso: 7% | Mi nota: 7/7
**Profe dice:** "Muy buen desempeno."
- [ ] Confirmar que sigue correcto

---

### IE 2.4.1 — Comunicacion entre microservicios | Peso: 8% | Mi nota: 2.4/8
**Profe dice:** "Comunicacion con errores o incoherencias."

**Revisar:**
- [ ] `RestTemplate` o `WebClient` configurado para llamar de un MS a otro
- [ ] Los flujos que cruzan microservicios funcionan end-to-end
- [ ] Manejo de errores cuando el otro MS no responde (timeout, fallback)
- [ ] La comunicacion tiene sentido segun el dominio del proyecto

---

### IE 2.4.2 — Endpoints REST convenciones | Peso: 7% | Mi nota: 2.1/7
**Profe dice:** "Endpoints incorrectos o incompletos."

**Revisar:**
- [ ] Nombres REST correctos: `GET /api/recursos`, `POST /api/recursos`, `GET /api/recursos/{id}`, `PUT /api/recursos/{id}`, `DELETE /api/recursos/{id}`
- [ ] Verbos HTTP correctos (GET para leer, POST para crear, PUT para actualizar, DELETE para borrar)
- [ ] Status codes HTTP correctos (200 OK, 201 Created, 400 Bad Request, 404 Not Found, 500 Internal Server Error)
- [ ] Plural en los nombres (`/api/productos` no `/api/producto`)

---

### IE 2.5.1 — GitHub commits | Peso: 7% | Mi nota: 7/7
**Profe dice:** "Muy buen desempeno."
- [ ] Confirmar que sigue correcto

---

### IE 2.5.2 — Trello o similar | Peso: 5% | Mi nota: 5/5
**Profe dice:** "Muy buen desempeno."
- [ ] Confirmar que sigue correcto

---

## Peso por prioridad de correccion

| Prioridad | Criterio | Peso | Nota actual | Impacto si sube a 80% |
|-----------|----------|------|-------------|----------------------|
| 🔴 ALTA | IE 2.2.1 Reglas de negocio | 20% | 6/20 | +11.2 pts |
| 🔴 ALTA | IE 2.3.1 Excepciones | 8% | 2.4/8 | +4.0 pts |
| 🔴 ALTA | IE 2.4.1 Comunicacion MS | 8% | 2.4/8 | +4.0 pts |
| 🔴 ALTA | IE 2.1.1 Modelo DB | 7% | 2.1/7 | +3.5 pts |
| 🔴 ALTA | IE 2.2.3 Relaciones | 7% | 2.1/7 | +3.5 pts |
| 🔴 ALTA | IE 2.4.2 Endpoints REST | 7% | 2.1/7 | +3.5 pts |
| 🟡 MEDIA | IE 1.2.1 Patron CSR | 8% | 4.8/8 | +2.0 pts |
| 🟡 MEDIA | IE 2.1.2 CRUD completo | 8% | 4.8/8 | +2.0 pts |
| ✅ OK | IE 2.2.2 Bean Validation | 8% | 8/8 | - |
| ✅ OK | IE 2.3.2 Logs | 7% | 7/7 | - |
| ✅ OK | IE 2.5.1 GitHub | 7% | 7/7 | - |
| ✅ OK | IE 2.5.2 Trello | 5% | 5/5 | - |

---

## Instruccion final para Codex

Por favor:
1. Lee este archivo (EP2_CONTEXT.md)
2. Escanea todo el codigo fuente del proyecto
3. Genera un reporte en un archivo EP2_REPORTE.md con:
   - Por cada item de la rubrica: estado actual (✅ / ⚠️ / ❌)
   - Explicacion de lo que esta mal (con referencias a archivos: `src/main/java/...`)
   - Prioridad de cada fix
4. NO modifiques ni generes codigo. Solo analiza y reporta.
