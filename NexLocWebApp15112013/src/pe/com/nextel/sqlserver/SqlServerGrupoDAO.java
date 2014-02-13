package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pe.com.nextel.bean.CoordenadaDTO;
import pe.com.nextel.bean.GrupoDTO;
import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.GrupoDAO;
import pe.com.nextel.dao.iface.LogDAO;
import pe.com.nextel.util.DataSourceBD;
import pe.com.nextel.util.ParserUtil;
import pe.com.nextel.util.PropertyUtil;

public class SqlServerGrupoDAO implements GrupoDAO {

	DAOFactory fabricaSqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	LogDAO sqlServerLogDAO = fabricaSqlServer.getLogDAO();
	
	@Override
	/**
	 * Método que lista los grupos en estado disponible
	 * @return lista de grupos. Si no existe ningún grupo disponible, devolverá la lista con tamaño 0, si ocurre algún problema devuelve null
	 */
	public List<GrupoDTO> listar(int idMonitor) {
		String sql="SELECT g.idGrupo, g.nombre, (SELECT COUNT(1) FROM t_usuarioxgrupo gxu, t_usuario u WHERE gxu.idGrupo = g.idGrupo AND gxu.idUsuario = u.idUsuario AND u.estado='1') FROM t_grupo g WHERE g.estado='1' AND g.idMonitor = ? ";
		List<GrupoDTO> grupos=null;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setInt(1, idMonitor);
			rs = pstm.executeQuery();
			grupos=new ArrayList<GrupoDTO>();
			while (rs.next()) {
				GrupoDTO grupo=new GrupoDTO();
				grupo.setIdGrupo(rs.getString(1));
				grupo.setNombre(rs.getString(2));
				grupo.setAsignados(rs.getString(3));
				grupos.add(grupo);
			}
		} catch (SQLException e) {		
			grupos=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return grupos;
	}

	@Override
	public List<GrupoDTO> listarPorCuenta(int idCuenta) {
		String sql="SELECT g.idGrupo, g.nombre," +
				" (SELECT COUNT(1) FROM t_usuarioxgrupo gxu WHERE gxu.idGrupo = g.idGrupo) " +
				"FROM t_grupo g WHERE g.estado='1' AND g.idMonitor IN (SELECT idUsuario FROM t_usuario WHERE idCuenta = ?) ";
		List<GrupoDTO> grupos=null;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setInt(1, idCuenta);
			rs = pstm.executeQuery();
			grupos=new ArrayList<GrupoDTO>();
			while (rs.next()) {
				GrupoDTO grupo=new GrupoDTO();
				grupo.setIdGrupo(rs.getString(1));
				grupo.setNombre(rs.getString(2));
				grupo.setAsignados(rs.getString(3));
				grupos.add(grupo);
			}
		} catch (SQLException e) {		
			grupos=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return grupos;
	}

	@Override
	/**
	 * Método que registra un grupo dado
	 * @param GrupoDTO bean que contiene la información a registrar del grupo
	 * @return int id del grupo que se acaba de registrar, si no se registra el grupo devolverá un id = -1
	 */
	public int registrar(GrupoDTO grupo) {
		String sqlINSERT="INSERT INTO t_grupo (nombre, idMonitor) VALUES (?, ?)";
		Connection conexion = null;
		PreparedStatement pstm = null;
		int id = -1;
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sqlINSERT,  Statement.RETURN_GENERATED_KEYS);
			pstm.setString(1, grupo.getNombre());
			pstm.setString(2, grupo.getIdMonitor());
			int i = pstm.executeUpdate();
			if(i!=0){
				ResultSet llaves = pstm.getGeneratedKeys();
				while (llaves.next()) {
					id = llaves.getInt(1);
				}
				llaves.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return id;
	}

	@Override
	/**
	 * Método que modifica la información de un grupo
	 * @param GrupoDTO bean que contiene la información a ser modificada
	 * @return si se realiza correctamente la modificación, se devuelve true, caso contrario, se devuelve false
	 */
	public boolean modificar(GrupoDTO grupo) {
		String sql="UPDATE t_grupo SET nombre = ? WHERE idGrupo = ?";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setString(1, grupo.getNombre());
			pstm.setInt(2, Integer.parseInt(grupo.getIdGrupo()));
			int i = pstm.executeUpdate();
			estado = i!=0?true:false;
		} catch (SQLException e) {	
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return estado;
	}

