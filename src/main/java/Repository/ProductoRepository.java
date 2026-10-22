package Repository;

import java.util.List;
import java.util.Optional;

import Model.Producto;

/** Almacenamiento de productos agrícolas. */
public interface ProductoRepository {

	void guardar(Producto producto);

	Optional<Producto> buscarPorCodigo(String codigo);

	List<Producto> listarTodos();

	void eliminar(String codigo);

}
