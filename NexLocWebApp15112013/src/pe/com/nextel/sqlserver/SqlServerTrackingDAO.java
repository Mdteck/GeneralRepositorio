package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pe.com.nextel.bean.CoordenadaDTO;
import pe.com.nextel.bean.DireccionDTO;
import pe.com.nextel.bean.HorarioDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.TrackingDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.iface.TrackingDAO;
import pe.com.nextel.util.DataSourceBD;

public class SqlServerTrackingDAO implements TrackingDAO {


	@Override
	public int registrar(TrackingDTO tracking){
		String sql="INSERT INTO t_tracking (idMonitor, idMonitoreado, fechaInicio, fechaFin, horaInicio, horaFin) VALUES(?, ?, ?, ?, ?, ?)";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		int idTracking = 0;
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm.setInt(1, Integer.parseInt(tracking.getMonitor().getIdUsuario()));
			pstm.setInt(2, Integer.parseInt(tracking.getMonitoreado().getIdUsuario()));
			pstm.setString(3, tracking.getHorario().getFechaInicio());
			pstm.setString(4, tracking.getHorario().getFechaFin());
			pstm.setString(5, tracking.getHorario().getHoraInicio());
			pstm.setString(6, tracking.getHorario().getHoraFin());
			int i = pstm.executeUpdate();
			estado=i!=0?true:false;
			if(estado){
				ResultSet llaves = pstm.getGeneratedKeys();
				while (llaves.next()) {
					idTracking = llaves.getInt(1);
				}
				llaves.close();
			}
		} catch (SQLException e) {
			estado=false;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return idTracking;
	}

