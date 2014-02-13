<%@page import="javax.management.monitor.Monitor"%>
<%@ page import="java.io.PrintWriter" pageEncoding="UTF-8" %>
<%@ page import="pe.com.nextel.action.*, java.util.List, java.util.ArrayList, pe.com.nextel.bean.*"%>
<%@ page contentType="application/excel" language="java" %>

<%
    response.reset();
    response.setHeader("Content-type","application/excel;charset=ISO-8859-1");
    response.setHeader("Content-disposition","inline; filename=reporteTrackingHistorial.xls");

    PrintWriter op = response.getWriter();
    String idMonitor = request.getParameter("txtMonitorId");
    String modo = request.getParameter("txtEstadoTracking");
    String fechaInicio = request.getParameter("txtFechaInicio");
    String fechaFin = request.getParameter("txtFechaFin");
    
    TrackingAction tracking = new TrackingAction();
 
    tracking.setModo(modo);
    tracking.setFechaInicial(fechaInicio);
    tracking.setFechaFin(fechaFin);
    tracking.setIdUsuario(Integer.parseInt(idMonitor));
    tracking.filtrar();    
    
    List<TrackingDTO> trackings = tracking.getTrackings();
      
    if(trackings!=null){    
%>
<table>
	<tr><td>Etiqueta</td><td>Número</td><td>Fecha Inicio</td><td>Hora Inicio</td><td>Fecha Fin</td><td>Hora Fin</td><td>Estado</td></tr>
	<% for(TrackingDTO tra : trackings){
		String strEstado = "";
		int estado = tra.getEstado();
		if (estado == 0)
			strEstado = "Cancelado";
		else if (estado == 2)
			strEstado = "Iniciado";
		else if (estado == 3)
			strEstado = "Terminado";
		%>
		<tr>
			<td><% out.print(tra.getMonitoreado().getEtiqueta()); %></td>
			<td><% out.print(tra.getMonitoreado().getNumero()); %></td>
			<td><% out.print(tra.getHorario().getFechaInicio()); %></td>
			<td><% out.print(tra.getHorario().getHoraInicio()); %></td>
			<td><% out.print(tra.getHorario().getFechaFin()); %></td>
			<td><% out.print(tra.getHorario().getHoraFin()); %></td>
			<td><% out.print(strEstado); %></td>			
		</tr>
	<% }
	if(trackings.size()==0){%>
		<tr><td colspan="7">No se encontro ningun registro para esta cuenta.</td></tr>	
	<%
	}
	%>
</table>
<%
}
%>