package Service;

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

	/** Elimina un producto que nunca ha sido utilizado (RF-11, RN-07). */
	public void eliminar(String codigo) {
		validarProductoExiste(codigo);
		validarProductoNoUtilizado(codigo);
		productoRepository.eliminar(codigo);
	}

	/** Consulta un producto por su código (RF-10). */
	public Producto consultarPorCodigo(String codigo) {
		return productoRepository.buscarPorCodigo(codigo)
				.orElseThrow(() -> new ReglaNegocioException("Producto no encontrado"));
	}

	/** Consulta el listado de todos los productos (RF-10). */
	public List<Producto> consultarTodos() {
		return productoRepository.listarTodos();
	}

	// Rechaza la operación si no existe un producto con ese código (RF-09).
	private void validarProductoExiste(String codigo) {
		if (productoRepository.buscarPorCodigo(codigo).isEmpty()) {
			throw new ReglaNegocioException("El producto no existe");
		}
	}

	// Rechaza la eliminación si el producto tiene al menos una aplicación (RN-07, S-13).
	private void validarProductoNoUtilizado(String codigo) {
		if (aplicacionRepository.existeConProducto(codigo)) {
			throw new ReglaNegocioException("El producto ya fue utilizado y no puede eliminarse");
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
