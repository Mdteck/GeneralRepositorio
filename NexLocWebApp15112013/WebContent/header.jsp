<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<body>
<div id="header">
	<% String path = request.getContextPath();
	   String t = session.getAttribute("tipo").toString();
	   String tipo = "";
	   if(t.equals("0"))		tipo="Administrador NSN";
	   else if(t.equals("1"))	tipo="Administrador";
	   else if(t.equals("2"))	tipo="Monitor";
	   else if(t.equals("3"))	tipo="Administrador CAL";
	%>	
	<table align="right" cellspacing="10">
		<tr align="right">
			<td>Bienvenido <b>${sessionScope.usuario } ( <%= tipo %> )</b></td>
		</tr>
		<tr align="right">
			<td>
				<ul id="menu-head">
					<li><a href="<%= path %>/user/index.jsp">Inicio</a></li>
					<li><a href="<%= path %>/user/perfil.jsp">Mi perfil</a></li>
					<li><a href="<%= path %>/logoutSeguridad">Salir</a></li>
				</ul>
			</td>
		</tr>
	</table>
</div>
</body>
<script type="text/javascript">
$(function(){
	if (localStorage.getItem('usuario') != '${sessionScope.idUsuario}') {
		location.reload();
	}
	$('#menu').hover(
	  	function () {
	      	$($(this)).stop().animate({'marginLeft':'-2px'},400);
	    },
	    function () {
	      	$($(this)).stop().animate({'marginLeft':'-150px'},400);
	    }
	);  
});
</script>
</html>