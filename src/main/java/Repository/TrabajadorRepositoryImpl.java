package Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Model.Trabajador;

/** Implementación en memoria de TrabajadorRepository (S-16). */
public class TrabajadorRepositoryImpl implements TrabajadorRepository {

	@Override
	public void guardar(Trabajador trabajador) {
	}

	@Override
	public Optional<Trabajador> buscarPorIdentificacion(String identificacion) {
		return Optional.empty();
	}

	@Override
	public List<Trabajador> listarTodos() {
		return new ArrayList<>();
	}

}
