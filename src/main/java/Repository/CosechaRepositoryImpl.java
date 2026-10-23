package Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import Model.Cosecha;

/** Implementación en memoria de CosechaRepository (S-16). */
public class CosechaRepositoryImpl implements CosechaRepository {

	private final Map<String, Cosecha> cosechas = new LinkedHashMap<>();

	/** Guarda la cosecha indexada por su código (RF-13). */
	@Override
	public void guardar(Cosecha cosecha) {
		cosechas.put(cosecha.getCodigo(), cosecha);
	}

	/** Busca una cosecha por su código (RF-13). */
	@Override
	public Optional<Cosecha> buscarPorCodigo(String codigo) {
		return Optional.ofNullable(cosechas.get(codigo));
	}

	/** Busca la cosecha de una siembra, si ya fue cosechada (RN-12). */
	@Override
	public Optional<Cosecha> buscarPorSiembra(String codigoSiembra) {
		return cosechas.values().stream()
				.filter(cosecha -> cosecha.getCodigoSiembra().equals(codigoSiembra))
				.findFirst();
	}

	/** Devuelve una copia con todas las cosechas guardadas (RF-14). */
	@Override
	public List<Cosecha> listarTodas() {
		return new ArrayList<>(cosechas.values());
	}

}
