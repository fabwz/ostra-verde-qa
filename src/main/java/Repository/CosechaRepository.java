package Repository;

import java.util.List;
import java.util.Optional;

import Model.Cosecha;

/** Almacenamiento de cosechas. */
public interface CosechaRepository {

	void guardar(Cosecha cosecha);

	Optional<Cosecha> buscarPorCodigo(String codigo);

	Optional<Cosecha> buscarPorSiembra(String codigoSiembra);

	List<Cosecha> listarTodas();

}
