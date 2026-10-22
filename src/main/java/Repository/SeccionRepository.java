package Repository;

import java.util.List;
import java.util.Optional;

import Model.Seccion;

/** Almacenamiento de secciones de producción. */
public interface SeccionRepository {

	void guardar(Seccion seccion);

	Optional<Seccion> buscarPorCodigo(String codigo);

	List<Seccion> listarTodas();

}
