# Resumen de Cobertura JaCoCo — EP3 (Auditoría Final)

**Proyecto:** EcoMarket SPA  
**Fecha de Validación:** Julio 2026  
**Estrategia de Control de Versiones:** Modelo **Polyrepo** (Repositorios Git independientes por Microservicio y Módulo Transversal)

A continuación se detalla el resultado final de la auditoría de pruebas unitarias y de integración (JUnit 5 + Mockito + Spring Boot Test) y el porcentaje de cobertura de código (Instruction Coverage) alcanzado en cada microservicio mediante **JaCoCo**. En coherencia con la arquitectura **Polyrepo** del ecosistema EcoMarket SPA, cada servicio fue evaluado de forma autónoma y descentralizada en su respectivo repositorio. La métrica mínima exigida por la rúbrica (IE 3.1.1) es **80%**, y el equipo ha superado este objetivo logrando **100% de cobertura** en todos los módulos de lógica de negocio.

| Microservicio (Repositorio) | Responsable | Pruebas Ejecutadas | Cobertura JaCoCo | Estado |
|---|---|:---:|:---:|:---:|
| **api-gateway** | Ignacio Valeria | 4 tests | N/A (Smoke & Route Tests) | ✅ OK |
| **ms-usuarios-identidad** | Benjamín Flores | 92 tests | **100%** | ✅ OK |
| **ms-catalogo** | Benjamín Espinoza | 75 tests | **100%** | ✅ OK |
| **ms-inventario-abastecimiento** | Benjamín Palma | 119 tests | **100%** | ✅ OK |
| **ms-pedidos-ventas** | Ignacio Valeria | 174 tests | **100%** | ✅ OK |
| **ms-logistica-envios** | Benjamín Espinoza | 98 tests | **100%** | ✅ OK |
| **ms-administracion-soporte** | Benjamín Flores | 85 tests | **100%** | ✅ OK |
| **ms-reportes** | Benjamín Palma | 70 tests | **100%** | ✅ OK |
| **TOTAL ECOSISTEMA** | **Equipo EcoMarket** | **717 tests** | **100% Promedio** | ✅ **APROBADO** |

---

## Puntos Clave de la Revisión Técnica (Enfoque Polyrepo)

1. **Autonomía y Desacoplamiento por Repositorio:** Al utilizar un modelo **Polyrepo**, cada responsable pudo desarrollar, refactorizar y validar su microservicio con ciclos de build (`mvn test`) y reportes JaCoCo completamente independientes, sin riesgo de cuellos de botella ni bloqueos entre repositorios en el control de versiones.
2. **Contexto Realista y Honesto:** Todas las pruebas unitarias en cada uno de los repositorios fueron validadas y enriquecidas utilizando datos reales del dominio del caso de estudio *EcoMarket SPA* (RUT chilenos válidos, precios en CLP, catálogos ecológicos como "Bolsa biodegradable", sucursales Lastarria/Valdivia/Antofagasta y roles administrativos reales), cumpliendo la regla de honestidad técnica exigida.
3. **Cero Deuda de Consola:** Se verificó de manera estricta la ausencia total de llamados a `System.out.println` o `printStackTrace()` en las clases Java del código fuente en todos los repositorios, respetando el estándar corporativo de logging mediante SLF4J/Logback.
4. **Pruebas de Contrato y E2E Transversales:** Para garantizar que la integración entre los distintos repositorios funcione sin problemas, se cuenta con la suite transversal en Postman (`postman/EcoMarket-E2E.postman_collection.json`), la cual ejecuta el flujo de negocio completo (login → catálogo → carrito → pedido → inventario → envíos → reportes) con assertions que validan los contratos REST inter-servicios.

> **Nota:** Las capturas individuales de cada reporte (HTML/CSV) y los logs de ejecución de `mvn test` se adjuntan como evidencias técnicas de la validación independiente por repositorio, demostrando el cumplimiento del **DoD** para la defensa técnica EP3.
