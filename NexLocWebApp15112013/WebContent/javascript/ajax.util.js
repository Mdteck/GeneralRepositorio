// localStorage.setItem('usuario', 6);
// localStorage.setItem('numero', '997531604');
// localStorage.setItem('tipo', 2);
// localStorage.setItem('cuenta', 1);

var initTable = true;
var elFiltro;
var optionscopy;
var showDataTable = true;

function filtrarSelectList() {
	var text = document.formulario.regexp.value;
	elFiltro.set(text);
}

/*Inicio 12/07/2013 MDTECK Sandy Huanco*/
function pasarParametrosExcelPuntosInteres() {
	var idCuenta, idUsuario, showPrivados;
	if (localStorage.getItem('tipo') != 0) {
		if (typeof flagPI != 'undefined' && flagPI) {
			idCuenta = localStorage.getItem('cuenta');
		} else {
			idUsuario = localStorage.getItem('usuario');
			idCuenta = localStorage.getItem('cuenta');
		}
	} else {
		idUsuario = localStorage.getItem('usuario');
	}

	if (typeof priv != 'undefined' && priv) {
		showPrivados = true;
	} else {
		showPrivados = false;
	}
		
	$('#txtIdCuenta').attr('value', idCuenta);
	$('#txtIdUsuario').attr('value', idUsuario);
	$('#txtShowPrivados').attr('value', showPrivados);
	
	document.forms["excelPuntosInteres"].submit();
}

/*Fin */

function listar(entidad) {
	var html = "";
	var colspan;
	var dataTable = true;
	var data = null;
	if (entidad == 'usuario') {
		data = new Array();
		data[data.length] = new param('usuario.cuenta.idCuenta', localStorage.getItem('cuenta'));
		colspan = 4;
	} else if (entidad == 'puntoInteres') {
		// TODO regresar a valor 5
		colspan = 6;
		data = new Array();
		if (localStorage.getItem('tipo') != 0) {
			if (typeof flagPI != 'undefined' && flagPI) {
				data[data.length] = new param("idCuenta", localStorage.getItem('cuenta'));
			} else {
				data[data.length] = new param("idUsuario", localStorage.getItem('usuario'));
				data[data.length] = new param("idCuenta", localStorage.getItem('cuenta'));
			}
		} else {
			data[data.length] = new param("idUsuario", localStorage.getItem('usuario'));
		}

		if (typeof priv != 'undefined' && priv) {
			data[data.length] = new param('showPrivados', true);
		} else {
			data[data.length] = new param('showPrivados', false);
		}

	} else if (entidad == 'handset')
		colspan = 4;
	else if (entidad == 'categoria')
		colspan = 3;
	else if (entidad == 'geocerca') {
		colspan = 9;
		data = new Array();
		data[data.length] = new param("geocerca.idUsuario", localStorage.getItem('usuario'));
	} else if (entidad == 'grupo') {
		colspan = 3;
		data = new Array();
		data[data.length] = new param("usuario.idUsuario", localStorage.getItem('usuario'));
		data[data.length] = new param('usuario.cuenta.idCuenta', localStorage.getItem('cuenta'));
	} else if (entidad == 'tracking') {
		colspan = 9;
		data = new Array();
		data[data.length] = new param("tracking.monitor.idUsuario",localStorage.getItem('usuario'));
	} else
		colspan = 0;
	$('#data-' + entidad).html("<tr><td colspan='"+ colspan+ "' align='center'><img src='images/loading.gif' /></td></tr>");
	var letra = entidad.substring(0, 1).toUpperCase();
	var accion = "listar" + letra + entidad.substring(1);
	doAjax(
			accion,
			data,
			'POST',
			function(resp) {
				if (resp) {
					if (resp.tipoRpta == 'OK') {
						if (entidad == 'usuario') {
							var data = resp.usuarios;
							for ( var i = 0; i < data.length; i++) {
								html += "<tr id='" + data[i].idUsuario + "'>";
								var etiqueta = data[i].etiqueta;
								etiqueta = etiqueta.length == 0 ? "---"	: etiqueta;
								html += "<td class='etiqueta'>" + etiqueta + "</td>";
								html += "<td class='numero'>" + data[i].numero + "</td>";
								html += "<td class='tipo'>" + data[i].tipo + "</td>";
								html += "<td><span class='detalle' onClick='detalles(\""+ entidad + "\", \"" + data[i].idUsuario
										+ "\")'><span><img title='Detalles' src='images/info.png'></span></span> <a href='#' title='Modificar' onClick='obtener(\""
										+ entidad+ "\", \""+ data[i].idUsuario+ "\")'> <img src='images/edit.png'></a></td>";
								html += "</tr>";
							}
							if (data.length == 0) {
								dataTable = false;
								html = "<tr><td colspan='4' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >No se encontro usuario usuario para esta cuenta.</td></tr>";
							}
						} else if (entidad == 'puntoInteres') {
							var data = resp.puntosInteres;
							for ( var i = 0; i < data.length; i++) {
								if (typeof flagPI != 'undefined' && flagPI) {
									if (typeof listarPI != 'undefined'
											&& listarPI) {
										html += "<tr id='"
												+ data[i].idPuntoInteres + "'>";
										html += "<td><input type='checkbox' class='elegir' value='"
												+ data[i].idPuntoInteres
												+ "'/>"
												+ "<input type='hidden' class='longitud' value='"
												+ data[i].coordenada.longitud
												+ "' />"
												+ "<input type='hidden' class='latitud' value='"
												+ data[i].coordenada.latitud
												+ "' />"
												+ "<input type='hidden' class='categoria' value='"
												+ data[i].categoria.categoria
												+ "' />"
												+ "<input type='hidden' class='direccion' value='"
												+ data[i].direccion
												+ "' />"
												+ "</td>";
										html += "<td class='nombre'><input type='hidden' value='"
												+ data[i].rutaImagen
												+ "' />"
												+ data[i].nombre + "</td>";
										// html+="<td
										// class='categoria'>"+data[i].categoria.categoria+"</td>";
										var fechaRegistroSplit = data[i].fechaRegistro
												.split("-");
										var fechaRegistro = fechaRegistroSplit[2]
												.substr(0, 2)
												+ "/"
												+ fechaRegistroSplit[1]
												+ "/" + fechaRegistroSplit[0];
										html += "<td>" + fechaRegistro
												+ "</td>";
										html += "<td><span class='detalle' onClick='detalles(\""
												+ entidad
												+ "\", \""
												+ data[i].idPuntoInteres
												+ "\")'><span><img title='Detalles' src='images/info.png'></span></span> <a href='#' title='Modificar' onClick='obtener(\""
												+ entidad
												+ "\", \""
												+ data[i].idPuntoInteres
												+ "\")'> <img src='images/edit.png'></a> <a href='#' onClick='return eliminar(\""
												+ entidad
												+ "\",\""
												+ data[i].idPuntoInteres
												+ "\")'><img title='Eliminar' src='images/delete.png'></a></td>";
										html += "</tr>";
									} else {
										// TODO colspan=7;
										colspan = 6;
										html += "<tr id='"
												+ data[i].idPuntoInteres
												+ "'><input type='hidden' class='longitud' value='"
												+ data[i].coordenada.longitud
												+ "' />"
												+ "<input type='hidden' class='latitud' value='"
												+ data[i].coordenada.latitud
												+ "' />"
												+ "<input type='hidden' class='categoria' value='"
												+ data[i].categoria.categoria
												+ "' />";
										var fechaRegistroSplit = data[i].fechaRegistro
												.split("-");
										var fechaRegistro = fechaRegistroSplit[2]
												.substr(0, 2)
												+ "/"
												+ fechaRegistroSplit[1]
												+ "/" + fechaRegistroSplit[0];
										html += "<td>" + fechaRegistro
												+ "</td>";
										html += "<td class='nombre'><input type='hidden' value='"
												+ data[i].rutaImagen
												+ "' />"
												+ data[i].nombre + "" + "</td>";
										// html+="<td
										// class='categoria'>"+data[i].categoria.categoria+"</td>";
										html += "<td class='usuario'>"
												+ data[i].usuario.etiqueta
												+ " - "
												+ data[i].usuario.numero
												+ "</td>";
										html += "<td class='direccion'>"
												+ data[i].direccion + "</td>";
										html += "<td class='distrito'>"
												+ data[i].distrito + "</td>";
										html += "<td><a href='#' onClick='buscarMapa(\""
												+ data[i].idPuntoInteres
												+ "\")'><img title='Ver Mapa' src='images/map.png' /></a></td>";
										html += "</tr>";
									}

								} else {
									html += "<tr id='" + data[i].idPuntoInteres
											+ "'>";
									html += "<td><input type='checkbox' class='elegir' value='"
											+ data[i].idPuntoInteres
											+ "'/>"
											+ "<input type='hidden' class='longitud' value='"
											+ data[i].coordenada.longitud
											+ "' />"
											+ "<input type='hidden' class='latitud' value='"
											+ data[i].coordenada.latitud
											+ "' /><input type='hidden' class='categoria' value='"
											+ data[i].categoria.categoria
											+ "'/><input type='hidden' class='direccion' value='"
											+ data[i].direccion + "' /></td>";
									html += "<td class='nombre'><input type='hidden' value='"
											+ data[i].rutaImagen
											+ "' />"
											+ data[i].nombre + "</td>";
									// html+="<td
									// class='categoria'>"+data[i].categoria.categoria+"</td>";
									var fechaRegistroSplit = data[i].fechaRegistro
											.split("-");
									var fechaRegistro = fechaRegistroSplit[2]
											.substr(0, 2)
											+ "/"
											+ fechaRegistroSplit[1]
											+ "/"
											+ fechaRegistroSplit[0];
									html += "<td>" + fechaRegistro + "</td>";
									html += "<td><span class='detalle' onClick='detalles(\""
											+ entidad
											+ "\", \""
											+ data[i].idPuntoInteres
											+ "\")'><span><img title='Detalles' src='images/info.png'></span></span> <a href='#' onClick='obtener(\""
											+ entidad
											+ "\", \""
											+ data[i].idPuntoInteres
											+ "\")'> <img title='Modificar' src='images/edit.png'></a> <a href='#' onClick='return eliminar(\""
											+ entidad
											+ "\",\""
											+ data[i].idPuntoInteres
											+ "\")'><img title='Eliminar' src='images/delete.png'></a></td>";
									html += "</tr>";
								}

							}
							if (data.length == 0) {
								dataTable = false;
								html = "<tr><td colspan='"
										+ colspan
										+ "' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >No se encontro ningun punto de interes para esta cuenta.</td></tr>";
							}
						} else if (entidad == 'categoria') {
							var data = resp.categorias;
							for ( var i = 0; i < data.length; i++) {
								html += "<tr>";
								html += "<td>" + data[i].categoria + "</td>";
								html += "<td>" + data[i].cantidadPuntoInteres
										+ "</td>";
								html += "<td><a href='#' onClick='obtener(\""
										+ entidad
										+ "\", \""
										+ data[i].idCategoria
										+ "\")'> <img title='Modificar' src='images/edit.png'></a> <a href='#' onClick='return eliminar(\""
										+ entidad
										+ "\",\""
										+ data[i].idCategoria
										+ "\")'><img title='Eliminar' src='images/delete.png'></a></td>";
								html += "</tr>";
							}
							if (data.length == 0) {
								dataTable = false;
								html = "<tr><td colspan='3' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >No se encontro ninguna categoria para esta cuenta.</td></tr>";
							}
						} else if (entidad == 'handset') {
							var data = resp.handsets;
							for ( var i = 0; i < data.length; i++) {
								html += "<tr>";
								html += "<td>" + data[i].modelo + "</td>";
								html += "<td>" + data[i].plataforma + "</td>";
								html += "<td>" + data[i].versionAplicacion
										+ "</td>";
								html += "<td>" + data[i].imagen + "</td>";
								html += "<td><a href='#' onClick='obtener(\""
										+ entidad
										+ "\", \""
										+ data[i].idHandset
										+ "\")'> <img title='Modificar' src='images/edit.png'></a> <a href='#' onClick='return eliminar(\""
										+ entidad
										+ "\",\""
										+ data[i].idHandset
										+ "\")'><img title='Eliminar' src='images/delete.png'></a></td>";
								html += "</tr>";
							}
							if (data.length == 0) {
								dataTable = false;
								html = "<tr><td colspan='4' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >No se encontro ningun handset para esta cuenta.</td></tr>";
							}
						} else if (entidad == 'geocerca') {
							var data = resp.geocercas;
							for ( var i = 0; i < data.length; i++) {
								html += "<tr id='" + data[i].idGeocerca + "'>";

								html += "<td class='nombre'>" + data[i].nombre
										+ "</td>";
								html += "<td>" + data[i].fechaRegistro
										+ "</td>";
								/* Mac - 230114 */
								html += "<td> Campo no oficial</td>";
								/* Fin*/
								html += "<td>" + data[i].horario.fechaInicio
										+ " " + data[i].horario.horaInicio
										+ "</td>";
								html += "<td>" + data[i].horario.fechaFin + ""
										+ data[i].horario.horaFin + "</td>";
								var estado = "";
								var z = "";
								estado = "Iniciado";
								z = "<a href='#' onClick='return eliminar(\"geocerca\",\""
										+ data[i].idGeocerca
										+ "\")'><img title='Cancelar' src='images/cancel.png'></a>";
								html += "<td>" + estado + "</td>";
								html += "<td>" + data[i].asignados + "</td>";
								html += "<td><span class='detalle' onClick='detalles(\""
										+ entidad
										+ "\", \""
										+ data[i].idGeocerca
										// TODO Guia Mac
										+ "\")'><span><img title='Detalles' src='images/info.png'></span></span> <a href='#' onClick='obtener(\""
										+ entidad
										+ "\", \""
										+ data[i].idGeocerca
										+ "\")'> <img title='Modificar' src='images/edit.png'></a> "
										+ z + "</td>";
								html += "</tr>";
							}
							if (data.length == 0) {
								dataTable = false;
								html = "<tr><td colspan='"
										+ colspan
										+ "' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >No se encontro ninguna geocerca para esta cuenta.</td></tr>";
							}
						} else if (entidad == 'grupo') {
							var data = resp.grupos;
							colspan = 3;
							for ( var i = 0; i < data.length; i++) {
								html += "<tr id='" + data[i].idGrupo + "'>";
								html += "<td class='nombre'>" + data[i].nombre
										+ "</td>";
								html += "<td>" + data[i].asignados + "</td>";
								html += "<td><span class='detalle' onClick='detalles(\""
										+ entidad
										+ "\", \""
										+ data[i].idGrupo

										+ "\")'><span><img title='Detalles' src='images/info.png'></span></span> <a href='#' onClick='obtener(\""
										+ entidad
										+ "\", \""
										+ data[i].idGrupo
										+ "\")'> <img title='Modificar' src='images/edit.png'></a> <a href='#' onClick='return eliminar(\""
										+ entidad
										+ "\",\""
										+ data[i].idGrupo
										+ "\")'><img title='Eliminar' src='images/delete.png'></a></td>";
								html += "</tr>";
							}
							if (data.length == 0) {
								dataTable = false;
								html = "<tr><td colspan='3' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >No se encontro ningun grupo para esta cuenta.</td></tr>";
							}
						} else if (entidad == 'tracking') {
							var data = resp.trackings;
							colspan = 8;
							for ( var i = 0; i < data.length; i++) {
								html += "<tr>";
								html += "<td class='nombre'>"+ data[i].monitoreado.etiqueta	+ "</td>";
								html += "<td>" + data[i].monitoreado.numero	+ "</td>";
								html += "<td>" + data[i].horario.fechaInicio + "</td>";
								html += "<td>" + data[i].horario.horaInicio	+ "</td>";
								html += "<td>" + data[i].horario.fechaFin+ "</td>";
								html += "<td>" + data[i].horario.horaFin+ "</td>";
								var z = "";
								z = "<a href='#' onClick='return detener(\""+ data[i].idTracking
										+ "\", \"tracking\")'><img title='Cancelar' src='images/cancel.png' /></a>";
								html += "<td>"
										+ z
										+ "<a href='#' onClick='verTracking(\""+ data[i].idTracking+"\","+"\""+	data[i].horario.fechaInicio+"\","+"\""+data[i].horario.fechaFin+"\","+"\""+data[i].monitoreado.etiqueta+"\","+"\""+data[i].monitoreado.numero+"\")'><img title='Ver Mapa' src='images/map.png' /></a></td>";
								html += "</tr>";
							}
							if (data.length == 0) {
								dataTable = false;
								html = "<tr><td colspan='"
										+ colspan
										+ "' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >No se encontro ningun registro para esta cuenta.</td></tr>";
							}
						}
					} else {
						html = "<tr><td colspan='"
								+ colspan
								+ "' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >"
								+ resp.mensaje + "</td></tr>";
						dataTable = false;
					}
					$('#data-' + entidad).html(html);
					if (dataTable) {
						$(".formato-tabla").dataTable(
								{
									"oLanguage" : {
										"sUrl" : "../javascript/language.txt"
									},
									"bSort" : false,
									"aLengthMenu" : [ [ 10, 25, 50, 100, -1 ],
											[ 10, 25, 50, 100, "Todos" ] ]
								});
					}

				}
			},
			function(e) {
				html = "<tr><td colspan='"
						+ colspan
						+ "' align='center'>Ocurrio un error al obtener la lista.</td></tr>";
				$('#data-' + entidad).html(html);
				dataTable = false;
			});
}

