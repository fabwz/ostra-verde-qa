package Service;

import java.util.ArrayList;
import java.util.List;

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
		validarCampos(seccion);
		validarCodigoUnico(seccion.getCodigo());
		seccionRepository.guardar(seccion);
	}

	/** Modifica los datos de una sección existente (RF-02, RN-01). */
	public void modificar(String codigo, Seccion datosNuevos) {
		validarSeccionExiste(codigo);
		validarCampos(datosNuevos);
		if (cambiaDeCodigo(codigo, datosNuevos)) {
			validarCodigoUnico(datosNuevos.getCodigo());
		}
		seccionRepository.guardar(datosNuevos);
	}

	/** Consulta las secciones disponibles para nuevas siembras (RF-03, RES-01). */
	public List<Seccion> consultarDisponibles() {
		return new ArrayList<>();
	}

	// Indica si los datos nuevos traen un código distinto al de la sección modificada.
	private boolean cambiaDeCodigo(String codigo, Seccion datosNuevos) {
		return !codigo.equals(datosNuevos.getCodigo());
	}

	// Rechaza la operación si no existe una sección con ese código (RF-02).
	private void validarSeccionExiste(String codigo) {
		if (seccionRepository.buscarPorCodigo(codigo).isEmpty()) {
			throw new ReglaNegocioException("La sección no existe");
		}
	}

	// Valida los campos comunes al registro y a la modificación (RF-01, RF-02).
	private void validarCampos(Seccion seccion) {
		validarCodigo(seccion.getCodigo());
		validarNombre(seccion.getNombre());
		validarCapacidad(seccion.getCapacidadMaxima());
	}

	// Rechaza la operación si ya existe una sección con ese código (RN-01).
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
