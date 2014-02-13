package pe.com.nextel.action;

import java.util.logging.Logger;

import pe.com.nextel.bean.CuentaDTO;
import pe.com.nextel.bean.HorarioDTO;
import pe.com.nextel.dao.iface.AuditoriaDAO;
import pe.com.nextel.service.AuditoriaService;
import pe.com.nextel.service.BusinessDelegate;
import pe.com.nextel.service.CuentaService;
import pe.com.nextel.util.LoggerUtil;
//import org.apache.log4j.Logger;
//import pe.com.nextel.util.LoggerUtil;

public class CuentaAction {
	
	//********************** Variables *************************//
	
	private CuentaDTO cuenta;
	private String tipoRpta;														//Tipo de Mensaje que se le devuelve a la interfaz (OK o ERROR)
	private String mensaje;												//Mensaje que se le devuelve a la interfaz
	private String idUsuario;
	CuentaService cuentaService=BusinessDelegate.getCuentaService();		//Instancia del servicio de Categorias para acceder a la BD
	AuditoriaService auditoriaService=BusinessDelegate.getAuditoriaService();
	Logger logger = LoggerUtil.getInstance();
//	Logger logger = Logger.getLogger(CuentaAction.class.getName());
	
	//********************** Setter & Getter *************************//
	
	public CuentaDTO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaDTO cuenta) {
		this.cuenta = cuenta;
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
	public String getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	
	//********************** Métodos *************************//
	
	public String obtenerHorario(){
		logger.info("INICIO ObtenerHorario");
		int idCuenta = -1;
		if(getCuenta().getIdCuenta()!=null){
			idCuenta = Integer.parseInt(getCuenta().getIdCuenta());
			logger.info("OK se obtuvo el IDCuenta del horario");
		}else{
			setTipoRpta("ERROR");
//			logger.error("Ocurrio un error obteniendo el IDCuenta del horario");
			logger.info("Ocurrio un error obteniendo el IDCuenta del horario");
			return "obtenerHorario";
		}
		
		HorarioDTO horario = cuentaService.obtenerHorario(idCuenta);
		if(horario!=null){
			CuentaDTO cuenta = new CuentaDTO();
			cuenta.setHorario(horario);
			setCuenta(cuenta);
			setTipoRpta("OK");
			logger.info("OK se obtuvo el horario Linea "+ new Exception().getStackTrace()[0].getLineNumber());
			
			//StackTraceElement l = new Exception().getStackTrace()[0];
			
			//logger.info("OK se obtuvo el horario Linea "+ l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
			
		}else{
			setTipoRpta("ERROR");
//			logger.error("Error al obtener horario de la BD");
			logger.info("Error al obtener horario de la BD");
		}
		logger.info("FIN ObtenerHorario");
		return "obtenerHorario";
	}
	
	public String registrarHorario(){
		logger.info("INICIO RegistrarHorario");
		int idCuenta = -1;
		if(getCuenta().getIdCuenta()!=null){
			idCuenta = Integer.parseInt(getCuenta().getIdCuenta());
			logger.info("OK se obtuvo el IDCuenta del Horario");
		}else{
			setTipoRpta("ERROR");
//			logger.error("Error al obtener IDCuenta del Horario");
			logger.info("Error al obtener IDCuenta del Horario");
			return "modificarHorario";
		}

		boolean resultado  = cuentaService.registrarHorario(cuenta);
		if(resultado){
			setTipoRpta("OK");
			setMensaje("Se registro el horario correctamente");
			logger.info("OK Se registro el horario en BD");
			auditoriaService.registrar("t_cuenta", Integer.parseInt(idUsuario), Integer.parseInt(cuenta.getIdCuenta()), AuditoriaDAO.ACTUALIZAR);
		}else{
			setTipoRpta("ERROR");
			setMensaje("Hubo un error accediendo a la BD. Por favor, reintente.");
//			logger.error("Hubo un error registrando en BD");
			logger.info("Hubo un error registrando en BD");
		}
		logger.info("FIN RegistrarHorario");
		return "registrarHorario";
	}
	
	

	
}
