package pe.com.nextel.service;

import pe.com.nextel.bean.CuentaDTO;
import pe.com.nextel.bean.HorarioDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.CuentaDAO;

public class CuentaServiceImpl implements CuentaService {
	
	DAOFactory fabrica = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	CuentaDAO cuentaDAO=fabrica.getCuentaDAO();

	@Override
	public HorarioDTO obtenerHorario(int idCuenta) {
		return cuentaDAO.obtenerHorario(idCuenta);
	}

	@Override
	public boolean registrarHorario(CuentaDTO cuenta) {
		return cuentaDAO.registrarHorario(cuenta);
	}

}
