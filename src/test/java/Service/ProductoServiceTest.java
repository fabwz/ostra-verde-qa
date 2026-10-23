package Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import Exception.ReglaNegocioException;
import Model.EstadoSeccion;
import Model.Producto;
import Model.Seccion;
import Repository.AplicacionRepository;
import Repository.ProductoRepository;
import Repository.ProductoRepositoryImpl;
import Repository.SeccionRepositoryImpl;

class ProductoServiceTest {

	private ProductoRepository productoRepository;
	private AplicacionRepository aplicacionRepository;
	private ProductoService productoService;

	@BeforeEach
	void setUp() {
		productoRepository = new ProductoRepositoryImpl();
		aplicacionRepository = Mockito.mock(AplicacionRepository.class);
		productoService = new ProductoService(productoRepository, aplicacionRepository);
	}

	@Test
	@DisplayName("CP-PRO-01 registrar producto válido")
	void registrarProductoValido() {
		// Arrange
		Producto producto = new Producto("PR-01", "Cal agrícola", "Desinfectante",
				"Desinfección de sustrato", "kg");

		// Act
		productoService.registrar(producto);

		// Assert
		verificarGuardado("PR-01", "Cal agrícola", "Desinfectante", "Desinfección de sustrato", "kg");
	}

	@Test
	@DisplayName("CP-PRO-03 registrar producto con código igual al de una sección")
	void registrarProductoConCodigoDeSeccion() {
		// Arrange
		SeccionService seccionService = new SeccionService(new SeccionRepositoryImpl());
		seccionService.registrar(new Seccion("S-01", "Sala Norte", 500, EstadoSeccion.DISPONIBLE));
		Producto producto = new Producto("S-01", "Cal agrícola", "Desinfectante",
				"Desinfección de sustrato", "kg");

		// Act
		productoService.registrar(producto);

		// Assert
		verificarGuardado("S-01", "Cal agrícola", "Desinfectante", "Desinfección de sustrato", "kg");
	}

	@Test
	@DisplayName("CP-PRO-05 registrar producto con descripción vacía")
	void registrarProductoConDescripcionVacia() {
		// Arrange
		Producto producto = new Producto("PR-02", "Cal agrícola", "Desinfectante", "", "kg");

		// Act
		productoService.registrar(producto);

		// Assert
		verificarGuardado("PR-02", "Cal agrícola", "Desinfectante", "", "kg");
	}

	@Test
	@DisplayName("CP-PRO-02 rechazar producto con código duplicado")
	void rechazarProductoConCodigoDuplicado() {
		// Arrange
		productoService.registrar(new Producto("PR-01", "Cal agrícola", "Desinfectante",
				"Desinfección de sustrato", "kg"));
		Producto duplicado = new Producto("PR-01", "Fungicida", "Fungicida", "", "litro");

		// Act
		ReglaNegocioException excepcion = assertThrows(ReglaNegocioException.class,
				() -> productoService.registrar(duplicado));

		// Assert
		assertTrue(excepcion.getMessage().contains("código"));
		Producto original = productoRepository.buscarPorCodigo("PR-01").get();
		assertEquals("Cal agrícola", original.getNombre());
		assertEquals("kg", original.getUnidadMedida());
	}

	@Test
	@DisplayName("CP-PRO-04 rechazar producto con unidad de medida vacía")
	void rechazarProductoConUnidadVacia() {
		// Arrange
		Producto producto = new Producto("PR-03", "Cal agrícola", "Desinfectante",
				"Desinfección de sustrato", "");

		// Act
		ReglaNegocioException excepcion = assertThrows(ReglaNegocioException.class,
				() -> productoService.registrar(producto));

		// Assert
		assertTrue(excepcion.getMessage().contains("unidad"));
		assertTrue(productoRepository.buscarPorCodigo("PR-03").isEmpty());
	}

