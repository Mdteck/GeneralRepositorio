package pe.com.nextel.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import pe.com.nextel.bean.GeocercaDTO;
import pe.com.nextel.bean.HorarioDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.dao.iface.AuditoriaDAO;
import pe.com.nextel.service.AuditoriaService;
import pe.com.nextel.service.BusinessDelegate;
import pe.com.nextel.service.GeocercaService;
import pe.com.nextel.util.LoggerUtil;
import pe.com.nextel.util.ParserUtil;
import pe.com.nextel.util.PropertyUtil;

/**
 * @author Devos Inc.
 * 
 * Clase que recepciona las solicitudes enviadas desde la interfaz web, para el manejo de datos de las geocercas.
 * 
 */

public class GeocercaAction {
	
		private static final DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	//********************** Variables *************************//
		
		private String listarPorUsuario;
		private List<PosicionDTO> posiciones;
		private GeocercaDTO geocerca;
		private List<GeocercaDTO> geocercas;
		private String tipoRpta;														//Tipo de Mensaje que se le devuelve a la interfaz (OK o ERROR)
		private String mensaje;															//Mensaje que se le devuelve a la interfaz
		private boolean puedeModificar;
		GeocercaService geocercaService=BusinessDelegate.getGeocercaService();			//Instancia del servicio de Geocercas para acceder a la BD
		AuditoriaService auditoriaService=BusinessDelegate.getAuditoriaService();
		Logger logger = LoggerUtil.getInstance();
//		Logger logger = Logger.getLogger(GeocercaAction.class.getName());
		
		//********************** Setter & Getter *************************//
		
		
		public List<PosicionDTO> getPosiciones() {
			return posiciones;
		}

		public void setPosiciones(List<PosicionDTO> posiciones) {
			this.posiciones = posiciones;
		}
		public String getMensaje() {
			return mensaje;
		}

		public String getListarPorUsuario() {
			return listarPorUsuario;
		}


		public void setListarPorUsuario(String listarPorUsuario) {
			this.listarPorUsuario = listarPorUsuario;
		}


		public GeocercaDTO getGeocerca() {
			return geocerca;
		}


		public void setGeocerca(GeocercaDTO geocerca) {
			this.geocerca = geocerca;
		}


		public List<GeocercaDTO> getGeocercas() {
			return geocercas;
		}


