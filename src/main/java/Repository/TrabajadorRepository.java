package Repository;

import java.util.List;
import java.util.Optional;

import Model.Trabajador;

/** Almacenamiento de trabajadores. */
public interface TrabajadorRepository {

	void guardar(Trabajador trabajador);

	Optional<Trabajador> buscarPorIdentificacion(String identificacion);

	List<Trabajador> listarTodos();

}
