**Universidad Técnica Nacional**

**Sede Regional San Carlos**

**ISW-622 Pruebas de Software**

**Proyecto de Pruebas de Software**

**Sistema de Gestión para la Producción de Hongos Ostra**

Fases 1 a 3: Requerimientos, Casos de Prueba y Matriz de Trazabilidad Inicial

**Facilitador:**

Víctor Iván Zúñiga Zúñiga

**Estudiantes:**

Roney Ruiz

Fabián Zamora

Octubre de 2026

# **1. Introducción**

Ostra Verde S.A. produce y vende hongos ostra, y hoy lleva sus registros en hojas de cálculo y formularios impresos. Por eso la gerencia quiere un sistema centralizado para la siembra, el mantenimiento y la cosecha.

Como equipo de QA, en este documento presentamos las tres primeras fases del proyecto: el análisis de requerimientos, el diseño de los casos de prueba y la primera versión de la matriz de trazabilidad. Las pruebas unitarias, las evidencias y la matriz final corresponden a las fases 4, 5 y 6.

# **2. Metodología**

Para desarrollar este documento se seguirá el orden de las fases que plantea el enunciado, de manera que cada fase sirva de base para la siguiente.

En la fase 1 se leerá el enunciado completo y se identificarán los requerimientos funcionales, los no funcionales, las reglas de negocio y las restricciones. A cada uno se le asignará un identificador único.

En la fase 2 se diseñarán, para cada requerimiento, casos de prueba positivos, negativos y límite. Cada caso incluirá sus datos de entrada, el resultado esperado y un espacio para el resultado obtenido durante la ejecución.

En la fase 3 se relacionará cada requerimiento con sus casos de prueba y se indicará su estado de cobertura.

Cuando el enunciado no defina un detalle, se registrará como supuesto para poder confirmarlo con el facilitador.

# **3. Fase 1: Análisis de Requerimientos**

## **3.1 Convención de identificadores**

|**Prefijo**|**Significado**|

| :- | :- |

|RF-##|Requerimiento funcional|

|RNF-##|Requerimiento no funcional|

|RN-##|Regla de negocio|

|RES-##|Restricción|

|S-##|Supuesto del equipo|

## **3.2 Requerimientos funcionales**

Los requerimientos funcionales se agruparon según los siete pasos del enunciado. Se identificaron 18.

|**ID**|**Módulo**|**Descripción**|

| :- | :- | :- |

|RF-01|Secciones|El sistema deberá permitir registrar una nueva sección de producción con código, nombre, capacidad máxima y estado (Disponible, Ocupada o En mantenimiento).|

|RF-02|Secciones|El sistema deberá permitir modificar la información de una sección existente.|

|RF-03|Secciones|El sistema deberá permitir consultar las secciones disponibles para nuevas siembras.|

|RF-04|Personal|El sistema deberá permitir registrar un trabajador con identificación, nombre completo, teléfono, correo electrónico y cargo.|

|RF-05|Personal|El sistema deberá permitir consultar los trabajadores registrados.|

|RF-06|Personal|El sistema deberá permitir actualizar la información de un trabajador existente.|

|RF-07|Siembras|El sistema deberá permitir registrar una siembra con código, fecha, sección utilizada, cantidad de sustratos, responsable principal y observaciones. Al registrarla, la sección pasa a estado Ocupada (ver S-01).|

|RF-08|Productos|El sistema deberá permitir registrar un producto agrícola con código, nombre, tipo, descripción y unidad de medida.|

|RF-09|Productos|El sistema deberá permitir modificar un producto agrícola existente.|

|RF-10|Productos|El sistema deberá permitir consultar los productos agrícolas registrados.|

|RF-11|Productos|El sistema deberá permitir eliminar un producto que nunca haya sido utilizado.|

|RF-12|Aplicaciones|El sistema deberá permitir registrar una aplicación con código, fecha, siembra asociada, producto aplicado, cantidad utilizada, trabajador responsable y observaciones.|

|RF-13|Cosechas|El sistema deberá permitir registrar una cosecha con código, fecha, siembra asociada, peso total (kg), cantidad de bandejas y responsable. Al registrarla, la sección de la siembra vuelve a estado Disponible.|

|RF-14|Reportes|El sistema deberá generar el reporte de producción total por período.|

|RF-15|Reportes|El sistema deberá generar el reporte de producción por sección.|

|RF-16|Reportes|El sistema deberá generar el reporte de producción por trabajador.|

|RF-17|Reportes|El sistema deberá generar el historial de aplicaciones realizadas.|

|RF-18|Reportes|El sistema deberá generar el reporte de rendimiento de cultivos (ver S-11).|

## **3.3 Requerimientos no funcionales**

El enunciado no los menciona de forma explícita, así que se derivaron del contexto del problema (registros manuales, necesidad de centralizar y de medir la productividad).

|**ID**|**Categoría**|**Descripción**|

| :- | :- | :- |

|RNF-01|Centralización|La información de siembra, mantenimiento y cosecha deberá almacenarse de forma centralizada, de modo que un dato registrado en un módulo sea visible en los demás.|

|RNF-02|Integridad de datos|El sistema no deberá guardar registros con campos obligatorios vacíos ni dejar datos parciales cuando una operación sea rechazada.|

|RNF-03|Usabilidad|Ante un dato inválido, el sistema deberá mostrar un mensaje claro que indique el campo y el motivo, sin exponer detalles técnicos.|

|RNF-04|Rendimiento|Los reportes gerenciales deberán generarse en un máximo de 3 segundos (valor propuesto, ver S-15).|

## **3.4 Reglas de negocio**

Se identificaron 18 reglas. Las primeras 15 aparecen en el enunciado y las últimas 3 se infirieron a partir de él.

|**ID**|**Módulo**|**Regla**|**Origen**|

| :- | :- | :- | :- |

|RN-01|Secciones|No pueden existir dos secciones con el mismo código.|Enunciado|

|RN-02|Personal|No pueden existir dos trabajadores con la misma identificación.|Enunciado|

|RN-03|Siembras|Solo pueden utilizarse secciones en estado Disponible.|Enunciado|

