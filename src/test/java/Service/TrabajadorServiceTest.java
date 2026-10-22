package Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Exception.ReglaNegocioException;
import Model.Trabajador;
import Repository.TrabajadorRepository;
import Repository.TrabajadorRepositoryImpl;

class TrabajadorServiceTest {

	private TrabajadorRepository trabajadorRepository;
	private TrabajadorService trabajadorService;

	@BeforeEach
	void setUp() {
		trabajadorRepository = new TrabajadorRepositoryImpl();
		trabajadorService = new TrabajadorService(trabajadorRepository);
	}

	@Test
	@DisplayName("CP-PER-01 registrar trabajador válido")
	void registrarTrabajadorValido() {
		// Arrange
		Trabajador trabajador = new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha");

		// Act
		trabajadorService.registrar(trabajador);

		// Assert
		Optional<Trabajador> guardado = trabajadorRepository.buscarPorIdentificacion("101230456");
		assertTrue(guardado.isPresent());
		assertEquals("101230456", guardado.get().getIdentificacion());
		assertEquals("Ana Mora Solís", guardado.get().getNombreCompleto());
		assertEquals("88881234", guardado.get().getTelefono());
		assertEquals("ana.mora@ostraverde.cr", guardado.get().getCorreo());
		assertEquals("Operaria de cosecha", guardado.get().getCargo());
	}

	@Test
	@DisplayName("CP-PER-07 registrar mismo nombre con identificación distinta")
	void registrarMismoNombreConIdentificacionDistinta() {
		// Arrange
		trabajadorService.registrar(new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha"));
		Trabajador trabajador = new Trabajador("404560789", "Ana Mora Solís", "88885678",
				"ana.mora2@ostraverde.cr", "Operaria de cosecha");

		// Act
		trabajadorService.registrar(trabajador);

		// Assert
		Optional<Trabajador> guardado = trabajadorRepository.buscarPorIdentificacion("404560789");
		assertTrue(guardado.isPresent());
		assertEquals("404560789", guardado.get().getIdentificacion());
		assertEquals("Ana Mora Solís", guardado.get().getNombreCompleto());
		assertEquals("88885678", guardado.get().getTelefono());
		assertEquals("ana.mora2@ostraverde.cr", guardado.get().getCorreo());
		assertEquals("Operaria de cosecha", guardado.get().getCargo());
	}

	@Test
	@DisplayName("CP-PER-08 registrar identificación distinta por un dígito")
	void registrarIdentificacionDistintaPorUnDigito() {
		// Arrange
		trabajadorService.registrar(new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha"));
		Trabajador trabajador = new Trabajador("101230457", "Luis Vega Rojas", "70001234",
				"luis.vega@ostraverde.cr", "Operario de cosecha");

		// Act
		trabajadorService.registrar(trabajador);

		// Assert
		Optional<Trabajador> guardado = trabajadorRepository.buscarPorIdentificacion("101230457");
		assertTrue(guardado.isPresent());
		assertEquals("101230457", guardado.get().getIdentificacion());
		assertEquals("Luis Vega Rojas", guardado.get().getNombreCompleto());
		assertEquals("70001234", guardado.get().getTelefono());
		assertEquals("luis.vega@ostraverde.cr", guardado.get().getCorreo());
		assertEquals("Operario de cosecha", guardado.get().getCargo());
	}

	@Test
	@DisplayName("CP-PER-02 rechazar cargo vacío")
	void rechazarCargoVacio() {
		// Arrange
		Trabajador trabajador = new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> trabajadorService.registrar(trabajador));

		// Assert
		assertTrue(error.getMessage().contains("cargo"));
		assertTrue(trabajadorRepository.buscarPorIdentificacion("101230456").isEmpty());
	}

	@Test
	@DisplayName("CP-PER-03 rechazar correo sin arroba")
	void rechazarCorreoSinArroba() {
		// Arrange
		Trabajador trabajador = new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora.ostraverde.cr", "Operaria de cosecha");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> trabajadorService.registrar(trabajador));

		// Assert
		assertTrue(error.getMessage().contains("correo"));
		assertTrue(trabajadorRepository.buscarPorIdentificacion("101230456").isEmpty());
	}

	@Test
	@DisplayName("CP-PER-04 rechazar teléfono de 7 dígitos")
	void rechazarTelefonoDeSieteDigitos() {
		// Arrange
		Trabajador trabajador = new Trabajador("101230456", "Ana Mora Solís", "8888123",
				"ana.mora@ostraverde.cr", "Operaria de cosecha");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> trabajadorService.registrar(trabajador));

		// Assert
		assertTrue(error.getMessage().contains("teléfono"));
		assertTrue(trabajadorRepository.buscarPorIdentificacion("101230456").isEmpty());
	}

	@Test
	@DisplayName("CP-PER-05 rechazar teléfono no numérico")
	void rechazarTelefonoNoNumerico() {
		// Arrange
		Trabajador trabajador = new Trabajador("101230456", "Ana Mora Solís", "8888abcd",
				"ana.mora@ostraverde.cr", "Operaria de cosecha");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> trabajadorService.registrar(trabajador));

		// Assert
		assertTrue(error.getMessage().contains("teléfono"));
		assertTrue(trabajadorRepository.buscarPorIdentificacion("101230456").isEmpty());
	}

}
