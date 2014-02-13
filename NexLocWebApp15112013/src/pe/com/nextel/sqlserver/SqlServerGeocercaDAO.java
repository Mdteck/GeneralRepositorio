package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pe.com.nextel.bean.DireccionDTO;
import pe.com.nextel.bean.GeocercaDTO;
import pe.com.nextel.bean.HorarioDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.iface.GeocercaDAO;
import pe.com.nextel.util.DataSourceBD;

/**
 * @author Devos Inc.
 * 
 * Clase que accede y maneja los datos del motos SqlServer para las funcionalidades de la intefaz GeocercaDAO.
 * 
 */

public class SqlServerGeocercaDAO implements GeocercaDAO{


	@Override
	/**
	 * Se registrará la información de la geocerca y sus coordenadas.
	 * @param geocerca : Geocerca a ser registrada
	 * @return int : devuelve el id con el que se registró la geocerca, si no se registró devolverá -1
	 * 
	 */
	public int registrar(GeocercaDTO geocerca) {
		
		String sqlGeocerca="INSERT INTO t_geocerca (nombre, mailNotificacion, idUsuario, observacion, rings, fechaInicio, fechaFin, horaInicio, horaFin, fechaRegistro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
		Connection conexion = null;
		PreparedStatement pstm = null;
		int idGeocerca = 0;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sqlGeocerca,  Statement.RETURN_GENERATED_KEYS);
			pstm.setString(1, geocerca.getNombre());
			pstm.setString(2, geocerca.getEmailNotificacion());
			pstm.setString(3, geocerca.getIdUsuario()); 
			pstm.setString(4, geocerca.getObservacion());
			pstm.setString(5, geocerca.getRings());
			pstm.setString(6, geocerca.getHorario().getFechaInicio());
			pstm.setString(7, geocerca.getHorario().getFechaFin());
			pstm.setString(8, geocerca.getHorario().getHoraInicio());
			pstm.setString(9, geocerca.getHorario().getHoraFin());			
			pstm.executeUpdate();
			ResultSet llaves = pstm.getGeneratedKeys();
			while (llaves.next()) {
				idGeocerca = llaves.getInt(1);
			}
			llaves.close();
		} catch (SQLException e){
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return idGeocerca;
	}

	@Override
	public boolean modificar(GeocercaDTO geocerca) {
		String sql = "UPDATE t_geocerca SET nombre = ?, mailNotificacion = ?, observacion = ?, rings = ?," +
				" fechaInicio = ?, fechaFin = ?, horaInicio = ?, horaFin = ? " +
				"WHERE idGeocerca = ?";
		boolean estado = false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setString(1, geocerca.getNombre());
			pstm.setString(2, geocerca.getEmailNotificacion());
			pstm.setString(3, geocerca.getObservacion());
			pstm.setString(4, geocerca.getRings());
			pstm.setString(5, geocerca.getHorario().getFechaInicio());
			pstm.setString(6, geocerca.getHorario().getFechaFin());
			pstm.setString(7, geocerca.getHorario().getHoraInicio());
			pstm.setString(8, geocerca.getHorario().getHoraFin());
			pstm.setString(9, geocerca.getIdGeocerca());
			
			int filas = pstm.executeUpdate();
			if (filas != 0) estado = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DataSourceBD.closeConnection(conexion);
		}
		
		return estado;
	}

	@Override
	/**
	 * Método que obtiene una lista de Geocercas de la BD.
	 * @return List<GeocercaDTO> geocercas: Develve la lista de geocercas de la BD. Si ocurre un error, devuelve null. Si no existe ninguna geocerca, devolverá la lista con tamaño = 0
	 */
	public List<GeocercaDTO> listar(int idUsuario, int tipo) {
		List<GeocercaDTO> geocercas;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String sql="SELECT g.idGeocerca, g.nombre, (SELECT COUNT(1) FROM t_usuarioxgeocerca gxu WHERE gxu.idGeocerca = g.idGeocerca), g.fechaRegistro, g.fechaInicio, g.fechaFin, g.horaInicio, g.horaFin, g.estado  FROM t_geocerca g WHERE (g.estado='1' OR g.estado='2') AND g.idUsuario = ?";
		
		

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setInt(1, idUsuario);
			rs = pstm.executeQuery();
			geocercas=new ArrayList<GeocercaDTO>();
			while (rs.next()) {
				GeocercaDTO geocerca=new GeocercaDTO();
				geocerca.setIdGeocerca(rs.getString(1));
				geocerca.setNombre(rs.getString(2));
				geocerca.setAsignados(rs.getString(3));
				geocerca.setFechaRegistro(rs.getString(4));
				HorarioDTO h = new HorarioDTO();
				h.setFechaInicio(rs.getString(5));
				h.setFechaFin(rs.getString(6));
				h.setHoraInicio(rs.getString(7));
				h.setHoraFin(rs.getString(8));
				geocerca.setHorario(h);
				geocerca.setEstado(rs.getInt(9));
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
	/**
	 * Dada una lista de usuarios, se registrarán/actualizarán con el idGeocerca respectivo.
	 * @param idGeocerca : idGeocerca para la que se registrarán/actualizarán usuarios
	 * @param usuarios: Lista de usuarios asignados para esa geocerca
	 * @return boolean : true si se actualizan todos los usuarios-geocerca correctamente, 
	 * 
	 */
	public boolean asignarGeocerca(int idGeocerca, List<UsuarioDTO> usuarios) {
		String sqlSELECT = "SELECT idUsuario FROM t_usuarioxgeocerca WHERE idGeocerca=?";
		String sqlDELETE="DELETE FROM t_usuarioxgeocerca WHERE idUsuario = ? AND idGeocerca = ?";
		String sqlINSERT="INSERT INTO t_usuarioxgeocerca (idUsuario, idGeocerca) VALUES (?, ?)";
		
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs=null;
		//Lista que se llenará con los usuarios obtenidos de la BD para idGeocerca
		List<String> usuariosAsignadosBD=new ArrayList<String>();
		try {
			conexion = DataSourceBD.openConnection();
			conexion.setAutoCommit(false);
			pstm = conexion.prepareStatement(sqlSELECT);
			pstm.setInt(1 , idGeocerca);
			rs = pstm.executeQuery();
			//Se guardan todos los idUsuario registrados para la geocerca en la BD en una lista
			while(rs.next()) usuariosAsignadosBD.add(rs.getString(1)); 
			//Se compara con los idUsuario recibidos como parámetro
			for(UsuarioDTO usuario : usuarios){
				if(usuariosAsignadosBD.contains(usuario.getIdUsuario())){
					//Si el usuario enviado desde la interfaz ya existe en la BD, no se hace nada y se elimina de la lista.
					usuariosAsignadosBD.remove(usuario.getIdUsuario());
					estado=true;
				}else{
					//Si no existe en la lista se hace un INSERT
					pstm=conexion.prepareStatement(sqlINSERT);
					pstm.setInt(1, Integer.parseInt(usuario.getIdUsuario()));
					pstm.setInt(2, idGeocerca);
					int i = pstm.executeUpdate();
					if (i!=0) 	estado= true;
				}
			}
			//Si en la lista llenada de la BD aún existen registros, se eliminarán.
			//Se supone que si aún están en la lista, es porque se eliminó el o los usuarios en la interfaz web
			if(usuariosAsignadosBD.size()!=0){
				for(String idUsuarioEliminado : usuariosAsignadosBD){
					pstm=conexion.prepareStatement(sqlDELETE);
					pstm.setInt(1, Integer.parseInt(idUsuarioEliminado));
					pstm.setInt(2, idGeocerca);
					pstm.executeUpdate();
					estado=true;
				}
				
			}
			//Si todos los registros se actualizaron correctamente, se commitea, sino, se ejecuta un rollback.
			if (estado)	conexion.commit();
			else		conexion.rollback();
			conexion.setAutoCommit(true);
		} catch (SQLException e) {	
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return estado;
	}


	@Override
	public GeocercaDTO obtener(int idGeocerca) {
		String sqlGEOCERCA="SELECT idGeocerca, nombre, observacion, mailNotificacion, idUsuario, rings, fechaInicio, fechaFin, horaInicio, horaFin FROM t_geocerca WHERE idGeocerca = ? ";
		String sqlUSUARIOS = "SELECT idUsuario FROM t_usuarioxgeocerca WHERE idGeocerca = ?";
		GeocercaDTO geocerca=new GeocercaDTO();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sqlGEOCERCA);
			pstm.setInt(1, idGeocerca);
			rs = pstm.executeQuery();
			while (rs.next()) {
				geocerca.setIdGeocerca(rs.getString(1));
				geocerca.setNombre(rs.getString(2));
				geocerca.setObservacion(rs.getString(3));
				geocerca.setEmailNotificacion(rs.getString(4));
				geocerca.setIdUsuario(rs.getString(5));
				geocerca.setRings(rs.getString(6));
				HorarioDTO horario = new HorarioDTO();
				horario.setFechaInicio(rs.getString(7));
				horario.setFechaFin(rs.getString(8));
				horario.setHoraInicio(rs.getString(9));
				horario.setHoraFin(rs.getString(10));
				geocerca.setHorario(horario);
			}
			
			//Se asigna los usuarios
			pstm = conexion.prepareStatement(sqlUSUARIOS);
			pstm.setInt(1, idGeocerca);
			rs=pstm.executeQuery();
			List<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
			while(rs.next()){
				UsuarioDTO usuario = new UsuarioDTO();
				usuario.setIdUsuario(rs.getString(1));
				usuarios.add(usuario);
			}
			geocerca.setUsuarios(usuarios);
		} catch (SQLException e) {		
			geocerca=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return geocerca;
	}


	@Override
	public boolean eliminar(int idGeocerca) {
		String sqlGeocerca="UPDATE t_geocerca SET estado='0' WHERE idGeocerca = ?";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sqlGeocerca);
			pstm.setInt(1, idGeocerca);		
			int i = pstm.executeUpdate();
			if(i!=0) estado=true;
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return estado;
	}


	@Override
	public List<PosicionDTO> detalle(int idGeocerca) {
		String sql="SELECT u.numero, u.etiqueta, c.time, c.metodo, e.estado, c.direccion, c.distrito FROM t_usuario u, t_consulta c, t_cambioEstado e WHERE  u.idUsuario=u.idUsuario AND c.idConsulta = e.idConsulta AND e.idUsuario = u.idUsuario AND e.idGeocerca = ?";
		
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<PosicionDTO> posiciones = new ArrayList<PosicionDTO>();

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setInt(1, idGeocerca);
			rs = pstm.executeQuery();
			while (rs.next()) {
				PosicionDTO posicion=new PosicionDTO();
				UsuarioDTO usuario = new UsuarioDTO();
				usuario.setNumero(rs.getString(1));
				usuario.setEtiqueta(rs.getString(2));
				posicion.setUsuario(usuario);
				posicion.setTime(rs.getString(3));
				posicion.setMetodo(rs.getString(4));
				posicion.setEstado(rs.getString(5));
				DireccionDTO direccion=new DireccionDTO();
				direccion.setDireccion(rs.getString(6));
				direccion.setDistrito(rs.getString(7));
				posicion.setDireccion(direccion);
				posiciones.add(posicion);
			}
		} catch (SQLException e) {		
			posiciones=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return posiciones;
	}


	

}
