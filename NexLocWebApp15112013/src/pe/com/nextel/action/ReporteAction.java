package pe.com.nextel.action;

import java.util.ArrayList;
import java.util.List;

//import org.apache.log4j.Logger;
import java.util.logging.Logger;

import pe.com.nextel.bean.BloqueadosDTO;
import pe.com.nextel.bean.GeocercaDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.ReporteTransaccionDTO;
import pe.com.nextel.service.BusinessDelegate;
import pe.com.nextel.service.ReporteService;
import pe.com.nextel.util.LoggerUtil;

public class ReporteAction {
	
	
	//********************** Variables *************************//
	private List<GeocercaDTO> geocercas;
	private List<PosicionDTO> posiciones;
	private List<BloqueadosDTO> bloqueados;
	private String listarPorUsuario;
	private PosicionDTO posicion;
	private GeocercaDTO geocerca;
	private String fechaInicio;
	private String fechaFin;
	private String tipo;
	private String valor;
	private String numero;
	private String etiqueta;
	private String tipoRpta;														//Tipo de Mensaje que se le devuelve a la interfaz (OK o ERROR)
	private String mensaje;
	private String idCuenta;
	private String tecnologia;
	private String minutos;
	private List<ReporteTransaccionDTO> reportes;
	private String fecha;
	private String hora;
	private String minuto;
	private String mes;
	private String anio;
	private String dia;
	private String origen;
	
	
	//Mensaje que se le devuelve a la interfaz
	ReporteService reporteService=BusinessDelegate.getReporteService();				//Instancia del servicio de Categorias para acceder a la BD
	Logger logger = LoggerUtil.getInstance();
//	Logger logger = Logger.getLogger(ReporteAction.class.getName());
	
	//********************** Setter & Getter *************************//
	
	public List<PosicionDTO> getPosiciones() {
		return posiciones;
	}


	public String getMinuto() {
		return minuto;
	}


	public void setMinuto(String minuto) {
		this.minuto = minuto;
	}


	public List<ReporteTransaccionDTO> getReportes() {
		return reportes;
	}


	public void setReportes(List<ReporteTransaccionDTO> reportes) {
		this.reportes = reportes;
	}


	public String getFecha() {
		return fecha;
	}


	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	public String getHora() {
		return hora;
	}


	public void setHora(String hora) {
		this.hora = hora;
	}


	public String getMes() {
		return mes;
	}


	public void setMes(String mes) {
		this.mes = mes;
	}


	public String getAnio() {
		return anio;
	}


	public void setAnio(String anio) {
		this.anio = anio;
	}


	public String getDia() {
		return dia;
	}


	public void setDia(String dia) {
		this.dia = dia;
	}


	public String getOrigen() {
		return origen;
	}


	public void setOrigen(String origen) {
		this.origen = origen;
	}


