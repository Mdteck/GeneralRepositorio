package pe.com.nextel.bean;

/**
 * @author Devos Inc.
 */

public class CategoriaDTO {
	
	private String idCategoria;
	private String categoria;
	private int cantidadPuntoInteres;
	
	
	
	public int getCantidadPuntoInteres() {
		return cantidadPuntoInteres;
	}
	public void setCantidadPuntoInteres(int cantidadPuntoInteres) {
		this.cantidadPuntoInteres = cantidadPuntoInteres;
	}
	public String getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	

}
