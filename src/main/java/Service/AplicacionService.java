package Service;

import Model.Aplicacion;
import Repository.AplicacionRepository;
import Repository.CosechaRepository;
import Repository.ProductoRepository;
import Repository.SiembraRepository;
import Repository.TrabajadorRepository;

/** Casos de uso del módulo de aplicaciones de productos. */
public class AplicacionService {

	private final AplicacionRepository aplicacionRepository;
	private final SiembraRepository siembraRepository;
	private final ProductoRepository productoRepository;
	private final TrabajadorRepository trabajadorRepository;
	private final CosechaRepository cosechaRepository;

	public AplicacionService(AplicacionRepository aplicacionRepository, SiembraRepository siembraRepository,
			ProductoRepository productoRepository, TrabajadorRepository trabajadorRepository,
			CosechaRepository cosechaRepository) {
		this.aplicacionRepository = aplicacionRepository;
		this.siembraRepository = siembraRepository;
		this.productoRepository = productoRepository;
		this.trabajadorRepository = trabajadorRepository;
		this.cosechaRepository = cosechaRepository;
	}

	/** Registra una aplicación nueva (RF-12, RN-08 a RN-11, RN-16, RN-17). */
	public void registrar(Aplicacion aplicacion) {
	}

}
