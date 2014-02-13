package pe.com.nextel.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import pe.com.nextel.bean.LogNumerosDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.dao.iface.AuditoriaDAO;
import pe.com.nextel.service.AuditoriaService;
import pe.com.nextel.service.BusinessDelegate;
import pe.com.nextel.service.LogService;
import pe.com.nextel.service.ReporteService;
import pe.com.nextel.service.UsuarioService;
import pe.com.nextel.util.LoggerUtil;
import pe.com.nextel.util.ParserUtil;
import pe.com.nextel.util.SmsUtil;

/**
 * @author Devos Inc.
 * 
 * Clase que recepciona las solicitudes enviadas desde la interfaz web, para el manejo de datos de los usuarios.
 * 
 */

public class UsuarioAction {
	
	//********************** Variables *************************//
	
	private UsuarioDTO usuario;
	private String sms;
	private String filtroUsuario;
	private List<UsuarioDTO> usuarios;
	private PosicionDTO posicion;
	private String tipoRpta;												//Tipo de Mensaje que se le devuelve a la interfaz (OK o ERROR)
	private String mensaje;													//Mensaje que se le devuelve a la interfaz
	UsuarioService usuarioService=BusinessDelegate.getUsuarioService();		//Instancia del servicio de Usuario para acceder a la BD
	AuditoriaService auditoriaService=BusinessDelegate.getAuditoriaService();
	LogService logService=BusinessDelegate.getLogService();
	
	Logger logger = LoggerUtil.getInstance();
	Logger loggerError = LoggerUtil.getInstanceError();
//	static final Logger logger = Logger.getLogger(UsuarioAction.class);
//	static final Logger loggerError = Logger.getLogger("LocHandler");
	//Prueba de deteeccion de push git
	
	
	//********************* Setter & Getter ********************//
		
	
	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getFiltroUsuario() {
		return filtroUsuario;
	}

	public void setFiltroUsuario(String filtroUsuario) {
		this.filtroUsuario = filtroUsuario;
	}

	public PosicionDTO getPosicion() {
		return posicion;
	}

	public void setPosicion(PosicionDTO posicion) {
		this.posicion = posicion;
	}

