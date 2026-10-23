# approach.md — Qué debe contener el proyecto

**Proyecto:** Pruebas de Software, Sistema de Gestión para la Producción de Hongos Ostra (Ostra Verde S.A.)
**Curso:** ISW-622 Pruebas de Software · UTN Sede San Carlos · valor 15%
**Facilitador:** Víctor Iván Zúñiga Zúñiga
**Equipo:** Roney Ruiz · Fabián Zamora

Este archivo es la base para el PDF. Resume todo lo que el proyecto tiene que entregar y cómo se evalúa.

---

## 1. Idea general

Somos el equipo de QA y desarrollo. Hay que analizar los requerimientos, diseñar casos de prueba, hacer pruebas unitarias con **AAA** y **TDD**, y dejar la trazabilidad completa entre requerimientos y pruebas.

### Contexto

Ostra Verde S.A. produce y comercializa hongos ostra para el mercado local y nacional. Hoy lleva sus registros en hojas de cálculo y formularios impresos, lo que dificulta controlar las actividades agrícolas, dar seguimiento a las aplicaciones de productos y medir la productividad de cada sección. La gerencia quiere un sistema que administre de forma centralizada la siembra, el mantenimiento y la cosecha.

Como equipo de QA y desarrollo, la responsabilidad es analizar los requerimientos, diseñar la estrategia y los casos de prueba, implementar pruebas unitarias automatizadas (AAA y TDD) y documentar la trazabilidad completa entre requerimientos y pruebas. En este repositorio la lógica de negocio es simulada y vive en memoria (S-16).

### Descripción del sistema

El sistema cubre siete procesos. Los requerimientos, reglas y casos de cada uno están en `testing.md`.

**Paso 1. Gestión de secciones de producción.** La finca se divide en secciones de cultivo.

- Datos: código único, nombre, capacidad máxima de producción y estado (Disponible, Ocupada o En mantenimiento).
- Permite: registrar secciones, modificar información existente, consultar las secciones disponibles para nuevas siembras y evitar dos secciones con el mismo código.

**Paso 2. Registro de personal.** Registra a las personas involucradas en el proceso productivo.

- Datos: identificación, nombre completo, teléfono, correo electrónico y cargo.
- Permite: registrar, consultar y actualizar trabajadores, e impedir dos colaboradores con la misma identificación.

**Paso 3. Registro de siembras.** Se registra cada vez que se inicia un cultivo.

- Datos: código de siembra, fecha, sección utilizada, cantidad de sustratos sembrados, responsable principal y observaciones.
- Reglas: solo secciones disponibles; no fecha futura; sustratos mayores que cero; una sección ocupada no recibe otra siembra simultánea.

**Paso 4. Gestión de productos agrícolas.** Productos que se usan para el mantenimiento y crecimiento del cultivo.

- Datos: código, nombre, tipo de producto, descripción y unidad de medida.
- Permite: registrar, modificar y consultar productos, y eliminar los que nunca hayan sido utilizados.

**Paso 5. Registro de aplicaciones.** Aplicaciones de productos a los cultivos durante el ciclo productivo.

- Datos: código de aplicación, fecha, siembra asociada, producto aplicado, cantidad utilizada, trabajador responsable y observaciones.
- Reglas: la siembra debe existir; el producto debe existir; cantidad mayor a cero; no se registran aplicaciones sobre cultivos ya cosechados.

**Paso 6. Registro de cosechas.** Se registra cuando el cultivo alcanza su punto óptimo.

- Datos: código de cosecha, fecha, siembra asociada, peso total cosechado (kg), cantidad de bandejas y responsable.
- Reglas: una siembra solo se cosecha una vez; peso mayor a cero; la cosecha ocurre después de la fecha de siembra; al registrarla, la sección vuelve a estar disponible.

**Paso 7. Reportes gerenciales.** Producción total por período, producción por sección, producción por trabajador, historial de aplicaciones realizadas y rendimiento de cultivos.

---

## 2. Estructura sugerida del documento (PDF)

1. **Portada:** universidad, sede, curso, proyecto, facilitador, estudiantes, fecha.
2. **Introducción:** contexto de Ostra Verde S.A. y qué cubre el documento.
3. **Metodología:** párrafo introductorio, redactada en futuro, siguiendo el orden de las fases.
4. **Fase 1: Análisis de requerimientos**
5. **Fase 2: Diseño de casos de prueba**
6. **Fase 3: Matriz de trazabilidad inicial**
7. **Fase 4: Pruebas unitarias (AAA + TDD)** con enlace al repositorio Git
8. **Fase 5: Bitácora y evidencias**
9. **Fase 6: Matriz de trazabilidad final**
10. **Lecciones aprendidas y dificultades**
11. **Conclusiones**

---

## 3. Entregables por fase

| Fase | Qué se hace | Entregable |
| --- | --- | --- |
| 1 | Leer el enunciado; identificar requerimientos funcionales, no funcionales, reglas de negocio y restricciones; ID único para cada uno | Sección de especificación de requerimientos |
| 2 | Por cada requerimiento: casos positivos, negativos y límite, con datos de entrada, resultado esperado, resultado obtenido y demás elementos vistos en clase | Catálogo de casos de prueba |
| 3 | Relacionar requerimiento, caso de prueba y estado de cobertura | Primera versión de la matriz de trazabilidad |
| 4 | Pruebas unitarias con AAA y TDD (Red, Green, Refactor) | Repositorio Git con las pruebas |
| 5 | Documentar requerimientos, casos, evidencias de ejecución, capturas, resultados, AAA y TDD | Bitácora del proyecto |
| 6 | Matriz final con ID, descripción, caso asociado, prueba unitaria asociada, estado de ejecución y observaciones | Matriz de trazabilidad final |

