package Model;

import java.time.LocalDate;

/** Aplicación de un producto sobre una siembra (RF-12). */
public class Aplicacion {

	private String codigo;
	private LocalDate fecha;
	private String codigoSiembra;
	private String codigoProducto;
	private double cantidad;
	private String identificacionResponsable;
	private String observaciones;

	public Aplicacion(String codigo, LocalDate fecha, String codigoSiembra, String codigoProducto,
			double cantidad, String identificacionResponsable, String observaciones) {
		this.codigo = codigo;
		this.fecha = fecha;
		this.codigoSiembra = codigoSiembra;
		this.codigoProducto = codigoProducto;
		this.cantidad = cantidad;
		this.identificacionResponsable = identificacionResponsable;
		this.observaciones = observaciones;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getCodigoSiembra() {
		return codigoSiembra;
	}

	public void setCodigoSiembra(String codigoSiembra) {
		this.codigoSiembra = codigoSiembra;
	}

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public String getIdentificacionResponsable() {
		return identificacionResponsable;
	}

	public void setIdentificacionResponsable(String identificacionResponsable) {
		this.identificacionResponsable = identificacionResponsable;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
