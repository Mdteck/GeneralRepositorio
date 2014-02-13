package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pe.com.nextel.bean.BloqueadosDTO;
import pe.com.nextel.bean.CoordenadaDTO;
import pe.com.nextel.bean.DireccionDTO;
import pe.com.nextel.bean.GeocercaDTO;
import pe.com.nextel.bean.HorarioDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.ReporteTransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.iface.ReporteDAO;
import pe.com.nextel.util.DataSourceBD;
import pe.com.nextel.util.MapUtil;

public class SqlServerReporteDAO implements ReporteDAO {

	@Override
	public List<PosicionDTO> localizacion(String fechaInicio, String fechaFin, int id, int modo, int idCuenta) {
		String filtroCuenta = "";
		if (idCuenta >0){
			filtroCuenta = "AND mon.idCuenta=?";
		}
		String sqlUsuario="SELECT c.idConsulta, c.time, c.msid, c.longitud, c.latitud, c.tecnologia, c.metodo, c.direccion, c.distrito, mon.numero " +
				" FROM t_consulta c " +
				" INNER JOIN t_transaccion t ON (t.idTransaccion=c.idTransaccion) " +
				" INNER JOIN t_usuario mon ON (mon.idUsuario=t.idUsuario) " +
				" INNER JOIN t_usuario u ON (u.numero=c.msid AND u.idUsuario=? AND u.estado='1') " + 
				" WHERE (c.time BETWEEN convert(datetime,?,103) AND convert(datetime,?,103)) AND c.resultado='1' order by c.time desc";
		//String sqlGrupo = "SELECT c.idConsulta, c.time, c.msid, c.longitud, c.latitud, c.tecnologia, c.metodo, c.direccion, c.distrito " +
		//		" FROM t_consulta c, t_usuario u, t_usuarioxgrupo gxu " +
		//		" WHERE u.idUsuario = gxu.idUsuario AND c.msid=u.numero AND (time BETWEEN ? AND ?) AND gxu.idGrupo = ? AND c.resultado='1' order by c.time desc";
		String sqlGrupo = "SELECT c.idConsulta, c.time, c.msid, c.longitud, c.latitud, c.tecnologia, c.metodo, c.direccion, c.distrito, mon.numero " +
				" FROM t_consulta c " +
				" INNER JOIN t_transaccion t ON (t.idTransaccion=c.idTransaccion) " +
				" INNER JOIN t_usuario mon ON (mon.idUsuario=t.idUsuario AND mon.estado='1') " +
				" INNER JOIN t_usuario u ON (u.numero=c.msid AND u.estado='1') " +
				" INNER JOIN t_usuarioxgrupo gxu ON (gxu.idUsuario=u.idUsuario) " + 
				" WHERE (c.time BETWEEN convert(datetime,?,103) AND convert(datetime,?,103)) AND gxu.idGrupo = ? AND c.resultado='1' order by c.time desc";
		//String sqlTodos="SELECT c.idConsulta, c.time, c.msid, c.longitud, c.latitud, c.tecnologia, c.metodo, c.direccion, c.distrito " +
		//		"FROM t_consulta WHERE (time BETWEEN ? AND ?) AND resultado = '1'  order by time desc";
		String sqlTodos="SELECT c.idConsulta, c.time, c.msid, c.longitud, c.latitud, c.tecnologia, c.metodo, c.direccion, c.distrito, mon.numero " +
				" FROM t_consulta c " +
				" INNER JOIN t_transaccion t ON (t.idTransaccion=c.idTransaccion) " +
				" INNER JOIN t_usuario mon ON (mon.idUsuario=t.idUsuario) " +
				" WHERE (c.time BETWEEN convert(datetime,?,103) AND convert(datetime,?,103)) " + filtroCuenta + " AND c.resultado = '1'  order by c.time desc";

		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<PosicionDTO> posiciones=new ArrayList<PosicionDTO>();
		
		try {
			conexion = DataSourceBD.openConnection();
			if(modo==ReporteDAO.INDIVIDUAL){
				pstm = conexion.prepareStatement(sqlUsuario);
				pstm.setInt(1, id);
				pstm.setString(2, fechaInicio);
				pstm.setString(3, fechaFin);
				rs=pstm.executeQuery();
				while(rs.next()){
					PosicionDTO p = new PosicionDTO();
					p.setIdPosicion(rs.getString(1));
					p.setTime(rs.getString(2));
					UsuarioDTO u = new UsuarioDTO();
					u.setNumero(rs.getString(3));
					p.setUsuario(u);
					CoordenadaDTO coordenada = new CoordenadaDTO();
					CoordenadaDTO coordenada2 = new CoordenadaDTO();
					
					String lon = rs.getString(4);
					String lat = rs.getString(5);
					
					coordenada2.setLongitud(lon);
					coordenada2.setLatitud(lat);

					try{
						lon = MapUtil.convertirCoordenada(Double.parseDouble(lon));
						lat = MapUtil.convertirCoordenada(Double.parseDouble(lat));
						coordenada.setLongitud(lon+"W");
						coordenada.setLatitud(lat+"S");
					}catch(Exception e){
						coordenada.setLongitud(lon);
						coordenada.setLatitud(lat);
					}	
					p.setCoordenada2(coordenada2);
					p.setCoordenada(coordenada);
					p.setTecnologia(rs.getString(6));
					p.setMetodo(rs.getString(7));
					DireccionDTO direccion = new DireccionDTO();
					direccion.setDireccion(rs.getString(8));
					direccion.setDistrito(rs.getString(9));
					p.setDireccion(direccion);
					UsuarioDTO moni = new UsuarioDTO();
					moni.setNumero(rs.getString(10));
					p.setMonitor(moni);
					posiciones.add(p);
				}
			}else if(modo==ReporteDAO.GRUPAL){
					pstm = conexion.prepareStatement(sqlGrupo);
					pstm.setString(1, fechaInicio);
					pstm.setString(2, fechaFin);
					pstm.setInt(3, id);
					rs=pstm.executeQuery();
					while(rs.next()){
						PosicionDTO p = new PosicionDTO();
						p.setIdPosicion(rs.getString(1));
						p.setTime(rs.getString(2));
						UsuarioDTO u = new UsuarioDTO();
						u.setNumero(rs.getString(3));
						p.setUsuario(u);
						CoordenadaDTO coordenada = new CoordenadaDTO();
						CoordenadaDTO coordenada2 = new CoordenadaDTO();
						String lon = rs.getString(4);
						String lat = rs.getString(5);

						coordenada2.setLongitud(lon);
						coordenada2.setLatitud(lat);
						try{
							lon = MapUtil.convertirCoordenada(Double.parseDouble(lon));
							lat = MapUtil.convertirCoordenada(Double.parseDouble(lat));
							coordenada.setLongitud(lon+"W");
							coordenada.setLatitud(lat+"S");
						}catch(Exception e){
							coordenada.setLongitud(lon);
							coordenada.setLatitud(lat);
						}	
						p.setCoordenada(coordenada);
						p.setCoordenada2(coordenada2);
						p.setTecnologia(rs.getString(6));
						p.setMetodo(rs.getString(7));
						DireccionDTO direccion = new DireccionDTO();
						direccion.setDireccion(rs.getString(8));
						direccion.setDistrito(rs.getString(9));
						p.setDireccion(direccion);
						UsuarioDTO moni = new UsuarioDTO();
						moni.setNumero(rs.getString(10));
						p.setMonitor(moni);
						posiciones.add(p);
				}
				
			}else if(modo==ReporteDAO.TODOS){
					pstm = conexion.prepareStatement(sqlTodos);
					pstm.setString(1, fechaInicio);
					pstm.setString(2, fechaFin);
					pstm.setInt(3, idCuenta);
					rs=pstm.executeQuery();
					while(rs.next()){
						PosicionDTO p = new PosicionDTO();
						p.setIdPosicion(rs.getString(1));
						p.setTime(rs.getString(2));
						UsuarioDTO u = new UsuarioDTO();
						u.setNumero(rs.getString(3));
						p.setUsuario(u);
						CoordenadaDTO coordenada = new CoordenadaDTO();
						CoordenadaDTO coordenada2 = new CoordenadaDTO();
						String lon = rs.getString(4);
						String lat = rs.getString(5);

						coordenada2.setLongitud(lon);
						coordenada2.setLatitud(lat);
						try{
							lon = MapUtil.convertirCoordenada(Double.parseDouble(lon));
							lat = MapUtil.convertirCoordenada(Double.parseDouble(lat));
							coordenada.setLongitud(lon+"W");
							coordenada.setLatitud(lat+"S");
						}catch(Exception e){
							coordenada.setLongitud(lon);
							coordenada.setLatitud(lat);
						}	
						p.setCoordenada(coordenada);
						p.setCoordenada2(coordenada2);
						p.setTecnologia(rs.getString(6));
						p.setMetodo(rs.getString(7));
						DireccionDTO direccion = new DireccionDTO();
						direccion.setDireccion(rs.getString(8));
						direccion.setDistrito(rs.getString(9));
						p.setDireccion(direccion);
						UsuarioDTO moni = new UsuarioDTO();
						moni.setNumero(rs.getString(10));
						p.setMonitor(moni);
						posiciones.add(p);
					}
			}
		} catch (SQLException e) {	
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return posiciones;
	}

	@Override
	public List<GeocercaDTO> geocerca(int id, boolean porIdUsuario) {
		String sqlUsu = "";
		String sqlCue = "";
		sqlUsu="SELECT g.idGeocerca, g.nombre, u.numero, u.etiqueta, g.estado, g.fechaRegistro, g.fechaInicio, g.fechaFin, g.horaInicio, g.horaFin " +
				" FROM t_geocerca g, t_usuario u WHERE g.idUsuario = u.idUsuario AND g.idUsuario = ? ORDER BY g.fechaRegistro DESC";
		sqlCue="SELECT g.idGeocerca, g.nombre, u.numero, u.etiqueta, g.estado, g.fechaRegistro, g.fechaInicio, g.fechaFin, g.horaInicio, g.horaFin " +
				" FROM t_geocerca g, t_usuario u, t_cuenta c WHERE c.idCuenta=u.idCuenta AND u.idUsuario=g.idUsuario AND c.idCuenta=? ORDER BY g.fechaRegistro DESC";
		List<GeocercaDTO> geocercas = new ArrayList<GeocercaDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			if(porIdUsuario)
				pstm = conexion.prepareStatement(sqlUsu);
			else
				pstm = conexion.prepareStatement(sqlCue);
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			geocercas=new ArrayList<GeocercaDTO>();
			while (rs.next()) {
				GeocercaDTO geocerca=new GeocercaDTO();
				geocerca.setIdGeocerca(rs.getString(1));
				geocerca.setNombre(rs.getString(2));
				UsuarioDTO usuario = new UsuarioDTO();
				usuario.setNumero(rs.getString(3));
				usuario.setEtiqueta(rs.getString(4));
				geocerca.setEstado(rs.getInt(5));
				geocerca.setFechaRegistro(rs.getString(6));
				HorarioDTO h = new HorarioDTO();
				h.setFechaInicio(rs.getString(7));
				h.setFechaFin(rs.getString(8));
				h.setHoraInicio(rs.getString(9));
				h.setHoraFin(rs.getString(10));
				geocerca.setHorario(h);
				geocerca.setUsuario(usuario);
				geocercas.add(geocerca);
			}
		} catch (SQLException e) {		
			geocercas=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return geocercas;
	}

	@Override
	public List<BloqueadosDTO> bloqueados(String idCuenta, String numero, String etiqueta) {
		String sql="select b.idBloqueo as idbloqueo, u.numero as numero, u.etiqueta as etiqueta, b.timestamp as timestamp, b.tipo as estado from t_usuario u INNER JOIN t_bloqueo b ON (u.idUsuario=b.idUsuario) WHERE u.idCuenta=? AND u.numero LIKE ? AND u.etiqueta LIKE ? AND u.estado='1' ORDER BY b.timestamp DESC";
		List<BloqueadosDTO> bloqueados = new ArrayList<BloqueadosDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, Integer.parseInt(idCuenta));
			pstm.setString(2, numero+ "%");
			pstm.setString(3, etiqueta + "%");
			rs = pstm.executeQuery();
			while (rs.next()) {
				BloqueadosDTO bloqueadosDTO = new BloqueadosDTO();
				bloqueadosDTO.setIdBloqueo(rs.getString("idbloqueo"));
				bloqueadosDTO.setNumero(rs.getString("numero"));
				bloqueadosDTO.setEtiqueta(rs.getString("etiqueta"));
				bloqueadosDTO.setTimestamp(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(rs.getTimestamp("timestamp")));
				bloqueadosDTO.setEstado(rs.getString("estado"));
				bloqueados.add(bloqueadosDTO);
			}
		} catch (SQLException e) {		
			bloqueados=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return bloqueados;
	}

