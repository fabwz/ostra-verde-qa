# testing.md — Qué hay que testear en el proyecto

**Proyecto:** Sistema de Gestión para la Producción de Hongos Ostra (Ostra Verde S.A.)
**Curso:** ISW-622 Pruebas de Software · Facilitador: Víctor Iván Zúñiga Zúñiga
**Equipo:** Roney Ruiz · Fabián Zamora

Este archivo es la fuente de verdad de las pruebas: requerimientos, reglas de negocio, restricciones, supuestos, catálogo de casos, matriz de trazabilidad inicial y estado de avance. La descripción del sistema, los entregables y la rúbrica están en `approach.md`.

## Contenido

1. [Resumen y estrategia](#1-resumen-y-estrategia)
2. [Requerimientos](#2-requerimientos)
3. [Supuestos](#3-supuestos)
4. [Catálogo de casos de prueba](#4-catálogo-de-casos-de-prueba)
5. [Matriz de trazabilidad inicial](#5-matriz-de-trazabilidad-inicial)
6. [Estado de avance (Fase 4)](#6-estado-de-avance-fase-4)
7. [Decisiones de implementación](#7-decisiones-de-implementación)
8. [Servicios disponibles](#8-servicios-disponibles)
9. [Reglas de oro al ejecutar](#9-reglas-de-oro-al-ejecutar)

---

## 1. Resumen y estrategia

| Categoría | Cantidad |
| --- | --- |
| Requerimientos funcionales (RF) | 18 |
| Requerimientos no funcionales (RNF) | 4 |
| Reglas de negocio (RN) | 18 |
| Restricciones (RES) | 3 |
| Supuestos (S) | 16 |
| **Casos de prueba** | **131** (34 positivos, 54 negativos, 43 límite) |

Cada requerimiento tiene como mínimo un caso positivo, uno negativo y, cuando aplica, uno límite. Un mismo caso puede cubrir varios requerimientos, por eso la columna *Requerimientos* lista todos los que valida.

| Tipo | Qué valida |
| --- | --- |
| Positivo | Datos válidos que deben ser aceptados y producir el resultado esperado. |
| Negativo | Datos inválidos o situaciones prohibidas por una regla, que deben ser rechazados con un mensaje claro. |
| Límite | Valores justo en el borde de una regla (por ejemplo 0, 1, la fecha de hoy o la fecha de siembra). |

Los casos usan el formato `CP-XXX-##`, donde `XXX` es el módulo: SEC (secciones), PER (personal), SIE (siembras), PRO (productos), APL (aplicaciones), COS (cosechas), REP (reportes) y NFR (no funcionales y restricciones).

Las fechas se expresan de forma relativa a la fecha de ejecución cuando la regla depende de "hoy". La columna *Resultado obtenido* dice **Aprobado** (con la clase de prueba) cuando el caso ya tiene prueba unitaria en verde; en los demás casos queda como **Pendiente**.

---

## 2. Requerimientos

### 2.1 Convención de identificadores

| Prefijo | Significado |
| --- | --- |
| RF-## | Requerimiento funcional |
| RNF-## | Requerimiento no funcional |
| RN-## | Regla de negocio |
| RES-## | Restricción |
| S-## | Supuesto del equipo |

### 2.2 Requerimientos funcionales

Se agruparon según los siete pasos del enunciado. Se identificaron 18.

| ID | Módulo | Descripción |
| --- | --- | --- |
| RF-01 | Secciones | El sistema deberá permitir registrar una nueva sección de producción con código, nombre, capacidad máxima y estado (Disponible, Ocupada o En mantenimiento). |
| RF-02 | Secciones | El sistema deberá permitir modificar la información de una sección existente. |
| RF-03 | Secciones | El sistema deberá permitir consultar las secciones disponibles para nuevas siembras. |
| RF-04 | Personal | El sistema deberá permitir registrar un trabajador con identificación, nombre completo, teléfono, correo electrónico y cargo. |
| RF-05 | Personal | El sistema deberá permitir consultar los trabajadores registrados. |
| RF-06 | Personal | El sistema deberá permitir actualizar la información de un trabajador existente. |
| RF-07 | Siembras | El sistema deberá permitir registrar una siembra con código, fecha, sección utilizada, cantidad de sustratos, responsable principal y observaciones. Al registrarla, la sección pasa a estado Ocupada (ver S-01). |
| RF-08 | Productos | El sistema deberá permitir registrar un producto agrícola con código, nombre, tipo, descripción y unidad de medida. |
| RF-09 | Productos | El sistema deberá permitir modificar un producto agrícola existente. |
| RF-10 | Productos | El sistema deberá permitir consultar los productos agrícolas registrados. |
| RF-11 | Productos | El sistema deberá permitir eliminar un producto que nunca haya sido utilizado. |
| RF-12 | Aplicaciones | El sistema deberá permitir registrar una aplicación con código, fecha, siembra asociada, producto aplicado, cantidad utilizada, trabajador responsable y observaciones. |
| RF-13 | Cosechas | El sistema deberá permitir registrar una cosecha con código, fecha, siembra asociada, peso total (kg), cantidad de bandejas y responsable. Al registrarla, la sección de la siembra vuelve a estado Disponible. |
| RF-14 | Reportes | El sistema deberá generar el reporte de producción total por período. |
| RF-15 | Reportes | El sistema deberá generar el reporte de producción por sección. |
| RF-16 | Reportes | El sistema deberá generar el reporte de producción por trabajador. |
| RF-17 | Reportes | El sistema deberá generar el historial de aplicaciones realizadas. |
| RF-18 | Reportes | El sistema deberá generar el reporte de rendimiento de cultivos (ver S-11). |

### 2.3 Requerimientos no funcionales

El enunciado no los menciona de forma explícita, así que se derivaron del contexto del problema (registros manuales, necesidad de centralizar y de medir la productividad).

| ID | Categoría | Descripción |
| --- | --- | --- |
| RNF-01 | Centralización | La información de siembra, mantenimiento y cosecha deberá almacenarse de forma centralizada, de modo que un dato registrado en un módulo sea visible en los demás. |
| RNF-02 | Integridad de datos | El sistema no deberá guardar registros con campos obligatorios vacíos ni dejar datos parciales cuando una operación sea rechazada. |
| RNF-03 | Usabilidad | Ante un dato inválido, el sistema deberá mostrar un mensaje claro que indique el campo y el motivo, sin exponer detalles técnicos. |
| RNF-04 | Rendimiento | Los reportes gerenciales deberán generarse en un máximo de 3 segundos (valor propuesto, ver S-15). |

### 2.4 Reglas de negocio

Son 18 reglas. Las primeras 15 aparecen en el enunciado y las últimas 3 se infirieron a partir de él.

| ID | Módulo | Regla | Origen |
| --- | --- | --- | --- |
| RN-01 | Secciones | No pueden existir dos secciones con el mismo código. | Enunciado |
| RN-02 | Personal | No pueden existir dos trabajadores con la misma identificación. | Enunciado |
| RN-03 | Siembras | Solo pueden utilizarse secciones en estado Disponible. | Enunciado |
| RN-04 | Siembras | No puede registrarse una siembra con fecha futura. | Enunciado |
| RN-05 | Siembras | La cantidad de sustratos debe ser mayor que cero. | Enunciado |
| RN-06 | Siembras | Una sección ocupada no puede recibir otra siembra simultánea. | Enunciado |
| RN-07 | Productos | Solo pueden eliminarse productos que nunca hayan sido utilizados. | Enunciado |
| RN-08 | Aplicaciones | La siembra asociada a la aplicación debe existir. | Enunciado |
| RN-09 | Aplicaciones | El producto aplicado debe existir. | Enunciado |
| RN-10 | Aplicaciones | La cantidad aplicada debe ser mayor a cero. | Enunciado |
| RN-11 | Aplicaciones | No pueden registrarse aplicaciones para cultivos ya cosechados. | Enunciado |
| RN-12 | Cosechas | Una siembra solo puede cosecharse una vez. | Enunciado |
| RN-13 | Cosechas | El peso cosechado debe ser mayor a cero. | Enunciado |
| RN-14 | Cosechas | La fecha de cosecha debe ser posterior a la fecha de siembra (ver S-10). | Enunciado |
| RN-15 | Cosechas | Al registrar una cosecha, la sección vuelve al estado Disponible. | Enunciado |
| RN-16 | General | El responsable de una siembra, aplicación o cosecha debe ser un trabajador registrado. | Inferida (S-06) |
| RN-17 | General | Los códigos de siembra, producto, aplicación y cosecha no pueden repetirse dentro de su propio registro. | Inferida (S-05) |
| RN-18 | Cosechas | La siembra asociada a la cosecha debe existir. | Inferida |

### 2.5 Restricciones

| ID | Restricción |
| --- | --- |
| RES-01 | El estado de una sección solo puede ser Disponible, Ocupada o En mantenimiento. |
| RES-02 | Las pruebas unitarias deberán seguir el patrón AAA y desarrollarse con el enfoque TDD (Red, Green, Refactor). |
| RES-03 | El código y las pruebas deberán mantenerse en un repositorio Git. |

---

## 3. Supuestos

Estos puntos no están definidos en el enunciado. Se tomó la interpretación más razonable para poder diseñar las pruebas.

| ID | Supuesto |
| --- | --- |
| S-01 | Al registrar una siembra, la sección pasa a estado Ocupada. El enunciado solo indica que vuelve a Disponible al cosechar, por lo que se deduce el cambio inverso. |
| S-02 | Una siembra con la fecha de hoy es válida, porque la fecha de hoy no es futura. |
| S-03 | La capacidad máxima de una sección debe ser mayor que cero. |
| S-04 | El teléfono tiene 8 dígitos numéricos y el correo debe tener formato válido (usuario, @ y dominio). |
| S-05 | Los códigos de siembra, producto, aplicación y cosecha son únicos, igual que el de las secciones. |
| S-06 | El responsable de una siembra, aplicación o cosecha debe estar registrado en el módulo de personal. |
| S-07 | La cantidad de sustratos y la cantidad de bandejas son números enteros mayores que cero. |
| S-08 | Las observaciones y la descripción del producto son opcionales; el resto de campos son obligatorios. |
| S-09 | La cantidad aplicada de un producto admite decimales, porque depende de la unidad de medida. |
| S-10 | "Posterior a la fecha de siembra" se interpreta como estrictamente posterior (la misma fecha no es válida). |
| S-11 | Rendimiento de un cultivo = kilogramos cosechados / cantidad de sustratos sembrados. |
| S-12 | Los reportes por período usan la fecha de cosecha, e incluyen las fechas de inicio y de fin. |
| S-13 | Un producto "utilizado" es el que tiene al menos una aplicación registrada. |
| S-14 | No se valida que la cantidad de sustratos sea menor o igual a la capacidad máxima de la sección, porque el enunciado no lo pide. |
| S-15 | El tiempo máximo de 3 segundos para reportes es un valor propuesto por el equipo, ya que el enunciado no define uno. |
| S-16 | Los datos se guardan en memoria (repositorios con un Map), sin base de datos, porque el enunciado no define el almacenamiento. Las pruebas unitarias se apoyan en esa implementación. |

Los supuestos **S-01, S-03, S-04, S-07, S-14 y S-15** son los que más conviene confirmar con el facilitador, porque cambian directamente el resultado esperado de algunos casos.

---

## 4. Catálogo de casos de prueba

Columnas: ID del caso, requerimientos que valida, tipo, precondición, datos de entrada, resultado esperado y resultado obtenido.

### 4.1 Secciones (18)

| ID caso | Requerimientos | Tipo | Precondición | Datos de entrada | Resultado esperado | Resultado obtenido |
| --- | --- | --- | --- | --- | --- | --- |
| CP-SEC-01 | RF-01, RN-01, RES-01 | Positivo | No existe la sección S-01 | Código S-01; nombre Sala Norte; capacidad 500; estado Disponible | Sección guardada y visible en la consulta de secciones | Aprobado (SeccionServiceTest) |
| CP-SEC-02 | RF-01 | Negativo | — | Código vacío; nombre Sala Norte; capacidad 500; estado Disponible | Registro rechazado; el mensaje indica que el código es obligatorio | Aprobado (SeccionServiceTest) |
| CP-SEC-03 | RF-01 | Límite | — | Código S-02; nombre Sala Sur; capacidad 1; estado Disponible | Sección guardada (capacidad mínima válida) | Aprobado (SeccionServiceTest) |
| CP-SEC-04 | RF-01 | Límite | — | Código S-03; nombre Sala Oeste; capacidad 0; estado Disponible | Registro rechazado; la capacidad debe ser mayor que cero | Aprobado (SeccionServiceTest) |
| CP-SEC-05 | RF-01 | Negativo | — | Código S-03; nombre Sala Oeste; capacidad -10; estado Disponible | Registro rechazado; la capacidad debe ser mayor que cero | Aprobado (SeccionServiceTest) |
| CP-SEC-06 | RF-01, RNF-02 | Negativo | — | Código S-03; nombre vacío; capacidad 500; estado Disponible | Registro rechazado; el nombre es obligatorio | Aprobado (SeccionServiceTest) |
| CP-SEC-07 | RN-01 | Negativo | Existe S-01 | Código S-01; nombre Sala Este; capacidad 300; estado Disponible | Registro rechazado; el mensaje indica que el código ya existe; S-01 conserva sus datos | Aprobado (SeccionServiceTest) |
| CP-SEC-08 | RN-01 | Positivo | Existe S-01 con nombre Sala Norte | Código S-04; nombre Sala Norte; capacidad 300; estado Disponible | Sección guardada (la unicidad aplica solo al código) | Aprobado (SeccionServiceTest) |
| CP-SEC-09 | RN-01 | Límite | Existe S-01 | Código S-011; nombre Sala Centro; capacidad 300; estado Disponible | Sección guardada (código distinto por un carácter) | Aprobado (SeccionServiceTest) |
| CP-SEC-10 | RES-01 | Positivo | — | Código S-05; nombre Sala Techo; capacidad 200; estado En mantenimiento | Sección guardada con estado En mantenimiento | Aprobado (SeccionServiceTest) |
| CP-SEC-11 | RES-01 | Negativo | — | Código S-06; nombre Sala Patio; capacidad 200; estado Cerrada | Registro rechazado; el estado no es uno de los tres permitidos | Aprobado (SeccionServiceTest) |
| CP-SEC-12 | RF-02 | Positivo | Existe S-01 | Código S-01; nuevo nombre Sala Norte 2; nueva capacidad 600 | Cambios guardados; la consulta muestra los nuevos valores | Aprobado (SeccionServiceTest) |
| CP-SEC-13 | RF-02 | Negativo | No existe S-99 | Modificar la sección S-99 | Operación rechazada; el mensaje indica que la sección no existe | Aprobado (SeccionServiceTest) |
| CP-SEC-14 | RF-02, RN-01 | Negativo | Existen S-01 y S-02 | Cambiar el código de S-02 a S-01 | Operación rechazada; el código ya existe; S-02 conserva su código | Aprobado (SeccionServiceTest) |
| CP-SEC-15 | RF-02 | Límite | Existe S-01 con capacidad 500 | Nueva capacidad 0 | Operación rechazada; S-01 conserva la capacidad 500 | Aprobado (SeccionServiceTest) |
| CP-SEC-16 | RF-03 | Positivo | S-01 Disponible; S-02 Ocupada; S-03 En mantenimiento | Consultar secciones disponibles | Devuelve únicamente S-01 | Aprobado (SeccionServiceTest) |
| CP-SEC-17 | RF-03 | Negativo | Todas las secciones están Ocupadas o En mantenimiento | Consultar secciones disponibles | Lista vacía con mensaje informativo; sin error | Aprobado (SeccionServiceTest) |
| CP-SEC-18 | RF-03 | Límite | Solo S-01 está Disponible | Consultar secciones disponibles | Lista con exactamente un elemento (S-01) | Aprobado (SeccionServiceTest) |

### 4.2 Personal (15)

| ID caso | Requerimientos | Tipo | Precondición | Datos de entrada | Resultado esperado | Resultado obtenido |
| --- | --- | --- | --- | --- | --- | --- |
| CP-PER-01 | RF-04, RN-02 | Positivo | No existe la identificación 101230456 | Identificación 101230456; nombre Ana Mora Solís; teléfono 88881234; correo ana.mora@ostraverde.cr; cargo Operaria de cosecha | Trabajador guardado y visible en la consulta | Aprobado (TrabajadorServiceTest) |
| CP-PER-02 | RF-04, RNF-02 | Negativo | — | Mismos datos del caso anterior, con el cargo vacío | Registro rechazado; el cargo es obligatorio | Aprobado (TrabajadorServiceTest) |
| CP-PER-03 | RF-04 | Negativo | — | Correo ana.mora.ostraverde.cr (sin @) | Registro rechazado; el correo no tiene formato válido | Aprobado (TrabajadorServiceTest) |
| CP-PER-04 | RF-04 | Límite | — | Teléfono 8888123 (7 dígitos) | Registro rechazado; el teléfono debe tener 8 dígitos | Aprobado (TrabajadorServiceTest) |
| CP-PER-05 | RF-04 | Negativo | — | Teléfono 8888abcd | Registro rechazado; el teléfono debe ser numérico | Aprobado (TrabajadorServiceTest) |
| CP-PER-06 | RN-02 | Negativo | Existe 101230456 (Ana Mora Solís) | Identificación 101230456; nombre Luis Vega Rojas; resto de datos válidos | Registro rechazado; la identificación ya está registrada; los datos de Ana no cambian | Aprobado (TrabajadorServiceTest) |
| CP-PER-07 | RN-02 | Positivo | Existe 101230456 (Ana Mora Solís) | Identificación 404560789; nombre Ana Mora Solís; resto de datos válidos | Trabajador guardado (mismo nombre, identificación distinta) | Aprobado (TrabajadorServiceTest) |
| CP-PER-08 | RN-02 | Límite | Existe 101230456 | Identificación 101230457; nombre Luis Vega Rojas; resto de datos válidos | Trabajador guardado (identificación distinta por un dígito) | Aprobado (TrabajadorServiceTest) |
| CP-PER-09 | RF-05 | Positivo | Existe 101230456 | Consultar por identificación 101230456 | Devuelve los cinco datos de Ana Mora Solís | Aprobado (TrabajadorServiceTest) |
| CP-PER-10 | RF-05 | Negativo | No existe 999999999 | Consultar por identificación 999999999 | Mensaje de trabajador no encontrado | Aprobado (TrabajadorServiceTest) |
| CP-PER-11 | RF-05 | Límite | No hay trabajadores registrados | Consultar el listado de trabajadores | Lista vacía; sin error | Aprobado (TrabajadorServiceTest) |
| CP-PER-12 | RF-06 | Positivo | Existe 101230456 | Nuevo teléfono 70001234; nuevo cargo Supervisora | Cambios guardados; la consulta muestra los nuevos valores | Aprobado (TrabajadorServiceTest) |
| CP-PER-13 | RF-06 | Negativo | No existe 999999999 | Actualizar el trabajador 999999999 | Operación rechazada; el trabajador no existe | Aprobado (TrabajadorServiceTest) |
| CP-PER-14 | RF-06, RN-02 | Negativo | Existen 101230456 y 202340567 | Cambiar la identificación de 202340567 a 101230456 | Operación rechazada; la identificación ya está registrada | Aprobado (TrabajadorServiceTest) |
| CP-PER-15 | RF-06 | Límite | Existe 101230456 | Actualizar sin modificar ningún dato | Operación aceptada; el registro queda sin cambios | Aprobado (TrabajadorServiceTest) |

### 4.3 Siembras (21)

*Precondición general: existen el trabajador 101230456 y la sección S-01 en estado Disponible, salvo que el caso indique otra cosa. "Hoy" es la fecha en que se ejecuta la prueba.*

| ID caso | Requerimientos | Tipo | Precondición | Datos de entrada | Resultado esperado | Resultado obtenido |
| --- | --- | --- | --- | --- | --- | --- |
| CP-SIE-01 | RF-07, RN-03, RN-04, RN-05, RN-16 | Positivo | S-01 Disponible; existe el trabajador 101230456 | Código SI-001; fecha: hace 3 días; sección S-01; sustratos 200; responsable 101230456; observaciones Lote inicial | Siembra guardada | Aprobado (SiembraServiceTest) |
| CP-SIE-02 | RF-07, RN-06 | Positivo | Siembra SI-001 recién registrada | Consultar el estado de S-01 | S-01 queda en estado Ocupada | Aprobado (SiembraServiceTest) |
| CP-SIE-03 | RF-07, RN-03 | Negativo | S-03 En mantenimiento | Código SI-002; sección S-03; resto de datos válidos | Registro rechazado; la sección no está disponible | Aprobado (SiembraServiceTest) |
| CP-SIE-04 | RF-07 | Negativo | No existe la sección S-99 | Código SI-002; sección S-99; resto de datos válidos | Registro rechazado; la sección no existe | Aprobado (SiembraServiceTest) |
| CP-SIE-05 | RN-04 | Positivo | — | Fecha: hace 30 días; resto de datos válidos | Siembra guardada | Aprobado (SiembraServiceTest) |
| CP-SIE-06 | RN-04 | Límite | — | Fecha: hoy; resto de datos válidos | Siembra guardada (hoy no es fecha futura) | Aprobado (SiembraServiceTest) |
| CP-SIE-07 | RN-04 | Límite | — | Fecha: mañana; resto de datos válidos | Registro rechazado; la fecha es futura | Aprobado (SiembraServiceTest) |
| CP-SIE-08 | RN-04 | Negativo | — | Fecha: dentro de 1 año; resto de datos válidos | Registro rechazado; la fecha es futura | Aprobado (SiembraServiceTest) |
| CP-SIE-09 | RN-05 | Límite | — | Sustratos 1; resto de datos válidos | Siembra guardada (valor mínimo válido) | Aprobado (SiembraServiceTest) |
| CP-SIE-10 | RN-05 | Límite | — | Sustratos 0; resto de datos válidos | Registro rechazado; los sustratos deben ser mayores que cero | Aprobado (SiembraServiceTest) |
| CP-SIE-11 | RN-05 | Negativo | — | Sustratos -5; resto de datos válidos | Registro rechazado; los sustratos deben ser mayores que cero | Aprobado (SiembraServiceTest) |
| CP-SIE-12 | RN-05 | Negativo | — | Sustratos 2.5; resto de datos válidos | Registro rechazado; los sustratos deben ser un número entero | Pendiente (sin prueba: el campo es int) |
| CP-SIE-13 | RN-06 | Negativo | S-01 Ocupada con la siembra SI-001 | Código SI-002; sección S-01; resto de datos válidos | Registro rechazado; la sección está ocupada; SI-001 no cambia | Aprobado (SiembraServiceTest) |
| CP-SIE-14 | RN-06, RN-03, RN-15 | Positivo | SI-001 ya fue cosechada y S-01 volvió a Disponible | Código SI-003; sección S-01; resto de datos válidos | Siembra guardada; S-01 vuelve a quedar Ocupada | Aprobado (SiembraServiceTest) |
| CP-SIE-15 | RN-06 | Límite | S-01 y S-02 Disponibles | Dos siembras el mismo día, una en S-01 y otra en S-02 | Ambas guardadas; ninguna afecta a la otra | Aprobado (SiembraServiceTest) |
| CP-SIE-16 | RF-07, RN-16 | Negativo | No existe el trabajador 999999999 | Responsable 999999999; resto de datos válidos | Registro rechazado; el responsable no está registrado | Aprobado (SiembraServiceTest) |
| CP-SIE-17 | RN-16 | Límite | Trabajador 303450678 recién registrado | Responsable 303450678; resto de datos válidos | Siembra guardada | Aprobado (SiembraServiceTest) |
| CP-SIE-18 | RF-07, RN-17 | Negativo | Existe SI-001; S-02 Disponible | Código SI-001; sección S-02; resto de datos válidos | Registro rechazado; el código ya existe | Aprobado (SiembraServiceTest) |
| CP-SIE-19 | RN-17 | Límite | Existe SI-001; S-02 Disponible | Código SI-0011; sección S-02; resto de datos válidos | Siembra guardada (código distinto por un carácter) | Aprobado (SiembraServiceTest) |
| CP-SIE-20 | RF-07 | Límite | — | Observaciones vacías; resto de datos válidos | Siembra guardada (campo opcional) | Aprobado (SiembraServiceTest) |
| CP-SIE-21 | RF-07, RNF-02 | Negativo | — | Código de siembra vacío; resto de datos válidos | Registro rechazado; el código es obligatorio | Aprobado (SiembraServiceTest) |

### 4.4 Productos (16)

| ID caso | Requerimientos | Tipo | Precondición | Datos de entrada | Resultado esperado | Resultado obtenido |
| --- | --- | --- | --- | --- | --- | --- |
| CP-PRO-01 | RF-08, RN-17 | Positivo | No existe PR-01 | Código PR-01; nombre Cal agrícola; tipo Desinfectante; descripción Desinfección de sustrato; unidad kg | Producto guardado y visible en la consulta | Aprobado (ProductoServiceTest) |
| CP-PRO-02 | RF-08, RN-17 | Negativo | Existe PR-01 | Código PR-01; nombre Fungicida; tipo Fungicida; unidad litro | Registro rechazado; el código ya existe | Aprobado (ProductoServiceTest) |
| CP-PRO-03 | RN-17 | Positivo | Existe la sección S-01 | Producto con código S-01 (igual al de una sección) | Producto guardado (la unicidad aplica dentro de cada tipo de registro) | Aprobado (ProductoServiceTest) |
| CP-PRO-04 | RF-08, RNF-02 | Negativo | — | Código PR-03; unidad de medida vacía; resto de datos válidos | Registro rechazado; la unidad es obligatoria | Aprobado (ProductoServiceTest) |
| CP-PRO-05 | RF-08 | Límite | — | Código PR-02; descripción vacía; resto de datos válidos | Producto guardado (campo opcional) | Aprobado (ProductoServiceTest) |
| CP-PRO-06 | RF-09 | Positivo | Existe PR-01 | Nuevo nombre Cal hidratada; nueva descripción Desinfección de sustrato y paredes | Cambios guardados; la consulta muestra los nuevos valores | Aprobado (ProductoServiceTest) |
| CP-PRO-07 | RF-09 | Negativo | No existe PR-99 | Modificar el producto PR-99 | Operación rechazada; el producto no existe | Aprobado (ProductoServiceTest) |
| CP-PRO-08 | RF-09 | Límite | PR-01 con una aplicación registrada | Nueva descripción | Cambios guardados; la aplicación sigue asociada a PR-01 | Aprobado (ProductoServiceTest) |
| CP-PRO-09 | RF-10 | Positivo | Existe PR-01 | Consultar PR-01 | Devuelve los cinco datos del producto | Aprobado (ProductoServiceTest) |
| CP-PRO-10 | RF-10 | Negativo | No existe PR-99 | Consultar PR-99 | Mensaje de producto no encontrado | Aprobado (ProductoServiceTest) |
| CP-PRO-11 | RF-10 | Límite | No hay productos registrados | Consultar el listado de productos | Lista vacía; sin error | Aprobado (ProductoServiceTest) |
| CP-PRO-12 | RF-11, RN-07 | Positivo | PR-02 sin aplicaciones | Eliminar PR-02 | Producto eliminado; ya no aparece en las consultas | Aprobado (ProductoServiceTest) |
| CP-PRO-13 | RN-07 | Negativo | PR-01 con 3 aplicaciones | Eliminar PR-01 | Operación rechazada; el producto fue utilizado; PR-01 sigue existiendo | Aprobado (ProductoServiceTest) |
| CP-PRO-14 | RN-07 | Límite | PR-01 con exactamente 1 aplicación | Eliminar PR-01 | Operación rechazada (un solo uso es suficiente) | Aprobado (ProductoServiceTest) |
| CP-PRO-15 | RF-11 | Negativo | No existe PR-99 | Eliminar PR-99 | Operación rechazada; el producto no existe | Aprobado (ProductoServiceTest) |
| CP-PRO-16 | RF-11 | Límite | Solo existe PR-02, sin aplicaciones | Eliminar PR-02 | Producto eliminado; el listado queda vacío | Aprobado (ProductoServiceTest) |

### 4.5 Aplicaciones (13)

*Precondición general: existen la siembra SI-001 sin cosecha, el producto PR-01 y el trabajador 101230456, salvo que el caso indique otra cosa.*

| ID caso | Requerimientos | Tipo | Precondición | Datos de entrada | Resultado esperado | Resultado obtenido |
| --- | --- | --- | --- | --- | --- | --- |
| CP-APL-01 | RF-12, RN-08, RN-09, RN-10, RN-11, RN-16 | Positivo | Datos generales | Código AP-001; fecha hoy; siembra SI-001; producto PR-01; cantidad 2.5; trabajador 101230456; observaciones Aplicación semanal | Aplicación guardada; aparece en el historial de aplicaciones | Aprobado (AplicacionServiceTest) |
| CP-APL-02 | RN-08 | Negativo | No existe la siembra SI-999 | Siembra SI-999; resto de datos válidos | Registro rechazado; la siembra no existe | Aprobado (AplicacionServiceTest) |
| CP-APL-03 | RF-12, RN-08 | Límite | SI-001 registrada hoy | Aplicación sobre SI-001 con fecha de hoy | Aplicación guardada | Aprobado (AplicacionServiceTest) |
| CP-APL-04 | RN-09 | Negativo | No existe el producto PR-999 | Producto PR-999; resto de datos válidos | Registro rechazado; el producto no existe | Aprobado (AplicacionServiceTest) |
| CP-APL-05 | RN-09 | Límite | Producto PR-03 recién registrado | Producto PR-03; resto de datos válidos | Aplicación guardada | Aprobado (AplicacionServiceTest) |
| CP-APL-06 | RN-10 | Límite | — | Cantidad 0.01; resto de datos válidos | Aplicación guardada (valor mínimo positivo) | Aprobado (AplicacionServiceTest) |
| CP-APL-07 | RN-10 | Límite | — | Cantidad 0; resto de datos válidos | Registro rechazado; la cantidad debe ser mayor que cero | Aprobado (AplicacionServiceTest) |
| CP-APL-08 | RN-10 | Negativo | — | Cantidad -1; resto de datos válidos | Registro rechazado; la cantidad debe ser mayor que cero | Aprobado (AplicacionServiceTest) |
| CP-APL-09 | RN-11 | Negativo | SI-001 con cosecha registrada | Aplicación sobre SI-001 | Registro rechazado; el cultivo ya fue cosechado | Aprobado (AplicacionServiceTest) |
| CP-APL-10 | RN-11 | Límite | SI-001 cosechada hoy | Aplicación sobre SI-001 con fecha de hoy | Registro rechazado (la cosecha ya existe aunque sea el mismo día) | Aprobado (AplicacionServiceTest) |
| CP-APL-11 | RF-12, RN-16 | Negativo | No existe el trabajador 999999999 | Trabajador 999999999; resto de datos válidos | Registro rechazado; el trabajador no está registrado | Aprobado (AplicacionServiceTest) |
| CP-APL-12 | RF-12, RN-17 | Negativo | Existe AP-001 | Código AP-001; resto de datos válidos | Registro rechazado; el código ya existe | Aprobado (AplicacionServiceTest) |
| CP-APL-13 | RF-12, RNF-02 | Negativo | — | Fecha de aplicación vacía; resto de datos válidos | Registro rechazado; la fecha es obligatoria | Aprobado (AplicacionServiceTest) |

### 4.6 Cosechas (17)

*Precondición general: existe la siembra SI-001 (200 sustratos) en la sección S-01 Ocupada, sembrada hace 30 días y sin cosecha, y existe el trabajador 101230456, salvo que el caso indique otra cosa.*

| ID caso | Requerimientos | Tipo | Precondición | Datos de entrada | Resultado esperado | Resultado obtenido |
| --- | --- | --- | --- | --- | --- | --- |
| CP-COS-01 | RF-13, RN-12, RN-13, RN-14, RN-16, RN-18 | Positivo | Datos generales | Código CO-001; fecha hoy; siembra SI-001; peso 45.5 kg; bandejas 30; responsable 101230456 | Cosecha guardada | Aprobado (CosechaServiceTest) |
| CP-COS-02 | RN-15 | Positivo | Cosecha CO-001 recién registrada | Consultar el estado de S-01 y las secciones disponibles | S-01 queda Disponible y aparece en la consulta de secciones disponibles | Aprobado (CosechaServiceTest) |
| CP-COS-03 | RN-12 | Negativo | SI-001 ya cosechada | Código CO-002; siembra SI-001; resto de datos válidos | Registro rechazado; la siembra ya fue cosechada | Aprobado (CosechaServiceTest) |
| CP-COS-04 | RN-12 | Límite | SI-001 cosechada hoy | Segunda cosecha de SI-001 con fecha de hoy | Registro rechazado (el límite es una cosecha por siembra) | Aprobado (CosechaServiceTest) |
| CP-COS-05 | RN-13 | Límite | — | Peso 0.01 kg; resto de datos válidos | Cosecha guardada (valor mínimo positivo) | Aprobado (CosechaServiceTest) |
| CP-COS-06 | RN-13 | Límite | — | Peso 0 kg; resto de datos válidos | Registro rechazado; el peso debe ser mayor que cero | Aprobado (CosechaServiceTest) |
| CP-COS-07 | RN-13 | Negativo | — | Peso -3 kg; resto de datos válidos | Registro rechazado; el peso debe ser mayor que cero | Aprobado (CosechaServiceTest) |
| CP-COS-08 | RN-14, RN-18 | Límite | SI-001 sembrada el día D | Fecha de cosecha D + 1 | Cosecha guardada (primer día válido) | Aprobado (CosechaServiceTest) |
| CP-COS-09 | RN-14 | Límite | SI-001 sembrada el día D | Fecha de cosecha D (misma fecha de siembra) | Registro rechazado; la fecha debe ser posterior a la siembra | Aprobado (CosechaServiceTest) |
| CP-COS-10 | RN-14 | Negativo | SI-001 sembrada el día D | Fecha de cosecha D - 1 | Registro rechazado; la fecha es anterior a la siembra | Aprobado (CosechaServiceTest) |
| CP-COS-11 | RN-18 | Negativo | No existe la siembra SI-999 | Siembra SI-999; resto de datos válidos | Registro rechazado; la siembra no existe | Aprobado (CosechaServiceTest) |
| CP-COS-12 | RF-13 | Negativo | — | Bandejas -2; resto de datos válidos | Registro rechazado; las bandejas deben ser mayores que cero | Aprobado (CosechaServiceTest) |
| CP-COS-13 | RF-13 | Límite | — | Bandejas 0; resto de datos válidos | Registro rechazado; las bandejas deben ser mayores que cero | Aprobado (CosechaServiceTest) |
| CP-COS-14 | RF-13, RN-16 | Negativo | No existe el trabajador 999999999 | Responsable 999999999; resto de datos válidos | Registro rechazado; el responsable no está registrado | Aprobado (CosechaServiceTest) |
| CP-COS-15 | RF-13, RN-17 | Negativo | Existe CO-001; SI-004 activa | Código CO-001; siembra SI-004; resto de datos válidos | Registro rechazado; el código ya existe | Aprobado (CosechaServiceTest) |
| CP-COS-16 | RN-15 | Negativo | SI-002 activa en S-02 Ocupada | Cosecha de SI-002 con peso 0 | Registro rechazado; S-02 sigue Ocupada | Aprobado (CosechaServiceTest) |
| CP-COS-17 | RN-15 | Límite | S-01 y S-02 Ocupadas; SI-001 está en S-01 | Cosechar SI-001 | Solo S-01 pasa a Disponible; S-02 sigue Ocupada | Aprobado (CosechaServiceTest) |

### 4.7 Reportes (18)

*Conjunto de datos D-01 (se carga antes de ejecutar estos casos): secciones S-01, S-02 y S-03; trabajadores Ana (101230456) y Luis (202340567). Cosechas: CO-001 (siembra SI-001, S-01, 200 sustratos, 2026-09-10, 40 kg, Ana); CO-002 (SI-002, S-01, 400 sustratos, 2026-09-25, 60 kg, Luis); CO-003 (SI-003, S-02, 100 sustratos, 2026-10-05, 30 kg, Ana). Aplicaciones: AP-001 (SI-001, PR-01, 2.5), AP-002 (SI-001, PR-02, 1) y AP-003 (SI-002, PR-01, 4). S-03 no tiene cosechas.*

| ID caso | Requerimientos | Tipo | Precondición | Datos de entrada | Resultado esperado | Resultado obtenido |
| --- | --- | --- | --- | --- | --- | --- |
| CP-REP-01 | RF-14 | Positivo | Datos D-01 | Período del 2026-09-01 al 2026-09-30 | Producción total: 100 kg | Aprobado (ReporteServiceTest) |
| CP-REP-02 | RF-14 | Límite | Datos D-01 | Período del 2026-09-10 al 2026-09-25 (fechas de cosecha en los extremos) | Producción total: 100 kg (se incluyen ambos extremos) | Aprobado (ReporteServiceTest) |
| CP-REP-03 | RF-14 | Límite | Datos D-01 | Período del 2026-09-11 al 2026-09-24 | Producción total: 0 kg; sin error | Aprobado (ReporteServiceTest) |
| CP-REP-04 | RF-14 | Negativo | Datos D-01 | Fecha de inicio 2026-09-30 y fecha de fin 2026-09-01 | Reporte rechazado; la fecha de inicio es posterior a la de fin | Aprobado (ReporteServiceTest) |
| CP-REP-05 | RF-15 | Positivo | Datos D-01 | Reporte de producción por sección | S-01: 100 kg; S-02: 30 kg | Aprobado (ReporteServiceTest) |
| CP-REP-06 | RF-15 | Límite | Datos D-01 | Producción de la sección S-03 (sin cosechas) | 0 kg; sin error | Aprobado (ReporteServiceTest) |
| CP-REP-07 | RF-15 | Negativo | Datos D-01 | Producción de la sección S-99 | Reporte rechazado; la sección no existe | Aprobado (ReporteServiceTest) |
| CP-REP-08 | RF-16 | Positivo | Datos D-01 | Reporte de producción por trabajador | Ana: 70 kg; Luis: 60 kg | Aprobado (ReporteServiceTest) |
| CP-REP-09 | RF-16 | Límite | Datos D-01 y un trabajador sin cosechas | Producción del trabajador sin cosechas | 0 kg; sin error | Aprobado (ReporteServiceTest) |
| CP-REP-10 | RF-16 | Negativo | Datos D-01 | Producción del trabajador 999999999 | Reporte rechazado; el trabajador no existe | Aprobado (ReporteServiceTest) |
| CP-REP-11 | RF-17 | Positivo | Datos D-01 | Historial completo de aplicaciones | 3 aplicaciones, cada una con fecha, siembra, producto, cantidad y trabajador | Aprobado (ReporteServiceTest) |
| CP-REP-12 | RF-17 | Límite | Datos D-01 | Historial de la siembra SI-002 | 1 aplicación (AP-003) | Aprobado (ReporteServiceTest) |
| CP-REP-13 | RF-17 | Límite | Datos D-01 | Historial de la siembra SI-003 (sin aplicaciones) | Lista vacía; sin error | Aprobado (ReporteServiceTest) |
| CP-REP-14 | RF-17 | Negativo | Datos D-01 | Historial de la siembra SI-999 | Reporte rechazado; la siembra no existe | Aprobado (ReporteServiceTest) |
| CP-REP-15 | RF-18 | Positivo | Datos D-01 | Rendimiento de SI-001, SI-002 y SI-003 | SI-001: 0.20; SI-002: 0.15; SI-003: 0.30 (kg por sustrato) | Aprobado (ReporteServiceTest) |
| CP-REP-16 | RF-18 | Límite | Siembra con 1 sustrato y cosecha de 0.5 kg | Rendimiento de esa siembra | 0.50 kg por sustrato | Aprobado (ReporteServiceTest) |
| CP-REP-17 | RF-18 | Negativo | Siembra SI-004 sin cosecha | Rendimiento de SI-004 | Mensaje de rendimiento no disponible por falta de cosecha | Aprobado (ReporteServiceTest) |
| CP-REP-18 | RF-18 | Negativo | Datos D-01 | Rendimiento de la siembra SI-999 | Reporte rechazado; la siembra no existe | Aprobado (ReporteServiceTest) |

### 4.8 No funcionales y restricciones (13)

*Los casos de RES-02 y RES-03 se verifican por inspección del repositorio y de las pruebas, no con datos de entrada.*

| ID caso | Requerimientos | Tipo | Precondición | Datos de entrada | Resultado esperado | Resultado obtenido |
| --- | --- | --- | --- | --- | --- | --- |
| CP-NFR-01 | RNF-01 | Positivo | Existe el trabajador 101230456 | Registrar una siembra y una aplicación con ese trabajador como responsable | Ambos módulos reconocen al trabajador registrado en Personal | Pendiente |
| CP-NFR-02 | RNF-01 | Positivo | Existe S-01 con la siembra SI-001 | Cambiar el nombre de S-01 y consultar SI-001 | La siembra muestra el nuevo nombre de la sección | Pendiente |
| CP-NFR-03 | RNF-02 | Positivo | — | Registrar una sección válida y consultarla | Los datos guardados coinciden exactamente con los ingresados | Pendiente |
| CP-NFR-04 | RNF-02 | Negativo | S-01 Disponible | Registrar una siembra con fecha futura en S-01 | Registro rechazado; no se crea la siembra y S-01 sigue Disponible | Pendiente |
| CP-NFR-05 | RNF-02 | Límite | — | Registrar una sección con el nombre formado solo por espacios | Registro rechazado; el nombre es obligatorio | Pendiente |
| CP-NFR-06 | RNF-03 | Positivo | Existe S-01 | Registrar otra sección con código S-01 | El mensaje indica el campo (código) y el motivo (ya existe), en español | Pendiente |
| CP-NFR-07 | RNF-03 | Negativo | Falla simulada de la base de datos | Cualquier operación de registro | Mensaje genérico para la persona usuaria, sin detalles técnicos ni trazas de error | Pendiente |
| CP-NFR-08 | RNF-04 | Positivo | Datos D-01 | Generar el reporte de producción por sección | El reporte se genera en 3 segundos o menos | Pendiente (verificación manual) |
| CP-NFR-09 | RNF-04 | Límite | 1000 cosechas registradas | Generar el reporte de producción total del año | El reporte se genera en 3 segundos o menos | Pendiente (verificación manual) |
| CP-NFR-10 | RES-02 | Positivo | Repositorio con historial | Revisar el historial de commits de una funcionalidad | Existen commits que evidencian Red, Green y Refactor | Pendiente (verificación manual) |
| CP-NFR-11 | RES-02 | Positivo | Pruebas unitarias escritas | Revisar la estructura de cada prueba | Cada prueba tiene las secciones Arrange, Act y Assert | Pendiente (verificación manual) |
| CP-NFR-12 | RES-02 | Negativo | Matriz de trazabilidad final | Buscar requerimientos sin prueba asociada | No se encuentra ningún requerimiento sin prueba | Pendiente (verificación manual) |
| CP-NFR-13 | RES-03 | Positivo | — | Abrir el repositorio del proyecto | El repositorio contiene el código, las pruebas y su historial de cambios | Pendiente (verificación manual) |

---

## 5. Matriz de trazabilidad inicial

Relaciona cada requerimiento, regla de negocio y restricción con los casos que lo validan y con su estado de cobertura. La columna P / N / L indica cuántos casos positivos, negativos y límite lo cubren. La columna *Probado (Fase 4)* dice **Sí** solo cuando todos los casos del elemento están aprobados.

### 5.1 Resumen de cobertura

| Categoría | Total | Con casos de prueba | Cobertura |
| --- | --- | --- | --- |
| Requerimientos funcionales | 18 | 18 | 100% |
| Requerimientos no funcionales | 4 | 4 | 100% |
| Reglas de negocio | 18 | 18 | 100% |
| Restricciones | 3 | 3 | 100% |

### 5.2 Matriz

| ID | Descripción | Casos de prueba asociados | P / N / L | Estado de cobertura | Probado (Fase 4) |
| --- | --- | --- | --- | --- | --- |
| RF-01 | Registrar secciones | CP-SEC-01, CP-SEC-02, CP-SEC-03, CP-SEC-04, CP-SEC-05, CP-SEC-06 | 1 / 3 / 2 | Cubierto | Sí |
| RF-02 | Modificar secciones | CP-SEC-12, CP-SEC-13, CP-SEC-14, CP-SEC-15 | 1 / 2 / 1 | Cubierto | Sí |
| RF-03 | Consultar secciones disponibles | CP-SEC-16, CP-SEC-17, CP-SEC-18 | 1 / 1 / 1 | Cubierto | Sí |
| RF-04 | Registrar trabajadores | CP-PER-01, CP-PER-02, CP-PER-03, CP-PER-04, CP-PER-05 | 1 / 3 / 1 | Cubierto | Sí |
| RF-05 | Consultar trabajadores | CP-PER-09, CP-PER-10, CP-PER-11 | 1 / 1 / 1 | Cubierto | Sí |
| RF-06 | Actualizar trabajadores | CP-PER-12, CP-PER-13, CP-PER-14, CP-PER-15 | 1 / 2 / 1 | Cubierto | Sí |
| RF-07 | Registrar siembras | CP-SIE-01, CP-SIE-02, CP-SIE-03, CP-SIE-04, CP-SIE-16, CP-SIE-18, CP-SIE-20, CP-SIE-21 | 2 / 5 / 1 | Cubierto | Sí |
| RF-08 | Registrar productos | CP-PRO-01, CP-PRO-02, CP-PRO-04, CP-PRO-05 | 1 / 2 / 1 | Cubierto | Sí |
| RF-09 | Modificar productos | CP-PRO-06, CP-PRO-07, CP-PRO-08 | 1 / 1 / 1 | Cubierto | Sí |
| RF-10 | Consultar productos | CP-PRO-09, CP-PRO-10, CP-PRO-11 | 1 / 1 / 1 | Cubierto | Sí |
| RF-11 | Eliminar productos nunca utilizados | CP-PRO-12, CP-PRO-15, CP-PRO-16 | 1 / 1 / 1 | Cubierto | Sí |
| RF-12 | Registrar aplicaciones | CP-APL-01, CP-APL-03, CP-APL-11, CP-APL-12, CP-APL-13 | 1 / 3 / 1 | Cubierto | Sí |
| RF-13 | Registrar cosechas | CP-COS-01, CP-COS-12, CP-COS-13, CP-COS-14, CP-COS-15 | 1 / 3 / 1 | Cubierto | Sí |
| RF-14 | Reporte de producción total por período | CP-REP-01, CP-REP-02, CP-REP-03, CP-REP-04 | 1 / 1 / 2 | Cubierto | Sí |
| RF-15 | Reporte de producción por sección | CP-REP-05, CP-REP-06, CP-REP-07 | 1 / 1 / 1 | Cubierto | Sí |
| RF-16 | Reporte de producción por trabajador | CP-REP-08, CP-REP-09, CP-REP-10 | 1 / 1 / 1 | Cubierto | Sí |
| RF-17 | Historial de aplicaciones | CP-REP-11, CP-REP-12, CP-REP-13, CP-REP-14 | 1 / 1 / 2 | Cubierto | Sí |
| RF-18 | Reporte de rendimiento de cultivos | CP-REP-15, CP-REP-16, CP-REP-17, CP-REP-18 | 1 / 2 / 1 | Cubierto | Sí |
| RNF-01 | Información centralizada | CP-NFR-01, CP-NFR-02 | 2 / 0 / 0 | Cubierto | No |
| RNF-02 | Integridad de datos | CP-SEC-06, CP-PER-02, CP-SIE-21, CP-PRO-04, CP-APL-13, CP-NFR-03, CP-NFR-04, CP-NFR-05 | 1 / 6 / 1 | Cubierto | No |
| RNF-03 | Mensajes de error claros | CP-NFR-06, CP-NFR-07 | 1 / 1 / 0 | Cubierto | No |
| RNF-04 | Reportes en 3 segundos o menos | CP-NFR-08, CP-NFR-09 | 1 / 0 / 1 | Cubierto | No |
| RN-01 | No pueden existir dos secciones con el mismo código. | CP-SEC-01, CP-SEC-07, CP-SEC-08, CP-SEC-09, CP-SEC-14 | 2 / 2 / 1 | Cubierto | Sí |
| RN-02 | No pueden existir dos trabajadores con la misma identificación. | CP-PER-01, CP-PER-06, CP-PER-07, CP-PER-08, CP-PER-14 | 2 / 2 / 1 | Cubierto | Sí |
| RN-03 | Solo pueden utilizarse secciones en estado Disponible. | CP-SIE-01, CP-SIE-03, CP-SIE-14 | 2 / 1 / 0 | Cubierto | Sí |
| RN-04 | No puede registrarse una siembra con fecha futura. | CP-SIE-01, CP-SIE-05, CP-SIE-06, CP-SIE-07, CP-SIE-08 | 2 / 1 / 2 | Cubierto | Sí |
| RN-05 | La cantidad de sustratos debe ser mayor que cero. | CP-SIE-01, CP-SIE-09, CP-SIE-10, CP-SIE-11, CP-SIE-12 | 1 / 2 / 2 | Cubierto | No |
| RN-06 | Una sección ocupada no puede recibir otra siembra simultánea. | CP-SIE-02, CP-SIE-13, CP-SIE-14, CP-SIE-15 | 2 / 1 / 1 | Cubierto | Sí |
| RN-07 | Solo pueden eliminarse productos que nunca hayan sido utilizados. | CP-PRO-12, CP-PRO-13, CP-PRO-14 | 1 / 1 / 1 | Cubierto | Sí |
| RN-08 | La siembra asociada a la aplicación debe existir. | CP-APL-01, CP-APL-02, CP-APL-03 | 1 / 1 / 1 | Cubierto | Sí |
| RN-09 | El producto aplicado debe existir. | CP-APL-01, CP-APL-04, CP-APL-05 | 1 / 1 / 1 | Cubierto | Sí |
| RN-10 | La cantidad aplicada debe ser mayor a cero. | CP-APL-01, CP-APL-06, CP-APL-07, CP-APL-08 | 1 / 1 / 2 | Cubierto | Sí |
| RN-11 | No pueden registrarse aplicaciones para cultivos ya cosechados. | CP-APL-01, CP-APL-09, CP-APL-10 | 1 / 1 / 1 | Cubierto | Sí |
| RN-12 | Una siembra solo puede cosecharse una vez. | CP-COS-01, CP-COS-03, CP-COS-04 | 1 / 1 / 1 | Cubierto | Sí |
| RN-13 | El peso cosechado debe ser mayor a cero. | CP-COS-01, CP-COS-05, CP-COS-06, CP-COS-07 | 1 / 1 / 2 | Cubierto | Sí |
| RN-14 | La fecha de cosecha debe ser posterior a la fecha de siembra (ver S-10). | CP-COS-01, CP-COS-08, CP-COS-09, CP-COS-10 | 1 / 1 / 2 | Cubierto | Sí |
| RN-15 | Al registrar una cosecha, la sección vuelve al estado Disponible. | CP-SIE-14, CP-COS-02, CP-COS-16, CP-COS-17 | 2 / 1 / 1 | Cubierto | Sí |
| RN-16 | El responsable de una siembra, aplicación o cosecha debe ser un trabajador registrado. | CP-SIE-01, CP-SIE-16, CP-SIE-17, CP-APL-01, CP-APL-11, CP-COS-01, CP-COS-14 | 3 / 3 / 1 | Cubierto | Sí |
| RN-17 | Los códigos de siembra, producto, aplicación y cosecha no pueden repetirse dentro de su propio registro. | CP-SIE-18, CP-SIE-19, CP-PRO-01, CP-PRO-02, CP-PRO-03, CP-APL-12, CP-COS-15 | 2 / 4 / 1 | Cubierto | Sí |
| RN-18 | La siembra asociada a la cosecha debe existir. | CP-COS-01, CP-COS-08, CP-COS-11 | 1 / 1 / 1 | Cubierto | Sí |
| RES-01 | Estados válidos de sección | CP-SEC-01, CP-SEC-10, CP-SEC-11 | 2 / 1 / 0 | Cubierto | Sí |
| RES-02 | Pruebas con AAA y TDD | CP-NFR-10, CP-NFR-11, CP-NFR-12 | 2 / 1 / 0 | Cubierto | No |
| RES-03 | Repositorio Git | CP-NFR-13 | 1 / 0 / 0 | Cubierto | No |

---

## 6. Estado de avance (Fase 4)

Registro de lo construido. Un caso está **aprobado** cuando tiene prueba unitaria en verde; un elemento de la matriz está **probado** cuando todos sus casos están aprobados.

Última actualización: 2026-10-22.

### 6.1 Avance por módulo

| Módulo | Casos | Lógica | Pruebas | Clase de prueba | Rama |
| --- | --- | --- | --- | --- | --- |
| Secciones (SEC) | 18 | Hecha (`SeccionService`) | 18 de 18 aprobadas | `SeccionServiceTest` | feature/seccion |
| Personal (PER) | 15 | Hecha (`TrabajadorService`) | 15 de 15 aprobadas | `TrabajadorServiceTest` | feature/personal |
| Productos (PRO) | 16 | Hecha (`ProductoService`) | 16 de 16 aprobadas | `ProductoServiceTest` | feature/producto |
| Siembras (SIE) | 21 | Hecha (`SiembraService`) | 20 de 21 aprobadas (falta CP-SIE-12) | `SiembraServiceTest` | main |
| Aplicaciones (APL) | 13 | Hecha (`AplicacionService`) | 13 de 13 aprobadas | `AplicacionServiceTest` | main |
| Cosechas (COS) | 17 | Hecha (`CosechaService`) | 17 de 17 aprobadas | `CosechaServiceTest` | main |
| Reportes (REP) | 18 | Hecha (`ReporteService`) | 18 de 18 aprobadas | `ReporteServiceTest` | main |
| No funcionales (NFR-01 a 07) | 7 | Parcial | Pendiente | — | — |
| NFR-08 a 13 | 6 | No aplica (no son pruebas unitarias) | Verificación manual | — | — |

Casos con prueba aprobada: **117 de 131**. Los 14 restantes son CP-SIE-12, CP-NFR-01 a CP-NFR-07 y los 6 casos de verificación manual (CP-NFR-08 a CP-NFR-13). `./mvnw test` ejecuta 118 pruebas, todas en verde; la que no es un caso del catálogo es la de arranque de Spring (`OstraVerdeQaApplicationTests`).

Elementos de la matriz probados: **36 de 43**. Faltan RNF-01 a RNF-04, RN-05 (por CP-SIE-12), RES-02 y RES-03.

### 6.2 Lógica de Siembras, Aplicaciones, Cosechas y Reportes

Repositorios en memoria (S-16): `SiembraRepositoryImpl`, `AplicacionRepositoryImpl` y `CosechaRepositoryImpl`. Los servicios comparten las validaciones de la clase `Validaciones` (interna del paquete `Service`).

| Servicio | Reglas implementadas |
| --- | --- |
| `SiembraService` (usa `Clock`) | RF-07, RN-03, RN-04, RN-05, RN-06, RN-16, RN-17, S-01 (la sección pasa a Ocupada) |
| `AplicacionService` | RF-12, RN-08, RN-09, RN-10, RN-11, RN-16, RN-17 |
| `CosechaService` | RF-13, RN-12, RN-13, RN-14, RN-15, RN-16, RN-17, RN-18 (la sección vuelve a Disponible) |
| `ReporteService` | RF-14 a RF-18, S-11, S-12 |

### 6.3 Evidencia TDD de Siembras, Aplicaciones, Cosechas y Reportes

La lógica de estos cuatro módulos se escribió antes que sus pruebas (commit `6823d3c`). Para poder evidenciar el ciclo Red, Green y Refactor, se vació la lógica de los servicios y se repitió el ciclo completo:

| Fase | Commit | Qué contiene | Resultado de `./mvnw test` |
| --- | --- | --- | --- |
| Esqueletos | `b077a2c` | Servicios con las firmas y sin lógica | 50 pruebas en verde (aún sin pruebas nuevas) |
| Red | `47271b8` | 68 pruebas nuevas (AAA) y valores centinela en `ReporteService` | 118 pruebas, 68 fallos, 0 errores |
| Green | `e2675cb` | Lógica restaurada tal como estaba en `6823d3c` | 118 pruebas, 0 fallos |
| Refactor | `36bf77d` | Clase `Validaciones` y separación de `buscarSeccion` y `validarDisponible` | 118 pruebas, 0 fallos |

Los valores centinela de `ReporteService` (los métodos numéricos devuelven `-1` y `historialAplicacionesDeSiembra` no filtra) existen solo para que CP-REP-03, 06, 09 y 13, que esperan cero o lista vacía, no pasen sin lógica.

### 6.4 Pendiente

- **CP-NFR-01 a CP-NFR-07:** sin prueba todavía. Reutilizan los servicios ya probados.
- **CP-SIE-12 (sustratos 2.5):** `Siembra.cantidadSustratos` es `int`, por lo que no se puede probar a nivel de servicio. Falta decidir entre dejarlo sin prueba y documentarlo, o cambiar el campo a `double`.
- **CP-NFR-02:** `Siembra` solo guarda el código de la sección, no su nombre. Falta definir cómo se expone el nombre al consultar una siembra.
- **CP-NFR-07:** no hay lógica que simule una falla de base de datos, porque los datos viven en memoria (S-16).
- Volver a ejecutar las pruebas de eliminación de productos (RN-07) con la implementación real de `AplicacionRepository`, porque hoy usan un mock.
- Por S-08 todos los campos son obligatorios salvo observaciones y descripción, pero el catálogo solo tiene un caso de campo vacío en Personal (CP-PER-02) y otro en Productos (CP-PRO-04). Los demás campos vacíos no tienen caso de prueba.
- RES-02 y RES-03 se verifican al cierre del proyecto (historial de commits, estructura AAA y matriz final).

---

## 7. Decisiones de implementación

- Los datos se guardan en memoria (S-16). Cada repositorio tiene su implementación en memoria, `<Entidad>RepositoryImpl`, con un `Map`.
- Toda regla de negocio rechazada lanza `ReglaNegocioException` con un mensaje en español que indica el campo y el motivo. Cada prueba verifica una palabra clave del mensaje.
- Consultar un registro inexistente lanza `ReglaNegocioException` (mensaje de no encontrado), porque un método que devuelve un objeto no puede transportar un mensaje sin lanzar excepción.
- Un listado sin resultados devuelve una lista vacía, sin error (CP-SEC-17, CP-PER-11, CP-PRO-11).
- Modificar o actualizar identifica el registro por el código o la identificación recibidos. El documento no define si ese dato puede cambiarse a uno libre. Solo se cubre el rechazo por duplicado (CP-SEC-14, CP-PER-14).
- Al actualizar un trabajador no se revalida el formato del correo ni del teléfono, porque ningún caso lo exige.
- Se agregó `EstadoSeccion.desdeTexto(String)` a la base compartida, para validar el estado de una sección (CP-SEC-11, RES-01).
- `ProductoService` recibe un `ProductoRepository` y un `AplicacionRepository`. Las pruebas de eliminación (RN-07) usan un mock de `AplicacionRepository.existeConProducto`.
- Una operación rechazada no guarda nada ni cambia el estado de la sección (RNF-02): `SiembraService` y `CosechaService` validan todo antes de guardar y de cambiar el estado.
- `AplicacionService` no valida que la fecha no sea futura, porque el enunciado no lo pide para aplicaciones; `CosechaService` tampoco, solo exige que sea posterior a la siembra.
- Los reportes por sección y por trabajador devuelven un `Map` ordenado por código, e incluyen con 0 kg a las secciones y trabajadores sin cosechas (CP-REP-06, CP-REP-09).
- El rendimiento de una siembra sin cosecha lanza `ReglaNegocioException` con el mensaje de rendimiento no disponible (CP-REP-17).
- Las pruebas de Siembras, Aplicaciones, Cosechas y Reportes preparan sus precondiciones directamente con los repositorios, no con otros servicios, para que cada prueba falle solo por la regla que valida.
- `Validaciones` (interna del paquete `Service`) concentra la comprobación de texto vacío y la de responsable registrado (RN-16) que usan los servicios de Siembras, Aplicaciones y Cosechas. `SeccionService`, `TrabajadorService` y `ProductoService` conservan sus propias comprobaciones.

---

## 8. Servicios disponibles

| Servicio | Dependencias | Métodos |
| --- | --- | --- |
| `SeccionService` | `SeccionRepository` | `registrar(Seccion)`, `modificar(String, Seccion)`, `consultarDisponibles()` |
| `TrabajadorService` | `TrabajadorRepository` | `registrar(Trabajador)`, `consultarPorIdentificacion(String)`, `consultarTodos()`, `actualizar(String, Trabajador)` |
| `ProductoService` | `ProductoRepository`, `AplicacionRepository` | `registrar(Producto)`, `modificar(String, Producto)`, `consultarPorCodigo(String)`, `consultarTodos()`, `eliminar(String)` |
| `SiembraService` | `SiembraRepository`, `SeccionRepository`, `TrabajadorRepository`, `Clock` | `registrar(Siembra)`, `consultarPorCodigo(String)` |
| `AplicacionService` | `AplicacionRepository`, `SiembraRepository`, `ProductoRepository`, `TrabajadorRepository`, `CosechaRepository` | `registrar(Aplicacion)` |
| `CosechaService` | `CosechaRepository`, `SiembraRepository`, `SeccionRepository`, `TrabajadorRepository` | `registrar(Cosecha)` |
| `ReporteService` | `CosechaRepository`, `SiembraRepository`, `SeccionRepository`, `TrabajadorRepository`, `AplicacionRepository` | `produccionTotalPorPeriodo(LocalDate, LocalDate)`, `produccionPorSeccion()`, `produccionDeSeccion(String)`, `produccionPorTrabajador()`, `produccionDeTrabajador(String)`, `historialAplicaciones()`, `historialAplicacionesDeSiembra(String)`, `rendimiento(String)` |

---

## 9. Reglas de oro al ejecutar

- Cada caso necesita **datos de entrada**, **resultado esperado** y **resultado obtenido** (se llena al correr la prueba).
- Cuando una operación se rechaza, el estado previo no debe cambiar (RNF-02).
- Todo requerimiento debe tener al menos un caso positivo, uno negativo y, si aplica, uno límite.
- Los mensajes de error deben decir qué campo falló y por qué (RNF-03).
