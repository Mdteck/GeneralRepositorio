package pe.com.nextel.bean;

public class BloqueadosDTO {
	private String idBloqueo;
	private String numero;
	private String etiqueta;
	private String timestamp;
	private String estado;
	
	public String getIdBloqueo() {
		return idBloqueo;
	}
	public void setIdBloqueo(String idBloqueo) {
		this.idBloqueo = idBloqueo;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
}
