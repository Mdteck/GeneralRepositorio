package pe.com.nextel.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.LogManager;

//import org.apache.log4j.Logger;
import java.util.logging.Logger;

import com.esri.arcgisws.Field;
import com.esri.arcgisws.Fields;
import com.esri.arcgisws.GeocodeServerBindingStub;
import com.esri.arcgisws.PointN;
import com.esri.arcgisws.PropertySet;
import com.esri.arcgisws.PropertySetProperty;
import com.esri.arcgisws.SpatialReference;


import pe.com.nextel.bean.CoordenadaDTO;
import pe.com.nextel.bean.DireccionDTO;
import pe.com.nextel.bean.ExtentDTO;
import pe.com.nextel.bean.PosicionDTO;


/**
 * 
 * @author Devos Inc
 *
 */

public class MapUtil {	
	static Logger logger = LoggerUtil.getInstance();
//	static Logger logger = Logger.getLogger(MapUtil.class.getName());
	public int ok = 0;
	public int error = 0;
	
	

	
	/**
	 * M√©todo que obtiene una lista con las coordenadas que determinan los l√≠mites de la imagen devuelta de la posici√≥n
	 * del equipo
	 * @param latitud :Latitud de una coordenada dada
	 * @param longitud:Longitud de una coordenada dada
	 * @param distancia  :Distancia en kil√≥metros para el que se calcularan los limites
	 * @return  Objeto ExtentDTO con los valores de acuerdo al c√°lculo.
	 */
	public static ExtentDTO getLimites(CoordenadaDTO coordenada, double distancia){
				
		double kmLatitud = 113.32; //Equivalencia de 1¬∞ en kilometros para las latitudes
		double kmLongitud = 111.11;//Equivalencia de 1¬∞ en kilometros para las longitudes
		double distanciaLatitud = distancia/kmLatitud; //offset Latitud
		double distanciaLongitud= distancia/kmLongitud;	//offset Longitud

		ExtentDTO limites = new ExtentDTO();
		for(int i=0; i<4;i++){
			switch (i){
			case 0: 	
					limites.setXmax(Double.parseDouble(coordenada.getLatitud())+distanciaLatitud); //xmax
					break;
			case 1: 
					limites.setXmin(Double.parseDouble(coordenada.getLatitud())-distanciaLatitud); //xmin
					break;
			case 2: 
					limites.setYmax(Double.parseDouble(coordenada.getLongitud())+distanciaLongitud); //ymax
					break;
			case 3: 
					limites.setYmin(Double.parseDouble(coordenada.getLongitud())-distanciaLongitud); //ymin
					break;
			default:
					break;		
			}			
		}
		return limites;
	}
	