|RN-04|Siembras|No puede registrarse una siembra con fecha futura.|Enunciado|

|RN-05|Siembras|La cantidad de sustratos debe ser mayor que cero.|Enunciado|

|RN-06|Siembras|Una sección ocupada no puede recibir otra siembra simultánea.|Enunciado|

|RN-07|Productos|Solo pueden eliminarse productos que nunca hayan sido utilizados.|Enunciado|

|RN-08|Aplicaciones|La siembra asociada a la aplicación debe existir.|Enunciado|

|RN-09|Aplicaciones|El producto aplicado debe existir.|Enunciado|

|RN-10|Aplicaciones|La cantidad aplicada debe ser mayor a cero.|Enunciado|

|RN-11|Aplicaciones|No pueden registrarse aplicaciones para cultivos ya cosechados.|Enunciado|

|RN-12|Cosechas|Una siembra solo puede cosecharse una vez.|Enunciado|

|RN-13|Cosechas|El peso cosechado debe ser mayor a cero.|Enunciado|

|RN-14|Cosechas|La fecha de cosecha debe ser posterior a la fecha de siembra (ver S-10).|Enunciado|

|RN-15|Cosechas|Al registrar una cosecha, la sección vuelve al estado Disponible.|Enunciado|

|RN-16|General|El responsable de una siembra, aplicación o cosecha debe ser un trabajador registrado.|Inferida (S-06)|

|RN-17|General|Los códigos de siembra, producto, aplicación y cosecha no pueden repetirse dentro de su propio registro.|Inferida (S-05)|

|RN-18|Cosechas|La siembra asociada a la cosecha debe existir.|Inferida|

## **3.5 Restricciones**

|**ID**|**Restricción**|

| :- | :- |

|RES-01|El estado de una sección solo puede ser Disponible, Ocupada o En mantenimiento.|

|RES-02|Las pruebas unitarias deberán seguir el patrón AAA y desarrollarse con el enfoque TDD (Red, Green, Refactor).|

|RES-03|El código y las pruebas deberán mantenerse en un repositorio Git.|

## **3.6 Supuestos**

Estos puntos no están definidos en el enunciado. Se tomó la interpretación más razonable para poder diseñar las pruebas, y se detalla aquí para evitar ambigüedades.

|**ID**|**Supuesto**|

| :- | :- |

|S-01|Al registrar una siembra, la sección pasa a estado Ocupada. El enunciado solo indica que vuelve a Disponible al cosechar, por lo que se deduce el cambio inverso.|

|S-02|Una siembra con la fecha de hoy es válida, porque la fecha de hoy no es futura.|

|S-03|La capacidad máxima de una sección debe ser mayor que cero.|

|S-04|El teléfono tiene 8 dígitos numéricos y el correo debe tener formato válido (usuario, @ y dominio).|

|S-05|Los códigos de siembra, producto, aplicación y cosecha son únicos, igual que el de las secciones.|

|S-06|El responsable de una siembra, aplicación o cosecha debe estar registrado en el módulo de personal.|

|S-07|La cantidad de sustratos y la cantidad de bandejas son números enteros mayores que cero.|

|S-08|Las observaciones y la descripción del producto son opcionales; el resto de campos son obligatorios.|

|S-09|La cantidad aplicada de un producto admite decimales, porque depende de la unidad de medida.|

|S-10|"Posterior a la fecha de siembra" se interpreta como estrictamente posterior (la misma fecha no es válida).|

|S-11|Rendimiento de un cultivo = kilogramos cosechados / cantidad de sustratos sembrados.|

|S-12|Los reportes por período usan la fecha de cosecha, e incluyen las fechas de inicio y de fin.|

|S-13|Un producto "utilizado" es el que tiene al menos una aplicación registrada.|

|S-14|No se valida que la cantidad de sustratos sea menor o igual a la capacidad máxima de la sección, porque el enunciado no lo pide.|

|S-15|El tiempo máximo de 3 segundos para reportes es un valor propuesto por el equipo, ya que el enunciado no define uno.|

Los supuestos S-01, S-03, S-04, S-07, S-14 y S-15 son los que más conviene confirmar con el facilitador, porque cambian directamente el resultado esperado de algunos casos.

# **4. Fase 2: Diseño de Casos de Prueba**

## **4.1 Estrategia**

Cada requerimiento tiene como mínimo un caso positivo, uno negativo y, cuando aplica, uno límite. Un mismo caso puede cubrir varios requerimientos, y por eso la columna Requerimientos lista todos los que valida.

|**Tipo**|**Qué valida**|

| :- | :- |

|Positivo|Datos válidos que deben ser aceptados y producir el resultado esperado.|

|Negativo|Datos inválidos o situaciones prohibidas por una regla, que deben ser rechazados con un mensaje claro.|

|Límite|Valores justo en el borde de una regla (por ejemplo 0, 1, la fecha de hoy o la fecha de siembra).|

Los identificadores de los casos usan el formato CP-XXX-##, donde XXX es el módulo: SEC (secciones), PER (personal), SIE (siembras), PRO (productos), APL (aplicaciones), COS (cosechas), REP (reportes) y NFR (no funcionales y restricciones).

La columna Resultado obtenido queda como Pendiente, porque se llena durante la ejecución de las pruebas en las fases siguientes. Las fechas se expresan de forma relativa a la fecha de ejecución cuando la regla depende de "hoy".

En total se diseñaron 131 casos de prueba: 34 positivos, 54 negativos y 43 límite.

## **4.2 Catálogo de casos de prueba**

### **4.2.1 Gestión de secciones de producción**

|**ID caso**|**Requerimientos**|**Tipo**|**Precondición**|**Datos de entrada**|**Resultado esperado**|**Resultado obtenido**|

| :- | :- | :- | :- | :- | :- | :- |

|CP-SEC-01|RF-01, RN-01, RES-01|Positivo|No existe la sección S-01|Código S-01; nombre Sala Norte; capacidad 500; estado Disponible|Sección guardada y visible en la consulta de secciones|Pendiente|

|CP-SEC-02|RF-01|Negativo|—|Código vacío; nombre Sala Norte; capacidad 500; estado Disponible|Registro rechazado; el mensaje indica que el código es obligatorio|Pendiente|

