package Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import Model.Siembra;

/** Implementación en memoria de SiembraRepository (S-16). */
public class SiembraRepositoryImpl implements SiembraRepository {

	private final Map<String, Siembra> siembras = new LinkedHashMap<>();

	/** Guarda la siembra indexada por su código (RF-07). */
	@Override
	public void guardar(Siembra siembra) {
		siembras.put(siembra.getCodigo(), siembra);
	}

	/** Busca una siembra por su código (RF-07). */
	@Override
	public Optional<Siembra> buscarPorCodigo(String codigo) {
		return Optional.ofNullable(siembras.get(codigo));
	}

	/** Devuelve una copia con todas las siembras guardadas (RF-07). */
	@Override
	public List<Siembra> listarTodas() {
		return new ArrayList<>(siembras.values());
	}

}
