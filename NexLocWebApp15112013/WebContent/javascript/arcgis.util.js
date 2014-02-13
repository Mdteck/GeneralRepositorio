var symbol_glob = new esri.symbol.PictureMarkerSymbol("images/orange.png",14,17);
var symbol_nsn = new esri.symbol.PictureMarkerSymbol("images/red.png",25,25);
var symbol_acc = new esri.symbol.PictureMarkerSymbol("images/green.png",25,25);
var symbol_priv = new esri.symbol.PictureMarkerSymbol("images/blue.png",25,25);
var symbol_mdtk = new esri.symbol.PictureMarkerSymbol("images/green-modificado.png",30,40);

var symbol = new esri.symbol.PictureMarkerSymbol("images/cell.png",25,25);
var symbol_pi = new esri.symbol.PictureMarkerSymbol("images/bluedot.png",30,30);
var symbol_subpi = new esri.symbol.PictureMarkerSymbol("images/blackdot.png",10,10);
var symbol_piInicio = new esri.symbol.PictureMarkerSymbol("images/reddot.png",30,30);

var symbol_cel = new esri.symbol.PictureMarkerSymbol("images/orange.png",25,25);
var symbol_estrella = new esri.symbol.PictureMarkerSymbol("images/estrella.png",30,30);
var symbol_chinche = new esri.symbol.PictureMarkerSymbol("images/chincheAzul.gif",30,30);

var graphic;
var cargarPosicion=true;
var direccionObtenida=false;
var puntosInteresObjetivoLayer = new esri.layers.GraphicsLayer();
var puntosInteresLayer = new esri.layers.GraphicsLayer();
var bufferLayer;
var infoTemplateGlobal;

