<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html  lang="es">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>::Localizador Satelital | Monitoreo::</title>
	<script src="../javascript/jquery-1.6.js" type="text/javascript"></script>
	<script type="text/javascript" language="javascript" src="../javascript/jquery.dataTables.min.js"></script>
	<script src="../javascript/jquery.ui.core.js" type="text/javascript"></script>
	<script src="../javascript/jquery.ui.widget.js" type="text/javascript"></script>
	<script src="../javascript/view.js" type="text/javascript"></script>
	
	<link class="include" rel="stylesheet" type="text/css" href="../javascript/jqplot/jquery.jqplot.min.css" />
	<script class="include" type="text/javascript" src="../javascript/jqplot/jquery.min.js"></script>
	<script class="include" type="text/javascript" src="../javascript/jqplot/jquery.jqplot.min.js"></script>
	<script class="include" type="text/javascript" src="../javascript/jqplot/plugins/jqplot.cursor.min.js"></script>
	<script class="include" language="javascript" type="text/javascript" src="../javascript/jqplot/plugins/jqplot.dateAxisRenderer.min.js"></script>
	<style type="text/css">
		
		.legenda{
			width:10px;
			height:10px;			
			border:1px solid #d4d4d4;
			float:right;
		}
		.agps{
			background-color:#FE642E;	
		}
		.triangulacion{
			background-color:#FFFF00;	
		}
		.celda{
			background-color:#0000FF;	
		}
		.exitosas{
			background-color:#04B431;	
		}
		.noExitosas{
			background-color:#0B6121;	
		}
		label{
			font-size: 10px;
		}
		#list-reporte ul{
			-webkit-padding-start:10px !important;
		}
		li {
			list-style-type: none !important;
		}
		#dataSeries_diaAgps
		{
			outline-color: #FE642E;
			outline-style:solid;
			outline-width:2px;
		}
		#dataSeries_diaTriangulacion
		{
			outline-color: #FFFF00;
			outline-style:solid;
			outline-width:2px;
		}
		#dataSeries_diaCelda
		{
			outline-color: #0000FF;			
		}
		checkbox{
			outline-style:solid;
			outline-width:2px;
		}
	  </style>
	
	<script class="code" type="text/javascript">
		$(function(){
			
			
			$('#dataSeries_urban').css('outline-color', '#FFFF00');
			$('#dataSeries_urban').css('outline-style', 'solid');
			$('#dataSeries_urban').css('outline-width', '2px');
			
			$('#dataSeries_rural').css('outline-color', '#0000FF');
			$('#dataSeries_rural').css('outline-style', 'solid');
			$('#dataSeries_rural').css('outline-width', '2px');		


			$('#dataSeries_national2').css('outline-color', '#FE642E');
			$('#dataSeries_national2').css('outline-style', 'solid');
			$('#dataSeries_national2').css('outline-width', '2px');
			
			$('#dataSeries_urban2').css('outline-color', '#FFFF00');
			$('#dataSeries_urban2').css('outline-style', 'solid');
			$('#dataSeries_urban2').css('outline-width', '2px');
			
			$('#dataSeries_rural2').css('outline-color', '#0000FF');
			$('#dataSeries_rural2').css('outline-style', 'solid');
			$('#dataSeries_rural2').css('outline-width', '2px');	

			


			$('#dataSeries_national3').css('outline-color', '#FE642E');
			$('#dataSeries_national3').css('outline-style', 'solid');
			$('#dataSeries_national3').css('outline-width', '2px');
			
			$('#dataSeries_urban3').css('outline-color', '#FFFF00');
			$('#dataSeries_urban3').css('outline-style', 'solid');
			$('#dataSeries_urban3').css('outline-width', '2px');
			
			$('#dataSeries_rural3').css('outline-color', '#0000FF');
			$('#dataSeries_rural3').css('outline-style', 'solid');
			$('#dataSeries_rural3').css('outline-width', '2px');			

				
			$('#dataSeries_exitosas4').css('outline-color', '#04B431');
			$('#dataSeries_exitosas4').css('outline-style', 'solid');
			$('#dataSeries_exitosas4').css('outline-width', '2px');	
			
			$('#dataSeries_exitosas5').css('outline-color', '#04B431');
			$('#dataSeries_exitosas5').css('outline-style', 'solid');
			$('#dataSeries_exitosas5').css('outline-width', '2px');	
			
			$('#dataSeries_exitosas6').css('outline-color', '#04B431');
			$('#dataSeries_exitosas6').css('outline-style', 'solid');
			$('#dataSeries_exitosas6').css('outline-width', '2px');	
			
			
			$('#dataSeries_totales4').css('outline-color', '#0B6121');
			$('#dataSeries_totales4').css('outline-style', 'solid');
			$('#dataSeries_totales4').css('outline-width', '2px');	
			
			$('#dataSeries_totales5').css('outline-color', '#0B6121');
			$('#dataSeries_totales5').css('outline-style', 'solid');
			$('#dataSeries_totales5').css('outline-width', '2px');	
			
			$('#dataSeries_totales6').css('outline-color', '#0B6121');
			$('#dataSeries_totales6').css('outline-style', 'solid');
			$('#dataSeries_totales6').css('outline-width', '2px');	
			
		});
	</script>
