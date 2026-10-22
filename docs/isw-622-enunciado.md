**ITI-622 Pruebas de Software**  
Facilitador: Víctor Iván Zúñiga  
Universidad Técnica Nacional  
15%

# Proyecto Pruebas de Software

## Sistema de Gestión para la Producción de Hongos Ostra

### Contexto del Proyecto

La empresa **Ostra Verde S.A.** se dedica a la producción y comercialización de hongos ostra para el mercado local y nacional. Actualmente, gran parte de sus registros se realizan de forma manual mediante hojas de cálculo y formularios impresos, lo que dificulta el control de las actividades agrícolas, el seguimiento de aplicaciones de productos y la medición de la productividad de cada sección de cultivo.

La gerencia ha decidido contratar el desarrollo de un sistema informático que permita administrar de forma centralizada la información relacionada con la siembra, mantenimiento y cosecha de los hongos ostra.

Como equipo de aseguramiento de la calidad (QA), su responsabilidad será analizar los requerimientos del sistema, diseñar estrategias de prueba, crear casos de prueba, implementar pruebas automatizadas unitarias y documentar la trazabilidad completa entre requerimientos y pruebas ejecutadas.

Se requiere como desarrollador y encargado de las pruebas de software, aplicar las técnicas fundamentales de aseguramiento de calidad de software mediante la identificación de requerimientos, diseño de casos de prueba, implementación de pruebas unitarias utilizando el patrón AAA (Arrange, Act, Assert), aplicación del enfoque TDD (Test Driven Development) y construcción de una matriz de trazabilidad para garantizar la cobertura de las funcionalidades del sistema.

# Descripción del Sistema

La empresa requiere un sistema que permita gestionar las distintas etapas del proceso productivo de hongos ostra.

El sistema deberá contemplar los siguientes procesos:

## Paso 1. Gestión de Secciones de Producción

La finca se encuentra dividida en varias secciones de cultivo. Cada sección posee:

- Código único.
- Nombre de la sección.
- Capacidad máxima de producción.
- Estado:
  - Disponible.
  - Ocupada.
  - En mantenimiento.

El sistema deberá permitir:

- Registrar nuevas secciones.
- Modificar información existente.
- Consultar las secciones disponibles para nuevas siembras.
- Evitar registrar dos secciones con el mismo código.

## Paso 2. Registro de Personal

La empresa necesita registrar a las personas involucradas en el proceso productivo. De cada trabajador se almacenará:

- Identificación.
- Nombre completo.
- Teléfono.
- Correo electrónico.
- Cargo.

El sistema permitirá:

- Registrar trabajadores.
- Consultar trabajadores.
- Actualizar información.
- Impedir que existan dos colaboradores con la misma identificación.

## Paso 3. Registro de Siembras

Cada vez que se inicia un nuevo cultivo debe registrarse:

- Código de siembra.
- Fecha de siembra.
- Sección utilizada.
- Cantidad de sustratos sembrados.
- Responsable principal de la siembra.
- Observaciones.

Reglas del negocio:

- Solo pueden utilizarse secciones disponibles.
- No puede registrarse una siembra con fecha futura.
- La cantidad de sustratos debe ser mayor que cero.
- Una sección ocupada no podrá recibir otra siembra simultánea.

## Paso 4. Gestión de Productos Agrícolas

La empresa utiliza diversos productos para el mantenimiento y crecimiento del cultivo. De cada producto se registrará:

- Código.
- Nombre.
- Tipo de producto.
- Descripción.
- Unidad de medida.

El sistema permitirá:

- Registrar productos.
- Modificar productos.
- Consultar productos.
- Eliminar productos que nunca hayan sido utilizados.

## Paso 5. Registro de Aplicaciones

Durante el ciclo productivo se realizan aplicaciones de productos a los cultivos. Cada aplicación deberá almacenar:

- Código de aplicación.
- Fecha de aplicación.
- Siembra asociada.
- Producto aplicado.
- Cantidad utilizada.
- Trabajador responsable.
- Observaciones.

Reglas del negocio:

- La siembra debe existir.
- El producto debe existir.
- La cantidad aplicada debe ser mayor a cero.
- No pueden registrarse aplicaciones para cultivos ya cosechados.

## Paso 6. Registro de Cosechas

Cuando el cultivo alcanza su punto óptimo, se registra la cosecha. Datos requeridos:

- Código de cosecha.
- Fecha de cosecha.
- Siembra asociada.
- Peso total cosechado (kg).
- Cantidad de bandejas producidas.
- Responsable de la cosecha.

Reglas del negocio:

- Una siembra solo puede cosecharse una vez.
- El peso cosechado debe ser mayor a cero.
- La cosecha debe ocurrir después de la fecha de siembra.
- Al registrar una cosecha, la sección vuelve al estado disponible.

## Paso 7. Reportes Gerenciales

El sistema deberá generar:

