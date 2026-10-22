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

}
