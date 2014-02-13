package pe.com.nextel.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import pe.com.nextel.bean.CoordenadaDTO;
import pe.com.nextel.bean.ErrorDTO;
import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.LogDAO;

import java.util.logging.Logger;

/**
 * 
 * @author Devos Inc.
 * Clase utilitaria que se comunica con la aplicación que llama al servidor de localización
 *
 */

public class ServicioLocalizacionConnectionUtil {
	static DAOFactory fabricaSqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	static LogDAO sqlServerLogDAO = fabricaSqlServer.getLogDAO();
	
	static Logger logger = LoggerUtil.getInstance();
	static Logger loggerError = LoggerUtil.getInstanceError();
//	static Logger logger = Logger.getLogger(ServicioLocalizacionConnectionUtil.class.getName());
//	static Logger loggerError = Logger.getLogger("LocHandler");
	
	/**
	 * 
	 * @param usuarios : Lista de usuarios de los cuales se obtendrá el número para ser enviados a la aplicación que llamará al servidor de Localización
	 * @return xmlResponse obtenido de la aplicación que se conecta con el servidor de localización
	 */
	public static String llamarAplicacionConexionLocalizacion(List<UsuarioDTO> usuarios, int idLog){
		String numeros=idLog+":";
		//Se enviarán los números concatenados por un ":" a la aplicación que se comunicará con el servidor
		for(int i = 0; i<usuarios.size() ;i++) numeros+=usuarios.get(i).getNumero()+":";
		numeros=numeros.substring(0,numeros.length()-1);
		String xmlResponse="";
		URL urlAplicacionConexionLocalizacion=null;
		URLConnection conn;
		InputStream is;
		LogDTO logDTO = new LogDTO();	
		String fechaEnvio = ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date()));
		String fechaRespuesta;
		
		try {
			//Se obtiene la ruta del archivo del archivo de configuracion
			logger.info(String.format("URL Servicio Localizacion : %s",PropertyUtil.readProperty("NEXLOC_LOCSERVICE")));
			urlAplicacionConexionLocalizacion = new URL(PropertyUtil.readProperty("NEXLOC_LOCSERVICE"));
			conn = urlAplicacionConexionLocalizacion.openConnection();
		    conn.setDoOutput(true);
		    conn.setReadTimeout(300000); //200 Segundos de TimeOut
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(numeros);
		    wr.flush();
		    // Obtiene la respuesta
		    is = conn.getInputStream();
			xmlResponse=ParserUtil.inputStreamToString(is);
			wr.close();			
			fechaRespuesta = ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date()));
			sqlServerLogDAO.registrarLogDetalle(idLog, logDTO.MENSAJE_PEDIR_LOCALIZACION, LogDTO.COMPONENTE_WEB, numeros, LogDTO.COMPONENTE_TRANSACCION, xmlResponse, 1, fechaEnvio, fechaRespuesta);
		} catch (MalformedURLException e) {
//			loggerError.info("La URL "+PropertyUtil.readProperty("NEXLOC_LOCSERVICE")+ " no esta disponible");
//			loggerError.info("Error conectandose con el servidor de localizacion mientras se localizaba a : "+numeros);
			loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, "-1", "-1", "NONE", "NONE", "Error conectandose con el servidor de localizacion mientras se localizaba a : "+numeros));
			e.printStackTrace();
			fechaRespuesta = ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date()));
			sqlServerLogDAO.registrarLogDetalle(idLog, logDTO.MENSAJE_PEDIR_LOCALIZACION, LogDTO.COMPONENTE_WEB, numeros, LogDTO.COMPONENTE_TRANSACCION, "", 0, fechaEnvio, fechaRespuesta);
			sqlServerLogDAO.registrarLogError(idLog, e.getMessage());
			
		} catch (IOException e) {
//			loggerError.info("Error conectandose con el servidor de localizacion mientras se localizaba a : "+numeros);
			loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, "-1", "-1", "NONE", "NONE", "Error conectandose con el servidor de localizacion mientras se localizaba a : "+numeros));
			e.printStackTrace();
			fechaRespuesta = ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date()));
			sqlServerLogDAO.registrarLogDetalle(idLog, logDTO.MENSAJE_PEDIR_LOCALIZACION, LogDTO.COMPONENTE_WEB, numeros, LogDTO.COMPONENTE_TRANSACCION, "", 0, fechaEnvio, fechaRespuesta);
			sqlServerLogDAO.registrarLogError(idLog, e.getMessage());		
		}
		
		if(!xmlResponse.isEmpty())
			logger.info("XMLResponse :"+xmlResponse);
		return xmlResponse;
	}
	
	public static String llamarAplicacionConexionLocalizacion(String numero, int idLog){
		List<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setNumero(numero);
		usuarios.add(usuario);
		return llamarAplicacionConexionLocalizacion(usuarios, idLog);
	}
	
	
	/**
	 * Método que llamará al servidor de localización mediante la aplicación de Devos. Este método solo se usará para el entorno de Prueba
	 * @param numero para el que solicita la localizacion
	 * @return un objeto PosicionDTO con la información obtenida del servidor
	 */
	public static PosicionDTO llamarAplicacionConexionLocalizacionJSON(String numero){
		PosicionDTO posicion = new PosicionDTO();
		String jsonResponse="";
		URL urlAplicacionConexionLocalizacion=null;
		URLConnection conn;
		InputStream is;
		try {
			//Se obtiene la ruta del archivo del archivo de configuración
			urlAplicacionConexionLocalizacion = new URL(PropertyUtil.readProperty("NEXLOC_LOCSERVICE"));
			conn = urlAplicacionConexionLocalizacion.openConnection();
		    conn.setDoOutput(true);
		    conn.setReadTimeout(80000); //80 Segundos de TimeOut
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(numero);
		    wr.flush();
		    // Obtiene la respuesta
		     is = conn.getInputStream();
			jsonResponse=ParserUtil.inputStreamToString(is);
			logger.info("JSONResponse :"+jsonResponse);
			wr.close();
			
			//Parsear de json a PosicionDTO
			JSONParser parser = new JSONParser();
			Object obj=null;
			obj=parser.parse(jsonResponse);
			JSONObject jsonResponseObj=(JSONObject)obj;
			obj=parser.parse(jsonResponseObj.get("posicion").toString());
			JSONObject jsonPosicion=(JSONObject)obj;
			Object e = jsonPosicion.get("error");
			if(e!=null){
				obj=parser.parse(e.toString());
				JSONObject jsonError=(JSONObject)obj;
				ErrorDTO error = new ErrorDTO();
				Object extrainfo = jsonError.get("extrainfo");
				error.setExtrainfo(extrainfo==null?null:extrainfo.toString());
				Object resultado = jsonError.get("resultado");
				error.setResultado(resultado==null?null:resultado.toString());
				posicion.setError(error);
			}
			obj=parser.parse(jsonPosicion.get("movil").toString());
			JSONObject jsonMovil=(JSONObject)obj;
			UsuarioDTO usuario = new UsuarioDTO();
			usuario.setNumero(jsonMovil.get("numero").toString());
			CoordenadaDTO coordenada = new CoordenadaDTO();
			Object x = jsonPosicion.get("x");
			coordenada.setLongitud(x==null?null:x.toString());
			Object y = jsonPosicion.get("y");
			coordenada.setLatitud(y==null?null:y.toString());
			posicion.setUsuario(usuario);
			posicion.setCoordenada(coordenada);
			Object radio = jsonPosicion.get("radio");
			posicion.setRadio(radio==null?null:radio.toString());
			posicion.setTime(jsonPosicion.get("time").toString());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return posicion;
	}

}
