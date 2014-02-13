<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<% String path = request.getContextPath(); 
	%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="refresh" content="3;url=<%= path %>/user/index.jsp" >
<link href="<%= path %>/stylesheets/style.css" rel="stylesheet" media="all" />
<title>::NexLoc:: | Error</title>
</head>
<body>
<table width="85%" align="center">
	<tr>
		<td height="90px"><jsp:include page="header.jsp"/> </td>
	</tr>
	<tr>
		<td>		
		<h2>Error</h2>
		<p>La direcci&oacute;n especificada no existe.</p>
		</td>
	</tr>
</table>
</body>
</html>