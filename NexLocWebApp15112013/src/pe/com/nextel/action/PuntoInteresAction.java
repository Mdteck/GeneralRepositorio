package pe.com.nextel.action;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
//import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import pe.com.nextel.bean.ExtentDTO;
import pe.com.nextel.bean.PuntoInteresDTO;
import pe.com.nextel.bean.UbigeoDTO;
import pe.com.nextel.dao.iface.AuditoriaDAO;
import pe.com.nextel.service.AuditoriaService;
import pe.com.nextel.service.BusinessDelegate;
import pe.com.nextel.service.PuntoInteresService;
import pe.com.nextel.service.UsuarioService;
import pe.com.nextel.util.LoggerUtil;

/**
 * @author Devos Inc.
 * 
 * Clase que recepciona las solicitudes enviadas desde la interfaz web, para el manejo de datos de los puntos de interos.
 * 
 */

public class PuntoInteresAction {
	
	private static final String DIRECTORIO_IMAGENES_PI = "/imagenes-puntos-interes/";

	//********************** Variables *************************//

	private String idCuenta;
	private String idUsuario;
	private String showPrivados;
	private String xmax, xmin, ymax, ymin;
	private ExtentDTO limites;
	private PuntoInteresDTO puntoInteres;
	private List<UbigeoDTO> provincias;
	private List<UbigeoDTO> departamentos;
	private List<UbigeoDTO> distritos;
	private List<PuntoInteresDTO> puntosInteres;
	private String tipoRpta;																//Tipo de Mensaje que se le devuelve a la interfaz (OK o ERROR)
	private String mensaje;																	//Mensaje que se le devuelve a la interfaz
	//Estos tres atributos son rellenados por Struts cuando se sube un archivo
	private File imagen;
	private String imagenContentType;
	private String imagenFileName;
	PuntoInteresService puntoInteresService=BusinessDelegate.getPuntoInteresService();		//Instancia del servicio de PuntoInteres para acceder a la BD
	UsuarioService usuarioService = BusinessDelegate.getUsuarioService();
	AuditoriaService auditoriaService = BusinessDelegate.getAuditoriaService();
	Logger logger = LoggerUtil.getInstance();
//	Logger logger = Logger.getLogger(PuntoInteresAction.class.getName());
	
	//********************** Setter & Getter *************************//
	
	
	
	public String getMensaje() {
		return mensaje;
	}

	public List<UbigeoDTO> getProvincias() {
		return provincias;
	}

	public void setProvincias(List<UbigeoDTO> provincias) {
		this.provincias = provincias;
	}