function localizar(entidad, idSeleccionado){ 
	map.graphics.clear();
	$("#time").html("");
	$("#loading").html('<img src="images/loading.gif" alt="cargando"/> Localizando...').fadeIn(300);
	$('#btnLocalizar').attr('disabled', 'disabled');
	//var idSeleccionado=-1;
	if (entidad == 'usuario') {
		idSeleccionado = $("#idUsuario").val();
	}else if(entidad=='grupo'){
		$("#data-localizacion").html("");
		idSeleccionado = $("#idGrupo").val();
	}
	if(idSeleccionado!=-1){
	var letra=entidad.substring(0,1).toUpperCase();
	var accion="localizar"+letra+entidad.substring(1);
	var id=$("#id"+letra+entidad.substring(1)).val();
		var data = new Array();
		data[data.length]=new param(entidad+".id"+entidad.substr(0,1).toUpperCase()+entidad.substr(1),id); 
		doAjax(accion, data, "POST", function(resp){
			if(resp.tipoRpta=="OK" || resp.tipoRpta=="WARN"){
				map.graphics.clear();
				puntosInteresObjetivoLayer.clear();
				if(entidad=='usuario'){
					var coordenada =resp.posicion.coordenada;
					var usuario = resp.posicion.usuario;
					var pt=new esri.geometry.Point(coordenada.longitud,coordenada.latitud,map.spatialReference);
					$('#pto').val(coordenada.longitud+","+coordenada.latitud);
					
					map.setLevel(levelInicial);
					map.centerAt(pt);
					var time=resp.posicion.timestamp;
					var horaFormateada = time.substring(8,10)+"/"+time.substring(5,7)+"/"+time.substring(0,4)+" "+time.substring(11,16);
					var infoTemplate = new esri.InfoTemplate("Usuario", "<tr><td>"+usuario.etiqueta+"<br/></td></tr><tr><td>Numero: "+usuario.numero+"<br/></td></tr>" +
							"<tr><td>" + horaFormateada + "<br/></td></tr><tr><td>" + resp.posicion.metodo + "<br/></td></tr>");
					
					var radio = 0;
					if(resp.posicion.metodo=='CELDA')				radio=RADIO_CELDA;
					else if(resp.posicion.metodo=='TRIANGULACION')	radio=RADIO_TRIANGULACION;
					else if(resp.posicion.metodo=='AGPS')			radio=RADIO_AGPS;

					var params = new esri.tasks.BufferParameters();
			        params.geometries = [ pt ];
			        params.distances = [ radio ];
			        params.unit = esri.tasks.GeometryService.UNIT_METER;
			        params.outSpatialReference = map.spatialReference;
			        infoTemplateGlobal=infoTemplate;
			        bufferLayer=new esri.layers.GraphicsLayer(); 
			        map.addLayer(bufferLayer);
			        gsvc.buffer(params, function(features){
					    showBuffer(features);
					});

			        mostrarLabel(pt, usuario.numero);
				    
					mostrar(pt, infoTemplate, symbol_cel);
					
					
				    $("#obj").attr("value",$("#idUsuario option:selected").val());
					$("#loading").html("").hide();
					$("#time").html(" <img src='images/check.jpg' width='15' /> "+usuario.etiqueta+" ("+usuario.numero+")"+" fue encontrado a las " + horaFormateada + " por "+resp.posicion.metodo);
					$('#btnLocalizar').removeAttr('disabled');
				}else if(entidad=='grupo'){
					map.graphics.clear();
					pisObj.length=0;
					var posiciones = resp.posiciones;
					var htmlData = "";
					var horaFormateada="";
					bufferLayer=new esri.layers.GraphicsLayer();
					map.addLayer(bufferLayer);
					if(posiciones.length!=0){
						var centro = resp.centro;
						for(var i = 0; i<posiciones.length; i++){
							var usuario = posiciones[i].usuario;
							var coordenada = posiciones[i].coordenada;
							var pt=new esri.geometry.Point(coordenada.longitud,coordenada.latitud,map.spatialReference);
							var time=resp.posiciones[0].timestamp;
							horaFormateada = time.substring(8,10)+"/"+time.substring(5,7)+"/"+time.substring(0,4)+" "+time.substring(11,16);
							
							var infoTemplate = new esri.InfoTemplate("Usuario", "<tr><td>"+usuario.etiqueta+"<br/></td></tr><tr><td>Numero: "+usuario.numero+"<br/></td></tr>" +
									"<tr><td>" + horaFormateada + "<br/></td></tr><tr><td>" + posiciones[i].metodo + "<br/></td></tr>");
							
							var radio = 0;
							if(posiciones[i].metodo=='CELDA')				radio=RADIO_CELDA;
							else if(posiciones[i].metodo=='TRIANGULACION')	radio=RADIO_TRIANGULACION;
							else if(posiciones[i].metodo=='AGPS')			radio=RADIO_AGPS;

							var params = new esri.tasks.BufferParameters();
					        params.geometries = [ pt ];
					        params.distances = [ radio ];
					        params.unit = esri.tasks.GeometryService.UNIT_METER;
					        params.outSpatialReference = map.spatialReference;
					        infoTemplateGlobal=infoTemplate;
					         
					        gsvc.buffer(params, function(features){
					        	showBuffer(features);
					        });

					        mostrarLabel(pt, usuario.numero);
							mostrar(pt, infoTemplate, symbol_cel);

							htmlData += "<tr><td>" + usuario.etiqueta + " (" + usuario.numero + ")</td>" +
									"<td>" + posiciones[i].metodo + "</td></tr>";
						}
						var center;
						if(posiciones.length==1)
							center = new esri.geometry.Point(posiciones[0].coordenada.longitud,posiciones[0].coordenada.latitud,map.spatialReference);
						else
							center = new esri.geometry.Point(centro.longitud,centro.latitud,map.spatialReference);
						map.centerAt(center);
					}
					$("#loading").html("").hide();
					if(resp.tipoRpta=='OK')
						$("#time").html(" <img src='images/check.jpg' width='15' /> El "+$("#idGrupo option:selected").text()+" fue encontrado a las "+horaFormateada).fadeIn(300);
					else if(resp.tipoRpta=="WARN")
						$("#time").html(" <img src='images/check.jpg' width='15' />"+resp.mensaje);
					if (resp.usuariosBloqueados.length > 0) {
						for (var i = 0; i < resp.usuariosBloqueados.length; i++) {
							htmlData += "<tr><td>" + resp.usuariosBloqueados[i].etiqueta + 
									" (" + resp.usuariosBloqueados[i].numero + ")</td>" +
									"<td>Bloqueado</td></tr>";
						}
					}
					if (resp.usuariosErrorLocalizacion.length > 0) {
						for (var i = 0; i < resp.usuariosErrorLocalizacion.length; i++) {
							//TODO cambio por ALONSODELAQ 16/07
							htmlData += "<tr><td>" + resp.usuariosErrorLocalizacion[i].usuario.etiqueta + 
							" (" + resp.usuariosErrorLocalizacion[i].usuario.numero + ")</td>" +
							"<td>No ubicado</td></tr>";
							//TODO FIN DEL CAMBIO
						}
					}
					if ((posiciones.length + resp.usuariosBloqueados.length + resp.usuariosErrorLocalizacion.length) == 0) {
						$("#loading").html("").hide();
						$("#time").html(" <img src='images/error.jpg' width='15' /> El "+$("#idGrupo option:selected").text()+" no tiene ningun usuario asignado.");
						htmlData = "<tr><td colspan='2' align='center' style='color: red;'>" +
								"El "+$("#idGrupo option:selected").text()+" no tiene ningun usuario asignado.</td></tr>";
					}
					$("#data-localizacion").html(htmlData);
					$('#btnLocalizar').removeAttr('disabled');
				}else{
					//Entidad inv√°lida
				}
			}else{
				$("#loading").html('');
				if(entidad=="grupo") $("#time").html(" <img src='images/error.jpg' width='15' /> No se encontro a "+$("#idGrupo option:selected").text()).fadeIn(300);
				else if(entidad=="usuario"){
					$("#time").html(" <img src='images/error.jpg' width='15' /> "+resp.mensaje);
				}else {
					$("#time").html(" <img src='images/error.jpg' width='15' /> Ocurrio un error al localizar al usuario.");
				}
				$('#btnLocalizar').removeAttr('disabled');
			}
		},function(e){
			$("#loading").html('');
			$("#time").html(" <img src='images/error.jpg' width='15' /> Hubo un error comunicandose con el servidor.").fadeIn(300);
			$('#btnLocalizar').removeAttr('disabled');
		});
//		if($("#tiempo option:selected").val() != '0'){
//			var min = parseInt($("#tiempo option:selected").val()) * 60000;
//			setTimeout("localizar('"+entidad+"')", min);
//		}
	}else{
		$("#loading").html('');
		$("#time").html(" <img src='images/error.jpg' width='15' /> Debe seleccionar una opcion").fadeIn(300);
		$('#btnLocalizar').removeAttr('disabled');
	}
	
//	},500);
}

