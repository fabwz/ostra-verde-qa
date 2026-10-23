package Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Exception.ReglaNegocioException;
import Model.Aplicacion;
import Model.Cosecha;
import Model.EstadoSeccion;
import Model.Seccion;
import Model.Siembra;
import Model.Trabajador;
import Repository.AplicacionRepository;
import Repository.AplicacionRepositoryImpl;
import Repository.CosechaRepository;
import Repository.CosechaRepositoryImpl;
import Repository.SeccionRepository;
import Repository.SeccionRepositoryImpl;
import Repository.SiembraRepository;
import Repository.SiembraRepositoryImpl;
import Repository.TrabajadorRepository;
import Repository.TrabajadorRepositoryImpl;

class ReporteServiceTest {

	private static final double DELTA = 0.0001;
	private static final String ANA = "101230456";
	private static final String LUIS = "202340567";

	private CosechaRepository cosechaRepository;
	private SiembraRepository siembraRepository;
	private SeccionRepository seccionRepository;
	private TrabajadorRepository trabajadorRepository;
	private AplicacionRepository aplicacionRepository;
	private ReporteService reporteService;

	// Carga el conjunto de datos D-01 del catálogo.
	@BeforeEach
	void setUp() {
		cosechaRepository = new CosechaRepositoryImpl();
		siembraRepository = new SiembraRepositoryImpl();
		seccionRepository = new SeccionRepositoryImpl();
		trabajadorRepository = new TrabajadorRepositoryImpl();
		aplicacionRepository = new AplicacionRepositoryImpl();
		reporteService = new ReporteService(cosechaRepository, siembraRepository,
				seccionRepository, trabajadorRepository, aplicacionRepository);
		cargarSecciones();
		cargarTrabajadores();
		cargarSiembrasYCosechas();
		cargarAplicaciones();
	}

	@Test
	@DisplayName("CP-REP-01 producción total del período de septiembre")
	void produccionTotalDeSeptiembre() {
		// Arrange
		LocalDate inicio = LocalDate.of(2026, 9, 1);
		LocalDate fin = LocalDate.of(2026, 9, 30);

		// Act
		double total = reporteService.produccionTotalPorPeriodo(inicio, fin);

		// Assert
		assertEquals(100.0, total, DELTA);
	}

	@Test
	@DisplayName("CP-REP-02 producción total incluye las fechas de los extremos")
	void produccionTotalIncluyeExtremos() {
		// Arrange
		LocalDate inicio = LocalDate.of(2026, 9, 10);
		LocalDate fin = LocalDate.of(2026, 9, 25);

		// Act
		double total = reporteService.produccionTotalPorPeriodo(inicio, fin);

		// Assert
		assertEquals(100.0, total, DELTA);
	}

	@Test
	@DisplayName("CP-REP-03 producción total de un período sin cosechas")
	void produccionTotalSinCosechas() {
		// Arrange
		LocalDate inicio = LocalDate.of(2026, 9, 11);
		LocalDate fin = LocalDate.of(2026, 9, 24);

		// Act
		double total = reporteService.produccionTotalPorPeriodo(inicio, fin);

		// Assert
		assertEquals(0.0, total, DELTA);
	}

	@Test
	@DisplayName("CP-REP-04 rechazar período con inicio posterior al fin")
	void rechazarPeriodoInvertido() {
		// Arrange
		LocalDate inicio = LocalDate.of(2026, 9, 30);
		LocalDate fin = LocalDate.of(2026, 9, 1);

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> reporteService.produccionTotalPorPeriodo(inicio, fin));

