package pe.com.nextel.bean;

/**
 * 
 * @author Devos Inc
 * Bean que tiene la información de los errores obtenidos del servidor de localiación
 *
 */

public class ErrorDTO {
	
	private String resultado;
	private String extrainfo;
	
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public String getExtrainfo() {
		return extrainfo;
	}
	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
	}
	
	
}
