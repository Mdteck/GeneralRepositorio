package pe.com.nextel.util;

import javax.sql.DataSource;

//import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @author Devos Inc.
 */

public class DataSourceBD {
	
 	static Logger logger = LoggerUtil.getInstance();
//	static Logger logger = Logger.getLogger(DataSourceBD.class.getName());
	
	public static Connection openConnection(){
		Connection con = null;
		try {
		DataSource ds = ServiceLocator.getInstance().getDataSource("jdbc/nexlocDataSource");
		con = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
		
	}
	
	public static void closeConnection(Connection con){
		try {
			if(con!=null && !con.isClosed()){con.close();}
		} catch (SQLException e) {
//			logger.error("Error cerrando conexion en BD");
			logger.info("Error cerrando conexion en BD");
		}
	}

}
