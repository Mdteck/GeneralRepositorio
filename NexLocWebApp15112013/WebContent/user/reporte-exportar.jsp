<%@ page import="java.io.PrintWriter" pageEncoding="UTF-8" %>
<%@ page import="pe.com.nextel.action.*, java.util.List, java.util.ArrayList, pe.com.nextel.bean.PosicionDTO"%>
<%@ page contentType="application/excel" language="java" %>

<%
    response.reset();
    response.setHeader("Content-type","application/excel;charset=UTF-8");
    response.setHeader("Content-disposition","inline; filename=reporteLocalizacion.xls");

    PrintWriter op = response.getWriter();
    String fechaInicio = request.getParameter("txtFechaInicio");
    String fechaFin = request.getParameter("txtFechaFin");
    String tipo = request.getParameter("txtTipo");
    String valor = request.getParameter("txtValor");
    String idCuenta = request.getParameter("txtIdCuenta");
    
    String direccion ="";
    String distrito ="";
    String tecnologia ="";
    
    ReporteAction reporte = new ReporteAction();
    reporte.setFechaInicio(fechaInicio);
    reporte.setFechaFin(fechaFin);
    reporte.setTipo(tipo);
    reporte.setValor(valor);
    reporte.setIdCuenta(idCuenta);
    reporte.localizacion();
    
    List<PosicionDTO> posiciones = reporte.getPosiciones();  
    if(posiciones!=null){    
%>
<table>
	<tr><td>Monitor</td><td>Fecha</td><td>Usuario</td><td>Latitud</td><td>Longitud</td><td>Direccion</td><td>Distrito</td><td>Metodo</td><td>Tecnologia</td></tr>
	<% for(PosicionDTO po : posiciones){
		direccion = po.getDireccion().getDireccion() == null ? "-" : po.getDireccion().getDireccion();
		distrito = po.getDireccion().getDistrito() == null ? "-" : po.getDireccion().getDistrito();
		tecnologia = po.getTecnologia();
		if(tecnologia=="2G"){tecnologia="IDEN";}else{tecnologia="Nextel+";}%>
		<tr <% out.print(po.getIdPosicion()); %>>
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
	<% }
	if(posiciones.size()==0){%>
		<tr><td colspan="9">No se encontraron registros para los parametros ingresados</td></tr>	
	<%
	}
	%>
</table>
<%
}
%>