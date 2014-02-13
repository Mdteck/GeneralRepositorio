<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<link type="text/css" href="../stylesheets/sunny/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
<link rel="stylesheet" href="../stylesheets/jquery.ui.all.css">
<link rel="stylesheet" href="../stylesheets/demos.css">
<script src="../javascript/messi.js" type="text/javascript"></script>
<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
<script type="text/javascript" language="javascript" src="../javascript/jquery.dataTables.min.js"></script>
<script src="../javascript/jquery.ui.core.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.widget.js" type="text/javascript"></script>
<script src="../javascript/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="../javascript/ui-widget.js" type="text/javascript"></script>
<script src="../javascript/ajax.util.js" type="text/javascript"></script>
<script src="../javascript/view.js" type="text/javascript"></script>
<link rel="stylesheet" href="../stylesheets/messi.css">
<link href="../stylesheets/jquery.dataTables.css" rel="stylesheet" media="all" />
<script type="text/javascript">
function validar(e) { // 1
	    var nombre=$('#categoria').val();
 		if(!(nombre.length<50)){
 			nombre=nombre.substring(0,49);
 			$('#categoria').val(nombre);
 		}
	} 
</script>

<title>::Localizador Satelital | Categoria::</title>
</head>
<body>
<div id="dialog" title="¿Desea Continuar?" class="hidden">
	<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Se eliminará la siguiente categoria.</p>
</div>
<jsp:include page="../menu.jsp"/>
<jsp:include page="../scriptMenu.jsp"/>
<table width="88%" class="tablaPrincipal"  cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="../header.jsp"/> </td>
	</tr>
	<tr>
		<td valign="top">
		<h2>Categorías</h2>
		<div id="lista-categoria">
			<p>
				<button type="button" onclick="ver('categoria')" class="right">Nueva</button>
				<div class="clear"></div>
			</p>
			<div id="lista-categoria">
			<table class="formato-tabla" cellspacing="0">
				<thead>
					<tr>
						<td>Categoría</td>
						<td>Puntos de Interés</td>
						<td>Opciones</td>
					</tr>
				</thead>
				<tbody id="data-categoria"></tbody>
			</table>
		</div>
		</div>
		<div id="registrar-categoria" class="hidden">
			<div id="formulario">
				<form id="categoria-form">
					<p><label>Categoría</label><input type="text" class="categoria" name="categoria" id="categoria" onkeypress="return validar(event)"/></p>
					<p><button type="button" onclick="javascript: window.location='categorias.jsp'">Regresar</button><button type="button" onclick="registrar('categoria')">Guardar</button></p>
				</form>
			</div>
		
		</div>
		<div id="modificar-categoria" class="hidden">
			<div id="formulario">
				<form id="categoria-form">
					<p><label>Categoría</label><input type="text" class="categoria" name="categoria" /></p>
					<input type="hidden" name="idCategoria" class="idcategoria">
				</form>
				<table>
					<tr>
						<td><p align="left"><button type="button" onclick="javascript: window.location='categorias.jsp'">Regresar</button><button type="button" onclick="modificar('categoria')">Modificar</button></p></td>
						
					</tr>
				</table>
			</div>
		
		</div>
		<p align="center" id="mensaje"></p>
		</td>
	</tr>
</table>
<script type="text/javascript">
	$(function(){
		listar('categoria');
		
	});
</script>
</body>
</html>