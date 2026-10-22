package Model;

import Exception.ReglaNegocioException;

/** Estados permitidos para una sección de producción (RES-01). */
public enum EstadoSeccion {

	DISPONIBLE,
	OCUPADA,
	EN_MANTENIMIENTO;

	/** Convierte un texto en un estado de sección válido (RES-01). */
	public static EstadoSeccion desdeTexto(String texto) {
		if ("Disponible".equals(texto)) {
			return DISPONIBLE;
		}
		if ("Ocupada".equals(texto)) {
			return OCUPADA;
		}
		if ("En mantenimiento".equals(texto)) {
			return EN_MANTENIMIENTO;
		}
		throw new ReglaNegocioException(
				"El estado no es válido; debe ser Disponible, Ocupada o En mantenimiento");
	}

}
