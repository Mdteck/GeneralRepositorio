<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="pe.com.nextel.util.*" %>
<!DOCTYPE directive  html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<link type="text/css" href="../stylesheets/sunny/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
<link type="text/css" href="../stylesheets/jquery.timeentry.css" rel="stylesheet" />
<link rel="stylesheet" href="../stylesheets/jquery.ui.all.css">
<link rel="stylesheet" href="../stylesheets/demos.css">
<link rel="stylesheet" href="../stylesheets/messi.css">
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script src="../javascript/jquery.timeentry.js" type="text/javascript"></script>
<script type="text/javascript" language="javascript" src="../javascript/jquery.dataTables.min.js"></script>
<script src="../javascript/messi.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.core.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.widget.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="../javascript/ui-widget.js" type="text/javascript"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<title>::Localizador Satelital | Tracking::</title>
<link rel="stylesheet" type="text/css" href="http://serverapi.arcgisonline.com/jsapi/arcgis/2.8/js/dojo/dijit/themes/claro/claro.css">
<link href="../stylesheets/jquery.dataTables.css" rel="stylesheet" media="all" />
<script type="text/javascript" src="http://serverapi.arcgisonline.com/jsapi/arcgis/?v=2.8"></script>
<script type="text/javascript">
 	dojo.require("esri.map");
	dojo.require("esri.layers.agstiled");
	dojo.require("esri.toolbars.draw");
	var map, tb;
	var flag_mapa = true;
	function init() {
		var startExtent = new esri.geometry.Extent(-77.0969,-12.1290,-76.9962,-12.0989, new esri.SpatialReference({wkid:4326}) );
        map = new esri.Map("map",{extent:startExtent, logo:false});
        var tiledMapServiceLayer = new esri.layers.ArcGISTiledMapServiceLayer("<%= PropertyUtil.readProperty("ARCGIS_REST") %>");
       	dojo.connect(map,"onLoad",initToolbarTracking);    
        map.addLayer(tiledMapServiceLayer);
	}
	
</script>
<script src="../javascript/arcgis.util.js" type="text/javascript"></script>
</head>
<body class="claro">
<div id="dialog" title="¿Desea Continuar?" class="hidden">
	<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Se detendrá el tracking seleccionado.</p>
</div>
<!-- Inicio 24/06/2013 MDTECK Sandy Huanco -->
<input type="hidden" id="path" />
<input type="hidden" id="pointsFinal"/>
<input type="hidden" id="pointsInicial"/>
<input type="hidden" id="points" />
<input type="hidden" id="nombreLocalizacion"/>
<input type="hidden" id="idTracking"/>
<input type="hidden" id="fechaInicioSel"/>
<input type="hidden" id="fechaFinSel"/>
<input type="hidden" id="latitudSel"/>
<input type="hidden" id="longitudSel"/>
<input type="hidden" id="posicionSel"/>
<!-- Fin -->
<!-- <input type="hidden" id="path" /> -->
<!-- <input type="hidden" id="points" /> -->
<!-- <input type="hidden" id="posicionFinal"/> -->
<!-- <input type="hidden" id="ultimaLocalizacion"/> -->
<!-- <input type="hidden" id="nombreLocalizacion"/> -->
<!-- <input type="hidden" id="metodo"/> -->
<!-- <input type="hidden" id="posicionInicial"/> -->
<!-- <input type="hidden" id="primeraLocalizacion"/> -->
<!-- <input type="hidden" id="metodoInicial"/> -->
<jsp:include page="../menu.jsp"/>
<jsp:include page="../scriptMenu.jsp"/>
<table width="88%" class="tablaPrincipal"  cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="../header.jsp"/> </td>
	</tr>
	<tr>
		<td valign="top">
		<h2>Tracking</h2>
		<div id="list-tracking">
			<p>	
				<b>En ejecución</b>
				<button type="button" onclick="javascript: window.location='tracking-historial.jsp'" class="right">Ver Historial</button>
				<button type="button" onclick="nuevoTracking()" class="right">Nuevo</button>

			</p>
			<table class="formato-tabla" id="tabla" cellspacing="0">
				<thead>
					<tr>
						<td>Etiqueta</td>
						<td>Número</td>
						<td>Fecha Inicio</td>
						<td>Hora Inicio</td>
						<td>Fecha Fin</td>
						<td>Hora Fin</td>
						<td>Opciones</td>
					</tr>
				</thead>
				<tbody id="data-tracking"></tbody>
			</table>
		</div>
		<div id="ver-tracking" class="hidden">
			<h2>Ver Tracking <span class="textoUsuario" id="etiquetaUsuario"></span></h2>
			<p id="tracking"></p>
			<div id="map" style="width:1200px; height:470px; border:1px solid #ccc;"></div>	
			<table cellpadding="5">
				<tr>
					<td><b>Fecha Inicial</b></td>
					<td><input type="text" id="fechaInicioB" name="fechaInicioB" readonly="readonly"/></td>
					<td><b>Hora Inicial</b></td>
					<td><input type="text" id="txtHoraInicio" name="txtHoraInicio" value="00:00" /></td>
					<td><b>Fecha Final</b></td>
					<td><input type="text" id="fechaFinB" name="fechaFinB" readonly="readonly"/></td>		
					<td><b>Hora Final</b></td>
					<td><input type="text" id="txtHoraFin" name="txtHoraFin" value="23:59"/></td>			
				</tr>
				<tr>
					<td><b>Metodo de Ubicación</b></td>
					<td>
						<select id="tipo_tracking">
							<option value="TODOS">TODOS</option>
							<option value="AGPS">AGPS</option>
							<option value="TRIANGULACION">TRIANGULACION</option>
							<option value="CELDA">CELDA</option>
						</select>
					</td>
					<td>
						
					</td>					
					<td colspan="5" align="right">
						<button type="button" onclick="obtenerFiltrosTracking()">Filtrar</button>
						<!-- CAMBIO POR ALONSODELAQ 15/07 -->
						<button type="button" onclick="focusMap()">Centrar</button>
						<input type="hidden" id="pto" />
						<!-- FIN DEL CAMBIO -->
						<button  type="button" style="width: 150px"  onclick="pasarParametrosExcelTracking()">Exportar Excel</button>
						<button type="button" id="btnRegresar">Regresar</button>
					</td>