|CP-SEC-03|RF-01|Límite|—|Código S-02; nombre Sala Sur; capacidad 1; estado Disponible|Sección guardada (capacidad mínima válida)|Pendiente|

|CP-SEC-04|RF-01|Límite|—|Código S-03; nombre Sala Oeste; capacidad 0; estado Disponible|Registro rechazado; la capacidad debe ser mayor que cero|Pendiente|

|CP-SEC-05|RF-01|Negativo|—|Código S-03; nombre Sala Oeste; capacidad -10; estado Disponible|Registro rechazado; la capacidad debe ser mayor que cero|Pendiente|

|CP-SEC-06|RF-01, RNF-02|Negativo|—|Código S-03; nombre vacío; capacidad 500; estado Disponible|Registro rechazado; el nombre es obligatorio|Pendiente|

|CP-SEC-07|RN-01|Negativo|Existe S-01|Código S-01; nombre Sala Este; capacidad 300; estado Disponible|Registro rechazado; el mensaje indica que el código ya existe; S-01 conserva sus datos|Pendiente|

|CP-SEC-08|RN-01|Positivo|Existe S-01 con nombre Sala Norte|Código S-04; nombre Sala Norte; capacidad 300; estado Disponible|Sección guardada (la unicidad aplica solo al código)|Pendiente|

|CP-SEC-09|RN-01|Límite|Existe S-01|Código S-011; nombre Sala Centro; capacidad 300; estado Disponible|Sección guardada (código distinto por un carácter)|Pendiente|

|CP-SEC-10|RES-01|Positivo|—|Código S-05; nombre Sala Techo; capacidad 200; estado En mantenimiento|Sección guardada con estado En mantenimiento|Pendiente|

|CP-SEC-11|RES-01|Negativo|—|Código S-06; nombre Sala Patio; capacidad 200; estado Cerrada|Registro rechazado; el estado no es uno de los tres permitidos|Pendiente|

|CP-SEC-12|RF-02|Positivo|Existe S-01|Código S-01; nuevo nombre Sala Norte 2; nueva capacidad 600|Cambios guardados; la consulta muestra los nuevos valores|Pendiente|

|CP-SEC-13|RF-02|Negativo|No existe S-99|Modificar la sección S-99|Operación rechazada; el mensaje indica que la sección no existe|Pendiente|

|CP-SEC-14|RF-02, RN-01|Negativo|Existen S-01 y S-02|Cambiar el código de S-02 a S-01|Operación rechazada; el código ya existe; S-02 conserva su código|Pendiente|

|CP-SEC-15|RF-02|Límite|Existe S-01 con capacidad 500|Nueva capacidad 0|Operación rechazada; S-01 conserva la capacidad 500|Pendiente|

|CP-SEC-16|RF-03|Positivo|S-01 Disponible; S-02 Ocupada; S-03 En mantenimiento|Consultar secciones disponibles|Devuelve únicamente S-01|Pendiente|

|CP-SEC-17|RF-03|Negativo|Todas las secciones están Ocupadas o En mantenimiento|Consultar secciones disponibles|Lista vacía con mensaje informativo; sin error|Pendiente|

|CP-SEC-18|RF-03|Límite|Solo S-01 está Disponible|Consultar secciones disponibles|Lista con exactamente un elemento (S-01)|Pendiente|

### **4.2.2 Registro de personal**

|**ID caso**|**Requerimientos**|**Tipo**|**Precondición**|**Datos de entrada**|**Resultado esperado**|**Resultado obtenido**|

| :- | :- | :- | :- | :- | :- | :- |

|CP-PER-01|RF-04, RN-02|Positivo|No existe la identificación 101230456|Identificación 101230456; nombre Ana Mora Solís; teléfono 88881234; correo ana.mora@ostraverde.cr; cargo Operaria de cosecha|Trabajador guardado y visible en la consulta|Pendiente|

|CP-PER-02|RF-04, RNF-02|Negativo|—|Mismos datos del caso anterior, con el cargo vacío|Registro rechazado; el cargo es obligatorio|Pendiente|

|CP-PER-03|RF-04|Negativo|—|Correo ana.mora.ostraverde.cr (sin @)|Registro rechazado; el correo no tiene formato válido|Pendiente|

|CP-PER-04|RF-04|Límite|—|Teléfono 8888123 (7 dígitos)|Registro rechazado; el teléfono debe tener 8 dígitos|Pendiente|

|CP-PER-05|RF-04|Negativo|—|Teléfono 8888abcd|Registro rechazado; el teléfono debe ser numérico|Pendiente|

|CP-PER-06|RN-02|Negativo|Existe 101230456 (Ana Mora Solís)|Identificación 101230456; nombre Luis Vega Rojas; resto de datos válidos|Registro rechazado; la identificación ya está registrada; los datos de Ana no cambian|Pendiente|

|CP-PER-07|RN-02|Positivo|Existe 101230456 (Ana Mora Solís)|Identificación 404560789; nombre Ana Mora Solís; resto de datos válidos|Trabajador guardado (mismo nombre, identificación distinta)|Pendiente|

|CP-PER-08|RN-02|Límite|Existe 101230456|Identificación 101230457; nombre Luis Vega Rojas; resto de datos válidos|Trabajador guardado (identificación distinta por un dígito)|Pendiente|

|CP-PER-09|RF-05|Positivo|Existe 101230456|Consultar por identificación 101230456|Devuelve los cinco datos de Ana Mora Solís|Pendiente|

|CP-PER-10|RF-05|Negativo|No existe 999999999|Consultar por identificación 999999999|Mensaje de trabajador no encontrado|Pendiente|

|CP-PER-11|RF-05|Límite|No hay trabajadores registrados|Consultar el listado de trabajadores|Lista vacía; sin error|Pendiente|

|CP-PER-12|RF-06|Positivo|Existe 101230456|Nuevo teléfono 70001234; nuevo cargo Supervisora|Cambios guardados; la consulta muestra los nuevos valores|Pendiente|

|CP-PER-13|RF-06|Negativo|No existe 999999999|Actualizar el trabajador 999999999|Operación rechazada; el trabajador no existe|Pendiente|

