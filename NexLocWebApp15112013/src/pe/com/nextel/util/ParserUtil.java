package pe.com.nextel.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * @author Devos Inc
 * 
 * Clase utilitaria que implementa métodos para parseo de diferentes tipos de Datos
 *
 */

public class ParserUtil {
	
	static Logger logger = LoggerUtil.getInstance();
//	static Logger logger = Logger.getLogger(ParserUtil.class.getName());
//	static Logger loggerError = Logger.getLogger("LocHandler");
	public static String LOGGER_FORMAT = "%s(T) %s(C) %s %s %s";
	
	public static String calcularMetodo(String radius, String tecnologia){
		String metodo="";
		int r = Integer.parseInt(radius);
		   if(tecnologia.equals("3G")){
			   //Cuando es 3G
			   String rango3GAGPS []= PropertyUtil.readProperty("RANGO_3G_AGPS").split(",");
			   String rango3GTriangulacion []= PropertyUtil.readProperty("RANGO_3G_TRIANGULACION").split(",");
			   String rango3GCelda = PropertyUtil.readProperty("RANGO_3G_CELDA");
			   if(r>=Integer.parseInt(rango3GAGPS[0]) && r<=Integer.parseInt(rango3GAGPS[1])){
				   metodo="AGPS";
			   }else if(r>Integer.parseInt(rango3GTriangulacion[0]) && r<=Integer.parseInt(rango3GTriangulacion[1])){
				   metodo="TRIANGULACION";
			   }else if(r>Integer.parseInt(rango3GCelda)){
				   metodo="CELDA";
			   }else{
				   metodo="NONE";
			   }
		   }else{
			   //Cuando es 2G o MIG
			   String rango2GAGPS []= PropertyUtil.readProperty("RANGO_2G_AGPS").split(",");
			   String rango2GTriangulacion []= PropertyUtil.readProperty("RANGO_2G_TRIANGULACION").split(",");
			   String rango2GCelda = PropertyUtil.readProperty("RANGO_2G_CELDA");
			   if(r>=Integer.parseInt(rango2GAGPS[0]) && r<=Integer.parseInt(rango2GAGPS[1])){
				   metodo="AGPS";
			   }else if(r>Integer.parseInt(rango2GTriangulacion[0]) && r<=Integer.parseInt(rango2GTriangulacion[1])){
				   metodo="TRIANGULACION";
			   }else if(r>=Integer.parseInt(rango2GCelda)){
				   metodo="CELDA";
			   }else{
				   metodo="NONE";
			   }
		   }
		   return metodo;
	}
	
	public static int devolverMetodo(String metodo){
		int idMetodo=0;
		if(metodo.equalsIgnoreCase("AGPS")){
			idMetodo=1;
		}else if(metodo.equalsIgnoreCase("TRIANGULACION")){
			idMetodo=2;
		}else if(metodo.equalsIgnoreCase("CELDA")){
			idMetodo=3;
		}else if(metodo.equalsIgnoreCase("NONE")){
			idMetodo=4;
		}
		
		return idMetodo;
	}
	
