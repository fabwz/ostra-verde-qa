package Service;

import Exception.ReglaNegocioException;
import Repository.TrabajadorRepository;

/** Validaciones comunes a los servicios de siembras, aplicaciones y cosechas. */
final class Validaciones {

	private Validaciones() {
	}

	/** Indica si un texto es nulo o está en blanco (S-08). */
	static boolean estaVacio(String texto) {
		return texto == null || texto.isBlank();
	}

	/** Exige que el responsable sea un trabajador registrado (RN-16, S-06). */
	static void exigirResponsableRegistrado(TrabajadorRepository trabajadorRepository, String identificacion) {
		if (identificacion == null || trabajadorRepository.buscarPorIdentificacion(identificacion).isEmpty()) {
			throw new ReglaNegocioException("El responsable no está registrado");
		}
	}

}
