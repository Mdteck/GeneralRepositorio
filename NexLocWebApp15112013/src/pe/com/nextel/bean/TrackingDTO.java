package pe.com.nextel.bean;

import java.util.List;

public class TrackingDTO {
	public static final int CANCELADO = 0;
	public static final int EJECUCION = 1;
	public static final int TERMINADO = 2;
	
	private String idTracking;
	private UsuarioDTO monitor;
	private UsuarioDTO monitoreado;
	private int estado;
	private HorarioDTO horario;
	private List<PosicionDTO> posiciones;
	
	
	public List<PosicionDTO> getPosiciones() {
		return posiciones;
	}
	public void setPosiciones(List<PosicionDTO> posiciones) {
		this.posiciones = posiciones;
	}
	public String getIdTracking() {
		return idTracking;
	}
	public void setIdTracking(String idTracking) {
		this.idTracking = idTracking;
	}
	public UsuarioDTO getMonitor() {
		return monitor;
	}
	public void setMonitor(UsuarioDTO monitor) {
		this.monitor = monitor;
	}
	public UsuarioDTO getMonitoreado() {
		return monitoreado;
	}
	public void setMonitoreado(UsuarioDTO monitoreado) {
		this.monitoreado = monitoreado;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public HorarioDTO getHorario() {
		return horario;
	}
	public void setHorario(HorarioDTO horario) {
		this.horario = horario;
	}
	
	
	

}