</head>
<body>
<jsp:include page="../menu.jsp"/>
<jsp:include page="../scriptMenu.jsp"/>

<input type="hidden" id="txtHorasMetodo">
<input type="hidden" id="txtHorasExitosas">
<input type="hidden" id="txtDiaMetodo">
<input type="hidden" id="txtDiaExitosas">
<input type="hidden" id="txtSemanaMetodo">
<input type="hidden" id="txtSemanaExitosas">

<table width="88%" class="tablaPrincipal"  cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="../header.jsp"/> </td>
	</tr>
	<tr>
		<td valign="top">
			<h2>Grafica de monitoreo de tráfico de equipos 3G. (actualización 5 minutos)</h2>
			<div id="list-reporte">
				<table>
					<tr height="60px">
						<td colspan="4" align="center">
							<table>
								<tr>
									<td>AGPS</td>
									<td><div class="legenda agps"></div></td>
									<td width="50px"></td>
									<td>RTT</td>
									<td><div class="legenda triangulacion"></div></td>
									<td width="50px"></td>
									<td>Celda</td>
									<td><div class="legenda celda"></div></td>
									<td width="50px"></td>
									<td>Exitosas</td>
									<td><div class="legenda exitosas"></div></td>
									<td width="50px"></td>
									<td>Totales</td>
									<td><div class="legenda noExitosas"></div></td>
								</tr>
							</table>				
						</td>
					</tr>
					<tr>
						<td>
							<ul>
								<li>
									<input class="dataSeries-checkbox" id="dataSeries_diaAgps" name="dataSeries_diaAgps" value="AGPS" type="checkbox" checked />
								</li>
								<li>
									<input class="dataSeries-checkbox" id="dataSeries_diaTriangulacion" name="dataSeries_diaTriangulacion" value="RTT" type="checkbox" checked />
								</li>
								<li>
									<input class="dataSeries-checkbox" id="dataSeries_diaCelda" name="dataSeries_diaCelda" value="Celda" type="checkbox" checked />
								</li>					
							</ul>				
						</td>
						<td><div id="chart1" style="height:180px; width:550px;"></div></td>
						<td>
							<ul>
								<li>
									<input class="dataSeries-checkbox4 exitosas" id="dataSeries_exitosas4" name="dataSeries_exitosas4" value="national" type="checkbox" checked />
								</li>
								<li>
									<input class="dataSeries-checkbox4 totales" id="dataSeries_totales4" name="dataSeries_totales4" value="urban" type="checkbox" checked />
								</li>					
							</ul>				
						</td>
						<td><div id="chart4" style="height:180px; width:550px;"></div></td>
					</tr>
					<tr height="30px"><td></td></tr>
					<tr>	
						<td>
							<ul>
								<li>
									<input class="dataSeries-checkbox3 agps" id="dataSeries_national3" name="dataSeries_national3" value="national" type="checkbox" checked />
								<li>
									<input class="dataSeries-checkbox3 triangulacion" id="dataSeries_urban3" name="dataSeries_urban3" value="urban" type="checkbox" checked />
								</li>
								<li>
									<input class="dataSeries-checkbox3 celda" id="dataSeries_rural3" name="dataSeries_rural3" value="rural" type="checkbox" checked />
								</li>					
							</ul>				
						</td>
						<td><div id="chart3" style="height:180px; width:550px;"></div></td>
						<td>
							<ul>
								<li>
									<input class="dataSeries-checkbox5 exitosas" id="dataSeries_exitosas5" name="dataSeries_exitosas5" value="national" type="checkbox" checked />
								</li>
								<li>
									<input class="dataSeries-checkbox5 totales" id="dataSeries_totales5" name="dataSeries_totales5" value="urban" type="checkbox" checked />
								</li>										
							</ul>				
						</td>
						<td>
							<div id="chart5" style="height:180px; width:550px;"></div>
						</td>
					</tr>
					<tr height="30px"><td></td></tr>
					<tr>	
						<td>
							<ul>
								<li>
									<input class="dataSeries-checkbox2 agps" id="dataSeries_national2" name="dataSeries_national2" value="national" type="checkbox" checked />
								</li>
								<li>
									<input class="dataSeries-checkbox2 triangulacion" id="dataSeries_urban2" name="dataSeries_urban2" value="urban" type="checkbox" checked />
								</li>
								<li>
									<input class="dataSeries-checkbox2 celda" id="dataSeries_rural2" name="dataSeries_rural2" value="rural" type="checkbox" checked />
								</li>					
							</ul>				
						</td>
						<td><div id="chart2" style="height:180px; width:550px;"></div></td>
						<td>
							<ul>
								<li>
									<input class="dataSeries-checkbox6 exitosas" id="dataSeries_exitosas6" name="dataSeries_exitosas6" value="national" type="checkbox" checked />
								</li>
								<li>
									<input class="dataSeries-checkbox6 totales" id="dataSeries_totales6" name="dataSeries_totales6" value="urban" type="checkbox" checked />
								</li>					
							</ul>				
						</td>
						<td><div id="chart6" style="height:180px; width:550px;"></div></td>
					</tr>
				</table>
				
			</div>		
		</td>
	</tr>
