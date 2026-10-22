package Service;

import Exception.ReglaNegocioException;
import Model.Seccion;
import Repository.SeccionRepository;

/** Casos de uso del módulo de secciones de producción. */
public class SeccionService {

	private final SeccionRepository seccionRepository;

	public SeccionService(SeccionRepository seccionRepository) {
		this.seccionRepository = seccionRepository;
	}

	/** Registra una sección nueva (RF-01, RN-01). */
	public void registrar(Seccion seccion) {
		validar(seccion);
		seccionRepository.guardar(seccion);
	}

	// Agrupa las validaciones previas al registro (RF-01, RN-01).
	private void validar(Seccion seccion) {
		validarCodigo(seccion.getCodigo());
		validarNombre(seccion.getNombre());
		validarCapacidad(seccion.getCapacidadMaxima());
		validarCodigoUnico(seccion.getCodigo());
	}

	// Rechaza el registro si ya existe una sección con ese código (RN-01).
	private void validarCodigoUnico(String codigo) {
		if (seccionRepository.buscarPorCodigo(codigo).isPresent()) {
			throw new ReglaNegocioException("El código de la sección ya existe");
		}
	}

	// Exige que el código no sea nulo ni esté en blanco (RF-01).
	private void validarCodigo(String codigo) {
		if (estaVacio(codigo)) {
			throw new ReglaNegocioException("El código es obligatorio");
		}
	}

	// Exige que el nombre no sea nulo ni esté en blanco (RF-01).
	private void validarNombre(String nombre) {
		if (estaVacio(nombre)) {
			throw new ReglaNegocioException("El nombre es obligatorio");
		}
	}

	// Exige una capacidad mayor que cero (RF-01, S-03).
	private void validarCapacidad(int capacidadMaxima) {
		if (capacidadMaxima <= 0) {
			throw new ReglaNegocioException("La capacidad debe ser mayor que cero");
		}
	}

	private boolean estaVacio(String texto) {
		return texto == null || texto.isBlank();
	}

}
