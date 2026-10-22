package Service;

import Model.Trabajador;
import Repository.TrabajadorRepository;

/** Reglas de negocio del módulo de personal. */
public class TrabajadorService {

	private final TrabajadorRepository trabajadorRepository;

	public TrabajadorService(TrabajadorRepository trabajadorRepository) {
		this.trabajadorRepository = trabajadorRepository;
	}

	/** Registra un trabajador nuevo (RF-04, RN-02). */
	public void registrar(Trabajador trabajador) {
		trabajadorRepository.guardar(trabajador);
	}

}
