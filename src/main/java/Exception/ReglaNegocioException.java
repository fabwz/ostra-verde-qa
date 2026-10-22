package Exception;

/** Excepción lanzada cuando una operación incumple una regla de negocio (RNF-03). */
public class ReglaNegocioException extends RuntimeException {

	public ReglaNegocioException(String mensaje) {
		super(mensaje);
	}

}
