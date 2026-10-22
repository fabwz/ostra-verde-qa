package Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

}
