<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pe.com.nextel.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" href="../stylesheets/sunny/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
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
<title>::Localizador Satelital | Reportes::</title>
<link rel="stylesheet" type="text/css" href="http://serverapi.arcgisonline.com/jsapi/arcgis/2.8/js/dojo/dijit/themes/claro/claro.css">
<link href="../stylesheets/jquery.dataTables.css" rel="stylesheet" media="all" />
<script type="text/javascript" src="http://serverapi.arcgisonline.com/jsapi/arcgis/?v=2.8"></script>
 <script type="text/javascript">
	
	 dojo.require("esri.map");
	 dojo.require("esri.layers.agstiled");
	 dojo.require("esri.toolbars.draw");
	 var map, tb;
	 var estado = true;
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
<body>
<jsp:include page="../menu.jsp"/>
<jsp:include page="../scriptMenu.jsp"/>
<table width="88%" class="tablaPrincipal"  cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="../header.jsp"/> </td>
	</tr>
	<tr>
		<td valign="top">
			<h2>Reporte de geocercas</h2>
			<div id="lista-reporte">
				<div id="filtro-reporte" class="margen">				
				<b>Geocerca</b>
				<input type="text" name="filtro" id="filtro"/>
				<b>Estado</b>
				<select class="estadoFiltro" name="estadoFiltro" id="estadoFiltro">
					<option selected="selected">Todos</option>
					<option>Iniciado</option>
					<option>Terminado</option>
					<option>Cancelado</option>
				</select>
				<b>Fecha de Ejecucion</b>
				<input type="text" name="fechaEjecucionFiltro" id="fechaEjecucionFiltro"/>
				<button type="button" onclick="filtrar('geocerca')">Filtrar</button>
				<button  type="button"  onclick="pasarParametrosExcelGeocerca('todo')">Exportar Excel</button>
				
				<form name="excelGeocerca" id="excelGeocerca" action="reporte-exportar-geocercas.jsp" method="POST" style="display:none;">
					<input type="text" name="txtIdUsuario" id="txtIdUsuario"/>
					<input type="text" name="txtLista" id="txtLista"/>
					<input type="text" name="txtTipo" id="txtTipo"/>
					<input type="text" name="txtTipo2" id="txtTipo2"/>
					<input type="text" name="txtIdDetalle" id="txtIdDetalle"/>
				</form> 
				</div>
				<br/>
				<div id="table-data">
				<table class="formato-tabla" cellspacing="0" id="tabla">
					<thead>
						<tr>
							<td>Geocerca</td>
							<td>Fecha Creacion</td>
							<td>Usuario</td>
							<td>Estado</td>
							<td>Fecha Ejecucion</td>
							<td>Fecha Culminacion</td>
							<td>Opciones</td>
						</tr>
					</thead>
					<tbody id="data-reporte"></tbody>
				</table>
				</div>	
			</div>
			<div id='buttonsDiv' class="hidden">
				<p><button type="button" onclick="javascript: window.location='reporte-geocercas.jsp'">Regresar</button><button  type="button"  onclick="pasarParametrosExcelGeocerca('detalle')">Exportar Excel</button></p>
			</div>
			<div id="detail-data" class="hidden">
				<table class="formato-tabla" cellspacing="0" id="tabla">
					<thead>
						<tr>
							<td>Monitoreado</td>
							<td>Fecha</td>
							<td>Movimiento</td>
							<td>Método de Ubicación</td>
							<td>Dirección</td>
							<td>Distrito</td>
						</tr>
					</thead>
					<tbody id="detail-reporte"></tbody>
				</table>
			</div>
			<div id="mapa-geocerca" class="hidden">
				<div id="map" style="width:900px; height:400px; border:1px solid #ccc; margin-bottom:20px;"></div>
				<p><button type="button" onclick="javascript: window.location='reporte-geocercas.jsp'">Regresar</button>
			</div>
				
		</td>
	</tr>
</table>
<input type="hidden" id="rings"/>
<input type="hidden" id="idDetalleSelec"/>
<script type="text/javascript">
	$(function(){
		$( "#fechaEjecucionFiltro" ).datepicker({ dateFormat: 'dd/mm/yy'});
		reporte('geocerca');
	});
</script>
</body>
</html>