</table>
</body>
</html>

<script class="code" type="text/javascript">

    $(document).ready(function() {
        $.jqplot.config.enablePlugins = false;
		$.jqplot.config.dashLength = 5;
		$.jqplot.config.gapLength = 2;
		
		var labels = ["AGPS", "Triangulacion", "Celda", "Exitosas","Totales"];
				
		var dataSets4Dia = {		
			line1: [['08:00', 100],['08:10', 120],['08:20', 140],['08:30', 160],['08:40', 140],['08:50', 160],['09:00', 180],['09:10', 200],['09:20', 220],['09:30', 250],['09:40', 260],['09:50', 245],['10:00', 280],['10:10', 269],['10:20', 276], ['10:30', 259], ['10:40', 259], ['10:50', 250], ['11:00', 240],['11:10', 239],['11:20', 216], ['11:30', 209], ['11:40', 200], ['11:50', 250], ['12:00', 210]],
			line2: [['08:00', 200],['08:10', 220],['08:20', 240],['08:30', 260],['08:40', 240],['08:50', 260],['09:00', 280],['09:10', 300],['09:20', 320],['09:30', 330],['09:40', 310],['09:50', 300],['10:00', 320],['10:10', 300],['10:20', 346], ['10:30', 309], ['10:40', 329], ['10:50', 309], ['11:00', 320],['11:10', 249],['11:20', 226], ['11:30', 219], ['11:40', 210], ['11:50', 230], ['12:00', 240]],
			line3: [['08:00', 300],['08:10', 320],['08:20', 340],['08:30', 360],['08:40', 340],['08:50', 360],['09:00', 380],['09:10', 400],['09:20', 390],['09:30', 370],['09:40', 356],['09:50', 350],['10:00', 300],['10:10', 328],['10:20', 306], ['10:30', 329], ['10:40', 309], ['10:50', 329], ['11:00', 350],['11:10', 259],['11:20', 236], ['11:30', 229], ['11:40', 229], ['11:50', 260], ['12:00', 230]],
			line4: [['08:00', 400],['08:10', 420],['08:20', 440],['08:30', 460],['08:40', 440],['08:50', 460],['09:00', 480],['09:10', 500],['09:20', 495],['09:30', 566],['09:40', 534],['09:50', 500],['10:00', 480],['10:10', 490],['10:20', 366], ['10:30', 509], ['10:40', 509], ['10:50', 509], ['11:00', 420],['11:10', 269],['11:20', 246], ['11:30', 239], ['11:40', 239], ['11:50', 290], ['12:00', 220]],
			line5: [['08:00', 550],['08:10', 570],['08:20', 560],['08:30', 580],['08:40', 600],['08:50', 570],['09:00', 680],['09:10', 610],['09:20', 600],['09:30', 696],['09:40', 574],['09:50', 520],['10:00', 490],['10:10', 500],['10:20', 486], ['10:30', 639], ['10:40', 639], ['10:50', 629], ['11:00', 530],['11:10', 479],['11:20', 356], ['11:30', 349], ['11:40', 449], ['11:50', 420], ['12:00', 360]]
		}	

		var dataSetsSemanal = {		
			line1: [['00:00', 100],
					['01:00', 100],
					['02:00', 180],
					['03:00', 280],
					['04:00', 100],
					['05:00', 180],
					['06:00', 280],
					['07:00', 240],
					['08:00', 100],
					['09:00', 180],
					['10:00', 280],
					['11:00', 240],
					['12:00', 100],
					['13:00', 100],
					['14:00', 180],
					['15:00', 280],
					['16:00', 100],
					['16:00', 180],
					['17:00', 280],
					['18:00', 240],
					['19:00', 100],
					['20:00', 180],
					['21:00', 280],
					['23:00', 240]
					],
			
			line2: [['00:00', 320],
					['01:00', 350],
					['02:00', 320],
					['03:00', 500],
					['04:00', 520],
					['05:00', 480],
					['06:00', 470],
					['07:00', 340],
					['08:00', 300],
					['09:00', 380],
					['10:00', 380],
					['11:00', 340],
					['12:00', 300],
					['13:00', 200],
					['14:00', 280],
					['15:00', 350],
					['16:00', 300],
					['16:00', 280],
					['17:00', 180],
					['18:00', 240],
					['19:00', 200],
					['20:00', 280],
					['21:00', 480],
					['23:00', 140]
					],
					
			line3: [['00:00', 390],
					['01:00', 450],
					['02:00', 420],
					['03:00', 600],
					['04:00', 620],
					['05:00', 580],
					['06:00', 570],
					['07:00', 440],
					['08:00', 500],
					['09:00', 480],
					['10:00', 480],
					['11:00', 440],
					['12:00', 400],
					['13:00', 300],
					['14:00', 380],
					['15:00', 450],
					['16:00', 400],
					['16:00', 380],
					['17:00', 280],
					['18:00', 340],
					['19:00', 300],
					['20:00', 380],
					['21:00', 480],
					['23:00', 340],
					],		
			line4: [['00:00', 490],
					['01:00', 550],
					['02:00', 520],
					['03:00', 700],
					['04:00', 720],
					['05:00', 680],
					['06:00', 670],
					['07:00', 540],
					['08:00', 600],
					['09:00', 580],
					['10:00', 580],
					['11:00', 540],
					['12:00', 500],
					['13:00', 400],
					['14:00', 480],
					['15:00', 550],
					['16:00', 500],
					['16:00', 480],
					['17:00', 380],
					['18:00', 440],
					['19:00', 400],
					['20:00', 480],
					['21:00', 480],
					['23:00', 440]
					],				
			line5: [['00:00', 590],
					['01:00', 650],
					['02:00', 620],
					['03:00', 800],
					['04:00', 820],
					['05:00', 780],
					['06:00', 770],
					['07:00', 640],
					['08:00', 700],
					['09:00', 680],
					['10:00', 680],
					['11:00', 640],
					['12:00', 600],
					['13:00', 500],
					['14:00', 580],
					['15:00', 650],
					['16:00', 100],
					['16:00', 480],
					['17:00', 380],
					['18:00', 440],
					['19:00', 400],
					['20:00', 480],
					['21:00', 480],
					['23:00', 440],
					],				
			
			
			
		}	
        
		var dataSetsSemanal2 = {	
		lineFechas : [
			['2013-09-02 00:00', 268],['2013-09-03 00:00', 127],
			['2013-09-02 06:00', 212],['2013-09-03 06:00', 281],
			['2013-09-02 12:00', 240],['2013-09-03 12:00', 393],
			['2013-09-02 18:00', 234],['2013-09-03 18:00', 424],			
			
			['2013-09-04 00:00', 368],['2013-09-05 00:00', 227],
			['2013-09-04 06:00', 212],['2013-09-05 06:00', 181],
			['2013-09-04 12:00', 240],['2013-09-05 12:00', 293],
			['2013-09-04 18:00', 334],['2013-09-05 18:00', 424],			
			
			['2013-09-06 00:00', 168],['2013-09-07 00:00', 227],
			['2013-09-06 06:00', 268],['2013-09-07 06:00', 217],
			['2013-09-06 12:00', 312],['2013-09-07 12:00', 181],
			['2013-09-06 18:00', 140],['2013-09-07 18:00', 193],
			
			['2013-09-08 00:00', 127],			
			['2013-09-08 06:00', 357],			
			['2013-09-08 12:00', 237],			
			['2013-09-08 18:00', 227]			
			],
		 lineFechas2 : [
			['2013-09-02 00:00', 138],['2013-09-03 00:00', 127],
			['2013-09-02 06:00', 242],['2013-09-03 06:00', 241],
			['2013-09-02 12:00', 310],['2013-09-03 12:00', 273],
			['2013-09-02 18:00', 124],['2013-09-03 18:00', 124],			
			
			['2013-09-04 00:50', 228],['2013-09-05 04:55', 157],
			['2013-09-04 05:00', 152],['2013-09-05 09:05', 341],
			['2013-09-04 10:10', 210],['2013-09-05 14:15', 223],
			['2013-09-04 15:20', 314],['2013-09-05 19:25', 344],
			['2013-09-04 20:30', 227],['2013-09-05 24:00', 260],
			
			['2013-09-06 00:50', 128],['2013-09-07 04:55', 117],
			['2013-09-06 05:50', 248],['2013-09-07 09:55', 217],
			['2013-09-06 10:00', 322],['2013-09-07 14:05', 231],
			['2013-09-06 15:10', 270],['2013-09-07 19:15', 123],
			['2013-09-06 20:20', 114],['2013-09-07 24:00', 124],
			['2013-09-08 00:00', 227],			
			['2013-09-08 06:00', 357],			
			['2013-09-08 12:00', 437],			
			['2013-09-08 18:00', 527]				
			],
		lineFechas3 : [
			['2013-09-02 00:00', 218],['2013-09-03 04:00', 237],
			['2013-09-02 05:00', 332],['2013-09-03 09:05', 131],
			['2013-09-02 10:10', 320],['2013-09-03 14:15', 253],
			['2013-09-02 15:20', 314],['2013-09-03 19:25', 314],
			['2013-09-02 20:30', 327],['2013-09-03 24:00', 450],			
			
			['2013-09-04 00:50', 238],['2013-09-05 04:55', 137],
			['2013-09-04 05:00', 322],['2013-09-05 09:05', 221],
			['2013-09-04 10:10', 120],['2013-09-05 14:15', 313],
			['2013-09-04 15:20', 234],['2013-09-05 19:25', 634],
			['2013-09-04 20:30', 517],['2013-09-05 24:00', 550],
			
			['2013-09-06 00:50', 228],['2013-09-07 04:55', 327],
			['2013-09-06 05:50', 148],['2013-09-07 09:55', 457],
			['2013-09-06 10:00', 222],['2013-09-07 14:05', 421],
			['2013-09-06 15:10', 570],['2013-09-07 19:15', 313],
			['2013-09-06 20:20', 314],['2013-09-07 24:00', 274],
			['2013-09-08 00:00', 427],			
			['2013-09-08 06:00', 357],			
			['2013-09-08 12:00', 537],			
			['2013-09-08 18:00', 327]	
			
			],
		lineFechas4 : [
			['2013-09-02 00:00', 130],['2013-09-03 04:00', 347],
			['2013-09-02 05:00', 240],['2013-09-03 09:05', 461],
			['2013-09-02 10:10', 130],['2013-09-03 14:15', 473],
			['2013-09-02 15:20', 124],['2013-09-03 19:25', 434],
			['2013-09-02 20:30', 247],['2013-09-03 24:00', 460],			
			
			['2013-09-04 00:50', 448],['2013-09-05 04:55', 347],
			['2013-09-04 05:00', 432],['2013-09-05 09:05', 331],
			['2013-09-04 10:10', 330],['2013-09-05 14:15', 323],
			['2013-09-04 15:20', 464],['2013-09-05 19:25', 444],
			['2013-09-04 20:30', 327],['2013-09-05 24:00', 360],
			
			['2013-09-06 00:50', 338],['2013-09-07 04:55', 437],
			['2013-09-06 05:50', 488],['2013-09-07 09:55', 467],
			['2013-09-06 10:00', 432],['2013-09-07 14:05', 341],
			['2013-09-06 15:10', 380],['2013-09-07 19:15', 523],
			['2013-09-06 20:20', 324],['2013-09-07 24:00', 584],
			
			['2013-09-08 00:00', 127],			
			['2013-09-08 06:00', 217],			
			['2013-09-08 12:00', 327],			
			['2013-09-08 18:00', 447]	
			],
		lineFechas5 : [
			['2013-09-02 00:00', 350],['2013-09-03 04:00', 367],
			['2013-09-02 05:00', 260],['2013-09-03 09:05', 481],
			['2013-09-02 10:10', 345],['2013-09-03 14:15', 383],
			['2013-09-02 15:20', 244],['2013-09-03 19:25', 344],
			['2013-09-02 20:30', 477],['2013-09-03 24:00', 470],			
			
			['2013-09-04 00:50', 368],['2013-09-05 04:55', 257],
			['2013-09-04 05:00', 342],['2013-09-05 09:05', 341],
			['2013-09-04 10:10', 360],['2013-09-05 14:15', 363],
			['2013-09-04 15:20', 384],['2013-09-05 19:25', 254],
			['2013-09-04 20:30', 347],['2013-09-05 24:00', 280],
			
			['2013-09-06 00:50', 258],['2013-09-07 04:55', 247],
			['2013-09-06 05:50', 108],['2013-09-07 09:55', 277],
			['2013-09-06 10:00', 252],['2013-09-07 14:05', 251],
			['2013-09-06 15:10', 390],['2013-09-07 19:15', 343],
			['2013-09-06 20:20', 234],['2013-09-07 24:00', 104],
			['2013-09-08 00:00', 287],			
			['2013-09-08 06:00', 297],			
			['2013-09-08 12:00', 307],			
			['2013-09-08 18:00', 127]	
			
			]		
			
			}

        var plot1 = $.jqplot('chart1', [dataSets4Dia.line1, dataSets4Dia.line2, dataSets4Dia.line3], {
			  title:'Diario (4 ultimas horas)',
			  axes:{
				xaxis:{
				  renderer:$.jqplot.DateAxisRenderer,
				  tickOptions:{
					formatString:'%H:%M'
				  } 
				},
				yaxis:{
				  tickOptions:{
					formatString:''
					},
					min:0, max: 900, numberTicks:7
				}
			  },
			  seriesColors: [ "#FE642E", "#FFFF00", "#0000FF", "#04B431","#0B6121"],
			  
			  highlighter: {
				show: true/*,
				sizeAdjust: 1.5*/
			  },
			  cursor: {
				show: false
			  },
			  seriesDefaults: {
				lineWidth: 1
			  },
			  markerOptions: {
				size: 5
			  }
		  });
		 
		var plot4 = $.jqplot('chart4', [dataSets4Dia.line4, dataSets4Dia.line5], {
			  title:'Diario (4 ultimas horas)',
			  axes:{
				xaxis:{
				  renderer:$.jqplot.DateAxisRenderer,
				  tickOptions:{
					formatString:'%H:%M'
				  } 
				},
				yaxis:{
				  tickOptions:{
					formatString:''
					},
					min:0, max: 900, numberTicks:7
				}
			  },
			  /*legend: {
				show: true,
				placement: 'outsideGrid',
				labels: labels,
				location: 'ne',
				rowSpacing: '0px'
			  },*/
			  seriesColors: [ "#04B431","#0B6121"],
			  
			  highlighter: {
				show: true,
				sizeAdjust: 6
			  },
			  cursor: {
				show: false
			  },
			  seriesDefaults: {
				lineWidth: 1
			  }
		  }); 

        var plot2 = $.jqplot('chart2',[dataSetsSemanal2.lineFechas,dataSetsSemanal2.lineFechas2,dataSetsSemanal2.lineFechas3],{
			  title:'Semanal',
			  axes:{
				xaxis:{
					renderer:$.jqplot.DateAxisRenderer,
					tickOptions:{
						formatString:'%a'
					}	
				},
				yaxis:{
				  tickOptions:{
					formatString:''
					},
					min:0, max: 900, numberTicks:7
				}
			  },
			  /*legend: {
				show: true,
				placement: 'outsideGrid',
				labels: labels,
				location: 'ne',
				rowSpacing: '0px'
			},*/
			  seriesColors: [ "#FE642E", "#FFFF00", "#0000FF", "#04B431","#0B6121"],
			  
			  highlighter: {
				show: true,
				sizeAdjust: 6
			  },
			  cursor: {
				show: false
			  },
			  seriesDefaults: {
				lineWidth: 1
			  }
		  });    
		  
		 var plot6 = $.jqplot('chart6',[dataSetsSemanal2.lineFechas4,dataSetsSemanal2.lineFechas5 ],{
			  title:'Semanal',
			  axes:{
				xaxis:{
					renderer:$.jqplot.DateAxisRenderer,
					tickOptions:{
						formatString:'%a'
					}	
				},
				yaxis:{
				  tickOptions:{
					formatString:''
					},
					min:0, max: 900, numberTicks:7
				}
			  },
			  /*legend: {
				show: true,
				placement: 'outsideGrid',
				labels: labels,
				location: 'ne',
				rowSpacing: '0px'
			},*/
			  seriesColors: [ "#04B431","#0B6121"],
			  
			  highlighter: {
				show: true,
				sizeAdjust: 6
			  },
			  cursor: {
				show: false
			  },
			  seriesDefaults: {
				lineWidth: 1
			  }
		  });     
       
		var plot3 = $.jqplot('chart3', [dataSetsSemanal.line1, dataSetsSemanal.line2, dataSetsSemanal.line3], {
			  title:'Dia (24 horas)',
			  axes:{
				xaxis:{
				  renderer:$.jqplot.DateAxisRenderer,
				  tickOptions:{
					formatString:'%#H'
				  } 
				},
				yaxis:{
				  tickOptions:{
					formatString:''
					},
					min:0, max: 900, numberTicks:7
				}
			  },
			  /*legend: {
				show: true,
				placement: 'outsideGrid',
				labels: labels,
				location: 'ne',
				rowSpacing: '0px'
			},*/
			  seriesColors: [ "#FE642E", "#FFFF00", "#0000FF", "#04B431","#0B6121"],
			  
			  highlighter: {
				show: true,
				sizeAdjust: 6
			  },
			  cursor: {
				show: false
			  },
			  seriesDefaults: {
				lineWidth: 1
			  }
		  });	
		  
		  var plot5 = $.jqplot('chart5', [dataSetsSemanal.line4, dataSetsSemanal.line5], {
			  title:'Dia (24 horas)',
			  axes:{
				xaxis:{
				  renderer:$.jqplot.DateAxisRenderer,
				  tickOptions:{
					formatString:'%#H'
				  } 
				},
				yaxis:{
				  tickOptions:{
					formatString:''
					},
					min:0, max: 900, numberTicks:7
				}
			  },
			  /*legend: {
				show: true,
				placement: 'outsideGrid',
				labels: labels,
				location: 'ne',
				rowSpacing: '0px'
			},*/
			  seriesColors: [ "#04B431","#0B6121"],
			  
			  highlighter: {
				show: true,
				sizeAdjust: 6
			  },
			  cursor: {
				show: false
			  },
			  seriesDefaults: {
				lineWidth: 1
			  }
		  });	
		
		$("input.dataSeries-checkbox").change(function(){ 
			plot1.series[0].show = false;
			plot1.series[1].show = false;
			plot1.series[2].show = false;			

			if ($('input[name=dataSeries_diaAgps]').get(0).checked === true) {
				plot1.series[0].show = true;
			}
			if ($('input[name=dataSeries_diaTriangulacion]').get(0).checked === true) {
				plot1.series[1].show = true;
			}
			if ($('input[name=dataSeries_diaCelda]').get(0).checked === true) {
				plot1.series[2].show = true;
			}
			
			plot1.replot();
		});	
		$("input.dataSeries-checkbox2").change(function(){ 
			plot2.series[0].show = false;
			plot2.series[1].show = false;
			plot2.series[2].show = false;
			
			if ($('input[name=dataSeries_national2]').get(0).checked === true) {
				plot2.series[0].show = true;
			}
			if ($('input[name=dataSeries_urban2]').get(0).checked === true) {
				plot2.series[1].show = true;
			}
			if ($('input[name=dataSeries_rural2]').get(0).checked === true) {
				plot2.series[2].show = true;
			}
			
			plot2.replot();
		});	
		$("input.dataSeries-checkbox3").change(function(){ 
			plot3.series[0].show = false;
			plot3.series[1].show = false;
			plot3.series[2].show = false;
			
			if ($('input[name=dataSeries_national3]').get(0).checked === true) {
				plot3.series[0].show = true;
			}
			if ($('input[name=dataSeries_urban3]').get(0).checked === true) {
				plot3.series[1].show = true;
			}
			if ($('input[name=dataSeries_rural3]').get(0).checked === true) {
				plot3.series[2].show = true;
			}
			plot3.replot();
		});	
		$("input.dataSeries-checkbox4").change(function(){ 
			plot4.series[0].show = false;
			plot4.series[1].show = false;			
			
			if ($('input[name=dataSeries_exitosas4]').get(0).checked === true) {
				plot4.series[0].show = true;
			}
			if ($('input[name=dataSeries_totales4]').get(0).checked === true) {
				plot4.series[1].show = true;
			}
			plot4.replot();
		});		
		
		$("input.dataSeries-checkbox5").change(function(){ 
			plot5.series[0].show = false;
			plot5.series[1].show = false;			
			
			if ($('input[name=dataSeries_exitosas5]').get(0).checked === true) {
				plot5.series[0].show = true;
			}
			if ($('input[name=dataSeries_totales5]').get(0).checked === true) {
				plot5.series[1].show = true;
			}
			plot5.replot();
		});
		
		$("input.dataSeries-checkbox6").change(function(){ 
			plot6.series[0].show = false;
			plot6.series[1].show = false;			
			
			if ($('input[name=dataSeries_exitosas6]').get(0).checked === true) {
				plot6.series[0].show = true;
			}
			if ($('input[name=dataSeries_totales6]').get(0).checked === true) {
				plot6.series[1].show = true;
			}
			plot6.replot();
		});
    });
</script> 