	public List<UbigeoDTO> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<UbigeoDTO> departamentos) {
		this.departamentos = departamentos;
	}

	public List<UbigeoDTO> getDistritos() {
		return distritos;
	}

	public void setDistritos(List<UbigeoDTO> distritos) {
		this.distritos = distritos;
	}

	public String getShowPrivados() {
		return showPrivados;
	}

	public void setShowPrivados(String showPrivados) {
		this.showPrivados = showPrivados;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getXmax() {
		return xmax;
	}

	public void setXmax(String xmax) {
		this.xmax = xmax;
	}

	public String getXmin() {
		return xmin;
	}

	public void setXmin(String xmin) {
		this.xmin = xmin;
	}

	public String getYmax() {
		return ymax;
	}

	public void setYmax(String ymax) {
		this.ymax = ymax;
	}

	public String getYmin() {
		return ymin;
	}

	public void setYmin(String ymin) {
		this.ymin = ymin;
	}

	public ExtentDTO getLimites() {
		return limites;
	}

	public void setLimites(ExtentDTO limites) {
		this.limites = limites;
	}

	public String getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	public List<PuntoInteresDTO> getPuntosInteres() {
		return puntosInteres;
	}

	public void setPuntosInteres(List<PuntoInteresDTO> puntosInteres) {
		this.puntosInteres = puntosInteres;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getTipoRpta() {
		return tipoRpta;
	}

	public void setTipoRpta(String tipoRpta) {
		this.tipoRpta = tipoRpta;
	}

	public PuntoInteresDTO getPuntoInteres() {
		return puntoInteres;
	}

	public void setPuntoInteres(PuntoInteresDTO puntoInteres) {
		this.puntoInteres = puntoInteres;
	}	
	
	public File getImagen() {
		return imagen;
	}

	public void setImagen(File imagen) {
		this.imagen = imagen;
	}

	public String getImagenContentType() {
		return imagenContentType;
	}

	public void setImagenContentType(String imagenContentType) {
		this.imagenContentType = imagenContentType;
	}

	public String getImagenFileName() {
		return imagenFileName;
	}

	public void setImagenFileName(String imagenFileName) {
		this.imagenFileName = imagenFileName;
	}

	//********************** Motodos *************************//

	public String listar(){
		logger.info("INICIO ListarPuntosInteres");
		String c = getIdCuenta();
		String u = getIdUsuario();
		String s = getShowPrivados();
		
		if(c==null && u!=null && s!=null)
			setPuntosInteres(puntoInteresService.listar());
		else if(c!=null && u==null && s!=null)//puntos de interes de la cuenta
			setPuntosInteres(puntoInteresService.listar(Integer.parseInt(c), s.equals("true")?true:false));
		else if(c!=null && u!=null)//punto interes de usuario
			setPuntosInteres(puntoInteresService.listar(Integer.parseInt(c), Integer.parseInt(u)));
		if(getPuntosInteres()==null){
			setTipoRpta("ERROR");
			setMensaje("Hubo un error obteniendo la lista de puntos de interes. Por favor, reintente.");
			logger.info("Error obteniendo puntos de interes de BD");
//			logger.error("Error obteniendo puntos de interes de BD");
		}else{
			// Asumimos que todas las rutas que comienzan con / son relativas a la raiz de la app
			for (PuntoInteresDTO puntoInteres : getPuntosInteres()) {
				if (puntoInteres.getRutaImagen() != null && puntoInteres.getRutaImagen().startsWith("/")) {
					puntoInteres.setRutaImagen(ServletActionContext.getServletContext().getContextPath() 
							+ puntoInteres.getRutaImagen());					
				}
			}
			setTipoRpta("OK");
			logger.info("OK Obteniendo puntos de interes de BD");
		}
		logger.info("FIN ListarPuntosInteres");
		return "listar";
	}

	public String registrar() {
		logger.info("INICIO RegistrarPuntosInteres");
		HttpServletRequest request =  ServletActionContext.getRequest();
		String respuesta;
		if (request.getHeader("User-Agent").contains("MSIE")) {
			// we all hate Internet Explorer!! :D
			respuesta = "registrarIE";
		}
		else {
			respuesta = "registrar";
		}
        HttpSession session = request.getSession(true); 
		int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
		PuntoInteresDTO puntoInteres = getPuntoInteres();
		// validaciones
		if(puntoInteres.getCategoria().getIdCategoria().equals("-1") || puntoInteres.getCoordenada().getLongitud().equals("0") || puntoInteres.getNombre()==null){
			setTipoRpta("ERROR");
			setMensaje("Debe completar todos los campos");
//			logger.warn("Campos incompletos en el formulario de registro");
			logger.info("Campos incompletos en el formulario de registro");
			logger.info("FIN RegistrarPuntosInteres");
			return respuesta;
		}
		if (imagen != null) {
			if (!imagenContentType.matches("image/(gif|jpeg|pjpeg|png|bmp|x-windows-bmp)")) {
				setTipoRpta("ERROR");
				setMensaje("Seleccione un archivo de imagen valido");
//				logger.warn("Formato de imagen invalido");
				logger.info("Formato de imagen invalido");
				logger.info("FIN RegistrarPuntosInteres");
				return respuesta;
			}
			// no aceptamos imagenes de mas de 50 kb
			if ((imagen.length() / 1024) > 50) {
				setTipoRpta("ERROR");
				setMensaje("El archivo de imagen seleccionado excede el tamano maximo (50 kb)");
				logger.info("Tamano de imagen maximo excedido");
//				logger.warn("Tamano de imagen maximo excedido");
				logger.info("FIN RegistrarPuntosInteres");
				return respuesta;
			}
		}
		if (puntoInteres.getNombre().length() > 30) {
			setTipoRpta("ERROR");
			setMensaje("El nombre no puede tener mas de 30 caracteres");
			logger.info("El nombre del PI tiene mas de 30 caracteres");
//			logger.warn("El nombre del PI tiene mas de 30 caracteres");
			logger.info("FIN RegistrarPuntosInteres");
			return respuesta;			
		}
		
		try {
			if (imagen != null) {
				grabarImagen(true);
			}else {
				puntoInteres.setRutaImagen(null);
			}
			int idPuntoInteres=puntoInteresService.registrar(puntoInteres);
			if(idPuntoInteres!=0){
				setTipoRpta("OK");
				auditoriaService.registrar("t_puntoInteres", idUsuario, idPuntoInteres, AuditoriaDAO.REGISTRAR);
				logger.info("OK Punto de Interes registrando en BD");
			}
			else{
				setTipoRpta("ERROR");
				setMensaje("Hubo un error registrando el punto de interes, por favor reintente.");
//				logger.error("Error registrando punto de interes en BD");
				logger.info("Error registrando punto de interes en BD");
			}
		} catch (Exception e) {
			setTipoRpta("ERROR");
			setMensaje("Hubo un error registrando el punto de interes, por favor reintente. Error: " + e.getMessage());
//			logger.error("Error registrando punto de interes en BD "+e.getMessage());
			logger.info("Error registrando punto de interes en BD "+e.getMessage());
			e.printStackTrace();
		}
		logger.info("FIN REgistrarPuntosInteres");
		return respuesta;
	}
	
	public String modificar(){
		logger.info("INICIO ModificarPuntosInteres");
		HttpServletRequest request =  ServletActionContext.getRequest();
        HttpSession session = request.getSession(true); 
		String respuesta;
		if (request.getHeader("User-Agent").contains("MSIE")) {
			// we all hate Internet Explorer!! :D
			respuesta = "modificarIE";
		}else {
			respuesta = "modificar";
		}
		int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
		PuntoInteresDTO puntoInteres = getPuntoInteres();
		// validaciones
		if(puntoInteres.getCategoria().equals("-1") || puntoInteres.getCoordenada().getLongitud().equals("0") || puntoInteres.getNombre()==null){
			setTipoRpta("ERROR");
			setMensaje("Debe completar todos los campos");
//			logger.warn("Campos del formulario incompletos");
			logger.info("Campos del formulario incompletos");
			logger.info("FIN ModificarPuntosInteres");
			return respuesta;
		}
		if (puntoInteres.getNombre().length() > 30) {
			setTipoRpta("ERROR");
			setMensaje("El nombre no puede tener mas de 30 caracteres");
			logger.info("Nombre del punto de interes tiene mas de 30 caracteres");
//			logger.warn("Nombre del punto de interes tiene mas de 30 caracteres");
			logger.info("FIN ModificarPuntosInteres");
			return respuesta;			
		}
		if (imagen != null) {
			if (!imagenContentType
					.matches("image/(gif|jpeg|pjpeg|png|bmp|x-windows-bmp)")) {
				setTipoRpta("ERROR");
				setMensaje("Seleccione un archivo de imagen valido");
				logger.info("Formato de imagen invalido");
//				logger.warn("Formato de imagen invalido");
				logger.info("FIN ModificarPuntosInteres");
				return respuesta;
			}
			// no aceptamos imagenes de mas de 50 kb
			if ((imagen.length() / 1024) > 50) {
				setTipoRpta("ERROR");
				setMensaje("El archivo de imagen seleccionado excede el tamano maximo (50 kb)");
				logger.info("Tamano de imagen excede el maximo (50kb)");
//				logger.warn("Tamano de imagen excede el maximo (50kb)");
				logger.info("FIN ModificarPuntosInteres");
				return respuesta;
			}
		}

		try {
			if (imagen != null) {
				grabarImagen(false);
			}
			boolean estado=puntoInteresService.modificar(puntoInteres);
			if(estado){
				setTipoRpta("OK");
				auditoriaService.registrar("t_puntoInteres", idUsuario, Integer.parseInt(getPuntoInteres().getIdPuntoInteres()), AuditoriaDAO.ACTUALIZAR);
				logger.info("OK se modifico punto de interes en BD");
			}else{
				setTipoRpta("ERROR");
				setMensaje("Hubo un error modificando el punto de interes, por favor reintente.");
//				logger.error("Error Modificando Punto de Interes en BD");
				logger.info("Error Modificando Punto de Interes en BD");
			}
		} catch (IOException e) {
//			logger.error("ERROR modificando punto de interes "+e.getMessage());
			logger.info("ERROR modificando punto de interes "+e.getMessage());
			e.printStackTrace();
		}
		logger.info("FIN ModificarPuntosInteres");
		return respuesta;
	}
	
	private void grabarImagen(boolean nueva) throws IOException {
		File archivoImagen;
		if (!nueva && puntoInteres.getRutaImagen() != null && !puntoInteres.getRutaImagen().isEmpty()) {
			archivoImagen = new File(ServletActionContext.getServletContext().getRealPath(puntoInteres.getRutaImagen()));
		}
		else {
			String rutaImagen = DIRECTORIO_IMAGENES_PI + imagenFileName;
			archivoImagen = new File(ServletActionContext.getServletContext().getRealPath(rutaImagen));
			if (archivoImagen.exists()) {
			// si ya existe un archivo con este nombre, vamos probando con el nombre 
			// del punto de interes y un correlativo, hasta encontrar un nombre que no exista
				String idCuenta = ServletActionContext.getRequest().getSession().getAttribute("idCuenta").toString();
				String extension = imagenFileName.substring(imagenFileName.lastIndexOf("."));
				int i = 1;
				do {
					rutaImagen = DIRECTORIO_IMAGENES_PI + puntoInteres.getNombre() + i + "-" + idCuenta + extension;
					archivoImagen = new File(ServletActionContext.getServletContext().getRealPath(rutaImagen));
					i++;
				} while (archivoImagen.exists());
			}
			puntoInteres.setRutaImagen(rutaImagen);
		}
		FileUtils.copyFile(imagen, archivoImagen);
	}
	
	public String eliminar(){
		logger.info("INICIO EliminarPuntosInteres");
		HttpServletRequest request =  ServletActionContext.getRequest();
        HttpSession session = request.getSession(true); 
		int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
		int id = Integer.parseInt(getPuntoInteres().getIdPuntoInteres()); 
		boolean resultado = puntoInteresService.eliminar(id);
		if(resultado){
			setTipoRpta("OK");
			setMensaje("Se elimino el punto de interes correctamente");
			logger.info("OK se elimino punto de interes de la BD");
			auditoriaService.registrar("t_puntoInteres", idUsuario, id, AuditoriaDAO.ELIMINAR);
		}else{
			setTipoRpta("ERROR");
			setMensaje("Hubo un error accediendo a la BD. Por favor, reintente.");
			logger.info("Eliminando punto de interes de la BD");
//			logger.error("Eliminando punto de interes de la BD");
		}
		logger.info("FIN EliminarPuntoInteres");
		return "eliminar";
	}
	
	public String obtener(){
		logger.info("INICIO ObtenerPuntoInteres");
		int id = Integer.parseInt(getPuntoInteres().getIdPuntoInteres());	
		setPuntoInteres(puntoInteresService.obtener(id));
		if(getPuntoInteres()==null){
			setTipoRpta("ERROR");
			setMensaje("Hubo un error obteniendo la informacion del puntos de interes. Por favor, reintente.");
//			logger.error("Error obteniendo informacion de punto de interes de la BD");
			logger.info("Error obteniendo informacion de punto de interes de la BD");
		}else{
			if (puntoInteres.getRutaImagen() != null && puntoInteres.getRutaImagen().startsWith("/")) {
				puntoInteres.setRutaImagen(ServletActionContext.getServletContext().getContextPath() + puntoInteres.getRutaImagen());					
			}
			setTipoRpta("OK");
			logger.info("OK se obtuvo informacion del punto de interes");
			
		}
		logger.info("FIN ObtenerPuntoInteres");
		return "obtener";
	}
	
	public String buscar(){
		logger.info("INICIO BuscarPuntoInteres");
		String nombrePuntoInteres = getPuntoInteres().getNombre();
		String cat = getPuntoInteres().getCategoria().getIdCategoria();
		int idUsuario = Integer.parseInt(getPuntoInteres().getUsuario().getIdUsuario());
		int idCategoria = 0;
		String idDepartamento=getPuntoInteres().getIdDepartamento();
		String idProvincia=getPuntoInteres().getIdProvincia();
		String idDistrito=getPuntoInteres().getIdDistrito();
		
		if(cat!=null) idCategoria=Integer.parseInt(cat);
		
		if(idDepartamento==null || idProvincia==null || idDistrito ==null){
			setTipoRpta("ERROR");
			setMensaje("Por favor, seleccione una departamento, provincia y distrito");
			logger.info("No se recibio departamento o provincia o distrito del formulario");
//			logger.warn("No se recibio departamento o provincia o distrito del formulario");
			logger.info("FIN buscarPuntoInteres");
			return "buscar";
		}
		
		if(idDepartamento.equals("Seleccione") || idProvincia.equals("Seleccione")){
			setTipoRpta("ERROR");
			setMensaje("Por favor, seleccione una departamento, provincia y distrito");
			logger.info("No se recibio departamento o provincia o distrito del formulario");
//			logger.warn("No se recibio departamento o provincia o distrito del formulario");
			logger.info("FIN buscarPuntoInteres");
			return "buscar";
		}
		if(idCategoria!=0 || !nombrePuntoInteres.isEmpty())
			setPuntosInteres(puntoInteresService.buscar(idCategoria, nombrePuntoInteres, idUsuario, idDepartamento, idProvincia, idDistrito)); 
		else
			setPuntosInteres(puntoInteresService.buscar(idUsuario));
			

		if(getPuntosInteres()==null){
			setTipoRpta("ERROR");
			setMensaje("Hubo un error obteniendo la informacion del puntos de interes. Por favor, reintente.");
//			logger.error("Error no se pudo obtener informacion del punto de interes");
			logger.info("Error no se pudo obtener informacion del punto de interes");
		}else{
			logger.info("OK se obtuvo informacion del punto de interes");
			setTipoRpta("OK");
		}
		return "buscar";
	}
	
	public String mapa(){
		logger.info("INICIO MapaPuntoInteres");
		String idCuenta = getIdCuenta();
		String idMonitoreado = getIdUsuario();
		ExtentDTO limites = new ExtentDTO();
		limites.setXmax(Double.parseDouble(xmax));
		limites.setXmin(Double.parseDouble(xmin));
		limites.setYmax(Double.parseDouble(ymax));
		limites.setYmin(Double.parseDouble(ymin));		
		if(idMonitoreado==null && idCuenta!=null)
			setPuntosInteres(puntoInteresService.mapa(limites, Integer.parseInt(idCuenta), 0));//cuenta 
		else if(idCuenta==null && idMonitoreado!=null)
			setPuntosInteres(puntoInteresService.mapa(limites, Integer.parseInt(idMonitoreado), 1));//usuario
		else
			setPuntosInteres(puntoInteresService.mapa(limites, -1, 2));//globales
		
		if(getPuntosInteres()!=null){
			setTipoRpta("OK");
			logger.info("OK Se obtuvieron los puntos de interes de la BD");
		}else{
			setTipoRpta("ERROR");
//			logger.error("Error obteniendo puntos de interes de la BD");
			logger.info("Error obteniendo puntos de interes de la BD");
		}
		logger.info("FIN MapaPuntoInteres");
		return "mapa";
	}
	
	public String listarDepartamentos(){
		logger.info("INICIO listarDepartamentos");
		setDepartamentos(puntoInteresService.listarDepartamentos());
		if(getDepartamentos()==null){
			setTipoRpta("ERROR");
//			logger.error("Error obteniendo lista de departamentos de la BD");
			logger.info("Error obteniendo lista de departamentos de la BD");
		}else{
			setTipoRpta("OK");
			logger.info("OK Se obtuvieron las departamentos de la BD");
		}
		logger.info("FIN listarDepartamentos");
		return "listarDepartamentos";
	}
	
	public String listarProvincias(){
		logger.info("INICIO listarProvincias");
		String departamento = getPuntoInteres().getIdDepartamento();
		if(departamento==null){
			setTipoRpta("ERROR");
//			logger.warn("No se obtuvo el departamento desde el formulario");
			logger.info("No se obtuvo el departamento desde el formulario");
		}else{
			if(departamento.isEmpty()){
				setTipoRpta("ERROR");
//				logger.warn("No se ha seleccionado un departamento en el formulario");
				logger.info("No se ha seleccionado un departamento en el formulario");

			}else{
				setProvincias(puntoInteresService.listarProvincias(departamento));
				if(getProvincias()!=null){
					setTipoRpta("OK");
					logger.info("OK se obtuvieron las provincias desde la BD");
				}else{
					setTipoRpta("ERROR");
					logger.info("Error obteniendo provincias desde la BD");
//					logger.error("Error obteniendo provincias desde la BD");
				}
			}
		}
		
		logger.info("FIN listarProvincias");
		return "listarProvincias";
	}
	
	public String listarDistritos(){
		logger.info("INICIO ListarDistritos");
		String departamento = getPuntoInteres().getIdDepartamento();
		String provincia = getPuntoInteres().getIdProvincia();
		if(departamento==null || provincia ==null ){
			setTipoRpta("ERROR");
			logger.info("No se han recibido correctamente el departamento y provincia desde el formulario");
		}else{
			if(!departamento.isEmpty() && !provincia.isEmpty()){
				setDistritos(puntoInteresService.listarDistritos(departamento, provincia));
			}else{
				setTipoRpta("ERROR");
				logger.info("No se ha elegido departamento o provincia desde el formulario");
//				logger.error("No se ha elegido departamento o provincia desde el formulario");

			}
			
			if(getDistritos()==null){
				setTipoRpta("ERROR");
				logger.info("Error obteniendo disrtitos desde la BD");
//				logger.error("Error obteniendo disrtitos desde la BD");
			}else{
				setTipoRpta("OK");
				logger.info("OK obteniendo distritos desde la BD");
			}
		}
		logger.info("FIN ListarDistritos");
		return "listarDistritos";
	}

}
