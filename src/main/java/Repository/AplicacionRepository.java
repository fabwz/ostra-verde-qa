package Repository;

import java.util.List;
import java.util.Optional;

import Model.Aplicacion;

/** Almacenamiento de aplicaciones de productos. */
public interface AplicacionRepository {

	void guardar(Aplicacion aplicacion);

	Optional<Aplicacion> buscarPorCodigo(String codigo);

	List<Aplicacion> listarTodas();

	boolean existeConProducto(String codigoProducto);

}
