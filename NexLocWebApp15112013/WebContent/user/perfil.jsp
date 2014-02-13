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
<title>::Localizador Satelital | Mi Perfil::</title>
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
			<h2>Modificar Perfil</h2>
			<div id="formulario">
			<form id="usuario-form">
				<input type="hidden" id="idEntidad" name="idUsuario"/>
				<p><label>Número</label><input type="text" id="numero" name="numero" readonly="readonly" /></p>
				<p><label>Etiqueta</label><input type="text" id="etiqueta" name="etiqueta" /></p>
				<p><label>Handset</label>
					<select class="tipoFiltroHandset" id="handset" name="handset.idHandset">
							<option value='-1'>Seleccione</option>
							
					</select>
				</p>
				<p><input type="checkbox" id="estadoHandset" name="estadoHandset" value="1"/> <label>Bloquear Equipo</label></p>
				<p><button type="button" onclick="perfilUsuario()">Guardar</button></p>
			</form>
			<p id="mensaje"></p>
			</div>
		</td>
	</tr>
</table>
<script type="text/javascript">
	$(function(){
		combobox('handset', function(){
			obtener('usuario',localStorage.getItem('usuario'));
		});
		
	});
</script>
</body>
</html>