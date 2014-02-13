package pe.com.nextel.service;

import java.util.List;

import pe.com.nextel.bean.HandsetDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.HandsetDAO;

/**
 * @author Devos Inc.
 */

public class HandsetServiceImpl implements HandsetService {

	DAOFactory fabrica = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	HandsetDAO handsetDAO=fabrica.getHandsetDAO();
	
	@Override
	public boolean modificar(HandsetDTO handset) {
		return handsetDAO.modificar(handset);
	}

	@Override
	public boolean eliminar(int idHandset) {
		return handsetDAO.eliminar(idHandset);
	}

	@Override
	public int registrar(HandsetDTO handset) {
		return handsetDAO.registrar(handset);
	}

	@Override
	public List<HandsetDTO> listar() {
		return handsetDAO.listar();
	}

	@Override
	public HandsetDTO obtener(int idHandset) {
		return handsetDAO.obtener(idHandset);
	}

}
