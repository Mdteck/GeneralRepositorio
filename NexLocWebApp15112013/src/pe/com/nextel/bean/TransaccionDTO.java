package pe.com.nextel.bean;

import java.util.List;

public class TransaccionDTO {
	
	public final static int INDIVIDUAL = 0;
	public final static int GRUPAL = 1;
	
	public final static int OK = 1;
	public final static int ERROR = 0;
	
	private int idTransaccion;
	private List<PosicionDTO> posiciones;
	private ErrorDTO error;
	private String horaInicio;
	private String horaFin;
	private boolean cache;
	//private List<LogDTO> logDetalleDTO;
	private int flagExito;
	private int idLog;
			
	public int getIdLog() {
		return idLog;
	}
	public void setIdLog(int idLog) {
		this.idLog = idLog;
	}
	public int getIdTransaccion() {
		return idTransaccion;
	}
	public void setIdTransaccion(int idTransaccion) {
		this.idTransaccion = idTransaccion;
	}
	public boolean isCache() {
		return cache;
	}
	public void setCache(boolean cache) {
		this.cache = cache;
	}
	public String getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}
	public String getHoraFin() {
		return horaFin;
	}
	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}
	public List<PosicionDTO> getPosiciones() {
		return posiciones;
	}
	public void setPosiciones(List<PosicionDTO> posiciones) {
		this.posiciones = posiciones;
	}
	public ErrorDTO getError() {
		return error;
	}
	public void setError(ErrorDTO error) {
		this.error = error;
	}
//	public List<LogDTO> getLogDetalleDTO() {
//		return logDetalleDTO;
//	}
//	public void setLogDetalleDTO(List<LogDTO> logDetalleDTO) {
//		this.logDetalleDTO = logDetalleDTO;
//	}
	public int getFlagExito() {
		return flagExito;
	}
	public void setFlagExito(int flagExito) {
		this.flagExito = flagExito;
	}
	
}
