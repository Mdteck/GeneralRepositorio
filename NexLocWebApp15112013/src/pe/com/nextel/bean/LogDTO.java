package pe.com.nextel.bean;

import java.util.List;

public class LogDTO {

	public static final int OPERACION_LOCALIZACION_INDIVIDUAL = 1;
	public static final int OPERACION_LOCALIZACION_GRUPAL = 2;
	
	public static final int COMPONENTE_WEB = 1;
	public static final int COMPONENTE_BACKGROUND = 2;
	public static final int COMPONENTE_BD = 3;
	public static final int COMPONENTE_TRANSACCION = 4;	
	
	public static final int MENSAJE_BUSCAR_LOCALIZACION_BD = 1;
	public static final int MENSAJE_PEDIR_LOCALIZACION = 2;
	public static final int MENSAJE_SOLICITAR_LOCALIZACION = 3;
	public static final int MENSAJE_MOSTRAR_LOCALIZACION = 4;
	
	public static final int FLAG_EXITO_RESPONDIO = 1;
	public static final int FLAG_EXITO_NO_RESPONDIO = 2;
	public static final int FLAG_EXITO_INVALIDO = 3;	
	
	private int idLog;
	private String numero;
	private String fechaRegistro;
	private int flagExito;
	private int idMetodo;
	private String fechaRespuesta;
	private int idComponente;
	private String error;
	private String descripcionError;
	private String origen;
	private int idTipoTransaccion;
	
	public int getIdTipoTransaccion() {
		return idTipoTransaccion;
	}
	public void setIdTipoTransaccion(int idTipoTransaccion) {
		this.idTipoTransaccion = idTipoTransaccion;
	}
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
	public int getIdMetodo() {
		return idMetodo;
	}
	public void setIdMetodo(int idMetodo) {
		this.idMetodo = idMetodo;
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
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getDescripcionError() {
		return descripcionError;
	}
	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}	
}
