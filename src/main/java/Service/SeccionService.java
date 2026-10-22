package Service;

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
		seccionRepository.guardar(seccion);
	}

}