/*Inicio 12/07/2013 MDTECK Sandy Huanco*/
function pasarParametrosExcelTrackingHistorial() {	
	var id = localStorage.getItem('usuario');
	var filtro = $('#estado_tracking').attr('value');
	var fechaInicio = $('#fechaInicio').attr('value');
	var fechaFin = $('#fechaFin').attr('value');
	
	$('#txtMonitorId').attr('value', id);
	$('#txtEstadoTracking').attr('value', filtro);
	$('#txtFechaInicio').attr('value', fechaInicio);
	$('#txtFechaFin').attr('value', fechaFin);
	
	document.forms["excelTrackingHistorial"].submit();
}

/*Fin */
function historial() {
	var dataTable = true;
	var data = new Array();
	var html = "";
	data[data.length] = new param("tracking.monitor.idUsuario", localStorage.getItem('usuario'));
	doAjax("historialTracking",data,'GET',function(resp) {
				if (resp) {
					if (resp.tipoRpta == 'OK') {
						var data = resp.trackings;
						colspan = 8;
						for ( var i = 0; i < data.length; i++) {
							var strEstado = "";
							if (data[i].estado == 0)
								strEstado = 'Cancelado';
							else if (data[i].estado == 2)
								strEstado = 'Iniciado';
							else if (data[i].estado == 3)
								strEstado = 'Terminado';
							html += "<tr>";
							html += "<td class='nombre'>"+ data[i].monitoreado.etiqueta + "</td>";
							html += "<td>" + data[i].monitoreado.numero+ "</td>";
							html += "<td>" + data[i].horario.fechaInicio+ "</td>";
							html += "<td>" + data[i].horario.horaInicio+ "</td>";
							html += "<td>" + data[i].horario.fechaFin + "</td>";
							html += "<td>" + data[i].horario.horaFin + "</td>";
							html += "<td class='estado'>" + strEstado + "</td>";
							html += "<td><a href='#' onClick='verTracking(\""+ data[i].idTracking+"\",\""+data[i].horario.fechaInicio+"\",\""+data[i].horario.fechaFin+ "\",\""+data[i].monitoreado.etiqueta+"\",\""+data[i].monitoreado.numero+"\")'><img title='Ver Mapa' src='images/map.png' /></a></td>";
							html += "</tr>";
						}
						if (data.length == 0) {
							html = "<tr><td colspan='"+ colspan+ "' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >No se encontro ningun registro para esta cuenta.</td></tr>";
							dataTable = false;
							showDataTable = false;
						}
					} else {
						html = "<tr><td colspan='" + colspan+ "' align='center'>" + resp.mensaje+ "</td></tr>";
						dataTable = false;
						showDataTable = false;
					}
					$('#data-tracking').html(html);

					if (dataTable) {
						$(".formato-tabla").dataTable(
								{
									"oLanguage" : {
										"sUrl" : "../javascript/language.txt"
									},
									"bSort" : false,
									"bDestroy" : true,
									"aLengthMenu" : [ [ 10, 25, 50, 100, -1 ],
											[ 10, 25, 50, 100, "Todos" ] ]
								});
					}
				}
			});
}
function buscar(entidad) {
	var dataTable = true;
	if (entidad == "puntoInteres") {
		if ($("#puntoInteres-form input[name=nombre]") != "") {
			var colspan = 4;
			var data = obtenerDatosDeForm(entidad);
			data.push({
				name : entidad + ".usuario.idUsuario",
				value : localStorage.getItem('usuario')
			});
			var letra = entidad.substring(0, 1).toUpperCase();
			var html = "";
			var accion = "buscar" + letra + entidad.substring(1);
			doAjax(
					accion,
					data,
					'GET',
					function(resp) {
						if (resp) {
							if (resp.tipoRpta == 'OK') {
								$("#lista-puntoInteres").fadeIn(300);
								$("#buscar-puntosInteres").hide();
								var data = resp.puntosInteres;
								for ( var i = 0; i < data.length; i++) {
									html += "<tr id='" + data[i].idPuntoInteres
											+ "'>";
									html += "<td class='nombre'><input type='hidden' value='"+ data[i].rutaImagen+ "' />"
											+ data[i].nombre
											+ "<input type='hidden' class='longitud' value='"
											+ data[i].coordenada.longitud
											+ "' />"
											+ "<input type='hidden' class='latitud' value='"
											+ data[i].coordenada.latitud
											+ "' />" + "</td>";
									html += "<td class='categoria'>"
											+ data[i].categoria.categoria
											+ "</td>";
									html += "<td class='direccion'>"
											+ data[i].direccion + "</td>";
									html += "<td><a href='#' onClick='buscarMapa(\""
											+ data[i].idPuntoInteres
											+ "\")'><img title='Ver Mapa' src='images/map.png' /></a></td>";
									html += "</tr>";
								}

								if (data.length == 0) {
									html = "<tr><td colspan='4' align='center' style='color: red;'><img src='images/error.jpg' width='15' />No se encontro ningun punto de interes para esta cuenta.</td></tr>";
									dataTable = false;
								}

							} else {
								html = "<tr><td colspan='"
										+ colspan
										+ "' align='center' style='color: red;'><img src='images/error.jpg' width='15' />Ocurrio un error al obtener la lista.</td></tr>";
								dataTable = false;
							}

							$("#data-puntoInteres").html(html);
							if (dataTable) {
								$(".formato-tabla")
										.dataTable(
												{
													"oLanguage" : {
														"sUrl" : "../javascript/language.txt"
													},
													"bSort" : false,
													"aLengthMenu" : [
															[ 10, 25, 50, 100,
																	-1 ],
															[ 10, 25, 50, 100,
																	"Todos" ] ]
												});
							}
						}

					}, function(e) {
						alert("error - doAjax");
					});

		} else {
			$("#mensaje")
					.text(
							"<img src='images/error.jpg' width='15' />Debe escribir un nombre y seleccionar una categoria.")
					.fadeIn(300);
		}
	}
}

function registrar(entidad) {
	if (entidad == 'puntoInteres') {
		// Tratamiento especial para poder subir la imagen del punto de
		// interï¿½s vï¿½a Ajax
		registrarPuntoInteres();
	} else {
		var letra = entidad.substring(0, 1).toUpperCase();
		var accion = "registrar" + letra + entidad.substring(1);
		if (entidad == 'geocerca') {
			// var sombreados = document.getElementById('usuarios');
			// sombrearTodos(sombreados);
		}
		if (entidad == 'grupo') {
			// var sombreados = document.getElementById('usuarios');
			// sombrearTodos(sombreados);
		}
		var data = obtenerDatosDeForm(entidad);
		// var metodo = 'GET';
		if (entidad == 'geocerca') {
			data.push({
				name : entidad + ".idUsuario",
				value : localStorage.getItem('usuario')
			});
			$("#mensaje").html("");
		}

		if (entidad == 'grupo') {
			data.push({
				name : entidad + ".idMonitor",
				value : localStorage.getItem('usuario')
			});
			$("#mensaje").html("");

		}

		if (entidad == 'tracking') {
			data.push({
				name : entidad + ".monitor.idUsuario",
				value : localStorage.getItem('usuario')
			});
			$("#mensaje").html("");
		}

		if (entidad == 'categoria') {
			$("#mensaje").html("");
		}

		var mensaje = "";
		doAjax(
				accion,
				data,
				'POST',
				function(resp) {
					if (resp) {
						if (resp.tipoRpta == 'OK') {
							var funcionACargar = function() {
								location.reload(true);
							};
							if (entidad == 'categoria') {
								mensaje = "La Categoria ha sido registrada correctamente.";
							} else if (entidad == 'puntoInteres') {
								mensaje = "El Punto de Interes ha sido registrado correctamente.";
								funcionACargar = function() {
									location.href = "./puntosInteres-listar.jsp";
								};
							} else if (entidad == 'handset') {
								mensaje = "El handset ha sido registrado correctamente.";
							} else if (entidad == 'grupo') {
								mensaje = "El grupo ha sido registrado correctamente.";
							} else if (entidad == 'geocerca') {
								mensaje = "La geocerca ha sido registrada correctamente";
								funcionACargar = function() {
									location.href = "./geocercas-listar.jsp";
								};
							} else if (entidad == 'tracking') {
								mensaje = "El tracking ha sido registrado correctamente";
							}
							// mensaje = "";
							// $("#mensaje").addClass("succes");
							// new Messi(mensaje, {title: '', buttons: [{id: 0,
							// label: 'Continuar', val: 'X'}], callback:
							// funcionACargar});
							mensaje = "<span class=\"ui-icon ui-icon-info\" style=\"float:left; margin:0 7px 20px 0;\" />"
									+ mensaje;
							$("#mensaje").html(mensaje).removeClass();
							crearDialogoInfo("#mensaje", funcionACargar);
						} else {
							mensaje = "<img src='images/error.jpg' width='15' />"
									+ resp.mensaje;
							$("#mensaje").addClass("error").html(mensaje);
						}
						if (resp.tipoRpta == 'OK')
							limpiarForm(entidad);
					}
				}, function(e) {
					// html="<tr><td colspan='"+colspan+"' align='center'><img
					// src='images/error.jpg' width='15' />Ocurrio un error al
					// obtener la lista.</td></tr>";
					// alert("error - doAjax");
					$("#mensaje").addClass("error").html(
							"Ocurrio un error en el registro, por favor intente nuevamente.<br/>Error: "
									+ e.statusText);
					// new Messi("Ocurrio un error en el registro, por favor
					// intente nuevamente.<br/>Error: " + e.statusText,
					// {title: 'Error', buttons: [{id: 0, label: 'Aceptar', val:
					// 'X'}]});
				});

	}

}

function crearDialogoInfo(selectorMensaje, callback) {
	$(selectorMensaje).dialog({
		autoOpen : true,
		resizable : false,
		height : 160,
		modal : true,
		buttons : {
			"Continuar" : function() {
				$(this).dialog("close");
				if (typeof callback != "undefined" && callback)
					callback();
			}
		},
		// para que crezca el overlay en caso la pantalla tenga scroll
		open : function(event, ui) {
			$('.ui-widget-overlay').width($(document).width());
			$('.ui-widget-overlay').height($(document).height());
		}
	});
}

function registrarPuntoInteres() {
	var data = {};
	if (localStorage.getItem('tipo') == 2) {
		data = {
			"puntoInteres.usuario.idUsuario" : localStorage.getItem('usuario'),
			"puntoInteres.usuario.cuenta.idCuenta" : localStorage
					.getItem('cuenta'),
			"puntoInteres.visibilidad" : 2
		};
	} else if (localStorage.getItem('tipo') == 1) {
		data = {
			"puntoInteres.usuario.cuenta.idCuenta" : localStorage
					.getItem('cuenta'),
			"puntoInteres.visibilidad" : 1
		};
	} else {
		data = {
			"puntoInteres.visibilidad" : 0,
			"puntoInteres.usuario.idUsuario" : localStorage.getItem('usuario')
		};
	}
	// data.push({ name: 'dataImagen', value: $("#dataImagen").attr('value')});
	var mensaje = "";
	$("#puntoInteres-form")
			.ajaxSubmit(
					{
						url : "registrarPuntoInteres",
						data : data,
						dataType : 'json', // tipo de dato de la respuesta del
											// servidor
						cache : false,
						// iframe: true, // necesario para manejar las subidas
						// de archivos en navegadores antiguos
						success : function(resp) {
							if (resp) {
								if (resp.tipoRpta == 'OK') {
									// new Messi(mensaje, {title: '', buttons:
									// [{id: 0, label: 'Continuar', val: 'X'}],
									// callback: function(){
									// location.href="./puntosInteres-listar.jsp";
									// }});
									crearDialogoInfo(
											"#dialogoExito",
											function() {
												location.href = "./puntosInteres-listar.jsp";
											});
								} else {
									mensaje = "<img src='images/error.jpg' width='15' />"
											+ resp.mensaje;
									$("#mensaje").addClass("error").html(
											mensaje);
								}
							}
						},
						error : function(e) {
							$("#mensaje").addClass("error").html(
									"Ocurrio un error en el registro, por favor intente nuevamente.<br/>Error: "
											+ e.statusText);
							// new Messi("Ocurrio un error en el registro, por
							// favor intente nuevamente.<br/>Error: " +
							// e.statusText,
							// {title: 'Error', buttons: [{id: 0, label:
							// 'Aceptar', val: 'X'}]});
						}
					});
}

