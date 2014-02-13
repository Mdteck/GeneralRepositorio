<%@page import="javax.management.monitor.Monitor"%>
<%@ page import="java.io.PrintWriter" pageEncoding="UTF-8" %>
<%@ page import="pe.com.nextel.action.*, java.util.List, java.util.ArrayList, pe.com.nextel.bean.*"%>
<%@ page contentType="application/excel" language="java" %>

<%
    response.reset();
    response.setHeader("Content-type","application/excel;charset=ISO-8859-1");
    response.setHeader("Content-disposition","inline; filename=reportePuntosInteres.xls");

    PrintWriter op = response.getWriter();
    String idCuenta = request.getParameter("txtIdCuenta");
    String showPrivados = request.getParameter("txtShowPrivados");
    String idUsuario = request.getParameter("txtIdUsuario");
    
    PuntoInteresAction punto = new PuntoInteresAction();
    punto.setIdCuenta(idCuenta);
    if(!idUsuario.equals("")){
    	punto.setIdUsuario(idUsuario);
    }
    punto.setShowPrivados(showPrivados);    
    punto.listar();
    
    List<PuntoInteresDTO> puntoInteres = punto.getPuntosInteres();
      
    if(puntoInteres!=null){    	
%>
<table>
	<tr><td>Fecha Creacion</td><td>Nombre</td><td>Creado por</td><td>Direccion</td><td>Distrito</td></tr>
	<% for(PuntoInteresDTO pu : puntoInteres){
			String[] fechaRegistroSplit = pu.getFechaRegistro().split("-");				
			String fechaRegistro = fechaRegistroSplit[2].substring(0, 2)+ "/"+ fechaRegistroSplit[1]+ "/" + fechaRegistroSplit[0];
		%>
		<tr>
			<td><% out.print(fechaRegistro); %></td>
			<td><% out.print(pu.getNombre()); %></td>
			<td><% out.print(pu.getUsuario().getEtiqueta()+" - "+pu.getUsuario().getNumero()); %></td>
			<td><% out.print(pu.getDireccion()); %></td>
			<td><% out.print(pu.getDistrito()); %></td>						
		</tr>
	<% }
	if(puntoInteres.size()==0){%>
		<tr><td colspan="5">No se encontro ningun punto de interes para esta cuenta.</td></tr>	
	<%
	}
	%>
</table>
<%
}
%>