		// Assert
		assertTrue(error.getMessage().contains("inicio"));
	}

	@Test
	@DisplayName("CP-REP-05 producción por sección")
	void produccionPorSeccion() {
		// Arrange: datos D-01 cargados en setUp

		// Act
		Map<String, Double> produccion = reporteService.produccionPorSeccion();

		// Assert
		assertEquals(100.0, produccion.getOrDefault("S-01", -1.0), DELTA);
		assertEquals(30.0, produccion.getOrDefault("S-02", -1.0), DELTA);
	}

	@Test
	@DisplayName("CP-REP-06 producción de una sección sin cosechas")
	void produccionDeSeccionSinCosechas() {
		// Arrange: S-03 no tiene cosechas en D-01

		// Act
		double produccion = reporteService.produccionDeSeccion("S-03");

		// Assert
		assertEquals(0.0, produccion, DELTA);
	}

	@Test
	@DisplayName("CP-REP-07 rechazar producción de una sección inexistente")
	void rechazarProduccionDeSeccionInexistente() {
		// Arrange: S-99 no existe

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> reporteService.produccionDeSeccion("S-99"));

		// Assert
		assertTrue(error.getMessage().contains("sección"));
	}

	@Test
	@DisplayName("CP-REP-08 producción por trabajador")
	void produccionPorTrabajador() {
		// Arrange: datos D-01 cargados en setUp

		// Act
		Map<String, Double> produccion = reporteService.produccionPorTrabajador();

		// Assert
		assertEquals(70.0, produccion.getOrDefault(ANA, -1.0), DELTA);
		assertEquals(60.0, produccion.getOrDefault(LUIS, -1.0), DELTA);
	}

	@Test
	@DisplayName("CP-REP-09 producción de un trabajador sin cosechas")
	void produccionDeTrabajadorSinCosechas() {
		// Arrange
		trabajadorRepository.guardar(trabajador("303450678", "Marta Rojas Vega"));

		// Act
		double produccion = reporteService.produccionDeTrabajador("303450678");

		// Assert
		assertEquals(0.0, produccion, DELTA);
	}

	@Test
	@DisplayName("CP-REP-10 rechazar producción de un trabajador inexistente")
	void rechazarProduccionDeTrabajadorInexistente() {
		// Arrange: 999999999 no está registrado

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> reporteService.produccionDeTrabajador("999999999"));

		// Assert
		assertTrue(error.getMessage().contains("trabajador"));
	}

	@Test
	@DisplayName("CP-REP-11 historial completo de aplicaciones")
	void historialCompletoDeAplicaciones() {
		// Arrange: datos D-01 cargados en setUp

		// Act
		List<Aplicacion> historial = reporteService.historialAplicaciones();

		// Assert
		assertEquals(3, historial.size());
		assertTrue(historial.stream().allMatch(this::tieneTodosLosDatos));
	}

	@Test
	@DisplayName("CP-REP-12 historial de aplicaciones de una siembra")
	void historialDeUnaSiembra() {
		// Arrange: SI-002 tiene una sola aplicación en D-01

		// Act
		List<Aplicacion> historial = reporteService.historialAplicacionesDeSiembra("SI-002");

		// Assert
		assertEquals(1, historial.size());
		assertEquals("AP-003", historial.get(0).getCodigo());
	}

	@Test
	@DisplayName("CP-REP-13 historial de una siembra sin aplicaciones")
	void historialDeSiembraSinAplicaciones() {
		// Arrange: SI-003 no tiene aplicaciones en D-01

		// Act
		List<Aplicacion> historial = reporteService.historialAplicacionesDeSiembra("SI-003");

		// Assert
		assertTrue(historial.isEmpty());
	}

	@Test
	@DisplayName("CP-REP-14 rechazar historial de una siembra inexistente")
	void rechazarHistorialDeSiembraInexistente() {
		// Arrange: SI-999 no existe

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> reporteService.historialAplicacionesDeSiembra("SI-999"));

		// Assert
		assertTrue(error.getMessage().contains("siembra"));
	}

	@Test
	@DisplayName("CP-REP-15 rendimiento de las siembras cosechadas")
	void rendimientoDeSiembrasCosechadas() {
		// Arrange: datos D-01 cargados en setUp

		// Act
		double rendimientoSi001 = reporteService.rendimiento("SI-001");
		double rendimientoSi002 = reporteService.rendimiento("SI-002");
		double rendimientoSi003 = reporteService.rendimiento("SI-003");

		// Assert
		assertEquals(0.20, rendimientoSi001, DELTA);
		assertEquals(0.15, rendimientoSi002, DELTA);
		assertEquals(0.30, rendimientoSi003, DELTA);
	}

	@Test
	@DisplayName("CP-REP-16 rendimiento de una siembra con un sustrato")
	void rendimientoConUnSustrato() {
		// Arrange
		siembraRepository.guardar(new Siembra("SI-005", LocalDate.of(2026, 10, 1), "S-03", 1, ANA, ""));
		cosechaRepository.guardar(new Cosecha("CO-005", LocalDate.of(2026, 10, 10), "SI-005", 0.5, 1, ANA));

		// Act
		double rendimiento = reporteService.rendimiento("SI-005");

		// Assert
		assertEquals(0.50, rendimiento, DELTA);
	}

	@Test
	@DisplayName("CP-REP-17 rendimiento no disponible para una siembra sin cosecha")
	void rendimientoNoDisponibleSinCosecha() {
		// Arrange
		siembraRepository.guardar(new Siembra("SI-004", LocalDate.of(2026, 10, 1), "S-03", 100, ANA, ""));

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> reporteService.rendimiento("SI-004"));

		// Assert
		assertTrue(error.getMessage().contains("no disponible"));
	}

	@Test
	@DisplayName("CP-REP-18 rechazar rendimiento de una siembra inexistente")
	void rechazarRendimientoDeSiembraInexistente() {
		// Arrange: SI-999 no existe

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> reporteService.rendimiento("SI-999"));

		// Assert
		assertTrue(error.getMessage().contains("siembra"));
	}

	// Verifica que la aplicación traiga fecha, siembra, producto, cantidad y trabajador.
	private boolean tieneTodosLosDatos(Aplicacion aplicacion) {
		return aplicacion.getFecha() != null && aplicacion.getCodigoSiembra() != null
				&& aplicacion.getCodigoProducto() != null && aplicacion.getCantidad() > 0
				&& aplicacion.getIdentificacionResponsable() != null;
	}

	// Registra las secciones S-01, S-02 y S-03 de D-01.
	private void cargarSecciones() {
		seccionRepository.guardar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
		seccionRepository.guardar(new Seccion("S-02", "Sala Sur", 400, EstadoSeccion.DISPONIBLE));
		seccionRepository.guardar(new Seccion("S-03", "Sala Oeste", 300, EstadoSeccion.DISPONIBLE));
	}

	// Registra a Ana y a Luis de D-01.
	private void cargarTrabajadores() {
		trabajadorRepository.guardar(trabajador(ANA, "Ana Mora Solís"));
		trabajadorRepository.guardar(trabajador(LUIS, "Luis Vega Rojas"));
	}

	// Registra las siembras SI-001 a SI-003 y sus cosechas CO-001 a CO-003 de D-01.
	private void cargarSiembrasYCosechas() {
		siembraRepository.guardar(new Siembra("SI-001", LocalDate.of(2026, 8, 1), "S-01", 200, ANA, ""));
		siembraRepository.guardar(new Siembra("SI-002", LocalDate.of(2026, 8, 15), "S-01", 400, LUIS, ""));
		siembraRepository.guardar(new Siembra("SI-003", LocalDate.of(2026, 9, 1), "S-02", 100, ANA, ""));
		cosechaRepository.guardar(new Cosecha("CO-001", LocalDate.of(2026, 9, 10), "SI-001", 40, 20, ANA));
		cosechaRepository.guardar(new Cosecha("CO-002", LocalDate.of(2026, 9, 25), "SI-002", 60, 30, LUIS));
		cosechaRepository.guardar(new Cosecha("CO-003", LocalDate.of(2026, 10, 5), "SI-003", 30, 15, ANA));
	}

	// Registra las aplicaciones AP-001 a AP-003 de D-01.
	private void cargarAplicaciones() {
		aplicacionRepository.guardar(new Aplicacion("AP-001", LocalDate.of(2026, 8, 10), "SI-001", "PR-01",
				2.5, ANA, ""));
		aplicacionRepository.guardar(new Aplicacion("AP-002", LocalDate.of(2026, 8, 20), "SI-001", "PR-02",
				1, ANA, ""));
		aplicacionRepository.guardar(new Aplicacion("AP-003", LocalDate.of(2026, 8, 25), "SI-002", "PR-01",
				4, LUIS, ""));
	}

	// Crea un trabajador con datos válidos.
	private Trabajador trabajador(String identificacion, String nombre) {
		return new Trabajador(identificacion, nombre, "88881234", "trabajador@ostraverde.cr", "Operario");
	}

}
