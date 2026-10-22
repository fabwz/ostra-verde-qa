package Model;

/** Sección de producción de la finca (RF-01). */
public class Seccion {

	private String codigo;
	private String nombre;
	private int capacidadMaxima;
	private EstadoSeccion estado;

	public Seccion(String codigo, String nombre, int capacidadMaxima, EstadoSeccion estado) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.capacidadMaxima = capacidadMaxima;
		this.estado = estado;
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

	public int getCapacidadMaxima() {
		return capacidadMaxima;
	}

	public void setCapacidadMaxima(int capacidadMaxima) {
		this.capacidadMaxima = capacidadMaxima;
	}

	public EstadoSeccion getEstado() {
		return estado;
	}

	public void setEstado(EstadoSeccion estado) {
		this.estado = estado;
	}

}
