<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<% String path = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>::Localizador Satelital | Inicio::</title>
<script src="<%=path %>/javascript/jquery-1.6.js" type="text/javascript"></script>
<script src="<%=path %>/javascript/view.js" type="text/javascript"></script>
<link href="../stylesheets/style.css" rel="stylesheet" media="all" />
</head>
<body>
<jsp:include page="../menu.jsp"/>
<table width="88%" class="tablaPrincipal" cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="../header.jsp"/> </td>
	</tr>
	<tr>
		<td>
		<table width="100%" >
			<tr>
				<td><h1>Localizador Satelital + </h1></td>
				<td align="right">
					<a href="descargue-manual.jsp" title="Descargar Manual">
						<img height="40px" alt="Descargar Manual" src="images/descargar-manual.png">
					</a>
				</td>
			</tr>
		</table>		
		<p class="parrafoJustificado">Localizador Satelital + es un servicio que permite ubicar geográficamente equipos Nextel por medio de una plataforma de localización GPS asistida (AGPS) con un margen de hasta 5 metros de precisión.</p>
		<p class="parrafoJustificado">La posición del equipo es indicada en los mapas de Nextel, que contienen información detalla de distritos, avenidas y calles.</p>
		
		<h2>¿Cómo se utiliza?</h2>
		<p class="parrafoJustificado">Ingrese a la página web de Nextel <a href="http://www.nextel.com.pe">www.nextel.com.pe</a> y utilice la siguiente ruta: (i) Servicios de Valor Agregado; y (ii) Localizador Satelital Nextel. Luego de ello ingresando su usuario y contraseña (el cual le será proporcionado a través del sistema "Mi Nextel" – antes Control Web), usted encontrará las siguientes opciones:</p>
		
		<h3>Localización</h3>
		<p class="parrafoJustificado">Use la función Localizar para ubicar equipos en tiempo real. Cualquier equipo activo de su cuenta que posea el servicio de Localizador Satelital + de Nextel podrá ser ubicado en el mapa.</p>
		<p class="parrafoJustificado">Puede solicitar la ubicación de uno o más equipos usando la función Localizar Equipo o Localizar Grupo. La posición, junto a la descripción, de los equipos aparecerá representada en el mapa.</p>
		
		<h3>Puntos De Interés</h3>
		<p class="parrafoJustificado">Son marcas visibles en el mapa que contienen información sobre lugares de interés para nuestro negocio o para uno mismo.</p>
		<p class="parrafoJustificado">En cualquier momento puedo crear nuevos puntos de interés y consultarlos desde la página web o desde mi equipo Nextel.</p>
		
		<h3>Alertas De Geocerca</h3>
		<p class="parrafoJustificado">Las Geocercas son áreas delimitadas en el mapa que representan una zona de control. Cuando un usuario ingresa o sale de esta zona, se genera una alerta por SMS o email notificando el evento.</p>
		<p class="parrafoJustificado">Las Geocercas pueden ser delimitadas a nuestro criterio y podemos asignar a cualquier numero de nuestra cuenta que tenga el servicio de Localizador activo.</p>
		
		<h3>Tracking</h3>
		<p class="parrafoJustificado">Esta opción permite mostrar la ruta que ha seguido un equipo en un intervalo de tiempo. Esta ruta es el resultado de varias capturas almacenadas por el sistema y dispuestas en el mapa unidas por líneas.</p>
		<p class="parrafoJustificado">Con esta opción es posible rastrear uno o más equipos a la vez </p>
		
		<h3>Administración</h3>
		<p class="parrafoJustificado">Accediendo al menú de Administración puedo asignar nombres a los usuarios, configurar los modelos de equipos y  formar grupos de usuarios.</p>
		<p class="parrafoJustificado">En caso de ser un usuario Monitoreado, permite realizar una auto consulta.</p>
		
		<h2>¿Cómo se activa este servicio?</h2>
		<p class="parrafoJustificado">Para suscribirse al servicio Localizador Satelital Nextel sólo necesita:</p>
		<ol>
			<li class="parrafoJustificado">Firma del representante legal de la empresa (o del titular de los equipos en caso de ser persona natural) en el formulario: Solicitud de acceso al servicio de Localización de Nextel, incluyendo los números de los equipos a los cuales se activará el servicio e indicando el número de equipo que será considerado el "Administrador del Servicio". Dicho formulario deberá ser entregado con la firma original a Nextel. Luego de 48 horas el servicio estará activo (sujeto a evaluación crediticia previa). Puede descargar el formato (dos caras) y proceder a su llenado y firma en ambas caras haciendo <a href="http://www.nextel.com.pe/satisfaccion/formato_solicitud/V10_SolicituddeaccesoalservicioLocalizacionNextelv9.xls">clic aquí</a>.</li>
			<li class="parrafoJustificado">Adjuntar una copia simple del documento de identidad del representante legal de la empresa y los poderes respectivos.</li>
		</ol>
		
		<h2>¿Qué usuarios existen y cuáles son sus funciones?</h2>
		<p class="parrafoJustificado">Existen tres tipos de usuarios:</p>
		<ol>
			<li class="parrafoJustificado">Administrador: es quien asigna los perfiles ya sea de Monitor o de Monitoreado entre los equipos de la empresa con el servicio activo.</li>
			<li class="parrafoJustificado">Monitor: es el usuario en capacidad de crear y modificar grupos de equipos monitoreados y de realizar consultas individuales o grupales sobre la ubicación de los mismos.</li>
			<li class="parrafoJustificado">Monitoreado: usuario cuyo equipo Nextel puede ser localizado siempre y cuando tenga la funcionalidad activa (no auto bloqueada), el equipo encendido y en ese momento no se encuentre realizando una llamada (interconexión telefónica, Conexión Directa o Nextel Online). En caso el usuario se haya auto bloqueado, aunque tenga el servicio activo, no estará en capacidad de ser localizado.</li>
		</ol>
		</td>
	</tr>
</table>
	<script type="text/javascript">
	active_menu();
	localStorage.setItem('etiqueta', '${ sessionScope.usuario }');
	localStorage.setItem('numero', '${ sessionScope.numero }');
	localStorage.setItem('tipo', '${sessionScope.tipo}');
	localStorage.setItem('cuenta', '${sessionScope.idCuenta}');
	localStorage.setItem('usuario', '${sessionScope.idUsuario}');
	</script>
	<script type="text/javascript">
		$(function() {
			$('#menu').delay(10000).animate({'marginLeft':'-150px'},100);
		});		
	</script>
</body>
</html>