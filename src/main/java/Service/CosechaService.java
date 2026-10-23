package Service;

import Exception.ReglaNegocioException;
import Model.Cosecha;
import Model.EstadoSeccion;
import Model.Seccion;
import Model.Siembra;
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
		validarCampos(cosecha);
		validarCodigoUnico(cosecha.getCodigo());
		Siembra siembra = validarSiembra(cosecha.getCodigoSiembra());
		validarFechaPosterior(cosecha, siembra);
		validarResponsable(cosecha.getIdentificacionResponsable());
		cosechaRepository.guardar(cosecha);
		liberarSeccion(siembra.getCodigoSeccion());
	}

	// Agrupa las validaciones de los campos propios de la cosecha (RF-13, RN-13).
	private void validarCampos(Cosecha cosecha) {
		if (cosecha.getCodigo() == null || cosecha.getCodigo().isBlank()) {
			throw new ReglaNegocioException("El código de la cosecha es obligatorio");
		}
		if (cosecha.getFecha() == null) {
			throw new ReglaNegocioException("La fecha de la cosecha es obligatoria");
		}
		if (cosecha.getPesoKg() <= 0) {
			throw new ReglaNegocioException("El peso cosechado debe ser mayor que cero");
		}
		if (cosecha.getCantidadBandejas() <= 0) {
			throw new ReglaNegocioException("La cantidad de bandejas debe ser mayor que cero");
		}
	}

	// Rechaza la operación si ya existe una cosecha con ese código (RN-17).
	private void validarCodigoUnico(String codigo) {
		if (cosechaRepository.buscarPorCodigo(codigo).isPresent()) {
			throw new ReglaNegocioException("El código de la cosecha ya existe");
		}
	}

	// Exige que la siembra exista y no haya sido cosechada; la devuelve (RN-12, RN-18).
	private Siembra validarSiembra(String codigoSiembra) {
		Siembra siembra = siembraRepository.buscarPorCodigo(codigoSiembra)
				.orElseThrow(() -> new ReglaNegocioException("La siembra no existe"));
		if (cosechaRepository.buscarPorSiembra(codigoSiembra).isPresent()) {
			throw new ReglaNegocioException("La siembra ya fue cosechada");
		}
		return siembra;
	}

	// Exige que la fecha de cosecha sea estrictamente posterior a la de siembra (RN-14, S-10).
	private void validarFechaPosterior(Cosecha cosecha, Siembra siembra) {
		if (!cosecha.getFecha().isAfter(siembra.getFecha())) {
			throw new ReglaNegocioException("La fecha de la cosecha debe ser posterior a la fecha de siembra");
		}
	}

	// Exige que el responsable sea un trabajador registrado (RN-16, S-06).
	private void validarResponsable(String identificacion) {
		if (identificacion == null || trabajadorRepository.buscarPorIdentificacion(identificacion).isEmpty()) {
			throw new ReglaNegocioException("El responsable no está registrado");
		}
	}

	// Devuelve la sección de la siembra al estado Disponible (RN-15).
	private void liberarSeccion(String codigoSeccion) {
		Seccion seccion = seccionRepository.buscarPorCodigo(codigoSeccion)
				.orElseThrow(() -> new ReglaNegocioException("La sección no existe"));
		seccion.setEstado(EstadoSeccion.DISPONIBLE);
		seccionRepository.guardar(seccion);
	}

}
