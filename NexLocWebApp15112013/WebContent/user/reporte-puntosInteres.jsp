<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pe.com.nextel.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" href="../stylesheets/sunny/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script type="text/javascript" language="javascript" src="../javascript/jquery.dataTables.min.js"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<title>::Localizador Satelital | Reportes::</title>
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
		<h2>Puntos de Interés</h2>
		<div class="puntoInteres" id="lista-puntoInteres">
			<p>	
				<!-- <b>Categoría</b>
				<select class="tipoFiltro" name="tipoFiltro" id="categoriaFiltro">
					<option value ="-1" selected="selected">Todos</option>
				</select> -->
				<b>Punto de Interés</b>
				<input type="text" name="filtro" id="filtro"/>
				<!-- <b>Distrito</b>
				<input type="text" name="distritoFiltro" id="distritoFiltro"/> -->
				<button type="button" onclick="filtrar('puntointeres')">Filtrar</button>
				<button type="button" onclick="pasarParametrosExcelPuntosInteres()">Exportar Excel</button>
			</p>
		<div id="table-data">
		<table class="formato-tabla" id="tabla" cellspacing="0">
			<thead>
				<tr>
					<td>Fecha Creación</td>
					<td>Nombre</td>
<!-- 					<td>Categoría</td> -->
					<td>Creado por</td>
					<td>Direccion</td>
					<td>Distrito</td>
					<td>Ver en Mapa</td>
				</tr>
			</thead>
			<tbody id="data-puntoInteres"></tbody>
		</table>
		</div>
		</div>
		<div class="hidden" id="mapa-puntoInteres">
				<div id="map" style="width:900px; height:400px; border:1px solid #ccc;"></div>
				<p><button type="button" onclick="javascript: window.location='reporte-puntosInteres.jsp'">Regresar</button></p>
			</div>
	</td>
	</tr>
</table>
<form name="excelPuntosInteres" id="excelPuntosInteres" action="reporte-exportar-puntosInteres.jsp" method="POST" style="display:none;">
	<input type="text" name="txtIdCuenta" id="txtIdCuenta"/>
	<input type="text" name="txtIdUsuario" id="txtIdUsuario"/>
	<input type="text" name="txtShowPrivados" id="txtShowPrivados"/>
	<input type="text" name="txtFlagPI" id="txtFlagPI"/>
</form> 
<script type="text/javascript">
var flagPI=true;
var listarPI=false;
var priv=true;
	$(function(){
		//combobox('categoria');
		listar('puntoInteres');
		
	});
</script>
</body>
</html>