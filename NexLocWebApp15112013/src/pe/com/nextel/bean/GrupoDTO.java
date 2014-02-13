package pe.com.nextel.bean;

import java.util.List;

/**
 * @author Devos Inc.
 */

public class GrupoDTO {
	
	private String idGrupo;
	private String nombre;
	private String idMonitor;
	private List<UsuarioDTO> usuarios;
	private String asignados;
	
	
	public String getAsignados() {
		return asignados;
	}
	public void setAsignados(String asignados) {
		this.asignados = asignados;
	}
	public String getIdGrupo() {
		return idGrupo;
	}
	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getIdMonitor() {
		return idMonitor;
	}
	public void setIdMonitor(String idMonitor) {
		this.idMonitor = idMonitor;
	}
	public List<UsuarioDTO> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<UsuarioDTO> usuarios) {
		this.usuarios = usuarios;
	}
	
	

}
