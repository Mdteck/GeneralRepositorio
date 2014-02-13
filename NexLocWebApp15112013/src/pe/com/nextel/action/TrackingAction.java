package pe.com.nextel.action;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import pe.com.nextel.bean.HorarioDTO;
import pe.com.nextel.bean.TrackingDTO;
import pe.com.nextel.dao.iface.AuditoriaDAO;
import pe.com.nextel.service.AuditoriaService;
import pe.com.nextel.service.BusinessDelegate;
import pe.com.nextel.service.TrackingService;
import pe.com.nextel.util.LoggerUtil;
import pe.com.nextel.util.ParserUtil;
import pe.com.nextel.util.PropertyUtil;

public class TrackingAction {

	//********************** Variables *************************//
	
		private String idCuenta;
		private TrackingDTO tracking;
		private List<HorarioDTO> horario;
		/*Inicio 24/06/2013 MDTECK Sandy Huanco*/
		public HorarioDTO horarioSolo;
		/*Fin*/
		private List<TrackingDTO> trackings;
		private String tipoRpta;													//Tipo de Mensaje que se le devuelve a la interfaz (OK o ERROR)
		private String mensaje;														//Mensaje que se le devuelve a la interfaz
		TrackingService trackingService=BusinessDelegate.getTrackingService();		//Instancia del servicio de Tracking para acceder a la BD
		AuditoriaService auditoriaService = BusinessDelegate.getAuditoriaService();
		Logger logger = LoggerUtil.getInstance();
		private String fechaInicial;
		private String fechaFin;
		private String modo;
		private int idUsuario;
//		Logger logger = Logger.getLogger(TrackingAction.class.getName());
		
		//********************** Setter & Getter *************************//
		public HorarioDTO getHorarioSolo() {
			return horarioSolo;
		}
		public void setHorarioSolo(HorarioDTO horarioSolo) {
			this.horarioSolo = horarioSolo;
		}
		
		public String getIdCuenta() {
			return idCuenta;
		}
		
