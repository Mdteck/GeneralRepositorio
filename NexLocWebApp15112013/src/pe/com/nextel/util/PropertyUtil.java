package pe.com.nextel.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Devos Inc.
 * 
 * Clase que accede al archivo de parámetros de configuración y los devuelve en un objeto Properties
 * 
 */

public class PropertyUtil {
	
	private static Properties prop;
	
	/**
	 * 
	 * @param propiedad : ruta de larchivo de configuración
	 * @return Objeto Properties creado con los parámetros del archivo
	 */
	public static String readProperty(String propiedad) {
		if(prop==null){
			prop = new Properties();
			try {
				prop.load(new FileInputStream("C:\\Temp\\config.properties"));
//				prop.load(new FileInputStream("/tmp/config.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return prop.getProperty(propiedad);
	}

}