Fases 1 a 3 ya están en el Word. Este PDF debe cubrir también 4, 5 y 6.

---

## 4. Qué debe contener cada parte

### Fase 1: Requerimientos

- 18 requerimientos funcionales (RF-01 a RF-18)
- 4 requerimientos no funcionales (RNF-01 a RNF-04)
- 18 reglas de negocio (RN-01 a RN-18)
- 3 restricciones (RES-01 a RES-03)
- Supuestos (S-01 a S-16) para lo que el enunciado no define
- Convención de identificadores

### Fase 2: Casos de prueba

- Estrategia (positivo, negativo, límite)
- 131 casos con: ID, requerimientos, tipo, precondición, datos de entrada, resultado esperado, resultado obtenido
- Conjunto de datos D-01 para los reportes

### Fase 3 y 6: Matrices

- Inicial: requerimiento, casos asociados, P/N/L, estado de cobertura, y resumen de cobertura (100%)
- Final: lo anterior más prueba unitaria asociada, estado de ejecución y observaciones
- Ningún requerimiento sin prueba y ningún caso sin evidencia de ejecución

### Fase 4: Pruebas unitarias

**Patrón AAA** en cada prueba:

- **Arrange:** preparar datos y configuración
- **Act:** ejecutar la funcionalidad
- **Assert:** validar el resultado esperado

**TDD** por funcionalidad:

- **Red:** escribir la prueba antes de implementar y verla fallar
- **Green:** código mínimo para que pase
- **Refactor:** mejorar el código sin cambiar el comportamiento

Evidencia de TDD: commits separados de Red, Green y Refactor en el repositorio Git.

Avance actual: 49 de 131 casos con prueba aprobada (Secciones, Personal y Productos). La lógica de Siembras, Aplicaciones, Cosechas y Reportes ya existe, pero sin pruebas. El detalle está en la sección 6 de `testing.md`.

### Fase 5: Bitácora

- Requerimientos identificados
- Casos de prueba diseñados
- Evidencias de ejecución y capturas de pantalla
- Resultados obtenidos
- Cómo se aplicó AAA
- Cómo se aplicó TDD

---

## 5. Exposición presencial (15 a 20 minutos)

Participan todos los integrantes. Hay que explicar:

1. Comprensión del problema
2. Requerimientos identificados
3. Estrategia de pruebas
4. Casos de prueba elaborados
5. Aplicación de AAA
6. Aplicación de TDD
7. Evidencias de ejecución
8. Matriz de trazabilidad final
9. Lecciones aprendidas
10. Principales dificultades

---

## 6. Rúbrica (100%)

| Criterio | % |
| --- | --- |
| Requerimientos funcionales y no funcionales | 15 |
| Reglas de negocio y restricciones | 10 |
| Calidad y cobertura de casos de prueba | 15 |
| Matriz de trazabilidad inicial y final | 10 |
| Pruebas unitarias con AAA | 10 |
| TDD (Red-Green-Refactor) | 10 |
| Evidencias y documentación | 10 |
| Organización, calidad técnica y estructura | 5 |
| Exposición: dominio del tema | 5 |
| Exposición: explicación técnica y defensa | 5 |
| Exposición: participación equilibrada | 5 |

---

## 7. Descuentos a evitar

| Área | Descuento |
| --- | --- |
| Requerimiento omitido | -1 por cada uno |
| Requerimiento ambiguo | -0,5 |
| Regla de negocio faltante | -0,5 por cada una |
| Caso sin resultado esperado | -0,5 |
| Cobertura incompleta | hasta -5 |
| Falta de escenarios negativos | hasta -3 |
| Prueba sin patrón AAA | -1 por prueba |
| Falta de evidencias de TDD | hasta -5 |
| Prueba que no valida resultados | -1 por prueba |
| Requerimiento sin asociar a pruebas | -1 por cada uno |
| Caso sin evidencia de ejecución | -0,5 por caso |
| Participación mínima de un integrante | hasta -2 |
| Desconocimiento de la solución | hasta -5 |
| No poder justificar decisiones | hasta -3 |

---

## 8. Observaciones recurrentes de Víctor

- Metodología con párrafo introductorio y redactada en futuro
- No usar el prefijo "Instrucción"
- Numerar y dividir los pasos para poder reproducir la guía
- Ejemplo paso a paso con su propio subtítulo
- Recortar las capturas e incluir evidencia de los pasos
- Poner el nombre de quien generó el contenido
- Párrafos cortos

---

## 9. Checklist final

- [ ] Portada completa con nombres y fecha
- [ ] Introducción y metodología (futuro)
- [ ] Todos los requerimientos con ID único
- [ ] Reglas de negocio y restricciones completas
- [ ] Supuestos documentados
- [ ] Casos positivos, negativos y límite, todos con resultado esperado
- [ ] Matriz inicial con cobertura
- [ ] Repositorio Git con pruebas AAA
- [ ] Commits de Red, Green y Refactor
- [ ] Bitácora con capturas y resultados
- [ ] Matriz final sin requerimientos huérfanos
- [ ] Lecciones aprendidas y dificultades
- [ ] Preparada la exposición de 15 a 20 minutos, con participación de ambos
- [ ] PDF revisado, con capturas recortadas