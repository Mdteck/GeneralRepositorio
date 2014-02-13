<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../stylesheets/messi.css">
<link type="text/css" href="../stylesheets/sunny/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
<link rel="stylesheet" href="../stylesheets/jquery.ui.all.css">
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script type="text/javascript" language="javascript" src="../javascript/jquery.dataTables.min.js"></script>
<script src="../javascript/jquery.ui.core.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.widget.js" type="text/javascript"></script>
<script src="../javascript/ui-widget.js" type="text/javascript"></script>
<script src="../javascript/tooltip.js" type="text/javascript"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<script src="../javascript/messi.js" type="text/javascript"></script>
<link href="../stylesheets/jquery.dataTables.css" rel="stylesheet" media="all" />
<script src="../javascript/ui.multiselect.js" type="text/javascript"></script>
<link href="../stylesheets/ui.multiselect.css" rel="stylesheet" media="all" />
<title>::Localizador Satelital | Usuarios::</title>
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
			<h2>Usuarios</h2>
			<div id="list-usuario">
				<div id="filtro-usuario" class="margen">
					<div class="left">
						<b>Filtrar por</b>
						<select id="tipo_usuarios" name="tipoFiltro">
							<option value="-1" selected="selected">Todos</option>
							<option value="2">Etiqueta</option>
							<option value="3">Número</option>
						</select>
						<b>Filtro</b>
						<input type="text" name="filtro" id="filtro"/>
						<button  type="button"  onclick="filtrar('usuario')">Filtrar</button>
						
					</div>
					<div class="clear"></div>
				</div>
				<br/>
				<table class="formato-tabla" cellspacing="0" id="tabla">
					<thead>
						<tr>
							<td>Etiqueta</td>
							<td>Número</td>
							<td>Tipo</td>
							<td>Opciones</td>
						</tr>
					</thead>
					<tbody id="data-usuario"></tbody>
				</table>	
			</div>
			<!--Modificar de Usuarios -->
			<div id="form-usuario" class="hidden">
				<form id="usuario-form">
					<input type="hidden" id="idEntidad" name="idUsuario" value="" />
					<p><label>Número</label><input type="text" id="numero" name="numero" readonly="readonly" /></p>
					<p><label>Etiqueta</label><input type="text" id="etiqueta" name="etiqueta" /></p>
					<p><label>Handset</label>
						<select class="tipoFiltroHandset" id="handset" name="handset.idHandset">
							<option>Seleccione</option>
						</select>
					</p>
					<!-- <p><label>Seleccion</label>
						<input type="radio" id="tipo-0" value="2" class="selection" onclick="checkTodos('0')" />Todos
						<input type="radio" id="tipo-1" value="3" class="selection" onclick="checkTodos('1')" />Ninguno
					</p> -->
					
					<!-- <p class="monitor"><label>Monitoreados (a pesar que no se seleccione a si mismo, siempre el usuario se monitoreaa a si mismo)</label>
					</p> -->
					<table>
						<tr>
							<td>
								<select name="monitoreados.idUsuario" style="width:600px; height:150px;" multiple="multiple" class="tipoFiltro" id="listaMonitoreados"></select>
							</td>
							<%-- <td align="center">
								<input type="button" value="->>" onclick="moverTodos(document.getElementById('listaMonitoreados'), document.getElementById('monitoreados'))"/><br>
								<input type="button" value="-->" onclick="moverNumero(document.getElementById('listaMonitoreados'), document.getElementById('monitoreados'))"/><br>
								<input type="button" value="<--" onclick="moverNumero(document.getElementById('monitoreados'), document.getElementById('listaMonitoreados'))"/><br>
								<input type="button" value="<<-" onclick="moverTodos(document.getElementById('monitoreados'), document.getElementById('listaMonitoreados'))"/>
							</td>
							<td>
								Monitoreados<br>
								<select size="10" multiple="multiple" id="monitoreados" name="monitoreados.idUsuario"></select>
							</td> --%>
						</tr>
						<tr><td colspan="3">Nota: El usuario siempre se monitorea a si mismo</td></tr>
					</table>
					<p><label>Envio SMS</label><input type="text" id="sms" onkeydown="permitirSoloNumeros(event)"/></p>
					
				</form>
				<table width="100%">
				<tr>
					<td>
						<p align="left">
							<button type="button" onclick="javascript: window.location= 'usuarios.jsp'">Regresar</button>
							<button type="button" onclick="modificar('usuario')">Modificar</button>
						</p>
					<td>
					<td><p align="right" id="mensaje"></p></td>
				</tr>
				</table>
			</div>
		
		</td>
	</tr>
</table>
<script type="text/javascript">
var selectFilter = true;
	$(function(){
		listar('usuario');
		combobox('handset');
		listarTipo('usuario');
	});
</script>
</body>
</html>