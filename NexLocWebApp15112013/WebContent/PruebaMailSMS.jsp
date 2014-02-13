<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="pe.com.nextel.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Pruebas</title>
</head>
<body>
<% 
String respuesta="";
if(request.getParameter("accion")!=null){
	String accion=(String)request.getParameter("accion");
	if(accion.equals("MAIL")){
		String destinatario=(String)request.getParameter("destinatario");
		String subject=(String)request.getParameter("subject");
		String body=(String)request.getParameter("body");	
		respuesta = MailUtil.enviarMailContext(destinatario, subject, body)+"";
	}else{
		String numero=(String)request.getParameter("numero");
		String mensaje=(String)request.getParameter("mensaje");
		respuesta = SmsUtil.sendSMS(numero, mensaje)+"";
	}
}
%>

<form action="PruebaMailSMS.jsp" method="POST">
<input type="hidden" name="accion" value="MAIL"/>
Destinatario<input type="text" name="destinatario"/>
Subject<input type="text" name="subject" />
Body<input type="text" name="body" />
<button type="Submit">Enviar Mail</button>
</form>
<br/>
<form action="PruebaMailSMS.jsp" method="POST">
<input type="hidden" name="accion" value="SMS"/>
Numero<input type="text" name="numero"/>
Mensaje<input type="text" name="mensaje" />
<button type="Submit">Enviar Mail</button>
</form>
<br/>
<%= respuesta %>
</body>
</html>