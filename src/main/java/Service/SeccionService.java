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
		validarDatos(seccion);
		seccionRepository.guardar(seccion);
	}

	// Valida los campos obligatorios y la capacidad antes de guardar (RF-01, S-03).
	private void validarDatos(Seccion seccion) {
		if (estaVacio(seccion.getCodigo())) {
			throw new ReglaNegocioException("El código es obligatorio");
		}
		if (estaVacio(seccion.getNombre())) {
			throw new ReglaNegocioException("El nombre es obligatorio");
		}
		if (seccion.getCapacidadMaxima() <= 0) {
			throw new ReglaNegocioException("La capacidad debe ser mayor que cero");
		}
	}

	private boolean estaVacio(String texto) {
		return texto == null || texto.isBlank();
	}

}
