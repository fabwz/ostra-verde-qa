package Service;

import java.time.Clock;

import Model.Siembra;
import Repository.SeccionRepository;
import Repository.SiembraRepository;
import Repository.TrabajadorRepository;

/** Casos de uso del módulo de siembras. */
public class SiembraService {

	private final SiembraRepository siembraRepository;
	private final SeccionRepository seccionRepository;
	private final TrabajadorRepository trabajadorRepository;
	private final Clock reloj;

	public SiembraService(SiembraRepository siembraRepository, SeccionRepository seccionRepository,
			TrabajadorRepository trabajadorRepository, Clock reloj) {
		this.siembraRepository = siembraRepository;
		this.seccionRepository = seccionRepository;
		this.trabajadorRepository = trabajadorRepository;
		this.reloj = reloj;
	}

	/** Registra una siembra nueva y ocupa su sección (RF-07, RN-03 a RN-06, RN-16, RN-17, S-01). */
	public void registrar(Siembra siembra) {
	}

	/** Consulta una siembra por su código (RF-07). */
	public Siembra consultarPorCodigo(String codigo) {
		return null;
	}

}
