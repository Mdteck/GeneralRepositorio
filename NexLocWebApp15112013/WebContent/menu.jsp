<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- <jsp:include page="scriptMenu.jsp"/> --%>
<html>
<head>
	<% String tipo = session.getAttribute("tipo").toString(); 
	   String path = request.getContextPath(); 
	%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%= path %>/stylesheets/style.css" rel="stylesheet" media="all" />
</head>
<body>
<ul id="menu">	
	<li><span><div class="localizacion"></div>Localización</span>
		<ul class="submenu">		
			<li><a href="<%=path %>/user/localizacion-handset.jsp">Equipo</a></li>
			<li><a href="<%=path %>/user/localizacion-grupo.jsp">Grupos</a></li>
		</ul>
	</li>
	<li><span><div class="puntosInteres"></div>Puntos de Interés</span>
		<ul class="submenu">
			<li><a href="<%=path %>/user/puntosInteres-listar.jsp">Mis Puntos de Interés</a></li>
			<li><a href="<%=path %>/user/puntosInteres-registrar.jsp">Registrar</a></li>
 			<li><a href="<%=path %>/user/puntosInteres-buscar.jsp">Buscar</a></li>
		</ul>
	</li>
	<li><span><div class="geocercas"></div>Geocercas</span>
		<ul class="submenu">
			<li><a href="<%=path %>/user/geocercas-listar.jsp">Mis Geocercas</a></li>
			<li><a href="<%=path %>/user/geocercas-registrar.jsp">Registrar</a></li>
		</ul>
	</li>
	<li><span><div class="tracking"></div>Tracking</span>
		<ul class="submenu">
			<li><a href="<%=path %>/user/tracking.jsp">Listado en Ejecucion</a></li>
			<li><a href="<%=path %>/user/tracking-historial.jsp">Historial</a></li>
		</ul>
	</li>	
	<li><span><div class="usuarios"></div>Administración</span>
		<ul class="submenu">
			<% if(!tipo.equals("2")){ %>
			<li><a href="<%=path %>/user/usuarios.jsp">Usuarios</a></li>
			<%} %>
			<li><a href="<%=path %>/user/grupos.jsp">Grupos</a></li>
			<% if(tipo.equals("0") || tipo.equals("3")){ %>
			<li><a href="<%=path %>/user/categorias.jsp">Categorias</a></li>
			<% } %>
			<% if(tipo.equals("1")){ %>
			<li><a href="<%=path %>/user/cuenta-parametros.jsp">Parametros</a></li>
			<% } %>
			<% if(tipo.equals("0") || tipo.equals("3")){ %>
			<li><a href="<%=path %>/user/handset.jsp">Handsets</a></li>
			<% } %>			
		</ul>
	</li>	
	<li><span><div class="reportes"></div>Reportes</span>
	<ul class="submenu">
			<li><a href="<%=path %>/user/reporte-localizacion.jsp">Localización</a></li>		
	<% if(tipo.equals("1") || tipo.equals("0") || tipo.equals("3")){ %>	
			<li><a href="<%=path %>/user/reporte-puntosInteres.jsp">Puntos Interés</a></li>
			<li><a href="<%=path %>/user/reporte-bloqueados.jsp">Bloqueos / Desbloqueos</a></li>
	<% } %>	
			<li><a href="<%=path %>/user/reporte-geocercas.jsp">Geocercas</a></li>
	<% if(tipo.equals("3")){%>
			<li><a href="<%=path %>/user/reporte-transacciones.jsp">Transacciones</a></li>  
			<li><a href="<%=path %>/user/reporte-mlp.jsp">Detalle MLP</a></li>
	<% }%>
		</ul>
	</li>
	
</ul>
<!-- Inicio 26/06/2013 MDTECK Sandy Huanco -->
<script type="text/javascript">
	$(function() {		
//     	$('#menu').stop().animate({'marginLeft':'-150px'},100);    	
//         $('#menu').hover(
// 	        function () {
// 	        	$($(this)).stop().animate({'marginLeft':'-2px'},400);
// 	        },
// 	        function () {
// 	        	$($(this)).stop().animate({'marginLeft':'-150px'},400);
// 	        }
// 	   	);
	});
</script>
<!-- Fin -->
</body>
</html>