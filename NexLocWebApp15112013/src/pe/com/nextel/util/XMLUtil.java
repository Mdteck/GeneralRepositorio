package pe.com.nextel.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

//import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pe.com.nextel.bean.CoordenadaDTO;
import pe.com.nextel.bean.DireccionDTO;
import pe.com.nextel.bean.ErrorDTO;
import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.service.BusinessDelegate;
import pe.com.nextel.service.LogService;

public class XMLUtil {
	static Logger logger = LoggerUtil.getInstance();
//	static Logger logger = Logger.getLogger(XMLUtil.class.getName());
	static LogService logService=BusinessDelegate.getLogService();
	/**
	  * Método que convierte el XML recibido (como String) del servidor de localización y lo devuelve en una lista de posiciones
	  * Cada bean PosicionDTO tiene la información que el servidor devuelve para un número dado.
	  * Si se le envían n números para localizar al servidor, el servidor devolverá información para esos n números,
	  * al entrar a este método la lista tendría longitud n, algunos de esas posiciones podrían tener valores de Error con la información obtenida del servidor
	  * @param stringXML: que contiene el xml de respuesta del servidor
	  * @return Lista de posiciones obtenidas del XML, se devuele una lista vacía en caso de no obtener posiciones (o errores) para ningún número enviado. Devuelve un null en cualquier otro caso.
	  */
	 public static TransaccionDTO readXMLLocalizationServer(String stringXML, int idLog, int idComponenteRespuesta){
		 	TransaccionDTO transaccion = new TransaccionDTO();
			List<PosicionDTO> posiciones = new ArrayList<PosicionDTO>();
			List<LogDTO> logDetalle = new ArrayList<LogDTO>();
			
				try {
					/*String s="<?xml version=\"1.0\" ?><!DOCTYPE svc_result SYSTEM \"MLP_SVC_RESULT_300.DTD\"><svc_result ver=\"3.0.0\"><slia ver=\"3.0.0\"><pos><msid enc=\"ASC\" type=\"MIN\">51946240592</msid><pd><time utc_off=\"+0000\">20111226234232</time><shape><CircularArea srsName=\"www.epsg.org#4326\"><coord><X>12 09 36.727295S</X><Y>76 57 21.934204W</Y><Z>NaN</Z></coord><radius>4999</radius></CircularArea></shape></pd></pos><pos><msid enc=\"ASC\" type=\"MIN\">51946240764</msid><pd><time utc_off=\"+0000\">20111227045129</time><shape><CircularArea srsName=\"www.epsg.org#4326\"><coord><X>12 00 48.120117S</X><Y>77 04 47.347412W</Y><Z>NaN</Z></coord><radius>4999</radius></CircularArea></shape></pd></pos><pos><msid enc=\"ASC\" type=\"MIN\">51981133152</msid><pd><time utc_off=\"+0000\">20111227021917</time><shape><CircularArea srsName=\"www.epsg.org#4326\"><coord><X>12 05 18.644714S</X><Y>77 05 37.03308W</Y><Z>NaN</Z></coord><radius>46</radius></CircularArea></shape></pd></pos><pos><msid enc=\"ASC\" type=\"MIN\">51981133199</msid><poserr><result resid=\"6\">POSITION METHOD FAILURE</result><add_info>Location server error: 163, The LFC Driver reported a position failure</add_info><time utc_off=\"+0000\">20111227050414</time></poserr></pos></slia></svc_result>";
					Quitamos el tag de DOCTYPE para evitar errores posteriores*/
					stringXML=stringXML.substring(0,stringXML.indexOf("!")-1).concat(stringXML.substring(stringXML.indexOf("DTD")+5));
					/*Convertimos el String recibido a un Documento XML*/
					Document doc = ParserUtil.stringToXML(stringXML);
					if(doc!=null){
			            doc.getDocumentElement ().normalize (); 
			            /*Normalizar el texto*/
			       			            
			            NodeList listaPos = doc.getElementsByTagName("pos"); 
			            /*Obtengo la lista de <pos> que se encuentran dentro del tag <slia>*/
			            if(listaPos.getLength()>0){
				            for(int i=0; i<listaPos.getLength(); i++){ 
				            	/*Recorre todos los tags <pos>*/
				            	
				            	PosicionDTO posicion = new PosicionDTO();
				            	Node pos = listaPos.item(i); 
				            	/*Obtiene el valor de cada nodo <pos>*/
				            	NodeList listaTagsPos = pos.getChildNodes(); 
				            	/*Lista de tags hijos de cada <pos> recorrido*/
				            	Node msidNode = listaTagsPos.item(0); 
				            	/*Nodo <msid> el cual siempre est� en el indice 0 de los tags <pos>*/
				            	UsuarioDTO usuario = new UsuarioDTO();
				            	usuario.setNumero(getValorNodo(msidNode));
				            	posicion.setUsuario(usuario); 
				            	/*Seteo numero de m�vil*/
				            	Node result = listaTagsPos.item(1); 
				            	/*Este Nodo contiene el tag del �ndice 1 del tag <pos>.*/
				            	String tipoResultado = result.getNodeName(); /*Nombre del nodo*/
				            	String numero = "";
								if(usuario.getNumero().substring(0,2).equals("51"))
									numero = usuario.getNumero().substring(2);
								else
									numero = usuario.getNumero();
								
				            	/* Si tipoResultado es 'pd', se obtiene los valor de posici�n, si es 'poserr', se obtendr� el msj de error*/
				            	if(tipoResultado.equals("pd")){ //�xito
				            		NodeList listaPd = result.getChildNodes();
				            		Node timeNode = listaPd.item(0); /*Obtengo el nodo Time*/
				            		posicion.setTime(ParserUtil.toTimeUTC5String(getValorNodo(timeNode)));
				            		NodeList listacircularAreaNode = listaPd.item(1).getChildNodes().item(0).getChildNodes();  /*Obtengo la lista de hijos del nodo Circular Area que se encuentra dentro del nodo Shape*/
				            		Node coord=listacircularAreaNode.item(0); /*Obtengo el nodo coord*/
				            		HashMap<String, String> coordenadas=getValoresNodo(coord); /*Obtengo en un hashmap los valores dentro de coordenadas*/
				            		CoordenadaDTO coordenada = new CoordenadaDTO();
				            		coordenada.setLongitud(ParserUtil.convertirCoordenada(coordenadas.get("Y"))+"");
				            		coordenada.setLatitud(ParserUtil.convertirCoordenada(coordenadas.get("X"))+"");
				            		posicion.setCoordenada(coordenada);
				            		Node radius=listacircularAreaNode.item(1); /*Obtengo el nodo Radius*/
				            		posicion.setRadio(getValorNodo(radius));
				            		posicion.setError(null);
				            		Node levelConf = listaPd.item(2);
				            		if(levelConf!=null)	posicion.setLevelConf(getValorNodo(levelConf));
				            		
									DireccionDTO direccion = MapUtil.geocoding(Double.parseDouble(coordenada.getLatitud()), Double.parseDouble(coordenada.getLongitud()));
				            		posicion.setDireccion(direccion);
				            			
				            		posicion.setFechaRespuesta(posicion.getTime());
				            		posicion.setFlagExito(1);
				            		posicion.setIdComponente(idComponenteRespuesta);	     		
				            						            		
				            	}else if (tipoResultado.equals("poserr")){ //Error
				            		HashMap<String, String> errorInfo = getValoresNodo(result);
				            		ErrorDTO error = new ErrorDTO();
				            		error.setResultado(errorInfo.get("result"));
				            		error.setExtrainfo(errorInfo.get("add_info"));
				            		posicion.setError(error);			
				            		posicion.setTime(ParserUtil.toTimeUTC5String(errorInfo.get("time")));
//				            		logService.updateLogDetalleLocalizacion(idLog, numero, ParserUtil.stringToStringFormat(posicion.getTime()), idComponenteRespuesta,0);
				            		posicion.setFechaRespuesta(posicion.getTime());
				            		posicion.setFlagExito(0);
				            		posicion.setIdComponente(idComponenteRespuesta);
				            	}else{ //Tag Desconocido
				            		logger.info("Se recibio un TAG Desconocido en el MLPResponse");
				            	}  
				            	
				            	Node extrainfo = listaTagsPos.item(2); //Info de los 3G
				            	if(extrainfo!=null){
				            		Node vmscid = extrainfo.getChildNodes().item(0).getChildNodes().item(0);
				            		HashMap<String, String> vmscidValues = getValoresNodo(vmscid);
				            		posicion.setCc(vmscidValues.get("cc"));
				            		posicion.setNdc(vmscidValues.get("ndc"));
				            		posicion.setVmscno(vmscidValues.get("vmscno"));
				            	}
				            	posiciones.add(posicion);
				            }
			            }else{
			            	ErrorDTO error = new ErrorDTO();
			            	error.setResultado(getValorNodo(doc.getElementsByTagName("result").item(0)));
			            	error.setExtrainfo(getValorNodo(doc.getElementsByTagName("add_info").item(0)));
			            	transaccion.setError(error);
			            }
			            transaccion.setPosiciones(posiciones);
			            transaccion.setCache(false);
			            return transaccion;
					}else{
						logger.info("Error parseando XMLResponse");
						return null;
					}

			        }catch (Throwable t) {
			        	t.printStackTrace ();
			        	return null;
			        }
	}
	 
	 /**
	  * Método que devuelve el valor de un nodo, sólo devuelve lo que hay dentro de él como un String
	  * @param nodo del cual se quiere obtener el valor
	  * @return String con valor dentro del nodo, devuelve null, si está vacío
	  */
	 private static String getValorNodo(Node nodo){
	    	return nodo.getChildNodes().item(0).getNodeValue();
	 }
	 
	 /**
	  * Método que devolverá los valores que están dentro de un tag de un xml. Normalmente se usa para devolver más de un valor en un hashmap con 
	  * el nombre de su tag correspondiente.
	  * @param nodo del cual se sacarán los subnodos y se añadirán dentro de un HashMap
	  * @return Hashmap con el key del nombre del tag de cada subnodo del nodo obtenido como parámetro
	  */
	 private static HashMap <String, String> getValoresNodo(Node nodo){
	    	HashMap<String, String> valores = new HashMap<String, String>();
	    	NodeList listaNodo = nodo.getChildNodes();
	    	for(int i=0; i<listaNodo.getLength(); i++){
	    		Node n = listaNodo.item(i);
	    		valores.put(n.getNodeName(), getValorNodo(n));
	    	}
	    	return valores;
	 }


}
