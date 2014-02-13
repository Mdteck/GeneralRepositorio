<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pe.com.nextel.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script type="text/javascript" language="javascript" src="../javascript/jquery.dataTables.min.js"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>

<title>::Localizador Satelital | Puntos de Interés - Buscar::</title>
<link rel="stylesheet" type="text/css" href="http://serverapi.arcgisonline.com/jsapi/arcgis/2.8/js/dojo/dijit/themes/claro/claro.css">
<link href="../stylesheets/jquery.dataTables.css" rel="stylesheet" media="all" />
<script type="text/javascript" src="http://serverapi.arcgisonline.com/jsapi/arcgis/?v=2.8"></script>
<script type="text/javascript">
	dojo.require("esri.map");
	var map;
	var flag_mapa = true;
	function init() {
	   var startExtent = new esri.geometry.Extent(-77.0969,-12.1290,-76.9962,-12.0989, new esri.SpatialReference({wkid:4326}) );
       map = new esri.Map("map",{extent:startExtent, logo:false});
       var tiledMapServiceLayer = new esri.layers.ArcGISTiledMapServiceLayer("<%= PropertyUtil.readProperty("ARCGIS_REST") %>");
	   dojo.connect(map,"onLoad",showMap);    
	   map.addLayer(tiledMapServiceLayer);
	}

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
		
			<div align="left">
				<h2>Buscar Puntos de Interés</h2>
				<div id="buscar-puntosInteres">
				<form id="puntoInteres-form">
					<p><label>Nombre:</label><input type="text" name="nombre" /></p>
					<p><label>Categoria:</label>
						<select name="categoria.idCategoria" class="tipoFiltro">
							
						</select>
					</p>
					<p><label>Departamento</label><select name="idDepartamento" id="departamento" onchange="comboboxProvincia()"></select></p>
					<p><label>Provincia</label><select name="idProvincia" id="provincia" onchange="comboboxDistrito()"></select></p>
					<p><label>Distrito</label><select name="idDistrito" id="distrito"></select></p>
					<p><button type="button" onclick="buscar('puntoInteres')">Buscar</button></p>
					<p id="mensaje"></p>
				</form>
				</div>
			</div>
			<div class="hidden" id="lista-puntoInteres">
				<p><button type="button" onclick="javascript: window.location='puntosInteres-buscar.jsp'">Regresar</button></p>
				<table class="formato-tabla" cellspacing="0">
					<thead>
						<tr>
							<td>Nombre</td>
							<td>Categoría</td>
							<td>Dirección</td>
							<td>Opción</td>
						</tr>
					</thead>
					<tbody id="data-puntoInteres"></tbody>
				</table>
			</div>
			<div class="hidden" id="mapa-puntoInteres">
				<div id="map" style="width:900px; height:400px; border:1px solid #ccc;"></div>
				<p><button id="back" type="button" onclick="regresarBuscarPI()">Regresar</button></p>
			</div>
		</td>
	</tr>
</table>

<script type="text/javascript">
	$(function(){
		combobox('categoria');
		//listar('puntoInteres');
		comboboxDepartamento();
	});
</script>
</body>
</html>