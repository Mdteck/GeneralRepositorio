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
		<h2>Monitoreo de Tráfico MLP</h2>
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
				<input type="hidden" id="tipoFiltro" value="dia"></input>
				<label class="filters" id="fechaInicioLabel">Fecha </label><input class="filters" id="fecha" type="text" size="10"></input>
				<br/>
				
			</p>
		<div class="tablas" id="table-data-horas">
		<button type="button" onclick="detalleMLP('horas', null, null)">Reportar</button>
		<button type="button" onclick="gotoExcel('table-data-horas')">Exportar Excel</button>
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
					<td>Acción</td>
				</tr>
			</thead>
			<tbody id="data-reporte-horas"></tbody>
		</table>
		</div>

		<div class="tablas" id="table-data-minutos" style="display:none;">
		<button type="button" onclick="showTable('horas')">Regresar</button>
		<button type="button" onclick="gotoExcel('table-data-minutos')">Exportar Excel</button>
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
					<td>Acción</td>
				</tr>
			</thead>
			<tbody id="data-reporte-minutos"></tbody>
		</table>
		</div>


		<div class="tablas" id="table-data-segundos" style="display:none;" >
		<button type="button" onclick="showTable('minutos')">Regresar</button>
		<button type="button" onclick="gotoExcel('table-data-segundos')">Exportar Excel</button>
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
			<tbody id="data-reporte-segundos"></tbody>
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
		$( "#fecha" ).datepicker({ dateFormat: 'yy-mm-dd'});
});

function showTable(mostrar){
	$('.tablas').hide();
	if(mostrar=='horas'){
		$('#table-data-horas').show();
	}else if(mostrar=='minutos'){
		$('#table-data-minutos').show();
	}else if(mostrar=='segundos'){
		$('#table-data-segundos').show();
	}
}
</script>
</body>
</html>