function loc(entidad){
	var idSeleccionado=-1;
	if (entidad == 'usuario') {
		idSeleccionado = $("#idUsuario").val();
	}else if(entidad=='grupo'){
		$("#data-localizacion").html("");
		idSeleccionado = $("#idGrupo").val();
	}
	if(idSeleccionado!=-1){
		//console.log(entidad);
		if($("#tiempo option:selected").val() != '0'){
			var min = parseInt($("#tiempo option:selected").val()) * 60000;
			setInterval(function(){localizar(entidad, idSeleccionado);}, min);
			//setInterval(function(){ alert("'"+entidad+"'"); }, min);
		}
	}
	localizar(entidad, idSeleccionado);
	
	
}

function mostrarGraphic(graphic, infoTemplate){
	graphic.setInfoTemplate(infoTemplate);
	map.graphics.add(graphic);
}

function mostrar(pt, infoTemplate, symbol){
	var g = new esri.Graphic(pt,symbol);
	g.setInfoTemplate(infoTemplate);
    map.graphics.add(g);
}

function puntosInteres(nombre,categoria,longitud,latitud){
	var infoTemplate = new esri.InfoTemplate("Punto Interes", "<tr>Nombre: <td>"+nombre+"</tr></td><br><tr>Categoria: <td>"+categoria+"</tr></td>");
	var pt=new esri.geometry.Point(longitud,latitud,map.spatialReference);
	map.centerAt(pt);
	mostrar(pt,infoTemplate,symbol_priv);
}

function puntosInteres_pi(nombre,direccion,imagen,longitud,latitud, categoria){
	var img;
	if(imagen == "null") img = "<img style='height: 50px;' src='images/icon_galeria.png' />";
	else img = "<img style='height: 50px; ' src='"+imagen+"' />";
	var cat = "";
	if(categoria!=undefined)
		cat="<tr>Categoria: <td>"+categoria+"</tr></td><br>";
	var infoTemplate = new esri.InfoTemplate("Punto Interes", "<tr>Nombre: <td>"+nombre+"</tr></td><br>"+cat+"<tr>Direccion: <td>"+direccion+"</tr></td><br><tr>Imagen: <td>"+img+"</tr></td>");
	var pt=new esri.geometry.Point(longitud,latitud,map.spatialReference);
	map.centerAt(pt);
	mostrar(pt,infoTemplate,symbol_priv);
}

