package Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Exception.ReglaNegocioException;
import Model.EstadoSeccion;
import Model.Seccion;
import Model.Siembra;
import Model.Trabajador;
import Repository.SeccionRepository;
import Repository.SeccionRepositoryImpl;
import Repository.SiembraRepository;
import Repository.SiembraRepositoryImpl;
import Repository.TrabajadorRepository;
import Repository.TrabajadorRepositoryImpl;

class SiembraServiceTest {

	private static final LocalDate HOY = LocalDate.of(2026, 10, 22);

	private SiembraRepository siembraRepository;
	private SeccionRepository seccionRepository;
	private TrabajadorRepository trabajadorRepository;
	private SiembraService siembraService;

	@BeforeEach
	void setUp() {
		siembraRepository = new SiembraRepositoryImpl();
		seccionRepository = new SeccionRepositoryImpl();
		trabajadorRepository = new TrabajadorRepositoryImpl();
		Clock reloj = Clock.fixed(HOY.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
		siembraService = new SiembraService(siembraRepository, seccionRepository, trabajadorRepository, reloj);
		seccionRepository.guardar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
		trabajadorRepository.guardar(trabajador("101230456"));
	}

	@Test
	@DisplayName("CP-SIE-01 registrar siembra válida")
	void registrarSiembraValida() {
		// Arrange
		Siembra siembra = new Siembra("SI-001", HOY.minusDays(3), "S-01", 200, "101230456", "Lote inicial");

		// Act
		siembraService.registrar(siembra);

		// Assert
		Optional<Siembra> guardada = siembraRepository.buscarPorCodigo("SI-001");
		assertTrue(guardada.isPresent());
		assertEquals("S-01", guardada.get().getCodigoSeccion());
		assertEquals(200, guardada.get().getCantidadSustratos());
		assertEquals("101230456", guardada.get().getIdentificacionResponsable());
	}

	@Test
	@DisplayName("CP-SIE-02 la sección queda Ocupada tras registrar la siembra")
	void seccionQuedaOcupadaTrasRegistrar() {
		// Arrange
		Siembra siembra = siembraValida("SI-001", "S-01");

		// Act
		siembraService.registrar(siembra);

		// Assert
		assertEquals(EstadoSeccion.OCUPADA, estadoDe("S-01"));
	}

	@Test
	@DisplayName("CP-SIE-03 rechazar sección En mantenimiento")
	void rechazarSeccionEnMantenimiento() {
		// Arrange
		seccionRepository.guardar(new Seccion("S-03", "Sala Oeste", 300, EstadoSeccion.EN_MANTENIMIENTO));
		Siembra siembra = siembraValida("SI-002", "S-03");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> siembraService.registrar(siembra));

		// Assert
		assertTrue(error.getMessage().contains("no está disponible"));
		assertTrue(siembraRepository.buscarPorCodigo("SI-002").isEmpty());
	}

	@Test
	@DisplayName("CP-SIE-04 rechazar sección inexistente")
	void rechazarSeccionInexistente() {
		// Arrange
		Siembra siembra = siembraValida("SI-002", "S-99");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> siembraService.registrar(siembra));