|CP-PER-14|RF-06, RN-02|Negativo|Existen 101230456 y 202340567|Cambiar la identificación de 202340567 a 101230456|Operación rechazada; la identificación ya está registrada|Pendiente|

|CP-PER-15|RF-06|Límite|Existe 101230456|Actualizar sin modificar ningún dato|Operación aceptada; el registro queda sin cambios|Pendiente|

### **4.2.3 Registro de siembras**

*Precondición general: existen el trabajador 101230456 y la sección S-01 en estado Disponible, salvo que el caso indique otra cosa. "Hoy" es la fecha en que se ejecuta la prueba.*

|**ID caso**|**Requerimientos**|**Tipo**|**Precondición**|**Datos de entrada**|**Resultado esperado**|**Resultado obtenido**|

| :- | :- | :- | :- | :- | :- | :- |

|CP-SIE-01|RF-07, RN-03, RN-04, RN-05, RN-16|Positivo|S-01 Disponible; existe el trabajador 101230456|Código SI-001; fecha: hace 3 días; sección S-01; sustratos 200; responsable 101230456; observaciones Lote inicial|Siembra guardada|Pendiente|

|CP-SIE-02|RF-07, RN-06|Positivo|Siembra SI-001 recién registrada|Consultar el estado de S-01|S-01 queda en estado Ocupada|Pendiente|

|CP-SIE-03|RF-07, RN-03|Negativo|S-03 En mantenimiento|Código SI-002; sección S-03; resto de datos válidos|Registro rechazado; la sección no está disponible|Pendiente|

|CP-SIE-04|RF-07|Negativo|No existe la sección S-99|Código SI-002; sección S-99; resto de datos válidos|Registro rechazado; la sección no existe|Pendiente|

|CP-SIE-05|RN-04|Positivo|—|Fecha: hace 30 días; resto de datos válidos|Siembra guardada|Pendiente|

|CP-SIE-06|RN-04|Límite|—|Fecha: hoy; resto de datos válidos|Siembra guardada (hoy no es fecha futura)|Pendiente|

|CP-SIE-07|RN-04|Límite|—|Fecha: mañana; resto de datos válidos|Registro rechazado; la fecha es futura|Pendiente|

|CP-SIE-08|RN-04|Negativo|—|Fecha: dentro de 1 año; resto de datos válidos|Registro rechazado; la fecha es futura|Pendiente|

|CP-SIE-09|RN-05|Límite|—|Sustratos 1; resto de datos válidos|Siembra guardada (valor mínimo válido)|Pendiente|

|CP-SIE-10|RN-05|Límite|—|Sustratos 0; resto de datos válidos|Registro rechazado; los sustratos deben ser mayores que cero|Pendiente|

|CP-SIE-11|RN-05|Negativo|—|Sustratos -5; resto de datos válidos|Registro rechazado; los sustratos deben ser mayores que cero|Pendiente|

|CP-SIE-12|RN-05|Negativo|—|Sustratos 2.5; resto de datos válidos|Registro rechazado; los sustratos deben ser un número entero|Pendiente|

|CP-SIE-13|RN-06|Negativo|S-01 Ocupada con la siembra SI-001|Código SI-002; sección S-01; resto de datos válidos|Registro rechazado; la sección está ocupada; SI-001 no cambia|Pendiente|

|CP-SIE-14|RN-06, RN-03, RN-15|Positivo|SI-001 ya fue cosechada y S-01 volvió a Disponible|Código SI-003; sección S-01; resto de datos válidos|Siembra guardada; S-01 vuelve a quedar Ocupada|Pendiente|

|CP-SIE-15|RN-06|Límite|S-01 y S-02 Disponibles|Dos siembras el mismo día, una en S-01 y otra en S-02|Ambas guardadas; ninguna afecta a la otra|Pendiente|

|CP-SIE-16|RF-07, RN-16|Negativo|No existe el trabajador 999999999|Responsable 999999999; resto de datos válidos|Registro rechazado; el responsable no está registrado|Pendiente|

|CP-SIE-17|RN-16|Límite|Trabajador 303450678 recién registrado|Responsable 303450678; resto de datos válidos|Siembra guardada|Pendiente|

|CP-SIE-18|RF-07, RN-17|Negativo|Existe SI-001; S-02 Disponible|Código SI-001; sección S-02; resto de datos válidos|Registro rechazado; el código ya existe|Pendiente|

|CP-SIE-19|RN-17|Límite|Existe SI-001; S-02 Disponible|Código SI-0011; sección S-02; resto de datos válidos|Siembra guardada (código distinto por un carácter)|Pendiente|

|CP-SIE-20|RF-07|Límite|—|Observaciones vacías; resto de datos válidos|Siembra guardada (campo opcional)|Pendiente|

|CP-SIE-21|RF-07, RNF-02|Negativo|—|Código de siembra vacío; resto de datos válidos|Registro rechazado; el código es obligatorio|Pendiente|

### **4.2.4 Gestión de productos agrícolas**

|**ID caso**|**Requerimientos**|**Tipo**|**Precondición**|**Datos de entrada**|**Resultado esperado**|**Resultado obtenido**|

| :- | :- | :- | :- | :- | :- | :- |

|CP-PRO-01|RF-08, RN-17|Positivo|No existe PR-01|Código PR-01; nombre Cal agrícola; tipo Desinfectante; descripción Desinfección de sustrato; unidad kg|Producto guardado y visible en la consulta|Pendiente|

|CP-PRO-02|RF-08, RN-17|Negativo|Existe PR-01|Código PR-01; nombre Fungicida; tipo Fungicida; unidad litro|Registro rechazado; el código ya existe|Pendiente|

|CP-PRO-03|RN-17|Positivo|Existe la sección S-01|Producto con código S-01 (igual al de una sección)|Producto guardado (la unicidad aplica dentro de cada tipo de registro)|Pendiente|

|CP-PRO-04|RF-08, RNF-02|Negativo|—|Código PR-03; unidad de medida vacía; resto de datos válidos|Registro rechazado; la unidad es obligatoria|Pendiente|

|CP-PRO-05|RF-08|Límite|—|Código PR-02; descripción vacía; resto de datos válidos|Producto guardado (campo opcional)|Pendiente|

