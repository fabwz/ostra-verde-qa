package Service;

import java.time.Clock;
import java.time.LocalDate;

import Exception.ReglaNegocioException;
import Model.EstadoSeccion;
import Model.Seccion;
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
		validarCampos(siembra);
		validarCodigoUnico(siembra.getCodigo());
		validarResponsable(siembra.getIdentificacionResponsable());
		Seccion seccion = validarSeccionDisponible(siembra.getCodigoSeccion());
		siembraRepository.guardar(siembra);
		ocupar(seccion);
	}

	/** Consulta una siembra por su código (RF-07). */
	public Siembra consultarPorCodigo(String codigo) {
		return siembraRepository.buscarPorCodigo(codigo)
				.orElseThrow(() -> new ReglaNegocioException("Siembra no encontrada"));
	}

	// Agrupa las validaciones de los campos propios de la siembra (RF-07, RN-04, RN-05).
	private void validarCampos(Siembra siembra) {
		validarCodigo(siembra.getCodigo());
		validarFecha(siembra.getFecha());
		validarSustratos(siembra.getCantidadSustratos());
	}

	// Exige que el código no sea nulo ni esté en blanco (RF-07).
	private void validarCodigo(String codigo) {
		if (codigo == null || codigo.isBlank()) {
			throw new ReglaNegocioException("El código de la siembra es obligatorio");
		}
	}

	// Exige una fecha informada que no sea futura; hoy es válido (RN-04, S-02).
	private void validarFecha(LocalDate fecha) {
		if (fecha == null) {
			throw new ReglaNegocioException("La fecha de la siembra es obligatoria");
		}
		if (fecha.isAfter(LocalDate.now(reloj))) {
			throw new ReglaNegocioException("La fecha de la siembra no puede ser futura");
		}
	}

	// Exige una cantidad de sustratos mayor que cero (RN-05, S-07).
	private void validarSustratos(int cantidadSustratos) {
		if (cantidadSustratos <= 0) {
			throw new ReglaNegocioException("La cantidad de sustratos debe ser mayor que cero");
		}
	}

	// Rechaza la operación si ya existe una siembra con ese código (RN-17).
	private void validarCodigoUnico(String codigo) {
		if (siembraRepository.buscarPorCodigo(codigo).isPresent()) {
			throw new ReglaNegocioException("El código de la siembra ya existe");
		}
	}

	// Exige que el responsable sea un trabajador registrado (RN-16, S-06).
	private void validarResponsable(String identificacion) {
		if (identificacion == null || trabajadorRepository.buscarPorIdentificacion(identificacion).isEmpty()) {
			throw new ReglaNegocioException("El responsable no está registrado");
		}
	}

	// Exige que la sección exista y esté Disponible; la devuelve (RN-03, RN-06).
	private Seccion validarSeccionDisponible(String codigoSeccion) {
		Seccion seccion = seccionRepository.buscarPorCodigo(codigoSeccion)
				.orElseThrow(() -> new ReglaNegocioException("La sección no existe"));
		if (seccion.getEstado() == EstadoSeccion.OCUPADA) {
			throw new ReglaNegocioException("La sección está ocupada");
		}
		if (seccion.getEstado() != EstadoSeccion.DISPONIBLE) {
			throw new ReglaNegocioException("La sección no está disponible");
		}
		return seccion;
	}

	// Cambia la sección a Ocupada una vez guardada la siembra (S-01).
	private void ocupar(Seccion seccion) {
		seccion.setEstado(EstadoSeccion.OCUPADA);
		seccionRepository.guardar(seccion);
	}

}
