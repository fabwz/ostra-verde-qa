package Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Model.Producto;

/** Implementación en memoria de ProductoRepository (S-16). */
public class ProductoRepositoryImpl implements ProductoRepository {

	@Override
	public void guardar(Producto producto) {
	}

	@Override
	public Optional<Producto> buscarPorCodigo(String codigo) {
		return Optional.empty();
	}

	@Override
	public List<Producto> listarTodos() {
		return new ArrayList<>();
	}

	@Override
	public void eliminar(String codigo) {
	}

}
