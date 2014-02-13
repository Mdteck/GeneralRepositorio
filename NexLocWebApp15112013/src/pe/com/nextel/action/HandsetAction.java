package pe.com.nextel.action;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import pe.com.nextel.bean.HandsetDTO;
import pe.com.nextel.dao.iface.AuditoriaDAO;
import pe.com.nextel.service.AuditoriaService;
import pe.com.nextel.service.BusinessDelegate;
import pe.com.nextel.service.HandsetService;
import pe.com.nextel.util.LoggerUtil;

/**
 * @author Devos Inc.
 * 
 * Clase que recepciona las solicitudes enviadas desde la interfaz web, para el manejo de datos de los handsets.
 * 
 */

public class HandsetAction {
	
	//********************** Variables *************************//
	
			private HandsetDTO handset;
			private List<HandsetDTO> handsets;
			private String tipoRpta;														//Tipo de Mensaje que se le devuelve a la interfaz (OK o ERROR)
			private String mensaje;															//Mensaje que se le devuelve a la interfaz
			HandsetService handsetService=BusinessDelegate.getHandsetService();				//Instancia del servicio de Handset para acceder a la BD
			AuditoriaService auditoriaService = BusinessDelegate.getAuditoriaService();
			Logger logger = LoggerUtil.getInstance();
//			Logger logger = Logger.getLogger(HandsetAction.class.getName());
			
			//********************** Setter & Getter *************************//
			
			

			public HandsetDTO getHandset() {
				return handset;
			}

			public void setHandset(HandsetDTO handset) {
				this.handset = handset;
			}

			public List<HandsetDTO> getHandsets() {
				return handsets;
			}

			public void setHandsets(List<HandsetDTO> handsets) {
				this.handsets = handsets;
			}
			public String getMensaje() {
				return mensaje;
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

			
			//********************** Metodos *************************//
			
			/**
			 * Metodo que obtiene una lista de todos los handsets
			 */
			public String listar(){
				logger.info("INICIO ListarHandset");
				setHandsets(handsetService.listar());
				if(getHandsets()==null){
					setTipoRpta("ERROR");
					setMensaje("Hubo un error obteniendo la lista de handsets. Por favor, reintente.");
					logger.info("Error obteniendo hadnsets de BD");
//					logger.error("Error obteniendo hadnsets de BD");
				}else{
					setTipoRpta("OK");
					logger.info("OK obteniendo handsets de BD ");
				}
				logger.info("FIN ListarHandset");
				return "listar";
			}
			
			/**
			 * Metodo que registra el handset dado
			 */
			public String registrar(){
				logger.info("INICIO RegistrarHandset");
				HttpServletRequest request =  ServletActionContext.getRequest();
		        HttpSession session = request.getSession(true); 
				int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
				HandsetDTO handset = getHandset();
				if (handset.getModelo().isEmpty() || handset.getPlataforma().equals("0")) {
					setTipoRpta("ERROR");
					setMensaje("Debe completar todos los campos.");
//					logger.warn("Campos incompletos");
					logger.info("Campos incompletos");
					logger.info("FIN RegistrarHandset");
					return "registrar";
				}
				if (handset.getModelo().length() > 15) {
					setTipoRpta("ERROR");
					setMensaje("El modelo del handset no debe ser mayor a 15 caracteres.");
					logger.info("Nombre de modelo mayor a 15 caracteres");
//					logger.warn("Nombre de modelo mayor a 15 caracteres");
					logger.info("FIN RegistrarHandset");
					return "registrar";
				}
				int idHandset=handsetService.registrar(handset);
				if(idHandset!=0){
					setTipoRpta("OK");
					auditoriaService.registrar("t_handset", idUsuario, idHandset, AuditoriaDAO.REGISTRAR);
					logger.info("OK Registrando Handset en BD");
				}else{
					setTipoRpta("ERROR");
					setMensaje("Hubo un error registrado el handset, por favor reintente.");
					logger.info("Error registrando handset en BD");
//					logger.error("Error registrando handset en BD");
				}
				logger.info("FIN RegistrarHandset");
				return "registrar";
			}
			
			/**
			 * Metodo que modifica un handset dado.
			 */
			public String modificar(){
				logger.info("INICIO ModificarHandset");
				HttpServletRequest request =  ServletActionContext.getRequest();
		        HttpSession session = request.getSession(true); 
				int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
				HandsetDTO handset = getHandset();
				boolean estado=handsetService.modificar(handset);
				if(estado){
					setTipoRpta("OK");
					auditoriaService.registrar("t_handset", idUsuario, Integer.parseInt(handset.getIdHandset()), AuditoriaDAO.ACTUALIZAR);
					logger.info("OK elimino Handset de BD");
				}else{
					setTipoRpta("ERROR");
					setMensaje("Hubo un error modificando el handset, por favor reintente.");
					logger.info("Error modificando handset de BD");
//					logger.error("Error modificando handset de BD");
				}
				logger.info("FIN ModificarHandset");
				return "modificar";
			}
			
			/**
			 * Metodo que elimina el handset dado
			 */
			public String eliminar(){
				logger.info("INICIO EliminarHandset");
				HttpServletRequest request =  ServletActionContext.getRequest();
		        HttpSession session = request.getSession(true); 
				int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
				int id = Integer.parseInt(getHandset().getIdHandset());
				boolean resultado = handsetService.eliminar(id);
				if(resultado){
					setTipoRpta("OK");
					setMensaje("Se elimino el handset correctamente");
					auditoriaService.registrar("t_handset", idUsuario, id, AuditoriaDAO.ELIMINAR);
					logger.info("OK se elimino handset de la BD");
				}else{
					setTipoRpta("ERROR");
					setMensaje("Hubo un error accediendo a la BD. Por favor, reintente.");
//					logger.error("Error eliminando handset de la BD");
					logger.info("Error eliminando handset de la BD");
				}
				logger.info("FIN EliminarHandset");
				return "eliminar";
			}
			
			/**
			 * Metodo que devuelve la informacion de un handset determinado
			 * 
			 */
			public String obtener(){
				logger.info("INICIO ObtenerHandset");
				int id = Integer.parseInt(getHandset().getIdHandset());
				setHandset(handsetService.obtener(id));
				if(getHandset()==null){
					setTipoRpta("ERROR");
					setMensaje("Hubo un error obteniendo la informacion del handset. Por favor, reintente.");
					logger.info("Error obteniendo informacion del handset desde la BD");
//					logger.error("Error obteniendo informacion del handset desde la BD");
				}else{
					setTipoRpta("OK");
					logger.info("OK obteniendo informacion del handset desde la BD");
				}
				logger.info("FIN ObtenerHandset");
				return "obtener";
			}

}
