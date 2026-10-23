package Service;

import Exception.ReglaNegocioException;
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
		validarCampos(aplicacion);
		validarCodigoUnico(aplicacion.getCodigo());
		validarSiembra(aplicacion.getCodigoSiembra());
		validarProducto(aplicacion.getCodigoProducto());
		Validaciones.exigirResponsableRegistrado(trabajadorRepository, aplicacion.getIdentificacionResponsable());
		aplicacionRepository.guardar(aplicacion);
	}

	// Agrupa las validaciones de los campos propios de la aplicación (RF-12, RN-10).
	private void validarCampos(Aplicacion aplicacion) {
		if (Validaciones.estaVacio(aplicacion.getCodigo())) {
			throw new ReglaNegocioException("El código de la aplicación es obligatorio");
		}
		if (aplicacion.getFecha() == null) {
			throw new ReglaNegocioException("La fecha de la aplicación es obligatoria");
		}
		if (aplicacion.getCantidad() <= 0) {
			throw new ReglaNegocioException("La cantidad aplicada debe ser mayor que cero");
		}
	}

	// Rechaza la operación si ya existe una aplicación con ese código (RN-17).
	private void validarCodigoUnico(String codigo) {
		if (aplicacionRepository.buscarPorCodigo(codigo).isPresent()) {
			throw new ReglaNegocioException("El código de la aplicación ya existe");
		}
	}

	// Exige que la siembra exista y no haya sido cosechada (RN-08, RN-11).
	private void validarSiembra(String codigoSiembra) {
		if (codigoSiembra == null || siembraRepository.buscarPorCodigo(codigoSiembra).isEmpty()) {
			throw new ReglaNegocioException("La siembra no existe");
		}
		if (cosechaRepository.buscarPorSiembra(codigoSiembra).isPresent()) {
			throw new ReglaNegocioException("La siembra ya fue cosechada");
		}
	}

	// Exige que el producto aplicado exista (RN-09).
	private void validarProducto(String codigoProducto) {
		if (codigoProducto == null || productoRepository.buscarPorCodigo(codigoProducto).isEmpty()) {
			throw new ReglaNegocioException("El producto no existe");
		}
	}

}