function showMap(){
	var nombre=$(".mapa .nombre").text();
	//var categoria=$(".mapa .categoria").text();
	var longitud=$(".mapa .longitud").val();
	var latitud=$(".mapa .latitud").val();
	var direccion=$(".mapa .direccion").text();
	var imagen=$(".mapa .nombre input").val();
	$(".mapa").removeClass("mapa");
	puntosInteres_pi(nombre,direccion,imagen,longitud,latitud);
}
/*Inicio 24/06/2013 MDTECK Sandy Huanco*/
function showMapMDTECK(){
	var usuario=$("#usuario").val();
	var longitud=$("#longitud").val();
	var latitud=$("#latitud").val();
	var direccion=$("#direccion").val();
	var metodo=$("#metodo").val();
	var distrito=$("#distrito").val();
	var infoTemplate = new esri.InfoTemplate("Reporte de Localizacion", "Celular: "+usuario+"<br>Distrito:"+distrito+"<br>Direccion: <td>"+direccion+"</tr></td><br><tr>Metodo: <td>"+metodo+"</tr></td>");
	var pt=new esri.geometry.Point(longitud,latitud,map.spatialReference);
	var g = new esri.Graphic(pt,symbol_priv);
	g.setInfoTemplate(infoTemplate);
	map.graphics.add(g);
	map.centerAt(pt);		
}

function showMapMDTECK2(){
	var usuario = localStorage.getItem('usuarioM');
	var longitud = localStorage.getItem('longitudM');
	var latitud = localStorage.getItem('latitudM');
	var distrito = localStorage.getItem('distritoM');
	var direccion = localStorage.getItem('direccionM');
	var metodo = localStorage.getItem('metodoM');
		
	var infoTemplate = new esri.InfoTemplate("Reporte de Localizacion", "Celular: "+usuario+"<br>Distrito:"+distrito+"<br>Direccion: <td>"+direccion+"</tr></td><br><tr>Metodo: <td>"+metodo+"</tr></td>");
	var pt=new esri.geometry.Point(longitud,latitud,map.spatialReference);
	var g = new esri.Graphic(pt,symbol_priv);
	g.setInfoTemplate(infoTemplate);
	map.graphics.add(g);
	map.centerAt(pt);		
}