function limpiarForm(entidad) {
	if (entidad == "geocerca") {
		$("#idGeocerca").attr("value", "");
		$("#nombre").attr("value", "");
		$("#emailNotificacion").attr("value", "");
		$("#observacion").attr("value", "");
		$("#fechaInicio").attr("value", "");
		$("#fechaFin").attr("value", "");
		$("#rings").attr("value", "");
		if (!(typeof map === 'undefined')) {
			map.graphics.clear();
		}
		var selectUsuarios = document.getElementById("usuarios");
		for ( var i = 0; i < selectUsuarios.options.length; i++) {
			selectUsuarios.options[i].selected = false;
		}
	} else if (entidad == "puntoInteres") {
		$("#nombre").attr("value", "");
		$("#direccion").attr("value", "");
		$("#imagenmuestra").attr("src", "images/icon_galeria.png");
		document.getElementById('categoria').options[0].selected = true;
		$("#idEntidad").attr("value", "");
		$("#longitud").attr("value", "");
		$("#latitud").attr("value", "");
		$("#img_prev").attr("src", "#");
		$("#rutaImagen").attr("value", "");
		$("#dataImagen").attr("value", "");
	} else if (entidad == "tracking") {
		document.getElementById('monitoreado').options[0].selected = true;
		$("#fechaInicio").attr("value", "");
		$("#fechaFin").attr("value", "");
	} else if (entidad == "grupo") {
		$("#nombreNuevo").attr("value", "");
		var selectUsuarios = document.getElementById("usuariosNuevo");
		for ( var i = 0; i < selectUsuarios.options.length; i++) {
			selectUsuarios.options[i].selected = false;
		}
	} else if (entidad == "categoria") {
		$("#categoria").attr("value", "");
	}
}

function modificar(entidad) {
	if (entidad == "puntoInteres") {
		modificarPuntoInteres();
	} else {

		/*
		 * if(entidad=='usuario'){ var monitores =
		 * document.getElementById('monitoreados'); sombrearTodos(monitores); }
		 * if(entidad=='geocerca'){ var monitores =
		 * document.getElementById('usuarios'); sombrearTodos(monitores); }
		 */
		// if(entidad=='grupo'){
		// var monitores = document.getElementById('usuarios');
		// sombrearTodos(monitores);
		// }
		var letra = entidad.substring(0, 1).toUpperCase();
		var accion = "modificar" + letra + entidad.substring(1);

		var data = obtenerDatosDeForm(entidad);
		if (entidad == "geocerca")
			data[data.length] = new param(entidad + ".idUsuario", localStorage
					.getItem('usuario'));
		if (entidad == "usuario")
			data[data.length] = new param("sms", $("#sms").val());
		var mensaje;
		doAjax(
				accion,
				data,
				'POST',
				function(resp) {
					if (resp) {
						if (resp.tipoRpta == 'OK') {
							var funcionACargar = function() {
								location.reload(true);
							};
							if (entidad == "usuario"){
								
								if(resp.usuarios.length>0){
									mensaje = "Los números ";
									for(var i=0; resp.usuarios.length; i++){
										mensaje += resp.usuarios.numero+" ";
									}
									mensaje = "no serán monitoreados ya que no pertenecen a una misma cuenta. El usuario ha sido modificado.";
								}else{
									
									mensaje = "El usuario fue modificado correctamente.";
									
								}
								
							}
							else if (entidad == "puntoInteres")
								mensaje = "El punto de Interes fue modificado correctamente";
							else if (entidad == "categoria")
								mensaje = "La categoria fue modificada satisfactoriamente";
							else if (entidad == "handset")
								mensaje = "El handset ha sido modificado correctamente.";
							else if (entidad == "grupo") {
								mensaje = "El grupo ha sido modificado correctamente.";
							} else if (entidad == "geocerca") {
								mensaje = "La geocerca ha sido modificada correctamente.";
							} else
								mensaje = "Correcto";
							// $("#mensaje").addClass("succes");
							// new Messi(mensaje, {title: '', buttons: [{id: 0,
							// label: 'Continuar', val: 'X'}], callback:
							// funcionACargar});
							mensaje = "<span class=\"ui-icon ui-icon-info\" style=\"float:left; margin:0 7px 20px 0;\" />"
									+ mensaje;
							$("#mensaje").html(mensaje).removeClass();
							crearDialogoInfo("#mensaje", funcionACargar);
						} else {
							mensaje = "<img src='images/error.jpg' width='15' />"
									+ resp.mensaje;
							$("#mensaje").addClass("error").html(mensaje)
									.fadeIn(300);
						}

					}
				},
				function(e) {
					$("#mensaje")
							.html(
									"<img src='images/error.jpg' width='15' />Error en conexion")
							.fadeIn(300);
				});
	}
}

function modificarPuntoInteres() {
	var mensaje = "";
	$("#puntoInteres-form")
			.ajaxSubmit(
					{
						url : "modificarPuntoInteres",
						dataType : 'json', // tipo de dato de la respuesta del
											// servidor
						cache : false,
						// iframe: true, // necesario para manejar las subidas
						// de archivos en navegadores antiguos
						success : function(resp) {
							if (resp) {
								if (resp.tipoRpta == 'OK') {
									crearDialogoInfo("#dialogoExito",
											function() {
												location.reload(true);
											});
									// mensaje="<img src='images/check.jpg'
									// width='15' />El punto de Interes fue
									// modificado correctamente.";
									// new Messi(mensaje, {title: '', buttons:
									// [{id: 0, label: 'Continuar', val: 'X'}],
									// callback: function(){
									// location.reload(true); }});
								} else {
									mensaje = "<img src='images/error.jpg' width='15' /> Error: "
											+ resp.mensaje;
									$("#mensaje").addClass("error").html(
											mensaje);
								}
							}
						},
						error : function(e) {
							mensaje = "<img src='images/error.jpg' width='15' />Ocurrio un error al modificar el punto de interes, "
									+ "por favor intente nuevamente.<br/>Error: "
									+ e.statusText;
							$("#mensaje").addClass("error").html(mensaje);
						}
					});
}

/* Funciones para efectos */
function checkMonitor(tipo) {
	if ($("input[name=tipo]:checked").attr("value") == '2') {
		$(".monitoreado").hide();
		$(".monitor").fadeIn(300);
	}
}
function listarTipo(entidad) {
	var letra = entidad.substring(0, 1).toUpperCase();
	var accion = "listar" + letra + entidad.substring(1);
	data = new Array();
	data[data.length] = new param('usuario.cuenta.idCuenta', localStorage
			.getItem('cuenta'));
	doAjax(accion, data, 'GET', function(resp) {
		if (resp) {
			var html = "";
			if (resp.tipoRpta == 'OK') {
				if (entidad == 'usuario') {
					var data = resp.usuarios;
					for ( var i = 0; i < data.length; i++) {
						html += "<option value='" + data[i].idUsuario + "'>"
								+ data[i].etiqueta + " - " + data[i].numero
								+ "</td>";
					}
					$('.tipoFiltro').html(html);
				}
			}
		}
	}, null);
}
function erase(a) {
	$(a).parent().remove();
}

function eliminar(entidad, idEntidad) {
	$("#dialog").dialog({
						autoOpen : true,
						resizable : false,
						height : 140,
						modal : true,
						buttons : {
							"Eliminar" : function() {
								var letra = entidad.substr(0, 1).toUpperCase();
								var accion = "eliminar" + letra
										+ entidad.substr(1);
								var data = new Array();
								data[data.length] = new param(entidad + ".id"+ letra + entidad.substr(1), idEntidad);
								doAjax(
										accion,
										data,
										'POST',
										function(resp) {
											if (resp) {
												var mensaje = resp.mensaje;
												if (resp.tipoRpta == 'OK')
													$("#mensaje").addClass(
															"succes");
												$("#mensaje").text(mensaje)
														.fadeIn(300);
											}
											if (entidad != 'usuario') {
												$(".formato-tabla").dataTable().fnDestroy();
											}
											listar(entidad);
										},
										function(e) {
											alert('Ocurrio un error comunicandose con el servidor. Por favor, reintente');
										});
								$(this).dialog("close");
							},
							Cancel : function() {
								$(this).dialog("close");
							}
						},
						// para que crezca el overlay en caso la pantalla tenga
						// scroll
						open : function(event, ui) {
							$('.ui-widget-overlay').width($(document).width());
							$('.ui-widget-overlay').height($(document).height());
						}
					});
	return false;
}

// TODO Modificado por HQUINTANA 15/07
function registrarHorario() {
	re = /^\d{1,2}:\d{2}([ap]m)?$/;
	if (!$('#horaInicio').val().match(re)) {
		$("#mensaje").addClass("error");
		$("#mensaje").html("<img src='images/error.jpg' width='15' />Formato de fecha de inicio incorrecto. El formato es (hh:mm)").fadeIn(300);
		return false;
	} else if (!$('#horaFin').val().match(re)) {
		$("#mensaje").addClass("error");
		$("#mensaje").html("<img src='images/error.jpg' width='15' />Formato de fecha de fin incorrecto. El formato es (hh:mm)").fadeIn(300);
		return false;
	}
	var validacionFecha = dateCompare($('#horaInicio').val(), $('#horaFin').val());
	if (validacionFecha == -1) {
		// Significa que horaInicio es mayor a horaFin
		$("#mensaje").addClass("error");
		$("#mensaje").html("<img src='images/error.jpg' width='15' />Hora de inicio mayor a Hora de fin").fadeIn(300);
		return false;
	} else if (validacionFecha == -2) {
		// Significa que los minutos son mas de 59 y las horas mas de 24
		$("#mensaje").addClass("error");
		$("#mensaje").html("<img src='images/error.jpg' width='15' />Formato de fecha incorrecto").fadeIn(300);
		return false;
	}
	var data = new Array();
	data[data.length] = new param('idUsuario', localStorage.getItem('usuario'));
	data[data.length] = new param('cuenta.idCuenta', localStorage.getItem('cuenta'));
	data[data.length] = new param('cuenta.horario.horaInicio', $('#horaInicio').attr("value"));
	data[data.length] = new param('cuenta.horario.horaFin', $('#horaFin').attr("value"));

	doAjax('registrarHorarioCuenta',data,'GET',
			function(resp) {
				var mensaje = "";
				if (resp) {
					if (resp.tipoRpta == 'OK') {
						mensaje = "<span class='ui-icon ui-icon-alert' style='float:left; margin:0 7px 20px 0;' />Correcto";
						// $("#mensaje").addClass("succes");
						$("#mensaje").html(mensaje).removeClass();
						crearDialogoInfo("#mensaje");
					}
				} else {
					mensaje = "<img src='images/error.jpg' width='15' />"
							+ resp.mensaje;
					$("#mensaje").html(mensaje).fadeIn(300);
				}
			}, null);
}

function obtenerHorario() {
	var data = new Array();
	data[data.length] = new param('cuenta.idCuenta', localStorage
			.getItem('cuenta'));
	doAjax('obtenerHorarioCuenta', data, 'GET', function(resp) {
		if (resp) {
			if (resp.tipoRpta == 'OK') {
				$('#horaInicio').attr('value', resp.cuenta.horario.horaInicio);
				$('#horaFin').attr('value', resp.cuenta.horario.horaFin);
			} else {
				mensaje = "<img src='images/error.jpg' width='15' />"
						+ resp.mensaje;
			}
		}
	}, null);
}

