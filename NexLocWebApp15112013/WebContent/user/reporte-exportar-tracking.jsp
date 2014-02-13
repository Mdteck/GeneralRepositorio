<%@ page import="java.io.PrintWriter" pageEncoding="UTF-8" %>
<%@ page import="pe.com.nextel.action.*, java.util.List, java.util.ArrayList, pe.com.nextel.bean.*"%>
<%@ page contentType="application/excel" language="java" %>

<%
    response.reset();
    response.setHeader("Content-type","application/excel;charset=ISO-8859-1");
    response.setHeader("Content-disposition","inline; filename=reporteTracking.xls");

    PrintWriter op = response.getWriter();
    String fechaInicio = request.getParameter("txtFechaInicioF");
    String fechaFin = request.getParameter("txtFechaFinF");
    String horaInicio = request.getParameter("txtHoraInicioF");
    String horaFin = request.getParameter("txtHoraFinF");
    String metodo = request.getParameter("txtMetodoF");
    String idTracking = request.getParameter("txtIdTrackingF");
    
    String direccion ="";
    String distrito ="";
    String tecnologia ="";
    
    TrackingAction tracking = new TrackingAction();
    TrackingDTO trackingDTO = new TrackingDTO();
    HorarioDTO horarioSolo = new HorarioDTO();
    
    trackingDTO.setIdTracking(idTracking);    
    horarioSolo.setHoraInicio(horaInicio);
    horarioSolo.setHoraFin(horaFin);
    tracking.setFechaInicial(fechaInicio);
    tracking.setFechaFin(fechaFin);
    tracking.setModo(metodo);
    tracking.setTracking(trackingDTO);
    tracking.setHorarioSolo(horarioSolo);
    tracking.obtenerFiltros();    
    
    List<PosicionDTO> posiciones = tracking.getTracking().getPosiciones();  
if(posiciones!=null){
	int i=1;
%>
<table>
	<tr><td>N&deg;</td><td>Monitor</td><td>Fecha</td><td>Usuario</td><td>Latitud</td><td>Longitud</td><td>Direccion</td><td>Distrito</td><td>Metodo</td><td>Tecnologia</td></tr>
	<% for(PosicionDTO po : posiciones){
		direccion = po.getDireccion().getDireccion() == null ? "-" : po.getDireccion().getDireccion();
		distrito = po.getDireccion().getDistrito() == null ? "-" : po.getDireccion().getDistrito();
		tecnologia = po.getTecnologia();
		if(tecnologia=="2G"){tecnologia="IDEN";}else{tecnologia="Nextel+";}%>
		<tr <% out.print(po.getIdPosicion()); %>>
			<td><% out.print(i);%></td>
			<td><% out.print(po.getMonitor().getNumero()); %></td>
			<td><% out.print(po.getTime().substring(0,16)); %></td>
			<td><% out.print(po.getUsuario().getNumero()); %></td>
			<td><% out.print(po.getCoordenada().getLatitud()); %></td>
			<td><% out.print(po.getCoordenada().getLongitud()); %></td>
			<td><% out.print(direccion); %></td>
			<td><% out.print(distrito); %></td>
			<td><% out.print(po.getMetodo()); %></td>
			<td><% out.print(tecnologia); %></td>
		</tr>
	<%
		i++;
	}
	if(posiciones.size()==0){%>
		<tr><td colspan="9">No se encontraron registros para los parametros ingresados</td></tr>	
	<%	
	}
	%>
</table>
<%
}
%>
  