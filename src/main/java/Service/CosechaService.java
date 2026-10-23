package Service;

import Model.Cosecha;
import Repository.CosechaRepository;
import Repository.SeccionRepository;
import Repository.SiembraRepository;
import Repository.TrabajadorRepository;

/** Casos de uso del módulo de cosechas. */
public class CosechaService {

	private final CosechaRepository cosechaRepository;
	private final SiembraRepository siembraRepository;
	private final SeccionRepository seccionRepository;
	private final TrabajadorRepository trabajadorRepository;

	public CosechaService(CosechaRepository cosechaRepository, SiembraRepository siembraRepository,
			SeccionRepository seccionRepository, TrabajadorRepository trabajadorRepository) {
		this.cosechaRepository = cosechaRepository;
		this.siembraRepository = siembraRepository;
		this.seccionRepository = seccionRepository;
		this.trabajadorRepository = trabajadorRepository;
	}

	/** Registra una cosecha y libera la sección (RF-13, RN-12 a RN-18). */
	public void registrar(Cosecha cosecha) {
	}

}
