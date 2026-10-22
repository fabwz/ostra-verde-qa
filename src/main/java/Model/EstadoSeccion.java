package Model;

/** Estados permitidos para una sección de producción (RES-01). */
public enum EstadoSeccion {

	DISPONIBLE,
	OCUPADA,
	EN_MANTENIMIENTO;

	/** Convierte un texto en un estado de sección válido (RES-01). */
	public static EstadoSeccion desdeTexto(String texto) {
		return null;
	}

}