function obtener(entidad, idEntidad) {
	var letra = entidad.substring(0, 1).toUpperCase();
	var accion = "obtener" + letra + entidad.substring(1);
	var data = new Array();
	data[data.length] = new param(entidad + ".id" + letra + entidad.substr(1),
			idEntidad);
	doAjax(
			accion,
			data,
			'GET',
			function(resp) {

				if (entidad == "usuario") {
					$("#list-usuario").hide();
					$("#form-usuario").fadeIn(300);
					if (resp) {
						if (resp.tipoRpta == 'OK') {
							var data = resp.usuario;
							$("#numero").attr("value", data.numero);
							$("#etiqueta").attr("value", data.etiqueta);
							$("#idEntidad").attr("value", idEntidad);
							var list = document.getElementById('handset');
							var num = $("#handset option");
							for ( var i = 0; i < num.length; i++) {
								if (list.options[i].value == data.handset.idHandset) {
									list.options[i].selected = true;
								}
							}

							// Aumentamos para que se cargue el combo de estado
							if (data.estadoHandset == 0) {
								$('input[name=estadoHandset]').attr("checked",
										"true");
							}
							// Fin aumentamos

							var listaDe = document
									.getElementById("listaMonitoreados");
							var listaA = document
									.getElementById("monitoreados");

							$("#tipo" + data.tipo).attr("checked", "checked");
							checkMonitor(data.tipo);
							var lista;
							lista = document
									.getElementById("listaMonitoreados");
							if (lista != null) {
								for ( var i = 0; i < lista.options.length; i++) {

									for ( var j = 0; j < data.monitoreados.length; j++) {

										if (lista.options[i].value == data.monitoreados[j].idUsuario) {
											lista.options[i].selected = true;
											// moverNumero(listaDe, listaA);
										}
									}
								}
								$(".tipoFiltro").multiselect('destroy');
								$(".tipoFiltro").multiselect();
							}

						} else {
							alert("Error-RESP");
						}
					}
				} else if (entidad == "puntoInteres") {
					$("#list-puntoInteres").hide();
					$("#form-puntoInteres").fadeIn(300);

					if (resp) {
						if (resp.tipoRpta == 'OK') {
							var data = resp.puntoInteres;
							$("#nombre").attr("value", data.nombre);
							$("#direccion").attr("value", data.direccion);
							if (data.rutaImagen == null)
								$("#img_prev").hide();
							else {
								$("#img_prev").attr("src", data.rutaImagen)
										.height(50);
							}

							var list = document.getElementById('categoria');
							var num = $("#categoria option");
							if (data.categoria != null) {
								for ( var i = 0; i < num.length; i++) {
									if (list.options[i].text == data.categoria.categoria) {
										list.options[i].selected = true;
										break;
									}
								}
							}

							$("#idEntidad").attr("value", idEntidad);
							$("#longitud").attr("value",
									data.coordenada.longitud);
							$("#latitud")
									.attr("value", data.coordenada.latitud);
							flag_mapa = true;
							dojo.addOnLoad(init);

						} else {
							alert("Error-RESP");
						}
					}
				} else if (entidad == "categoria") {
					$("#lista-categoria").hide();
					$("#modificar-categoria").fadeIn(300);
					$("#registrar-categoria").remove();

					if (resp) {
						if (resp.tipoRpta == 'OK') {
							var data = resp.categoria;
							$(".idcategoria").attr("value", data.idCategoria);
							$(".categoria").attr("value", data.categoria);
						} else {
							alert("Error-RESP");
						}
					}
				} else if (entidad == "handset") {
					$("#lista-handset").hide();
					$("#modificar-handset").fadeIn(300);
					$("#registrar-handset").remove();

					if (resp) {
						if (resp.tipoRpta == 'OK') {
							var data = resp.handset;
							$("#idhandset").attr("value", data.idHandset);
							$("#modelo").attr("value", data.modelo);
							$("#plataforma").attr("value", data.plataforma);
							$("#versionAplicacion").attr("value",
									data.versionAplicacion);
							$("#imagen").attr("value", data.imagen);
						} else {
							alert("Error-RESP");
						}
					}
				} else if (entidad == "geocerca") {
					$("#mensaje").html("");
					$("#lista-geocerca").hide();
					$("#modificar-geocerca").fadeIn(300);
					$("#registrar-geocerca").hide();

					if (resp) {
						if (resp.tipoRpta == 'OK') {
							var data = resp.geocerca;
							var listaDe = document
									.getElementById("listaUsuarios");
							var listaA = document.getElementById("usuarios");
							$("#idGeocerca").attr("value", data.idGeocerca);
							$("#nombre").attr("value", data.nombre);
							$("#nombreOriginal").attr("value", data.nombre);
							$("#emailNotificacion").attr("value",
									data.emailNotificacion);
							$("#observacion").text(data.observacion);
							var lista = document
									.getElementById('listaUsuarios');
							for ( var i = 0; i < lista.options.length; i++) {
								for ( var j = 0; j < data.usuarios.length; j++) {
									if (lista.options[i].value == data.usuarios[j].idUsuario) {
										lista.options[i].selected = true;
										// moverNumero(listaDe, listaA);
									}
								}
							}
							$("#fechaInicio").attr("value",
									data.horario.fechaInicio);
							$("#fechaFin").attr("value", data.horario.fechaFin);
							$("#horaInicio").attr("value",
									data.horario.horaInicio);
							$("#horaFin").attr("value", data.horario.horaFin);
							var coordenadas = "";
							var rings = data.rings.split(",");
							for ( var i = 0; i < rings.length; i++) {
								coordenadas += "[" + rings[i] + ","
										+ rings[i + 1] + "],";
								i++;
								if ((i + 1) == rings.length) {
									break;
								}
							}
							coordenadas = coordenadas.substring(0,
									coordenadas.length - 1);
							coordenadas = "[[" + coordenadas + "]]";
							coordenadas = '{"rings":' + coordenadas
									+ ',"spatialReference":{" wkid":4326 }}';

							$("#rings").attr("value", coordenadas);
							if (resp.puedeModificar) {
								$("#modificar").show();
								$("#clonar").hide();
								$("#fechaInicio").datepicker({
									dateFormat : 'dd/mm/yy',
									minDate : new Date()
								});
								$("#fechaFin").datepicker({
									dateFormat : 'dd/mm/yy',
									minDate : new Date()
								});
							} else {
								$("#modificar").hide();
								// los campos inician inhabilitados
								$("#geocerca-form :input").attr("readonly",
										true);
								// asi simulamos 'readonly' en la lista de
								// usuarios
								$("#usuario").on("mousedown", false);
							}
							dojo.addOnLoad(init);
							$(".tipoFiltro").multiselect('destroy');
							$(".tipoFiltro").multiselect();
						} else {
							alert("Error-Obtener");
						}
					}
				} else if (entidad == "grupo") {
					$("#lista-grupo").hide();
					$("#modificar-grupo").fadeIn(300);
					$("#registrar-grupo").remove();

					if (resp) {
						if (resp.tipoRpta == 'OK') {
							var data = resp.grupo;
							$("#idGrupo").attr("value", data.idGrupo);
							$("#nombre").attr("value", data.nombre);
							var listaDe = document
									.getElementById("listaUsuarios");
							var listaA = document.getElementById("usuarios");
							lista = document.getElementById("listaUsuarios");

							for ( var i = 0; i < lista.options.length; i++) {
								for ( var j = 0; j < data.usuarios.length; j++) {
									if (lista.options[i].value == data.usuarios[j].idUsuario) {
										lista.options[i].selected = true;
										// moverNumero(listaDe, listaA);
									}
								}
							}
							$(".tipoFiltro").multiselect('destroy');
							$(".tipoFiltro").multiselect();
						} else {
							alert("Error-Obtener");
						}
					}
				}
			},
			function(e) {
				html = "<tr><td colspan='"
						+ colspan
						+ "' align='center'>Ocurrio un error al obtener la lista.</td></tr>";
				alert("Error");
			});
}

function detalles(entidad, idEntidad) {
	var fila = $("#" + idEntidad).find(".detalle");
	fila.find("span").addClass("clickTip exampleTip");
	var letra = entidad.substring(0, 1).toUpperCase();
	var accion = "obtener" + letra + entidad.substring(1);
	var data = new Array();
	//var dattim = new Array();
	data[data.length] = new param(entidad + ".id" + letra + entidad.substr(1),
			idEntidad);
	var texto = "";
	doAjax(
			accion,
			data,
			'GET',
			function(resp) {
				if (resp) {
					if (entidad == "puntoInteres") {
						if (resp.tipoRpta == 'OK') {
							var data = resp.puntoInteres;
							texto = "Direccion:<hr>" + data.direccion;
						} else {
							alert("Error-RESP");
						}
					} else if (entidad == "geocerca") {
						if (resp.tipoRpta == 'OK') {
							var data = resp.geocerca;
							//var dattim = resp.horario;
							//texto = "Observacion:<hr>" + data.observacion
							texto = "Informacion Adicional:<hr> <strong>Nombre: </strong><br>" + data.nombre
									+ "<br>" + "<strong>Observacion: </strong> "
									+ data.observacion
									+ "<br>" + "<strong>Email Notificaciones: </strong> "
									+ data.emailNotificacion;
								/*	+ "<br>" + "Fecha Inicio:<hr>"
									+ dattim.setFechaInicio
									+ "<br>" + "Fecha Fin:<hr>"
									+ dattim.setFechaFin;*/
						} else {
							alert("Error-RESP");
						}
					} else if (entidad == "usuario") {
						if (resp.tipoRpta == 'OK') {
							var data = resp.usuario;

							texto = "Usuarios Monitoreados:<hr>";
							texto += data.monitoreados.length;
							try {
								// lista =
								// document.getElementById("monitoreados");

								/*
								 * for (var i=0; i<lista.options.length; i++) {
								 * for(var j=0;j<data.monitoreados.length;j++){
								 * if (lista.options[i].value ==
								 * data.monitoreados[j].idUsuario) { texto+=
								 * lista.options[i].text; } } }
								 */
							} catch (e) {

							}
						} else {
							alert("Error-RESP");
						}
					} else if (entidad == "grupo") {
						if (resp.tipoRpta == 'OK') {
							var data = resp.grupo;
							texto = "Usuarios Asignados:<hr>";
							// lista = document.getElementById("usuarios");

							// for (var i=0; i<lista.options.length; i++) {
							for ( var j = 0; j < data.usuarios.length; j++) {
								// if(lista.options[i].value ==
								// data.usuarios[j].idUsuario) {
								// texto+= lista.options[i].text;
								// if((i+1)!=lista.options.length)
								// texto+="<br>";
								// }
								texto += data.usuarios[j].etiqueta + "<br>";
							}
							// }
						} else {
							alert("Error-RESP");
						}
					}
					$('span.clickTip').aToolTip({
						clickIt : true,
						tipContent : texto
					});
				} else {
					alert("Error-RESP");
				}

				fila.find("span").trigger("click");
				fila.find("span").removeClass("clickTip exampleTip");
			},
			function(e) {
				alert('Ocurrio un error comunicandose con el servidor. Por favor, reintente');
			});
}

function ver(entidad) {
	$("#modificar-" + entidad).remove();
	$("#mensaje").text("");
	$("#lista-" + entidad).hide();
	$("#registrar-" + entidad).fadeIn(300);
	if (entidad == "geocerca") {
		dojo.addOnLoad(init);
	}
}
function combobox(entidad) {
	var letra = entidad.substring(0, 1).toUpperCase();
	var accion = "listar" + letra + entidad.substring(1);
	var data = new Array();

	if (entidad == 'usuario') {
		if (typeof flag != 'undefined' && flag) {
			data[data.length] = new param("filtroUsuario", true);
		}
	}
	if (entidad == 'grupo') {
		if (localStorage.getItem('tipo') == 1) {
			data[data.length] = new param("filtroUsuario", true);
		}
	}

	data[data.length] = new param("usuario.idUsuario", localStorage
			.getItem('usuario'));
	data[data.length] = new param("usuario.cuenta.idCuenta", localStorage
			.getItem('cuenta'));

	doAjax(accion, data, 'GET', function(resp) {
		if (resp) {
			var html = "";
			if (resp.tipoRpta == 'OK') {
				if (entidad == 'categoria') {
					var data = resp.categorias;
					for ( var i = 0; i < data.length; i++) {
						html += "<option value='" + data[i].idCategoria + "'>"
								+ data[i].categoria + "</td>";
					}
					$('.tipoFiltro').append(html);
				}
				if (entidad == 'usuario') {
					var data = resp.usuarios;
					for ( var i = 0; i < data.length; i++) {
						html += "<option value='" + data[i].idUsuario + "'>"
								+ data[i].etiqueta + " - " + data[i].numero
								+ "</td>";
					}
					$('.tipoFiltro').append(html);

				}
				if (entidad == 'handset') {
					var data = resp.handsets;
					for ( var i = 0; i < data.length; i++) {
						html += "<option value='" + data[i].idHandset + "'>"
								+ data[i].modelo + "</td>";
					}
					$('.tipoFiltroHandset').append(html);
				}
				if (entidad == 'grupo') {
					var data = resp.grupos;
					for ( var i = 0; i < data.length; i++) {
						html += "<option value='" + data[i].idGrupo + "'>"
								+ data[i].nombre + "</td>";
					}
					$('.tipoFiltro').append(html);
				}
				if (entidad == 'geocerca') {
					var data = resp.geocercas;
					for ( var i = 0; i < data.length; i++) {
						if (localStorage.getItem('tipo') == 2)
							html += "<option value='" + data[i].idGeocerca
									+ "'>" + data[i].nombre + "</td>";
						else
							html += "<option value='" + data[i].idGeocerca
									+ "'>" + data[i].nombre + " ("
									+ data[i].usuario.etiqueta + " - "
									+ data[i].numero + ")</td>";
					}
					$('.tipoFiltro').append(html);
				}

			}
		}
	}, null);
}

// Re implementacion de Combobox para permitir las llamadas asincronas. Se le
// aumento una funciï¿½n que se deberï¿½ llamar al final
function combobox(entidad, f) {
	var letra = entidad.substring(0, 1).toUpperCase();
	var accion = "listar" + letra + entidad.substring(1);
	var data = new Array();

	if (entidad == "usuario") {
		if (typeof flag != 'undefined' && flag) {
			data[data.length] = new param("filtroUsuario", true);
		}
		data[data.length] = new param("usuario.idUsuario", localStorage
				.getItem('usuario'));
		data[data.length] = new param("usuario.cuenta.idCuenta", localStorage
				.getItem('cuenta'));
	} else if (entidad == 'grupo') {
		data[data.length] = new param("usuario.idUsuario", localStorage
				.getItem('usuario'));
		data[data.length] = new param("usuario.cuenta.idCuenta", localStorage
				.getItem('cuenta'));
	}

	doAjax(accion, data, 'GET', function(resp) {
		if (resp) {
			var html = "";
			if (resp.tipoRpta == 'OK') {
				if (entidad == 'categoria') {
					var data = resp.categorias;
					for ( var i = 0; i < data.length; i++) {
						html += "<option value='" + data[i].idCategoria + "'>"
								+ data[i].categoria + "</td>";
					}
					$('.tipoFiltro').append(html);
				}
				if (entidad == 'usuario') {
					var data = resp.usuarios;
					for ( var i = 0; i < data.length; i++) {
						html += "<option value='" + data[i].idUsuario + "'>"
								+ data[i].etiqueta + " - " + data[i].numero
								+ "</td>";
					}
					$('.tipoFiltro').append(html);
					if (typeof selectFilter != 'undefined') {
						// optionscopy=$('#listaUsuariosNuevos option');
						$(".tipoFiltro").multiselect();
						// $("#listaUsuariosNuevos").multiselect({sortable:
						// false, searchable: false});
					}
				}
				if (entidad == 'handset') {
					var data = resp.handsets;
					for ( var i = 0; i < data.length; i++) {
						html += "<option value='" + data[i].idHandset + "'>"
								+ data[i].modelo + "</td>";
					}
					$('.tipoFiltroHandset').append(html);
				}
				if (entidad == 'grupo') {
					var data = resp.grupos;
					for ( var i = 0; i < data.length; i++) {
						html += "<option value='" + data[i].idGrupo + "'>"
								+ data[i].nombre + "</td>";
					}
					$('.tipoFiltro').append(html);
				}
				if (entidad == 'geocerca') {
					var data = resp.geocercas;
					for ( var i = 0; i < data.length; i++) {
						if (localStorage.getItem('tipo') == 2)
							html += "<option value='" + data[i].idGeocerca
									+ "'>" + data[i].nombre + "</td>";
						else
							html += "<option value='" + data[i].idGeocerca
									+ "'>" + data[i].nombre + " ("
									+ data[i].usuario.etiqueta + " - "
									+ data[i].numero + ")</td>";
					}
					$('.tipoFiltro').append(html);
				}
			}
		}
		if (typeof f != 'undefined' && f) {
			f();
		}
	}, null);
}

