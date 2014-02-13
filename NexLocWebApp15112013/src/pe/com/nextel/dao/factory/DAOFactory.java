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

/**
 * @author Devos Inc.
 */


public abstract class DAOFactory {
    
    public static final int MYSQL = 1;
    public static final int ORACLE = 2;
    public static final int DB2 = 3;
    public static final int SQLSERVER = 4;
    public static final int XML = 5;
    public static final int JPA = 6;
    public static final int LOCALIZACION=7;

    public abstract UsuarioDAO getUsuarioDAO();
    public abstract PuntoInteresDAO getPuntoInteresDAO();
    public abstract GeocercaDAO getGeocercaDAO();
    public abstract CategoriaDAO getCategoriaDAO();
    public abstract HandsetDAO getHandsetDAO();
    public abstract GrupoDAO getGrupoDAO();
    public abstract TrackingDAO getTrackingDAO();
    public abstract CuentaDAO getCuentaDAO();
    public abstract AuditoriaDAO getAuditoriaDAO(); 
    public abstract TransaccionDAO getTransaccionDAO();
    public abstract ReporteDAO getReporteDAO();
    public abstract LogDAO getLogDAO();


    public static DAOFactory getDAOFactory(int whichFactory){
       switch(whichFactory){
       case SQLSERVER:
      	    return new SqlServerDAOFactory();
       case LOCALIZACION:
    	   return new LocalizacionDAOFactory();
       /*case MYSQ
    public abstract GeocercaDAO getGeocercaDAO();L:
       	    return new MySqlDAOFactory();
       	case XML:
       	    return new XmlDAOFactory();
       	case ORACLE:
       	    return new OracleDAOFactory();
       	case JPA:
       	    return new JpaDAOFactory();
       	case DB2:
       	    return new Db2DAOFactory();
       	
       	case XML:
       	    return new XmlDAOFactory();*/
       	default:
       	    return null;
       }
    }
    
    
}