	@Override
	public ReporteTransaccionDTO transacciones(String tecnologia, String fechaInicial, String fechaFinal, String origen) {
		String sqlMETODO="SELECT  c.metodo, COUNT(1) FROM t_consulta c, t_transaccion t WHERE t.idTransaccion=c.idTransaccion AND (c.time BETWEEN ? AND ?) AND c.resultado='1' ";
		String sqlRESULTADO="SELECT  c.resultado, COUNT(1) FROM t_consulta c, t_transaccion t WHERE t.idTransaccion=c.idTransaccion AND (c.time BETWEEN ? AND ?)  ";

		String sqlTecnologia = "";
		if(tecnologia.equals("3G"))
			sqlTecnologia="AND c.tecnologia LIKE '3G' " ;
		else if(tecnologia.equals("2G"))
			sqlTecnologia="AND (c.tecnologia LIKE '2G' OR c.tecnologia LIKE 'MIG') ";
		String sqlOrigen="";
		if(origen.equals("0")){
			sqlOrigen= "AND (t.tipo LIKE '1' OR t.tipo LIKE '0') ";
		}else if(origen.equals("2") || origen.equals("3")){
			sqlOrigen = String.format("AND t.tipo LIKE '%s' ",origen);
		}
		
		
		String sqlGROUP="group by ";
		
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ReporteTransaccionDTO reporte = new ReporteTransaccionDTO();

		try {
			conexion = DataSourceBD.openConnection();

				pstm = conexion.prepareStatement(sqlMETODO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.metodo");
				pstm.setString(1, fechaInicial);
				pstm.setString(2, fechaFinal);
				rs = pstm.executeQuery();
				while(rs.next()){
					String metodo = rs.getString(1);
					int value = rs.getInt(2);

					if(metodo.equals("AGPS"))				reporte.setAgps(value);
					else if(metodo.equals("TRIANGULACION"))	reporte.setTriangulacion(value);
					else if(metodo.equals("CELDA"))			reporte.setCelda(value);

				}
				
				pstm = conexion.prepareStatement(sqlRESULTADO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.resultado");
				pstm.setString(1, fechaInicial);
				pstm.setString(2, fechaFinal);
				rs = pstm.executeQuery();
				while(rs.next()){
					String resultado = rs.getString(1);
					int value = rs.getInt(2);
					
					if(resultado.equals("1"))			reporte.setOk(value);
					else if(resultado.equals("0"))		reporte.setError(value);
				}
			
			
		} catch (SQLException e) {		
			reporte=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return reporte;
	}

	@Override
	public ReporteTransaccionDTO transaccionesPorHora(String tecnologia, String date, String hora, String origen) {
		String sqlMETODO="SELECT  c.metodo, COUNT(1) FROM t_consulta c, t_transaccion t WHERE c.idTransaccion=t.idTransaccion AND (c.time BETWEEN ? AND ?) AND c.resultado='1' ";
		String sqlRESULTADO="SELECT  c.resultado, COUNT(1) FROM t_consulta c, t_transaccion t WHERE c.idTransaccion=t.idTransaccion AND (c.time BETWEEN ? AND ?)  ";

		String sqlTecnologia = "";
		if(tecnologia.equals("3G"))
			sqlTecnologia="AND c.tecnologia LIKE '3G' ";
		else if(tecnologia.equals("2G"))
			sqlTecnologia="AND (c.tecnologia LIKE '2G' OR c.tecnologia LIKE 'MIG') ";
		
		String sqlOrigen="";
		if(origen.equals("0")){
			sqlOrigen= "AND (t.tipo LIKE '1' OR t.tipo LIKE '0') ";
		}else if(origen.equals("2") || origen.equals("3")){
			sqlOrigen = String.format("AND t.tipo LIKE '%s' ",origen);
		}
		
		String sqlGROUP="group by ";
		
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ReporteTransaccionDTO reporte = new ReporteTransaccionDTO();
		int hour = Integer.parseInt(hora);
		int proxhora = hour+1;
		String fecha = date+" "+hour+":00";
		String fechaSgte = date+" "+proxhora+":00";
		try {
			conexion = DataSourceBD.openConnection();

				pstm = conexion.prepareStatement(sqlMETODO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.metodo");
				pstm.setString(1, fecha);
				pstm.setString(2, fechaSgte);
				rs = pstm.executeQuery();
				while(rs.next()){
					String metodo = rs.getString(1);
					int value = rs.getInt(2);

					if(metodo.equals("AGPS"))				reporte.setAgps(value);
					else if(metodo.equals("TRIANGULACION"))	reporte.setTriangulacion(value);
					else if(metodo.equals("CELDA"))			reporte.setCelda(value);

				}
				
				pstm = conexion.prepareStatement(sqlRESULTADO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.resultado");
				pstm.setString(1, fecha);
				pstm.setString(2, fechaSgte);
				rs = pstm.executeQuery();
				while(rs.next()){
					String resultado = rs.getString(1);
					int value = rs.getInt(2);
					
					if(resultado.equals("1"))			reporte.setOk(value);
					else if(resultado.equals("0"))		reporte.setError(value);
				}
			
			
		} catch (SQLException e) {		
			reporte=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return reporte;
	}

	@Override
	public List<ReporteTransaccionDTO> transaccionesPorDia(String tecnologia, String dia, String origen) {
		String sqlMETODO="SELECT  c.metodo, COUNT(1) FROM t_consulta c, t_transaccion t WHERE c.idTransaccion=t.idTransaccion AND (c.time BETWEEN convert(datetime,?,120) AND convert(datetime,?,120)) AND c.resultado='1' ";
		String sqlRESULTADO="SELECT  c.resultado, COUNT(1) FROM t_consulta c, t_transaccion t WHERE c.idTransaccion=t.idTransaccion AND (c.time BETWEEN convert(datetime,?,120) AND convert(datetime,?,120))  ";

		String sqlTecnologia = "";
		if(tecnologia.equals("3G"))
			sqlTecnologia="AND tecnologia LIKE '3G' ";
		else if(tecnologia.equals("2G"))
			sqlTecnologia="AND (tecnologia LIKE '2G' OR tecnologia LIKE 'MIG') ";
		
		String sqlOrigen="";
		if(origen.equals("0")){
			sqlOrigen= "AND (t.tipo LIKE '1' OR t.tipo LIKE '0') ";
		}else if(origen.equals("2") || origen.equals("3")){
			sqlOrigen = String.format("AND t.origen LIKE '%s' ",origen);
		}
		
		String sqlGROUP="group by ";
		List<ReporteTransaccionDTO> reportes= new ArrayList<ReporteTransaccionDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		int hour = 0;
		try {
			conexion = DataSourceBD.openConnection();
				
			for(int i = 0; i<=23; i++){
				String fecha = dia+" "+hour+":00"; 
				String fechaSgte = dia+" "+hour+":59";

				
				ReporteTransaccionDTO reporte = new ReporteTransaccionDTO();
				String h = hour+"";
				if(h.length()==1) h="0"+h;
				reporte.setIdentificador(h+":00");
				
				pstm = conexion.prepareStatement(sqlMETODO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.metodo");
				pstm.setString(1, fecha);
				pstm.setString(2, fechaSgte);
				rs = pstm.executeQuery();
				while(rs.next()){
					String metodo = rs.getString(1);
					int value = rs.getInt(2);

					if(metodo.equals("AGPS"))				reporte.setAgps(value);
					else if(metodo.equals("TRIANGULACION"))	reporte.setTriangulacion(value);
					else if(metodo.equals("CELDA"))			reporte.setCelda(value);
					reporte.setParam(hour+"");

				}
				
				pstm = conexion.prepareStatement(sqlRESULTADO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.resultado");
				pstm.setString(1, fecha);
				pstm.setString(2, fechaSgte);
				rs = pstm.executeQuery();
				while(rs.next()){
					String resultado = rs.getString(1);
					int value = rs.getInt(2);
					
					if(resultado.equals("1"))			reporte.setOk(value);
					else if(resultado.equals("0"))		reporte.setError(value);
				}
				hour++;
				reportes.add(reporte);
			}
				
			
			
		} catch (SQLException e) {		
			reportes=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return reportes;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReporteTransaccionDTO> transaccionesPorMes(String tecnologia, String mes, String origen) {
		String sqlMETODO="SELECT day(c.time) as dia,  c.metodo, COUNT(1) FROM t_consulta c, t_transaccion t WHERE c.idTransaccion=t.idTransaccion AND year(c.time) = year(getdate()) AND month(c.time)=? AND c.resultado='1' ";
		String sqlRESULTADO="SELECT day(c.time) as dia,  c.resultado, COUNT(1) FROM t_consulta c, t_transaccion t WHERE c.idTransaccion=t.idTransaccion AND year(c.time) = year(getdate()) AND month(c.time)=? ";
		String sqlTecnologia = "";
		if(tecnologia.equals("3G"))
			sqlTecnologia="AND c.tecnologia LIKE '3G' ";
		else if(tecnologia.equals("2G"))
			sqlTecnologia="AND (c.tecnologia LIKE '2G' OR c.tecnologia LIKE 'MIG') ";
		
		String sqlOrigen="";
		
		if(origen.equals("0")){
			sqlOrigen= "AND (t.tipo LIKE '1' OR t.tipo LIKE '0') ";
		}else if(origen.equals("2") || origen.equals("3")){
			sqlOrigen = String.format("AND t.tipo LIKE '%s' ",origen);
		}
		
		String sqlGROUP="group by day(c.time), ";
		HashMap<String, ReporteTransaccionDTO> transacciones = new HashMap<String, ReporteTransaccionDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			
			pstm = conexion.prepareStatement(sqlMETODO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.metodo");
			pstm.setString(1, mes);
			rs = pstm.executeQuery();
			while(rs.next()){
				String id = rs.getString(1); //El mes
				String metodo = rs.getString(2);
				int value = rs.getInt(3);
				ReporteTransaccionDTO r = transacciones.get(id);
				if(r==null){
					r = new ReporteTransaccionDTO();
				}
				if(metodo.equals("AGPS"))				r.setAgps(value);
				else if(metodo.equals("TRIANGULACION"))	r.setTriangulacion(value);
				else if(metodo.equals("CELDA"))			r.setCelda(value);
				transacciones.put(id, r);
			}
			
			pstm = conexion.prepareStatement(sqlRESULTADO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.resultado");
			pstm.setString(1, mes);
			rs = pstm.executeQuery();
			while(rs.next()){
				String id = rs.getString(1); //El mes
				String resultado = rs.getString(2);
				int value = rs.getInt(3);
				ReporteTransaccionDTO r = transacciones.get(id);
				if(r==null){
					r = new ReporteTransaccionDTO();
				}
				if(resultado.equals("1"))			r.setOk(value);
				else if(resultado.equals("0"))		r.setError(value);
				transacciones.put(id, r);
			}
			
			
		} catch (SQLException e) {		
			transacciones.clear();
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		
		List<ReporteTransaccionDTO> reportes = new ArrayList<ReporteTransaccionDTO>();
		
		
		Iterator i = transacciones.entrySet().iterator();
		Map<String, ReporteTransaccionDTO> tx = new HashMap<String, ReporteTransaccionDTO>();
		
		while(i.hasNext()){
			Map.Entry e =  (Map.Entry)i.next();
			String k = e.getKey().toString();
			if(k.length()==1) k="0"+k;
			tx.put(k, (ReporteTransaccionDTO)e.getValue());
		}
		
		Map<String, ReporteTransaccionDTO>  t = new TreeMap<String, ReporteTransaccionDTO>(tx);
		Iterator it = t.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			ReporteTransaccionDTO reporte = (ReporteTransaccionDTO) e.getValue();
			reporte.setIdentificador(e.getKey().toString());
			reportes.add(reporte);
		}
		
		return reportes;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReporteTransaccionDTO> transaccionesPorAnio(String tecnologia, String anio, String origen) {
		String sqlMETODO="SELECT month(c.time),  c.metodo, COUNT(1) as cantidad FROM t_consulta c, t_transaccion t WHERE c.idTransaccion=t.idTransaccion AND year(c.time) = ?  AND c.resultado= '1' ";
		String sqlRESULTADO="SELECT month(c.time) as mes,  c.resultado, COUNT(1) FROM t_consulta c, t_transaccion t WHERE c.idTransaccion=t.idTransaccion AND year(c.time) = ? ";
		String sqlTecnologia = "";
		if(tecnologia.equals("3G"))
			sqlTecnologia="AND c.tecnologia LIKE '3G' ";
		else if(tecnologia.equals("2G"))
			sqlTecnologia="AND (c.tecnologia LIKE '2G' OR c.tecnologia LIKE 'MIG') ";
		
		String sqlOrigen="";
		if(origen.equals("0")){
			sqlOrigen= "AND (t.tipo LIKE '1' OR t.tipo LIKE '0') ";
		}else if(origen.equals("2") || origen.equals("3")){
			sqlOrigen = String.format("AND t.tipo LIKE '%s' ",origen);
		}
		
		String sqlGROUP="group by month(c.time), ";
		
		HashMap<String, ReporteTransaccionDTO> transacciones = new HashMap<String, ReporteTransaccionDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			
			pstm = conexion.prepareStatement(sqlMETODO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.metodo");
			pstm.setString(1, anio);
			rs = pstm.executeQuery();
			while(rs.next()){
				String id = rs.getString(1); //El mes
				String metodo = rs.getString(2);
				int value = rs.getInt(3);
				ReporteTransaccionDTO r = transacciones.get(id);
				if(r==null){
					r = new ReporteTransaccionDTO();
				}
				if(metodo.equals("AGPS"))				r.setAgps(value);
				else if(metodo.equals("TRIANGULACION"))	r.setTriangulacion(value);
				else if(metodo.equals("CELDA"))			r.setCelda(value);
				transacciones.put(id, r);
			}
			
			pstm = conexion.prepareStatement(sqlRESULTADO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.resultado");
			pstm.setString(1, anio);
			rs = pstm.executeQuery();
			while(rs.next()){
				String id = rs.getString(1); //El mes
				String resultado = rs.getString(2);
				int value = rs.getInt(3);
				ReporteTransaccionDTO r = transacciones.get(id);
				if(r==null){
					r = new ReporteTransaccionDTO();
				}
				if(resultado.equals("1"))			r.setOk(value);
				else if(resultado.equals("0"))		r.setError(value);
				transacciones.put(id, r);
			}
					
			
		} catch (SQLException e) {		
			transacciones.clear();
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		
		List<ReporteTransaccionDTO> reportes = new ArrayList<ReporteTransaccionDTO>();
		
		Iterator i = transacciones.entrySet().iterator();
		Map<String, ReporteTransaccionDTO> tx = new HashMap<String, ReporteTransaccionDTO>();
		
		while(i.hasNext()){
			Map.Entry e =  (Map.Entry)i.next();
			String k = e.getKey().toString();
			if(k.length()==1) k="0"+k;
			tx.put(k, (ReporteTransaccionDTO)e.getValue());
		}
		
		Map<String, ReporteTransaccionDTO>  t = new TreeMap<String, ReporteTransaccionDTO>(tx);
		Iterator it = t.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			ReporteTransaccionDTO reporte = (ReporteTransaccionDTO) e.getValue();
			reporte.setIdentificador(e.getKey().toString());
			reportes.add(reporte);
		}
		
		return reportes;
	}

	@Override
	public List<ReporteTransaccionDTO> transaccionesPorMinutos(String tecnologia, String dia, String hora, String origen) {
		String sqlMETODO="SELECT  c.metodo, COUNT(1) FROM t_consulta c, t_transaccion t WHERE c.idTransaccion=t.idTransaccion AND (c.time BETWEEN convert(datetime,?,120) AND convert(datetime,?,120)) AND c.resultado='1' ";
		String sqlRESULTADO="SELECT  c.resultado, COUNT(1) FROM t_consulta c, t_transaccion t WHERE c.idTransaccion=t.idTransaccion AND (c.time BETWEEN convert(datetime,?,120) AND convert(datetime,?,120))  ";

		String sqlTecnologia = "";
		if(tecnologia.equals("3G"))
			sqlTecnologia="AND tecnologia LIKE '3G' ";
		else if(tecnologia.equals("2G"))
			sqlTecnologia="AND (tecnologia LIKE '2G' OR tecnologia LIKE 'MIG') ";
		
		String sqlOrigen="";
		if(origen.equals("0")){
			sqlOrigen= "AND (t.tipo LIKE '1' OR t.tipo LIKE '0') ";
		}else if(origen.equals("2") || origen.equals("3")){
			sqlOrigen = String.format("AND t.origen LIKE '%s' ",origen);
		}
		
		String sqlGROUP="group by ";
		List<ReporteTransaccionDTO> reportes= new ArrayList<ReporteTransaccionDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		int minuto = 0;
		try {
			conexion = DataSourceBD.openConnection();
				
			for(int i = 0; i<=59; i++){
				String fecha = dia+" "+hora+":"+minuto+":00"; 
				String fechaSgte = dia+" "+hora+":"+minuto+":59";

				
				ReporteTransaccionDTO reporte = new ReporteTransaccionDTO();
				String m = minuto+"";
				if(m.length()==1) m="0"+m;
				String h = hora+"";
				if(h.length()==1) h="0"+h;
				reporte.setIdentificador(h+":"+m+":00");
				
				pstm = conexion.prepareStatement(sqlMETODO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.metodo");
				pstm.setString(1, fecha);
				pstm.setString(2, fechaSgte);
				rs = pstm.executeQuery();
				while(rs.next()){
					String metodo = rs.getString(1);
					int value = rs.getInt(2);

					if(metodo.equals("AGPS"))				reporte.setAgps(value);
					else if(metodo.equals("TRIANGULACION"))	reporte.setTriangulacion(value);
					else if(metodo.equals("CELDA"))			reporte.setCelda(value);
					reporte.setParam(hora+":"+m);

				}
				
				pstm = conexion.prepareStatement(sqlRESULTADO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.resultado");
				pstm.setString(1, fecha);
				pstm.setString(2, fechaSgte);
				rs = pstm.executeQuery();
				while(rs.next()){
					String resultado = rs.getString(1);
					int value = rs.getInt(2);
					
					if(resultado.equals("1"))			reporte.setOk(value);
					else if(resultado.equals("0"))		reporte.setError(value);
				}
				minuto++;
				reportes.add(reporte);
			}
				
			
			
		} catch (SQLException e) {		
			reportes=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return reportes;
	}

	@Override
	public List<ReporteTransaccionDTO> transaccionesPorSegundos(String tecnologia, String dia, String hora, String origen) {
		String sqlMETODO="SELECT  c.metodo, COUNT(1) FROM t_consulta c, t_transaccion t WHERE c.idTransaccion=t.idTransaccion AND (c.time BETWEEN convert(datetime,?,120) AND convert(datetime,?,120)) AND c.resultado='1' ";
		String sqlRESULTADO="SELECT  c.resultado, COUNT(1) FROM t_consulta c, t_transaccion t WHERE c.idTransaccion=t.idTransaccion AND (c.time BETWEEN convert(datetime,?,120) AND convert(datetime,?,120))  ";

		String sqlTecnologia = "";
		if(tecnologia.equals("3G"))
			sqlTecnologia="AND tecnologia LIKE '3G' ";
		else if(tecnologia.equals("2G"))
			sqlTecnologia="AND (tecnologia LIKE '2G' OR tecnologia LIKE 'MIG') ";
		
		String sqlOrigen="";
		if(origen.equals("0")){
			sqlOrigen= "AND (t.tipo LIKE '1' OR t.tipo LIKE '0') ";
		}else if(origen.equals("2") || origen.equals("3")){
			sqlOrigen = String.format("AND t.origen LIKE '%s' ",origen);
		}
		
		String sqlGROUP="group by ";
		List<ReporteTransaccionDTO> reportes= new ArrayList<ReporteTransaccionDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		int segundos = 0;
		try {
			conexion = DataSourceBD.openConnection();
				
			for(int i = 0; i<=59; i++){
				String fecha = dia+" "+hora+":"+segundos; 
				String fechaSgte = dia+" "+hora+":"+segundos;

				
				ReporteTransaccionDTO reporte = new ReporteTransaccionDTO();
				String s = segundos+"";
				if(s.length()==1) s="0"+s;
				String horasplit [] = hora.split(":");
				String h = horasplit[0]+"";
				if(h.length()==1) h="0"+h;
				String m = horasplit[1]+"";
				if(m.length()==1) m="0"+m;
				reporte.setIdentificador(h+":"+m+":"+s);
				
				pstm = conexion.prepareStatement(sqlMETODO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.metodo");
				pstm.setString(1, fecha);
				pstm.setString(2, fechaSgte);
				rs = pstm.executeQuery();
				while(rs.next()){
					String metodo = rs.getString(1);
					int value = rs.getInt(2);

					if(metodo.equals("AGPS"))				reporte.setAgps(value);
					else if(metodo.equals("TRIANGULACION"))	reporte.setTriangulacion(value);
					else if(metodo.equals("CELDA"))			reporte.setCelda(value);

				}
				
				pstm = conexion.prepareStatement(sqlRESULTADO+sqlTecnologia+sqlOrigen+sqlGROUP+"c.resultado");
				pstm.setString(1, fecha);
				pstm.setString(2, fechaSgte);
				rs = pstm.executeQuery();
				while(rs.next()){
					String resultado = rs.getString(1);
					int value = rs.getInt(2);
					
					if(resultado.equals("1"))			reporte.setOk(value);
					else if(resultado.equals("0"))		reporte.setError(value);
				}
				segundos++;
				reportes.add(reporte);
			}
				
			
			
		} catch (SQLException e) {		
			reportes=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return reportes;
	}


}