function ubicarMapa(pos,longitud, latitud, time, metodo, direccion, distrito, tamanio){
	initToolbarTracking(map);
	var i=pos+1;
	var etiqueta = $('#nombreLocalizacion').val();
//	var infoTemplate = new esri.InfoTemplate("Ubicacion "+(i+1),"Metodo:"+data[3]+"<br>"+data[2]+"<br>Distrito: "+data[5]+"<br>Direccion:"+data[4]);	
	var infoTemplate;
	if(pos==1){
		infoTemplate = new esri.InfoTemplate("Tracking", "<tr>Nombre: <td>"+etiqueta+"</tr></td><br><tr><b>Primera Localizacion</b>: <td>"+time+"</tr></td><br><tr>Metodo: <td>"+metodo+"</tr></td><br>Ditrito: "+distrito+"<br>Direccion: "+direccion);
	}else{
		if(pos==tamanio){
			infoTemplate = new esri.InfoTemplate("Tracking", "<tr>Nombre: <td>"+etiqueta+"</tr></td><br><tr><b>Ultima Localizacion</b>: <td>"+time+"</tr></td><br><tr>Metodo: <td>"+metodo+"</tr></td><br>Distrito: "+distrito+"<br>Direccion: "+direccion);
		}else{
			infoTemplate = new esri.InfoTemplate("Ubicacion "+pos,"Metodo:"+metodo+"<br>"+time+"<br>Distrito: "+distrito+"<br>Direccion:"+direccion);	
		}
	}
	
	var pt=new esri.geometry.Point(longitud,latitud,map.spatialReference);
	var g = new esri.Graphic(pt,symbol_mdtk);
	g.setInfoTemplate(infoTemplate);
	map.graphics.add(g);
	map.centerAt(pt);
	$("#latitudSel").val(latitud);
	$("#longitudSel").val(longitud);
	$("#posicionSel").val(pos);
}
/*fin MDTECK*/
function showPuntoInteres(){
	dojo.connect(locator, "onLocationToAddressComplete", onLocationToAddressComplete);
	if(flag_mapa){
		dojo.connect(map,"onClick", seleccionarPunto);
		var nombre = $("#nombre").val();
		var categoria = $("#categoria option:selected").text();
		var longitud=$("#longitud").val();
		var latitud=$("#latitud").val();
		var direccion=$("#direccion").val();
		var imagen=$('#img_prev').attr('src');
		if(imagen==undefined) imagen="null";
		puntosInteres_pi(nombre,direccion, imagen, longitud,latitud, categoria);
	}else{
		var xmax=0.0, xmin=0.0, ymax=0.0, ymin=0.0;
		var list = $(".elegir:checked").get();
		for(var i=0; i<list.length;i++){
			var longitud = $(list[i]).parent().find(".longitud").val();
			var latitud = $(list[i]).parent().find(".latitud").val();
			var direccion= $(list[i]).parent().find(".direccion").val();
			//TODO regresar a la normalidad
//			var categoria = $(list[i]).parent().parent().find(".categoria").text();
			var categoria = $(list[i]).parent().find(".categoria").val();
			var imagen=$(list[i]).parent().parent().find(".nombre").find('input').val();
			var nombre = $(list[i]).parent().parent().find(".nombre").text();
			puntosInteres_pi(nombre,direccion,imagen,longitud,latitud, categoria);
			var lon=parseFloat(longitud);
			var	lat=parseFloat(latitud);
			if(i==0){
				xmin=lon;
				xmax=lon;
				ymin=lat;
				ymax=lat;
			}else{
				if(lon<xmin) xmin=lon;
				if(lon>xmax) xmax=lon;
				if(lat<ymin) ymin=lat;
				if(lat>ymax) ymax=lat;
			}
		}
		xmax-=0.0005;
		ymax-=0.0005;
		xmin+=0.0005;
		xmax+=0.0005;
		if(list.length==1){
			var pt=new esri.geometry.Point(xmin,ymin,map.spatialReference);
			map.centerAt(pt);
		}else{
			var difx=xmax-xmin;
			var dify=ymax-ymin;
			var centerX=xmax-difx/2;
			var centerY=ymax-dify/2;
			var pt=new esri.geometry.Point(centerX,centerY,map.spatialReference);
			map.centerAndZoom(pt,0);
		}
	}
	
}
function existePunto(id){
    if(pis.length==0) return false;
    for(var i=0; i<pis.length; i++){
            if (pis[i]==id)        return true;
            else                        return false;
    }
    
}

function puntosInteresObjetivoLayer(id){
    if(pisObj.length==0) return false;
    for(var i=0; i<pisObj.length; i++){
            if (pisObj[i]==id)        	return true;
            else                        return false;
    }
    
}

function seleccionarPunto(evt) {
	map.graphics.clear();
	direccionObtenida = false;
	locator.locationToAddress(evt.mapPoint, 100);
    
  }

function initToolbar(map) {
  tb = new esri.toolbars.Draw(map);
  dojo.connect(tb, "onDrawEnd", addGraphic);
  crearGeocerca($("#rings").val());
}

function initToolbarTracking(map){
	tb = new esri.toolbars.Draw(map);
	dojo.connect(tb, "onDrawEnd", addGraphicTracking);
	crearTracking($("#path").val());
}

function addGraphic(geometry) {
	map.graphics.clear();
  var symbol = new esri.symbol.SimpleMarkerSymbol(esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE, 10, new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([255,0,0]), 2), new dojo.Color([255,0,0,0.25]));
  if (symbol) {
      symbol = eval(symbol);
  }
  map.graphics.add(new esri.Graphic(geometry, symbol));
  $("#rings").attr("value",geometry.rings);
}