|CP-PRO-06|RF-09|Positivo|Existe PR-01|Nuevo nombre Cal hidratada; nueva descripción Desinfección de sustrato y paredes|Cambios guardados; la consulta muestra los nuevos valores|Pendiente|

|CP-PRO-07|RF-09|Negativo|No existe PR-99|Modificar el producto PR-99|Operación rechazada; el producto no existe|Pendiente|

|CP-PRO-08|RF-09|Límite|PR-01 con una aplicación registrada|Nueva descripción|Cambios guardados; la aplicación sigue asociada a PR-01|Pendiente|

|CP-PRO-09|RF-10|Positivo|Existe PR-01|Consultar PR-01|Devuelve los cinco datos del producto|Pendiente|

|CP-PRO-10|RF-10|Negativo|No existe PR-99|Consultar PR-99|Mensaje de producto no encontrado|Pendiente|

|CP-PRO-11|RF-10|Límite|No hay productos registrados|Consultar el listado de productos|Lista vacía; sin error|Pendiente|

|CP-PRO-12|RF-11, RN-07|Positivo|PR-02 sin aplicaciones|Eliminar PR-02|Producto eliminado; ya no aparece en las consultas|Pendiente|

|CP-PRO-13|RN-07|Negativo|PR-01 con 3 aplicaciones|Eliminar PR-01|Operación rechazada; el producto fue utilizado; PR-01 sigue existiendo|Pendiente|

|CP-PRO-14|RN-07|Límite|PR-01 con exactamente 1 aplicación|Eliminar PR-01|Operación rechazada (un solo uso es suficiente)|Pendiente|

|CP-PRO-15|RF-11|Negativo|No existe PR-99|Eliminar PR-99|Operación rechazada; el producto no existe|Pendiente|

|CP-PRO-16|RF-11|Límite|Solo existe PR-02, sin aplicaciones|Eliminar PR-02|Producto eliminado; el listado queda vacío|Pendiente|

### **4.2.5 Registro de aplicaciones**

*Precondición general: existen la siembra SI-001 sin cosecha, el producto PR-01 y el trabajador 101230456, salvo que el caso indique otra cosa.*

|**ID caso**|**Requerimientos**|**Tipo**|**Precondición**|**Datos de entrada**|**Resultado esperado**|**Resultado obtenido**|

| :- | :- | :- | :- | :- | :- | :- |

|CP-APL-01|RF-12, RN-08, RN-09, RN-10, RN-11, RN-16|Positivo|Datos generales|Código AP-001; fecha hoy; siembra SI-001; producto PR-01; cantidad 2.5; trabajador 101230456; observaciones Aplicación semanal|Aplicación guardada; aparece en el historial de aplicaciones|Pendiente|

|CP-APL-02|RN-08|Negativo|No existe la siembra SI-999|Siembra SI-999; resto de datos válidos|Registro rechazado; la siembra no existe|Pendiente|

|CP-APL-03|RF-12, RN-08|Límite|SI-001 registrada hoy|Aplicación sobre SI-001 con fecha de hoy|Aplicación guardada|Pendiente|

|CP-APL-04|RN-09|Negativo|No existe el producto PR-999|Producto PR-999; resto de datos válidos|Registro rechazado; el producto no existe|Pendiente|

|CP-APL-05|RN-09|Límite|Producto PR-03 recién registrado|Producto PR-03; resto de datos válidos|Aplicación guardada|Pendiente|

|CP-APL-06|RN-10|Límite|—|Cantidad 0.01; resto de datos válidos|Aplicación guardada (valor mínimo positivo)|Pendiente|

|CP-APL-07|RN-10|Límite|—|Cantidad 0; resto de datos válidos|Registro rechazado; la cantidad debe ser mayor que cero|Pendiente|

|CP-APL-08|RN-10|Negativo|—|Cantidad -1; resto de datos válidos|Registro rechazado; la cantidad debe ser mayor que cero|Pendiente|

|CP-APL-09|RN-11|Negativo|SI-001 con cosecha registrada|Aplicación sobre SI-001|Registro rechazado; el cultivo ya fue cosechado|Pendiente|

|CP-APL-10|RN-11|Límite|SI-001 cosechada hoy|Aplicación sobre SI-001 con fecha de hoy|Registro rechazado (la cosecha ya existe aunque sea el mismo día)|Pendiente|

|CP-APL-11|RF-12, RN-16|Negativo|No existe el trabajador 999999999|Trabajador 999999999; resto de datos válidos|Registro rechazado; el trabajador no está registrado|Pendiente|

|CP-APL-12|RF-12, RN-17|Negativo|Existe AP-001|Código AP-001; resto de datos válidos|Registro rechazado; el código ya existe|Pendiente|

|CP-APL-13|RF-12, RNF-02|Negativo|—|Fecha de aplicación vacía; resto de datos válidos|Registro rechazado; la fecha es obligatoria|Pendiente|

### **4.2.6 Registro de cosechas**

*Precondición general: existe la siembra SI-001 (200 sustratos) en la sección S-01 Ocupada, sembrada hace 30 días y sin cosecha, y existe el trabajador 101230456, salvo que el caso indique otra cosa.*

|**ID caso**|**Requerimientos**|**Tipo**|**Precondición**|**Datos de entrada**|**Resultado esperado**|**Resultado obtenido**|

| :- | :- | :- | :- | :- | :- | :- |

|CP-COS-01|RF-13, RN-12, RN-13, RN-14, RN-16, RN-18|Positivo|Datos generales|Código CO-001; fecha hoy; siembra SI-001; peso 45.5 kg; bandejas 30; responsable 101230456|Cosecha guardada|Pendiente|

|CP-COS-02|RN-15|Positivo|Cosecha CO-001 recién registrada|Consultar el estado de S-01 y las secciones disponibles|S-01 queda Disponible y aparece en la consulta de secciones disponibles|Pendiente|

|CP-COS-03|RN-12|Negativo|SI-001 ya cosechada|Código CO-002; siembra SI-001; resto de datos válidos|Registro rechazado; la siembra ya fue cosechada|Pendiente|

