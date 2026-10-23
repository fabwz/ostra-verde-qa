package Service;

import java.util.ArrayList;
import java.util.List;

import Exception.ReglaNegocioException;
import Model.Producto;
import Repository.AplicacionRepository;
import Repository.ProductoRepository;

/** Casos de uso del módulo de productos agrícolas. */
public class ProductoService {

	private final ProductoRepository productoRepository;
	private final AplicacionRepository aplicacionRepository;

	public ProductoService(ProductoRepository productoRepository, AplicacionRepository aplicacionRepository) {
		this.productoRepository = productoRepository;
		this.aplicacionRepository = aplicacionRepository;
	}

	/** Registra un producto nuevo (RF-08, RN-17). */
	public void registrar(Producto producto) {
		validar(producto);
		productoRepository.guardar(producto);
	}

	/** Modifica los datos de un producto existente (RF-09). */
	public void modificar(String codigo, Producto datosNuevos) {
		validarProductoExiste(codigo);
		productoRepository.guardar(datosNuevos);
	}

	/** Consulta un producto por su código (RF-10). */
	public Producto consultarPorCodigo(String codigo) {
		return null;
	}

	/** Consulta el listado de todos los productos (RF-10). */
	public List<Producto> consultarTodos() {
		return new ArrayList<>();
	}

	// Rechaza la operación si no existe un producto con ese código (RF-09).
	private void validarProductoExiste(String codigo) {
		if (productoRepository.buscarPorCodigo(codigo).isEmpty()) {
			throw new ReglaNegocioException("El producto no existe");
		}
	}

	// Agrupa las validaciones del registro (RF-08, RN-17).
	private void validar(Producto producto) {
		validarUnidadMedida(producto.getUnidadMedida());
		validarCodigoUnico(producto.getCodigo());
	}

	// Rechaza la operación si ya existe un producto con ese código (RN-17).
	private void validarCodigoUnico(String codigo) {
		if (productoRepository.buscarPorCodigo(codigo).isPresent()) {
			throw new ReglaNegocioException("El código del producto ya existe");
		}
	}

	// Exige que la unidad de medida no sea nula ni esté en blanco (RF-08).
	private void validarUnidadMedida(String unidadMedida) {
		if (unidadMedida == null || unidadMedida.isBlank()) {
			throw new ReglaNegocioException("La unidad de medida es obligatoria");
		}
	}

}
