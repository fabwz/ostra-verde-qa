package Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Model.Seccion;

/** Implementación en memoria de SeccionRepository (S-16). */
public class SeccionRepositoryImpl implements SeccionRepository {

	@Override
	public void guardar(Seccion seccion) {
	}

	@Override
	public Optional<Seccion> buscarPorCodigo(String codigo) {
		return Optional.empty();
	}

	@Override
	public List<Seccion> listarTodas() {
		return new ArrayList<>();
	}

}