	@Test
	@DisplayName("CP-PRO-06 modificar nombre y descripción de un producto")
	void modificarNombreYDescripcion() {
		// Arrange
		productoService.registrar(new Producto("PR-01", "Cal agrícola", "Desinfectante",
				"Desinfección de sustrato", "kg"));
		Producto datosNuevos = new Producto("PR-01", "Cal hidratada", "Desinfectante",
				"Desinfección de sustrato y paredes", "kg");

		// Act
		productoService.modificar("PR-01", datosNuevos);

		// Assert
		verificarGuardado("PR-01", "Cal hidratada", "Desinfectante",
				"Desinfección de sustrato y paredes", "kg");
	}

	@Test
	@DisplayName("CP-PRO-07 rechazar modificación de producto inexistente")
	void rechazarModificacionDeProductoInexistente() {
		// Arrange
		Producto datosNuevos = new Producto("PR-99", "Cal hidratada", "Desinfectante",
				"Desinfección de sustrato y paredes", "kg");

		// Act
		ReglaNegocioException excepcion = assertThrows(ReglaNegocioException.class,
				() -> productoService.modificar("PR-99", datosNuevos));

		// Assert
		assertTrue(excepcion.getMessage().contains("no existe"));
	}

	@Test
	@DisplayName("CP-PRO-08 modificar producto con aplicación registrada")
	void modificarProductoConAplicacionRegistrada() {
		// Arrange
		productoService.registrar(new Producto("PR-01", "Cal agrícola", "Desinfectante",
				"Desinfección de sustrato", "kg"));
		Mockito.when(aplicacionRepository.existeConProducto("PR-01")).thenReturn(true);
		Producto datosNuevos = new Producto("PR-01", "Cal agrícola", "Desinfectante",
				"Desinfección de sustrato y paredes", "kg");

		// Act
		productoService.modificar("PR-01", datosNuevos);

		// Assert
		verificarGuardado("PR-01", "Cal agrícola", "Desinfectante",
				"Desinfección de sustrato y paredes", "kg");
		Mockito.verify(aplicacionRepository, Mockito.never()).guardar(ArgumentMatchers.any());
	}

	@Test
	@DisplayName("CP-PRO-09 consultar producto por código")
	void consultarProductoPorCodigo() {
		// Arrange
		productoService.registrar(new Producto("PR-01", "Cal agrícola", "Desinfectante",
				"Desinfección de sustrato", "kg"));

		// Act
		Producto consultado = productoService.consultarPorCodigo("PR-01");

		// Assert
		assertNotNull(consultado);
		assertEquals("PR-01", consultado.getCodigo());
		assertEquals("Cal agrícola", consultado.getNombre());
		assertEquals("Desinfectante", consultado.getTipo());
		assertEquals("Desinfección de sustrato", consultado.getDescripcion());
		assertEquals("kg", consultado.getUnidadMedida());
	}

	@Test
	@DisplayName("CP-PRO-10 rechazar consulta de producto inexistente")
	void rechazarConsultaDeProductoInexistente() {
		// Arrange
		String codigo = "PR-99";

		// Act
		ReglaNegocioException excepcion = assertThrows(ReglaNegocioException.class,
				() -> productoService.consultarPorCodigo(codigo));

		// Assert
		assertTrue(excepcion.getMessage().contains("encontrado"));
	}

	@Test
	@DisplayName("CP-PRO-11 consultar listado sin productos registrados")
	void consultarListadoSinProductos() {
		// Arrange
		// Sin productos registrados.

		// Act
		List<Producto> productos = productoService.consultarTodos();

		// Assert
		assertTrue(productos.isEmpty());
	}

	// Comprueba que el producto existe y que sus cinco campos coinciden con los esperados.
	private void verificarGuardado(String codigo, String nombre, String tipo, String descripcion,
			String unidadMedida) {
		Optional<Producto> guardado = productoRepository.buscarPorCodigo(codigo);
		assertTrue(guardado.isPresent());
		assertEquals(codigo, guardado.get().getCodigo());
		assertEquals(nombre, guardado.get().getNombre());
		assertEquals(tipo, guardado.get().getTipo());
		assertEquals(descripcion, guardado.get().getDescripcion());
		assertEquals(unidadMedida, guardado.get().getUnidadMedida());
	}

}
