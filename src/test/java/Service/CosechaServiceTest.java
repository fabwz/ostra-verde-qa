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
import Model.Cosecha;
import Model.EstadoSeccion;
import Model.Seccion;
import Model.Siembra;
import Model.Trabajador;
import Repository.CosechaRepository;
import Repository.CosechaRepositoryImpl;
import Repository.SeccionRepository;
import Repository.SeccionRepositoryImpl;
import Repository.SiembraRepository;
import Repository.SiembraRepositoryImpl;
import Repository.TrabajadorRepository;
import Repository.TrabajadorRepositoryImpl;

class CosechaServiceTest {

	private static final LocalDate HOY = LocalDate.of(2026, 10, 22);
	private static final LocalDate FECHA_SIEMBRA = HOY.minusDays(30);

	private CosechaRepository cosechaRepository;
	private SiembraRepository siembraRepository;
	private SeccionRepository seccionRepository;
	private TrabajadorRepository trabajadorRepository;
	private CosechaService cosechaService;

	@BeforeEach
	void setUp() {
		cosechaRepository = new CosechaRepositoryImpl();
		siembraRepository = new SiembraRepositoryImpl();
		seccionRepository = new SeccionRepositoryImpl();
		trabajadorRepository = new TrabajadorRepositoryImpl();
		cosechaService = new CosechaService(cosechaRepository, siembraRepository,
				seccionRepository, trabajadorRepository);
		seccionRepository.guardar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.OCUPADA));
		siembraRepository.guardar(new Siembra("SI-001", FECHA_SIEMBRA, "S-01", 200, "101230456", ""));
		trabajadorRepository.guardar(new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha"));
	}

	@Test
	@DisplayName("CP-COS-01 registrar cosecha válida")
	void registrarCosechaValida() {
		// Arrange
		Cosecha cosecha = new Cosecha("CO-001", HOY, "SI-001", 45.5, 30, "101230456");

		// Act
		cosechaService.registrar(cosecha);

		// Assert
		Optional<Cosecha> guardada = cosechaRepository.buscarPorCodigo("CO-001");
		assertTrue(guardada.isPresent());
		assertEquals("SI-001", guardada.get().getCodigoSiembra());
		assertEquals(45.5, guardada.get().getPesoKg(), 0.0001);
		assertEquals(30, guardada.get().getCantidadBandejas());
	}

	@Test
	@DisplayName("CP-COS-02 la sección queda Disponible tras registrar la cosecha")
	void seccionQuedaDisponibleTrasCosechar() {
		// Arrange
		SeccionService seccionService = new SeccionService(seccionRepository);
		Cosecha cosecha = cosechaValida("CO-001");

		// Act
		cosechaService.registrar(cosecha);

		// Assert
		assertEquals(EstadoSeccion.DISPONIBLE, estadoDe("S-01"));
		assertTrue(seccionService.consultarDisponibles().stream()
				.anyMatch(seccion -> seccion.getCodigo().equals("S-01")));
	}

	@Test
	@DisplayName("CP-COS-03 rechazar segunda cosecha de la misma siembra")
	void rechazarSegundaCosechaDeLaMismaSiembra() {
		// Arrange
		cosechaRepository.guardar(cosechaValida("CO-001"));
		Cosecha segunda = cosechaValida("CO-002");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> cosechaService.registrar(segunda));

		// Assert
		assertTrue(error.getMessage().contains("cosechada"));
		assertTrue(cosechaRepository.buscarPorCodigo("CO-002").isEmpty());
	}

	@Test
	@DisplayName("CP-COS-04 rechazar segunda cosecha de la misma siembra el mismo día")
	void rechazarSegundaCosechaElMismoDia() {
		// Arrange
		cosechaRepository.guardar(new Cosecha("CO-001", HOY, "SI-001", 45.5, 30, "101230456"));
		Cosecha segunda = new Cosecha("CO-002", HOY, "SI-001", 10, 5, "101230456");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> cosechaService.registrar(segunda));

		// Assert
		assertTrue(error.getMessage().contains("cosechada"));
		assertEquals(1, cosechaRepository.listarTodas().size());
	}

	@Test
	@DisplayName("CP-COS-05 registrar cosecha con peso mínimo positivo")
	void registrarCosechaConPesoMinimo() {
		// Arrange
		Cosecha cosecha = new Cosecha("CO-001", HOY, "SI-001", 0.01, 30, "101230456");

		// Act
		cosechaService.registrar(cosecha);

		// Assert
		Optional<Cosecha> guardada = cosechaRepository.buscarPorCodigo("CO-001");
		assertTrue(guardada.isPresent());
		assertEquals(0.01, guardada.get().getPesoKg(), 0.0001);
	}

	@Test
	@DisplayName("CP-COS-06 rechazar cosecha con peso cero")
	void rechazarCosechaConPesoCero() {
		// Arrange
		Cosecha cosecha = new Cosecha("CO-001", HOY, "SI-001", 0, 30, "101230456");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> cosechaService.registrar(cosecha));

		// Assert
		assertTrue(error.getMessage().contains("peso"));
		assertTrue(cosechaRepository.buscarPorCodigo("CO-001").isEmpty());
		assertEquals(EstadoSeccion.OCUPADA, estadoDe("S-01"));
	}

	@Test
	@DisplayName("CP-COS-07 rechazar cosecha con peso negativo")
	void rechazarCosechaConPesoNegativo() {
		// Arrange
		Cosecha cosecha = new Cosecha("CO-001", HOY, "SI-001", -3, 30, "101230456");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> cosechaService.registrar(cosecha));

		// Assert
		assertTrue(error.getMessage().contains("peso"));
		assertTrue(cosechaRepository.buscarPorCodigo("CO-001").isEmpty());
	}

	@Test
	@DisplayName("CP-COS-08 registrar cosecha el día siguiente a la siembra")
	void registrarCosechaElDiaSiguienteALaSiembra() {
		// Arrange
		Cosecha cosecha = new Cosecha("CO-001", FECHA_SIEMBRA.plusDays(1), "SI-001", 45.5, 30, "101230456");

		// Act
		cosechaService.registrar(cosecha);

		// Assert
		Optional<Cosecha> guardada = cosechaRepository.buscarPorCodigo("CO-001");
		assertTrue(guardada.isPresent());
		assertEquals(FECHA_SIEMBRA.plusDays(1), guardada.get().getFecha());
	}

	@Test
	@DisplayName("CP-COS-09 rechazar cosecha con la misma fecha de la siembra")
	void rechazarCosechaConLaMismaFechaDeSiembra() {
		// Arrange
		Cosecha cosecha = new Cosecha("CO-001", FECHA_SIEMBRA, "SI-001", 45.5, 30, "101230456");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> cosechaService.registrar(cosecha));

		// Assert
		assertTrue(error.getMessage().contains("posterior"));
		assertTrue(cosechaRepository.buscarPorCodigo("CO-001").isEmpty());
	}

	@Test
	@DisplayName("CP-COS-10 rechazar cosecha anterior a la siembra")
	void rechazarCosechaAnteriorALaSiembra() {
		// Arrange
		Cosecha cosecha = new Cosecha("CO-001", FECHA_SIEMBRA.minusDays(1), "SI-001", 45.5, 30, "101230456");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> cosechaService.registrar(cosecha));

		// Assert
		assertTrue(error.getMessage().contains("posterior"));
		assertTrue(cosechaRepository.buscarPorCodigo("CO-001").isEmpty());
	}

	@Test
	@DisplayName("CP-COS-11 rechazar siembra inexistente")
	void rechazarSiembraInexistente() {
		// Arrange
		Cosecha cosecha = new Cosecha("CO-001", HOY, "SI-999", 45.5, 30, "101230456");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> cosechaService.registrar(cosecha));

		// Assert
		assertTrue(error.getMessage().contains("siembra"));
		assertTrue(cosechaRepository.buscarPorCodigo("CO-001").isEmpty());
	}

	@Test
	@DisplayName("CP-COS-12 rechazar cosecha con bandejas negativas")
	void rechazarCosechaConBandejasNegativas() {
		// Arrange
		Cosecha cosecha = new Cosecha("CO-001", HOY, "SI-001", 45.5, -2, "101230456");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> cosechaService.registrar(cosecha));

		// Assert
		assertTrue(error.getMessage().contains("bandejas"));
		assertTrue(cosechaRepository.buscarPorCodigo("CO-001").isEmpty());
	}

	@Test
	@DisplayName("CP-COS-13 rechazar cosecha con cero bandejas")
	void rechazarCosechaConCeroBandejas() {
		// Arrange
		Cosecha cosecha = new Cosecha("CO-001", HOY, "SI-001", 45.5, 0, "101230456");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> cosechaService.registrar(cosecha));

		// Assert
		assertTrue(error.getMessage().contains("bandejas"));
		assertTrue(cosechaRepository.buscarPorCodigo("CO-001").isEmpty());
	}

	@Test
	@DisplayName("CP-COS-14 rechazar responsable no registrado")
	void rechazarResponsableNoRegistrado() {
		// Arrange
		Cosecha cosecha = new Cosecha("CO-001", HOY, "SI-001", 45.5, 30, "999999999");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> cosechaService.registrar(cosecha));

		// Assert
		assertTrue(error.getMessage().contains("responsable"));
		assertTrue(cosechaRepository.buscarPorCodigo("CO-001").isEmpty());
		assertEquals(EstadoSeccion.OCUPADA, estadoDe("S-01"));
	}

	@Test
	@DisplayName("CP-COS-15 rechazar código de cosecha duplicado")
	void rechazarCodigoDeCosechaDuplicado() {
		// Arrange
		seccionRepository.guardar(new Seccion("S-04", "Sala Este", 300, EstadoSeccion.OCUPADA));
		siembraRepository.guardar(new Siembra("SI-004", FECHA_SIEMBRA, "S-04", 100, "101230456", ""));
		cosechaRepository.guardar(cosechaValida("CO-001"));
		Cosecha duplicada = new Cosecha("CO-001", HOY, "SI-004", 20, 10, "101230456");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> cosechaService.registrar(duplicada));

		// Assert
		assertTrue(error.getMessage().contains("ya existe"));
		assertTrue(cosechaRepository.buscarPorSiembra("SI-004").isEmpty());
		assertEquals(EstadoSeccion.OCUPADA, estadoDe("S-04"));
	}

	@Test
	@DisplayName("CP-COS-16 rechazar cosecha con peso cero y mantener la sección Ocupada")
	void rechazarCosechaConPesoCeroMantieneSeccionOcupada() {
		// Arrange
		seccionRepository.guardar(new Seccion("S-02", "Sala Sur", 400, EstadoSeccion.OCUPADA));
		siembraRepository.guardar(new Siembra("SI-002", FECHA_SIEMBRA, "S-02", 100, "101230456", ""));
		Cosecha cosecha = new Cosecha("CO-002", HOY, "SI-002", 0, 10, "101230456");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> cosechaService.registrar(cosecha));

		// Assert
		assertTrue(error.getMessage().contains("peso"));
		assertEquals(EstadoSeccion.OCUPADA, estadoDe("S-02"));
	}

	@Test
	@DisplayName("CP-COS-17 cosechar libera solo la sección de la siembra cosechada")
	void cosecharLiberaSoloSuSeccion() {
		// Arrange
		seccionRepository.guardar(new Seccion("S-02", "Sala Sur", 400, EstadoSeccion.OCUPADA));
		siembraRepository.guardar(new Siembra("SI-002", FECHA_SIEMBRA, "S-02", 100, "101230456", ""));
		Cosecha cosecha = cosechaValida("CO-001");

		// Act
		cosechaService.registrar(cosecha);

		// Assert
		assertEquals(EstadoSeccion.DISPONIBLE, estadoDe("S-01"));
		assertEquals(EstadoSeccion.OCUPADA, estadoDe("S-02"));
	}

	// Crea una cosecha válida de SI-001 con los datos base del catálogo.
	private Cosecha cosechaValida(String codigo) {
		return new Cosecha(codigo, HOY, "SI-001", 45.5, 30, "101230456");
	}

	// Consulta el estado actual de una sección guardada.
	private EstadoSeccion estadoDe(String codigoSeccion) {
		return seccionRepository.buscarPorCodigo(codigoSeccion).orElseThrow().getEstado();
	}

}
