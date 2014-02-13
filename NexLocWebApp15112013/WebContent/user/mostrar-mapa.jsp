<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pe.com.nextel.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../stylesheets/jquery.ui.all.css">
<link rel="stylesheet" href="../stylesheets/demos.css">
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script type="text/javascript" language="javascript" src="../javascript/jquery.dataTables.min.js"></script>
<script src="../javascript/jquery.ui.core.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.widget.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="../javascript/tooltip.js" type="text/javascript"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<link href="../stylesheets/jquery.dataTables.css" rel="stylesheet" media="all" />
<title>::Localizador Satelital | Localización::</title>
<link rel="stylesheet" type="text/css" href="http://serverapi.arcgisonline.com/jsapi/arcgis/2.8/js/dojo/dijit/themes/claro/claro.css">
<link href="../stylesheets/style.css" rel="stylesheet" media="all" />
<script type="text/javascript" src="http://serverapi.arcgisonline.com/jsapi/arcgis/?v=2.8"></script>
<script type="text/javascript">
	dojo.require("esri.map");
	dojo.require("esri.tasks.geometry");
	var map;
	var pis=new Array();
	var pisObj=new Array();
	var showPI=false;
	var initPI=true;
	var primeraCarga=true;
	var levelPI=<%= PropertyUtil.readProperty("LEVEL_PI") %>;
	var levelInicial=2;
	var RADIO_CELDA = <%= PropertyUtil.readProperty("RADIO_CELDA") %>;
	var RADIO_TRIANGULACION = <%= PropertyUtil.readProperty("RADIO_TRIANGULACION") %>;
	var RADIO_AGPS=<%= PropertyUtil.readProperty("RADIO_AGPS") %>;
	var gsvc;
	function cargar() {
		var monitor=localStorage.getItem('monitorM');
		var fecha = localStorage.getItem('fechaM');
		var usuario = localStorage.getItem('usuarioM');
		var longitud = localStorage.getItem('longitudM');
		var latitud = localStorage.getItem('latitudM');
		var distrito = localStorage.getItem('distritoM');
		var direccion = localStorage.getItem('direccionM');
		var metodo = localStorage.getItem('metodoM');
		var tecnologia = localStorage.getItem('tecnologiaM');

		var startExtent = new esri.geometry.Extent(-77.0969,-12.1290,-76.9962,-12.0989, new esri.SpatialReference({wkid:4326}) );
        map = new esri.Map("map",{extent:startExtent, logo:false});
        var tiledMapServiceLayer = new esri.layers.ArcGISTiledMapServiceLayer("<%= PropertyUtil.readProperty("ARCGIS_REST") %>");
        dojo.connect(map,"onLoad",showMapMDTECK2);    
        map.addLayer(tiledMapServiceLayer);
		
		var html = "<tr><td>"+monitor+"</td><td>"+fecha+"</td><td>"+usuario+"</td><td>"+longitud+"</td>"+
					"<td>"+latitud+"</td><td>"+distrito+"</td><td>"+direccion+"</td><td>"+metodo+"</td>"+
					"<td>"+tecnologia+"</td></tr>";
		$('#data-reporte').html(html);
	}	
	
	function init() {
		  
	}	
// 	dojo.addOnLoad(init);
</script>
<script src="../javascript/arcgis.util.js" type="text/javascript"></script>
</head>
<body class="claro" onload="cargar()">	
	<div class="clear"></div>
	<table align="center">
		<tr>
			<td>
				<div id="map" style="width:850px; height:400px; border:1px solid #ccc;"></div>	
			</td>
		</tr>
		<tr>
			<td>
<!-- 			<div id="list-reporte"> -->
<!-- 				<div id="table-data"> -->
				<table class="formato-tabla" cellspacing="0" id="tabla">
					<thead>
						<tr>
							<td>Monitor</td>
							<td>Fecha</td>
							<td>Usuario</td>
							<td>Latitud</td>
							<td>Longitud</td>
							<td>Dirección</td>
							<td>Distrito</td>
							<td>Metodo</td>
							<td>Tecnologia</td>							
						</tr>
					</thead>
					<tbody id="data-reporte"></tbody>
				</table>
<!-- 				</div> -->
<!-- 				</div> -->
			</td>
		</tr>
	</table>	
</body>
</html>