	public List<UsuarioDTO> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<UsuarioDTO> usuarios) {
		this.usuarios = usuarios;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	
	public String getTipoRpta() {
		return tipoRpta;
	}

	public void setTipoRpta(String tipoRpta) {
		this.tipoRpta = tipoRpta;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
		
	//********************* MÃ©todos **************************//

	
	/**
	 * MÃ©todo que modifica un usuario
	 */
	public String modificar(){
		logger.info("INICIO ModificarUsuario");
		HttpServletRequest request =  ServletActionContext.getRequest();
        HttpSession session = request.getSession(true); 
		int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
		UsuarioDTO usuario = getUsuario();
		// validaciones
		if (usuario.getEtiqueta().length() > 40) {
			setTipoRpta("ERROR");
			setMensaje("La etiqueta no puede ser mayor a 40 caracteres.");
			logger.info("Etiqueta es mayor a los 40 caracteres permitidos");
			logger.info("FIN ModificarUsuario");
			return "modificar";
		}
		
		if(usuario.getHandset().getIdHandset().equals("Seleccione")){
			setTipoRpta("ERROR");
			setMensaje("Por favor, seleccione un handset.");
			logger.info("No se selecciono un handset para el usuario");
			return "modificar";
		}
		
		String sms =getSms();
		boolean estado=usuarioService.modificar(usuario);
		//Luego de registrar el tipo y la etiqueta del usuario, se registrarÃ¡ el monitor (si es un usuario monitoreado) o los monitoreados (si el usuario es monitor)
		if(estado){
			/*
			 * Esta porcion de codigo es debido a que puede darse el caso que al no haber seleccionado ningun monitoreado, se mantenga por defecto
			 * como monitoreado al usuario.
			 */
			if (usuario.getMonitoreados() == null) {// Si no ha seleccionado ningun monitoreado, siempre por lo menos debe de quedar el mismo como monitoreado
				UsuarioDTO usuarioMonitor = new UsuarioDTO();
				usuarioMonitor.setIdUsuario(String.valueOf(usuario.getIdUsuario()));
				List<UsuarioDTO> monitoreados = new ArrayList<UsuarioDTO>();
				monitoreados.add(usuarioMonitor);
				usuario.setMonitoreados(monitoreados);
			}
			estado=usuarioService.asignarMonitoreados(Integer.parseInt(usuario.getIdUsuario()), usuario.getMonitoreados());
			setTipoRpta("OK");
			setUsuarios(usuarioService.listarNoMonitoreados(Integer.parseInt(usuario.getIdUsuario()), usuario.getMonitoreados()));
			logger.info("OK se modifico usuario en BD");
			if(!sms.isEmpty()){
				logger.info("Se envia mensaje informando de los cambios del usuario "+usuario.getNumero());
				final String numeroSms = sms, 
						mensaje = "Se hicieron cambios al usuario con numero "+usuario.getNumero();
				
				Executors.newSingleThreadExecutor().execute(new Runnable(){
					@Override
					public void run() {
						SmsUtil.sendSMS(numeroSms, mensaje);
					}
				});
			}
			auditoriaService.registrar("t_usuario", idUsuario, Integer.parseInt(usuario.getIdUsuario()), AuditoriaDAO.ACTUALIZAR);
		}else{
		setTipoRpta("ERROR");
		setMensaje("Hubo un error modificando los datos del usuario, por favor reintente.");
		logger.info("Error al modificar usuario en BD");
		}
		logger.info("FIN ModificarUsuario");
		return "modificar";
	}
	
	/**
	 * MÃ©todo que obtiene una lista de todos los usuarios
	 */
	public String listar(){
		logger.info("INICIO ListarUsuario");
		String filtroUsuario = getFiltroUsuario();
		if(filtroUsuario!=null)		listarMonitoreados();
		else{
			int idCuenta = Integer.parseInt(getUsuario().getCuenta().getIdCuenta());
			setUsuarios(usuarioService.listar(idCuenta));
		}
		if(getUsuarios()==null){
			setTipoRpta("ERROR");
			setMensaje("Hubo un error obteniendo la lista de usuarios. Por favor, reintente.");
			logger.info("Error obteniendo lista de usuarios de BD");
		}else{
			setTipoRpta("OK");
			logger.info("OK Se obtuvo lista de Usuarios en BD");
		}
		logger.info("FIN ListarUsuario Linea "+ new Exception().getStackTrace()[0].getLineNumber());
		
		//StackTraceElement l = new Exception().getStackTrace()[0];
		
		//logger.info("FIN ListarUsuario Linea "+ l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		
		return "listar";
	}
	

	/**
	 * MÃ©todo que localiza al usuario dado
	 */
	public String localizar(){
		logger.info("INICIO LocalizarUsuario");
		HttpServletRequest request =  ServletActionContext.getRequest();
        HttpSession session = request.getSession(true); 
		int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
		int id = Integer.parseInt(getUsuario().getIdUsuario());
		String fechaEnvio = ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date()));
				
		LogDTO logDTO = logService.registrarLog(LogDTO.OPERACION_LOCALIZACION_INDIVIDUAL, idUsuario, id);
		setUsuario(usuarioService.obtener(id));
				
		if(getUsuario().getEstadoHandset().equals(UsuarioDTO.BLOQUEADO)){
			setTipoRpta("ERROR");
			setMensaje("El usuario "+usuario.getEtiqueta()+" "+usuario.getNumero()+" se encuentra bloqueado.");
			
			logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, "El usuario "+usuario.getEtiqueta()+" "+usuario.getNumero()+" se encuentra bloqueado.",0, "", 1, fechaEnvio,"");
			loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, "-1", "-1", "ERROR BD", usuario.getNumero(), "Usuario "+getUsuario().getNumero()+", se encuentra bloqueado"));
			logger.info("FIN LocalizarUsuario");
			return "localizar";
		}
		//TODO 
		TransaccionDTO transaccion = usuarioService.localizar(usuario, idUsuario, logDTO);
		
		if(transaccion==null){
			setTipoRpta("ERROR");
			setMensaje("No se pudo localizar al usuario.");
			loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, "-1", "-1", "ERROR BD", getUsuario().getNumero(), "Error guardando la transaccion en BD de localizacion"));
//			logService.registrarLogDetalleNumero(transaccion.getLogDetalleDTO(), transaccion.INDIVIDUAL);
			logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, "No se pudo localizar al usuario.",0, "", 1, fechaEnvio,"");
			logService.updateLogExito(logDTO.getIdLog(), transaccion.getFlagExito());
			return "localizar";
		}
		
		
		
		if(transaccion.getError()!=null){
			loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, transaccion.getIdTransaccion(), "-1", transaccion.getError().getResultado(), getUsuario().getNumero(), transaccion.getError().getExtrainfo()));
			//final String mensaje = "Ocurrio un error durante una transaccion de localizacion."+transaccion.getError().getResultado();
			logger.info("Se envia un mail sobre error de localizacion");
			/*new Thread(){
				public void run(){
					MailUtil.enviarMailContext("Error de Transaccion de Localizacion", mensaje);
					logger.info("Se Envio mail correctamente");
				}
			}.start();*/
			setTipoRpta("ERROR");
			setMensaje("No se encontr— al usuario "+usuario.getEtiqueta()+"("+usuario.getNumero()+") ( S ) ");
