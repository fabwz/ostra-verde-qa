package Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Model.Aplicacion;
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
		return 0;
	}

	/** Devuelve los kg cosechados por cada sección registrada (RF-15). */
	public Map<String, Double> produccionPorSeccion() {
		return new TreeMap<>();
	}

	/** Devuelve los kg cosechados en una sección (RF-15). */
	public double produccionDeSeccion(String codigoSeccion) {
		return 0;
	}

	/** Devuelve los kg cosechados por cada trabajador registrado (RF-16). */
	public Map<String, Double> produccionPorTrabajador() {
		return new TreeMap<>();
	}

	/** Devuelve los kg cosechados por un trabajador (RF-16). */
	public double produccionDeTrabajador(String identificacion) {
		return 0;
	}

	/** Devuelve el historial completo de aplicaciones (RF-17). */
	public List<Aplicacion> historialAplicaciones() {
		return List.of();
	}

	/** Devuelve el historial de aplicaciones de una siembra (RF-17). */
	public List<Aplicacion> historialAplicacionesDeSiembra(String codigoSiembra) {
		return List.of();
	}

	/** Calcula el rendimiento de una siembra en kg por sustrato (RF-18, S-11). */
	public double rendimiento(String codigoSiembra) {
		return 0;
	}

}