		public int getIdUsuario() {
			return idUsuario;
		}
		public void setIdUsuario(int idUsuario) {
			this.idUsuario = idUsuario;
		}
		public String getFechaInicial() {
			return fechaInicial;
		}
		public void setFechaInicial(String fechaInicial) {
			this.fechaInicial = fechaInicial;
		}
		public String getFechaFin() {
			return fechaFin;
		}
		public void setFechaFin(String fechaFin) {
			this.fechaFin = fechaFin;
		}
		public String getModo() {
			return modo;
		}
		public void setModo(String modo) {
			this.modo = modo;
		}
		public List<HorarioDTO> getHorario() {
			return horario;
		}
		public void setHorario(List<HorarioDTO> horario) {
			this.horario = horario;
		}
		public void setIdCuenta(String idCuenta) {
			this.idCuenta = idCuenta;
		}
		public TrackingDTO getTracking() {
			return tracking;
		}
		public void setTracking(TrackingDTO tracking) {
			this.tracking = tracking;
		}
		public List<TrackingDTO> getTrackings() {
			return trackings;
		}
		public void setTrackings(List<TrackingDTO> trackings) {
			this.trackings = trackings;
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
		

		//********************** Metodos *************************//

		public String registrar(){
			logger.info("INICIO RegistrarTracking");
			HttpServletRequest request =  ServletActionContext.getRequest();
	        HttpSession session = request.getSession(true); 
			int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
			TrackingDTO tracking = getTracking();
			if (tracking.getMonitoreado().getIdUsuario().equals("-1") 
					|| tracking.getHorario().getFechaInicio().isEmpty()
					|| tracking.getHorario().getFechaFin().isEmpty()) {
				setTipoRpta("ERROR");
				setMensaje("Debe completar todos los campos.");
//				logger.warn("Campos de formulario incompletos");
				logger.info("Campos de formulario incompletos");
				logger.info("FIN RegistrarTracking");
				return "registrar";
			}
			String fechaInicio = tracking.getHorario().getFechaInicio();
			String fechaFin = tracking.getHorario().getFechaFin();
			int difFechas = ParserUtil.restarFechas(fechaInicio, fechaFin);
			int maxDiasGeocerca=Integer.parseInt(PropertyUtil.readProperty("MAX_TIEMPOTRACKING"));
			if(difFechas>maxDiasGeocerca){
				setTipoRpta("ERROR");
				setMensaje("Solo se pueden asignar un maximo de "+maxDiasGeocerca+ " dias para este Tracking.");
//				logger.warn("Maximo de dias para tracking permitido excedido");
				logger.info("Maximo de dias para tracking permitido excedido");
				logger.info("FIN RegistrarTracking");
				return "registrar";
			}
			int idTracking = trackingService.registrar(tracking);
			if(idTracking==0){
				setTipoRpta("ERROR");
				setMensaje("Hubo un error registrado el tracking. Por favor, reintente.");
				logger.info("Error registrando tracking en la BD");
//				logger.error("Error registrando tracking en la BD");
			}else{
				setTrackings(trackings);
				setTipoRpta("OK");
				logger.info("OK registro tracking en la BD");
				auditoriaService.registrar("t_tracking", idUsuario, idTracking, AuditoriaDAO.REGISTRAR);
			}
			logger.info("FIN RegistrarTracking");
			return "registrar";
		}
		
		public String cancelar(){
			logger.info("INICIO CancelarTracking");
			HttpServletRequest request =  ServletActionContext.getRequest();
	        HttpSession session = request.getSession(true); 
			int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
			boolean estado = trackingService.cancelar(getTracking());
			if(!estado){
				setTipoRpta("ERROR");
				setMensaje("Hubo un error deteniendo el tracking. Por favor, reintente.");
				logger.info("Error deteniendo tracking de la BD");
//				logger.error("Error deteniendo tracking de la BD");
			}else{
				setTrackings(trackings);
				setTipoRpta("OK");
				setMensaje("Se detuva el tracking correctamente.");
				logger.info("OK se cancelo el tracking");
				auditoriaService.registrar("t_tracking", idUsuario, Integer.parseInt(getTracking().getIdTracking()), AuditoriaDAO.ACTUALIZAR);
			}
			logger.info("FIN CancelarTracking");
			return "cancelar";
		}
		
		public String eliminar(){
			HttpServletRequest request =  ServletActionContext.getRequest();
	        HttpSession session = request.getSession(true); 
			int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
			boolean estado = trackingService.cancelar(getTracking());
			if(!estado){
				setTipoRpta("ERROR");
				setMensaje("Hubo un error eliminando el tracking. Por favor, reintente.");
				logger.info("Eliminando tracking de la BD");
//				logger.error("Eliminando tracking de la BD");
			}else{
				setTrackings(trackings);
				setTipoRpta("OK");
				setMensaje("Se elimina el tracking correctamente.");
				auditoriaService.registrar("t_tracking", idUsuario, Integer.parseInt(getTracking().getIdTracking()), AuditoriaDAO.ELIMINAR);
				logger.info("OK eliminando tracking de la BD");
			}
			logger.info("FIN EliminarTracking");
			return "eliminar";
		}
		
		public String listar(){
			logger.info("INICIO ListarTracking");
			int idCuenta = Integer.parseInt(getTracking().getMonitor().getIdUsuario());
			List<TrackingDTO> trackings = trackingService.listar(idCuenta);
			if(trackings==null){
				setTipoRpta("ERROR");
				setMensaje("Hubo un error obteniendo la lista de trackings. Por favor, reintente.");
				logger.info("Error obteniendo lista de trackings");
//				logger.error("Error obteniendo lista de trackings");

			}else{
				setTrackings(trackings);
				setTipoRpta("OK");
				logger.info("OK obteniendo lista de tracings");
			}
			logger.info("FIN ListarTracking");
			return "listar";
		}
		
		public String obtener(){
			logger.info("INICIO ObtenerTracking");
			int idTracking = Integer.parseInt(getTracking().getIdTracking());
			TrackingDTO tracking = trackingService.obtener(idTracking);
			if(tracking==null){
				setTipoRpta("ERROR");
				setMensaje("Hubo un error obteniendo el tracking. Por favor, reintente.");
				logger.info("Error obteniendo informacion de tracking");
//				logger.error("Error obteniendo informacion de tracking");

			}else{
				setTracking(tracking);
				setTipoRpta("OK");
				logger.info("OK Obteniendo informacion de tracking");
			}
			logger.info("FIN ObtenerTracking");
			return "obtener";
		}
		/*Inicio 24/06/2013 MDTECK Sandy Huanco*/
		public String obtenerFiltros(){
			logger.info("INICIO ObtenerFiltrosTracking");
			int idTracking = Integer.parseInt(getTracking().getIdTracking());
			String fechaInicial = getFechaInicial();
			String fechaFin = getFechaFin();
			String metodo = getModo();
			String horaInicio = getHorarioSolo().getHoraInicio();
			String horaFin = getHorarioSolo().getHoraFin();			
			
			if(!horaInicio.equalsIgnoreCase("")){
				fechaInicial+=" "+horaInicio+":00";
			}else{
				fechaInicial+=" 0:00:00";
			}
			if(!horaFin.equalsIgnoreCase("")){
				fechaFin+=" "+horaFin+":00";
			}else{
				fechaFin+=" 23:59:59";
			}
			
			TrackingDTO tracking = trackingService.obtenerFiltros(idTracking, fechaInicial, fechaFin, horaInicio, horaFin, metodo);
			if(tracking==null){
				setTipoRpta("ERROR");
				setMensaje("Hubo un error obteniendo el tracking. Por favor, reintente.");
				logger.info("Error obteniendo informacion de tracking");
//				logger.error("Error obteniendo informacion de tracking");

			}else{
				setTracking(tracking);
				setTipoRpta("OK");
				logger.info("OK Obteniendo informacion de tracking");
			}
			logger.info("FIN ObtenerFiltrosTracking");
			return "obtenerFiltros";
		}
		/*Fin*/
		public String historial(){
			logger.info("INICIO HistorialTracking");
			int idCuenta = Integer.parseInt(getTracking().getMonitor().getIdUsuario());
			List<TrackingDTO> trackings = trackingService.historial(idCuenta);
			if(trackings==null){
				setTipoRpta("ERROR");
				setMensaje("Hubo un error obteniendo la lista de trackings. Por favor, reintente.");
				logger.info("Error obteniendo historial de tracking");
//				logger.error("Error obteniendo historial de tracking");
			}else{
				setTrackings(trackings);
				setTipoRpta("OK");
				logger.info("OK Obteniendo historial de tracking");
			}
			logger.info("FIN HistorialTracking");
			return "historial";
		}
		
		public String mapa(){
			logger.info("INICIO MapaTracking");
			int idCuenta = Integer.parseInt(getTracking().getMonitor().getIdUsuario());
			TrackingDTO tracking = trackingService.mapa(idCuenta);
			if(trackings==null){
				setTipoRpta("ERROR");
				setMensaje("Hubo un error obteniendo la lista de trackings. Por favor, reintente.");
//				logger.error("Error obteniendo mapa de tracking");
				logger.info("Error obteniendo mapa de tracking");
			}else{
				setTracking(tracking);
				setTipoRpta("OK");
				logger.info("OK obteniendo mapa de tracking");
			}
			logger.info("FIN MapaTracking");
			return "mapa";
		}
		
		public String filtrar(){
			logger.info("INICIO FiltrarTracking");
			String modo = getModo();
			String fechaInicial = getFechaInicial();
			String fechaFin = getFechaFin();
			int idUsuario = getIdUsuario();
			List<TrackingDTO> trackings = new ArrayList<TrackingDTO>();
			trackings = trackingService.filtrar(idUsuario, modo, fechaInicial, fechaFin);
			if(trackings==null){
				setTipoRpta("ERROR");
				setMensaje("Hubo filtrando los trackings. Por favor, reintente.");
//				logger.error("Error filtrando trackings de tracking");
				logger.info("Error filtrando trackings de tracking");
			}else{
				setTipoRpta("OK");
				setTrackings(trackings);
				logger.info("OK Filtrando Trackings");
			}
			logger.info("FIN FiltrarTracking");
			return "filtrar";
			
		}
	
}

	