<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pe.com.nextel.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<title>::Localizador Satelital | Localización::</title>
<link rel="stylesheet" type="text/css" href="http://serverapi.arcgisonline.com/jsapi/arcgis/2.8/js/dojo/dijit/themes/claro/claro.css">
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
    function init() {
    	 var startExtent = new esri.geometry.Extent(-77.0969,-12.1290,-76.9962,-12.0989, new esri.SpatialReference({wkid:4326}) );
         map = new esri.Map("map",{extent:startExtent, logo:false});
         var tiledMapServiceLayer = new esri.layers.ArcGISTiledMapServiceLayer("<%= PropertyUtil.readProperty("ARCGIS_REST") %>");
         gsvc = new esri.tasks.GeometryService("<%= PropertyUtil.readProperty("GEOMETRY_SERVICE") %>");
         map.addLayer(tiledMapServiceLayer);
         dojo.connect(map,"onLoad", initPuntosI);
         //dojo.connect(map,"onLoad", doBuffer);
         
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
			<h2>Localizar por Equipo</h2>
		<div id="list-usuario">
		<div id="filtro-usuario">
			<div id="formulario" class="margen">
				<b><label class="labelw">Usuario</label></b>
				<select name="usuarios" class="tipoFiltro" id="idUsuario">
					<option value="-1">Seleccione</option>
				</select>
				<input type="hidden" name="objetivo" id="obj" value="0" />
				<label id="loading" class="labelw hidden"></label>
				<label id="time" class="labelw hidden">ssssssssssssssss</label>
<!-- 				Cambio por ALONSODELAQ 17/05 -->
				<input type="hidden" id="pto"/>
				
<!-- 				Fin de Cambio  -->
				
				<button type="button" id="btnLocalizar" onclick="loc('usuario')">Localizar</button>
				<label></label>
				Tiempo
				<select id="tiempo">
					<option value="0">0 min</option>
					<option value="15">15 min</option>
					<option value="30">30 min</option>
					<option value="45">45 min</option>
				</select>
				<button type="button" onclick="focusMap()">Centrar</button>
			</div>
<!-- 			<p class="right"><input type="checkbox" value="1" onchange="ocultarPuntos()" checked="checked"/> Ocultar Puntos de Interés</p> -->
			<div class="clear"></div>
			<div id="map" style="width:1200px; height:470px; border:1px solid #CCC;"></div>
		</div>
		<br/>
		
		</div>
		</td>
	</tr>
</table>
<script type="text/javascript">
	// Snippet para sollucionar el problema de que en IE8 no funciona el evento onchange en checkboxes
	if ($.browser.msie) {  
	  $(function() {  
	    $('input:radio, input:checkbox').click(function() {  
	      this.blur();  
	      this.focus();  
	    });  
	  });  
	} 

	var flag = true;
	var me = true;
	$(function(){
		combobox('usuario');
	});
</script>
</body>
</html>