//			transaccion.getLogDetalleDTO().get(0).setFlagExito(0);
//			logService.registrarLogDetalleNumero(transaccion.getLogDetalleDTO(),1);
			logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, "No se encontr— al usuario "+usuario.getEtiqueta()+"("+usuario.getNumero()+") ( S ) ", 0, "", 1, fechaEnvio, "");
			logService.updateLogExito(logDTO.getIdLog(), transaccion.getFlagExito());
			logger.info("FIN LocalizarUsuario");
			return "localizar";
		}
		
		String numero = "";
		if(getUsuario().getNumero().substring(0,2).equals("51"))
			numero = getUsuario().getNumero().substring(2);
		else
			numero = getUsuario().getNumero();
		
		if(!ParserUtil.validarTime(transaccion.getPosiciones().get(0).getTime())){
			setTipoRpta("ERROR");
			setMensaje("No se ubic— al usuario "+usuario.getEtiqueta()+" ("+numero+") ( C )");
			logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, "No se ubic— al usuario "+usuario.getEtiqueta()+" ("+numero+") ( C )", 0, "", 1, fechaEnvio, "");
//			transaccion.getLogDetalleDTO().get(0).setFlagExito(0);
//			logService.registrarLogDetalleNumero(transaccion.getLogDetalleDTO(), 1);			
			logService.updateLogExito(logDTO.getIdLog(), transaccion.getFlagExito());
			loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, transaccion.getIdTransaccion()+"", transaccion.getPosiciones().get(0).getIdPosicion()+"", "CACHE", getUsuario().getNumero(), "Error en CachŽ"));
			logger.info("FIN LocalizarUsuario");
			return "localizar";
		}
		
		if(transaccion.getPosiciones().get(0).getError()!=null){
			setTipoRpta("ERROR");
			setMensaje("No se localiz— al		 usuario "+usuario.getEtiqueta()+" ("+numero+") ( P )");
			
			logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB,"No se localiz— al usuario "+usuario.getEtiqueta()+" ("+numero+") ( P )", 0, "", 1, fechaEnvio, "");
//			transaccion.getLogDetalleDTO().get(0).setFlagExito(0);
//			logService.registrarLogDetalleNumero(transaccion.getLogDetalleDTO(), 1);
			logService.updateLogExito(logDTO.getIdLog(), transaccion.getFlagExito());
			loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, transaccion.getIdTransaccion()+"", transaccion.getPosiciones().get(0).getIdPosicion()+"", transaccion.getPosiciones().get(0).getError().getResultado()+"", numero, transaccion.getPosiciones().get(0).getError().getExtrainfo()+""));
			logger.info("FIN LocalizarUsuario");
			return "localizar";
		}

		PosicionDTO posicion = transaccion.getPosiciones().get(0);
		
		String metodo=posicion.getMetodo();
		int idMetodo =0;
		if(metodo==null){
			String origen = usuarioService.getOrigen(numero);
			metodo=ParserUtil.calcularMetodo(posicion.getRadio(), origen);
			idMetodo = ParserUtil.devolverMetodo(metodo);
//			transaccion.getLogDetalleDTO().get(0).setIdMetodo(idMetodo);
		}
		
		posicion.setMetodo(metodo);
		posicion.setTimestamp(usuarioService.getTimestamp(numero));
		setPosicion(posicion);
		getPosicion().setUsuario(getUsuario());
		if(getPosicion()!=null){
			
			setTipoRpta("OK");
			logger.info("OK Devolviendo posicion al cliente");
//			transaccion.getLogDetalleDTO().get(0).setFlagExito(1);
//			logService.registrarLogDetalleNumero(transaccion.getLogDetalleDTO(), 1);
			logService.updateLogExito(logDTO.getIdLog(), transaccion.getFlagExito());
			logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB,"OK Devolviendo posicion al cliente", 0, "", 1, fechaEnvio, "");
		}else{
			setTipoRpta("ERROR");
			setMensaje("Hubo un error accediendo a la BD. Por favor, reintente.");
			loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, transaccion.getIdTransaccion()+"", "-1", "ERROR BD", numero, "Error accediendo a BD"));
			logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB,"Hubo un error accediendo a la BD. Por favor, reintente.", 0, "", 1, fechaEnvio, "");
//			transaccion.getLogDetalleDTO().get(0).setFlagExito(0);
			logService.updateLogExito(logDTO.getIdLog(), transaccion.getFlagExito());