|CP-COS-04|RN-12|Límite|SI-001 cosechada hoy|Segunda cosecha de SI-001 con fecha de hoy|Registro rechazado (el límite es una cosecha por siembra)|Pendiente|

|CP-COS-05|RN-13|Límite|—|Peso 0.01 kg; resto de datos válidos|Cosecha guardada (valor mínimo positivo)|Pendiente|

|CP-COS-06|RN-13|Límite|—|Peso 0 kg; resto de datos válidos|Registro rechazado; el peso debe ser mayor que cero|Pendiente|

|CP-COS-07|RN-13|Negativo|—|Peso -3 kg; resto de datos válidos|Registro rechazado; el peso debe ser mayor que cero|Pendiente|

|CP-COS-08|RN-14, RN-18|Límite|SI-001 sembrada el día D|Fecha de cosecha D + 1|Cosecha guardada (primer día válido)|Pendiente|

|CP-COS-09|RN-14|Límite|SI-001 sembrada el día D|Fecha de cosecha D (misma fecha de siembra)|Registro rechazado; la fecha debe ser posterior a la siembra|Pendiente|

|CP-COS-10|RN-14|Negativo|SI-001 sembrada el día D|Fecha de cosecha D - 1|Registro rechazado; la fecha es anterior a la siembra|Pendiente|

|CP-COS-11|RN-18|Negativo|No existe la siembra SI-999|Siembra SI-999; resto de datos válidos|Registro rechazado; la siembra no existe|Pendiente|

|CP-COS-12|RF-13|Negativo|—|Bandejas -2; resto de datos válidos|Registro rechazado; las bandejas deben ser mayores que cero|Pendiente|

|CP-COS-13|RF-13|Límite|—|Bandejas 0; resto de datos válidos|Registro rechazado; las bandejas deben ser mayores que cero|Pendiente|

|CP-COS-14|RF-13, RN-16|Negativo|No existe el trabajador 999999999|Responsable 999999999; resto de datos válidos|Registro rechazado; el responsable no está registrado|Pendiente|

|CP-COS-15|RF-13, RN-17|Negativo|Existe CO-001; SI-004 activa|Código CO-001; siembra SI-004; resto de datos válidos|Registro rechazado; el código ya existe|Pendiente|

|CP-COS-16|RN-15|Negativo|SI-002 activa en S-02 Ocupada|Cosecha de SI-002 con peso 0|Registro rechazado; S-02 sigue Ocupada|Pendiente|

|CP-COS-17|RN-15|Límite|S-01 y S-02 Ocupadas; SI-001 está en S-01|Cosechar SI-001|Solo S-01 pasa a Disponible; S-02 sigue Ocupada|Pendiente|

### **4.2.7 Reportes gerenciales**

*Conjunto de datos D-01 (se carga antes de ejecutar estos casos): secciones S-01, S-02 y S-03; trabajadores Ana (101230456) y Luis (202340567). Cosechas: CO-001 (siembra SI-001, S-01, 200 sustratos, 2026-09-10, 40 kg, Ana); CO-002 (SI-002, S-01, 400 sustratos, 2026-09-25, 60 kg, Luis); CO-003 (SI-003, S-02, 100 sustratos, 2026-10-05, 30 kg, Ana). Aplicaciones: AP-001 (SI-001, PR-01, 2.5), AP-002 (SI-001, PR-02, 1) y AP-003 (SI-002, PR-01, 4). S-03 no tiene cosechas.*

|**ID caso**|**Requerimientos**|**Tipo**|**Precondición**|**Datos de entrada**|**Resultado esperado**|**Resultado obtenido**|

| :- | :- | :- | :- | :- | :- | :- |

|CP-REP-01|RF-14|Positivo|Datos D-01|Período del 2026-09-01 al 2026-09-30|Producción total: 100 kg|Pendiente|

|CP-REP-02|RF-14|Límite|Datos D-01|Período del 2026-09-10 al 2026-09-25 (fechas de cosecha en los extremos)|Producción total: 100 kg (se incluyen ambos extremos)|Pendiente|

|CP-REP-03|RF-14|Límite|Datos D-01|Período del 2026-09-11 al 2026-09-24|Producción total: 0 kg; sin error|Pendiente|

|CP-REP-04|RF-14|Negativo|Datos D-01|Fecha de inicio 2026-09-30 y fecha de fin 2026-09-01|Reporte rechazado; la fecha de inicio es posterior a la de fin|Pendiente|

|CP-REP-05|RF-15|Positivo|Datos D-01|Reporte de producción por sección|S-01: 100 kg; S-02: 30 kg|Pendiente|

|CP-REP-06|RF-15|Límite|Datos D-01|Producción de la sección S-03 (sin cosechas)|0 kg; sin error|Pendiente|

|CP-REP-07|RF-15|Negativo|Datos D-01|Producción de la sección S-99|Reporte rechazado; la sección no existe|Pendiente|

|CP-REP-08|RF-16|Positivo|Datos D-01|Reporte de producción por trabajador|Ana: 70 kg; Luis: 60 kg|Pendiente|

|CP-REP-09|RF-16|Límite|Datos D-01 y un trabajador sin cosechas|Producción del trabajador sin cosechas|0 kg; sin error|Pendiente|

|CP-REP-10|RF-16|Negativo|Datos D-01|Producción del trabajador 999999999|Reporte rechazado; el trabajador no existe|Pendiente|

|CP-REP-11|RF-17|Positivo|Datos D-01|Historial completo de aplicaciones|3 aplicaciones, cada una con fecha, siembra, producto, cantidad y trabajador|Pendiente|

|CP-REP-12|RF-17|Límite|Datos D-01|Historial de la siembra SI-002|1 aplicación (AP-003)|Pendiente|

|CP-REP-13|RF-17|Límite|Datos D-01|Historial de la siembra SI-003 (sin aplicaciones)|Lista vacía; sin error|Pendiente|

|CP-REP-14|RF-17|Negativo|Datos D-01|Historial de la siembra SI-999|Reporte rechazado; la siembra no existe|Pendiente|

