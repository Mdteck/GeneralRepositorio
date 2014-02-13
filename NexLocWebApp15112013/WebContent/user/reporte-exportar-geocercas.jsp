<%@ page import="java.io.PrintWriter" pageEncoding="UTF-8"%>
<%@ page import="pe.com.nextel.action.*,java.util.List,java.util.ArrayList,pe.com.nextel.bean.*"%>
<%@ page contentType="application/excel" language="java"%>

<%
	response.reset();
	response.setHeader("Content-type","application/excel;charset=ISO-8859-1");
	response.setHeader("Content-disposition","inline; filename=reporteGeocerca.xls");

	PrintWriter op = response.getWriter();
	String tipo = request.getParameter("txtTipo");
	if (tipo.equals("todo")) {
		String idUsuario = request.getParameter("txtIdUsuario");
		String lista = request.getParameter("txtLista");
		String tipo2 = request.getParameter("txtTipo2");

		ReporteAction reporte = new ReporteAction();
		GeocercaDTO geocercaDTO = new GeocercaDTO();
		UsuarioDTO usuario = new UsuarioDTO();
		CuentaDTO cuenta = new CuentaDTO();
		if (tipo2.equalsIgnoreCase("2")) {
			usuario.setIdUsuario(idUsuario);
			
		} else {
			cuenta.setIdCuenta(idUsuario);
			usuario.setCuenta(cuenta);
		}		
		geocercaDTO.setUsuario(usuario);
		reporte.setGeocerca(geocercaDTO);	
		reporte.setListarPorUsuario(lista);
		
		reporte.geocerca();

		List<GeocercaDTO> geocercas = reporte.getGeocercas();
		if (geocercas != null) {
			int i = 1;
%>
<table>
	<tr>
		<td>Geocerca</td>
		<td>Fecha Creacion</td>
		<td>Usuario</td>
		<td>Estado</td>
		<td>Fecha Ejecucion</td>
		<td>Fecha Culminacion</td>
	</tr>
	<%
		for (GeocercaDTO po : geocercas) {
					String nombre = po.getNombre() == null ? "-" : po
							.getNombre();
					String fechaRegistro = po.getFechaRegistro() == null ? "-"
							: po.getFechaRegistro();
					String estado = "";
					int e = po.getEstado();
					if (e == 0)
						estado = "Cancelado";
					else if (e == 1)
						estado = "StandBy";
					else if (e == 2)
						estado = "Iniciado";
					else if (e == 3)
						estado = "Terminado";
					else
						estado = " - ";
	%>
	<tr>
		<td>
			<%
				out.print(nombre);
			%>
		</td>
		<td>
			<%
				out.print(fechaRegistro);
			%>
		</td>
		<td>
			<%
				if ((po.getUsuario().getEtiqueta() == null)
									&& (po.getUsuario().getNumero() == null))
								out.print("-");
							else if (po.getUsuario().getEtiqueta() == null)
								out.print(po.getUsuario().getNumero());
							else if (po.getUsuario().getNumero() == null)
								out.print(po.getUsuario().getEtiqueta());
							else
								out.print(po.getUsuario().getEtiqueta() + " - "
										+ po.getUsuario().getNumero());
			%>
		</td>
		<td>
			<%
				out.print(estado);
			%>
		</td>
		<td>
			<%
				if ((po.getHorario().getFechaInicio() == null)
									&& (po.getHorario().getHoraInicio() == null))
								out.print("-");
							else if (po.getHorario().getFechaInicio() == null)
								out.print(po.getHorario().getHoraInicio());
							else if (po.getHorario().getHoraInicio() == null)
								out.print(po.getHorario().getFechaInicio());
							else
								out.print(po.getHorario().getFechaInicio() + " "
										+ po.getHorario().getHoraInicio());
			%>
		</td>
		<td>
			<%
				if ((po.getHorario().getFechaFin() == null)
									&& (po.getHorario().getHoraFin() == null))
								out.print("-");
							else if (po.getHorario().getFechaFin() == null)
								out.print(po.getHorario().getHoraFin());
							else if (po.getHorario().getHoraFin() == null)
								out.print(po.getHorario().getFechaFin());
							else
								out.print(po.getHorario().getFechaFin() + " "
										+ po.getHorario().getHoraFin());
			%>
		</td>

	</tr>
	<%
		i++;
				}
				if (geocercas.size() == 0) {
	%>
	<tr>
		<td colspan="7">No se encontraron registros para los geocercas
			registrada</td>
	</tr>
	<%
		}
	%>
</table>
<%
	}
	}else{
		if (tipo.equals("detalle")) {
			String idGeocerca = request.getParameter("txtIdDetalle");
			
			GeocercaAction geocerca = new GeocercaAction();
			GeocercaDTO geocercaDTO = new GeocercaDTO();
			
			geocercaDTO.setIdGeocerca(idGeocerca);
			geocerca.setGeocerca(geocercaDTO);			
			
			geocerca.detalle();

			List<PosicionDTO> posiciones = geocerca.getPosiciones();
			if (posiciones != null) {
				int i=1;%>
				<table>
					<tr>
						<td>Monitoreado</td>
						<td>Fecha</td>
						<td>Movimiento</td>
						<td>Método de Ubicación</td>
						<td>Dirección</td>
						<td>Distrito</td>
					</tr>
					<%
					for (PosicionDTO po : posiciones) {
						String fecha = po.getTime() == null ? "-":po.getTime();
						String metodo = po.getMetodo() ==null ? "-": po.getMetodo();
						String direccion = po.getDireccion().getDireccion() ==null ? "-": po.getDireccion().getDireccion();
						String distrito = po.getDireccion().getDistrito() ==null ? "-": po.getDireccion().getDistrito();
					%>
						<tr>
							<td><% out.print(po.getUsuario().getEtiqueta()+" - "+po.getUsuario().getNumero());%></td>
							<td><% out.print(fecha);%></td>
							<td>
								<%
								String e = po.getEstado();								;
								String estado = " - ";
								if (e == "0")
									estado = "Salida";
								else
									estado = "Entrada";
								out.print(estado);
								%>								
							</td>
							<td><% out.print(metodo);%></td>
							<td><% out.print(direccion);%></td>
							<td><% out.print(distrito);%></td>
						</tr>
					<%
					i++;
					}
					if (posiciones.size() == 0) {
					%>
					<tr>
						<td colspan="6">No se encontraron resultados</td>
					</tr>
					<%
						}
					%>
				</table>
			<%	
			}
		}
	}
%>
