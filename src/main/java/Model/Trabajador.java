package Model;

/** Trabajador involucrado en el proceso productivo (RF-04). */
public class Trabajador {

	private String identificacion;
	private String nombreCompleto;
	private String telefono;
	private String correo;
	private String cargo;

	public Trabajador(String identificacion, String nombreCompleto, String telefono, String correo, String cargo) {
		this.identificacion = identificacion;
		this.nombreCompleto = nombreCompleto;
		this.telefono = telefono;
		this.correo = correo;
		this.cargo = cargo;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

}
