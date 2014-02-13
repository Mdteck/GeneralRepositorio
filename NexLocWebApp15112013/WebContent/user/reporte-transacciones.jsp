<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../stylesheets/jquery.ui.all.css">
<link rel="stylesheet" href="../stylesheets/demos.css">
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script type="text/javascript" language="javascript" src="../javascript/jquery.dataTables.min.js"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.core.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.widget.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.datepicker.js" type="text/javascript"></script>
<link href="../stylesheets/jquery.dataTables.css" rel="stylesheet" media="all" />
<title>::Localizador Satelital | Reportes::</title>

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
		<h2>Monitoreo de Tráfico</h2>
		<div class="puntoInteres" id="lista-puntoInteres">
			<p>	
				<b>Tecnología</b>
				<select class="tipoFiltro" name="tecnologia" id="tecnologia">
					<option value ="TODOS" selected="selected">Todos</option>
					<option value ="2G" >2G y 2GM</option>
					<option value ="3G" >3G</option>
				</select>
				<b><label>Origen</label></b>
				<select id="tipoTransaccion">
					<option value="-1">Todos</option>
					<option value="0">Web</option>
					<option value="2">Tracking</option>
					<option value="3">Geocerca</option>
				</select>
				<b>Filtrar Por</b>
				<select onchange="onchangeFilter(this.value)" id="tipoFiltro">
					<option value="hora">Hora</option>
					<option value="dia">Día</option>
					<option value="fecha">Fechas</option>
					<option value="mes">Mes</option>
					<option value="anio">Año</option>
				</select>
				<label class="filters" id="fechaInicioLabel">Fecha Inicio</label><input class="filters" id="fechaInicio" type="text" size="10"></input>
				<label class="filters" id="fechaFinLabel" style="display: none;">Fecha Fin</label><input style="display: none;" class="filters" id="fechaFin" type="text" size="10"></input>
				<label class="filters" id="tipoFiltroLabel">Hora</label><input class="filters" id="val" type="text" size="5"></input>
				<label class="filters" id="mesLabel" style="display: none;">Mes</label>
				<select class="filters" id="mes" style="display: none;">
					<option value="1">Enero</option>
					<option value="2">Febrero</option>
					<option value="3">Marzo</option>
					<option value="4">Abril</option>
					<option value="5">Mayo</option>
					<option value="6">Junio</option>
					<option value="7">Julio</option>
					<option value="8">Agosto</option>
					<option value="9">Septiembre</option>
					<option value="10">Octubre</option>
					<option value="11">Noviembre</option>
					<option value="12">Diciembre</option>
				</select>
				
				<br/>
				<button type="button" onclick="reporte('transacciones')">Reportar</button>
				<button type="button" onclick="gotoExcel('table-data')">Exportar Excel</button>
			</p>
		<div id="table-data">
		<table class="formato-tabla" id="tabla" cellspacing="0">
			<thead>
				<tr>	
					<td></td>
					<td>Cantidad AGPS</td>
					<td>Cantidad Triangulacion</td>
					<td>Cantidad Celda</td>
					<td>Cantidad Exitosas</td>
					<td>Cantidad No Exitosas</td>
					<td>Total</td>
				</tr>
			</thead>
			<tbody id="data-reporte"></tbody>
		</table>
		</div>
		</div>
	</td>
	</tr>
</table>
<form name="excel" id="excel" action="reporte.jsp" method="POST" style="display:none;">
	<input type="text" name="data" id="data"/>
</form> 
<script type="text/javascript">	
$(function(){
		$( "#fechaInicio" ).datepicker({ dateFormat: 'yy-mm-dd'});
		$( "#fechaFin" ).datepicker({ dateFormat: 'yy-mm-dd'});
	});
function onchangeFilter(value){
	$('.filters').hide();
	if(value=='hora'){
		$("#fechaInicioLabel").show();
		$("#fechaInicioLabel").html('Fecha');
		$('#fechaInicio').show();
		$('#tipoFiltroLabel').show();
		$('#tipoFiltroLabel').html('Hora');
		$('#val').show();
	}else if(value=='dia'){
		$("#fechaInicioLabel").show();
		$("#fechaInicioLabel").html('Fecha');
		$('#fechaInicio').show();
	}else if(value=="fecha"){
		$("#fechaInicioLabel").show();
		$("#fechaInicioLabel").html('Fecha Inicio');
		$('#fechaInicio').show();
		$("#fechaFinLabel").show();
		$("#fechaFin").show();
	}else if(value=="anio"){
		$('#tipoFiltroLabel').show();
		$('#tipoFiltroLabel').html('Año');
		$('#val').show();
	}else if(value=='mes'){
		$('#labelMes').show()
		$('#mes').show();
	}
}
</script>
</body>
</html>