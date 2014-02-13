package pe.com.nextel.service;

import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.AuditoriaDAO;

public class AuditoriaServiceImpl implements AuditoriaService {
	
	DAOFactory fabrica = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	AuditoriaDAO auditoriaDAO=fabrica.getAuditoriaDAO();

	@Override
	public boolean registrar(String tabla, int idUsuario, int idRegistro, String accion) {
		return auditoriaDAO.registrar(tabla, idUsuario, idRegistro, accion);
	}

}