	public static int restarFechas(String fechaInicio, String fechaFin){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		long fechaInicialMs = 0;
		long fechaFinalMs = 0;
		try {
			Date fechaI = sdf.parse(fechaInicio);
			Date fechaF = sdf.parse(fechaFin);
			fechaInicialMs = fechaI.getTime();
			fechaFinalMs = fechaF.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		long diferencia = fechaFinalMs - fechaInicialMs;
		double dias = TimeUnit.MILLISECONDS.toDays(diferencia);
		return ( (int) dias) +1;
	}
	public static int restarHoras(String horaInicio, String horaFin){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		long horaInicialMs = 0;
		long horaFinalMs = 0;
		try {
			Date horaI = sdf.parse(horaInicio);
			Date horaF = sdf.parse(horaFin);
			horaInicialMs = horaI.getTime();
			horaFinalMs = horaF.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		long diferencia = horaFinalMs - horaInicialMs;
		double minutos = TimeUnit.MILLISECONDS.toHours(diferencia);
		return ( (int) minutos);
	}
	
	public static Date stringToDateloc(String time){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		 Date t=new Date();
		try {
			t = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 return t;
	}
	
	 public static boolean validarTime(String time){
			Date fechaI = stringToDateloc(time);
			Date now = new Date();
			long fechaInicialMs = fechaI.getTime();
			long fechaFinalMs = now.getTime();
			long diferencia = fechaFinalMs - fechaInicialMs;
			int minutos = (int)TimeUnit.MILLISECONDS.toMinutes(diferencia);
			logger.info("La diferencia de localizacion con la hora actual (en minutos) : "+minutos);
//			logger.info("La diferencia de localizacion con la hora actual (en minutos) : "+minutos);
			int maxMinutos = Integer.parseInt(PropertyUtil.readProperty("MAX_MIN_LOC"));
			logger.info("Valor en Property de minutos maximos para localizacion permitida "+maxMinutos);
//			logger.info("Valor en Property de minutos maximos para localizacion permitida "+maxMinutos);
			if(minutos<=maxMinutos)	return true;
			else					return false;
	 }
	 
	 public static String glueDate(String time){
		 String tiempo=time.substring(0,4)+time.substring(5,7)+time.substring(8,10)+time.substring(11,13)+time.substring(14,16)+time.substring(17,19);
		 return tiempo;
	 }
	 
	 public static String stringToStringFormat(String time){
		 SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		 String tiempo = time.substring(4,6)+"/"+time.substring(6,8)+"/"+time.substring(0,4)+" "+time.substring(8,10)+":"+time.subSequence(10, 12)+":"+time.substring(12);
		 Date t=new Date();
		 String f = "";
		try {
			t = sdf.parse(tiempo);
			f = sdf.format(t);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 return f;
	 }
	 
	 public static String stringToStringFormat2(String time){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		 String tiempo =time.substring(0,4)+"/"+ time.substring(4,6)+"/"+time.substring(6,8)+" "+time.substring(8,10)+":"+time.subSequence(10, 12)+":"+time.substring(12);
		 Date t=new Date();
		 String f = "";
		try {
			t = sdf.parse(tiempo);
			f = sdf.format(t);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 return f;
	 }
	 
	 public static String toTimeUTC5String(String time){
		 String fecha="";
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		 TimeZone utcZone = TimeZone.getTimeZone("UTC-5");
		 sdf.setTimeZone(utcZone);
		 Date t=new Date();
		try {
			t = sdf.parse(time);
			SimpleDateFormat qwe = new SimpleDateFormat("yyyyMMddHHmmss");
			fecha = qwe.format(t);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 return fecha;
	 }

	 
	 public static Date stringToDate(String time){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		 Date t=new Date();
		try {
			t = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 return t;
	 }

	 /**
	  * Método que convierte un inputstream en una cadena String.
	  * Norlmalmente se usa para convertir la respuesta recibida del servidor de localización.
	  * @param is: tiene la información recibida de alǵuna aplicación externa.
	  * @return Cadena String con la información parseada
	  */
	 public static String inputStreamToString(InputStream is){
		 StringBuffer out = new StringBuffer();
		    byte[] b = new byte[128];
		    try {
				for (int n; (n = is.read(b))!= -1;) {
				    out.append(new String(b, 0, n));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		    return out.toString();
	 }
	 
	 public static String inputStreamToString(InputStream is, int lengthBytes){
		 byte  [] bytes = new byte [lengthBytes];
		 try {
			is.read(bytes, 0, lengthBytes);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return new String(bytes, 0, lengthBytes);
	 }
	 
	 /**
	  * Método que convierte una cadena String en un formato XML para el manejo de tags
	  * @param stringXML que contiene el XML a convertirse
	  * @return Document que contiene todos los tags del xml convertido, si ocurre algún error en el parseado devuelve null
	  */
	 public static Document stringToXML(String stringXML){ 
			DocumentBuilderFactory dbf =  DocumentBuilderFactory.newInstance();
		    DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				InputSource is = new InputSource();
			    is.setCharacterStream(new StringReader(stringXML));
		        Document doc = db.parse(is);
		        return doc;
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				return null;
			} catch (SAXException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	 
	 /**
	  * Convierte las coordenadas tipo 12 00 48.120117S en coordenadas tipo -12.434345235
	  * @param coordenada: String que contiene la coordenada a ser convertida
	  * @return coordenada convertida a un formato como '-12.53423423'
	  */
	 public static double convertirCoordenada(String coordenada){
		 String [] c = coordenada.split(" ");
		 String orientacion=c[2].substring(c[2].length()-1);
		 c[2]=c[2].substring(0,c[2].length()-1);
		 double pos=0;
		 for(int i = 0; i<c.length;i++){
			 int m=0;
			 switch(i){
				 case 0: m=1; break;
				 case 1: m=60; break;
				 case 2: m=3600; break;
				 default: m=0; break;
			 }
			 pos=pos+Double.parseDouble(c[i])/m;
		 }
		 //Para los valores finales de cada coordenada W y S indican que son negativos por lo que multiplicará por -1.
		 //Para valores como N y E los valores son positivos, por eso se multiplicará por 1
		 int orientacionValue = 0;
		 if(orientacion.equals("S") || orientacion.equals("W")) orientacionValue = -1;
		 else													orientacionValue = 1;
		 pos=pos * orientacionValue;
		 return pos;
	 }


	public static String redondear(String coordenada, int cantDecimales){
		if(coordenada==null || coordenada.equals("")){}
		else{							
				String[] data=coordenada.split("\\.");
				if(data!=null && data.length>1 && data[1]!=null && data[1].length()>cantDecimales){
					String entero=data[0];	
					String decimal=data[1];	
					String sig=decimal.substring(5,6);	
					decimal=decimal.substring(0,5);
					int decimalI=Integer.parseInt(decimal);
					
					int sigI=Integer.parseInt(sig);					
					if(sigI>5)decimalI=decimalI+1;
					
					if((decimalI+"").length()==4) coordenada=entero+".0"+decimalI;
					else coordenada=entero+"."+decimalI;
				}
			
			
		}
		return coordenada;	
	}
	
	public static String formatMSID(String numero){
		if(numero.substring(0,2).equals("51"))
			return numero.substring(2);
		else
			return numero;
	}
	
	public static Date restarMinutos(Date fecha, int minutos){
		long fechaDateTime = fecha.getTime();
		long milis = TimeUnit.MINUTES.toMillis(minutos);
		long diferencia = fechaDateTime-milis;
		Date fechaConResta = new Date(diferencia);
		return fechaConResta;
	}
	
	public static String toSqlDateTimeFormat(Date date){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String f = "";
		 f = sdf.format(date);
		 return f;
	}
	
	public static String toLoggerDate(Date date){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		 String f = "";
		 f = sdf.format(date);
		 return f;
	}

}
