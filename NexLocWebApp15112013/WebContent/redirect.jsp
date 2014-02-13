<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>::Localizador Satelital::</title>
<link href="stylesheets/style.css" rel="stylesheet" media="all" />
<style type="text/css">
h1 {color: #E05206}
h3 {color: #E05206}
</style>
<script src="javascript/jquery-1.6.js" type="text/javascript"></script>
<script src="javascript/view.js" type="text/javascript"></script>
</head>
<body>
<table width="85%" align="center">
	<tr>
		<td width="25%"><img width="50%"  src="images/popup2.png"/></td>
		<td width="75%">Este 23 de Julio, renovamos nuestro servicio de Localizador Satelital Nextel con nuestro nuevo servicio Localizador Satelital +.
Este servicio contará con nuevas interfaces de usuario y más ventajas que próximamente daremos a conocer.
		</td>
	</tr>
	<tr><td colspan="2"><br/><br/></td></tr>
	<tr>
		<td align="center" colspan="2">Nueva interface de consulta de ubicaciones de usuarios o grupos</td>
	</tr>
	<tr>
		<td align="center" colspan="2"><img src="images/popup1.png"/></td>
	</tr>
</table>
<% 
	String form = (String) request.getParameter("form");
	if(form.equals("nueva")){
	String numero = (String) request.getParameter("numero");
	String tipo = (String) request.getParameter("tipo");
%>
<form name="formNexLocWebApp" action="http://186.160.40.201:8080/NexLocWebApp/loginSeguridad" method="POST">
<input type="hidden" name="numero" type="hidden" value="<%=numero %>" />
<input type="hidden" name="tipo" type="hidden" value="<%=tipo %>" />
</form>
<% }else{ 
	String authid = (String) request.getParameter("authid");
	String tipo = (String) request.getParameter("tipo");
%>
<form name="formNexLocWebApp" action="http://agps.conexiondirecta.com.pe/LocalizadorSatelital/ServletAuth" method="POST">
<input type="hidden" name="authid" type="hidden" value="<%=authid %>" />
<input type="hidden" name="tipo" type="hidden" value="<%=tipo %>" />
</form>
<%} %>
</body>
<script>
setTimeout(function() { 
    document.formNexLocWebApp.submit();
 }, 10000);
</script>
</html>