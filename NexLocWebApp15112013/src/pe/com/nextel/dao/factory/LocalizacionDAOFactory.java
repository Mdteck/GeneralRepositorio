package pe.com.nextel.dao.factory;

import pe.com.nextel.dao.iface.AuditoriaDAO;
import pe.com.nextel.dao.iface.CategoriaDAO;
import pe.com.nextel.dao.iface.CuentaDAO;
import pe.com.nextel.dao.iface.GeocercaDAO;
import pe.com.nextel.dao.iface.GrupoDAO;
import pe.com.nextel.dao.iface.HandsetDAO;
import pe.com.nextel.dao.iface.LogDAO;
import pe.com.nextel.dao.iface.PuntoInteresDAO;
import pe.com.nextel.dao.iface.ReporteDAO;
import pe.com.nextel.dao.iface.TrackingDAO;
import pe.com.nextel.dao.iface.TransaccionDAO;
import pe.com.nextel.dao.iface.UsuarioDAO;
import pe.com.nextel.localizacion.LocalizacionGrupoDAO;
import pe.com.nextel.localizacion.LocalizacionUsuarioDAO;

/**
 * 
 * @author Devos Inc.
 *
 */

public class LocalizacionDAOFactory extends DAOFactory {

	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new LocalizacionUsuarioDAO();
	}

	@Override
	public PuntoInteresDAO getPuntoInteresDAO() {
		return null;
	}

	@Override
	public GeocercaDAO getGeocercaDAO() {
		return null;
	}

	@Override
	public CategoriaDAO getCategoriaDAO() {
		return null;
	}

	@Override
	public HandsetDAO getHandsetDAO() {
		return null;
	}

	@Override
	public GrupoDAO getGrupoDAO() {
		return new LocalizacionGrupoDAO();
	}

	@Override
	public TrackingDAO getTrackingDAO() {
		return null;
	}

	@Override
	public CuentaDAO getCuentaDAO() {
		return null;
	}

	@Override
	public AuditoriaDAO getAuditoriaDAO() {
		return null;
	}

	@Override
	public TransaccionDAO getTransaccionDAO() {
		return null;
	}

	@Override
	public ReporteDAO getReporteDAO() {
		return null;
	}
	
	@Override
	public LogDAO getLogDAO() {
		return null;
	}

}