function addGraphicTracking(geometry){
	map.graphics.clear();
	var symbol = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([243,133,16]), 3);
	if (symbol) {
	      symbol = eval(symbol);
	  }
	  map.graphics.add(new esri.Graphic(geometry, symbol));
	  var points=$('#points').val();
	  var pointsArray=points.split("$$");
	  for(var i=0; i<pointsArray.length;i++){
		  var c = pointsArray[i];
		  //console.info("data:"+c);
		  var data = c.split('ø?');
		  var infoTemplate = new esri.InfoTemplate("Ubicacion "+(i+1),"Metodo:"+data[3]+"<br>"+data[2]+"<br>Distrito: "+data[5]+"<br>Direccion:"+data[4]);	
		  if(data[0].length!=0){
			  var pt=new esri.geometry.Point(data[0],data[1],map.spatialReference);
			  var g = new esri.Graphic(pt,symbol_subpi);
			  g.setInfoTemplate(infoTemplate);
			  map.graphics.add(g);			  
		  }
	  }	  
	  
	  
	  var c = $('#pointsFinal').val();
	  var coordenada = c.split('$');
	  var infoTemplate = new esri.InfoTemplate("Tracking", "<tr>Nombre: <td>"+$('#nombreLocalizacion').val()+"</tr></td><br><tr><b>Ultima Localizacion</b>: <td>"+coordenada[2]+"</tr></td><br><tr>Metodo: <td>"+coordenada[3]+"</tr></td><br>Distrito: "+coordenada[5]+"<br>Direccion: "+coordenada[4]);
	  if(coordenada[0].length!=0){
		  var pt=new esri.geometry.Point(coordenada[0],coordenada[1],map.spatialReference);
		  var g = new esri.Graphic(pt,symbol_pi);
		  g.setInfoTemplate(infoTemplate);
		  map.graphics.add(g);
		//TODO CAMBIO POR ALONSODELAQ 15/07
		  $('#pto').val(coordenada[0]+","+coordenada[1]);
		 //TODO FIN DE CAMBIO
		  map.centerAt(pt);
		  //$("#path").attr("value",geometry.paths); 
	  }
	  	 
	  
	  var c = $('#pointsInicial').val();  
	  var coordenada = c.split('$');
	  var infoTemplate = new esri.InfoTemplate("Tracking", "<tr>Nombre: <td>"+$('#nombreLocalizacion').val()+"</tr></td><br><tr><b>Primera Localizacion</b>: <td>"+coordenada[2]+"</tr></td><br><tr>Metodo: <td>"+coordenada[3]+"</tr></td><br>Ditrito: "+coordenada[5]+"<br>Direccion: "+coordenada[4]);
	  if(coordenada[0].length!=0){
		  var pt=new esri.geometry.Point(coordenada[0],coordenada[1],map.spatialReference);
		  var g = new esri.Graphic(pt,symbol_piInicio);
		  g.setInfoTemplate(infoTemplate);
		  map.graphics.add(g);
	  }
	  
}

function dibujarGeocerca(e){
	if(e){
		$("#rings").attr("value","");
		tb.activate(esri.toolbars.Draw.FREEHAND_POLYGON);
		estado = false;
		$("#draw").text("Mover Mapa");
	}else{
		tb.deactivate();
		estado=true;
		$("#draw").text("Dibujar Geocerca");
	}

}
function crearGeocerca(polygonJson){
	polygonJson = jQuery.parseJSON(polygonJson);
	var polygon = new esri.geometry.Polygon(polygonJson);
	addGraphic(polygon);
	map.centerAt(polygon.getExtent().getCenter());
	$('#pto').val(polygon.getExtent().getCenter().x+","+polygon.getExtent().getCenter().y);
}


function crearTracking(polylineJSON){
	polylineJSON = jQuery.parseJSON(polylineJSON);
	var polyline = new esri.geometry.Polyline(polylineJSON);
	addGraphicTracking(polyline);
}