	@Override
	/**
	 * Método que elimina un grupo. Solo se acutalizará el estado a '0'
	 * @param idGrupo el id del grupo a eliminar
	 * @return si se realiza correctamente la eliminación, se devuelve true, caso contrario, devuelve false
	 */
	public boolean eliminar(int idGrupo) {
		String sql="UPDATE t_grupo SET estado = '0' WHERE idGrupo = ?";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idGrupo);		
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
	/**
	 * Método que asigna a un Grupo una cantidad determinada de usuarios
	 * @param idGrupo es el id del grupo al que se le asignarán los usuarios dados
	 * @param usuarios que serán asignados al grupo
	 * @return devuelve true si se asignaron correctamente, caso contrario devolverá false
	 */
	public boolean asignarGrupo(int idGrupo, List<UsuarioDTO> usuarios) {
		String sqlSELECT = "SELECT idUsuario FROM t_usuarioxgrupo WHERE idGrupo=?";
		String sqlDELETE="DELETE FROM t_usuarioxgrupo WHERE idUsuario = ? AND idGrupo = ?";
		String sqlINSERT="INSERT INTO t_usuarioxgrupo (idUsuario, idGrupo) VALUES (?, ?)";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs=null;
		//Lista que se llenará con los usuarios obtenidos de la BD para idGrupo
		List<String> usuariosAsignadosBD=new ArrayList<String>();
		try {
			conexion = DataSourceBD.openConnection();
			conexion.setAutoCommit(false);
			pstm = conexion.prepareStatement(sqlSELECT);
			pstm.setInt(1 , idGrupo);
			rs = pstm.executeQuery();
			//Se guardan todos los idUsuario registrados para el grupo en la BD en una lista
			while(rs.next()) usuariosAsignadosBD.add(rs.getString(1)); 
			//Se compara con los idUsuario recibidos como parámetro
			for(UsuarioDTO usuario : usuarios){
				if(usuariosAsignadosBD.contains(usuario.getIdUsuario())){
					//Si el usuario enviado desde la interfaz ya existe en la BD, no se hace nada y se elimina de la lista.
					usuariosAsignadosBD.remove(usuario.getIdUsuario());
					estado = true;
				}else{
					//Si no existe en la lista se hace un INSERT
					pstm=conexion.prepareStatement(sqlINSERT);
					pstm.setInt(1, Integer.parseInt(usuario.getIdUsuario()));
					pstm.setInt(2, idGrupo);
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
					pstm.setInt(2, idGrupo);
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
	public GrupoDTO obtener(int idGrupo) {
		String sqlGRUPO="SELECT idGrupo, nombre FROM t_grupo WHERE idGrupo = ?";
		String sqlUSUARIOS = "SELECT u.idUsuario, u.etiqueta, u.numero, u.estadoHandset FROM t_usuario u, t_usuarioxgrupo gxu WHERE u.idUsuario = gxu.idUsuario AND gxu.idGrupo = ? AND u.estado='1'";
		GrupoDTO grupo=null;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sqlGRUPO);	
			pstm.setInt(1, idGrupo);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				grupo=new GrupoDTO();
				grupo.setIdGrupo(rs.getString(1));
				grupo.setNombre(rs.getString(2));
			}
			
			pstm=conexion.prepareStatement(sqlUSUARIOS);
			pstm.setInt(1, idGrupo);
			rs=pstm.executeQuery();
			List<UsuarioDTO> usuarios=new ArrayList<UsuarioDTO>();
			while(rs.next()){
				UsuarioDTO usuario = new UsuarioDTO();
				usuario.setIdUsuario(rs.getString(1));
				usuario.setEtiqueta(rs.getString(2));
				usuario.setNumero(rs.getString(3));
				usuario.setEstadoHandset(rs.getString(4));
				usuarios.add(usuario);
			}
			grupo.setUsuarios(usuarios);
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return grupo;
	}

	@Override
	public TransaccionDTO localizar(List<UsuarioDTO> usuarios, LogDTO logDTO) {
				Date now = new Date();
				int exito =0;
				int idMetodo=0;
				//String sql = "SELECT TOP 1 longitud, latitud, time, metodo FROM t_consulta WHERE msid = (SELECT numero FROM t_usuario WHERE idUsuario = ?) AND resultado='1' ORDER BY time DESC";
				String sql = "SELECT longitud, latitud, time, metodo, timestamp, origen FROM t_usuario WHERE idUsuario = ?";
				Connection conexion = null;
				PreparedStatement pstm = null;
				ResultSet rs = null;
				List<PosicionDTO> posiciones = new ArrayList<PosicionDTO>();
				
				TransaccionDTO transaccion = new TransaccionDTO();			
				String numeroUsuario = "";
				String fechaEnvio = ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date()));
				String fechaRespuesta;
				String numerosConsulta = "";
				try {
					conexion = DataSourceBD.openConnection();
					pstm = conexion.prepareStatement(sql);
					for(UsuarioDTO usuario : usuarios){
						LogDTO log = new LogDTO();
						
						numeroUsuario = usuario.getNumero();
						numerosConsulta+=numeroUsuario+":"; 
						pstm.setInt(1, Integer.parseInt(usuario.getIdUsuario()));
						rs = pstm.executeQuery();
						String fechaRegistro = new String();
						PosicionDTO posicion = new PosicionDTO();
						posicion.setIdTipoTransaccion(log.OPERACION_LOCALIZACION_GRUPAL);
						posicion.setIdComponente(logDTO.COMPONENTE_BD);
						
						if (rs.next()) {
							CoordenadaDTO c = new CoordenadaDTO();
							c.setLongitud(rs.getString(1));
							c.setLatitud(rs.getString(2));
							posicion.setCoordenada(c);
							posicion.setMetodo(rs.getString(4));
							fechaRegistro = rs.getString(3);
							String timestamp = rs.getString(5);
							String origen = rs.getString(6);
							if(timestamp!=null){
								posicion.setTimestamp(ParserUtil.glueDate(timestamp));
							}else{
								posicion.setTimestamp(ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date())));
							}
							posicion.setTecnologia(origen);							
						}
						
						if(fechaRegistro!=null){
							long diferencia = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - ParserUtil.stringToDate(fechaRegistro).getTime());
							if(diferencia<=Integer.parseInt(PropertyUtil.readProperty("TIEMPO_CACHE"))){
								posicion.setTime(ParserUtil.glueDate(fechaRegistro));
								posicion.setCache(true);	
								posicion.setFlagExito(1);								
							}else{
								posicion.setCache(false);
								posicion.setCoordenada(null);
								posicion.setFlagExito(0);
							}		
							idMetodo = ParserUtil.devolverMetodo(posicion.getMetodo());
							posicion.setIdMetodo(idMetodo);
							posicion.setFechaRespuesta(ParserUtil.glueDate(fechaRegistro));
//							sqlServerLogDAO.updateLogDetalleLocalizacion(idLog, usuario.getNumero(), fechaRegistro, LogDTO.COMPONENTE_BD,exito );
						}else{
							posicion.setCache(false);
							posicion.setCoordenada(null);
							posicion.setFlagExito(0);
						}						
						posicion.setUsuario(usuario);
						posiciones.add(posicion);	
					}
					fechaRespuesta = ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date()));
					sqlServerLogDAO.registrarLogDetalle(logDTO.getIdLog(), logDTO.MENSAJE_BUSCAR_LOCALIZACION_BD, LogDTO.COMPONENTE_WEB, numerosConsulta, LogDTO.COMPONENTE_BD, "", 1,fechaEnvio, fechaRespuesta);
					transaccion.setFlagExito(logDTO.FLAG_EXITO_RESPONDIO);
				} catch (SQLException e) {	
					e.printStackTrace();
					transaccion = null;
					fechaRespuesta = ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date()));
					sqlServerLogDAO.registrarLogDetalle(logDTO.getIdLog(), logDTO.MENSAJE_BUSCAR_LOCALIZACION_BD, logDTO.COMPONENTE_WEB, numeroUsuario, LogDTO.COMPONENTE_BD, "", 0, fechaEnvio, fechaRespuesta);
					sqlServerLogDAO.registrarLogError(logDTO.getIdLog(), e.getMessage());					
				} finally{
					DataSourceBD.closeConnection(conexion);
				}
				transaccion.setPosiciones(posiciones);
				return transaccion;
	}
	
	

}