	/**
	 * Obtiene los l√≠mites de coordenadas para las posiciones en la lista recibida
	 * @param posiciones: las posiciones para las que se generarÔøΩ el extent. DeberÔøΩ por lo menos existir una posiciÔøΩn en la lista
	 * @return Lista<Double> con las coordenadas del extent
	 */
	public static ExtentDTO getLimites(List<CoordenadaDTO> coordenadas){
		double xmin=0, xmax=0, ymin=0, ymax=0;
		boolean init=true;
		if(coordenadas.size()==1){
			CoordenadaDTO coordenada = new CoordenadaDTO();
			coordenada.setLongitud(coordenadas.get(0).getLongitud());
			coordenada.setLatitud(coordenadas.get(0).getLatitud());
			return getLimites(coordenada, 0.3);
		}
		for(CoordenadaDTO coordenada : coordenadas){
				if(init){
					xmin=Double.parseDouble(coordenada.getLongitud());
					xmax=Double.parseDouble(coordenada.getLongitud());
					ymin=Double.parseDouble(coordenada.getLatitud());
					ymax=Double.parseDouble(coordenada.getLatitud());
					init=false;			
				}else{
					if(Double.parseDouble(coordenada.getLongitud())<xmin) 	xmin=Double.parseDouble(coordenada.getLongitud());
					if(Double.parseDouble(coordenada.getLongitud())>xmax) 	xmax=Double.parseDouble(coordenada.getLongitud());
					if(Double.parseDouble(coordenada.getLatitud())<ymin) 	ymin=Double.parseDouble(coordenada.getLatitud());
					if(Double.parseDouble(coordenada.getLatitud())>ymax) 	ymax=Double.parseDouble(coordenada.getLatitud());
				}

		}
		ExtentDTO limites = new ExtentDTO();
		limites.setXmax(xmax);
		limites.setXmin(xmin);
		limites.setYmax(ymax);
		limites.setYmin(ymin);
		return limites;
	}

	
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
		 int o = 0;
		 if(orientacion.equals("S") || orientacion.equals("W")) o = -1;
		 else													o =  1;
		 pos=pos * o;
		 return pos;
	 }
	
	public static String convertirCoordenada(double coordenadaI){
		String coordenaConvertida = "";
		coordenadaI = coordenadaI * -1;
		double coordenada = Math.abs( Math.round(coordenadaI * 100000000.));
		double grados = Math.floor(coordenada / 100000000) * 1;
		double minutos = Math.floor(((coordenada/100000000) - Math.floor(coordenada/100000000)) * 60);
		double segundos = Math.floor(((((coordenada/100000000) - Math.floor(coordenada/100000000)) * 60) - 
				Math.floor(((coordenada/100000000) - Math.floor(coordenada/100000000)) * 60)) * 100000000) *60/100000000 ;
		int minutosEntero = (int)minutos;
		String segs = segundos<10?"0"+segundos:segundos+"";
		String min = minutosEntero<10?"0"+minutosEntero:minutosEntero+"";
		coordenaConvertida = (int)grados+" "+min+" "+segs;
		return coordenaConvertida;
	 }

	 
	 public static CoordenadaDTO calcularCentro(ExtentDTO limites){
		 double difx=limites.getXmax()-limites.getXmin();
		 double dify=limites.getYmax()-limites.getYmin();
		 double centroX=limites.getXmin()+difx/2;
		 double centroY=limites.getYmin()+dify/2;
		 CoordenadaDTO centro = new CoordenadaDTO();
		 centro.setLongitud(centroX+""); //Se cambian las coordenadas al est√°ndar, Por eso se cruzan
		 centro.setLatitud(centroY+"");
		 return centro;
	 }

	 
	 /**
	  * MÔøΩtodo que obtendrÔøΩ una lista de posiciones vÔøΩlidas, ya que se pudieron haber recibido algunas posiciones con mensajes de error
	  */
	 public static List<PosicionDTO> coordenadasValidas(List<PosicionDTO> posiciones){
		 List<PosicionDTO> posicionesValidas=new ArrayList<PosicionDTO>();
		 for(PosicionDTO posicion : posiciones)
			 if(posicion.getError()==null)	posicionesValidas.add(posicion);
		 return posicionesValidas;
	 }
	 
	 public static List<PosicionDTO> coordenadasInvalidas(List<PosicionDTO> posiciones){
		 List<PosicionDTO> posicionesInvalidas=new ArrayList<PosicionDTO>();
		 for(PosicionDTO posicion : posiciones)
			 if(posicion.getError()!=null)	posicionesInvalidas.add(posicion);
		 return posicionesInvalidas;
	 }
	 
	 public static PropertySetProperty [] geocode(double latitud, double longitud, String propiedad, String radio){
		 
		 
		 try{
//		 String url = PropertyUtil.readProperty("LIMA_GEOCODE");
		 String url = PropertyUtil.readProperty(propiedad);
		 System.out.println(url);
		 GeocodeServerBindingStub geocodeServer=new GeocodeServerBindingStub(url);
		 PropertySet geocodePropSet = new PropertySet();
		 PropertySetProperty[] propArray = new PropertySetProperty[2];

		 PropertySetProperty geocodeProp = new PropertySetProperty();
		 geocodeProp.setKey("ReverseDistanceUnits");
		 geocodeProp.setValue("Meters");
		 propArray[0] = geocodeProp;

		 PropertySetProperty geocodeProp1 = new PropertySetProperty();
		 geocodeProp1.setKey("ReverseDistance");
//		 geocodeProp1.setValue("500");
		 geocodeProp1.setValue(radio);
		 propArray[1] = geocodeProp1;

		 geocodePropSet.setPropertyArray(propArray);
		 			
		 // Create point to reverse geocode
		 PointN inputPoint = new PointN();

		 inputPoint.setX(longitud);
		 inputPoint.setY(latitud);

		 // Point to reverse geocode must be in the same coordinate system as locator
		 SpatialReference resultsr = null;
		 			
		 Fields resultflds = geocodeServer.getResultFields(geocodePropSet);
		 			
		 for(int i = 0;i < resultflds.getFieldArray().length;i++){
		 	Field fld = resultflds.getFieldArray()[i];
		 	if(fld.getType().getValue().equals("esriFieldTypeGeometry")){
		 		resultsr = fld.getGeometryDef().getSpatialReference();
		 	}
		 }
		 	
		 inputPoint.setSpatialReference(resultsr);
		 			
		 // Reverse Geocode
		 PropertySet results = geocodeServer.reverseGeocode(inputPoint, false, geocodePropSet);
		 PropertySetProperty []  resultsArray = results.getPropertyArray();
		 
		 

		 return resultsArray;
		 }catch(Exception ex){
			 logger.info("ERROR obteniendo el geocoding de la URL : "+ propiedad);
			 return null;
		 }
		 
	 }
	 
	 public static DireccionDTO geocoding(double latitud, double longitud){
		 
		 DireccionDTO direccion = new DireccionDTO();
		 HashMap<String, String> valores = new HashMap<String, String>();
		 
		 PropertySetProperty [] resultsArray = geocode(latitud, longitud, "GEO_CALLES", "500");
		 
		 if(resultsArray!=null){
			 //Buscando Calles
			 for (int i = 0; i < resultsArray.length; i++) {
				 	PropertySetProperty result = resultsArray[i];
				 	String valor="";
				 	if(result.getValue()!=null)	valor=result.getValue().toString();
				 	valores.put(result.getKey().toString(), valor);
				 }
			 logger.info("Se encontró direccion en CALLES : "+valores.get("Calle")+", "+valores.get("Distrito")+", "+valores.get("Ubigeo"));
				 direccion.setDireccion(valores.get("Calle")); //Calle y su numero
				 direccion.setDistrito(valores.get("Distrito")); //Distrito
				 direccion.setUbigeo(valores.get("Ubigeo")); //Numero de UBIGEO

		 }else{
			 //Buscando Urbanizaciones
			 resultsArray = geocode(latitud, longitud, "GEO_URBANIZACIONES", "2000");
			 if(resultsArray!=null){
			 for (int i = 0; i < resultsArray.length; i++) {
				 	PropertySetProperty result = resultsArray[i];
				 	String valor="";
				 	if(result.getValue()!=null)	valor=result.getValue().toString();
				 	valores.put(result.getKey().toString(), valor);
				 }
			 logger.info("Se encontró direccion en URBANIZACIONES: "+valores.get("City"));
			 direccion.setDireccion(valores.get("City")); //Urbanizacion
			 direccion.setDistrito("-"); //Distrito
			 }else{
				 //Buscando Distritos
				 resultsArray = geocode(latitud, longitud, "GEO_DISTRITOS", "15000");
				 if(resultsArray!=null){
					 for (int i = 0; i < resultsArray.length; i++) {
						 	PropertySetProperty result = resultsArray[i];
						 	String valor="";
						 	if(result.getValue()!=null)	valor=result.getValue().toString();
						 	valores.put(result.getKey().toString(), valor);
						 }
					 logger.info("Se encontro direccion en DISTRITOS : "+valores.get("SingleKey"));
					 direccion.setDireccion(valores.get("SingleKey")); //Distrito
					 direccion.setDistrito(valores.get("SingleKey")); //Distrito
					 
				 }else{
					 logger.info("ERROR No se pudo obtener geocoding");
				 }
				 
			 }
		 }
		 return direccion;
	 }

	 
}