function puntosInteresObjetivo(){
	var levelAcualMap = map.getLevel();
	if(levelAcualMap>=levelPI){

		if(showPI){
			var data = new Array();
			data[data.length] = new param("ymax", parseFloat(map.extent.ymax));
			data[data.length] = new param("ymin", parseFloat(map.extent.ymin));
			data[data.length] = new param("xmax", parseFloat(map.extent.xmax));
			data[data.length] = new param("xmin", parseFloat(map.extent.xmin));
			data[data.length] = new param("idUsuario", $("#obj").val());
			 doAjax("mapaPuntoInteres", data, 'GET', function(resp){
				 if(resp.tipoRpta == "OK"){
					 var puntos = resp.puntosInteres;
					 for(var i=0; i<puntos.length;i++){
						 if(!existePunto(puntos[i].idPuntoInteres)){
							 	var pt=new esri.geometry.Point(puntos[i].coordenada.longitud,puntos[i].coordenada.latitud,map.spatialReference);
								var img = "";
								if(puntos[i].rutaImagen == null) img = "<img src='images/icon_galeria.png' />";
								else img = "<img src='"+puntos[i].rutaImagen+"' />";
		
								var infoTemplate = new esri.InfoTemplate("Punto Interes", "<tr>Nombre: <td>"+puntos[i].nombre+"</tr></td><br><tr>Categoria: <td>"+puntos[i].categoria.categoria+"</tr></td><br><tr>Direccion: <td>"+ puntos[i].direccion +"</tr></td><br><tr>Direccion: <td>"+ puntos[i].distrito +"</tr></td><br><tr>Imagen: <td>"+img+"</tr></td>");
								var g = new esri.Graphic(pt,symbol_chinche);
								g.setInfoTemplate(infoTemplate);
								puntosInteresObjetivoLayer.add(g);
								pis[pis.length] = puntos[i].idPuntoInteres;
						 }
					 }
	
				 }
			 },function (e){
				 
			 });
		}
	}
}

function puntosInteresGlobales(){
	var levelAcualMap = map.getLevel();
	if(levelAcualMap>=levelPI){
		if(showPI){
			var data = new Array();
			data[data.length] = new param("ymax", parseFloat(map.extent.ymax));
			data[data.length] = new param("ymin", parseFloat(map.extent.ymin));
			data[data.length] = new param("xmax", parseFloat(map.extent.xmax));
			data[data.length] = new param("xmin", parseFloat(map.extent.xmin));
			//data[data.length] = new param("idCuenta", localStorage.getItem('cuenta')); 
			 doAjax("mapaPuntoInteres", data, 'GET', function(resp){
				 if(resp.tipoRpta == "OK"){
					 var puntos = resp.puntosInteres;
		
					 for(var i=0; i<puntos.length;i++){
						 if(!existePunto(puntos[i].idPuntoInteres)){
							 	var pt=new esri.geometry.Point(puntos[i].coordenada.longitud,puntos[i].coordenada.latitud,map.spatialReference);
								var img = "";
								if(puntos[i].rutaImagen == null) img = "<img src='images/icon_galeria.png' />";
								else img = "<img src='"+puntos[i].rutaImagen+"' />";
		
								var infoTemplate = new esri.InfoTemplate("Punto Interes", "<tr>Nombre: <td>"+puntos[i].nombre+"</tr></td><br><tr>Categoria: <td>"+puntos[i].categoria.categoria+"</tr></td><br><tr>Imagen: <td>"+img+"</tr></td>");
								var g = new esri.Graphic(pt, symbol_estrella);
								g.setInfoTemplate(infoTemplate);
								puntosInteresLayer.add(g);
								pis[pis.length] = puntos[i].idPuntoInteres;
						 }
					 }
	
				 }
			 },function (e){
				 
			 });
		}
	}
}
function puntosInteresCuenta(){
	var levelAcualMap = map.getLevel();
	if(levelAcualMap>=levelPI){

		if(showPI){
			var data = new Array();
			data[data.length] = new param("ymax", parseFloat(map.extent.ymax));
			data[data.length] = new param("ymin", parseFloat(map.extent.ymin));
			data[data.length] = new param("xmax", parseFloat(map.extent.xmax));
			data[data.length] = new param("xmin", parseFloat(map.extent.xmin));
			data[data.length] = new param("idCuenta", localStorage.getItem('cuenta')); 
			 doAjax("mapaPuntoInteres", data, 'GET', function(resp){
				 if(resp.tipoRpta == "OK"){
					 var puntos = resp.puntosInteres;
		
					 for(var i=0; i<puntos.length;i++){
						 if(!existePunto(puntos[i].idPuntoInteres)){
							 	var pt=new esri.geometry.Point(puntos[i].coordenada.longitud,puntos[i].coordenada.latitud,map.spatialReference);
								var img = "";
								if(puntos[i].rutaImagen == null) img = "<img src='images/icon_galeria.png' />";
								else img = "<img src='"+puntos[i].rutaImagen+"' />";
								var infoTemplate = new esri.InfoTemplate("Punto Interes", "<tr>Nombre: <td>"+puntos[i].nombre+"</tr></td><br><tr>Categoria: <td>"+puntos[i].categoria.categoria+"</tr></td><br><tr>Imagen: <td>"+img+"</tr></td>");
								var g = new esri.Graphic(pt, symbol_chinche);
								g.setInfoTemplate(infoTemplate);
								puntosInteresLayer.add(g);
								pis[pis.length] = puntos[i].idPuntoInteres;
						 }
					 }
	
				 }
			 },function (e){
				 
			 });
		}
	}
}
function ocultarPuntos(){
	if (showPI){
		map.removeLayer(puntosInteresLayer);
		map.removeLayer(puntosInteresObjetivoLayer);
		showPI=false;
	}else{
		showPI=true;
		map.addLayer(puntosInteresLayer);
		map.addLayer(puntosInteresObjetivoLayer);
		if(primeraCarga){
			puntosInteresGlobales();
			if($("#obj").val() != '0') {
				puntosInteresCuenta();
				puntosInteresObjetivo();
			}
			primeraCarga=false;
		}

	}
}
function initPuntosI(){
	dojo.connect(map, "onPanEnd", function(){
		puntosInteresGlobales();
		if($("#obj").val() != '0') {
			puntosInteresCuenta();
			puntosInteresObjetivo();
		}
	});
	
	dojo.connect(map, "onZoomEnd", function(){
		var levelMapa = map.getLevel();
		if(levelMapa>=levelPI){
			if(showPI==false){
				ocultarPuntos();
//				document.getElementById("showPI").checked=false;
			}
		}else{
			if(showPI){
				ocultarPuntos();
//				document.getElementById("showPI").checked=true;
			}
			
		}
		//ocultarPuntos();
	});
	
	dojo.connect(map.graphics, "onClick", function(evt) {
	  map.infoWindow.show(evt.screenPoint,map.getInfoWindowAnchor(evt.screenPoint));
	});
}

