package pe.com.nextel.bean;

public class LogNumerosDTO {

	private int idLog;
	private String numero;
	private String fechaRegistro;
	private int flagExito;
	private String metodo;
	private String fechaRespuesta;
	private int idComponente;
		
	public int getIdLog() {
		return idLog;
	}
	public void setIdLog(int idLog) {
		this.idLog = idLog;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public int getFlagExito() {
		return flagExito;
	}
	public void setFlagExito(int flagExito) {
		this.flagExito = flagExito;
	}
	public String getMetodo() {
		return metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	public String getFechaRespuesta() {
		return fechaRespuesta;
	}
	public void setFechaRespuesta(String fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}
	public int getIdComponente() {
		return idComponente;
	}
	public void setIdComponente(int idComponente) {
		this.idComponente = idComponente;
	}
	
	
}