// ****** MÃ©todos Utilitarios *********

// construye un parÃ¡metro json
var param = function(name, value) {
	this.name = name;
	this.value = value;
};

// MÃ©todo para enviar requests via Ajax
var doAjax = function(url, filterData, method, successFn, errorFn) {
	$.ajax({
		url : url,
		type : method,
		cache : false,
		data : filterData,
		success : function(resp) {
			if (successFn)
				successFn(resp);
		},
		error : function(e) {
			if (errorFn)
				errorFn(e);
		}
		
	});
};

var obtenerDatosDeForm = function(entity) {
	var datos = new Array();
	var formEleList = $('form#' + entity + '-form').serializeArray();
	var name;
	for ( var i = 0; i < formEleList.length; i++) {
		if (!(formEleList[i].name == 'estadoHandset')) {
			datos[datos.length] = new param(entity + "." + formEleList[i].name,
					formEleList[i].value);
		}
	}
	// Cambio para corregir carga de check de bloqueo (SOLO PARA ESE CASO)
	if (entity == 'usuario') {
		if ($('input[name=estadoHandset]') != null) {
			if ($('input[name=estadoHandset]').is(':checked')) {
				datos[datos.length] = new param(entity + "." + "estadoHandset",
						0);
			} else {
				datos[datos.length] = new param(entity + "." + "estadoHandset",
						1);
			}
		}
	}
	return datos;
};
function verMapa() {
	var list = $(".elegir:checked").get();
	if (list.length == 0) {
		// $(".elegir").focus();
		$("#mensajeLista")
				.addClass("error")
				.html(
						"<img src='images/error.jpg' width='15' />Elija uno o mas puntos de interes")
				.fadeIn();
		// new Messi("Elija uno o mas puntos de interes", {title: '', buttons:
		// [{id: 0, label: 'Continuar', val: 'X'}]});
	} else {
		flag_mapa = false;
		$("#list-puntoInteres").hide();
		$("#form-puntoInteres form").remove();
		$("#form-puntoInteres").fadeIn(300);
		$("#modificar").hide();
		dojo.addOnLoad(init);
	}
}

function puntoInteresMapa() {
	dojo.addOnLoad(init);
}

function buscarMapa(a) {
	$("tr#" + a).addClass("mapa");
	$("#lista-puntoInteres").hide();
	$("#mapa-puntoInteres").fadeIn(300);
	dojo.addOnLoad(init);
}

function regresarBuscarPI() {
	$("#mapa-puntoInteres").hide();
	$("#lista-puntoInteres").fadeIn(300);
	map.destroy();
}

function filtrar(entidad) {
	var colspan = 3;
	var cont = 0;
	var lista = new Array();
	$("#tabla tbody #error").remove();
	if (entidad == "puntointeres") {
		$("#mensajeLista").hide();
		colspan = 3;
		var texto = $("#filtro").val().toUpperCase();
		// var distrito = $("#distritoFiltro").val();
		// if (typeof distrito == 'undefined') {
		// distrito = "";
		// }
		// else {
		// distrito = distrito.toUpperCase();
		// }
		// var categoria = $("#categoriaFiltro
		// option:selected").text().toUpperCase();
		lista = $("#tabla tr").get();
		var nombre;
		for ( var i = 1; i < lista.length; i++) {
			nombre = "" + $(lista[i]).find('.nombre').text().toUpperCase();
			// nombreCategoria=""+$(lista[i]).find('.categoria').text().toUpperCase();
			// nombreDistrito=""+$(lista[i]).find('.distrito').text().toUpperCase();

			if (((nombre.search(texto) != -1 && texto != "") || (texto == ""))) {
				// &&
				// ((nombreDistrito.search(distrito) !=-1 && distrito !="") ||
				// (distrito=="")) &&
				// ((nombreCategoria.search(categoria) !=-1 && categoria !="")
				// || (categoria=='TODOS'))){
				$(lista[i]).show();
			} else {
				$(lista[i]).hide();
				cont++;
			}
		}
	} else if (entidad == "geocerca") {
		colspan = 8;
		var texto = $("#filtro").val().toUpperCase();
		var estadoFiltro = $("#estadoFiltro").val().toUpperCase();
		var fechaEjecucionFiltro = $("#fechaEjecucionFiltro").val()
				.toUpperCase();
		if (fechaEjecucionFiltro != "") {
			fechaEjecucionFiltro = fechaEjecucionFiltro.substring(0, 10);
		}
		lista = $("#tabla tr").get();
		var nombre;
		for ( var i = 1; i < lista.length; i++) {
			nombre = "" + $(lista[i]).find('.geocerca').text().toUpperCase();
			estado = "" + $(lista[i]).find('.estado').text().toUpperCase();
			fechaEjecucion = ""
					+ $(lista[i]).find('.fechaEjecucion').text().toUpperCase();
			fechaEjecucion = fechaEjecucion.substring(0, 10);

			if (((nombre.search(texto) != -1 && texto != "") || (texto == ""))
					&& ((estado.search(estadoFiltro) != -1 && estadoFiltro != "") || (estadoFiltro == "TODOS"))
					&& ((fechaEjecucion == fechaEjecucionFiltro) || (fechaEjecucionFiltro == ""))) {
				$(lista[i]).show();

			} else {
				$(lista[i]).hide();
				cont++;
			}
		}
	} else if (entidad == "grupo") {
		colspan = 3;
		var texto = $("#filtro").val().toUpperCase();
		lista = $("#tabla tr").get();
		var nombre;
		for ( var i = 1; i < lista.length; i++) {
			nombre = "" + $(lista[i]).find('.nombre').text().toUpperCase();

			if ((nombre.search(texto) == -1 && texto != "")) {
				$(lista[i]).hide();
				cont++;

			} else {
				$(lista[i]).show();
			}
		}

	} else if (entidad == "usuario") {
		colspan = 4;
		var texto = $("#filtro").val().toUpperCase();
		var categoria = $("#tipo_usuarios option:selected").text()
				.toUpperCase();
		lista = $("#tabla tr").get();

		for ( var i = 1; i < lista.length; i++) {

			$(lista[i]).show();
			if (categoria == 'TODOS') {
				var etiqueta = ""
						+ $(lista[i]).find('.etiqueta').text().toUpperCase();
				var numero = ""
						+ $(lista[i]).find('.numero').text().toUpperCase();

				if (numero.search(texto) == -1 && etiqueta.search(texto) == -1) {
					$(lista[i]).hide();
					cont++;
				}
			} else if (categoria == 'ETIQUETA') {
				var etiqueta = ""
						+ $(lista[i]).find('.etiqueta').text().toUpperCase();
				if (etiqueta.search(texto) == -1) {
					$(lista[i]).hide();
					cont++;
				}
			} else {
				// Por Numero
				var numero = ""
						+ $(lista[i]).find('.numero').text().toUpperCase();
				if (numero.search(texto) == -1) {
					$(lista[i]).hide();
					cont++;
				}
			}

		}
	} else if (entidad == "tracking") {
		colspan = 8;
		var estadoSeleccionado = $("#estado_tracking option:selected").text()
				.toUpperCase();
		lista = $("#tabla tr").get();
		var estado;
		for ( var i = 1; i < lista.length; i++) {
			estado = "" + $(lista[i]).find('.estado').text().toUpperCase();

			if (estadoSeleccionado != 'TODOS' && estado != estadoSeleccionado) {
				$(lista[i]).hide();
				cont++;
			} else {
				$(lista[i]).show();
			}
		}
	}
	if (cont + 1 == lista.length) {
		$("#tabla tbody")
				.append(
						"<tr id='error'><td colspan='"
								+ colspan
								+ "'>No se encontraron resultados para la busqueda.</td></tr>");
	}
}

function filtrarTracking() {
	var filtro = $('#estado_tracking').attr('value');
	var fechaInicio = $('#fechaInicio').attr('value');
	var fechaFin = $('#fechaFin').attr('value');
	var data = new Array();
	var colspan = 8;
	var html = "";
	// showDataTable=true;
	data[data.length] = new param('modo', filtro);
	data[data.length] = new param('fechaInicial', fechaInicio);
	data[data.length] = new param('fechaFin', fechaFin);
	data[data.length] = new param('idUsuario', localStorage.getItem('usuario'));

	doAjax('filtrarTracking',data,'GET',function(resp) {
				if (resp) {
					if (showDataTable)
						$(".formato-tabla").dataTable().fnDestroy();
					if (resp.tipoRpta == 'OK') {
						var data = resp.trackings;
						colspan = 8;
						for ( var i = 0; i < data.length; i++) {
							var strEstado = "";
							if (data[i].estado == 0)
								strEstado = 'Cancelado';
							else if (data[i].estado == 2)
								strEstado = 'Iniciado';
							else if (data[i].estado == 3)
								strEstado = 'Terminado';
							html += "<tr>";
							html += "<td class='nombre'>"
									+ data[i].monitoreado.etiqueta + "</td>";
							html += "<td>" + data[i].monitoreado.numero
									+ "</td>";
							html += "<td>" + data[i].horario.fechaInicio
									+ "</td>";
							html += "<td>" + data[i].horario.horaInicio
									+ "</td>";
							html += "<td>" + data[i].horario.fechaFin + "</td>";
							html += "<td>" + data[i].horario.horaFin + "</td>";
							html += "<td class='estado'>" + strEstado + "</td>";
							html += "<td><a href='#' onClick='verTracking(\""
									+ data[i].idTracking
									+ "\")'><img title='Ver Mapa' src='images/map.png' /></a></td>";
							html += "</tr>";
						}
						showDataTable = true;
						if (data.length == 0) {
							showDataTable = false;
							html = "<tr><td colspan='"
									+ colspan
									+ "' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >No se encontro ningun registro para esta cuenta.</td></tr>";
						}
					} else {
						showDataTable = false;
						html = "<tr><td colspan='" + colspan
								+ "' align='center'>" + resp.mensaje
								+ "</td></tr>";
					}
					$('#data-tracking').html(html);

					// $(".formato-tabla").dataTable().fnDestroy();
					if (showDataTable) {
						$(".formato-tabla").dataTable(
								{
									"oLanguage" : {
										"sUrl" : "../javascript/language.txt"
									},
									"bSort" : false,
									"bRetrieve" : false,
									"bDestroy" : true,
									"aLengthMenu" : [ [ 10, 25, 50, 100, -1 ],
											[ 10, 25, 50, 100, "Todos" ] ]
								});
					}
				}
			}, null);

}

function filtrarLocalizacionTracking() {
	colspan = 2;
	var tipoSeleccionado = $("#tipo_tracking option:selected").text().toUpperCase();
	lista = $("#tabla-detalle-tracking tr").get();
	var tipo;
	var cont = 0;
	for ( var i = 1; i < lista.length; i++) {
		tipo = "" + $(lista[i]).find('.metodo').text().toUpperCase();

		if (tipoSeleccionado != 'TODOS' && tipo != tipoSeleccionado) {
			$(lista[i]).hide();
			cont++;
		} else {
			$(lista[i]).show();
		}
	}

	if (cont + 1 == lista.length) {
		$("#tabla tbody")
				.append(
						"<tr id='error'><td colspan='"
								+ colspan
								+ "'>No se encontraron resultados para la busqueda.</td></tr>");
	}
}

/*Inicio 12/07/2013 MDTECK Sandy Huanco*/
function pasarParametrosExcelGeocerca(tipo) {
	if(tipo=='todo'){
		var idUsuario, lista;
		if (localStorage.getItem('tipo') == 2) {
			idUsuario =	localStorage.getItem('usuario');
			lista = true;
		} else {
			idUsuario = localStorage.getItem('cuenta');
			lista = false;
		}
		
		$('#txtIdUsuario').attr('value', idUsuario);
		$('#txtLista').attr('value', lista);
		$('#txtTipo').attr('value', "todo");
		$('#txtTipo2').attr('value', localStorage.getItem('tipo'));
		document.forms["excelGeocerca"].submit();
	}else{
		if(tipo=='detalle'){			
			$('#txtIdDetalle').attr('value', $('#idDetalleSelec').val());
			$('#txtTipo').attr('value', "detalle");
			document.forms["excelGeocerca"].submit();
		}
	}
	
}
/*Fin*/

