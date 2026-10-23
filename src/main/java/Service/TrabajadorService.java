package Service;

import java.util.List;

import Exception.ReglaNegocioException;
import Model.Trabajador;
import Repository.TrabajadorRepository;

/** Reglas de negocio del módulo de personal. */
public class TrabajadorService {

	private static final int LONGITUD_TELEFONO = 8;

	private final TrabajadorRepository trabajadorRepository;

	public TrabajadorService(TrabajadorRepository trabajadorRepository) {
		this.trabajadorRepository = trabajadorRepository;
	}

	/** Registra un trabajador nuevo (RF-04, RN-02). */
	public void registrar(Trabajador trabajador) {
		validar(trabajador);
		trabajadorRepository.guardar(trabajador);
	}

	/** Consulta un trabajador por su identificación (RF-05). */
	public Trabajador consultarPorIdentificacion(String identificacion) {
		return trabajadorRepository.buscarPorIdentificacion(identificacion)
				.orElseThrow(() -> new ReglaNegocioException("El trabajador no fue encontrado"));
	}

	/** Actualiza los datos de un trabajador existente (RF-06, RN-02). */
	public void actualizar(String identificacion, Trabajador datosNuevos) {
		validarTrabajadorExiste(identificacion);
		if (cambiaDeIdentificacion(identificacion, datosNuevos)) {
			validarIdentificacionUnica(datosNuevos.getIdentificacion());
		}
		trabajadorRepository.guardar(datosNuevos);
	}

	/** Consulta el listado de trabajadores (RF-05). */
	public List<Trabajador> consultarTodos() {
		return trabajadorRepository.listarTodos();
	}

	// Rechaza la operación si no existe un trabajador con esa identificación (RF-06).
	private void validarTrabajadorExiste(String identificacion) {
		if (trabajadorRepository.buscarPorIdentificacion(identificacion).isEmpty()) {
			throw new ReglaNegocioException("El trabajador no existe");
		}
	}

	// Indica si los datos nuevos traen una identificación distinta a la del trabajador actualizado.
	private boolean cambiaDeIdentificacion(String identificacion, Trabajador datosNuevos) {
		return !identificacion.equals(datosNuevos.getIdentificacion());
	}

	// Rechaza la operación si ya existe un trabajador con esa identificación (RN-02).
	private void validarIdentificacionUnica(String identificacion) {
		if (trabajadorRepository.buscarPorIdentificacion(identificacion).isPresent()) {
			throw new ReglaNegocioException("La identificación ya está registrada");
		}
	}

	// Valida los campos del trabajador antes de guardarlo (RF-04, RNF-02).
	private void validar(Trabajador trabajador) {
		validarTelefono(trabajador.getTelefono());
		validarCorreo(trabajador.getCorreo());
		validarCargo(trabajador.getCargo());
		validarIdentificacionUnica(trabajador.getIdentificacion());
	}

	// Exige un teléfono numérico de exactamente 8 dígitos (S-04).
	private void validarTelefono(String telefono) {
		if (telefono != null && !telefono.chars().allMatch(c -> c >= '0' && c <= '9')) {
			throw new ReglaNegocioException("El teléfono debe ser numérico");
		}
		if (telefono == null || telefono.length() != LONGITUD_TELEFONO) {
			throw new ReglaNegocioException("El teléfono debe tener 8 dígitos");
		}
	}

	// Exige un correo con formato usuario@dominio (S-04).
	private void validarCorreo(String correo) {
		if (correo == null || !tieneFormatoDeCorreo(correo)) {
			throw new ReglaNegocioException("El correo no tiene formato válido");
		}
	}

	// Verifica que haya texto, una sola arroba y texto después de ella.
	private boolean tieneFormatoDeCorreo(String correo) {
		int arroba = correo.indexOf('@');
		return arroba > 0 && arroba == correo.lastIndexOf('@') && arroba < correo.length() - 1;
	}

	// Exige que el cargo no sea nulo ni esté en blanco (RF-04).
	private void validarCargo(String cargo) {
		if (cargo == null || cargo.isBlank()) {
			throw new ReglaNegocioException("El cargo es obligatorio");
		}
	}

}
