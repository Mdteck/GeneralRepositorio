package pe.com.nextel.service;

import java.util.List;

import pe.com.nextel.bean.GeocercaDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.GeocercaDAO;

/**
 * @author Devos Inc.
 */

public class GeocercaServiceImpl implements GeocercaService {

	DAOFactory fabrica = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	GeocercaDAO geocercaDAO=fabrica.getGeocercaDAO();

	@Override
	public int registrar(GeocercaDTO geocerca) {
		return geocercaDAO.registrar(geocerca);
	}

	@Override
	public boolean modificarGeocerca(GeocercaDTO geocerca) {
		return geocercaDAO.modificar(geocerca);
	}

	@Override
	public List<GeocercaDTO> listar(int idUsuario, int tipo) {
		return geocercaDAO.listar(idUsuario,tipo);
	}

	@Override
	public boolean asignarGeocerca(int idGeocerca, List<UsuarioDTO> usuarios) {
		return geocercaDAO.asignarGeocerca(idGeocerca, usuarios);
	}

	@Override
	public GeocercaDTO obtener(int idGeocerca) {
		return geocercaDAO.obtener(idGeocerca);
	}

	@Override
	public boolean eliminar(int idGeocerca) {
		return geocercaDAO.eliminar(idGeocerca);
	}

	@Override
	public List<PosicionDTO> detalle(int idGeocerca) {
		return geocercaDAO.detalle(idGeocerca);
	}

}