function reporte(tipo) {
	var accion = tipo + "Reporte";
	var data = new Array();
	var colspan = 6;
	var html = "";
	var dataTable = true;
	var t;

	if (tipo == 'geocerca') {
		colspan = 7;
		if (localStorage.getItem('tipo') == 2) {
			data[data.length] = new param("geocerca.usuario.idUsuario",	localStorage.getItem('usuario'));
			data[data.length] = new param("listarPorUsuario", true);
		} else {
			data[data.length] = new param("geocerca.usuario.cuenta.idCuenta",localStorage.getItem('cuenta'));
			data[data.length] = new param("listarPorUsuario", false);
		}
	}
	if (tipo == 'localizacion') {
		/*Inicio 15/07/2013 MDTECK Sandy Huanco: Validación de reporte*/
		var fechaI = $('#fechaInicio').val();
		var fechaF = $('#fechaFin').val();
		
		if(!(fechaI=="" && fechaF=="")){
			if (!initTable) {
				$(".formato-tabla").dataTable().fnDestroy();
				initTable = true;
			}
			colspan = 10;
			data[data.length] = new param('fechaInicio', $('#fechaInicio').val());
			data[data.length] = new param('fechaFin', $('#fechaFin').val());
			data[data.length] = new param('tipo', $('#tipo').val());
			data[data.length] = new param('valor', $('#valor').val());
			data[data.length] = new param("idCuenta", localStorage.getItem('cuenta'));
		}else{
			alert("Ingrese el rango de fechas, por favor.");
			return false;
		}
		
	}
	// Aumento para el reporte de equipos bloqueados
	if (tipo == 'bloqueados') {
		if (!initTable) {
			$(".formato-tabla").dataTable().fnDestroy();
			initTable = true;
		}
		colspan = 9;
		data[data.length] = new param("geocerca.usuario.cuenta.idCuenta",localStorage.getItem('cuenta'));
		data[data.length] = new param('numero', $('#numero').val());
		data[data.length] = new param('etiqueta', $('#etiqueta').val());
	}

	if (tipo == 'transacciones') {
		dataTable = false;
		colspan = 7;
		t = $('#tipoFiltro').val();
		data[data.length] = new param('tipo', t);
		if (t == 'hora') {
			data[data.length] = new param('fecha', $('#fechaInicio').val());
			data[data.length] = new param('hora', $('#val').val());
		} else if (t == 'mes') {
			data[data.length] = new param('mes', $('#mes').val());
		} else if (t == 'anio') {
			data[data.length] = new param('anio', $('#val').val());
		} else if (t == 'fecha') {
			data[data.length] = new param('fechaInicio', $('#fechaInicio').val());
			data[data.length] = new param('fechaFin', $('#fechaFin').val());
		} else if (t == 'dia') {
			data[data.length] = new param('dia', $('#fechaInicio').val());
		}
		data[data.length] = new param('origen', $('#tipoTransaccion').val());
		data[data.length] = new param('tecnologia', $('#tecnologia').val());
	}

	$('#data-reporte').html("<tr><td colspan='"	+ colspan+ "' align='center'><img src='images/loading.gif' /></td></tr>");
	doAjax(	accion,	data,'GET',	function(resp) {
				if (resp) {
					if (resp.tipoRpta == 'OK') {
						if (tipo == 'localizacion') {
							var datos = resp.posiciones;
							for ( var i = 0; i < datos.length; i++) {
								html += "<tr id='" + datos[i].idPosicion + "'>";
								// html+="<td
								// class='nombre'>"+datos[i].idPosicion+"</td>";
								html += "<td class='nombre'>"+ datos[i].monitor.numero + "</td>";
								html += "<td class='time'>"	+ (datos[i].time).substr(0, 16)	+ "</td>";
								html += "<td class='numero'>"+ datos[i].usuario.numero + "</td>";
								html += "<td class='latitud'>"+ datos[i].coordenada.latitud + "</td>";
								html += "<td class='longitud'>"	+ datos[i].coordenada.longitud+ "</td>";
								strDireccion = datos[i].direccion.direccion == null ? "-": datos[i].direccion.direccion;
								html += "<td class='direccion'>" + strDireccion	+ "</td>";
								strDistrito = datos[i].direccion.distrito == null ? "-"	: datos[i].direccion.distrito;
								html += "<td class='distrito'>" + strDistrito+ "</td>";
								html += "<td>" + datos[i].metodo + "</td>";
								var tecnologia = datos[i].tecnologia;
								if (tecnologia == '2G')
									tecnologia = "IDEN";
								else
									tecnologia = "Nextel+";

								html += "<td>" + tecnologia + "</td>";
								// Inicio 11/06/2013 MDTECK Sandy Huanco
								html += "<td><input type='hidden' id='usuario"+datos[i].idPosicion+"' value='"	+ datos[i].usuario.numero+ "' />" +
										    "<input type='hidden' id='metodo"+datos[i].idPosicion+"' value='"	+ datos[i].metodo+ "' />" +
										    "<input type='hidden' id='direccion"+datos[i].idPosicion+"' value='"	+ datos[i].direccion.direccion+ " - "+strDistrito+"' />" +
										    "<input type='hidden' id='longitud"+datos[i].idPosicion+"' value='"	+ datos[i].coordenada2.longitud+ "' />" +
										    "<input type='hidden' id='latitud"+datos[i].idPosicion+"' value='"+ datos[i].coordenada2.latitud+ "' />"+
								            "<a href='#' onClick='verMapaTracking(\""+ datos[i].idPosicion+"\","+"\""+datos[i].monitor.numero+"\","+"\""+(datos[i].time).substr(0, 16)+"\","+"\""+datos[i].usuario.numero+"\","+"\""+	datos[i].coordenada2.latitud+"\","+"\""+datos[i].coordenada2.longitud+"\","+"\""+strDireccion+"\","+"\""+strDistrito+"\","+"\""+datos[i].metodo+"\","+"\""+tecnologia+"\")'><img title='Ver Mapa' src='images/map.png' /></a></td>";								
								// Fin cambio
								html += "</tr>";
							}
							if (datos.length == 0) {
								dataTable = false;
								html = "<tr><td colspan='"+ colspan	+ "' align='center'>No se encontraron registros para los parametros ingresados</td></tr>";
							}
						} else if (tipo == 'geocerca') {
							var datos = resp.geocercas;
							for ( var i = 0; i < datos.length; i++) {
								html += "<tr id='" + datos[i].idGeocerca + "'>";
								if (datos[i].nombre == null)
									html += "<td class='geocerca'> - </td>";
								else
									html += "<td class='geocerca'>"	+ datos[i].nombre + "</td>";
								if (datos[i].fechaRegistro == null)
									html += "<td class='fechaRegistro'> - </td>";
								else
									html += "<td class='fechaRegistro'>"+ datos[i].fechaRegistro + "</td>";

								if ((datos[i].usuario.etiqueta == null)	&& (datos[i].usuario.numero == null))
									html += "<td class='nombre'> - </td>";
								else if (datos[i].usuario.etiqueta == null)
									html += "<td class='nombre'>"+ datos[i].usuario.numero + "</td>";
								else if (datos[i].usuario.numero == null)
									html += "<td class='nombre'>"+ datos[i].usuario.etiqueta+ "</td>";
								else
									html += "<td class='nombre'>"+ datos[i].usuario.etiqueta + " - "+ datos[i].usuario.numero + "</td>";

								var e = datos[i].estado;
								var estado = "";
								if (e == '0')
									estado = 'Cancelado';
								else if (e == '1')
									estado = 'StandBy';
								else if (e == '2')
									estado = 'Iniciado';
								else if (e == '3')
									estado = 'Terminado';
								else
									estado = " - ";
								html += "<td class='estado'>" + estado	+ "</td>";

								if ((datos[i].horario.fechaInicio == null)&& (datos[i].horario.horaInicio == null))
									html += "<td class='fechaEjecucion'> - </td>";
								else if (datos[i].horario.fechaInicio == null)
									html += "<td class='fechaEjecucion'>"+ datos[i].horario.horaInicio	+ "</td>";
								else if (datos[i].horario.horaInicio == null)
									html += "<td class='fechaEjecucion'>"+ datos[i].horario.fechaInicio	+ "</td>";
								else
									html += "<td class='fechaEjecucion'>"+ datos[i].horario.fechaInicio	+ " " + datos[i].horario.horaInicio	+ "</td>";

								if ((datos[i].horario.fechaFin == null)	&& (datos[i].horario.horaFin == null))
									html += "<td class='fechaCulminacion'> - </td>";
								else if (datos[i].horario.fechaFin == null)
									html += "<td class='fechaCulminacion'>"	+ datos[i].horario.horaFin	+ "</td>";
								else if (datos[i].horario.horaFin == null)
									html += "<td class='fechaCulminacion'>"	+ datos[i].horario.fechaFin	+ "</td>";
								else
									html += "<td class='fechaCulminacion'>"	+ datos[i].horario.fechaFin + " "	+ datos[i].horario.horaFin+ "</td>";

								html += "<td>"	+ "<a href='#' onClick='detalleGeocerca(\""	+ datos[i].idGeocerca+ "\")'> <img title='Ver Localizaciones' src='images/detail.png'></a><a href='#' onClick='mapaGeocerca(\""	+ datos[i].idGeocerca+ "\")'> <img title='Ver Mapa' src='images/map.png'></a></td>";
								html += "</tr>";
							}
							if (datos.length == 0) {
								dataTable = false;
								html = "<tr><td colspan='"+ colspan	+ "' align='center'>No se encontraron registros para los geocercas registrada</td></tr>";
							}
						} else if (tipo == 'bloqueados') {
							var datos = resp.bloqueados;
							for ( var i = 0; i < datos.length; i++) {
								html += "<tr id='" + datos[i].idBloqueo + "'>";
								html += "<td class='timestamp'>"+ datos[i].timestamp + "</td>";
								html += "<td class='numero'>" + datos[i].numero	+ "</td>";
								html += "<td class='nombre'>"	+ datos[i].etiqueta + "</td>";
								var e = datos[i].estado;
								var estado = "";
								if (e == '0')
									estado = 'Bloqueado';
								else if (e == '1')
									estado = 'Activo';
								html += "<td class='estado'>" + estado	+ "</td>";
								// html+="<td>"+"<a href='#'
								// onClick='detalleGeocerca(\""+
								// datos[i].idGeocerca +"\")'> <img
								// src='images/detail.png'></a><a href='#'
								// onClick='mapaGeocerca(\""+
								// datos[i].idGeocerca +"\")'> <img
								// src='images/map.png'></a></td>";
								html += "</tr>";
							}
							if (datos.length == 0) {
								dataTable = false;
								html = "<tr><td colspan='"+ colspan	+ "' align='center'>No se encontraron registros para usuarios bloqueado</td></tr>";
							}
						} else if (tipo == 'transacciones') {
							dataTable = false;
							var datos = resp.reportes;
							for ( var i = 0; i < datos.length; i++) {
								html += "<tr>";
								var mes;
								if (t == 'anio') {
									var id = datos[i].identificador;
									if (id == 01)
										mes = "Enero";
									else if (id == 02)
										mes = "Febrero";
									else if (id == 03)
										mes = "Marzo";
									else if (id == 04)
										mes = "Abril";
									else if (id == 05)
										mes = "Mayo";
									else if (id == 06)
										mes = "Junio";
									else if (id == 07)
										mes = "Julio";
									else if (id == 08)
										mes = "Agosto";
									else if (id == 09)
										mes = "Septiembre";
									else if (id == 10)
										mes = "Octubre";
									else if (id == 11)
										mes = "Noviembre";
									else if (id == 12)
										mes = "Diciembre";
									html += "<td>" + mes + "</td>";
								} else {
									html += "<td>" + datos[i].identificador
											+ "</td>";
								}
								html += "<td>" + datos[i].agps + "</td>";
								html += "<td>" + datos[i].triangulacion+ "</td>";
								html += "<td>" + datos[i].celda + "</td>";
								html += "<td>" + datos[i].ok + "</td>";
								html += "<td>" + datos[i].error + "</td>";
								var total = datos[i].ok + datos[i].error;
								html += "<td>" + total + "</td>";
								html += "</tr>";
							}
							if (datos.length == 0) {
								dataTable = false;
								html = "<tr><td colspan='"
										+ colspan
										+ "' align='center' style='color: red;'>No se encontro informacion para el reporte</td></tr>";
							}

						}
						$('#data-reporte').html(html);
						if (dataTable) {
							$(".formato-tabla").dataTable({"oLanguage" : {"sUrl" : "../javascript/language.txt"},"bSort" : false,
												"aLengthMenu" : [[ 10, 25, 50, 100, -1 ],[ 10, 25, 50, 100,	"Todos" ] ]
											});
							if (tipo == 'localizacion')
								initTable = false;
							if (tipo == 'bloqueados')
								initTable = false;
						}

					} else {
						html = "<tr><td colspan='"+ colspan	+ "' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >"+ resp.mensaje + "</td></tr>";
						$('#data-reporte').html(html);
					}
				}
			},
			function(e) {
				html = "<tr><td colspan='"+ colspan	+ "' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >Ocurrio un error en el servidor.</td></tr>";
				$('#data-reporte').html(html);

			});
}

function detalleMLP(tipo, hora, minuto) {
	var accion = "mlpReporte";
	var data = new Array();
	var colspan = 0;
	var html = "";

	dataTable = false;
	colspan = 8;
	data[data.length] = new param('tipo', tipo);
	if (tipo == 'horas') {
		data[data.length] = new param('fecha', $('#fecha').val());
	} else if (tipo == 'minutos') {
		data[data.length] = new param('fecha', $('#fecha').val());
		data[data.length] = new param('hora', hora);
	} else if (tipo == 'segundos') {
		colspan = 7;
		data[data.length] = new param('fecha', $('#fecha').val());
		data[data.length] = new param('hora', hora);
		data[data.length] = new param('minuto', minuto);
	}
	data[data.length] = new param('origen', $('#tipoTransaccion').val());
	data[data.length] = new param('tecnologia', $('#tecnologia').val());

	showTable(tipo);

	if (tipo == 'horas')
		$('#data-reporte-horas')
				.html(
						"<tr><td colspan='"
								+ colspan
								+ "' align='center'><img src='images/loading.gif' /></td></tr>");
	else if (tipo == 'minutos')
		$('#data-reporte-minutos')
				.html(
						"<tr><td colspan='"
								+ colspan
								+ "' align='center'><img src='images/loading.gif' /></td></tr>");
	else if (tipo == 'segundos')
		$('#data-reporte-segundos')
				.html(
						"<tr><td colspan='"
								+ colspan
								+ "' align='center'><img src='images/loading.gif' /></td></tr>");

	doAjax(
			accion,
			data,
			'GET',
			function(resp) {
				if (resp) {
					if (resp.tipoRpta == 'OK') {

						dataTable = false;
						var datos = resp.reportes;
						for ( var i = 0; i < datos.length; i++) {
							html += "<tr>";

							html += "<td>" + datos[i].identificador + "</td>";
							html += "<td>" + datos[i].agps + "</td>";
							html += "<td>" + datos[i].triangulacion + "</td>";
							html += "<td>" + datos[i].celda + "</td>";
							html += "<td>" + datos[i].ok + "</td>";
							html += "<td>" + datos[i].error + "</td>";
							var total = datos[i].ok + datos[i].error;
							html += "<td>" + total + "</td>";
							if (tipo != 'segundos') {
								var tipoDetalle = "";
								if (tipo == 'horas')
									html += "<td><a href='#' onClick='detalleMLP(\"minutos\", "
											+ datos[i].param
											+ ")'><img title='Ver Detalle' src='images/detail.png' /></a></td>";
								else if (tipo == 'minutos') {
									var p = datos[i].identificador;
									console.log("IDENTIFICADOR : " + p);
									var pSplit = p.split(":");
									html += "<td><a href='#' onClick='detalleMLP(\"segundos\", "
											+ pSplit[0]
											+ ","
											+ pSplit[1]
											+ ")'><img title='Ver Detalle' src='images/detail.png' /></a></td>";
								}
							} else {
								colspan = 7;
							}
							html += "</tr>";
						}
						if (datos.length == 0) {
							html = "<tr><td colspan='"
									+ colspan
									+ "' align='center' style='color: red;'>No se encontro informacion para el reporte</td></tr>";
						}
						if (tipo == 'horas')
							$('#data-reporte-horas').html(html);
						else if (tipo == 'minutos')
							$('#data-reporte-minutos').html(html);
						else if (tipo == 'segundos')
							$('#data-reporte-segundos').html(html);

					} else {
						html = "<tr><td colspan='"
								+ colspan
								+ "' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >"
								+ resp.mensaje + "</td></tr>";
						if (tipo == 'horas')
							$('#data-reporte-horas').html(html);
						else if (tipo == 'minutos')
							$('#data-reporte-minutos').html(html);
						else if (tipo == 'segundos')
							$('#data-reporte-segundos').html(html);
					}
				}
			},
			function(e) {
				html = "<tr><td colspan='"
						+ colspan
						+ "' align='center' style='color: red;'><img src='images/error.jpg'  width='15' >Ocurrio un error en el servidor.</td></tr>";
				if (tipo == 'horas')
					$('#data-reporte-horas').html(html);
				else if (tipo == 'minutos')
					$('#data-reporte-minutos').html(html);
				else if (tipo == 'segundos')
					$('#data-reporte-segundos').html(html);

			});
}

