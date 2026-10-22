package Repository;

import java.util.List;
import java.util.Optional;

import Model.Siembra;

/** Almacenamiento de siembras. */
public interface SiembraRepository {

	void guardar(Siembra siembra);

	Optional<Siembra> buscarPorCodigo(String codigo);

	List<Siembra> listarTodas();

}
