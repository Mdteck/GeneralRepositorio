package pe.com.nextel.util;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//import org.apache.log4j.Logger;


/**
 * @author Devos Inc.
 */

public class ServiceLocator {

	private Context initalContext;

	private Map<String, DataSource> cache;

	private static ServiceLocator ourInstance = new ServiceLocator();
	Logger logger = LoggerUtil.getInstance();
//	Logger logger = Logger.getLogger(ServiceLocator.class.getName());

	public static ServiceLocator getInstance() {
		return ourInstance;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ServiceLocator() {
		try {
			this.initalContext = new InitialContext();
			this.cache = Collections.synchronizedMap(new HashMap());

		} catch (NamingException ex) {
//            logger.fatal("Error en CTX lookup: ServiceLocator()");
			logger.info("Error en CTX lookup: ServiceLocator()");
            ex.printStackTrace();
		}

	}

	public DataSource getDataSource(String dataSourceName) {

		DataSource datasource = null;
		try {

			if (this.cache.containsKey(dataSourceName))
				datasource = (DataSource) this.cache.get(dataSourceName);

			else {
				Context envContext = (Context) initalContext.lookup("java:comp/env");
				datasource = (DataSource) envContext.lookup(dataSourceName);
				this.cache.put(dataSourceName, datasource);
			}

		}
		catch (NamingException ex)
		{
//			logger.fatal("Error buscando en CTX: getDataSource()");
			logger.info("Error buscando en CTX: getDataSource()");
			ex.printStackTrace();
		}

		return datasource;

	}

	
}