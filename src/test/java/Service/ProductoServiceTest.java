package Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
