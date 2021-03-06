<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pe.com.nextel.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../stylesheets/messi.css">
<link type="text/css" href="../stylesheets/sunny/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
<link rel="stylesheet" href="../stylesheets/jquery.ui.all.css">
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script src="../javascript/messi.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.core.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.widget.js" type="text/javascript"></script>
<script src="../javascript/ui-widget.js" type="text/javascript"></script>
<script src="../javascript/jquery.form.js" type="text/javascript"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<title>::Localizador Satelital | Puntos de Interés - Registrar::</title>
<link rel="stylesheet" type="text/css" href="http://serverapi.arcgisonline.com/jsapi/arcgis/2.8/js/dojo/dijit/themes/claro/claro.css">
<script type="text/javascript" src="http://serverapi.arcgisonline.com/jsapi/arcgis/?v=2.8"></script>
<script type="text/javascript">
 dojo.require("esri.map");
 var map;
 var locator = new esri.tasks.Locator("<%= PropertyUtil.readProperty("LIMA_GEOCODE_REST") %>");
	function init() {
		var startExtent = new esri.geometry.Extent(-77.0969,-12.1290,-76.9962,-12.0989, new esri.SpatialReference({wkid:4326}) );
        map = new esri.Map("map",{extent:startExtent, logo:false});
        var tiledMapServiceLayer = new esri.layers.ArcGISTiledMapServiceLayer("<%= PropertyUtil.readProperty("ARCGIS_REST") %>");
        
        dojo.connect(map,"onLoad",function(){
        	dojo.connect(map,"onClick",seleccionarPunto);
        	dojo.connect(locator, "onLocationToAddressComplete", onLocationToAddressComplete);
        	dojo.connect(locator, "onError", onLocatorError);
        });
        map.addLayer(tiledMapServiceLayer);
	}	

	dojo.addOnLoad(init);

</script>
<script src="../javascript/arcgis.util.js" type="text/javascript"></script>
</head>
<body class="claro">
<div id="dialogoExito" class="hidden">
	<p><span class="ui-icon ui-icon-info" style="float:left; margin:0 7px 20px 0;"></span>El Punto de Interes ha sido registrado correctamente.</p>
</div>
<jsp:include page="../menu.jsp"/>
<jsp:include page="../scriptMenu.jsp"/>
<table width="88%" class="tablaPrincipal"  cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="../header.jsp"/> </td>
	</tr>
	<tr>
		<td valign="top">
		<h2>Registrar Puntos de Interés</h2>
		<div class="puntoInteres" id="form-puntoInteres">	
			<form id="puntoInteres-form" class="columns"  enctype="multipart/form-data" method="post">
			<input name="puntoInteres.coordenada.longitud" id="longitud" type="hidden" value="0" />
			<input name="puntoInteres.coordenada.latitud" id="latitud" type="hidden" value="0" />
			
			<div class="left">
				<p><label>Nombre</label><input type="text" id="nombre" name="puntoInteres.nombre"/></p>
				<p><label>Dirección</label><input type="text" id="direccion" name="puntoInteres.direccion" size="25" />
				<input type="hidden" id="distrito" name="puntoInteres.distrito" /></p>		
			</div>
			<div class="right">
						
				<p><label>Imagen</label>
				<input type="file" id="imagen" name="imagen" 
					accept="image/gif, image/jpeg, image/pjpeg, image/png, image/bmp, image/x-windows-bmp" /></p>
				<p><!-- <label>Categoría</label>-->
					<select style="visibility:hidden;" class="tipoFiltro" id="categoria" name="puntoInteres.categoria.idCategoria">
						<option value ="54" selected="selected">Seleccione</option>
					</select></p>
			</div>				
			</form>			
			<div class="clear"></div>
			<table width="100%">
			<tr>
				<td><p align="left">
					<button type="button" onclick="javascript: window.location='puntosInteres-listar.jsp'">Regresar</button>
					<button type="button" onclick="registrar('puntoInteres')">Guardar</button></p>
				</td>
				<td><p align="right" id="mensaje"></p></td>
			</tr>
			</table>
			
			<div id="map" style="width:900px; height:400px; border:1px solid #ccc;"></div>
		</div>
		</td>
	</tr>
</table>
<script type="text/javascript">
	$(function(){
		//combobox('categoria');
	});
</script>
</body>
</html>