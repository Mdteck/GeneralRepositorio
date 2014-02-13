<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pe.com.nextel.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../stylesheets/jquery.ui.all.css">
<link rel="stylesheet" href="../stylesheets/demos.css">
<link rel="stylesheet" href="../stylesheets/messi.css">
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script src="../javascript/messi.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.core.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.widget.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="../javascript/ui-widget.js" type="text/javascript"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<script src="../javascript/ui.multiselect.js" type="text/javascript"></script>
<link href="../stylesheets/ui.multiselect.css" rel="stylesheet" media="all" />
<title>::Localizador Satelital | Geocercas::</title>
<link rel="stylesheet" type="text/css" href="http://serverapi.arcgisonline.com/jsapi/arcgis/2.8/js/dojo/dijit/themes/claro/claro.css">
<script type="text/javascript" src="http://serverapi.arcgisonline.com/jsapi/arcgis/?v=2.8"></script>
 <script type="text/javascript">
 dojo.require("esri.map");
 dojo.require("esri.layers.agstiled");
 dojo.require("esri.toolbars.draw");
 var map, tb;
 var estado = true;
 var selectFilter = true;
 
 function validar(e) { // 1
	    var nombre=$('#nombre').val();
 		if(!(nombre.length<40)){
 			nombre=nombre.substring(0,39);
 			$('#nombre').val(nombre);
 		}
	} 
 
function init() {
 var startExtent = new esri.geometry.Extent(-77.0969,-12.1290,-76.9962,-12.0989, new esri.SpatialReference({wkid:4326}) );
 map = new esri.Map("map",{extent:startExtent, logo:false});
 var tiledMapServiceLayer = new esri.layers.ArcGISTiledMapServiceLayer("<%= PropertyUtil.readProperty("ARCGIS_REST") %>");
 dojo.connect(map, "onLoad", initToolbar);
 map.addLayer(tiledMapServiceLayer);
}
	dojo.addOnLoad(init);

</script>
<script src="../javascript/arcgis.util.js" type="text/javascript"></script>
</head>
<body class="claro">
<jsp:include page="../menu.jsp"/>
<jsp:include page="../scriptMenu.jsp"/>
<table width="88%" class="tablaPrincipal"  cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="../header.jsp"/> </td>
	</tr>
	<tr>
		<td valign="top">
		<h2>Geocercas</h2>
		
		<div id="registro-geocerca">
			
			
			<form id="geocerca-form" class="columns">
			<div class="left">
				<input type="hidden" name="rings" id="rings" />
				<table>
						<tr>
							<td>
								<select name="usuarios.idUsuario" style="width:420px ; height:200px;" multiple="multiple" class="tipoFiltro" id="listaUsuarios"></select>
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
				<p><label>Periodo de actividad&nbsp;</label>&nbsp;Desde:&nbsp;<input class="medium" size="10" type="text" name="horario.fechaInicio" id="fechaInicio"/><input class="short hidden" readonly="readonly" size="10" type="text" name="horario.horaInicio" id="horaInicio" />
				 &nbsp;Hasta:&nbsp;<input size="10" class="medium" type="text" name="horario.fechaFin" id="fechaFin" /> <input class="short hidden" readonly="readonly" size="10" type="text" name="horario.horaFin" id="horaFin" /></p>				
			
			</div>
			<div class="right">
			<input type="hidden" name="idGeocerca" id="idGeocerca" />
				<p><label>Nombre</label><input type="text" name="nombre" id="nombre" onkeypress="return validar(event)" /></p>
				<p><label>Email de  Notificación</label><input type="text" name="emailNotificacion" id="emailNotificacion" /></p>
				<p><label>Observación</label><textarea style="height: 100px" name="observacion" id="observacion"></textarea></p>
				
				
			</div>
			<div class="clear"></div>
			</form>
			<table width="100%">
			<tr>
				<td><p align="left"><button type="button" id="draw" onclick="dibujarGeocerca(estado)" class="margin">Dibujar Geocerca</button><button type="button" onclick="javascript: window.location='geocercas-listar.jsp'">Regresar</button><button type="button" onclick="registrar('geocerca')">Guardar</button></p></td>
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
		$( "#fechaInicio" ).datepicker({ dateFormat: 'dd/mm/yy', minDate: new Date()});
		$( "#fechaFin" ).datepicker({ dateFormat: 'dd/mm/yy', minDate: new Date()});
		//listar('geocerca');
		combobox('usuario');
		obtenerHorario();
	});
</script>
</body>
</html>