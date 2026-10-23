# Proyecto ISW-622: Sistema de Gestión para la Producción de Hongos Ostra

Proyecto universitario de Pruebas de Software (Universidad Técnica Nacional). Lo que se evalúa es el proceso de QA: pruebas unitarias con patrón AAA, TDD (Red-Green-Refactor) y trazabilidad entre requerimientos y pruebas. No se evalúa una aplicación completa.

## Permisos y restricciones (leer primero)

Por defecto, solo se permiten comandos de lectura. Cualquier otra acción requiere que la persona la pida de forma explícita en ese mensaje, y el permiso aplica solo a esa orden.

**Permitido sin pedir permiso:**

- Leer y listar archivos: `ls`, `cat`, `find`, `grep`, `head`, `tail`.
- Consultar Git sin modificarlo: `git status`, `git log`, `git diff`, `git show`.
- Compilar y ejecutar las pruebas para verificar su propio trabajo: `./mvnw test` y `./mvnw compile`. No modifican el repositorio.
- Crear y editar archivos dentro de `src/main/java` y `src/test/java`, siguiendo el flujo TDD.

Además de la verificación de Claude, la persona vuelve a ejecutar las pruebas en Spring Tools (Eclipse) para confirmar el resultado y tomar las capturas de evidencia.

**Prohibido salvo orden explícita:**

- `git add`, `git commit`, `git push`, `git pull`, `git merge`, `git rebase`, `git reset`, `git checkout`, `git switch`, `git branch`, `git stash`, `git tag` y cualquier otro comando que modifique Git o su historial.
- Borrar o mover archivos y carpetas.
- Modificar `pom.xml`, `.gitignore`, `CLAUDE.md` o los documentos de `docs/`.
- Lanzar la aplicación o ejecutar cualquier comando de Maven distinto de `test` y `compile`.
- Instalar dependencias o ejecutar comandos fuera de la carpeta del proyecto.

Si hace falta algo prohibido, indicarlo y esperar la decisión de la persona.

## Fuentes de verdad

El trabajo se basa estrictamente en estos documentos de `docs/`:

