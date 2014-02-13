package pe.com.nextel.service;

import java.util.Date;
import java.util.List;

//import org.apache.log4j.Logger;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.xml.bind.ParseConversionEvent;

import pe.com.nextel.bean.ErrorDTO;
import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.LogDAO;
import pe.com.nextel.dao.iface.TransaccionDAO;
import pe.com.nextel.dao.iface.UsuarioDAO;
import pe.com.nextel.util.LoggerUtil;
import pe.com.nextel.util.ParserUtil;

/**
 * @author Devos Inc.
 */

public class UsuarioServiceImpl implements UsuarioService {
	
	DAOFactory fabricaSqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	UsuarioDAO sqlServerUsuarioDAO=fabricaSqlServer.getUsuarioDAO();
	DAOFactory fabricaLocalizacion = DAOFactory.getDAOFactory(DAOFactory.LOCALIZACION);
	UsuarioDAO localizacionUsuarioDAO=fabricaLocalizacion.getUsuarioDAO();
	TransaccionDAO sqlServerTransaccionDAO=fabricaSqlServer.getTransaccionDAO();
	LogDAO sqlServerLogDAO = fabricaSqlServer.getLogDAO();
	Logger logger = LoggerUtil.getInstance();
	LogService logService=BusinessDelegate.getLogService();
//	Logger loggerError = LoggerUtil.getInstanceError();
//	Logger logger = Logger.getLogger(UsuarioServiceImpl.class.getName());
//	Logger loggerError = Logger.getLogger("LocHandler");

	@Override
	public boolean modificar(UsuarioDTO usuario) {
		return sqlServerUsuarioDAO.modificar(usuario);
	}
	
	@Override
	public String getOrigen(String numero){
		return sqlServerUsuarioDAO.getOrigen(numero);
	}

	@Override
	public List<UsuarioDTO> listar(int idCuenta) {
		return sqlServerUsuarioDAO.listar(idCuenta);
	}

	@Override
	public TransaccionDTO localizar(UsuarioDTO usuario, int idUsuario, LogDTO logDTO) {
		logger.info("Se recibio el usuario "+usuario.getNumero());
		boolean estado = true;
		TransaccionDTO transaccionLoc = null;
		TransaccionDTO transaccion = sqlServerUsuarioDAO.localizar(usuario, logDTO);
		transaccion.setIdLog(logDTO.getIdLog());
		
		logger.info("El usuario "+usuario.getNumero()+" tiene cache?: "+transaccion.getPosiciones().get(0).isCache());
		
		
		if(!transaccion.getPosiciones().get(0).isCache()){
			transaccionLoc = localizacionUsuarioDAO.localizar(usuario, logDTO);			
			if(transaccionLoc!=null){	
				transaccionLoc.setIdLog(logDTO.getIdLog());
				logger.info("Se guardara la transaccion en BD");
				estado = sqlServerTransaccionDAO.registrar(transaccionLoc, idUsuario, TransaccionDTO.INDIVIDUAL);
				transaccion = transaccionLoc;
			}else{
				ErrorDTO error = new ErrorDTO();
				error.setResultado("Error de Conexión con el componente Transaccion.");
				transaccion.setFlagExito(logDTO.FLAG_EXITO_NO_RESPONDIO);
				transaccion.setError(error);			
			}
		}else{
			logService.registrarLogDetalleNumero(transaccion);
		}
		logger.info("Se grabo la transaccion en BD : "+estado);
		
//		if(estado)	return transaccion;
//		else			return null;
		return transaccion;
	}

	@Override
	public UsuarioDTO obtener(int idUsuario) {
		return sqlServerUsuarioDAO.obtener(idUsuario);
	}

	@Override
	public boolean asignarMonitoreados(int idMonitor, List<UsuarioDTO> monitoreados) {
		return sqlServerUsuarioDAO.asignarMonitoreados(idMonitor, monitoreados);
	}


	@Override
	public List<UsuarioDTO> listarMonitoreados(int idMonitor) {
		return sqlServerUsuarioDAO.listarMonitoreados(idMonitor);
	}

	@Override
	public boolean perfil(UsuarioDTO usuario) {
		return sqlServerUsuarioDAO.perfil(usuario);
	}

	@Override
	public UsuarioDTO autenticar(String numero) {
		return sqlServerUsuarioDAO.autenticar(numero);
	}

	@Override
	public void actualizarTipo(String numero, String tipo) {
		sqlServerUsuarioDAO.actualizarTipo(numero, tipo);
		
	}

	@Override
	public UsuarioDTO autenticarAdmins(String numero, String password) {
		return sqlServerUsuarioDAO.autenticarAdmin(numero, password);
	}
	
	@Override
	public String getTimestamp(String numero){
		return sqlServerUsuarioDAO.getTimestamp(numero);
	}

	@Override
	public String getEtiqueta(String numero) {
		return sqlServerUsuarioDAO.getEtiqueta(numero);
	}

	@Override
	public List<UsuarioDTO> listarNoMonitoreados(int idMonitor,
			List<UsuarioDTO> monitoreados) {
		// TODO Auto-generated method stub
		return sqlServerUsuarioDAO.listarNoMonitoreados(idMonitor, monitoreados);
	}

}
