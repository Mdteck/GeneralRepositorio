<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<title>::Localizador Satelital | Reportes::</title>
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
			<h2>Reporte de Localización</h2>
			<div id="list-reporte">
				<div id="filtro-reporte" class="margen">
				<table width="100%">
					<tr>
						<td><b>Fecha Inicial</b></td>
						<td><input type="text" id="fechaInicio" name="fechaInicio"/></td>
						<td><b>Fecha Final</b></td>
						<td><input type="text" id="fechaFin" name="fechaFin" id="filtro"/></td>
						<td align="center"><button  type="button" onclick="reporte('localizacion')">Consultar</button></td>
					</tr>
					<tr>
						<td><b>Por</b></td>
						<td>
							<select id="tipo" name="tipo" onchange="tipoReporte()">
								<option value="0">Todos</option>
								<option value="1">Usuarios</option>
								<option value="2">Grupos</option>
							</select>
						</td>
						<td ><b id="labelValor" style="display: none;">Valor</b></td>
						<td>
							<select style="display: none;" id="valor" name="valor" class="tipoFiltro">
								<option value="-1">Elegir</option>
							</select>
						</td>
						
						<td align="center"><button  type="button" style="width: 150px"  onclick="pasarParametrosExcel()">Exportar Excel</button></td>
					</tr>
				
				</table>
				<form name="excel" id="excel" action="reporte-exportar.jsp" method="POST" style="display:none;">
					<input type="text" name="txtFechaInicio" id="txtFechaInicio"/>
					<input type="text" name="txtFechaFin" id="txtFechaFin"/>
					<input type="text" name="txtTipo" id="txtTipo"/>
					<input type="text" name="txtValor" id="txtValor"/>
					<input type="text" name="txtIdCuenta" id="txtIdCuenta"/>
				</form> 
				</div>
				<br/>
				<div id="table-data">
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
								<td>Ver en Mapa</td>
							</tr>
						</thead>
						<tbody id="data-reporte"></tbody>
					</table>
				</div>	
			</div>		
		</td>
	</tr>
</table>
<script type="text/javascript">
var flag;
	$(function(){
		$("#fechaInicio").datepicker({ dateFormat: 'dd/mm/yy'});
		$("#fechaFin").datepicker({ dateFormat: 'dd/mm/yy'});
		if(localStorage.getItem('tipo')==2){
			flag=true;
		}
	});
</script>
</body>
</html>