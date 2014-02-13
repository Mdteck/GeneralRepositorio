<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../stylesheets/messi.css">
<link type="text/css" href="../stylesheets/sunny/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
<link rel="stylesheet" href="../stylesheets/jquery.ui.all.css">
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.core.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.widget.js" type="text/javascript"></script>
<script src="../javascript/ui-widget.js" type="text/javascript"></script>
<script src="../javascript/messi.js" type="text/javascript"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<title>::Localizador Satelital | Parametros Cuenta::</title>
</head>
<body>
<jsp:include page="../menu.jsp"/>
<jsp:include page="../scriptMenu.jsp"/>
<table width="88%" class="tablaPrincipal"  cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="../header.jsp"/> </td>
	</tr>
	<tr>
		<td valign="top">
			<h2>Parametros Cuenta</h2>
			<div id="formulario">
			<form id="usuario-form">
				<input type="hidden" id="idEntidad" name="idUsuario"/>
				<p><label>Hora Inicio (tracking / geocerca) (hh:mm, 24 horas)</label><input type="text" id="horaInicio" name="horaInicio" style="width: 50px" maxlength="5" /></p>
				<p><label>Hora Fin (tracking / geocerca) (hh:mm, 24 horas)</label><input type="text" id="horaFin" name="horaFin" style="width: 50px" maxlength="5" /></p>
				<p><button type="button" onclick="registrarHorario()">Guardar</button></p>
			</form>
			<p id="mensaje"></p>
			</div>
		</td>
	</tr>
</table>
<script type="text/javascript">
	$(function(){
		combobox('handset', function(){
			obtenerHorario('cuenta',localStorage.getItem('cuenta'));
		});
		
	});
</script>
</body>
</html>