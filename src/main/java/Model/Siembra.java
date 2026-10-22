package Model;

import java.time.LocalDate;

/** Siembra registrada en una sección (RF-07). */
public class Siembra {

	private String codigo;
	private LocalDate fecha;
	private String codigoSeccion;
	private int cantidadSustratos;
	private String identificacionResponsable;
	private String observaciones;

	public Siembra(String codigo, LocalDate fecha, String codigoSeccion,
			int cantidadSustratos, String identificacionResponsable, String observaciones) {
		this.codigo = codigo;
		this.fecha = fecha;
		this.codigoSeccion = codigoSeccion;
		this.cantidadSustratos = cantidadSustratos;
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

	public String getCodigoSeccion() {
		return codigoSeccion;
	}

	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	public int getCantidadSustratos() {
		return cantidadSustratos;
	}

	public void setCantidadSustratos(int cantidadSustratos) {
		this.cantidadSustratos = cantidadSustratos;
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
