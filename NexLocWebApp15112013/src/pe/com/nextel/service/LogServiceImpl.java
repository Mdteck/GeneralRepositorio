package pe.com.nextel.service;

import java.util.List;

import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.LogDAO;
import pe.com.nextel.dao.iface.TrackingDAO;

public class LogServiceImpl implements LogService{

	DAOFactory fabrica = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	LogDAO logDAO = fabrica.getLogDAO();	
	
	@Override
	public LogDTO registrarLog(int idOperacion, int idUsuario, int id){
		return logDAO.registrarLog(idOperacion, idUsuario, id);
	}
	
	@Override
	public int registrarLogDetalle(int idLog, int idMensaje,int idComponenteEnvio, String descripcionEnvio,int idComponenteRespuesta, String descripcionRespuesta, int flagExito, String fechaEnvio, String fechaRespuesta){
		return logDAO.registrarLogDetalle(idLog, idMensaje, idComponenteEnvio, descripcionEnvio, idComponenteRespuesta, descripcionRespuesta, flagExito, fechaEnvio, fechaRespuesta);
	}
	
	@Override
	public int registrarLogError(int idLog,  String descripcion){
		return logDAO.registrarLogError(idLog, descripcion);
	}
	
	@Override
	public boolean registrarLogDetalleNumero(TransaccionDTO transaccion){
		return logDAO.registrarLogDetalleNumero(transaccion);
	}
	
	@Override
	public int updateLogExito(int idLog, int flagExito){
		return logDAO.updateLogExito(idLog, flagExito);
	}
}
