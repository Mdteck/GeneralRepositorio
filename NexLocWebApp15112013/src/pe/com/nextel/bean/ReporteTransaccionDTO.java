package pe.com.nextel.bean;

public class ReporteTransaccionDTO {

	private String identificador;
	private int ok;
	private int error;
	private int total;
	private int agps;
	private int triangulacion;
	private int celda;
	private String param;
	
	
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public int getOk() {
		return ok;
	}
	public void setOk(int ok) {
		this.ok = ok;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getAgps() {
		return agps;
	}
	public void setAgps(int agps) {
		this.agps = agps;
	}
	public int getTriangulacion() {
		return triangulacion;
	}
	public void setTriangulacion(int triangulacion) {
		this.triangulacion = triangulacion;
	}
	public int getCelda() {
		return celda;
	}
	public void setCelda(int celda) {
		this.celda = celda;
	}
	
	

}
