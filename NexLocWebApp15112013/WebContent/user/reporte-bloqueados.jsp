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
			<h2>Reporte Equipos Bloqueados</h2>
			<div id="list-reporte">
				<div id="filtro-reporte" class="margen">
				<table width="100%">
					<tr>
						<td><b>Numero</b></td>
						<td><input type="text" id="numero" name="numero"/></td>
						<td><b>Etiqueta</b></td>
						<td><input type="text" id="etiqueta" name="etiqueta" /></td>
						<td align="center"><button  type="button" onclick="reporte('bloqueados')">Consultar</button></td>
					</tr>
				</table>
				<form name="excel" id="excel" action="reporte.jsp" method="POST" style="display:none;">
					<input type="text" name="data" id="data"/>
				</form> 
				</div>
				<br/>
				<div id="table-data">
				<table class="formato-tabla" cellspacing="0" id="tabla">
					<thead>
						<tr>
							<td>Fecha</td>
							<td>Numero</td>
							<td>Etiqueta</td>
							<td>Estado</td>
						</tr>
					</thead>
					<tbody id="data-reporte"></tbody>
				</table>
				</div>	
			</div>
		
		</td>
	</tr>
</table>
</body>
</html>