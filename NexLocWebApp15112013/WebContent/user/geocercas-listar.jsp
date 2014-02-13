<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pe.com.nextel.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" href="../stylesheets/sunny/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
<link rel="stylesheet" href="../stylesheets/jquery.ui.all.css">
<link rel="stylesheet" href="../stylesheets/demos.css">
<link rel="stylesheet" href="../stylesheets/messi.css">
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script type="text/javascript" language="javascript" src="../javascript/jquery.dataTables.min.js"></script>
<script src="../javascript/messi.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.core.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.widget.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="../javascript/ui-widget.js" type="text/javascript"></script>
<script src="../javascript/tooltip.js" type="text/javascript"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<script src="../javascript/ui.multiselect.js" type="text/javascript"></script>
<link href="../stylesheets/ui.multiselect.css" rel="stylesheet" media="all" />
<title>::Localizador Satelital | Geocercas::</title>
<link rel="stylesheet" type="text/css" href="http://serverapi.arcgisonline.com/jsapi/arcgis/2.8/js/dojo/dijit/themes/claro/claro.css">
<link href="../stylesheets/jquery.dataTables.css" rel="stylesheet" media="all" />
<script type="text/javascript" src="http://serverapi.arcgisonline.com/jsapi/arcgis/?v=2.8"></script>
 <script type="text/javascript">
	
	 dojo.require("esri.map");
	 dojo.require("esri.layers.agstiled");
	 dojo.require("esri.toolbars.draw");
	 var map, tb;
	 var estado = true;
	 var selectFilter = true;
	function init() {
	 var startExtent = new esri.geometry.Extent(-77.0969,-12.1290,-76.9962,-12.0989, new esri.SpatialReference({wkid:4326}) );
	 map = new esri.Map("map",{extent:startExtent, logo:false});
	 var tiledMapServiceLayer = new esri.layers.ArcGISTiledMapServiceLayer("<%= PropertyUtil.readProperty("ARCGIS_REST") %>");
	 dojo.connect(map, "onLoad", initToolbar);
	 map.addLayer(tiledMapServiceLayer);
	}

</script>
<script src="../javascript/arcgis.util.js" type="text/javascript"></script>
</head>
<body class="claro">
<div id="dialog" title="¿Desea Continuar?" class="hidden">
	<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Se eliminara la siguiente geocerca.</p>
</div>
<jsp:include page="../menu.jsp"/>
<jsp:include page="../scriptMenu.jsp"/>
<table width="88%" class="tablaPrincipal"  cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="../header.jsp"/> </td>
	</tr>
	<tr>
		<td valign="top">
		<h2>Mis Geocercas</h2>
		<div id="lista-geocerca">
		<div id="filtro-geocercas">
			<b>Nombre de Geocerca </b>
			<input type="text" name="geocerca" id="filtro"/> 
			<button type="button" onclick="filtrar('geocerca')">Filtrar</button>
		</div>
		<br/>
		<table class="formato-tabla" id="tabla" cellspacing="0">
			<thead>
				<tr>
					<td>Nombre</td>
					<td>Fecha Registro</td>
					<td>Nuevo Campo</td>
					<td>Inicio</td>
					<td>Fin</td>
					<td>Estado</td>
					<td>Asignados</td>
					<td>Opciones</td>
				</tr>
			</thead>
			<tbody id="data-geocerca"></tbody>
		</table>
		
		</div>
		
		<div id="modificar-geocerca" class="hidden">
			<form id="geocerca-form" class="columns">
			<input type="hidden" name="idGeocerca" id="idGeocerca">
			<input type="hidden" name="rings" id="rings">
			<!-- <input type="hidden" name="nombreOriginal" id="nombreOriginal" /> -->
			<div class="left">
				<table>
						<tr>
							<td>
								<select name="usuarios.idUsuario" style="height: 200px; width:420px;" multiple="multiple" class="tipoFiltro" id="listaUsuarios"></select>
							</td>
<%-- 							<td align="center">
								<input type="button" value="->>" onclick="moverTodos(document.getElementById('listaUsuarios'), document.getElementById('usuarios'))"/><br>
								<input type="button" value="-->" onclick="moverNumero(document.getElementById('listaUsuarios'), document.getElementById('usuarios'))"/><br>
								<input type="button" value="<--" onclick="moverNumero(document.getElementById('usuarios'), document.getElementById('listaUsuarios'))"/><br>
								<input type="button" value="<<-" onclick="moverTodos(document.getElementById('usuarios'), document.getElementById('listaUsuarios'))"/>
							</td>
							<td>
								Asignados a Geocerca<br>
								<select size="7" id="usuarios" name="usuarios.idUsuario" multiple="multiple"></select>
							</td> --%>
						</tr>
					</table>
				<p><label>Periodo de actividad&nbsp;</label>&nbsp;Desde:&nbsp;<input class="medium" size="10" type="text" name="horario.fechaInicio" id="fechaInicio" /> <input class="short hidden" readonly="readonly" size="10" type="text" name="horario.horaInicio" id="horaInicio" /> 
				&nbsp;Hasta:&nbsp;<input size="10" class="medium" type="text" name="horario.fechaFin" id="fechaFin" /> <input class="short hidden" readonly="readonly" size="10" type="text" name="horario.horaFin" id="horaFin" /></p>
			</div>
			<div class="right">
				<p><label>Nombre</label><input type="text" id="nombre" name="nombre" /></p>
				<p><label>Email de  Notificación</label><input type="text" name="emailNotificacion" id="emailNotificacion" /></p>
				<p><label>Observación</label><textarea name="observacion" id="observacion"></textarea></p>				
			</div>
			<div class="clear"></div>
			</form>
			<table width="100%">
			<tr>
				<td>
					<!-- 			CAMBIO POR ALONSODELAQ 15/07 -->
					
					<input type="hidden" id="pto" />
					<!-- 			FIN DEL CAMBIO -->
					<p align="left"><button type="button" id="draw" class="hidden" onclick="dibujarGeocerca(estado)" class="margin">Dibujar Geocerca</button><button type="button" onclick="javascript: window.location='geocercas-listar.jsp'">Regresar</button>
					<button type="button" id="clonar" onclick="prepararClonarGeocerca()">Duplicar</button><button type="button" id="modificar" onclick="modificar('geocerca')">Guardar</button>
					<button type="button" onclick="focusMap()">Centrar</button>
					<button type="button" id="registrar" class="hidden" onclick="registrar('geocerca')">Registrar</button></p></td>
					
				<td><p id="mensaje"></p></td>
			</tr>
			</table>
			<div id="map" style="width:900px; height:400px; border:1px solid #ccc; margin-bottom:20px;"></div>
		</div>
		
		</td>
	</tr>
</table>
<script type="text/javascript">
var flag = true;
var me = false;
	$(function(){
		listar('geocerca');
		combobox('usuario');
	});
</script>
</body>
</html>