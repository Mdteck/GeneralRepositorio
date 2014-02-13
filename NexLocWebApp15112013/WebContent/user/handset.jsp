<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<link type="text/css" href="../stylesheets/sunny/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
<link rel="stylesheet" href="../stylesheets/jquery.ui.all.css">
<link rel="stylesheet" href="../stylesheets/demos.css">
<link rel="stylesheet" href="../stylesheets/messi.css">
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script type="text/javascript" language="javascript" src="../javascript/jquery.dataTables.min.js"></script>
<script src="../javascript/jquery.ui.core.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.widget.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="../javascript/ui-widget.js" type="text/javascript"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<script src="../javascript/messi.js" type="text/javascript"></script>
<link href="../stylesheets/jquery.dataTables.css" rel="stylesheet" media="all" />
<title>::Localizador Satelital | Handset::</title>
</head>
<body>
<div id="dialog" title="¿Desea Continuar?" class="hidden">
	<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Se eliminará el siguiente handset.</p>
</div>
<jsp:include page="../menu.jsp"/>
<jsp:include page="../scriptMenu.jsp"/>
<table width="88%" class="tablaPrincipal"  cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="../header.jsp"/> </td>
	</tr>
	<tr>
		<td valign="top">
			<h2>Handsets</h2>
			<div id="lista-handset">
			<p class="right"><button type="button" onclick="ver('handset')">Registrar</button></p>
			<div class="clear"></div>
				<table class="formato-tabla" cellspacing="0">
					<thead>
						<tr>
							<td>Modelo</td>
							<td>Plataforma</td>
							<td>Versión de la Aplicación</td>
							<td>Soporte de Imágenes</td>
							<td>Opciones</td>
						</tr>
					</thead>
					<tbody id="data-handset"></tbody>
				</table>
			
			</div>
			<div id="modificar-handset" class="hidden">
				<form id="handset-form">
				<input type="hidden" name="idHandset" id="idhandset" value="" />
					<p><label>Modelo</label><input type="text" name="modelo" id="modelo"></p>
					<p><label>Plataforma</label>
						<select name="plataforma" id="plataforma">
							<option value="0">Elegir</option>
							<option value="1">IDEN</option>
							<option value="2">J2ME</option>
							<option value="3">Blackberry</option>
							<option value="4">Android</option>
						</select>
					</p>
					<p><label>Versión de la Aplicación</label>
						<select name="versionAplicacion" id="versionAplicacion">
							<option value="0">Sms</option>
							<option value="1">Sms e imagen</option>
						</select>	
					<p><label>Soporte de Imágenes</label>
						<select name="imagen" id="imagen">
							<option value="0">No soporta</option>
							<option value="1">Sí soporta</option>
						</select>	
					</p>
					<p><button type="button" onclick="javascript: window.location='handset.jsp'">Regresar</button><button type="button" onclick="modificar('handset')">Modificar</button></p>
				</form>
			</div>
			<div id="registrar-handset" class="hidden">
				<form id="handset-form">
					<p><label>Modelo</label><input type="text" name="modelo" id="modelo"></p>
					<p><label>Plataforma</label>
						<select name="plataforma" id="plataforma">
							<option value="0">Elegir</option>
							<option value="1">IDEN</option>
							<option value="2">J2ME</option>
							<option value="3">Blackberry</option>
							<option value="4">Android</option>
						</select>
					</p>
					<p><label>Versión de la Aplicación</label>
						<select name="versionAplicacion" id="versionAplicacion">
							<option value="0">SMS</option>
							<option value="1">SMS e Imagen</option>
						</select>	
					</p>
					<p><label>Soporte de Imágenes</label>
						<select name="imagen" id="imagen">
							<option value="0">No soporta</option>
							<option value="1">Sí soporta</option>
						</select>	
					</p>
					<p><button type="button" onclick="javascript: window.location='handset.jsp'">Regresar</button><button type="button" onclick="registrar('handset')">Registrar</button></p>
				</form>
			</div>
			<p align="center" id="mensaje"></p>
		</td>
	</tr>
</table>
<script type="text/javascript">
	$(function(){
		listar('handset');
	});
</script>
</body>
</html>