	public String getTecnologia() {
		return tecnologia;
	}

	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}

	public String getMinutos() {
		return minutos;
	}

	public void setMinutos(String minutos) {
		this.minutos = minutos;
	}

	public GeocercaDTO getGeocerca() {
		return geocerca;
	}
	public void setGeocerca(GeocercaDTO geocerca) {
		this.geocerca = geocerca;
	}
	public String getListarPorUsuario() {
		return listarPorUsuario;
	}
	public void setListarPorUsuario(String listarPorUsuario) {
		this.listarPorUsuario = listarPorUsuario;
	}
	public List<GeocercaDTO> getGeocercas() {
		return geocercas;
	}
	public void setGeocercas(List<GeocercaDTO> geocercas) {
		this.geocercas = geocercas;
	}
	public void setPosiciones(List<PosicionDTO> posiciones) {
		this.posiciones = posiciones;
	}
	public PosicionDTO getPosicion() {
		return posicion;
	}
	public void setPosicion(PosicionDTO posicion) {
		this.posicion = posicion;
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getTipoRpta() {
		return tipoRpta;
	}
	public void setTipoRpta(String tipoRpta) {
		this.tipoRpta = tipoRpta;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public List<BloqueadosDTO> getBloqueados() {
		return bloqueados;
	}
	public void setBloqueados(List<BloqueadosDTO> bloqueados) {
		this.bloqueados = bloqueados;
	}
	
	public String getIdCuenta() {
		return idCuenta;
	}
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}
	
	
	//********************** Metodos *************************//
	
	public String localizacion(){
		logger.info("INICIO ReporteLocalizacion");
		String fechaInicio=getFechaInicio() + " 0:00:00";
		String fechaFin=getFechaFin() + " 23:59:59";
		int tipo=Integer.parseInt(getTipo());
		int valor =  -1;
		if (!getValor().equals("null")) valor = Integer.parseInt(getValor());

		posiciones=reporteService.localizacion(fechaInicio, fechaFin, valor, tipo, Integer.parseInt(idCuenta));
		if(posiciones!=null){
			setTipoRpta("OK");
			logger.info("OK Obteniendo reporte de localizaciones");
		}else{
			setTipoRpta("ERROR");
			setMensaje("Hubo un error realizando el reporte");
			logger.info("Error obteniendo reporte de localizaciones");
//			logger.error("Error obteniendo reporte de localizaciones");
		}
		logger.info("FIN REporteLocalizacion");
		return "localizacion";
	}
	
	public String geocerca(){
		logger.info("INICIO ReporteGeocerca");
		if(getListarPorUsuario().equals("true")){
			int id = Integer.parseInt(getGeocerca().getUsuario().getIdUsuario());
			setGeocercas(reporteService.geocerca(id, true));
		}else{
			int id = Integer.parseInt(getGeocerca().getUsuario().getCuenta().getIdCuenta());
			setGeocercas(reporteService.geocerca(id, false));
		}
		if(getGeocercas()==null){
			setTipoRpta("ERROR");
			logger.info("Error obteniendo reporte de geocerca");
//			logger.error("Error obteniendo reporte de geocerca");
		}else{
			logger.info("OK Obteniendo reporte de geocerca");
			setTipoRpta("OK");
		}
		logger.info("FIN ReporteGeocerca");
		return "geocerca";
	}
	
	//Aumentado para el reporte de bloqueados
	public String bloqueados(){
		logger.info("INICIO ReporteBloqueados");
		String numero = getNumero();
		String etiqueta = getEtiqueta();
		
		setBloqueados(reporteService.bloqueados(getGeocerca().getUsuario().getCuenta().getIdCuenta(), numero, etiqueta));
		
		if (bloqueados == null){
//			logger.error("Error obteniendo reporte de usuarios bloqueados");
			logger.info("Error obteniendo reporte de usuarios bloqueados");
			setTipoRpta("ERROR");
		}else{
			logger.info("OK Obteniendo reporte de usuarios bloqueados");
			setTipoRpta("OK");
		}
		logger.info("FIN ReporteBloqueado");
		return "bloqueados";
	}
	

	public String transacciones(){
		logger.info("INICIO ReporteTransaccion");
		String origen = getOrigen();
		String tecnologia = getTecnologia();
		String tipo = getTipo();
		logger.info("ORIGEN "+origen);
		List<ReporteTransaccionDTO> reportes=new ArrayList<ReporteTransaccionDTO>();
		
		if(tipo.equals("hora")){
			String fecha = getFecha();
			String hora = getHora();
			Object [] values = {fecha, hora};
			String [] campos = {"Fecha", "Hora"};
			boolean isOk = validar(values,campos);
			if(!isOk)	return "transacciones";
			ReporteTransaccionDTO reporte = reporteService.transaccionesPorHora(tecnologia, fecha, hora, origen);
			if(reporte==null){
				setTipoRpta("ERROR");
				setMensaje("Ocurrio un error obteniendo el reporte");
				logger.info("Ocurrio un error obteniendo el reporte de transacciones por hora");
				return "transacciones";
			}else{
				if(hora.length()==1) hora="0"+hora;
				reporte.setIdentificador(fecha+" "+hora+":00");
				reportes.add(reporte);
			}

		}else if(tipo.equals("fecha")){
			String fechaInicio = getFechaInicio();
			String fechaFin = getFechaFin();
			Object [] values = {fechaInicio, fechaFin};
			String [] campos = {"Fecha Inicio", "Fecha Fin"};
			boolean isOk=validar(values, campos);
			if(!isOk) return "transacciones";
			ReporteTransaccionDTO reporte = reporteService.transacciones(tecnologia, fechaInicio, fechaFin, origen);
			if(reporte==null){
				setTipoRpta("ERROR");
				setMensaje("Ocurrio un error obteniendo el reporte");
				logger.info("Ocurrio un error obteniendo el reporte de transacciones por fecha");
				return "transacciones";
			}else{
				reporte.setIdentificador(fechaInicio+" - "+fechaFin);
				reportes.add(reporte);
			}
		}else if(tipo.equals("dia")){
			String dia = getDia();
			Object [] values = {dia};
			String [] campos = {"Dia"};
			boolean isOk = validar(values, campos);
			if(!isOk) return "transacciones";
			reportes = reporteService.transaccionesPorDia(tecnologia, dia, origen);
			if(reportes==null){
				setTipoRpta("ERROR");
				setMensaje("Ocurrio un error obteniendo el reporte");
				logger.info("Ocurrio un error obteniendo el reporte de transacciones por dia");
				return "transacciones";
			}
		}else if(tipo.equals("mes")){
			String mes = getMes();
			Object [] values = {mes};
			String [] campos = {"Mes"};
			boolean isOk = validar(values, campos);
			if(!isOk) return "transacciones";
			reportes = reporteService.transaccionesPorMes(tecnologia, mes, origen);
			
		}else if(tipo.equals("anio")){
			String anio = getAnio();
			Object [] values = {anio};
			String [] campos = {"A–o"};
			boolean isOk = validar(values, campos);
			if(!isOk) return "transacciones";
			reportes = reporteService.transaccionesPorAnio(tecnologia, anio, origen);

		}else{
			setTipoRpta("ERROR");
			setMensaje("Por favor, seleccione una opcion de reporte valida");
			logger.info("Se selecciono una opcion de reporte invalida");
			logger.info("FIN ReporteTransaccion");
			return "transacciones";
		}
		setTipoRpta("OK");
		setReportes(reportes);
		logger.info("FIN ReporteTransaccion");
		return "transacciones";
	}
	
	public String mlp(){
		logger.info("INICIO ReporteTransaccion");
		String origen = getOrigen();
		String tecnologia = getTecnologia();
		String tipo = getTipo();
		logger.info("ORIGEN "+origen);
		List<ReporteTransaccionDTO> reportes=new ArrayList<ReporteTransaccionDTO>();
		
		if(tipo.equals("horas")){
			String dia = getFecha();
			Object [] values = {dia};
			String [] campos = {"Dia"};
			boolean isOk = validar(values, campos);
			if(!isOk) return "transacciones";
			reportes = reporteService.transaccionesPorDia(tecnologia, dia, origen);
			if(reportes==null){
				setTipoRpta("ERROR");
				setMensaje("Ocurrio un error obteniendo el reporte");
				logger.info("Ocurrio un error obteniendo el reporte de MLP por horas");
				return "transacciones";
			}
		}else if(tipo.equals("minutos")){
			String dia = getFecha();
			String hora = getHora();
			Object [] values = {dia, hora};
			String [] campos = {"Dia", "Hora"};
			boolean isOk = validar(values, campos);
			if(!isOk) return "transacciones";
			reportes = reporteService.transaccionesPorMinutos(tecnologia, dia, hora, origen);
			if(reportes==null){
				setTipoRpta("ERROR");
				setMensaje("Ocurrio un error obteniendo el reporte");
				logger.info("Ocurrio un error obteniendo el reporte de MLP por minutos");
				return "transacciones";
			}
		}else if(tipo.equals("segundos")){
			String dia = getFecha();
			String hora = getHora();
			String minuto = getMinuto();
			Object [] values = {dia, hora, minuto};
			String [] campos = {"Dia", "Hora", "Minuto"};
			boolean isOk = validar(values, campos);
			if(!isOk) return "transacciones";
			reportes = reporteService.transaccionesPorSegundos(tecnologia, dia, hora+":"+minuto ,origen);
			if(reportes==null){
				setTipoRpta("ERROR");
				setMensaje("Ocurrio un error obteniendo el reporte");
				logger.info("Ocurrio un error obteniendo el reporte de MLP por segundos");
				return "transacciones";
			}
		}
		
		setTipoRpta("OK");
		setReportes(reportes);
		logger.info("FIN ReporteTransaccion");
		return "mlp";
	}
	
	public boolean validar(Object [] values, String [] campos){	
		for(int i =0; i<campos.length; i++){
			boolean isOk=false;
			Object value = values[i];
			String campo = campos[i];
			if(value!=null){
				if(value instanceof Integer){
					int val = Integer.parseInt(value.toString());
					if(val!=0) isOk=true;
				}else if(value instanceof String){
					String val = value.toString();
					if(!val.isEmpty()) isOk=true;
				}
			}
			if(!isOk){
				setTipoRpta("ERROR");
				setMensaje(String.format("Por favor, complete el campo %s",campo));
				setMensaje(mensaje);
				return false;
			}
		}
		return true;
	}
	
}
