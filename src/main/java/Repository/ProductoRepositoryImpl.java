package Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import Model.Producto;

/** Implementación en memoria de ProductoRepository (S-16). */
public class ProductoRepositoryImpl implements ProductoRepository {

	private final Map<String, Producto> productos = new HashMap<>();

	/** Guarda el producto indexado por su código (RF-08). */
	@Override
	public void guardar(Producto producto) {
		productos.put(producto.getCodigo(), producto);
	}

	/** Busca un producto por su código (RF-08). */
	@Override
	public Optional<Producto> buscarPorCodigo(String codigo) {
		return Optional.ofNullable(productos.get(codigo));
	}

	/** Devuelve una copia con todos los productos guardados (RF-10). */
	@Override
	public List<Producto> listarTodos() {
		return new ArrayList<>(productos.values());
	}

	/** Elimina el producto con ese código (RF-11). */
	@Override
	public void eliminar(String codigo) {
		productos.remove(codigo);
	}

}
