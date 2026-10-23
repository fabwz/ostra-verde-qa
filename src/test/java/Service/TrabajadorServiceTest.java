package Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
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
	@DisplayName("CP-PER-06 rechazar identificación duplicada")
	void rechazarIdentificacionDuplicada() {
		// Arrange
		trabajadorService.registrar(new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha"));
		Trabajador duplicado = new Trabajador("101230456", "Luis Vega Rojas", "70001234",
				"luis.vega@ostraverde.cr", "Operario de cosecha");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> trabajadorService.registrar(duplicado));

		// Assert
		assertTrue(error.getMessage().contains("identificación"));
		Trabajador existente = trabajadorRepository.buscarPorIdentificacion("101230456").get();
		assertEquals("Ana Mora Solís", existente.getNombreCompleto());
		assertEquals("88881234", existente.getTelefono());
		assertEquals("ana.mora@ostraverde.cr", existente.getCorreo());
		assertEquals("Operaria de cosecha", existente.getCargo());
	}

	@Test
	@DisplayName("CP-PER-07registrar mismo nombre con identificación distinta")
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
	@DisplayName("CP-PER-09 consultar trabajador por identificación")
	void consultarTrabajadorPorIdentificacion() {
		// Arrange
		trabajadorService.registrar(new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha"));

		// Act
		Trabajador consultado = trabajadorService.consultarPorIdentificacion("101230456");

		// Assert
		assertEquals("101230456", consultado.getIdentificacion());
		assertEquals("Ana Mora Solís", consultado.getNombreCompleto());
		assertEquals("88881234", consultado.getTelefono());
		assertEquals("ana.mora@ostraverde.cr", consultado.getCorreo());
		assertEquals("Operaria de cosecha", consultado.getCargo());
	}

	@Test
	@DisplayName("CP-PER-10 rechazar consulta de trabajador inexistente")
	void rechazarConsultaDeTrabajadorInexistente() {
		// Arrange
		String identificacion = "999999999";

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> trabajadorService.consultarPorIdentificacion(identificacion));

		// Assert
		assertTrue(error.getMessage().contains("encontrado"));
	}

	@Test
	@DisplayName("CP-PER-11 consultar listado sin trabajadores")
	void consultarListadoSinTrabajadores() {
		// Arrange
		// Sin trabajadores registrados.

		// Act
		List<Trabajador> trabajadores = trabajadorService.consultarTodos();

		// Assert
		assertTrue(trabajadores.isEmpty());
	}

	@Test
	@DisplayName("CP-PER-02rechazar cargo vacío")
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

	@Test
	@DisplayName("CP-PER-12 actualizar teléfono y cargo")
	void actualizarTelefonoYCargo() {
		// Arrange
		trabajadorService.registrar(new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha"));
		Trabajador datosNuevos = new Trabajador("101230456", "Ana Mora Solís", "70001234",
				"ana.mora@ostraverde.cr", "Supervisora");

		// Act
		trabajadorService.actualizar("101230456", datosNuevos);

		// Assert
		Trabajador actualizado = trabajadorRepository.buscarPorIdentificacion("101230456").get();
		assertEquals("101230456", actualizado.getIdentificacion());
		assertEquals("Ana Mora Solís", actualizado.getNombreCompleto());
		assertEquals("70001234", actualizado.getTelefono());
		assertEquals("ana.mora@ostraverde.cr", actualizado.getCorreo());
		assertEquals("Supervisora", actualizado.getCargo());
	}

	@Test
	@DisplayName("CP-PER-13 rechazar actualización de trabajador inexistente")
	void rechazarActualizacionDeTrabajadorInexistente() {
		// Arrange
		Trabajador datosNuevos = new Trabajador("999999999", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> trabajadorService.actualizar("999999999", datosNuevos));

		// Assert
		assertTrue(error.getMessage().contains("no existe"));
	}

	@Test
	@DisplayName("CP-PER-14 rechazar cambio a identificación ya registrada")
	void rechazarCambioAIdentificacionYaRegistrada() {
		// Arrange
		trabajadorService.registrar(new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha"));
		trabajadorService.registrar(new Trabajador("202340567", "Luis Vega Rojas", "70001234",
				"luis.vega@ostraverde.cr", "Operario de cosecha"));
		Trabajador datosNuevos = new Trabajador("101230456", "Luis Vega Rojas", "70001234",
				"luis.vega@ostraverde.cr", "Operario de cosecha");

		// Act
		ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
				() -> trabajadorService.actualizar("202340567", datosNuevos));

		// Assert
		assertTrue(error.getMessage().contains("identificación"));
		Trabajador luis = trabajadorRepository.buscarPorIdentificacion("202340567").get();
		assertEquals("202340567", luis.getIdentificacion());
		assertEquals("Luis Vega Rojas", luis.getNombreCompleto());
		assertEquals("70001234", luis.getTelefono());
		assertEquals("luis.vega@ostraverde.cr", luis.getCorreo());
		assertEquals("Operario de cosecha", luis.getCargo());
		Trabajador ana = trabajadorRepository.buscarPorIdentificacion("101230456").get();
		assertEquals("Ana Mora Solís", ana.getNombreCompleto());
		assertEquals("88881234", ana.getTelefono());
		assertEquals("ana.mora@ostraverde.cr", ana.getCorreo());
		assertEquals("Operaria de cosecha", ana.getCargo());
	}

	@Test
	@DisplayName("CP-PER-15 actualizar sin modificar datos")
	void actualizarSinModificarDatos() {
		// Arrange
		trabajadorService.registrar(new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha"));
		Trabajador mismosDatos = new Trabajador("101230456", "Ana Mora Solís", "88881234",
				"ana.mora@ostraverde.cr", "Operaria de cosecha");

		// Act
		trabajadorService.actualizar("101230456", mismosDatos);

		// Assert
		Trabajador actualizado = trabajadorRepository.buscarPorIdentificacion("101230456").get();
		assertEquals("101230456", actualizado.getIdentificacion());
		assertEquals("Ana Mora Solís", actualizado.getNombreCompleto());
		assertEquals("88881234", actualizado.getTelefono());
		assertEquals("ana.mora@ostraverde.cr", actualizado.getCorreo());
		assertEquals("Operaria de cosecha", actualizado.getCargo());
	}

}
