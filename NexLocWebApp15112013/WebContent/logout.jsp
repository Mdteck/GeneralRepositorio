<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>::NexLoc:: | Salir</title>
<script src="javascript/jquery-1.6.js" type="text/javascript"></script>
</head>
<body>
<div><p>${requestScope.mensaje}</p>
<p><a href="#" onclick="cerrarVentana();">Cerrar</a></p></div>

<script type="text/javascript">
	function cerrarVentana() {
		window.opener = self
		window.close()
	}
	$(function(){
		cerrarVentana();
	});
</script>
</body>
</html>