function onLocationToAddressComplete(candidate){
	direccionObtenida = true;
	if (candidate.address) {
		var graphic = new esri.Graphic(candidate.location, symbol_priv, candidate.address);
        map.graphics.add(graphic);
	    $("#longitud").attr("value",candidate.location.x);
		$("#latitud").attr("value",candidate.location.y);
		if (candidate.address.Street) {
			$("#direccion").attr("value",candidate.address.Street);
		}
		else if (candidate.address.Urbanizacion) {
			$("#direccion").attr("value",candidate.address.Urbanizacion);
		}
		else {
			$("#direccion").attr("value", "(No se pudo obtener una direccion para el punto indicado)");
		}
		$("#distrito").attr("value",candidate.address.Distrito);
    }
	else {
		$("#direccion").attr("value","(No se pudo obtener una direccion para el punto indicado)");
	}
}

function onLocatorError(error) {
	if (!direccionObtenida) {
		$("#direccion").attr("value","(Error al obtener direccion, intente nuevamente)");
	}
}

function mostrarLabel(pt, numero){
	var font  = new esri.symbol.Font();
	font.setSize("11pt");
	font.setWeight(esri.symbol.Font.WEIGHT_BOLD);
	var textSymbol = new esri.symbol.TextSymbol(numero);
	textSymbol.setColor( new dojo.Color([0, 0, 0]));
	textSymbol.setOffset(0,-25);
	textSymbol.setAlign(esri.symbol.TextSymbol.ALIGN_MIDDLE);
    textSymbol.setFont(font);      
    var gra = new esri.Graphic(pt,textSymbol);
    map.graphics.add(gra);
}

function focusMap(){
	var pto = $('#pto').val();
	if(pto.length!=0){
		var c = pto.split(',');
		var longitud = c[0];
		var latitud = c[1];
		var pt=new esri.geometry.Point(longitud,latitud,map.spatialReference);
		map.centerAt(pt);

	}
}

function showBuffer(geometries, bufferLayer) {

    var symbol = new esri.symbol.SimpleFillSymbol(
      esri.symbol.SimpleFillSymbol.STYLE_SOLID,
      new esri.symbol.SimpleLineSymbol(
        esri.symbol.SimpleLineSymbol.STYLE_SOLID,
        new dojo.Color([0,0,255,0.65]), 2
      ),
      new dojo.Color([0,0,255,0.35])
    );

    dojo.forEach(geometries, function(geometry) {
      var graphic = new esri.Graphic(geometry,symbol);
      bufferLayer.add(graphic);
    });
  }