		// Assert
		assertTrue(error.getMessage().contains("no existe"));
		assertTrue(siembraRepository.buscarPorCodigo("SI-002").isEmpty());
	}

	@Test
	@DisplayName("CP-SIE-05 registrar siembra con fecha de hace 30 días")
	void registrarSiembraConFechaPasada() {
		// Arrange
		Siembra siembra = new Siembra("SI-001", HOY.minusDays(30), "S-01", 200, "101230456", "Lote inicial");

		// Act
		siembraService.registrar(siembra);

		// Assert
		Optional<Siembra> guardada = siembraRepository.buscarPorCodigo("SI-001");
		assertTrue(guardada.isPresent());
		assertEquals(HOY.minusDays(30), guardada.get().getFecha());
	}

	@Test
	@DisplayName("CP-SIE-06 registrar siembra con fecha de hoy")
	void registrarSiembraConFechaDeHoy() {
		// Arrange
		Siembra siembra = new Siembra("SI-001", HOY, "S-01", 200, "101230456", "Lote inicial");

		// Act
		siembraService.registrar(siembra);

		// Assert
		Optional<Siembra> guardada = siembraRepository.buscarPorCodigo("SI-001");
		assertTrue(guardada.isPresent());
		assertEquals(HOY, guardada.get().getFecha());
	}

	@Test
	@DisplayName("CP-SIE-07 rechazar siembra con fecha de mañana")
	void rechazarSiembraConFechaDeManana() {
		// Arrange
		Siembra siembra = new Siembra("SI-001", HOY.plusDays(1), "S-01", 200, "101230456", "Lote inicial");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> siembraService.registrar(siembra));

		// Assert
		assertTrue(error.getMessage().contains("futura"));
		assertTrue(siembraRepository.buscarPorCodigo("SI-001").isEmpty());
	}

	@Test
	@DisplayName("CP-SIE-08 rechazar siembra con fecha dentro de un año")
	void rechazarSiembraConFechaDentroDeUnAnio() {
		// Arrange
		Siembra siembra = new Siembra("SI-001", HOY.plusYears(1), "S-01", 200, "101230456", "Lote inicial");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> siembraService.registrar(siembra));

		// Assert
		assertTrue(error.getMessage().contains("futura"));
		assertTrue(siembraRepository.buscarPorCodigo("SI-001").isEmpty());
	}

	@Test
	@DisplayName("CP-SIE-09 registrar siembra con un sustrato")
	void registrarSiembraConUnSustrato() {
		// Arrange
		Siembra siembra = new Siembra("SI-001", HOY.minusDays(3), "S-01", 1, "101230456", "Lote inicial");

		// Act
		siembraService.registrar(siembra);

		// Assert
		Optional<Siembra> guardada = siembraRepository.buscarPorCodigo("SI-001");
		assertTrue(guardada.isPresent());
		assertEquals(1, guardada.get().getCantidadSustratos());
	}

	@Test
	@DisplayName("CP-SIE-10 rechazar siembra con cero sustratos")
	void rechazarSiembraConCeroSustratos() {
		// Arrange
		Siembra siembra = new Siembra("SI-001", HOY.minusDays(3), "S-01", 0, "101230456", "Lote inicial");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> siembraService.registrar(siembra));

		// Assert
		assertTrue(error.getMessage().contains("sustratos"));
		assertTrue(siembraRepository.buscarPorCodigo("SI-001").isEmpty());
	}

	@Test
	@DisplayName("CP-SIE-11 rechazar siembra con sustratos negativos")
	void rechazarSiembraConSustratosNegativos() {
		// Arrange
		Siembra siembra = new Siembra("SI-001", HOY.minusDays(3), "S-01", -5, "101230456", "Lote inicial");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> siembraService.registrar(siembra));

		// Assert
		assertTrue(error.getMessage().contains("sustratos"));
		assertTrue(siembraRepository.buscarPorCodigo("SI-001").isEmpty());
	}

	@Test
	@DisplayName("CP-SIE-13 rechazar siembra en sección Ocupada")
	void rechazarSiembraEnSeccionOcupada() {
		// Arrange
		siembraRepository.guardar(siembraValida("SI-001", "S-01"));
		seccionRepository.guardar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.OCUPADA));
		Siembra segunda = siembraValida("SI-002", "S-01");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> siembraService.registrar(segunda));

		// Assert
		assertTrue(error.getMessage().contains("ocupada"));
		assertTrue(siembraRepository.buscarPorCodigo("SI-002").isEmpty());
		assertEquals(200, siembraRepository.buscarPorCodigo("SI-001").orElseThrow().getCantidadSustratos());
	}

	@Test
	@DisplayName("CP-SIE-14 registrar siembra en sección liberada tras una cosecha")
	void registrarSiembraEnSeccionLiberada() {
		// Arrange: SI-001 ya fue cosechada y S-01 volvió a Disponible
		siembraRepository.guardar(siembraValida("SI-001", "S-01"));
		Siembra nueva = siembraValida("SI-003", "S-01");

		// Act
		siembraService.registrar(nueva);

		// Assert
		assertTrue(siembraRepository.buscarPorCodigo("SI-003").isPresent());
		assertEquals(EstadoSeccion.OCUPADA, estadoDe("S-01"));
	}

	@Test
	@DisplayName("CP-SIE-15 registrar dos siembras el mismo día en secciones distintas")
	void registrarDosSiembrasEnSeccionesDistintas() {
		// Arrange
		seccionRepository.guardar(new Seccion("S-02", "Sala Sur", 400, EstadoSeccion.DISPONIBLE));
		Siembra primera = new Siembra("SI-001", HOY, "S-01", 200, "101230456", "Lote A");
		Siembra segunda = new Siembra("SI-002", HOY, "S-02", 150, "101230456", "Lote B");

		// Act
		siembraService.registrar(primera);
		siembraService.registrar(segunda);

		// Assert
		assertTrue(siembraRepository.buscarPorCodigo("SI-001").isPresent());
		assertTrue(siembraRepository.buscarPorCodigo("SI-002").isPresent());
		assertEquals(EstadoSeccion.OCUPADA, estadoDe("S-01"));
		assertEquals(EstadoSeccion.OCUPADA, estadoDe("S-02"));
	}

	@Test
	@DisplayName("CP-SIE-16 rechazar responsable no registrado")
	void rechazarResponsableNoRegistrado() {
		// Arrange
		Siembra siembra = new Siembra("SI-001", HOY.minusDays(3), "S-01", 200, "999999999", "Lote inicial");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> siembraService.registrar(siembra));

		// Assert
		assertTrue(error.getMessage().contains("responsable"));
		assertTrue(siembraRepository.buscarPorCodigo("SI-001").isEmpty());
		assertEquals(EstadoSeccion.DISPONIBLE, estadoDe("S-01"));
	}

	@Test
	@DisplayName("CP-SIE-17 registrar siembra con trabajador recién registrado")
	void registrarSiembraConTrabajadorRecienRegistrado() {
		// Arrange
		trabajadorRepository.guardar(trabajador("303450678"));
		Siembra siembra = new Siembra("SI-001", HOY.minusDays(3), "S-01", 200, "303450678", "Lote inicial");

		// Act
		siembraService.registrar(siembra);

		// Assert
		Optional<Siembra> guardada = siembraRepository.buscarPorCodigo("SI-001");
		assertTrue(guardada.isPresent());
		assertEquals("303450678", guardada.get().getIdentificacionResponsable());
	}

	@Test
	@DisplayName("CP-SIE-18 rechazar código de siembra duplicado")
	void rechazarCodigoDeSiembraDuplicado() {
		// Arrange
		seccionRepository.guardar(new Seccion("S-02", "Sala Sur", 400, EstadoSeccion.DISPONIBLE));
		siembraRepository.guardar(siembraValida("SI-001", "S-01"));
		Siembra duplicada = siembraValida("SI-001", "S-02");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> siembraService.registrar(duplicada));

		// Assert
		assertTrue(error.getMessage().contains("ya existe"));
		assertEquals("S-01", siembraRepository.buscarPorCodigo("SI-001").orElseThrow().getCodigoSeccion());
		assertEquals(EstadoSeccion.DISPONIBLE, estadoDe("S-02"));
	}

	@Test
	@DisplayName("CP-SIE-19 registrar siembra con código distinto por un carácter")
	void registrarSiembraConCodigoDistintoPorUnCaracter() {
		// Arrange
		seccionRepository.guardar(new Seccion("S-02", "Sala Sur", 400, EstadoSeccion.DISPONIBLE));
		siembraRepository.guardar(siembraValida("SI-001", "S-01"));
		Siembra siembra = siembraValida("SI-0011", "S-02");

		// Act
		siembraService.registrar(siembra);

		// Assert
		assertTrue(siembraRepository.buscarPorCodigo("SI-0011").isPresent());
		assertTrue(siembraRepository.buscarPorCodigo("SI-001").isPresent());
	}

	@Test
	@DisplayName("CP-SIE-20 registrar siembra con observaciones vacías")
	void registrarSiembraConObservacionesVacias() {
		// Arrange
		Siembra siembra = new Siembra("SI-001", HOY.minusDays(3), "S-01", 200, "101230456", "");

		// Act
		siembraService.registrar(siembra);

		// Assert
		Optional<Siembra> guardada = siembraRepository.buscarPorCodigo("SI-001");
		assertTrue(guardada.isPresent());
		assertEquals("", guardada.get().getObservaciones());
	}

	@Test
	@DisplayName("CP-SIE-21 rechazar código de siembra vacío")
	void rechazarCodigoDeSiembraVacio() {
		// Arrange
		Siembra siembra = new Siembra("", HOY.minusDays(3), "S-01", 200, "101230456", "Lote inicial");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> siembraService.registrar(siembra));

		// Assert
		assertTrue(error.getMessage().contains("código"));
		assertEquals(EstadoSeccion.DISPONIBLE, estadoDe("S-01"));
	}

	// Crea una siembra válida con los datos base del catálogo.
	private Siembra siembraValida(String codigo, String codigoSeccion) {
		return new Siembra(codigo, HOY.minusDays(3), codigoSeccion, 200, "101230456", "Lote inicial");
	}

	// Crea un trabajador con datos válidos y la identificación indicada.
	private Trabajador trabajador(String identificacion) {
		return new Trabajador(identificacion, "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha");
	}

	// Consulta el estado actual de una sección guardada.
	private EstadoSeccion estadoDe(String codigoSeccion) {
		return seccionRepository.buscarPorCodigo(codigoSeccion).orElseThrow().getEstado();
	}

}
