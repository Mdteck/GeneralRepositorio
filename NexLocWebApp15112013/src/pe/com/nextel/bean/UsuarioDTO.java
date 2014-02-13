package pe.com.nextel.bean;

import java.util.List;

/**
 * @author Devos Inc.
 */

public class UsuarioDTO {
	
	public static final int ADMIN_SIS = 0;
	public static final int ADMIN_ACC = 1;
	public static final int MONITOR = 2;
	public static final String BLOQUEADO = "0";
	public static final String DESBLOQUEADO = "1";
	
	private String idUsuario;
	private CuentaDTO cuenta;
	private String numero;
	private String etiqueta;
	private String tipo;
	private String estadoHandset;
	private String estado;
	private HandsetDTO handset;
	private List<UsuarioDTO> monitores;
	private List<UsuarioDTO> monitoreados;
	

	public CuentaDTO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaDTO cuenta) {
		this.cuenta = cuenta;
	}
	public List<UsuarioDTO> getMonitores() {
		return monitores;
	}
	public void setMonitores(List<UsuarioDTO> monitores) {
		this.monitores = monitores;
	}
	public List<UsuarioDTO> getMonitoreados() {
		return monitoreados;
	}
	public void setMonitoreados(List<UsuarioDTO> monitoreados) {
		this.monitoreados = monitoreados;
	}
	public HandsetDTO getHandset() {
		return handset;
	}
	public void setHandset(HandsetDTO handset) {
		this.handset = handset;
	}
	public String getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getEstadoHandset() {
		return estadoHandset;
	}
	public void setEstadoHandset(String estadoHandset) {
		this.estadoHandset = estadoHandset;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	

}
