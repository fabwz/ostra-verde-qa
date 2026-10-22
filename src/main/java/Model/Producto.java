package Model;

/** Producto agrícola usado en el cultivo (RF-08). */
public class Producto {

	private String codigo;
	private String nombre;
	private String tipo;
	private String descripcion;
	private String unidadMedida;

	public Producto(String codigo, String nombre, String tipo, String descripcion, String unidadMedida) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.unidadMedida = unidadMedida;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

}
