package pe.com.nextel.bean;

/**
 * @author Devos Inc.
 */

public class PuntoInteresDTO {
	
	public static final int GLOBALES = 2;
	public static final int CUENTA = 1;
	public static final int PRIVADO = 0;
	
	private String idPuntoInteres;
	private CategoriaDTO categoria;
	private String nombre;
	private CoordenadaDTO coordenada;
	private String fechaRegistro;
	private String direccion;
	private UsuarioDTO usuario;
	private String rutaImagen;
	private String visibilidad;
	private String distrito;
	private String idProvincia;
	private String idDepartamento;
	private String idDistrito;
	
	
	
	public String getIdProvincia() {
		return idProvincia;
	}
	public void setIdProvincia(String idProvincia) {
		this.idProvincia = idProvincia;
	}
	public String getIdDepartamento() {
		return idDepartamento;
	}
	public void setIdDepartamento(String idDepartamento) {
		this.idDepartamento = idDepartamento;
	}
	public String getIdDistrito() {
		return idDistrito;
	}
	public void setIdDistrito(String idDistrito) {
		this.idDistrito = idDistrito;
	}
	public String getDistrito() {
		return distrito;
	}
	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}
	public String getVisibilidad() {
		return visibilidad;
	}
	public void setVisibilidad(String visibilidad) {
		this.visibilidad = visibilidad;
	}
	public String getRutaImagen() {
		return rutaImagen;
	}
	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public void setCoordenada(CoordenadaDTO coordenada) {
		this.coordenada = coordenada;
	}
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getIdPuntoInteres() {
		return idPuntoInteres;
	}
	public void setIdPuntoInteres(String idPuntoInteres) {
		this.idPuntoInteres = idPuntoInteres;
	}
	public CategoriaDTO getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaDTO categoria) {
		this.categoria = categoria;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public CoordenadaDTO getCoordenada() {
		return coordenada;
	}
	
	

}
