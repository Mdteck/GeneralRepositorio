<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pe.com.nextel.util.*" %>
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
<script src="../javascript/jquery.form.js" type="text/javascript"></script>
<script src="../javascript/ui-widget.js" type="text/javascript"></script>
<script src="../javascript/tooltip.js" type="text/javascript"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<script src="../javascript/messi.js" type="text/javascript"></script>
<title>::Localizador Satelital | Puntos de Interés::</title>
<link rel="stylesheet" type="text/css" href="http://serverapi.arcgisonline.com/jsapi/arcgis/2.8/js/dojo/dijit/themes/claro/claro.css">
<link href="../stylesheets/jquery.dataTables.css" rel="stylesheet" media="all" />
<script type="text/javascript" src="http://serverapi.arcgisonline.com/jsapi/arcgis/?v=2.8"></script>
 <script type="text/javascript">
	 dojo.require("esri.map");
	 var map;
	 var flag_mapa = true;
	 var locator = new esri.tasks.Locator("<%= PropertyUtil.readProperty("LIMA_GEOCODE_REST") %>");
	function init() {
		var startExtent = new esri.geometry.Extent(-77.0969,-12.1290,-76.9962,-12.0989, new esri.SpatialReference({wkid:4326}) );
        map = new esri.Map("map",{extent:startExtent, logo:false});
        var tiledMapServiceLayer = new esri.layers.ArcGISTiledMapServiceLayer("<%= PropertyUtil.readProperty("ARCGIS_REST") %>");
        dojo.connect(map,"onLoad",showPuntoInteres);    
        map.addLayer(tiledMapServiceLayer);
	}
	
</script>
<script src="../javascript/arcgis.util.js" type="text/javascript"></script>
</head>
<body class="claro">
<div id="dialog" title="¿Desea Continuar?" class="hidden">
	<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Se eliminará el siguiente punto de interés.</p>
</div>
<div id="dialogoExito" class="hidden">
	<p><span class="ui-icon ui-icon-info" style="float:left; margin:0 7px 20px 0;"></span>El punto de Interes fue modificado correctamente.</p>
</div>
<jsp:include page="../menu.jsp"/>
<jsp:include page="../scriptMenu.jsp"/>
<table width="88%" class="tablaPrincipal"  cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="../header.jsp"/> </td>
	</tr>
	<tr>
		<td valign="top">
		<h2>Mis Puntos de Interés</h2>
		<div class="puntoInteres" id="list-puntoInteres">
			<p>	
<!-- 				<b>Categoría</b> -->
<!-- 				<select class="tipoFiltro" name="tipoFiltro" id="categoriaFiltro"> -->
<!-- 					<option value ="-1" selected="selected">Todos</option> -->
<!-- 				</select> -->
				<b>Punto de Interés</b>
				<input type="text" name="filtro" id="filtro"/>
				<button type="button" onclick="filtrar('puntointeres')">Filtrar</button>
				<button type="button" onclick="verMapa()">Ver en Mapa</button>
			</p>
			<p id="mensajeLista" class="hidden"></p>
		<table class="formato-tabla" id="tabla" cellspacing="0">
			<thead>
				<tr>
					<td> <!-- <input type="checkbox" id="elegir" value="all">--></td>
					<td>Nombre</td>
<!-- 					<td>Categoría</td> -->
					<td>Fecha de Registro</td>
					<td>Opciones</td>
				</tr>
			</thead>
			<tbody id="data-puntoInteres"></tbody>
		</table>
		
		</div>
		<div class="puntoInteres" id="form-puntoInteres" style="display: none;">
			
			<div class="margin">
			</div>
			<form id="puntoInteres-form" class="columns"  enctype="multipart/form-data" method="post">
			<input name="puntoInteres.coordenada.longitud" id="longitud" type="hidden" value="" />
			<input name="puntoInteres.coordenada.latitud" id="latitud" type="hidden" value="" />
			<input name="puntoInteres.idPuntoInteres" id="idEntidad" type="hidden" value="" />
			<div class="left">
				<p><label>Nombre</label><input type="text" id="nombre" name="puntoInteres.nombre"/></p>
 				<p><label>Dirección</label><input type="text" id="direccion" name="puntoInteres.direccion" />
				<input type="hidden" id="distrito" name="puntoInteres.distrito"/></p>
			</div>
			<div class="right">
				<p></p>
				<p><label style="vertical-align: top">Imagen</label>
				<img id="img_prev" style="height: 50 px" />
				<!-- <img id="img_prev" alt="Imagen" src="#" /> --></p>
 				<p><label>Cambiar imagen</label>
 				<input type="file" id="imagen" name="imagen" /></p>
 				<!--<label>Categoría</label> -->
					<select style="visibility: hidden;" class="tipoFiltro" id="categoria" name="puntoInteres.categoria.idCategoria">
						<option value ="54" selected="selected">Favoritos</option>
					</select>
			</div>
			</form>
			<div class="clear"></div>
			<table width="100%">
			<tr>
				<td><p align="left"><button type="button" onclick="javascript: window.location='puntosInteres-listar.jsp'">Regresar</button>
				<button type="button" id="modificar" onclick="modificar('puntoInteres')">Modificar</button></p></td>
				<td><p align="right" id="mensaje"></p></td>
			</tr>
			</table>
			<div id="map" style="width:900px; height:400px; border:1px solid #ccc;"></div>
		</div>
		
		</td>
	</tr>
</table>
<script type="text/javascript">
var flagPI;
var listarPI=true;
var priv=false;
if(localStorage.getItem('tipo')==1)	flagPI=true;
else								flagPI=false;
	$(function(){
		//combobox('categoria');
		listar('puntoInteres');
	});
</script>
</body>
</html>