function tipoReporte() {
	var tipo = $("#tipo option:selected").val();
	var valor = document.getElementById('valor');
	valor.options.length = 0;
	if (tipo == '0') {
		$('#labelValor').hide();
		$('#valor').hide();
	} else if (tipo == '1') {
		$('#labelValor').show();
		$('#valor').show();
		combobox('usuario');
	} else if (tipo == '2') {
		$('#labelValor').show();
		$('#valor').show();
		combobox('grupo');
	}
}

function detener(id, entidad) {
	var letra = entidad.substring(0, 1).toUpperCase();
	var accion = "cancelar" + letra + entidad.substring(1);
	$("#dialog")
			.dialog(
					{
						autoOpen : true,
						resizable : false,
						height : 140,
						modal : true,
						buttons : {
							"Detener" : function() {
								$('#mensaje')
										.html(
												"<tr><td colspan='8' align='center'><img src='images/loading.gif' /></td></tr>");
								var data = new Array();
								data[data.length] = new param(entidad + '.id'
										+ letra + entidad.substring(1), id);
								var mensaje = '';
								doAjax(
										accion,
										data,
										'POST',
										function(resp) {
											if (resp) {
												if (resp.tipoRpta == 'OK') {
													mensaje = "<img src='images/check.jpg'  width='15' >Se detuvo "
															+ entidad
															+ " correctamente";
													$("#mensaje").addClass(
															"succes");
													$(".formato-tabla")
															.dataTable()
															.fnDestroy();
													listar(entidad);
												} else {
													mensaje = "<img src='images/error.jpg'  width='15' >Hubo un error deteniendo "
															+ entidad;
												}
											} else {
												mensaje = "<img src='images/error.jpg'  width='15' >Hubo un error deteniendo "
														+ entidad;
											}
											$("#mensaje").html(mensaje).fadeIn(
													300);
										},
										function(e) {
											mensaje = "<img src='images/error.jpg'  width='15' >Hubo un error conectandose con el servidor";
											$("#mensaje").text(mensaje).fadeIn(
													300);
										});
								$(this).dialog("close");
							},
							"Cerrar" : function() {
								$(this).dialog("close");
							}
						},
						// para que el overlay ocupe toda la pantalla en caso la
						// pantalla tenga scroll
						open : function(event, ui) {
							$('.ui-widget-overlay').width($(document).width());
							$('.ui-widget-overlay')
									.height($(document).height());
						}
					});

}

function cancelar(id, entidad) {
	var letra = entidad.substring(0, 1).toUpperCase();
	var accion = "cancelar" + letra + entidad.substring(1);
	$("#dialog")
			.dialog(
					{
						autoOpen : true,
						resizable : false,
						height : 140,
						modal : true,
						buttons : {
							"Cancelar" : function() {
								$('#mensaje')
										.html(
												"<tr><td colspan='8' align='center'><img src='images/loading.gif' /></td></tr>");
								var data = new Array();
								data[data.length] = new param(entidad + '.id'
										+ letra + entidad.substring(1), id);
								var mensaje = '';
								doAjax(
										accion,
										data,
										'POST',
										function(resp) {
											if (resp) {
												if (resp.tipoRpta == 'OK') {
													mensaje = "<img src='images/check.jpg'  width='15' >Se cancelo "
															+ entidad
															+ " correctamente";
													$("#mensaje").addClass(
															"succes");
													listar(entidad);
													$(".formato-tabla")
															.dataTable()
															.fnDestroy();
												} else {
													mensaje = "<img src='images/error.jpg'  width='15' >Hubo un error cancelando "
															+ entidad;
												}
											} else {
												mensaje = "<img src='images/error.jpg'  width='15' >Hubo un error cancelando "
														+ entidad;
											}
											$("#mensaje").html(mensaje).fadeIn(
													300);
										},
										function(e) {
											mensaje = "<img src='images/error.jpg'  width='15' >Hubo un error conectandose con el servidor";
											$("#mensaje").text(mensaje).fadeIn(
													300);
										});
								$(this).dialog("close");
							},
							"Cerrar" : function() {
								$(this).dialog("close");
							}
						},
						// para que el overlay ocupe toda la pantalla en caso la
						// pantalla tenga scroll
						open : function(event, ui) {
							$('.ui-widget-overlay').width($(document).width());
							$('.ui-widget-overlay')
									.height($(document).height());
						}
					});
	return false;

}
function perfilUsuario() {
	var accion = "perfilUsuario";
	var data = obtenerDatosDeForm("usuario");
	var mensaje = "";
	doAjax(accion,data,'GET',function(resp) {
				if (resp) {
					if (resp.tipoRpta == 'OK') {
						// mensaje="El perfil fue modificado
						// satisfactoriamente.";
						$("#mensaje")
								.removeClass()
								.html(
										"<span class='ui-icon ui-icon-info' style='float:left; margin:0 7px 20px 0;'/>El perfil fue modificado satisfactoriamente.");
						// new Messi(mensaje, {title: '', buttons: [{id: 0,
						// label: 'Continuar', val: 'X'}],callback: function()
						// {location.href="./index.jsp";}});
						crearDialogoInfo("#mensaje");
					} else {
						$("#mensaje").addClass("error").text(
								"No se pudo modificar: " + resp.mensaje)
								.fadeIn(300);
					}
				}
			}, function(e) {
				$("#mensaje").addClass("error").text("Error").fadeIn(300);
			});
}
function addCampos() {
	var html = '<p><select class="horario" name="horario.dia"><option value ="-1" selected="selected">Seleccione</option><option value ="1">Lunes</option><option value ="2">Martes</option><option value ="3">Miercoles</option><option value ="4">Jueves</option><option value ="5">Viernes</option><option value ="6">Sabado</option><option value ="7">Domingo</option></select> <input type="text" class="short horaInicio" name="horario.horaInicio" /> a <input type="text" class="short horaFin" name="horario.horaFin" /></p>';
	$("#agregados").append(html);
}
/* Tracking */
function nuevoTracking() {
	$("#list-tracking").hide();
	$("#registrar-tracking").fadeIn(300);
	$('#mensaje').html('');
}
/*Inicio 24/06/2013 MDTECK Sandy Huanco
 * Guardar, datos asociados al tracking seleccionado*/
function verTracking(id, fechaInicial, fechaFinal, etiqueta, numero) {	
	/*Inicio 24/06/2013 MDTECK Sandy Huanco**/
	var idTracking = $("#idTracking").val();
	$("#list-tracking").slideUp(500);
	/*Fin*/
	$("#ver-tracking").slideDown(500).show();	
//	$("#mensajeVer").css("display", "block");
	
	$("#idTracking").val(id);
	$("#fechaInicioSel").val(fechaInicial);
	$("#fechaFinSel").val(fechaFinal);
	
	$("#fechaInicioB").val(fechaInicial);
	$("#fechaFinB").val(fechaFinal);
	$('#fechaInicioB').datepicker('destroy');
	$('#fechaFinB').datepicker('destroy');
	$("#fechaInicioB").datepicker({ dateFormat: 'dd/mm/yy', minDate: fechaInicial, maxDate: fechaFinal});
	$("#fechaFinB").datepicker({ dateFormat: 'dd/mm/yy', minDate: fechaInicial, maxDate: fechaFinal});
	
	$("#etiquetaUsuario").html(etiqueta+" - "+numero);
	/*Inicio 24/06/2013 MDTECK Sandy Huanco**/

	if(idTracking!=''){		
		$(".formato-tabla-detalle").dataTable().fnDestroy();
		$('#path').val("");
		$('#points').val("");	
		$('#pointsInicial').val("");
		$('#pointsFinal').val("");
		$('#nombreLocalizacion').val("");
		$(".formato-tabla-detalle").dataTable().fnDestroy();
		$('#detalle-tracking').html("");
		map.destroy();
	}	
	/*Fin*/
	dojo.addOnLoad(init);	
}

/*filtrar dentro del tracking*/

function obtenerFiltrosTracking() {
	var id = $('#idTracking').val();
	var fechaInicio = $('#fechaInicioB').attr('value');
	var fechaFin = $('#fechaFinB').attr('value');
	var horaInicio = $('#txtHoraInicio').attr('value');
	var horaFin = $('#txtHoraFin').attr('value');
	var metodo = $("#tipo_tracking option:selected").text().toUpperCase();
	
	var dataTable = true;
	var data = new Array();
	var html = "";
		
	if(fechaInicio==""){
		fechaInicio = $("#fechaInicioSel").val();
	}
	if(fechaFin==""){
		fechaFin = $("#fechaFinSel").val();
	}
	
	$(".formato-tabla-detalle").dataTable().fnDestroy();
	$('#path').val("");
	$('#points').val("");	
	$('#pointsInicial').val("");
	$('#pointsFinal').val("");
	$('#nombreLocalizacion').val("");
	
	data[data.length] = new param('tracking.idTracking', id);
	data[data.length] = new param('fechaInicial', fechaInicio);
	data[data.length] = new param('fechaFin', fechaFin);
	data[data.length] = new param('horarioSolo.horaInicio', horaInicio);
	data[data.length] = new param('horarioSolo.horaFin', horaFin);
	data[data.length] = new param('modo', metodo);
		
	$('#detalle-tracking').html("<tr><td colspan='6' align='center'><img src='images/loading.gif' /></td></tr>");
	doAjax('obtenerFiltrosTracking',data,'GET',function(resp) {
				if (resp) {
					if (resp.tipoRpta == 'OK') {
						var posiciones = resp.tracking.posiciones;
						if (posiciones.length == 0) {
							$('#detalle-tracking').html("<tr><td colspan='4' align='center'>No hay posiciones registradas para este tracking</td></tr>");
						}
						var paths = [];// "";
						var pathsGoogle = "";// "";
						var points = "";
						// el arreglo debe venir ordenado por fecha/hora
						// slice(0) es una forma rapida de copiar un arreglo
						// (copia superficial)
//						var ultimas20Pos = posiciones.slice(0).reverse().slice(0,20);
						/*Inicio 24/06/2013 MDTECK Sandy Huanco
						 * Se mostrara todo*/
//						var ultimas20Pos = posiciones;
						
						for ( var j = 0; j < posiciones.length; j++) {
							html += "<tr id='" + posiciones[j].time + "'>";
							html += "<td>"+(j+1)+"</td>";
							html += "<td class='time'>" + (posiciones[j].time).substr(0, 16)+ "</td>";
							html += "<td class='direccion'>"	+ posiciones[j].direccion.direccion+ "</td>";
							html += "<td class='distrito'>"	+ posiciones[j].direccion.distrito+ "</td>";
							html += "<td class='metodo'>"+ posiciones[j].metodo + "</td>";
							html += "<td class='metodo'><a href='#' onclick='ubicarMapa(\""+(j+1)+"\","+"\""+ posiciones[j].coordenada.longitud+ "\","+"\""+ posiciones[j].coordenada.latitud+"\","+"\""+ posiciones[j].time+"\","+"\""+ posiciones[j].metodo+"\","+"\""+ posiciones[j].direccion.direccion+"\","+"\""+ posiciones[j].direccion.distrito+"\","+"\""+posiciones.length +"\")'><img title='Ver Mapa' src='images/map.png' /></td>";
							html += "</tr>";
						}
						var strDireccion;
						for ( var i = 0; i < posiciones.length; i++) {
							var c = posiciones[i].coordenada;
							/*Inicio 25/06/2013 MDTECK Sandy Huanco*/
							strDireccion = posiciones[i].direccion.direccion == null ? "-": posiciones[i].direccion.direccion;
							points += "" + c.longitud + "¿?" + c.latitud + "¿?"+ posiciones[i].time + "¿?"	+ posiciones[i].metodo + "¿?"+strDireccion+ "¿?"+posiciones[i].direccion.distrito+"$$";
							/*Fin 25/06/2013*/
							// paths+= "["+c.longitud+","+c.latitud+"],";
							if (i != posiciones.length - 1) {
								var c2 = posiciones[i + 1].coordenada;
								paths.push([ [ c.longitud, c.latitud ],[ c2.longitud, c2.latitud ] ]);
								pathsGoogle +=""+c.longitud+"??"+ c.latitud+"$$";
							}
						}
						if (posiciones.length != 0) {
							dataTable = true;
							// paths=paths.substring(0,paths.length-1);
							points = points.substring(0, points.length - 1);
							// paths="[["+paths+"]]";
							var strPaths = '{"paths":' + JSON.stringify(paths)+ ',"spatialReference":{" wkid":4326 }}';
							var ultimo = posiciones.length - 1;
							var infoPointInicial = posiciones[0].coordenada.longitud + "$"+ posiciones[0].coordenada.latitud+"$"+posiciones[0].time+"$"+posiciones[0].metodo+"$"+posiciones[0].direccion.direccion+"$"+posiciones[0].direccion.distrito;
							var infoPointFinal = posiciones[ultimo].coordenada.longitud + "$"+ posiciones[ultimo].coordenada.latitud+"$"+posiciones[ultimo].time+"$"+posiciones[ultimo].metodo+"$"+posiciones[ultimo].direccion.direccion+"$"+posiciones[ultimo].direccion.distrito;
							$('#path').val(strPaths);
							$('#pathG').val(pathsGoogle);
							$('#points').val(points);	
							$('#pointsInicial').val(infoPointInicial);
							$('#pointsFinal').val(infoPointFinal);
							$('#nombreLocalizacion').val(resp.tracking.monitoreado.etiqueta);
							
//							$('#posicionFinal').val(posiciones[posiciones.length - 1].coordenada.longitud+ ","+ posiciones[posiciones.length - 1].coordenada.latitud);
//							$('#ultimaLocalizacion').val(posiciones[posiciones.length - 1].time);
//							
//							$('#metodo').val(posiciones[posiciones.length - 1].metodo);
//
//							$('#posicionInicial').val(posiciones[0].coordenada.longitud + ","+ posiciones[0].coordenada.latitud);
//							$('#primeraLocalizacion').val(posiciones[0].time);
//							$('#metodoInicial').val(posiciones[0].metodo);

						}

					} else {
						$('#detalle-tracking').html("<tr><td colspan='4' align='center'>"+ resp.mensaje + "</td></tr>");
					}
					$('#detalle-tracking').html(html);
					/*Inicio MDTECK*/
					if (dataTable) {
						$(".formato-tabla-detalle").dataTable(
								{
									"oLanguage" : {
										"sUrl" : "../javascript/language.txt"
									},
									"bSort" : false,
									"bDestroy" : true,
									"aLengthMenu" : [ [ 10, 25, 50, 100, -1 ],
											[ 10, 25, 50, 100, "Todos" ] ]
								});
					}
					map.destroy();
					/*Fin MDTECK*/
					dojo.addOnLoad(init);
				} else {
					$('#detalle-tracking').html("<tr><td colspan='4' align='center'>Hubo un error comunicandose con el servidor</td></tr>");
				}
			}, function(e) {

			});
	$("#list-tracking").hide();
	$("#ver-tracking").fadeIn();

}
/*Fin 24/06/2013 MDTECK Sandy Huanco*/
/* Seleccion Usuarios */
function checkTodos(a) {

	$(".selection").attr("checked", false);
	$("#tipo-" + a).attr("checked", "checked");
	if (a == '0') {
		$("#monitoreados option").attr("selected", true);
	} else if (a == '1') {
		$("#monitoreados option").removeAttr("selected");
	}
}