|CP-REP-15|RF-18|Positivo|Datos D-01|Rendimiento de SI-001, SI-002 y SI-003|SI-001: 0.20; SI-002: 0.15; SI-003: 0.30 (kg por sustrato)|Pendiente|

|CP-REP-16|RF-18|Límite|Siembra con 1 sustrato y cosecha de 0.5 kg|Rendimiento de esa siembra|0\.50 kg por sustrato|Pendiente|

|CP-REP-17|RF-18|Negativo|Siembra SI-004 sin cosecha|Rendimiento de SI-004|Mensaje de rendimiento no disponible por falta de cosecha|Pendiente|

|CP-REP-18|RF-18|Negativo|Datos D-01|Rendimiento de la siembra SI-999|Reporte rechazado; la siembra no existe|Pendiente|

### **4.2.8 Requerimientos no funcionales y restricciones**

*Los casos de RES-02 y RES-03 se verifican por inspección del repositorio y de las pruebas, no con datos de entrada.*

|**ID caso**|**Requerimientos**|**Tipo**|**Precondición**|**Datos de entrada**|**Resultado esperado**|**Resultado obtenido**|

| :- | :- | :- | :- | :- | :- | :- |

|CP-NFR-01|RNF-01|Positivo|Existe el trabajador 101230456|Registrar una siembra y una aplicación con ese trabajador como responsable|Ambos módulos reconocen al trabajador registrado en Personal|Pendiente|

|CP-NFR-02|RNF-01|Positivo|Existe S-01 con la siembra SI-001|Cambiar el nombre de S-01 y consultar SI-001|La siembra muestra el nuevo nombre de la sección|Pendiente|

|CP-NFR-03|RNF-02|Positivo|—|Registrar una sección válida y consultarla|Los datos guardados coinciden exactamente con los ingresados|Pendiente|

|CP-NFR-04|RNF-02|Negativo|S-01 Disponible|Registrar una siembra con fecha futura en S-01|Registro rechazado; no se crea la siembra y S-01 sigue Disponible|Pendiente|

|CP-NFR-05|RNF-02|Límite|—|Registrar una sección con el nombre formado solo por espacios|Registro rechazado; el nombre es obligatorio|Pendiente|

|CP-NFR-06|RNF-03|Positivo|Existe S-01|Registrar otra sección con código S-01|El mensaje indica el campo (código) y el motivo (ya existe), en español|Pendiente|

|CP-NFR-07|RNF-03|Negativo|Falla simulada de la base de datos|Cualquier operación de registro|Mensaje genérico para la persona usuaria, sin detalles técnicos ni trazas de error|Pendiente|

|CP-NFR-08|RNF-04|Positivo|Datos D-01|Generar el reporte de producción por sección|El reporte se genera en 3 segundos o menos|Pendiente|

|CP-NFR-09|RNF-04|Límite|1000 cosechas registradas|Generar el reporte de producción total del año|El reporte se genera en 3 segundos o menos|Pendiente|

|CP-NFR-10|RES-02|Positivo|Repositorio con historial|Revisar el historial de commits de una funcionalidad|Existen commits que evidencian Red, Green y Refactor|Pendiente|

|CP-NFR-11|RES-02|Positivo|Pruebas unitarias escritas|Revisar la estructura de cada prueba|Cada prueba tiene las secciones Arrange, Act y Assert|Pendiente|

|CP-NFR-12|RES-02|Negativo|Matriz de trazabilidad final|Buscar requerimientos sin prueba asociada|No se encuentra ningún requerimiento sin prueba|Pendiente|

|CP-NFR-13|RES-03|Positivo|—|Abrir el repositorio del proyecto|El repositorio contiene el código, las pruebas y su historial de cambios|Pendiente|

# **5. Fase 3: Matriz de Trazabilidad Inicial**

Esta primera versión relaciona cada requerimiento, regla de negocio y restricción con los casos de prueba que lo validan y con su estado de cobertura. Todavía no incluye pruebas unitarias ni estado de ejecución, que se agregarán en la matriz final.

La columna P / N / L indica cuántos casos positivos, negativos y límite cubren el elemento.

## **5.1 Resumen de cobertura**

|**Categoría**|**Total**|**Con casos de prueba**|**Cobertura**|

| :- | :- | :- | :- |

|Requerimientos funcionales|18|18|100%|

|Requerimientos no funcionales|4|4|100%|

|Reglas de negocio|18|18|100%|

|Restricciones|3|3|100%|

## **5.2 Matriz**

|**ID**|**Descripción**|**Casos de prueba asociados**|**P / N / L**|**Estado de cobertura**|

| :- | :- | :- | :- | :- |

|RF-01|Registrar secciones|CP-SEC-01, CP-SEC-02, CP-SEC-03, CP-SEC-04, CP-SEC-05, CP-SEC-06|1 / 3 / 2|Cubierto|

|RF-02|Modificar secciones|CP-SEC-12, CP-SEC-13, CP-SEC-14, CP-SEC-15|1 / 2 / 1|Cubierto|

|RF-03|Consultar secciones disponibles|CP-SEC-16, CP-SEC-17, CP-SEC-18|1 / 1 / 1|Cubierto|

|RF-04|Registrar trabajadores|CP-PER-01, CP-PER-02, CP-PER-03, CP-PER-04, CP-PER-05|1 / 3 / 1|Cubierto|

|RF-05|Consultar trabajadores|CP-PER-09, CP-PER-10, CP-PER-11|1 / 1 / 1|Cubierto|

|RF-06|Actualizar trabajadores|CP-PER-12, CP-PER-13, CP-PER-14, CP-PER-15|1 / 2 / 1|Cubierto|

|RF-07|Registrar siembras|CP-SIE-01, CP-SIE-02, CP-SIE-03, CP-SIE-04, CP-SIE-16, CP-SIE-18, CP-SIE-20, CP-SIE-21|2 / 5 / 1|Cubierto|

|RF-08|Registrar productos|CP-PRO-01, CP-PRO-02, CP-PRO-04, CP-PRO-05|1 / 2 / 1|Cubierto|

|RF-09|Modificar productos|CP-PRO-06, CP-PRO-07, CP-PRO-08|1 / 1 / 1|Cubierto|

