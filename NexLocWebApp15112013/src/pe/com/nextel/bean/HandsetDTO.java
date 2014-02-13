package pe.com.nextel.bean;

/**
 * @author Devos Inc.
 */

public class HandsetDTO {
	
	public static final int IMAGE_SUPPORT = 1;
	public static final int IMAGE_NO_SUPPORT = 0;
	
	private String idHandset;
	private String modelo;
	private String plataforma;
	private String versionAplicacion;
	private String estado;
	private String imagen;
	
	
	
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public String getIdHandset() {
		return idHandset;
	}
	public void setIdHandset(String idHandset) {
		this.idHandset = idHandset;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getPlataforma() {
		return plataforma;
	}
	public void setPlataforma(String plataforma) {
		this.plataforma = plataforma;
	}
	public String getVersionAplicacion() {
		return versionAplicacion;
	}
	public void setVersionAplicacion(String versionAplicacion) {
		this.versionAplicacion = versionAplicacion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	
	

}
