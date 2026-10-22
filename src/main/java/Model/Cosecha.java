package Model;

import java.time.LocalDate;

/** Cosecha de una siembra (RF-13). */
public class Cosecha {

	private String codigo;
	private LocalDate fecha;
	private String codigoSiembra;
	private double pesoKg;
	private int cantidadBandejas;
	private String identificacionResponsable;

	public Cosecha(String codigo, LocalDate fecha, String codigoSiembra,
			double pesoKg, int cantidadBandejas, String identificacionResponsable) {
		this.codigo = codigo;
		this.fecha = fecha;
		this.codigoSiembra = codigoSiembra;
		this.pesoKg = pesoKg;
		this.cantidadBandejas = cantidadBandejas;
		this.identificacionResponsable = identificacionResponsable;
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

	public double getPesoKg() {
		return pesoKg;
	}

	public void setPesoKg(double pesoKg) {
		this.pesoKg = pesoKg;
	}

	public int getCantidadBandejas() {
		return cantidadBandejas;
	}

	public void setCantidadBandejas(int cantidadBandejas) {
		this.cantidadBandejas = cantidadBandejas;
	}

	public String getIdentificacionResponsable() {
		return identificacionResponsable;
	}

	public void setIdentificacionResponsable(String identificacionResponsable) {
		this.identificacionResponsable = identificacionResponsable;
	}

}