- Producción total por período.
- Producción por sección.
- Producción por trabajador.
- Historial de aplicaciones realizadas.
- Rendimiento de cultivos.

# Actividades a Desarrollar

## Fase 1. Análisis de Requerimientos

Los estudiantes deberán:

- Leer el enunciado completo.
- Identificar:
  - Requerimientos funcionales.
  - Requerimientos no funcionales.
  - Reglas de negocio.
  - Restricciones.
- Documentar cada requerimiento utilizando un identificador único.

### Entregable

Sección de especificación de requerimientos.

## Fase 2. Diseño de Casos de Prueba

Para cada requerimiento identificado:

- Diseñar casos de prueba positivos.
- Diseñar casos de prueba negativos.
- Diseñar casos de prueba limitantes.
- Identificar:
  - Datos de entrada.
  - Resultado esperado.
  - Resultado obtenido (durante ejecución).
  - Demás elementos analizados en clase.

### Entregable

Catálogo de casos de prueba.

## Fase 3. Construcción de Matriz de Trazabilidad Inicial

Los estudiantes deberán relacionar:

- Requerimiento.
- Caso de prueba.
- Estado de cobertura.

### Entregable

Primera versión de matriz de trazabilidad.

## Fase 4. Desarrollo de Pruebas Unitarias

Implementar pruebas unitarias utilizando:

### Patrón AAA

Cada prueba deberá evidenciar:

#### Arrange

Preparación de los datos y configuración de la prueba.

#### Act

Ejecución de la funcionalidad.

#### Assert

Validación de resultados esperados.

### Aplicación de TDD

Los estudiantes deberán seguir las fases:

#### Red

Crear la prueba antes de implementar la funcionalidad.

#### Green

Implementar el código mínimo para que la prueba sea aprobada.

#### Refactor

Mejorar el código sin alterar el comportamiento validado.

### Entregable

Repositorio GIT con las pruebas desarrolladas.

## Fase 5. Evidencias del Proceso

Cada grupo deberá documentar:

- Requerimientos identificados.
- Casos de prueba diseñados.
- Evidencias de ejecución.
- Capturas de pantalla.
- Resultados obtenidos.
- Aplicación de AAA.
- Aplicación de TDD.

### Entregable

Bitácora del proyecto.

## Fase 6. Matriz de Trazabilidad Final

La matriz final deberá relacionar:

- ID del requerimiento.
- Descripción.
- Caso de prueba asociado.
- Prueba unitaria asociada.
- Estado de ejecución.
- Observaciones.

### Entregable

Matriz de trazabilidad final.

# Exposición Presencial

Cada equipo deberá realizar una presentación presencial de **15 a 20 minutos** donde explique:

- Comprensión del problema planteado.
- Requerimientos identificados.
- Estrategia de pruebas utilizada.
- Casos de prueba elaborados.
- Aplicación del patrón AAA.
- Aplicación de TDD.
- Evidencias de ejecución.
- Matriz de trazabilidad final.
- Lecciones aprendidas.
- Principales dificultades encontradas.

Todos los integrantes deberán participar activamente.

# Rúbrica de Evaluación

| **Criterio** | **Porcentaje** |
| --- | --- |
| Identificación correcta de requerimientos funcionales y no funcionales | 15% |
| Identificación de reglas de negocio y restricciones | 10% |
| Calidad y cobertura de casos de prueba | 15% |
| Construcción de matriz de trazabilidad inicial y final | 10% |
| Implementación de pruebas unitarias utilizando AAA | 10% |
| Aplicación correcta de TDD (Red-Green-Refactor) | 10% |
| Evidencias y documentación del proceso | 10% |
| Organización, calidad técnica y estructura de los entregables | 5% |
| Exposición presencial: dominio del tema | 5% |
| Exposición presencial: explicación técnica y defensa del proyecto | 5% |
| Exposición presencial: participación equilibrada del equipo | 5% |
| **Total** | **100%** |

# Criterios de Descuento de Puntos

### Requerimientos

- Requerimiento omitido: -1 punto por cada uno.
- Requerimiento ambiguo: -0,5 puntos.
- Reglas de negocio faltantes: -0,5 puntos cada una.

### Casos de Prueba

- Caso de prueba sin resultado esperado: -0,5 puntos.
- Cobertura incompleta: hasta -5 puntos.
- Falta de escenarios negativos: hasta -3 puntos.

### Pruebas Unitarias

- Ausencia del patrón AAA: -1 punto por prueba.
- Falta de evidencias de TDD: hasta -5 puntos.
- Pruebas que no validan resultados: -1 punto por prueba.

### Matriz de Trazabilidad

- Requerimientos sin asociar a pruebas: -1 punto por cada requerimiento.
- Casos sin evidencia de ejecución: -0,5 puntos por caso.

### Exposición

- Participación mínima de un integrante: hasta -2 puntos.
- Desconocimiento de la solución desarrollada: hasta -5 puntos.
- Incapacidad para justificar decisiones tomadas: hasta -3 puntos.
