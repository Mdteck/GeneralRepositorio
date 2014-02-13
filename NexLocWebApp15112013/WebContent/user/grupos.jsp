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
<script src="../javascript/tooltip.js" type="text/javascript"></script>
<script src="../javascript/ui.multiselect.js" type="text/javascript"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<script src="../javascript/messi.js" type="text/javascript"></script>
<link href="../stylesheets/ui.multiselect.css" rel="stylesheet" media="all" />

<link href="../stylesheets/jquery.dataTables.css" rel="stylesheet" media="all" />

<title>::Localizador Satelital | Grupos::</title>
<script type="text/javascript">
	<!--
	
	//var usuariosFilter = new filterlist(document.getElementById('usuarios'));
	//-->
</script>
</head>
<body>
<div id="dialog" title="¿Desea Continuar?" class="hidden">
	<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Se eliminará el siguiente grupo.</p>
</div>
<jsp:include page="../menu.jsp"/>
<jsp:include page="../scriptMenu.jsp"/>
<table width="88%" class="tablaPrincipal"  cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="../header.jsp"/> </td>
	</tr>
	<tr>
		<td valign="top">
			<h2>Grupos</h2>
			<div id="lista-grupo">
				<div id="filtro-usuario" class="margen">
					<div class="left">
						<label>Nombre de Grupo</label>
						<input type="text" name="nomb-grupo" id="filtro" />
						<button  type="button" onclick="filtrar('grupo')">Filtrar</button>
					</div>
					<button class="right" type="button" onclick="ver('grupo')">Crear Grupo</button>
					<div class="clear"></div>
				</div>
				<br/>
				<table class="formato-tabla" cellspacing="0" id="tabla">
					<thead>
						<tr>
							<td>Nombre</td>
							<td>Integrantes</td>
							<td>Opciones</td>
						</tr>
					</thead>
					<tbody id="data-grupo"></tbody>
				</table>	
			</div>
			<!--Modificar de Usuarios -->
			<div id="modificar-grupo" style="display: none;">
				<form id="grupo-form">
					<input type="hidden" name="idGrupo" id="idGrupo"/>
					<p><label>Nombre</label><input type="text" id="nombre" name="nombre" /></p>
					<table>
						<tr>
							<td>
								<select name="usuarios.idUsuario" style="height: 200px; width:600px;" multiple="multiple" class="tipoFiltro" id="listaUsuarios"></select>
							</td>
							<%-- <td align="center">
								<input type="button" value="->>" onclick="moverTodos(document.getElementById('listaUsuarios'), document.getElementById('usuarios'))"/><br>
								<input type="button" value="-->" onclick="moverNumero(document.getElementById('listaUsuarios'), document.getElementById('usuarios'))"/><br>
								<input type="button" value="<--" onclick="moverNumero(document.getElementById('usuarios'), document.getElementById('listaUsuarios'))"/><br>
								<input type="button" value="<<-" onclick="moverTodos(document.getElementById('usuarios'), document.getElementById('listaUsuarios'))"/>
							</td>
							<td>
								Asignados a Grupo<br>
								<select size="7" id="usuarios" name="usuarios.idUsuario" multiple="multiple"></select>
							</td>  --%>
						</tr>
					</table>
					<p><button type="button" onclick="javascript: window.location='grupos.jsp'">Regresar</button><button type="button" onclick="modificar('grupo')">Guardar</button></p>
				</form>
			</div>
			<!-- Registro de Grupos -->
			<div id="registrar-grupo"class="hidden">
				<form id="grupo-form" name="formulario">
					<p><label>Nombre</label><input type="text" name="nombre" id="nombreNuevo" /></p>
					<table>
						<tr>
							<td>						
								<select style="height: 200px; width:600px;" name="usuarios.idUsuario" multiple="multiple" class="tipoFiltro" id="listaUsuariosNuevos" name="selectField"></select>
								<br>
								<!-- Filtro : <input name="regexp" onkeyup="filterSelectBox(this.value,'listaUsuariosNuevos')"> -->
							</td>
							<%-- <td align="center">
								<input type="button" value="->>" onclick="moverTodos(document.getElementById('listaUsuariosNuevos'), document.getElementById('usuarios'))"/><br>
								<input type="button" value="-->" onclick="moverNumero(document.getElementById('listaUsuariosNuevos'), document.getElementById('usuarios'))"/><br>
								<input type="button" value="<--" onclick="moverNumero(document.getElementById('usuarios'), document.getElementById('listaUsuariosNuevos'))"/><br>
								<input type="button" value="<<-" onclick="moverTodos(document.getElementById('usuarios'), document.getElementById('listaUsuariosNuevos'))"/>
							</td>
							<td>
								Asignados a Grupo<br>
								<select size="7" id="usuarios" name="usuarios.idUsuario" multiple="multiple"></select>
							</td> --%>
						</tr>
					</table>
				</form>
				<table width="100%">
				<tr>
					<td><p align="left"><button type="button" onclick="javascript: window.location= 'grupos.jsp'">Regresar</button><button type="button" onclick="registrar('grupo')">Guardar</button></p></td>
				</tr>
				</table>
			</div>
			<p id="mensaje"></p>
		</td>
	</tr>
</table>
<script type="text/javascript">
var flag=true;
var me = false;
var selectFilter = true;
	$(function(){
		
		listar('grupo');
		combobox('usuario');
		
	});
</script>
</body>
</html>