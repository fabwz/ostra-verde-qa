package Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import Model.Trabajador;

/** Implementación en memoria de TrabajadorRepository (S-16). */
public class TrabajadorRepositoryImpl implements TrabajadorRepository {

	private final Map<String, Trabajador> trabajadores = new HashMap<>();

	/** Guarda el trabajador indexado por su identificación (RF-04). */
	@Override
	public void guardar(Trabajador trabajador) {
		trabajadores.put(trabajador.getIdentificacion(), trabajador);
	}

	/** Busca un trabajador por su identificación (RF-04). */
	@Override
	public Optional<Trabajador> buscarPorIdentificacion(String identificacion) {
		return Optional.ofNullable(trabajadores.get(identificacion));
	}

	/** Devuelve una copia con todos los trabajadores guardados (RF-05). */
	@Override
	public List<Trabajador> listarTodos() {
		return new ArrayList<>(trabajadores.values());
	}

}