<!-- 					<td></td> -->
<!-- 					<td></td> -->
				</tr>
			</table>			
					
			<div id="table-data">
				<table class="formato-tabla-detalle" id="tabla-detalle-tracking">
					<thead>
						<tr>
							<td>N°</td>
							<td>Fecha de Ubicación</td>
							<td>Dirección</td>
							<td>Distrito</td>
							<td>Método de Ubicación</td>
							<td>Ver en Mapa</td>
						</tr>
					</thead>
					<tbody id="detalle-tracking"></tbody>
				</table>
			</div>			
		</div>
		<div id="registrar-tracking" class="hidden">
			<h2>Registrar Tracking</h2>
			<form id="tracking-form">
				<p><label>Usuario</label>
					<select name="monitoreado.idUsuario" id="monitoreado" class="tipoFiltro">
						<option value="-1">Seleccione</option>
					</select>
				</p>
				<p><label>Periodo de actividad&nbsp;</label>&nbsp;Desde:&nbsp;<input class="medium" size="10" type="text" name="horario.fechaInicio" id="fechaInicio" readonly="readonly"/> 	
				<input class="short hidden" readonly="readonly" size="10" type="text" name="horario.horaInicio" id="horaInicio" />						
				&nbsp;Hasta:&nbsp;				
				<input size="10" class="medium" type="text" name="horario.fechaFin" id="fechaFin" readonly="readonly"/>
				<input class="short hidden" readonly="readonly" size="10" type="text" name="horario.horaFin" id="horaFin" />
				</p>				
			</form>
			<table width="100%">
			<tr>
				<td><p align="left"><button type="button" onclick="registrar('tracking')" class="right">Registrar</button><button type="button" onclick="javascript: window.location='tracking.jsp'" class="right">Regresar</button></p></td>	
			</tr>
			
			</table>
			<form name="excelTracking" id="excelTracking" action="reporte-exportar-tracking.jsp" method="POST" style="display:none;">
					<input type="text" name="txtFechaInicioF" id="txtFechaInicioF"/>
					<input type="text" name="txtFechaFinF" id="txtFechaFinF"/>
					<input type="text" name="txtHoraInicioF" id="txtHoraInicioF"/>
					<input type="text" name="txtHoraFinF" id="txtHoraFinF"/>
					<input type="text" name="txtMetodoF" id="txtMetodoF"/>
					<input type="text" name="txtIdTrackingF" id="txtIdTrackingF"/>
				</form> 
	<script type="text/javascript">
	var flag = true;
	$(function(){
		$("#fechaInicio").datepicker({ dateFormat: 'dd/mm/yy', minDate: new Date()});
		$("#fechaFin").datepicker({ dateFormat: 'dd/mm/yy', minDate: new Date()});
		$("#txtHoraInicio").timeEntry({show24Hours: true});
		$("#txtHoraFin").timeEntry({show24Hours: true});
		listar("tracking");
		combobox('usuario');
		obtenerHorario();
		
		/*Inicio 15/07/2013 MDTECK Sandy Huanco*/
		$("#btnRegresar").click(function(){
			var idTracking = $("#idTracking").val();
			if(idTracking!=''){
				if($("#list-tracking").is(":hidden")){
					$("#mensajeVer").css("display", "none");
					$("#ver-tracking").slideUp(500);
					$("#list-tracking").slideDown(500).show();
				}else{	
					$("#mensajeVer").css("display", "block");
					$("#list-tracking").slideUp(500);
					$("#ver-tracking").slideDown(500).show();					
				}
			}else{
				alert('Seleccionar un tracking');
			}
			
		});		
		/*Fin 15/07/2013*/
	});
</script>
		</div>
		<p align="center" id="mensaje"></p>
		</td>
	</tr>
</table>

</body>
</html>