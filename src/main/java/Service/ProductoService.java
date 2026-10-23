package Service;

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
		productoRepository.guardar(producto);
	}

}