|RF-10|Consultar productos|CP-PRO-09, CP-PRO-10, CP-PRO-11|1 / 1 / 1|Cubierto|

|RF-11|Eliminar productos nunca utilizados|CP-PRO-12, CP-PRO-15, CP-PRO-16|1 / 1 / 1|Cubierto|

|RF-12|Registrar aplicaciones|CP-APL-01, CP-APL-03, CP-APL-11, CP-APL-12, CP-APL-13|1 / 3 / 1|Cubierto|

|RF-13|Registrar cosechas|CP-COS-01, CP-COS-12, CP-COS-13, CP-COS-14, CP-COS-15|1 / 3 / 1|Cubierto|

|RF-14|Reporte de producción total por período|CP-REP-01, CP-REP-02, CP-REP-03, CP-REP-04|1 / 1 / 2|Cubierto|

|RF-15|Reporte de producción por sección|CP-REP-05, CP-REP-06, CP-REP-07|1 / 1 / 1|Cubierto|

|RF-16|Reporte de producción por trabajador|CP-REP-08, CP-REP-09, CP-REP-10|1 / 1 / 1|Cubierto|

|RF-17|Historial de aplicaciones|CP-REP-11, CP-REP-12, CP-REP-13, CP-REP-14|1 / 1 / 2|Cubierto|

|RF-18|Reporte de rendimiento de cultivos|CP-REP-15, CP-REP-16, CP-REP-17, CP-REP-18|1 / 2 / 1|Cubierto|

|RNF-01|Información centralizada|CP-NFR-01, CP-NFR-02|2 / 0 / 0|Cubierto|

|RNF-02|Integridad de datos|CP-SEC-06, CP-PER-02, CP-SIE-21, CP-PRO-04, CP-APL-13, CP-NFR-03, CP-NFR-04, CP-NFR-05|1 / 6 / 1|Cubierto|

|RNF-03|Mensajes de error claros|CP-NFR-06, CP-NFR-07|1 / 1 / 0|Cubierto|

|RNF-04|Reportes en 3 segundos o menos|CP-NFR-08, CP-NFR-09|1 / 0 / 1|Cubierto|

|RN-01|No pueden existir dos secciones con el mismo código.|CP-SEC-01, CP-SEC-07, CP-SEC-08, CP-SEC-09, CP-SEC-14|2 / 2 / 1|Cubierto|

|RN-02|No pueden existir dos trabajadores con la misma identificación.|CP-PER-01, CP-PER-06, CP-PER-07, CP-PER-08, CP-PER-14|2 / 2 / 1|Cubierto|

|RN-03|Solo pueden utilizarse secciones en estado Disponible.|CP-SIE-01, CP-SIE-03, CP-SIE-14|2 / 1 / 0|Cubierto|

|RN-04|No puede registrarse una siembra con fecha futura.|CP-SIE-01, CP-SIE-05, CP-SIE-06, CP-SIE-07, CP-SIE-08|2 / 1 / 2|Cubierto|

|RN-05|La cantidad de sustratos debe ser mayor que cero.|CP-SIE-01, CP-SIE-09, CP-SIE-10, CP-SIE-11, CP-SIE-12|1 / 2 / 2|Cubierto|

|RN-06|Una sección ocupada no puede recibir otra siembra simultánea.|CP-SIE-02, CP-SIE-13, CP-SIE-14, CP-SIE-15|2 / 1 / 1|Cubierto|

|RN-07|Solo pueden eliminarse productos que nunca hayan sido utilizados.|CP-PRO-12, CP-PRO-13, CP-PRO-14|1 / 1 / 1|Cubierto|

|RN-08|La siembra asociada a la aplicación debe existir.|CP-APL-01, CP-APL-02, CP-APL-03|1 / 1 / 1|Cubierto|

|RN-09|El producto aplicado debe existir.|CP-APL-01, CP-APL-04, CP-APL-05|1 / 1 / 1|Cubierto|

|RN-10|La cantidad aplicada debe ser mayor a cero.|CP-APL-01, CP-APL-06, CP-APL-07, CP-APL-08|1 / 1 / 2|Cubierto|

|RN-11|No pueden registrarse aplicaciones para cultivos ya cosechados.|CP-APL-01, CP-APL-09, CP-APL-10|1 / 1 / 1|Cubierto|

|RN-12|Una siembra solo puede cosecharse una vez.|CP-COS-01, CP-COS-03, CP-COS-04|1 / 1 / 1|Cubierto|

|RN-13|El peso cosechado debe ser mayor a cero.|CP-COS-01, CP-COS-05, CP-COS-06, CP-COS-07|1 / 1 / 2|Cubierto|

|RN-14|La fecha de cosecha debe ser posterior a la fecha de siembra (ver S-10).|CP-COS-01, CP-COS-08, CP-COS-09, CP-COS-10|1 / 1 / 2|Cubierto|

|RN-15|Al registrar una cosecha, la sección vuelve al estado Disponible.|CP-SIE-14, CP-COS-02, CP-COS-16, CP-COS-17|2 / 1 / 1|Cubierto|

|RN-16|El responsable de una siembra, aplicación o cosecha debe ser un trabajador registrado.|CP-SIE-01, CP-SIE-16, CP-SIE-17, CP-APL-01, CP-APL-11, CP-COS-01, CP-COS-14|3 / 3 / 1|Cubierto|

|RN-17|Los códigos de siembra, producto, aplicación y cosecha no pueden repetirse dentro de su propio registro.|CP-SIE-18, CP-SIE-19, CP-PRO-01, CP-PRO-02, CP-PRO-03, CP-APL-12, CP-COS-15|2 / 4 / 1|Cubierto|

|RN-18|La siembra asociada a la cosecha debe existir.|CP-COS-01, CP-COS-08, CP-COS-11|1 / 1 / 1|Cubierto|

|RES-01|Estados válidos de sección|CP-SEC-01, CP-SEC-10, CP-SEC-11|2 / 1 / 0|Cubierto|

|RES-02|Pruebas con AAA y TDD|CP-NFR-10, CP-NFR-11, CP-NFR-12|2 / 1 / 0|Cubierto|

|RES-03|Repositorio Git|CP-NFR-13|1 / 0 / 0|Cubierto|
