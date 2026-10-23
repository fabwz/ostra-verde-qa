package Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Exception.ReglaNegocioException;
import Model.Aplicacion;
import Model.Cosecha;
import Model.Producto;
import Model.Siembra;
import Model.Trabajador;
import Repository.AplicacionRepository;
import Repository.AplicacionRepositoryImpl;
import Repository.CosechaRepository;
import Repository.CosechaRepositoryImpl;
import Repository.ProductoRepository;
import Repository.ProductoRepositoryImpl;
import Repository.SiembraRepository;
import Repository.SiembraRepositoryImpl;
import Repository.TrabajadorRepository;
import Repository.TrabajadorRepositoryImpl;

class AplicacionServiceTest {

	private static final LocalDate HOY = LocalDate.of(2026, 10, 22);

	private AplicacionRepository aplicacionRepository;
	private SiembraRepository siembraRepository;
	private ProductoRepository productoRepository;
	private TrabajadorRepository trabajadorRepository;
	private CosechaRepository cosechaRepository;
	private AplicacionService aplicacionService;

	@BeforeEach
	void setUp() {
		aplicacionRepository = new AplicacionRepositoryImpl();
		siembraRepository = new SiembraRepositoryImpl();
		productoRepository = new ProductoRepositoryImpl();
		trabajadorRepository = new TrabajadorRepositoryImpl();
		cosechaRepository = new CosechaRepositoryImpl();
		aplicacionService = new AplicacionService(aplicacionRepository, siembraRepository,
				productoRepository, trabajadorRepository, cosechaRepository);
		siembraRepository.guardar(new Siembra("SI-001", HOY.minusDays(10), "S-01", 200, "101230456", ""));
		productoRepository.guardar(producto("PR-01"));
		trabajadorRepository.guardar(new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha"));
	}

	@Test
	@DisplayName("CP-APL-01 registrar aplicación válida")
	void registrarAplicacionValida() {
		// Arrange
		Aplicacion aplicacion = new Aplicacion("AP-001", HOY, "SI-001", "PR-01", 2.5,
				"101230456", "Aplicación semanal");

		// Act
		aplicacionService.registrar(aplicacion);

		// Assert
		Optional<Aplicacion> guardada = aplicacionRepository.buscarPorCodigo("AP-001");
		assertTrue(guardada.isPresent());
		assertEquals("SI-001", guardada.get().getCodigoSiembra());
		assertEquals("PR-01", guardada.get().getCodigoProducto());
		assertEquals(2.5, guardada.get().getCantidad(), 0.0001);
		assertEquals(1, aplicacionRepository.listarTodas().size());
	}

	@Test
	@DisplayName("CP-APL-02 rechazar siembra inexistente")
	void rechazarSiembraInexistente() {
		// Arrange
		Aplicacion aplicacion = new Aplicacion("AP-001", HOY, "SI-999", "PR-01", 2.5, "101230456", "");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> aplicacionService.registrar(aplicacion));

		// Assert
		assertTrue(error.getMessage().contains("siembra"));
		assertTrue(aplicacionRepository.buscarPorCodigo("AP-001").isEmpty());
	}

	@Test
	@DisplayName("CP-APL-03 registrar aplicación sobre una siembra de hoy")
	void registrarAplicacionSobreSiembraDeHoy() {
		// Arrange
		siembraRepository.guardar(new Siembra("SI-001", HOY, "S-01", 200, "101230456", ""));
		Aplicacion aplicacion = aplicacionValida("AP-001");

		// Act
		aplicacionService.registrar(aplicacion);

		// Assert
		assertTrue(aplicacionRepository.buscarPorCodigo("AP-001").isPresent());
	}

	@Test
	@DisplayName("CP-APL-04 rechazar producto inexistente")
	void rechazarProductoInexistente() {
		// Arrange
		Aplicacion aplicacion = new Aplicacion("AP-001", HOY, "SI-001", "PR-999", 2.5, "101230456", "");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> aplicacionService.registrar(aplicacion));

