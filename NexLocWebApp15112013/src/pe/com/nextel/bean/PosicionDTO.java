package pe.com.nextel.bean;

/**
 * 
 * @author Devos Inc
 *
 *Clase bean que contiene la información de la posición recibida del servidor de localización. Incluye el número del usuarios. El tiempo de respuesta
 *el radio recibido, las coordenadas y los errores que puedan ocurrir al localizar
 *
 */

public class PosicionDTO {

	
	private UsuarioDTO usuario;
	private UsuarioDTO monitor;
	private CoordenadaDTO coordenada;
	private CoordenadaDTO coordenada2;
	private String time;
	private String timestamp;
	private String radio;
	private ErrorDTO error;
	private String metodo;
	private String idPosicion;
	private String cc;
	private String ndc;
	private String vmscno;
	private boolean cache;
	private String levelConf;
	private String tecnologia;
	private String estado;
	private DireccionDTO direccion;
	
	
	private int flagExito;
	private int idComponente;	
	private int idMetodo;
	private int idTipoTransaccion;
	private String fechaRespuesta; 
		
	public int getFlagExito() {
		return flagExito;
	}
	public void setFlagExito(int flagExito) {
		this.flagExito = flagExito;
	}
	public int getIdComponente() {
		return idComponente;
	}
	public void setIdComponente(int idComponente) {
		this.idComponente = idComponente;
	}
	public int getIdMetodo() {
		return idMetodo;
	}
	public void setIdMetodo(int idMetodo) {
		this.idMetodo = idMetodo;
	}
	public int getIdTipoTransaccion() {
		return idTipoTransaccion;
	}
	public void setIdTipoTransaccion(int idTipoTransaccion) {
		this.idTipoTransaccion = idTipoTransaccion;
	}
	public String getFechaRespuesta() {
		return fechaRespuesta;
	}
	public void setFechaRespuesta(String fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}
	public CoordenadaDTO getCoordenada2() {
		return coordenada2;
	}
	public void setCoordenada2(CoordenadaDTO coordenada2) {
		this.coordenada2 = coordenada2;
	}
		
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public DireccionDTO getDireccion() {
		return direccion;
	}
	public void setDireccion(DireccionDTO direccion) {
		this.direccion = direccion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTecnologia() {
		return tecnologia;
	}
	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}
	public String getLevelConf() {
		return levelConf;
	}
	public void setLevelConf(String levelConf) {
		this.levelConf = levelConf;
	}
	public boolean isCache() {
		return cache;
	}
	public void setCache(boolean cache) {
		this.cache = cache;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getNdc() {
		return ndc;
	}
	public void setNdc(String ndc) {
		this.ndc = ndc;
	}
	public String getVmscno() {
		return vmscno;
	}
	public void setVmscno(String vmscno) {
		this.vmscno = vmscno;
	}
	public String getIdPosicion() {
		return idPosicion;
	}
	public void setIdPosicion(String idPosicion) {
		this.idPosicion = idPosicion;
	}

	public String getMetodo() {
		return metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	public ErrorDTO getError() {
		return error;
	}
	public void setError(ErrorDTO error) {
		this.error = error;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	public CoordenadaDTO getCoordenada() {
		return coordenada;
	}
	public void setCoordenada(CoordenadaDTO coordenada) {
		this.coordenada = coordenada;
	}
	public String getTime() {
		return time;
	}
	public String getRadio() {
		return radio;
	}
	public void setRadio(String radio) {
		this.radio = radio;
	}
	public UsuarioDTO getMonitor() {
		return monitor;
	}
	public void setMonitor(UsuarioDTO monitor) {
		this.monitor = monitor;
	}
}
