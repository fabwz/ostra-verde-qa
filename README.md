# Ostra Verde QA

Suite de pruebas unitarias del **Sistema de Gestión para la Producción de Hongos Ostra** de la empresa ficticia Ostra Verde S.A. El proyecto aplica TDD (Red, Green, Refactor), el patrón AAA (Arrange, Act, Assert) y una matriz de trazabilidad entre requerimientos, casos de prueba y pruebas unitarias.

Proyecto del curso **ISW-622 Pruebas de Software**, Universidad Técnica Nacional, Sede Regional San Carlos.

## Contenido

- [Alcance](#alcance)
- [Stack](#stack)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Requisitos previos](#requisitos-previos)
- [Ejecución de las pruebas](#ejecución-de-las-pruebas)
- [Resultados](#resultados)
- [Metodología de pruebas](#metodología-de-pruebas)
- [Decisiones de diseño](#decisiones-de-diseño)
- [Alcance y limitaciones](#alcance-y-limitaciones)
- [Autores](#autores)

## Alcance

El sistema gestiona el ciclo productivo de hongos ostra: secciones de cultivo, personal, siembras, productos agrícolas, aplicaciones, cosechas y reportes gerenciales. La lógica de negocio está en una capa de servicios, sin interfaz gráfica ni API, porque el enunciado evalúa el proceso de pruebas y no una aplicación completa.

| Módulo | Servicio | Requerimientos | Casos de prueba |
| --- | --- | --- | --- |
| Secciones | `SeccionService` | RF-01 a RF-03, RN-01, RES-01 | CP-SEC-01 a 18 |
| Personal | `TrabajadorService` | RF-04 a RF-06, RN-02 | CP-PER-01 a 15 |
| Siembras | `SiembraService` | RF-07, RN-03 a RN-06 | CP-SIE-01 a 21 |
| Productos | `ProductoService` | RF-08 a RF-11, RN-07 | CP-PRO-01 a 16 |
| Aplicaciones | `AplicacionService` | RF-12, RN-08 a RN-11 | CP-APL-01 a 13 |
| Cosechas | `CosechaService` | RF-13, RN-12 a RN-18 | CP-COS-01 a 17 |
| Reportes | `ReporteService` | RF-14 a RF-18 | CP-REP-01 a 18 |
| No funcionales | varios | RNF-01 a RNF-04, RES-02, RES-03 | CP-NFR-01 a 13 |

Los 131 casos de prueba, los requerimientos y los supuestos están en [`docs/`](docs/).

## Stack

| Componente | Versión |
| --- | --- |
| JDK | 21 (Eclipse Adoptium) |
| Spring Boot | 4.1.1 (solo como base del proyecto Maven) |
| JUnit Jupiter | 6.0.3 |
| Mockito | 5.23.0 |
| AssertJ | 3.27.7 |
| Maven | Wrapper incluido (`mvnw`) |

Spring Boot se usa únicamente como esqueleto del proyecto. Los servicios y repositorios se instancian de forma manual, sin contenedor de inyección, lo que mantiene las pruebas rápidas y aisladas.

## Estructura del proyecto

```text
ostra-verde-qa/
├── docs/
│   ├── approach.md               Enunciado resumido, entregables y rúbrica
│   └── testing.md                Requerimientos, supuestos, 131 casos de prueba y matriz de trazabilidad
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── cr/ostraverde/    Clase de arranque de Spring Boot (OstraVerdeQaApplication)
│   │   │   ├── Model/            Entidades y EstadoSeccion
│   │   │   ├── Repository/       Interfaces y su implementación en memoria (<Entidad>RepositoryImpl)
│   │   │   ├── Service/          Un servicio por módulo y Validaciones (validaciones comunes)
│   │   │   └── Exception/        ReglaNegocioException
│   │   └── resources/            application.properties
│   └── test/java/
│       ├── cr/ostraverde/        Prueba de arranque del proyecto
│       └── Service/              Una clase de prueba por servicio y RequisitosNoFuncionalesTest
├── .mvn/                         Configuración del Maven Wrapper
├── CLAUDE.md                     Convenciones y reglas de trabajo del proyecto
├── HELP.md                       Referencias generadas por Spring Initializr
├── pom.xml
├── mvnw / mvnw.cmd
└── README.md
```

## Requisitos previos

- JDK 21 instalado y disponible en el `PATH` (`java -version`).
- Git.
- No hace falta instalar Maven: el wrapper lo descarga en la primera ejecución.

## Ejecución de las pruebas

Todas las pruebas:

```bash
./mvnw test
```

En Windows (PowerShell o CMD) también funciona `.\mvnw.cmd test`.

Una clase de prueba, o un solo método:

```bash
./mvnw -Dtest=SeccionServiceTest test
./mvnw -Dtest=SeccionServiceTest#rechazarCodigoDuplicado test
```

Los reportes de Surefire quedan en `target/surefire-reports/`. Desde Spring Tools (Eclipse) se ejecutan con clic derecho sobre la clase de prueba, **Run As, JUnit Test**.

## Resultados

Última ejecución completa: **126 pruebas, 0 fallos, 0 errores, 0 omitidas.** De ellas, 125 corresponden a casos del catálogo y 1 es la prueba de arranque del proyecto.

| Clase de prueba | Pruebas |
| --- | --- |
| `SeccionServiceTest` | 18 |
| `TrabajadorServiceTest` | 15 |
| `ProductoServiceTest` | 16 |
| `SiembraServiceTest` | 20 |
| `AplicacionServiceTest` | 13 |
| `CosechaServiceTest` | 17 |
| `ReporteServiceTest` | 18 |
| `RequisitosNoFuncionalesTest` | 8 |
| **Total de casos** | **125** |

## Metodología de pruebas

**TDD.** Cada funcionalidad se desarrolló en tres fases: Red (prueba que falla, con esqueletos vacíos para que falle por su `assert` y no por compilación), Green (código mínimo para que pase) y Refactor (mejora sin cambiar el comportamiento, cuando hay una mejora real).

**Patrón AAA.** Las 125 pruebas separan la preparación, la ejecución y la validación con los comentarios `// Arrange`, `// Act` y `// Assert`, y tienen al menos un `assert` real.

**Trazabilidad.** Cada prueba lleva en su `@DisplayName` el identificador del caso que implementa (`CP-XXX-##`), lo que permite relacionar requerimiento, caso y prueba.

Ejemplo (CP-SEC-07, código de sección duplicado):

```java
@Test
@DisplayName("CP-SEC-07 rechazar código duplicado")
void rechazarCodigoDuplicado() {
    // Arrange
    seccionService.registrar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
    Seccion duplicada = new Seccion("S-01", "Sala Este", 300, EstadoSeccion.DISPONIBLE);

    // Act
    ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
            () -> seccionService.registrar(duplicada));

    // Assert
    assertTrue(error.getMessage().contains("código"));
    Seccion original = seccionRepository.buscarPorCodigo("S-01").get();
    assertEquals("Sala Norte", original.getNombre());
    assertEquals(500, original.getCapacidadMaxima());
}
```

## Decisiones de diseño

- **Persistencia en memoria (S-16).** El enunciado no define el almacenamiento. Cada repositorio tiene una implementación en memoria basada en un `Map`, y las pruebas usan esas implementaciones reales.
- **Reglas de negocio como excepciones.** Toda regla rechazada lanza `ReglaNegocioException` con un mensaje en español que indica el campo y el motivo (RNF-03).
- **Fechas controladas.** Los servicios que validan fechas reciben un `Clock` por constructor. Las pruebas usan un reloj fijo para que los casos límite sean deterministas.
- **Inyección por constructor.** Cada servicio recibe sus repositorios de forma explícita.
- **Mockito donde hace falta.** `AplicacionRepository` se simula en las pruebas de eliminación de productos (RN-07), y un repositorio que falla se simula en CP-NFR-07.
- **Consultas sobre registros inexistentes.** Lanzan `ReglaNegocioException`; los listados sin resultados devuelven una lista vacía.

## Alcance y limitaciones

- No hay base de datos, API ni interfaz gráfica.
- **CP-SIE-12** (sustratos con valor decimal) no se prueba a nivel de servicio: la cantidad de sustratos es un entero (S-07) y Java no admite el valor 2.5.
- **CP-NFR-07** (falla de la base de datos) está verificado solo para el registro de secciones (`SeccionService.registrar`).
- **CP-NFR-08 y CP-NFR-10 a CP-NFR-13** no son pruebas unitarias y se verifican de forma manual; su evidencia está en la matriz de trazabilidad final.
- **CP-NFR-09** (rendimiento con 1000 cosechas) sí tiene una prueba automatizada en `RequisitosNoFuncionalesTest`.

## Autores

- Fabián Zamora
- Roney Ruiz

Facilitador: Víctor Iván Zúñiga Zúñiga. Universidad Técnica Nacional, Sede Regional San Carlos, 2026.
