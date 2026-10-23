package Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

import Exception.ReglaNegocioException;
import Model.Aplicacion;
import Model.Cosecha;
import Model.Seccion;
import Model.Siembra;
import Model.Trabajador;
import Repository.AplicacionRepository;
import Repository.CosechaRepository;
import Repository.SeccionRepository;
import Repository.SiembraRepository;
import Repository.TrabajadorRepository;

/** Reportes gerenciales de producción, aplicaciones y rendimiento. */
public class ReporteService {

	private final CosechaRepository cosechaRepository;
	private final SiembraRepository siembraRepository;
	private final SeccionRepository seccionRepository;
	private final TrabajadorRepository trabajadorRepository;
	private final AplicacionRepository aplicacionRepository;

	public ReporteService(CosechaRepository cosechaRepository, SiembraRepository siembraRepository,
			SeccionRepository seccionRepository, TrabajadorRepository trabajadorRepository,
			AplicacionRepository aplicacionRepository) {
		this.cosechaRepository = cosechaRepository;
		this.siembraRepository = siembraRepository;
		this.seccionRepository = seccionRepository;
		this.trabajadorRepository = trabajadorRepository;
		this.aplicacionRepository = aplicacionRepository;
	}

	/** Suma los kg cosechados entre dos fechas, ambas incluidas (RF-14, S-12). */
	public double produccionTotalPorPeriodo(LocalDate inicio, LocalDate fin) {
		if (inicio.isAfter(fin)) {
			throw new ReglaNegocioException("La fecha de inicio no puede ser posterior a la fecha de fin");
		}
		return cosechaRepository.listarTodas().stream()
				.filter(cosecha -> !cosecha.getFecha().isBefore(inicio) && !cosecha.getFecha().isAfter(fin))
				.mapToDouble(Cosecha::getPesoKg)
				.sum();
	}

	/** Devuelve los kg cosechados por cada sección registrada (RF-15). */
	public Map<String, Double> produccionPorSeccion() {
		Map<String, Double> reporte = new TreeMap<>();
		for (Seccion seccion : seccionRepository.listarTodas()) {
			reporte.put(seccion.getCodigo(), produccionDeSeccion(seccion.getCodigo()));
		}
		return reporte;
	}

	/** Devuelve los kg cosechados en una sección (RF-15). */
	public double produccionDeSeccion(String codigoSeccion) {
		if (seccionRepository.buscarPorCodigo(codigoSeccion).isEmpty()) {
			throw new ReglaNegocioException("La sección no existe");
		}
		return sumarPeso(cosecha -> seccionDeCosecha(cosecha).equals(codigoSeccion));
	}

	/** Devuelve los kg cosechados por cada trabajador registrado (RF-16). */
	public Map<String, Double> produccionPorTrabajador() {
		Map<String, Double> reporte = new TreeMap<>();
		for (Trabajador trabajador : trabajadorRepository.listarTodos()) {
			reporte.put(trabajador.getIdentificacion(), produccionDeTrabajador(trabajador.getIdentificacion()));
		}
		return reporte;
	}

	/** Devuelve los kg cosechados por un trabajador (RF-16). */
	public double produccionDeTrabajador(String identificacion) {
		if (trabajadorRepository.buscarPorIdentificacion(identificacion).isEmpty()) {
			throw new ReglaNegocioException("El trabajador no existe");
		}
		return sumarPeso(cosecha -> cosecha.getIdentificacionResponsable().equals(identificacion));
	}

	/** Devuelve el historial completo de aplicaciones (RF-17). */
	public List<Aplicacion> historialAplicaciones() {
		return aplicacionRepository.listarTodas();
	}

	/** Devuelve el historial de aplicaciones de una siembra (RF-17). */
	public List<Aplicacion> historialAplicacionesDeSiembra(String codigoSiembra) {
		validarSiembraExiste(codigoSiembra);
		return aplicacionRepository.listarTodas().stream()
				.filter(aplicacion -> aplicacion.getCodigoSiembra().equals(codigoSiembra))
				.toList();
	}

	/** Calcula el rendimiento de una siembra en kg por sustrato (RF-18, S-11). */
	public double rendimiento(String codigoSiembra) {
		Siembra siembra = validarSiembraExiste(codigoSiembra);
		Cosecha cosecha = cosechaRepository.buscarPorSiembra(codigoSiembra)
				.orElseThrow(() -> new ReglaNegocioException(
						"Rendimiento no disponible: la siembra no tiene cosecha"));
		return cosecha.getPesoKg() / siembra.getCantidadSustratos();
	}

	// Suma el peso de las cosechas que cumplen el filtro dado.
	private double sumarPeso(Predicate<Cosecha> filtro) {
		return cosechaRepository.listarTodas().stream()
				.filter(filtro)
				.mapToDouble(Cosecha::getPesoKg)
				.sum();
	}

	// Obtiene el código de la sección donde se sembró lo cosechado (RF-15).
	private String seccionDeCosecha(Cosecha cosecha) {
		return siembraRepository.buscarPorCodigo(cosecha.getCodigoSiembra())
				.map(Siembra::getCodigoSeccion)
				.orElse("");
	}

	// Exige que la siembra exista y la devuelve (RF-17, RF-18).
	private Siembra validarSiembraExiste(String codigoSiembra) {
		return siembraRepository.buscarPorCodigo(codigoSiembra)
				.orElseThrow(() -> new ReglaNegocioException("La siembra no existe"));
	}

}
