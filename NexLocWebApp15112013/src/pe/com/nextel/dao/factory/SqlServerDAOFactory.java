package pe.com.nextel.dao.factory;

import pe.com.nextel.bean.UsuarioDTO;
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
import pe.com.nextel.sqlserver.SqlServerAuditoriaDAO;
import pe.com.nextel.sqlserver.SqlServerCategoriaDAO;
import pe.com.nextel.sqlserver.SqlServerCuentaDAO;
import pe.com.nextel.sqlserver.SqlServerGeocercaDAO;
import pe.com.nextel.sqlserver.SqlServerGrupoDAO;
import pe.com.nextel.sqlserver.SqlServerHandsetDAO;
import pe.com.nextel.sqlserver.SqlServerLogDAO;
import pe.com.nextel.sqlserver.SqlServerPuntoInteresDAO;
import pe.com.nextel.sqlserver.SqlServerReporteDAO;
import pe.com.nextel.sqlserver.SqlServerTrackingDAO;
import pe.com.nextel.sqlserver.SqlServerTransaccionDAO;
import pe.com.nextel.sqlserver.SqlServerUsuarioDAO;

/**
 * @author Devos Inc.
 */

public class SqlServerDAOFactory extends DAOFactory {

	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new SqlServerUsuarioDAO();
	}

	@Override
	public PuntoInteresDAO getPuntoInteresDAO() {
		return new SqlServerPuntoInteresDAO();
	}

	@Override
	public GeocercaDAO getGeocercaDAO() {
		return new SqlServerGeocercaDAO();
	}

	@Override
	public CategoriaDAO getCategoriaDAO() {
		return new SqlServerCategoriaDAO();
	}

	@Override
	public HandsetDAO getHandsetDAO() {
		return new SqlServerHandsetDAO();
	}

	@Override
	public GrupoDAO getGrupoDAO() {
		return new SqlServerGrupoDAO();
	}

	@Override
	public TrackingDAO getTrackingDAO() {
		return new SqlServerTrackingDAO();
	}

	@Override
	public CuentaDAO getCuentaDAO() {
		return new SqlServerCuentaDAO();
	}

	@Override
	public AuditoriaDAO getAuditoriaDAO() {
		return new SqlServerAuditoriaDAO();
	}

	@Override
	public TransaccionDAO getTransaccionDAO() {
		return new SqlServerTransaccionDAO();
	}

	@Override
	public ReporteDAO getReporteDAO() {
		return new SqlServerReporteDAO();
	}

	@Override
	public LogDAO getLogDAO(){
		return new SqlServerLogDAO();
	}
}