/* Inicio MDTECK: Modificacion del metodo, para la exportación */
function pasarParametrosExcel() {
	$('#txtFechaInicio').attr('value', $('#fechaInicio').val());
	$('#txtFechaFin').attr('value', $('#fechaFin').val());
	$('#txtTipo').attr('value', $('#tipo').val());
	$('#txtValor').attr('value', $('#valor').val());
	$('#txtIdCuenta').attr('value', localStorage.getItem('cuenta'));

	document.forms["excel"].submit();
}

function pasarParametrosExcelTracking() {
	var id = $('#idTracking').val();	
	var metodo = $("#tipo_tracking option:selected").text().toUpperCase();
	$('#txtFechaInicioF').attr('value', $('#fechaInicioB').val());
	$('#txtFechaFinF').attr('value', $('#fechaFinB').val());
	$('#txtHoraInicioF').attr('value', $('#txtHoraInicio').val());
	$('#txtHoraFinF').attr('value', $('#txtHoraFin').val());
	$('#txtMetodoF').attr('value', metodo);
	$('#txtIdTrackingF').attr('value', id);
	document.forms["excelTracking"].submit();
}


function verMapaTracking(id, monitor, fecha, usuario, latitud, longitud, direccion, distrito, metodo, tecnologia) {
	localStorage.setItem('idTrackingM', id);
	localStorage.setItem('monitorM', monitor);
	localStorage.setItem('fechaM', fecha);
	localStorage.setItem('usuarioM', usuario);
	localStorage.setItem('latitudM', latitud);
	localStorage.setItem('longitudM', longitud);
	localStorage.setItem('direccionM', direccion);
	localStorage.setItem('distritoM', distrito);
	localStorage.setItem('metodoM', metodo);
	localStorage.setItem('tecnologiaM', tecnologia);
	var url = "../user/mostrar-mapa.jsp";
	var windowName = "popUp";
	var caracteristicas = 'height=520px,width=900,top=100px,left=200px,scrollbars=yes';
	
	window.open(url, windowName, caracteristicas);	
	
//	var w = window.open();
//	var html = $("#toNewWindow").html();
//
//	$(w.document.body).html(html);
}
/* Fin MDTECK */

function gotoExcel(tabla) {
	$('#data').attr('value', $('#' + tabla).html());
	document.forms["excel"].submit();
}

function mapaGeocerca(idGeocerca) {
	$('#lista-reporte').hide();
	$('#mapa-geocerca').fadeIn(300);
	var datos = new Array();
	datos[datos.length] = new param('geocerca.idGeocerca', idGeocerca);
	var coordenadas = "";
	doAjax('obtenerGeocerca', datos, 'GET', function(resp) {
		var rings = resp.geocerca.rings.split(",");
		for ( var i = 0; i < rings.length; i++) {
			coordenadas += "[" + rings[i] + "," + rings[i + 1] + "],";
			i++;
			if ((i + 1) == rings.length) {
				break;
			}
		}
		coordenadas = coordenadas.substring(0, coordenadas.length - 1);
		coordenadas = "[[" + coordenadas + "]]";
		coordenadas = '{"rings":' + coordenadas
				+ ',"spatialReference":{" wkid":4326 }}';

		$("#rings").attr("value", coordenadas);
		dojo.addOnLoad(init);
	}, function(e) {
		alert("ERROR");
	});
}

function detalleGeocerca(idGeocerca) {
	$('#lista-reporte').hide();
	$('#buttonsDiv').show();
	$('#detail-data').fadeIn(300);
	$('#idDetalleSelec').val(idGeocerca);
	
	var datos = new Array();
	datos[datos.length] = new param('geocerca.idGeocerca', idGeocerca);
	var html = "";
	$('#data-reporte').html("<tr><td colspan='6' align='center'><img src='images/loading.gif' /></td></tr>");
	doAjax(
			'detalleGeocerca',
			datos,
			'GET',
			function(resp) {
				var data = resp.posiciones;
				if (resp) {
					if (resp.tipoRpta == 'OK') {
						if (data != null) {
							for ( var i = 0; i < data.length; i++) {
								html += "<tr>";
								html += "<td class='nombre'>"
										+ data[i].usuario.etiqueta + " - "
										+ data[i].usuario.numero + "</td>";
								if (data[i].time == null)
									html += "<td>-</td>";
								else
									html += "<td>" + data[i].time + "</td>";
								var e = data[i].estado;
								var estado = ' - ';
								if (e == '0')
									estado = 'Salida';
								else
									estado = 'Entrada';
								html += "<td>" + estado + "</td>";
								if (data[i].metodo == null)
									html += "<td class='tipo'>-</td>";
								else
									html += "<td class='tipo'>"
											+ data[i].metodo + "</td>";
								if (data[i].direccion.direccion == null)
									html += "<td class='tipo'>-</td>";
								else
									html += "<td class='tipo'>"
											+ data[i].direccion.direccion
											+ "</td>";
								if (data[i].direccion.distrito == null)
									html += "<td class='tipo'>-</td>";
								else
									html += "<td class='tipo'>"
											+ data[i].direccion.distrito
											+ "</td>";
								html += "</tr>";
							}
						} else {
							html = "<tr><td colspan='6' align='center'>Hubo un error obteniendo el detalle de la geocerca</td></tr>";
						}
					} else {
						html = "<tr><td colspan='6' align='center'>"
								+ data.mensaje + "</td></tr>";
					}
				}
				if (data.length == 0)
					html = "<tr><td colspan='6' align='center'>No se encontraron registros para esta geocerca.</td></tr>";
				$('#detail-reporte').html(html);
			}, function(e) {
				alert("ERROR");
			});

}

function prepararClonarGeocerca() {
	// volvemos modificables los campos
	$("#geocerca-form :input").not("#horaInicio").not("#horaFin").attr(
			"readonly", false);
	$("#fechaInicio").datepicker({
		dateFormat : 'dd/mm/yy',
		minDate : new Date()
	});
	$("#fechaFin").datepicker({
		dateFormat : 'dd/mm/yy',
		minDate : new Date()
	});
	$("#usuario").off("mousedown", false);
	// mostramos y ocultamos los botones necesarios
	$("button#registrar").show();
	$("button#clonar").hide();
	$("button#draw").show();
}

function prepararImagen(input) {
	if (window.FileReader) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#img_prev').attr('src', e.target.result).height(50);
				$('#img_prev').show();
			};
			reader.readAsDataURL(input.files[0]);
		}
	}
	/*
	 * if (navigator.appName === "Microsoft Internet Explorer") {
	 * document.getElementById("img_prev").src = input.value;
	 * $('#img_prev').show(); }
	 */
}

function permitirSoloNumeros(event) {
	if (event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9
			|| event.keyCode == 27 || event.keyCode == 13 ||
			// Allow: Ctrl+A
			(event.keyCode == 65 && event.ctrlKey === true) ||
			// Allow: home, end, left, right
			(event.keyCode >= 35 && event.keyCode <= 39)) {
		// let it happen, don't do anything
		return;
	} else {
		// Ensure that it is a number and stop the keypress
		if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57)
				&& (event.keyCode < 96 || event.keyCode > 105)) {
			event.preventDefault();
		}
	}
}

// TODO CAMBIO POR HERNANQ 15/07
function dateCompare(time1, time2) {

	var parts = time1.split(":");

	if (parts[0] > 23 || parts[1] > 59)
		return -2;

	var t1 = new Date();

	t1.setHours(parts[0], parts[1], 0, 0);

	parts = time2.split(":");

	if (parts[0] > 23 || parts[1] > 59)
		return -2;

	var t2 = new Date();

	t2.setHours(parts[0], parts[1], 0, 0);

	// returns 1 if greater, -1 if less and 0 if the same

	if (t1.getTime() > t2.getTime())
		return -1;

	if (t1.getTime() < t2.getTime())
		return 1;

	return 0;

}
// TODO FIN DE CAMBIO

function comboboxDepartamento() {
	$('#distrito').html(" ");
	doAjax('listarDepartamentosPuntoInteres', null, 'GET', function(resp) {
		var html = "<option >Seleccione</option>";
		if (resp) {
			if (resp.tipoRpta == "OK") {
				var data = resp.departamentos;
				for ( var i = 0; i < data.length; i++) {
					html += "<option value='" + data[i].idUbigeo + "'>"
							+ data[i].nombre + "</option>";
				}
				$('#departamento').html(html);
			}
		}
	}, null);
}
function comboboxProvincia() {
	var datos = new Array();
	datos[datos.length] = new param('puntoInteres.idDepartamento', $(
			'#departamento').val());
	doAjax('listarProvinciasPuntoInteres', datos, 'GET', function(resp) {
		var html = "<option >Seleccione</option>";
		if (resp) {
			if (resp.tipoRpta == "OK") {
				var data = resp.provincias;
				for ( var i = 0; i < data.length; i++) {
					html += "<option value='" + data[i].idUbigeo + "'>"
							+ data[i].nombre + "</option>";
				}
				$('#provincia').html(html);
			}
		}
	}, null);
}

function comboboxDistrito() {
	var datos = new Array();
	datos[datos.length] = new param('puntoInteres.idDepartamento', $(
			'#departamento').val());
	datos[datos.length] = new param('puntoInteres.idProvincia', $('#provincia')
			.val());
	doAjax('listarDistritosPuntoInteres', datos, 'GET', function(resp) {
		var html = "";
		if (resp) {
			if (resp.tipoRpta = "OK") {
				var data = resp.distritos;
				for ( var i = 0; i < data.length; i++) {
					html += "<option value='" + data[i].idUbigeo + "'>"	+ data[i].nombre + "</option>";
				}
				$('#distrito').html(html);
			}
		}
	}, null);
}

function moverNumero(de, a) {
	var deLength = de.length;
	var textoSeleccionado = new Array();
	var valorSeleccionado = new Array();
	var selectedCount = 0;

	var i;

	for (i = deLength - 1; i >= 0; i--) {
		if (de.options[i].selected) {
			textoSeleccionado[selectedCount] = de.options[i].text;
			valorSeleccionado[selectedCount] = de.options[i].value;
			eliminarOpcion(de, i);
			eliminarDeSeleccionCopy(valorSeleccionado[selectedCount]);
			selectedCount++;
		}
	}
	for (i = selectedCount - 1; i >= 0; i--) {
		agregarOpcion(a, textoSeleccionado[i], valorSeleccionado[i]);
	}

	if ($('.tipoFiltro') == a) {
		actualizarSeleccionCopy(de);
	}
}

function eliminarDeSeleccionCopy(value) {
	var otro = new Array();
	for ( var i = 0; i < optionscopy.length; i++) {
		if (optionscopy[i].value != value) {
			otro.push(optionscopy[i]);
		}
	}
	optionscopy = otro;
}

function actualizarSeleccionCopy(de) {
	var copy = optionscopy;
	var otro = new Array();
	for ( var i = 0; i < de.length; i++) {
		var v = true;
		for ( var j = 0; j < copy.length; j++) {
			if (de.options[i].value == copy[j].value) {
				v = false;
				break;
			}
		}
		if (v) {
			otro.push(de.options[i]);
		}
	}

	for ( var i = 0; i < otro.length; i++) {
		optionscopy.push(otro[i]);
	}
}

function filterSelectBox(pattern, id) {
	var loop = 0, index = 0, regexp;
	match_text = true;
	match_value = false;
	var selectobj = document.getElementById(id);
	selectobj.options.length = 0;

	try {
		regexp = new RegExp(pattern, 'i');
	} catch (e) {
		return;
	}

	for (loop = 0; loop < optionscopy.length; loop++) {
		var option = optionscopy[loop];
		if ((match_text && regexp.test(option.text))
				|| (match_value && regexp.test(option.value))) {
			selectobj.options[index++] = new Option(option.text, option.value,
					false);

		}
	}

}

function eliminarOpcion(seleccionado, indice) {
	var seleccionadoLength = seleccionado.length;
	if (seleccionadoLength > 0) {
		seleccionado.options[indice] = null;
	}
}

function agregarOpcion(seleccionado, texto, valor) {
	var opcion = new Option(texto, valor);
	var seleccionadoLength = seleccionado.length;
	seleccionado.options[seleccionadoLength] = opcion;
	if (seleccionado.options == optionscopy) {
		optionscopy.push(opcion);
	}
}

function sombrearTodos(lista) {
	// var obj = document.getElementById(lista);
	for ( var i = 0; i < lista.options.length; i++) {
		lista.options[i].selected = true;
	}
}

function moverTodos(de, a) {
	sombrearTodos(de);
	moverNumero(de, a);

}