<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="javascript/jquery-1.6.js" type="text/javascript"></script>
<title>::Localizador Satelital:: | Login</title>
</head>
<body>
<form action="loginSeguridad" method="POST">
	<p>Numero</p>
	<input name="numero" />
	<p id="labelPassword" style="display:none;">Password</p>
	<input type="password" name="password" style="display:none;" id="password" />
	<p>Tipo Usuario</p>
	<select id="tipo" name="tipo" onchange="mostrarPassword()">
		<option value="1">Monitor</option>
		<option value="2">Administrador Cuenta</option>
		<option value="0">Administrador NSN</option>
		<option value="3">Administrador CAL</option>
	</select>
	<button type="submit">Ingresar</button>
</form>
<div><p style="color:red;">${requestScope.mensaje}</p></div>
<script type="text/javascript">
$(function(){
		
});

function mostrarPassword(){
	if($('#tipo').attr('value')=='0' || $('#tipo').attr('value')=='3'){
		$('#labelPassword').show();
		$('#password').show();
	}else{
		$('#labelPassword').hide();
		$('#password').hide();
	}
}


	
</script>
</body>
</html>