- `isw-622-enunciado.md`: el enunciado del proyecto (ITI-622).
- `isw-622-proyecto-fases1-3.md`: requerimientos (RF, RNF, RN, RES), supuestos (S-##) y los 131 casos de prueba (CP-XXX-##).

Reglas:

- Antes de cada funcionalidad, leer en el documento los requerimientos, reglas y casos de prueba que la cubren.
- No agregar reglas de negocio, campos, funcionalidades ni validaciones que no estén en esos documentos.
- Si algo no está definido o es ambiguo, preguntar antes de decidir. No inventar.
- Cada prueba corresponde a un caso del catálogo, con los datos de entrada y el resultado esperado que allí se indican.
- Los supuestos S-01 a S-15 forman parte de la especificación. Se agrega el supuesto S-16: los datos se guardan en memoria, sin base de datos.

## Stack y estructura

- Java con Maven, Spring Boot (solo como base del proyecto), JUnit 5 y Mockito. Usar la versión de Java definida en `pom.xml`.
- Sin base de datos, sin web, sin interfaz gráfica.
- Los paquetes van en la raíz de `src/main/java`, sin prefijo `cr.ostraverde`, siguiendo la convención del curso (nombres en PascalCase). Las pruebas en `src/test/java` usan los mismos nombres de paquete:
  - `Model/`: Seccion, Trabajador, Siembra, Producto, Aplicacion, Cosecha, EstadoSeccion.
  - `Repository/`: una interfaz por entidad y su implementación en memoria (un `Map`), por ejemplo `SeccionRepository` y `SeccionRepositoryImpl`.
  - `Service/`: un servicio por módulo (SeccionService, TrabajadorService, SiembraService, ProductoService, AplicacionService, CosechaService, ReporteService).
  - `Exception/`: ReglaNegocioException.
- La clase `OstraVerdeQaApplication` y su prueba de arranque se quedan en `cr.ostraverde`; no moverlas.
- Nomenclatura de clases, con el patrón `<Entidad><Capa>` usado en las actividades del curso:
  - Entidad en singular y sin sufijo: `Seccion`, `Trabajador`, `Siembra`, `Producto`, `Aplicacion`, `Cosecha`.
  - Repositorio: `SeccionRepository` (interfaz) y `SeccionRepositoryImpl` (implementación en memoria).
  - Servicio: `SeccionService`. Prueba: `SeccionServiceTest`.
  - Excepción única para todas las reglas de negocio: `ReglaNegocioException`.
  - Nombres de métodos y atributos en español y camelCase (`registrar`, `buscarPorCodigo`).
- La lógica de negocio va en `src/main/java`. Las pruebas, en `src/test/java`.
- Los servicios que validan fechas reciben un `Clock` por constructor. Nunca usar `LocalDate.now()` sin `Clock`.
- Los servicios reciben sus repositorios por constructor (inyección manual, sin `@Autowired` en campos).
- Los mensajes de error van en español y deben indicar el campo y el motivo (RNF-03), sin detalles técnicos.
- Una operación rechazada no debe dejar datos parciales (RNF-02).

## Reparto del trabajo

El trabajo se divide entre dos integrantes:

- **Mitad A:** Secciones, Personal y Productos.
- **Mitad B:** Siembras, Aplicaciones, Cosechas y Reportes.

Los modelos, `EstadoSeccion`, las interfaces de repositorio y `ReglaNegocioException` son la base compartida. No cambiar su firma sin avisar, porque la usan ambas mitades. Los reportes dependen de datos de siembras, aplicaciones y cosechas, por lo que se desarrollan al final.

## Pruebas y patrón AAA (estricto)

- Todas las pruebas siguen el patrón AAA, sin excepciones. Cada prueba lleva los tres marcadores literales `// Arrange`, `// Act` y `// Assert`, en ese orden.
- Cada prueba valida un resultado con al menos un `assert`. Una prueba sin assert no es válida.
- `@DisplayName("CP-XXX-## descripción corta")` en cada prueba, para la trazabilidad.
- Una prueba por caso. Mantener los nombres y datos del catálogo (S-01, 101230456, SI-001, etc.).
- Casos que no son pruebas unitarias: CP-NFR-08 y CP-NFR-09 (rendimiento) y CP-NFR-10 a CP-NFR-13 (revisión de Git, AAA y matriz). No generar pruebas para ellos.
- CP-SIE-12 (sustratos 2.5): si el campo es `int`, no se puede probar a nivel de servicio. Consultar antes de decidir.

## Flujo TDD (estricto)

Trabajar una funcionalidad (uno o pocos casos relacionados) a la vez, en tres fases separadas. En cada fase Claude escribe el código y ejecuta `./mvnw test` para verificarlo. Después de cada fase, detenerse, mostrar el resultado real y esperar a que la persona lo confirme en Spring Tools (clic derecho sobre la clase de prueba, Run As, JUnit Test) y le indique continuar.

1. **Red:** escribir solo la prueba, ejecutarla y mostrar que falla. Si no compila porque la clase o el método aún no existen, se permite crear únicamente los esqueletos vacíos (firmas sin lógica) para que la prueba corra y falle por su `assert`. No escribir lógica de producción.
2. **Green:** escribir el código mínimo para que la prueba pase. Ejecutar `./mvnw test` y mostrar el resultado.
3. **Refactor:** mejorar el código sin cambiar el comportamiento. Ejecutar `./mvnw test` y confirmar que todo sigue en verde.

Reglas:

- Informar solo resultados reales: no afirmar que una prueba pasa o falla sin haberla ejecutado. Si hay un error, mostrar el mensaje completo.
- Si el resultado de la persona en Spring Tools difiere del de Claude, prevalece el de la persona y se investiga la diferencia.
- No avanzar a la siguiente fase ni a la siguiente funcionalidad sin que la persona lo indique.
- No escribir código de producción antes de que exista su prueba.
- No adelantar funcionalidades que aún no tienen su prueba.
- Al terminar cada fase, sugerir el mensaje de commit correspondiente. No ejecutarlo.

## Comentarios y estilo de código

- Los comentarios deben ser profesionales y breves: una sola línea que explique qué hace el bloque que sigue. No comentar cada línea ni explicar lo que el código ya dice por sí mismo.
- En las pruebas, los marcadores `// Arrange`, `// Act` y `// Assert` abren cada bloque. Si hace falta aclarar algo, va en esa misma línea o en una línea corta debajo.
- En los métodos públicos de los servicios, un Javadoc de una línea que indique qué hacen y qué requerimiento o regla cubren, por ejemplo `/** Registra una sección nueva (RF-01, RN-01). */`.
- Los comentarios explicativos van en español. Los marcadores AAA se mantienen en inglés.
- Evitar líneas de código demasiado largas (máximo aproximado de 120 caracteres) y métodos extensos. Dividir en métodos privados con nombres claros.
- Nombres de clases, métodos y variables claros y consistentes con el vocabulario del enunciado.

## Convenciones de commits

Los commits los hace la persona, uno por fase de TDD. Claude solo sugiere el mensaje.

- Formato Conventional Commits, en inglés: `tipo(scope): description`.
- Tipos: `test` (Red), `feat` o `fix` (Green), `refactor` (Refactor), `chore`, `docs`.
- Scopes: `seccion`, `personal`, `siembra`, `producto`, `aplicacion`, `cosecha`, `reporte`, `model`, `build`, `repo`.
- Descripción en minúscula, en imperativo, sin punto final. Incluir el ID del caso cuando aplique.
- Ejemplo: `test(seccion): add failing test for valid section registration (CP-SEC-01)`.
- No reescribir el historial: los commits Red, Green y Refactor son la evidencia del proyecto.
