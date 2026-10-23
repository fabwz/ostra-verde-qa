package Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import Model.Aplicacion;

/** Implementación en memoria de AplicacionRepository (S-16). */
public class AplicacionRepositoryImpl implements AplicacionRepository {

	private final Map<String, Aplicacion> aplicaciones = new LinkedHashMap<>();

	/** Guarda la aplicación indexada por su código (RF-12). */
	@Override
	public void guardar(Aplicacion aplicacion) {
		aplicaciones.put(aplicacion.getCodigo(), aplicacion);
	}

	/** Busca una aplicación por su código (RF-12). */
	@Override
	public Optional<Aplicacion> buscarPorCodigo(String codigo) {
		return Optional.ofNullable(aplicaciones.get(codigo));
	}

	/** Devuelve una copia con todas las aplicaciones guardadas (RF-17). */
	@Override
	public List<Aplicacion> listarTodas() {
		return new ArrayList<>(aplicaciones.values());
	}

	/** Indica si el producto tiene al menos una aplicación registrada (RN-07, S-13). */
	@Override
	public boolean existeConProducto(String codigoProducto) {
		return aplicaciones.values().stream()
				.anyMatch(aplicacion -> aplicacion.getCodigoProducto().equals(codigoProducto));
	}

}
