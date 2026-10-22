package Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Exception.ReglaNegocioException;
import Model.EstadoSeccion;
import Model.Seccion;
import Repository.SeccionRepository;
import Repository.SeccionRepositoryImpl;

class SeccionServiceTest {

	private SeccionRepository seccionRepository;
	private SeccionService seccionService;

	@BeforeEach
	void setUp() {
		seccionRepository = new SeccionRepositoryImpl();
		seccionService = new SeccionService(seccionRepository);
	}

	@Test
	@DisplayName("CP-SEC-01 registrar sección válida")
	void registrarSeccionValida() {
		// Arrange
		Seccion seccion = new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE);

		// Act
		seccionService.registrar(seccion);

		// Assert
		Optional<Seccion> guardada = seccionRepository.buscarPorCodigo("S-01");
		assertTrue(guardada.isPresent());
		assertEquals("S-01", guardada.get().getCodigo());
		assertEquals("Sala Norte", guardada.get().getNombre());
		assertEquals(500, guardada.get().getCapacidadMaxima());
		assertEquals(EstadoSeccion.DISPONIBLE, guardada.get().getEstado());
	}

	@Test
	@DisplayName("CP-SEC-03 registrar con capacidad mínima válida")
	void registrarSeccionConCapacidadMinima() {
		// Arrange
		Seccion seccion = new Seccion("S-02", "Sala Sur", 1, EstadoSeccion.DISPONIBLE);

		// Act
		seccionService.registrar(seccion);

		// Assert
		Optional<Seccion> guardada = seccionRepository.buscarPorCodigo("S-02");
		assertTrue(guardada.isPresent());
		assertEquals("S-02", guardada.get().getCodigo());
		assertEquals("Sala Sur", guardada.get().getNombre());
		assertEquals(1, guardada.get().getCapacidadMaxima());
		assertEquals(EstadoSeccion.DISPONIBLE, guardada.get().getEstado());
	}

	@Test
	@DisplayName("CP-SEC-10 registrar con estado En mantenimiento")
	void registrarSeccionEnMantenimiento() {
		// Arrange
		Seccion seccion = new Seccion("S-05", "Sala Techo", 200, EstadoSeccion.EN_MANTENIMIENTO);

		// Act
		seccionService.registrar(seccion);

		// Assert
		Optional<Seccion> guardada = seccionRepository.buscarPorCodigo("S-05");
		assertTrue(guardada.isPresent());
		assertEquals("S-05", guardada.get().getCodigo());
		assertEquals("Sala Techo", guardada.get().getNombre());
		assertEquals(200, guardada.get().getCapacidadMaxima());
		assertEquals(EstadoSeccion.EN_MANTENIMIENTO, guardada.get().getEstado());
	}

	@Test
	@DisplayName("CP-SEC-02 rechazar código vacío")
	void rechazarCodigoVacio() {
		// Arrange
		Seccion seccion = new Seccion("", "Sala Norte", 500, EstadoSeccion.DISPONIBLE);

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> seccionService.registrar(seccion));

		// Assert
		assertTrue(error.getMessage().contains("código"));
	}

	@Test
	@DisplayName("CP-SEC-04 rechazar capacidad cero")
	void rechazarCapacidadCero() {
		// Arrange
		Seccion seccion = new Seccion("S-03", "Sala Oeste", 0, EstadoSeccion.DISPONIBLE);

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> seccionService.registrar(seccion));

		// Assert
		assertTrue(error.getMessage().contains("capacidad"));
		assertTrue(seccionRepository.buscarPorCodigo("S-03").isEmpty());
	}

	@Test
	@DisplayName("CP-SEC-05 rechazar capacidad negativa")
	void rechazarCapacidadNegativa() {
		// Arrange
		Seccion seccion = new Seccion("S-03", "Sala Oeste", -10, EstadoSeccion.DISPONIBLE);

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> seccionService.registrar(seccion));

		// Assert
		assertTrue(error.getMessage().contains("capacidad"));
		assertTrue(seccionRepository.buscarPorCodigo("S-03").isEmpty());
	}

	@Test
	@DisplayName("CP-SEC-06 rechazar nombre vacío")
	void rechazarNombreVacio() {
		// Arrange
		Seccion seccion = new Seccion("S-03", "", 500, EstadoSeccion.DISPONIBLE);

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> seccionService.registrar(seccion));

		// Assert
		assertTrue(error.getMessage().contains("nombre"));
		assertTrue(seccionRepository.buscarPorCodigo("S-03").isEmpty());
	}

	@Test
	@DisplayName("CP-SEC-11 rechazar estado no permitido")
	void rechazarEstadoNoPermitido() {
		// Arrange
		String estadoTexto = "Cerrada";

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> seccionService.registrar(
						new Seccion("S-06", "Sala Patio", 200, EstadoSeccion.desdeTexto(estadoTexto))));

		// Assert
		assertTrue(error.getMessage().contains("estado"));
	}

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

	@Test
	@DisplayName("CP-SEC-08 registrar nombre repetido con código distinto")
	void registrarNombreRepetidoConCodigoDistinto() {
		// Arrange
		seccionService.registrar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
		Seccion seccion = new Seccion("S-04", "Sala Norte", 300, EstadoSeccion.DISPONIBLE);

		// Act
		seccionService.registrar(seccion);

		// Assert
		Optional<Seccion> guardada = seccionRepository.buscarPorCodigo("S-04");
		assertTrue(guardada.isPresent());
		assertEquals("Sala Norte", guardada.get().getNombre());
		assertEquals(300, guardada.get().getCapacidadMaxima());
		assertEquals(EstadoSeccion.DISPONIBLE, guardada.get().getEstado());
	}

	@Test
	@DisplayName("CP-SEC-09 registrar código distinto por un carácter")
	void registrarCodigoDistintoPorUnCaracter() {
		// Arrange
		seccionService.registrar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
		Seccion seccion = new Seccion("S-011", "Sala Centro", 300, EstadoSeccion.DISPONIBLE);

		// Act
		seccionService.registrar(seccion);

		// Assert
		Optional<Seccion> guardada = seccionRepository.buscarPorCodigo("S-011");
		assertTrue(guardada.isPresent());
		assertEquals("Sala Centro", guardada.get().getNombre());
		assertEquals(300, guardada.get().getCapacidadMaxima());
		assertEquals(EstadoSeccion.DISPONIBLE, guardada.get().getEstado());
	}

	@Test
	@DisplayName("CP-SEC-12 modificar nombre y capacidad de sección existente")
	void modificarSeccionExistente() {
		// Arrange
		seccionService.registrar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
		Seccion datosNuevos = new Seccion("S-01", "Sala Norte 2", 600, EstadoSeccion.DISPONIBLE);

		// Act
		seccionService.modificar("S-01", datosNuevos);

		// Assert
		Optional<Seccion> modificada = seccionRepository.buscarPorCodigo("S-01");
		assertTrue(modificada.isPresent());
		assertEquals("S-01", modificada.get().getCodigo());
		assertEquals("Sala Norte 2", modificada.get().getNombre());
		assertEquals(600, modificada.get().getCapacidadMaxima());
		assertEquals(EstadoSeccion.DISPONIBLE, modificada.get().getEstado());
	}

	@Test
	@DisplayName("CP-SEC-13 rechazar modificar sección inexistente")
	void rechazarModificarSeccionInexistente() {
		// Arrange
		Seccion datosNuevos = new Seccion("S-99", "Sala Fantasma", 100, EstadoSeccion.DISPONIBLE);

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> seccionService.modificar("S-99", datosNuevos));

		// Assert
		assertTrue(error.getMessage().contains("no existe"));
	}

	@Test
	@DisplayName("CP-SEC-14 rechazar cambio de código a uno existente")
	void rechazarCambioDeCodigoExistente() {
		// Arrange
		seccionService.registrar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
		seccionService.registrar(new Seccion("S-02", "Sala Sur", 300, EstadoSeccion.DISPONIBLE));
		Seccion datosNuevos = new Seccion("S-01", "Sala Sur", 300, EstadoSeccion.DISPONIBLE);

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> seccionService.modificar("S-02", datosNuevos));

		// Assert
		assertTrue(error.getMessage().contains("código"));
		Seccion segunda = seccionRepository.buscarPorCodigo("S-02").get();
		assertEquals("S-02", segunda.getCodigo());
		assertEquals("Sala Sur", segunda.getNombre());
		assertEquals(300, segunda.getCapacidadMaxima());
		Seccion primera = seccionRepository.buscarPorCodigo("S-01").get();
		assertEquals("Sala Norte", primera.getNombre());
		assertEquals(500, primera.getCapacidadMaxima());
	}

	@Test
	@DisplayName("CP-SEC-15 rechazar modificar con capacidad cero")
	void rechazarModificarConCapacidadCero() {
		// Arrange
		seccionService.registrar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
		Seccion datosNuevos = new Seccion("S-01", "Sala Norte", 0, EstadoSeccion.DISPONIBLE);

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> seccionService.modificar("S-01", datosNuevos));

		// Assert
		assertTrue(error.getMessage().contains("capacidad"));
		assertEquals(500, seccionRepository.buscarPorCodigo("S-01").get().getCapacidadMaxima());
	}

}