	@Override
	public boolean cancelar(TrackingDTO tracking) {
		String sql="UPDATE t_tracking SET estado = '0' WHERE idTracking = ?";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, Integer.parseInt(tracking.getIdTracking()));
			int i = pstm.executeUpdate();
			estado=i!=0?true:false;
		} catch (SQLException e) {
			estado=false;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return estado;
	}

	@Override
	public TrackingDTO mapa(int idTracking) {
		String sql="SELECT u.etiqueta, u.numero, t.idTracking FROM t_usuario u, t_tracking t WHERE ts.idMonitor = u.idUsuario AND t.idTracking = ?";
		String sqlPosicion="SELECT idPosicion, timestamp, longitud, latitud FROM t_posicion WHERE idTracking = ?";
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		TrackingDTO tracking = new TrackingDTO();
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idTracking);
			rs = pstm.executeQuery();
			if(rs.next()){
				UsuarioDTO usuario = new UsuarioDTO();
				usuario.setEtiqueta(rs.getString(1));
				usuario.setNumero(rs.getString(2));
				tracking.setMonitoreado(usuario);
				tracking.setIdTracking(rs.getString(4));
			}
			pstm=conexion.prepareStatement(sqlPosicion);
			pstm.setInt(1, idTracking);
			rs=pstm.executeQuery();
			List<PosicionDTO> posiciones = new ArrayList<PosicionDTO>();
			while(rs.next()){
				PosicionDTO posicion = new PosicionDTO();
				posicion.setIdPosicion(rs.getString(1));
				posicion.setTime(rs.getString(2));
				CoordenadaDTO coordenada = new CoordenadaDTO();
				coordenada.setLongitud(rs.getString(3));
				coordenada.setLatitud(rs.getString(4));
				posicion.setCoordenada(coordenada);
				posiciones.add(posicion);
			}
			tracking.setPosiciones(posiciones);
		} catch (SQLException e) {
			tracking=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return tracking;
	}

	@Override
	public List<TrackingDTO> listar(int idMonitor) {
		String sql="SELECT u.etiqueta, u.numero, u.idUsuario, t.idTracking, t.fechaInicio, t.fechaFin, t.horaInicio, t.horaFin, t.estado FROM t_usuario u, t_tracking t WHERE t.idMonitoreado = u.idUsuario AND t.idMonitor = ? AND (t.estado = '1' OR t.estado = '2')";
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<TrackingDTO> trackings = new ArrayList<TrackingDTO>();
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idMonitor);
			rs = pstm.executeQuery();
			while(rs.next()){
				TrackingDTO tracking = new TrackingDTO();
				UsuarioDTO usuario = new UsuarioDTO();
				usuario.setEtiqueta(rs.getString(1));
				usuario.setNumero(rs.getString(2));
				usuario.setIdUsuario(rs.getString(3));
				tracking.setMonitoreado(usuario);
				tracking.setIdTracking(rs.getString(4));
				HorarioDTO h = new HorarioDTO();
				h.setFechaInicio(rs.getString(5));
				h.setFechaFin(rs.getString(6));
				h.setHoraInicio(rs.getString(7));
				h.setHoraFin(rs.getString(8));
				tracking.setHorario(h);
				tracking.setEstado(rs.getInt(9));
				trackings.add(tracking);
			}
			
		} catch (SQLException e) {
			trackings=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return trackings;
	}
	
	
	@Override
	public List<TrackingDTO> historial(int idMonitor) {
		String sql="SELECT u.etiqueta, u.numero, u.idUsuario, t.idTracking, t.fechaInicio, t.horaInicio,  t.fechaFin, t.horaFin, t.estado FROM t_usuario u, t_tracking t WHERE t.idMonitoreado = u.idUsuario AND t.idMonitor = ? AND t.estado in ('2', '0', '3')";
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<TrackingDTO> trackings = new ArrayList<TrackingDTO>();
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idMonitor);
			rs = pstm.executeQuery();
			while(rs.next()){
				TrackingDTO tracking = new TrackingDTO();
				UsuarioDTO usuario = new UsuarioDTO();
				usuario.setEtiqueta(rs.getString(1));
				usuario.setNumero(rs.getString(2));
				usuario.setIdUsuario(rs.getString(3));
				tracking.setMonitoreado(usuario);
				tracking.setIdTracking(rs.getString(4));
				HorarioDTO h = new HorarioDTO();
				h.setFechaInicio(rs.getString(5));
				h.setHoraInicio(rs.getString(6));
				h.setFechaFin(rs.getString(7));
				h.setHoraFin(rs.getString(8));
				tracking.setHorario(h);
				tracking.setEstado(rs.getInt(9));
				trackings.add(tracking);
			}
			
		} catch (SQLException e) {
			trackings=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return trackings;
	}

	@Override
	public TrackingDTO obtenerFiltros(int idTracking, String fechaInicial, String fechaFinal, String horaInicio, String horaFin, String modo){
		String sql="SELECT u.etiqueta, u.numero, u.idUsuario, p.idTracking," +
				" c.time, c.longitud, c.latitud, c.metodo , c.direccion, c.distrito,c.msid " +
				"FROM t_usuario u, t_posicion p, t_consulta c, t_tracking t " +
				"WHERE t.idMonitoreado = u.idUsuario AND p.idTracking = t.idTracking" +
				" AND p.idConsulta = c.idConsulta AND p.idTracking = ? AND c.resultado = '1' " ;				
		if(fechaInicial!=""){
			sql+=" AND c.time>=convert(datetime,'"+fechaInicial+"',103)";
		}
		if(fechaFinal!=""){
			sql+=" AND c.time<=convert(datetime,'"+fechaFinal+"',103)";
		}
		if(!modo.equalsIgnoreCase("TODOS")){
			sql+=" AND c.metodo='"+modo+"'";
		}
		sql+=" ORDER BY c.time ASC";
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		TrackingDTO tracking = new TrackingDTO();
		List<PosicionDTO> posiciones=new ArrayList<PosicionDTO>();
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idTracking);
			rs = pstm.executeQuery();
			while(rs.next()){
				UsuarioDTO usuario = new UsuarioDTO();
				usuario.setEtiqueta(rs.getString(1));
				usuario.setNumero(rs.getString(2));
				usuario.setIdUsuario(rs.getString(3));
				tracking.setMonitoreado(usuario);
				tracking.setIdTracking(rs.getString(4));
				PosicionDTO posicion = new PosicionDTO();
				posicion.setTime(rs.getString(5));
				
				/*Inicio 28/06/2013 MDTECK Sandy Huanco*/
				DireccionDTO direccion = new DireccionDTO();
				direccion.setDireccion(rs.getString(9));
				direccion.setDistrito(rs.getString(10));
				posicion.setDireccion(direccion);
				
				UsuarioDTO moni = new UsuarioDTO();
				moni.setNumero(rs.getString(11));
				/*Fin*/
				
				CoordenadaDTO coordenada = new CoordenadaDTO();
				coordenada.setLongitud(rs.getString(6));
				coordenada.setLatitud(rs.getString(7));
				posicion.setCoordenada(coordenada);
				posicion.setMetodo(rs.getString(8));
				posicion.setMonitor(moni);
				posicion.setUsuario(usuario);
				posiciones.add(posicion);
			}
			tracking.setPosiciones(posiciones);
		} catch (SQLException e) {
			tracking=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return tracking;
	}

	@Override
	public TrackingDTO obtener(int idTracking) {
		String sql="SELECT u.etiqueta, u.numero, u.idUsuario, p.idTracking," +
				" c.time, c.longitud, c.latitud, c.metodo , c.direccion " +
				"FROM t_usuario u, t_posicion p, t_consulta c, t_tracking t " +
				"WHERE t.idMonitoreado = u.idUsuario AND p.idTracking = t.idTracking" +
				" AND p.idConsulta = c.idConsulta AND p.idTracking = ? AND c.resultado = '1' " +
				"ORDER BY c.time ASC";
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		TrackingDTO tracking = new TrackingDTO();
		List<PosicionDTO> posiciones=new ArrayList<PosicionDTO>();
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idTracking);
			rs = pstm.executeQuery();
			while(rs.next()){
				UsuarioDTO usuario = new UsuarioDTO();
				usuario.setEtiqueta(rs.getString(1));
				usuario.setNumero(rs.getString(2));
				usuario.setIdUsuario(rs.getString(3));
				tracking.setMonitoreado(usuario);
				tracking.setIdTracking(rs.getString(4));
				PosicionDTO posicion = new PosicionDTO();
				posicion.setTime(rs.getString(5));
				
				DireccionDTO direccion = new DireccionDTO();
				direccion.setDireccion(rs.getString(9));
				posicion.setDireccion(direccion);
				
				CoordenadaDTO coordenada = new CoordenadaDTO();
				coordenada.setLongitud(rs.getString(6));
				coordenada.setLatitud(rs.getString(7));
				posicion.setCoordenada(coordenada);
				posicion.setMetodo(rs.getString(8));
				posiciones.add(posicion);
			}
			tracking.setPosiciones(posiciones);
		} catch (SQLException e) {
			tracking=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return tracking;
	}

	@Override
	public List<TrackingDTO> filtrar(int idUsuario, String modo, String fechaInicial, String fechaFin) {
		String condicionModo;
		String condicionFechaInicial="";
		String condicionFechaFin="";
		if(modo.equals("-1"))	condicionModo= " AND t.estado in('2', '0', '3') ";
		else					condicionModo= String.format(" AND (t.estado = '%s')",modo);	
		if(!fechaInicial.isEmpty())	condicionFechaInicial=String.format(" AND DATEDIFF(dd,convert(datetime,'%s',103),convert(datetime,t.fechaInicio,103))>=0",fechaInicial);
		if(!fechaFin.isEmpty())		condicionFechaFin=String.format(" AND DATEDIFF(dd,convert(datetime,'%s',103),convert(datetime,t.fechaFin,103))<=0", fechaFin);
			
		String sql=String.format("SELECT u.etiqueta, u.numero, u.idUsuario, t.idTracking, t.fechaInicio, t.fechaFin, t.horaInicio, t.horaFin, t.estado FROM t_usuario u, t_tracking t WHERE t.idMonitoreado = u.idUsuario AND t.idMonitor = ? %s %s %s ",condicionModo,condicionFechaInicial, condicionFechaFin);
		System.out.println(String.format("SQL : %s",sql));
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<TrackingDTO> trackings = new ArrayList<TrackingDTO>();
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idUsuario);
			rs = pstm.executeQuery();
			while(rs.next()){
				TrackingDTO tracking = new TrackingDTO();
				UsuarioDTO usuario = new UsuarioDTO();
				usuario.setEtiqueta(rs.getString(1));
				usuario.setNumero(rs.getString(2));
				usuario.setIdUsuario(rs.getString(3));
				tracking.setMonitoreado(usuario);
				tracking.setIdTracking(rs.getString(4));
				HorarioDTO h = new HorarioDTO();
				h.setFechaInicio(rs.getString(5));
				h.setFechaFin(rs.getString(6));
				h.setHoraInicio(rs.getString(7));
				h.setHoraFin(rs.getString(8));
				tracking.setHorario(h);
				tracking.setEstado(rs.getInt(9));
				trackings.add(tracking);
			}
			
		} catch (SQLException e) {
			trackings=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return trackings;
	}


}
