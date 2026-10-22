package Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import Model.Seccion;

/** Implementación en memoria de SeccionRepository (S-16). */
public class SeccionRepositoryImpl implements SeccionRepository {

	private final Map<String, Seccion> secciones = new HashMap<>();

	/** Guarda la sección indexada por su código (RF-01). */
	@Override
	public void guardar(Seccion seccion) {
		secciones.put(seccion.getCodigo(), seccion);
	}

	/** Busca una sección por su código (RF-01). */
	@Override
	public Optional<Seccion> buscarPorCodigo(String codigo) {
		return Optional.ofNullable(secciones.get(codigo));
	}

	@Override
	public List<Seccion> listarTodas() {
		return new ArrayList<>();
	}

}