		public void setGeocercas(List<GeocercaDTO> geocercas) {
			this.geocercas = geocercas;
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

		public boolean isPuedeModificar() {
			return puedeModificar;
		}

		public void setPuedeModificar(boolean puedeModificar) {
			this.puedeModificar = puedeModificar;
		}

		//********************** Metodos *************************//
		
		
		/**
		 * Metodo que obtiene una lista de todos las geocercas
		 */
		public String listar(){
				logger.info("INICIO ListarGeocerca");
				int idUsuario = Integer.parseInt(getGeocerca().getIdUsuario());
				
				HttpServletRequest request =  ServletActionContext.getRequest();
		        HttpSession session = request.getSession(false); 
				String tipo = session.getAttribute("tipo").toString();
				int tipo1=Integer.parseInt(tipo);
				
				setGeocercas(geocercaService.listar(idUsuario, tipo1));
				if(getGeocercas()==null){
					setTipoRpta("ERROR");
					setMensaje("Hubo un error obteniendo la lista de geocercas. Por favor, reintente.");
//					logger.error("Error obteniendo geocercas en BD");
					logger.info("Error obteniendo geocercas en BD");
				}else{
					setTipoRpta("OK");
					logger.info("OK se obtuvo geocercas de BD");
				}
				logger.info("FIN ListarGeocerca");
			return "listar";
		}

		/**
		 * Metodo que registra la geocerca dada
		 */
		public String registrar(){
			logger.info("INICIO RegistrarGeocerca");
			HttpServletRequest request =  ServletActionContext.getRequest();
	        HttpSession session = request.getSession(true); 
			int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
			GeocercaDTO geocerca = getGeocerca();
			
			if (validar() == false) {
				setTipoRpta("ERROR"); // validar() setea los mensajes de error apropiados
				return "registrar";
			}
			
			int idGeocerca = geocercaService.registrar(geocerca);
			boolean resultado = geocercaService.asignarGeocerca(idGeocerca, geocerca.getUsuarios());
			if(resultado){
					setTipoRpta("OK");
					setMensaje("Se registro la geocerca correctamente");
					auditoriaService.registrar("t_geocerca", idUsuario, idGeocerca, AuditoriaDAO.REGISTRAR);
					logger.info("OK se registro la geocerca en BD");
			}else{
				setTipoRpta("ERROR");
				setMensaje("Hubo un error accediendo a la BD. Por favor, reintente.");
//				logger.error("Error registrando geocerca en BD");
				logger.info("Error registrando geocerca en BD");
			}
			logger.info("FIN RegistrarGeocerca");
			return "registrar";
		}
		
		private boolean validar() {
			if(geocerca.getNombre().isEmpty()){
				setMensaje("Debe indicar el nombre de la geocerca");
//				logger.warn("No se indico el nombre de la geocerca");
				logger.info("No se indico el nombre de la geocerca");
				return false;
			}
			if (geocerca.getNombre().length() > 40) {
				setMensaje("El nombre no puede ser mayor a 40 caracteres");
//				logger.warn("El nombre de la geocerca es mayor a 40 caracteres");
				logger.info("El nombre de la geocerca es mayor a 40 caracteres");
				return false;
			}

			if (geocerca.getEmailNotificacion().isEmpty()) {
				setMensaje("Debe indicar un email de notificacion");
//				logger.warn("No se indico un email de notificacion");
				logger.info("No se indico un email de notificacion");
				return false;
			}
			if (geocerca.getEmailNotificacion().length() > 100) {
				setMensaje("El email de notificacion no puede ser mayor a 100 caracteres");
//				logger.warn("El email de notificacion es mayor a 100 caracteres");
				logger.info("El email de notificacion es mayor a 100 caracteres");
				return false;
			}
			// chequeamos un email valido con expresiones regulares
			if (!geocerca.getEmailNotificacion().matches("^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) {
				setMensaje("Debe ingresar un email de notificacion valido");
//				logger.warn("El email es invalido");
				logger.info("El email es invalido");
				return false;
			}

			if (geocerca.getObservacion().length() > 50) {
				setMensaje("La observacion no puede ser mayor a 50 caracteres");
//				logger.warn("La observacion tiene mas de 50 caracteres");
				logger.info("La observacion tiene mas de 50 caracteres");
				return false;
			}			
			
			if (geocerca.getUsuarios()==null) {
				setMensaje("Debe seleccionar los usuarios monitoreados dentro de la geocerca");
//				logger.warn("No se seleccionaron monitores para la geocerca");
				logger.info("No se seleccionaron monitores para la geocerca");
				return false;
			}
			int cantidadUsuarios = geocerca.getUsuarios().size();
			int maxUsuarioxGeocerca=Integer.parseInt(PropertyUtil.readProperty("MAX_USERSXGEOCERCA"));
			if(cantidadUsuarios>maxUsuarioxGeocerca){
				setMensaje("Solo se pueden asignar un maximo de "+maxUsuarioxGeocerca+ " monitoreados para esta Geocerca.");
//				logger.warn("Se asignaron mas usuarios de los permitidos por la geocerca");
				logger.info("Se asignaron mas usuarios de los permitidos por la geocerca");
				return false;
			}

			if (geocerca.getHorario().getFechaInicio().isEmpty()) {
				setMensaje("Debe indicar la fecha de inicio");
//				logger.warn("No se indico la fecha de inicio");
				logger.info("No se indico la fecha de inicio");
				return false;
			}
			if (geocerca.getHorario().getFechaFin().isEmpty()) {
				setMensaje("Debe indicar la fecha de fin");
//				logger.warn("No se indico la fecha de fin");
				logger.info("No se indico la fecha de fin");
				return false;
			}
			String fechaInicio = geocerca.getHorario().getFechaInicio();
			String fechaFin = geocerca.getHorario().getFechaFin();
			int difFechas = ParserUtil.restarFechas(fechaInicio, fechaFin);
			int maxDiasGeocerca=Integer.parseInt(PropertyUtil.readProperty("MAX_TIEMPOGEOCERCA"));
			if(difFechas>maxDiasGeocerca){
				setMensaje("Solo se pueden asignar un maximo de "+maxDiasGeocerca+ " dias para esta Geocerca.");
//				logger.warn("Se asignaron mas dias de lo permitido para la geocerca");
				logger.info("Se asignaron mas dias de lo permitido para la geocerca");
				return false;
			}
			if(difFechas==1){
				String horaInicio=geocerca.getHorario().getHoraInicio();
				String horaFin=geocerca.getHorario().getHoraFin();
				int diffHoras = ParserUtil.restarHoras(horaInicio, horaFin);
				if(!(diffHoras>0)){
					setMensaje("Si las fechas inicial y final son iguales, la hora inicial debe ser menor a la hora final para esta Geocerca.");
//					logger.warn("Fechas/hora de fin es menor a la de inicio");
					logger.info("Fechas/hora de fin es menor a la de inicio");
					return false;
				}
			}
			if (difFechas <= 0) {
				setMensaje("La fecha de inicio debe ser anterior a la fecha de fin");
//				logger.warn("La fecha de inicio es mayor a la fecha de fin");
				logger.info("La fecha de inicio es mayor a la fecha de fin");
				return false;
			}
			
			if(geocerca.getRings().isEmpty()){
				setMensaje("Debe indicar la forma de la geocerca en el mapa");
//				logger.warn("No se indico una geocerca en el mapa");
				logger.info("No se indico una geocerca en el mapa");
				return false;
			}
			
			return true;
		}
		
		public String modificar() {
			logger.info("INICIO ModificarGeocerca");
			HttpServletRequest request =  ServletActionContext.getRequest();
	        HttpSession session = request.getSession(true); 
			int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
			GeocercaDTO geocerca = getGeocerca();
			
			if (validar() == false) {
				setTipoRpta("ERROR"); // validar() setea los mensajes de error apropiados
				return "modificar";
			}
			
			boolean resultado = geocercaService.modificarGeocerca(geocerca);
			if(resultado)	resultado = geocercaService.asignarGeocerca(Integer.parseInt(geocerca.getIdGeocerca()), geocerca.getUsuarios());
			if(resultado){
					setTipoRpta("OK");
					setMensaje("Se modifico la geocerca correctamente");
					auditoriaService.registrar("t_geocerca", idUsuario, Integer.parseInt(geocerca.getIdGeocerca()), AuditoriaDAO.ACTUALIZAR);
					logger.info("OK se modifico geocerca en BD");
			}else{
				setTipoRpta("ERROR");
				setMensaje("Hubo un error accediendo a la BD. Por favor, reintente.");
				logger.info("Error modificando geocerca en BD");
//				logger.error("Error modificando geocerca en BD");
			}
			logger.info("FIN ModificarGeocerca");
			return "modificar";
		}
		
		public String eliminar(){
			logger.info("INICIO EliminarGeocerca");
			HttpServletRequest request =  ServletActionContext.getRequest();
	        HttpSession session = request.getSession(true); 
			int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
			int id = Integer.parseInt(getGeocerca().getIdGeocerca());
			boolean resultado = geocercaService.eliminar(id);
			if(resultado){
				setTipoRpta("OK");
				setMensaje("Se elimino la geocerca correctamente");
				auditoriaService.registrar("t_geocerca", idUsuario, id, AuditoriaDAO.ELIMINAR);
				logger.info("OK se elimino geocerca de BD");
			}else{
				setTipoRpta("ERROR");
				setMensaje("Hubo un error accediendo a la BD. Por favor, reintente.");
//				logger.error("Error eliminando geocerca de BD");
				logger.info("Error eliminando geocerca de BD");
			}
			logger.info("FIN ELiminarGeocerca");
			return "eliminar";
		}
		
		/**
		 * Metodo que obtiene una geocerca dada
		 */
		public String obtener(){
			logger.info("INICIO ObtenerGeocerca");
			int id = Integer.parseInt(getGeocerca().getIdGeocerca());
			setGeocerca(geocercaService.obtener(id));
			if(getGeocerca()==null){
				setTipoRpta("ERROR");
				setMensaje("Hubo un error obteniendo la informacion de la geocerca. Por favor, reintente.");
//				logger.error("Error obteniendo geocerca con id "+id);
				logger.info("Error obteniendo geocerca con id "+id);
			}else{
				setTipoRpta("OK");
				setPuedeModificar(false);
				try {
					HorarioDTO horario = getGeocerca().getHorario();
					Date fechaInicio = formatoFecha.parse(horario.getFechaInicio() + " " + horario.getHoraInicio());
					setPuedeModificar(fechaInicio.after(new Date()));
					logger.info("OK se puede modificar Geocerca");
				} catch (ParseException e) {
//					logger.error("Error al interpretar la fecha de inicio de la geocerca: " + e.getMessage());
//					logger.error("Revisar el formato de las fechas guardadas en la BD");
//					logger.error("Error revisando formato de fechas de Geocerca");
					logger.info("Error al interpretar la fecha de inicio de la geocerca: " + e.getMessage());
					logger.info("Revisar el formato de las fechas guardadas en la BD");
					logger.info("Error revisando formato de fechas de Geocerca");
					e.printStackTrace();
				}
			}
			logger.info("FIN ObtenerGeocerca");
			return "obtener";
		}
		
		public String detalle(){
			logger.info("INICIO DetalleGeocerca");
			int id = Integer.parseInt(getGeocerca().getIdGeocerca());
			setPosiciones(geocercaService.detalle(id));
			if(getGeocerca()==null){
				setTipoRpta("ERROR");
				setMensaje("Hubo un error obteniendo la informacion de la geocerca. Por favor, reintente.");
				logger.info("Error obteniendo informacion de Geocerca de BD");
//				logger.error("Error obteniendo informacion de Geocerca de BD");
			}else{
				setTipoRpta("OK");
				logger.info("OK Se obtuvo informacion de Goecerca de BD");
			}
			logger.info("FIN DetalleGeocerca");
			return "detalle";
		}
}