		// Assert
		assertTrue(error.getMessage().contains("producto"));
		assertTrue(aplicacionRepository.buscarPorCodigo("AP-001").isEmpty());
	}

	@Test
	@DisplayName("CP-APL-05 registrar aplicación con producto recién registrado")
	void registrarAplicacionConProductoRecienRegistrado() {
		// Arrange
		productoRepository.guardar(producto("PR-03"));
		Aplicacion aplicacion = new Aplicacion("AP-001", HOY, "SI-001", "PR-03", 2.5, "101230456", "");

		// Act
		aplicacionService.registrar(aplicacion);

		// Assert
		Optional<Aplicacion> guardada = aplicacionRepository.buscarPorCodigo("AP-001");
		assertTrue(guardada.isPresent());
		assertEquals("PR-03", guardada.get().getCodigoProducto());
	}

	@Test
	@DisplayName("CP-APL-06 registrar aplicación con cantidad mínima positiva")
	void registrarAplicacionConCantidadMinima() {
		// Arrange
		Aplicacion aplicacion = new Aplicacion("AP-001", HOY, "SI-001", "PR-01", 0.01, "101230456", "");

		// Act
		aplicacionService.registrar(aplicacion);

		// Assert
		Optional<Aplicacion> guardada = aplicacionRepository.buscarPorCodigo("AP-001");
		assertTrue(guardada.isPresent());
		assertEquals(0.01, guardada.get().getCantidad(), 0.0001);
	}

	@Test
	@DisplayName("CP-APL-07 rechazar aplicación con cantidad cero")
	void rechazarAplicacionConCantidadCero() {
		// Arrange
		Aplicacion aplicacion = new Aplicacion("AP-001", HOY, "SI-001", "PR-01", 0, "101230456", "");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> aplicacionService.registrar(aplicacion));

		// Assert
		assertTrue(error.getMessage().contains("cantidad"));
		assertTrue(aplicacionRepository.buscarPorCodigo("AP-001").isEmpty());
	}

	@Test
	@DisplayName("CP-APL-08 rechazar aplicación con cantidad negativa")
	void rechazarAplicacionConCantidadNegativa() {
		// Arrange
		Aplicacion aplicacion = new Aplicacion("AP-001", HOY, "SI-001", "PR-01", -1, "101230456", "");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> aplicacionService.registrar(aplicacion));

		// Assert
		assertTrue(error.getMessage().contains("cantidad"));
		assertTrue(aplicacionRepository.buscarPorCodigo("AP-001").isEmpty());
	}

	@Test
	@DisplayName("CP-APL-09 rechazar aplicación sobre un cultivo cosechado")
	void rechazarAplicacionSobreCultivoCosechado() {
		// Arrange
		cosechaRepository.guardar(new Cosecha("CO-001", HOY.minusDays(1), "SI-001", 45.5, 30, "101230456"));
		Aplicacion aplicacion = aplicacionValida("AP-001");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> aplicacionService.registrar(aplicacion));

		// Assert
		assertTrue(error.getMessage().contains("cosechada"));
		assertTrue(aplicacionRepository.buscarPorCodigo("AP-001").isEmpty());
	}

	@Test
	@DisplayName("CP-APL-10 rechazar aplicación el mismo día de la cosecha")
	void rechazarAplicacionElMismoDiaDeLaCosecha() {
		// Arrange
		cosechaRepository.guardar(new Cosecha("CO-001", HOY, "SI-001", 45.5, 30, "101230456"));
		Aplicacion aplicacion = aplicacionValida("AP-001");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> aplicacionService.registrar(aplicacion));

		// Assert
		assertTrue(error.getMessage().contains("cosechada"));
		assertTrue(aplicacionRepository.buscarPorCodigo("AP-001").isEmpty());
	}

	@Test
	@DisplayName("CP-APL-11 rechazar responsable no registrado")
	void rechazarResponsableNoRegistrado() {
		// Arrange
		Aplicacion aplicacion = new Aplicacion("AP-001", HOY, "SI-001", "PR-01", 2.5, "999999999", "");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> aplicacionService.registrar(aplicacion));

		// Assert
		assertTrue(error.getMessage().contains("responsable"));
		assertTrue(aplicacionRepository.buscarPorCodigo("AP-001").isEmpty());
	}

	@Test
	@DisplayName("CP-APL-12 rechazar código de aplicación duplicado")
	void rechazarCodigoDeAplicacionDuplicado() {
		// Arrange
		aplicacionRepository.guardar(aplicacionValida("AP-001"));
		Aplicacion duplicada = new Aplicacion("AP-001", HOY, "SI-001", "PR-01", 9, "101230456", "");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> aplicacionService.registrar(duplicada));

		// Assert
		assertTrue(error.getMessage().contains("ya existe"));
		assertEquals(2.5, aplicacionRepository.buscarPorCodigo("AP-001").orElseThrow().getCantidad(), 0.0001);
	}

	@Test
	@DisplayName("CP-APL-13 rechazar aplicación con fecha vacía")
	void rechazarAplicacionConFechaVacia() {
		// Arrange
		Aplicacion aplicacion = new Aplicacion("AP-001", null, "SI-001", "PR-01", 2.5, "101230456", "");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> aplicacionService.registrar(aplicacion));

		// Assert
		assertTrue(error.getMessage().contains("fecha"));
		assertTrue(aplicacionRepository.buscarPorCodigo("AP-001").isEmpty());
	}

	// Crea una aplicación válida con los datos base del catálogo.
	private Aplicacion aplicacionValida(String codigo) {
		return new Aplicacion(codigo, HOY, "SI-001", "PR-01", 2.5, "101230456", "Aplicación semanal");
	}

	// Crea un producto con datos válidos y el código indicado.
	private Producto producto(String codigo) {
		return new Producto(codigo, "Cal agrícola", "Desinfectante", "Desinfección de sustrato", "kg");
	}

}
