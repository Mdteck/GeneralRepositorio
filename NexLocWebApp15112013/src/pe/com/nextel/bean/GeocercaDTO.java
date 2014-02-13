package pe.com.nextel.bean;

/**
 * @author Devos Inc.
 */

import java.util.List;

public class GeocercaDTO {

	private String idGeocerca;
	private UsuarioDTO usuario;
	private String nombre;
	private String rings;
	private List<UsuarioDTO> usuarios;
	private String emailNotificacion;
	private String asignados;
	private String idUsuario;
	private String observacion;
	private HorarioDTO horario;
	private String fechaRegistro;
	private int estado;
	
		
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getAsignados() {
		return asignados;
	}
	public void setAsignados(String asignados) {
		this.asignados = asignados;
	}
	public String getEmailNotificacion() {
		return emailNotificacion;
	}
	public void setEmailNotificacion(String emailNotificacion) {
		this.emailNotificacion = emailNotificacion;
	}
	public String getIdGeocerca() {
		return idGeocerca;
	}
	public void setIdGeocerca(String idGeocerca) {
		this.idGeocerca = idGeocerca;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<UsuarioDTO> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<UsuarioDTO> usuarios) {
		this.usuarios = usuarios;
	}
	public String getRings() {
		return rings;
	}
	public void setRings(String rings) {
		this.rings = rings;
	}
	public HorarioDTO getHorario() {
		return horario;
	}
	public void setHorario(HorarioDTO horario) {
		this.horario = horario;
	}
	
	
	
}
