package Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import Model.Aplicacion;
import Model.Cosecha;
import Model.EstadoSeccion;
import Model.Producto;
import Model.Seccion;
import Model.Siembra;
import Model.Trabajador;
import Repository.AplicacionRepository;
import Repository.AplicacionRepositoryImpl;
import Repository.CosechaRepository;
import Repository.CosechaRepositoryImpl;
import Repository.ProductoRepository;
import Repository.ProductoRepositoryImpl;
import Repository.SeccionRepository;
import Repository.SeccionRepositoryImpl;
import Repository.SiembraRepository;
import Repository.SiembraRepositoryImpl;
import Repository.TrabajadorRepository;
import Repository.TrabajadorRepositoryImpl;

/** Pruebas de los requerimientos no funcionales RNF-01 a RNF-04 (CP-NFR). */
class RequisitosNoFuncionalesTest {

	private static final LocalDate HOY = LocalDate.of(2026, 10, 22);
	private static final String ANA = "101230456";
	private static final long LIMITE_MS = 3000;

	private SeccionRepository seccionRepository;
	private SiembraRepository siembraRepository;
	private TrabajadorRepository trabajadorRepository;
	private AplicacionRepository aplicacionRepository;
	private CosechaRepository cosechaRepository;
	private ProductoRepository productoRepository;
	private SeccionService seccionService;
	private SiembraService siembraService;
	private AplicacionService aplicacionService;
	private ReporteService reporteService;

	@BeforeEach
	void setUp() {
		seccionRepository = new SeccionRepositoryImpl();
		siembraRepository = new SiembraRepositoryImpl();
		trabajadorRepository = new TrabajadorRepositoryImpl();
		aplicacionRepository = new AplicacionRepositoryImpl();
		cosechaRepository = new CosechaRepositoryImpl();
		productoRepository = new ProductoRepositoryImpl();
		Clock reloj = Clock.fixed(HOY.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
		seccionService = new SeccionService(seccionRepository);
		siembraService = new SiembraService(siembraRepository, seccionRepository, trabajadorRepository, reloj);
		aplicacionService = new AplicacionService(aplicacionRepository, siembraRepository,
				productoRepository, trabajadorRepository, cosechaRepository);
		reporteService = new ReporteService(cosechaRepository, siembraRepository, seccionRepository,
				trabajadorRepository, aplicacionRepository);
	}

	@Test
	@DisplayName("CP-NFR-01 siembra y aplicación reconocen al trabajador registrado en Personal")
	void siembraYAplicacionReconocenAlTrabajadorRegistrado() {
		// Arrange
		trabajadorRepository.guardar(trabajador(ANA));
		seccionRepository.guardar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
		productoRepository.guardar(new Producto("PR-01", "Cal agrícola", "Desinfectante",
				"Desinfección de sustrato", "kg"));
		Siembra siembra = new Siembra("SI-001", HOY.minusDays(3), "S-01", 200, ANA, "");
		Aplicacion aplicacion = new Aplicacion("AP-001", HOY, "SI-001", "PR-01", 2.5, ANA, "");

		// Act
		siembraService.registrar(siembra);
		aplicacionService.registrar(aplicacion);

		// Assert
		assertTrue(siembraRepository.buscarPorCodigo("SI-001").isPresent());
		assertTrue(aplicacionRepository.buscarPorCodigo("AP-001").isPresent());
	}

	@Test
	@DisplayName("CP-NFR-02 la siembra muestra el nuevo nombre de la sección")
	void siembraMuestraElNuevoNombreDeLaSeccion() {
		// Arrange
		trabajadorRepository.guardar(trabajador(ANA));
		seccionRepository.guardar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
		siembraService.registrar(new Siembra("SI-001", HOY.minusDays(3), "S-01", 200, ANA, ""));
		Seccion datosNuevos = new Seccion("S-01", "Sala Principal", 500, EstadoSeccion.OCUPADA);

		// Act
		seccionService.modificar("S-01", datosNuevos);

		// Assert
		Siembra siembra = siembraService.consultarPorCodigo("SI-001");
		Optional<Seccion> seccionDeLaSiembra = seccionRepository.buscarPorCodigo(siembra.getCodigoSeccion());
		assertTrue(seccionDeLaSiembra.isPresent());
		assertEquals("Sala Principal", seccionDeLaSiembra.get().getNombre());
	}

	@Test
	@DisplayName("CP-NFR-03 los datos guardados coinciden con los ingresados")
	void datosGuardadosCoincidenConLosIngresados() {
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
	@DisplayName("CP-NFR-04 siembra con fecha futura rechazada sin dejar datos parciales")
	void siembraConFechaFuturaNoDejaDatosParciales() {
		// Arrange
		trabajadorRepository.guardar(trabajador(ANA));
		seccionRepository.guardar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
		Siembra siembra = new Siembra("SI-001", HOY.plusDays(1), "S-01", 200, ANA, "");

		// Act
		assertThrows(ReglaNegocioException.class, () -> siembraService.registrar(siembra));

		// Assert
		assertFalse(siembraRepository.buscarPorCodigo("SI-001").isPresent());
		assertEquals(EstadoSeccion.DISPONIBLE, seccionRepository.buscarPorCodigo("S-01").get().getEstado());
	}

	@Test
	@DisplayName("CP-NFR-05 sección con nombre de solo espacios rechazada")
	void seccionConNombreDeSoloEspaciosRechazada() {
		// Arrange
		Seccion seccion = new Seccion("S-01", "   ", 500, EstadoSeccion.DISPONIBLE);

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> seccionService.registrar(seccion));

		// Assert
		assertTrue(error.getMessage().contains("nombre"));
		assertFalse(seccionRepository.buscarPorCodigo("S-01").isPresent());
	}

	@Test
	@DisplayName("CP-NFR-06 mensaje de código duplicado indica el campo y el motivo")
	void mensajeDeCodigoDuplicadoIndicaCampoYMotivo() {
		// Arrange
		seccionRepository.guardar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
		Seccion duplicada = new Seccion("S-01", "Sala Sur", 300, EstadoSeccion.DISPONIBLE);

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> seccionService.registrar(duplicada));

		// Assert
		assertTrue(error.getMessage().contains("código"));
		assertTrue(error.getMessage().contains("ya existe"));
	}

	@Test
	@DisplayName("CP-NFR-09 reporte de producción anual con 1000 cosechas en 3 segundos o menos")
	void reporteAnualConMilCosechasEnTresSegundosOMenos() {
		// Arrange
		for (int i = 1; i <= 1000; i++) {
			cosechaRepository.guardar(new Cosecha("CO-" + i, LocalDate.of(2026, 1, 1).plusDays(i % 365),
					"SI-001", 1.0, 1, ANA));
		}

		// Act
		long inicio = System.nanoTime();
		double total = reporteService.produccionTotalPorPeriodo(LocalDate.of(2026, 1, 1),
				LocalDate.of(2026, 12, 31));
		long duracionMs = (System.nanoTime() - inicio) / 1_000_000;

		// Assert
		assertTrue(duracionMs <= LIMITE_MS);
		assertEquals(1000.0, total, 0.0001);
	}

	// Crea un trabajador con datos válidos y la identificación indicada.
	private Trabajador trabajador(String identificacion) {
		return new Trabajador(identificacion, "Ana Mora Solís", "88881234", "ana.mora@ostraverde.cr",
				"Operaria de cosecha");
	}

}