//			logService.registrarLogDetalleNumero(transaccion.getLogDetalleDTO(), 1);
		}
		logger.info("FIN LocalizarUsuario");
		return "localizar";
	}
	
	/**
	 * MÃ©todo que obtiene informaciï¿½n de un usuario dado.
	 */
	public String obtener(){
		logger.info("INICIO ObtenerUsuario");
		int id = Integer.parseInt(getUsuario().getIdUsuario());
		setUsuario(usuarioService.obtener(id));
		if(getUsuario()==null){
			setTipoRpta("ERROR");
			setMensaje("Hubo un error obteniendo la informacion del usuario. Por favor, reintente.");
			logger.info("Error obteniendo informacion del usuario");
//			logger.error("Error obteniendo informacion del usuario");

		}else{
			setTipoRpta("OK");
			logger.info("OK obteniendo informacion del usuario");
		}
		logger.info("FIN ObtenerUsuario");
		return "obtener";
	}
	
	public String listarMonitoreados(){
		logger.info("INICIO ListarMonitoreadosUsuario");
		String idUsuario;
		idUsuario = getUsuario().getIdUsuario();//si pide solo los monitoreados de un usuario
		setUsuarios(usuarioService.listarMonitoreados(Integer.parseInt(idUsuario)));

		if(getUsuarios()==null){
			setTipoRpta("ERROR");
			logger.info("Error obteniendo lista de monitioreados de un usuario");
//			logger.error("Error obteniendo lista de monitioreados de un usuario");
		}else{
			setTipoRpta("OK");
			logger.info("OK Obteniendo lista de monitoreados de un usuario");
		}
		logger.info("FIN ListarMonitoreadosUsuario");
		return "listarMonitoreados";
	}

	
	public String perfil(){
		logger.info("INICIO PerfilUsuario");
		//logger.info("INFO "+getUsuario().getEstadoHandset()+" "+getUsuario().getHandset().getIdHandset()+"------------------");
		
		HttpServletRequest request =  ServletActionContext.getRequest();
        HttpSession session = request.getSession(true); 
		int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
		UsuarioDTO usuario = getUsuario();
		if(usuario.getHandset().getIdHandset().equals("-1") || usuario.getEstadoHandset()==null){
			setTipoRpta("ERROR");
			setMensaje("Hubo un error modificando el perfil del usuario, por favor reintente.");
			logger.info("Ocurrio un error modificando el perfil del usuario en la BD");
//			logger.error("Ocurrio un error modificando el perfil del usuario en la BD");

		}
		else{
			boolean estado=usuarioService.perfil(usuario);
			if(estado){
				setTipoRpta("OK");
				logger.info("OK se modifico el perfil del usuario en la BD");
				auditoriaService.registrar("t_usuario", idUsuario, Integer.parseInt(usuario.getIdUsuario()), AuditoriaDAO.ACTUALIZAR);
				if(getSms()!=null){
					logger.info("Se envia un SMS informando de la modificacion");
					if(!getSms().isEmpty()){
						String numeros [] = getSms().split(",");
						final String msj="Se modifico correctamente al usuario con numero "+usuario.getNumero();
						for(int i = 0; i<numeros.length ; i++){
							final String numeroSms = numeros[i];
							Executors.newSingleThreadExecutor().execute(new Runnable(){
								@Override
								public void run() {
									SmsUtil.sendSMS(numeroSms, msj);
								}
							});
							
						}
					}
				}
				
			}
			else{
			setTipoRpta("ERROR");
			setMensaje("Hubo un error modificando el perfil del usuario, por favor reintente.");
//			logger.error("Error modificando el perfil del usuario en la BD");
			logger.info("Error modificando el perfil del usuario en la BD");
			}
		}
		logger.info("FIN PerfilUsuario");
		return "perfil";
	}
	
	public void verlog() throws IOException{
		String tipoLog = "";
		String actualFileName = "";
		String fileName = "";
		
		HttpServletRequest request =  ServletActionContext.getRequest();
		HttpServletResponse response =  ServletActionContext.getResponse();

		BufferedInputStream buf=null;
		ServletOutputStream myOut=null;

		tipoLog = request.getParameter("tipo");

		if (tipoLog.equals("NexLocWebApp")){
			actualFileName = "/tmp/config.properties";
			fileName = "webApp.log";
		}else if (tipoLog.equals("NexLocServidorBackground")){

		}else if (tipoLog.equals("NexLocServidorMobile")){

		}

		try{

			myOut = response.getOutputStream( );
			File myfile = new File(actualFileName);

			//set response headers
			response.setContentType("text/plain");

			response.addHeader(
					"Content-Disposition","attachment; filename="+fileName );

			response.setContentLength( (int) myfile.length( ) );

			FileInputStream input = new FileInputStream(myfile);
			buf = new BufferedInputStream(input);
			int readBytes = 0;

			//read from the file; write to the ServletOutputStream
			while((readBytes = buf.read( )) != -1)
				myOut.write(readBytes);

		} catch (IOException ioe){

			ioe.printStackTrace();
		} finally {

			//close the input/output streams
			if (myOut != null)
				myOut.close( );
			if (buf != null)
				buf.close( );

		}
	}
	
}
