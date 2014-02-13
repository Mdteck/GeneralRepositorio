package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.log4j.Logger;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;

import pe.com.nextel.bean.CoordenadaDTO;
import pe.com.nextel.bean.CuentaDTO;
import pe.com.nextel.bean.HandsetDTO;
import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.LogDAO;
import pe.com.nextel.dao.iface.UsuarioDAO;
import pe.com.nextel.util.DataSourceBD;
import pe.com.nextel.util.LoggerUtil;
import pe.com.nextel.util.ParserUtil;
import pe.com.nextel.util.PropertyUtil;

/**
 * @author Devos Inc.
 * 
 * Clase que accede y maneja los datos del motos SqlServer para las funcionalidades de la intefaz UsuarioDAO.
 * 
 */

public class SqlServerUsuarioDAO implements UsuarioDAO {
	
	DAOFactory fabricaSqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	LogDAO sqlServerLogDAO = fabricaSqlServer.getLogDAO();
	Logger logger = LoggerUtil.getInstance();
//	Logger logger = Logger.getLogger(SqlServerUsuarioDAO.class.getName());
	
	@Override
	public String getOrigen(String numero){
		String sqlSelectUsuario ="SELECT origen FROM t_usuario WHERE numero = ? AND estado = '1'"; 
		String origen="";
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sqlSelectUsuario);
			pstm.setString(1, numero);
			rs=pstm.executeQuery();
			if(rs.next())	origen=rs.getString(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceBD.closeConnection(conexion);
		}
		return origen;
	}
	
	@Override
	/**
	 * Método que modifica la información de un usuario
	 * @param UsuarioDTO usuario : objeto con la información y datos del usuario a modificar
	 * @return boolean estado: si la modificación se realizó correctamente, devolverá True. Si ocurrió algún error al modificar el suario, devolverá False.
	 */
	public boolean modificar(UsuarioDTO usuario) {
		String sql="UPDATE t_usuario SET etiqueta=?, idHandset= ? WHERE idUsuario = ?";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		try {
			conexion = DataSourceBD.openConnection();
			conexion.setAutoCommit(false);
			
			//Se modifican los datos
			pstm = conexion.prepareStatement(sql);	
			
			pstm.setString(1, usuario.getEtiqueta());
			pstm.setString(2, usuario.getHandset().getIdHandset());
			pstm.setString(3, usuario.getIdUsuario());
			int i = pstm.executeUpdate();
			estado = i!=0?true:false;
			
			conexion.commit();
			conexion.setAutoCommit(true);
		} catch (SQLException e) {	
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		
		return estado;
	}

	
	@Override
	/**
	 * Método que obtiene una lista de Usuarios de la BD.
	 * @return List<UsuarioDTO> usuarios: Develve la lista de usuarios de la BD. Si ocurre un error, devuelve null. Si no existe ningun usuario, devolverá la lista con tamaño = 0
	 */
	public List<UsuarioDTO> listar(int idCuenta) {
		String sql="SELECT idUsuario, numero, etiqueta, tipo FROM t_usuario WHERE (tipo = '2' OR tipo = '1') AND idCuenta = ? AND estado = '1'"; 
		List<UsuarioDTO> usuarios=null;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setInt(1, idCuenta);
			rs = pstm.executeQuery();
			usuarios=new ArrayList<UsuarioDTO>();
			while (rs.next()) {
				UsuarioDTO usuario=new UsuarioDTO();
				usuario.setIdUsuario(rs.getString(1));
				usuario.setNumero(rs.getString(2));
				usuario.setEtiqueta(rs.getString(3));
				if(idCuenta == -1){
					CuentaDTO cuenta = new CuentaDTO();
					cuenta.setNombre(rs.getString(4));
					usuario.setCuenta(cuenta);
				}else{
					String tipo = rs.getString(4);
					if(tipo.equals("0")) tipo="Administrador del Sistema";
					else if(tipo.equals("1")) tipo="Administrador";
					else if(tipo.equals("2")) tipo="Monitor";
					else					  tipo="Sin asignar";
					usuario.setTipo(tipo);
				}
				
				usuarios.add(usuario);
			}
		} catch (SQLException e) {		
			usuarios=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return usuarios;
	}
	
	public List<UsuarioDTO> listarNoMonitoreados(int idMonitor, List<UsuarioDTO> monitoreados){
		List<UsuarioDTO> listaNoMonitoreados = new ArrayList<UsuarioDTO>();
		List<UsuarioDTO> listaNoBD = new ArrayList<UsuarioDTO>();
		
		String sqlSELECTERROR = "SELECT idMonitoreado FROM t_monitorxmonitoreadoerror WHERE idMonitor = ?";
		String sqlSELECT="SELECT idMonitoreado FROM t_monitorxmonitoreado WHERE idMonitor = ?";
		
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		try {
			conexion = DataSourceBD.openConnection();
			
			pstm = conexion.prepareStatement(sqlSELECT);	
			pstm.setInt(1, idMonitor);
			rs=pstm.executeQuery();
			List<String> monitoreadosEnBD = new ArrayList<String>();
			
			//Se obtienen los valores antiguos en la BD
			while(rs.next()) monitoreadosEnBD.add(rs.getString(1));
			
			
			for(UsuarioDTO usuario : monitoreados){
				
				if(monitoreadosEnBD.contains(usuario.getIdUsuario())){
					//Si el usuario enviado desde la interfaz ya existe en la BD, no se hace nada y se elimina de la lista.
					monitoreadosEnBD.remove(usuario.getIdUsuario());
				}else{
					//listaNoBD.add(usuario);
					listaNoMonitoreados.add(usuario);
				}
				
			}
			/*
			pstm = conexion.prepareStatement(sqlSELECTERROR);	
			pstm.setInt(1, idMonitor);
			rs=pstm.executeQuery();
			List<String> monitoreadosEnBDError = new ArrayList<String>();
			
			//Se obtienen los valores antiguos en la BD
			while(rs.next()) monitoreadosEnBDError.add(rs.getString(1));
			
			for(UsuarioDTO usuarioNoBD: listaNoBD){
				if(monitoreadosEnBDError.contains(usuarioNoBD.getIdUsuario())){
					//Si el usuario enviado desde la interfaz ya existe en la BD, no se hace nada y se elimina de la lista.
					monitoreadosEnBD.remove(usuarioNoBD.getIdUsuario());
				}else{
					listaNoMonitoreados.add(usuarioNoBD);
				}
			}*/
			
		} catch (SQLException e) {	
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		
		return listaNoMonitoreados;
		
	}
	

	@Override
	/**
	 * Dada una lista de usuarios, se actualizarán los registros de dichos usuarios con el idMonitor.
	 * @param idMonitor : idUsuario del usuario monitor
	 * @param monitoreados: Lista de usuarios para los que se le asignará un monitor
	 * @return boolean : true si se actualizan todos los usuarios correctamente, 
	 * 
	 */
	public boolean asignarMonitoreados(int idMonitor, List<UsuarioDTO> monitoreados) {
		String sqlDELETE="DELETE FROM t_monitorxmonitoreado WHERE idMonitor = ? AND idMonitoreado = ?";
		String sqlSELECT="SELECT idMonitoreado FROM t_monitorxmonitoreado WHERE idMonitor = ?";
		String sqlINSERT = "INSERT INTO t_monitorxmonitoreado (idMonitor, idMonitoreado) VALUES (?, ?)";
		String sqlINSERTERROR = "INSERT INTO t_monitorxmonitoreadoerror (idMonitor, idMonitoreado, fechaRegistro) VALUES (?, ?, GETDATE())";
		String sqlSELECTGRUPO="SELECT idGrupo FROM t_grupo WHERE idMonitor = ?";
		String sqlDELETEUSUARIOXGRUPO="DELETE FROM t_usuarioxgrupo WHERE idUsuario = ? AND idGrupo = ?";
		String sqlSELECTUSUARIOXGRUPO="SELECT COUNT(1) FROM t_usuarioxgrupo WHERE idGrupo = ?";
		String sqlDELETEGRUPO="UPDATE t_grupo SET estado = '0' WHERE idGrupo= ?";
		String sqlSELECTGEOCERCA="SELECT idGeocerca FROM t_geocerca WHERE idUsuario = ?";
		String sqlDELETEUSUARIOXGEOCERCA="DELETE FROM t_usuarioxgeocerca WHERE idUsuario = ? AND idGeocerca = ?";
		String sqlSELECTUSUARIOXGEOCERCA="SELECT COUNT(1) FROM t_usuarioxgeocerca WHERE idGeocerca = ?";
		String sqlDELETEGEOCERCA="UPDATE t_geocerca SET estado = '0' WHERE idGeocerca = ?";
		String sqlSELECTCUENTAUSUARIO = "SELECT idCuenta FROM t_usuario WHERE idUsuario = ?";
		
		String idCuentaMonitor = "";
		String idCuentaMonitoreado = "";
		
		List<UsuarioDTO> listaNoMonitoreados = new ArrayList<UsuarioDTO>();
		
		
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conexion = DataSourceBD.openConnection();
			conexion.setAutoCommit(false);
			pstm = conexion.prepareStatement(sqlSELECT);	
			pstm.setInt(1, idMonitor);
			rs=pstm.executeQuery();
			List<String> monitoreadosEnBD = new ArrayList<String>();
			
			//Se obtienen los valores antiguos en la BD
			while(rs.next()) monitoreadosEnBD.add(rs.getString(1));
			
			pstm = conexion.prepareStatement(sqlSELECTCUENTAUSUARIO);
			pstm.setInt(1, idMonitor);
			rs=pstm.executeQuery();
			
			while(rs.next()) idCuentaMonitor = rs.getString(1);
			
			//Se compara con los idUsuario recibidos como parámetro y se ve si se ha elegido al usuario a modificar como monitor. Si no se le ha
			//seleccionado, se le tiene que aumentar.
			
			for(UsuarioDTO usuario : monitoreados){
				if(monitoreadosEnBD.contains(usuario.getIdUsuario())){
					//Si el usuario enviado desde la interfaz ya existe en la BD, no se hace nada y se elimina de la lista.
					monitoreadosEnBD.remove(usuario.getIdUsuario());
					estado=true;
				}else{
					//Si no existe en la lista se hace un INSERT
					idCuentaMonitoreado = "";
					
					pstm = conexion.prepareStatement(sqlSELECTCUENTAUSUARIO);
					pstm.setString(1, usuario.getIdUsuario());
					rs=pstm.executeQuery();
					
					while(rs.next()) idCuentaMonitoreado = rs.getString(1);
					
					if(idCuentaMonitor.equals(idCuentaMonitoreado)){
						pstm=conexion.prepareStatement(sqlINSERT);
						pstm.setInt(1, idMonitor);
						pstm.setInt(2, Integer.parseInt(usuario.getIdUsuario()));
						int i = pstm.executeUpdate();
						if (i!=0) 	estado= true;
					}else{
						pstm=conexion.prepareStatement(sqlINSERTERROR);
						pstm.setInt(1, idMonitor);
						pstm.setInt(2, Integer.parseInt(usuario.getIdUsuario()));
						int i = pstm.executeUpdate();
						if (i!=0) 	estado= true;
						
						listaNoMonitoreados.add(usuario);
					}
					
					
				}
			}
			
			//Si en la lista llenada de la BD aún existen registros, se eliminarán.
			//Se supone que si aún están en la lista, es porque se eliminó el o los usuarios en la interfaz web
			if(monitoreadosEnBD.size()!=0){
				for(String idUsuarioEliminado : monitoreadosEnBD){
					if (!idUsuarioEliminado.equals(String.valueOf(idMonitor))){ // solo se eliminara si este no es monitor
						pstm=conexion.prepareStatement(sqlDELETE);
						pstm.setInt(1, idMonitor);
						pstm.setInt(2, Integer.parseInt(idUsuarioEliminado));
						pstm.executeUpdate();
						estado=true;
						//Si se desasigna un monitoreado, debe desaparecer de los grupos creados por el monitor
						//Se busca los grupos creados por el monitor
						pstm = conexion.prepareStatement(sqlSELECTGRUPO);
						pstm.setInt(1, idMonitor);
						rs=pstm.executeQuery();
						List<Integer> ids = new ArrayList<Integer>();
						while(rs.next())	ids.add(rs.getInt(1));
						//Si hubiera algún grupo creado por el monitor, elimino todos aquellos registros donde esté el usuario desasignado y el grupo
						if(ids.size()!=0){
							for(int idGrupo : ids){
								pstm = conexion.prepareStatement(sqlDELETEUSUARIOXGRUPO);
								pstm.setInt(1, Integer.parseInt(idUsuarioEliminado));
								pstm.setInt(2, idGrupo);
								pstm.executeUpdate();
								pstm = conexion.prepareStatement(sqlSELECTUSUARIOXGRUPO);
								pstm.setInt(1, idGrupo);
								rs = pstm.executeQuery();
								int usuariosxgrupo = -1;
								if(rs.next()) usuariosxgrupo = rs.getInt(1);
								if(usuariosxgrupo==0 || usuariosxgrupo==1){
									pstm=conexion.prepareStatement(sqlDELETEGRUPO);
									pstm.setInt(1, idGrupo);
									pstm.executeUpdate();
								}
							}
							
						}
						//Si se desasigna un monitoreado, debe desaparecer de los grupos 
						pstm = conexion.prepareStatement(sqlSELECTGEOCERCA);
						pstm.setInt(1, idMonitor);
						rs=pstm.executeQuery();
						ids = new ArrayList<Integer>();
						while(rs.next())	ids.add(rs.getInt(1));
						//Si hubiera alguna geocerca creado por el monitor, elimino todos aquellos registros donde esté el usuario desasignado y la geocerca
						if(ids.size()!=0){
							for(int idGeocerca : ids){
								pstm = conexion.prepareStatement(sqlDELETEUSUARIOXGEOCERCA);
								pstm.setInt(1, Integer.parseInt(idUsuarioEliminado));
								pstm.setInt(2, idGeocerca);
								pstm.executeUpdate();
								pstm=conexion.prepareStatement(sqlSELECTUSUARIOXGEOCERCA);
								pstm.setInt(1, idGeocerca);
								rs= pstm.executeQuery();
								int usuariosxgeocerca = -1;
								if(rs.next()) usuariosxgeocerca=rs.getInt(1);
								if(usuariosxgeocerca==0 || usuariosxgeocerca==1){
									pstm=conexion.prepareStatement(sqlDELETEGEOCERCA);
									pstm.setInt(1, idGeocerca);
									pstm.executeUpdate();
								}
							}
							
						}
					}
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

	//Se corrigio Query para el cache
	@Override
	public TransaccionDTO localizar(UsuarioDTO usuario, LogDTO logDTO) {
		String sql = "SELECT longitud, latitud, time, metodo, timestamp, origen FROM t_usuario WHERE idUsuario = ?";
		long diferencia = 0;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		PosicionDTO posicion = new PosicionDTO();
		posicion.setUsuario(usuario);
		List<PosicionDTO> posiciones = new ArrayList<PosicionDTO>();
		TransaccionDTO transaccion = new TransaccionDTO();		
		String fechaEnvio = ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date()));
		String fechaRespuesta;
		posicion.setIdTipoTransaccion(logDTO.OPERACION_LOCALIZACION_INDIVIDUAL);
		posicion.setIdComponente(logDTO.COMPONENTE_BD);
		
		int idMetodo;
		String origen="";
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, Integer.parseInt(usuario.getIdUsuario()));
			rs = pstm.executeQuery();
			String fechaRegistro = new String();
			if (rs.next()) {
				CoordenadaDTO c = new CoordenadaDTO();
				c.setLongitud(rs.getString(1));
				c.setLatitud(rs.getString(2));				
				posicion.setCoordenada(c);
				fechaRegistro=rs.getString(3);
				posicion.setMetodo(rs.getString(4));
				String timestamp = rs.getString(5);
				origen = rs.getString(6);
				if(timestamp!=null){
					posicion.setTimestamp(ParserUtil.glueDate(timestamp));
				}else{
					posicion.setTimestamp(ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date())));
				}
				posicion.setTecnologia(origen);
			}
			Date now = new Date();
			
			if(fechaRegistro!=null){				
				diferencia = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - ParserUtil.stringToDate(fechaRegistro).getTime());
				logger.info("La ultima localizacion exitosa para "+usuario.getNumero()+" fue hace :"+diferencia+ " minutos.");
				if(diferencia<0){
					posicion.setCache(false);
					posicion.setFlagExito(0);					
				}else if(diferencia<=Integer.parseInt(PropertyUtil.readProperty("TIEMPO_CACHE"))){
					posicion.setTime(ParserUtil.glueDate(fechaRegistro));
					posicion.setCache(true);
					posicion.setFlagExito(1);
				}else{
					posicion.setCache(false);
					posicion.setFlagExito(0);
				}	
				idMetodo = ParserUtil.devolverMetodo(posicion.getMetodo());
				posicion.setIdMetodo(idMetodo);
				posicion.setFechaRespuesta(ParserUtil.glueDate(fechaRegistro));
			}else{
				posicion.setCache(false);
				posicion.setFlagExito(0);
			}
			posiciones.add(0,posicion);
			transaccion.setPosiciones(posiciones);
			fechaRespuesta = ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date()));
			transaccion.setFlagExito(logDTO.FLAG_EXITO_RESPONDIO);
			sqlServerLogDAO.registrarLogDetalle(logDTO.getIdLog(), logDTO.MENSAJE_BUSCAR_LOCALIZACION_BD, LogDTO.COMPONENTE_WEB, usuario.getNumero(), LogDTO.COMPONENTE_BD, "Ultima Localizacion: "+fechaRegistro, 1, fechaEnvio, fechaRespuesta);			
		} catch (SQLException e) {	
			e.printStackTrace();
			fechaRespuesta = ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date()));
			sqlServerLogDAO.registrarLogDetalle(logDTO.getIdLog(), logDTO.MENSAJE_BUSCAR_LOCALIZACION_BD, LogDTO.COMPONENTE_WEB, usuario.getNumero(), LogDTO.COMPONENTE_BD, "", 0, fechaEnvio, fechaRespuesta);			
			sqlServerLogDAO.registrarLogError(logDTO.getIdLog(), e.getMessage());
			transaccion.setFlagExito(logDTO.FLAG_EXITO_NO_RESPONDIO);
		} finally{			
			DataSourceBD.closeConnection(conexion);			
		}
		return transaccion;
	}

	@Override
	/**
	 * Método que obtiene información de un usuario y lo devuelve como un objeto UsuarioDTO
	 * @param idUsuario del usuario para el que se requiere obtener la información
	 * @return UsuarioDTO el cual se devolverá, si es monitor, devolverá la lista de monitoreados, si no encuentra al usuario, devolverá null
	 */
	public UsuarioDTO obtener(int idUsuario) {
		String sqlSELECT="SELECT idUsuario, numero, etiqueta, tipo, idHandset, idCuenta, estadoHandset FROM t_usuario WHERE idUsuario = ?";
		String sqlMONITOR="SELECT idMonitoreado FROM t_monitorxmonitoreado WHERE idMonitor = ?";
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		UsuarioDTO usuario = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sqlSELECT);
			pstm.setInt(1, idUsuario);
			rs = pstm.executeQuery();
			if (rs.next()) {
				usuario=new UsuarioDTO();
				usuario.setIdUsuario(rs.getString(1));
				usuario.setNumero(rs.getString(2));
				usuario.setEtiqueta(rs.getString(3));
				String tipo = rs.getString(4);
				usuario.setTipo(tipo);
				HandsetDTO handset = new HandsetDTO();
				handset.setIdHandset(rs.getString(5));
				usuario.setHandset(handset);
				CuentaDTO cuenta = new CuentaDTO();;
				cuenta.setIdCuenta(rs.getString(6));
				usuario.setCuenta(cuenta);
				usuario.setEstadoHandset(rs.getString(7));
				
				pstm = conexion.prepareStatement(sqlMONITOR);
				pstm.setInt(1, idUsuario);
				rs = pstm.executeQuery();
				List<UsuarioDTO> monitoreados = new ArrayList<UsuarioDTO>();
				while(rs.next()){
					UsuarioDTO usuarioMonitoreado = new UsuarioDTO();
					usuarioMonitoreado.setIdUsuario(rs.getString(1));
					monitoreados.add(usuarioMonitoreado);
				}
				usuario.setMonitoreados(monitoreados);
			}
	
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return usuario;
	}

	@Override
	public List<UsuarioDTO> listarMonitoreados(int idMonitor) {
		String sql="SELECT u.idUsuario, u.numero, u.etiqueta FROM t_usuario u, t_monitorxmonitoreado mxm WHERE u.idUsuario = mxm.idMonitoreado AND mxm.idMonitor = ? AND u.estado='1'";
		List<UsuarioDTO> usuarios=null;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setInt(1,idMonitor);
			rs = pstm.executeQuery();
			usuarios=new ArrayList<UsuarioDTO>();
			while (rs.next()) {
				UsuarioDTO usuario=new UsuarioDTO();
				usuario.setIdUsuario(rs.getString(1));
				usuario.setNumero(rs.getString(2));
				usuario.setEtiqueta(rs.getString(3));
				usuarios.add(usuario);
			}
		} catch (SQLException e) {		
			usuarios=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return usuarios;
	}

	@Override
	public UsuarioDTO autenticar(String numero) {
		String sql="SELECT idUsuario, numero, etiqueta, tipo, idCuenta FROM t_usuario WHERE numero = ? AND estado = '1'";
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		UsuarioDTO usuario = null;
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			
			pstm.setString(1, numero);
			rs = pstm.executeQuery();
			
			if(rs.next()){
				usuario=new UsuarioDTO();
				usuario.setIdUsuario(rs.getString(1));
				usuario.setNumero(rs.getString(2));
				usuario.setEtiqueta(rs.getString(3));
				usuario.setTipo(rs.getString(4));
				CuentaDTO cuenta = new CuentaDTO();
				cuenta.setIdCuenta(rs.getString(5));
				usuario.setCuenta(cuenta);
			}

		} catch (SQLException e) {	
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		
		return usuario;
	}


	@Override
	public boolean perfil(UsuarioDTO usuario) {
		//desbloqueado 1 bloqueado 0
		String sqlConEstadoHandset="UPDATE t_usuario SET etiqueta=?, idHandset= ?, estadoHandset = ? WHERE idUsuario = ?";
		String sqlSinEstadoHandset="UPDATE t_usuario SET etiqueta=?, idHandset= ? WHERE idUsuario = ?";
		String sqlVerificaSiBloqueado="SELECT estadoHandset from t_usuario where idUsuario=?";
		String sqlBloqueo = "INSERT INTO t_bloqueo (idUsuario, timestamp, tipo) VALUES (?,CURRENT_TIMESTAMP,?)";
		boolean estado=false, continuar=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		try {
			conexion = DataSourceBD.openConnection();
			
			//int estadoHandset = -1;
			//if(usuario.getEstadoHandset()==null) 	estadoHandset=1;
			//else									estadoHandset=0;
			String estadoHandset = usuario.getEstadoHandset();
			
			pstm = conexion.prepareStatement(sqlVerificaSiBloqueado);	
			pstm.setString(1, usuario.getIdUsuario());
			ResultSet res=pstm.executeQuery();
			if(res.next()){
				String estadoHandsetActual=res.getString(1);
				if(estadoHandsetActual!=null && !estadoHandsetActual.equals(estadoHandset+""))
					continuar=true;				
				else
					continuar=false;
			}
			
			if(continuar){						
				conexion.setAutoCommit(false);
				
				//Se modifican los datos
				pstm = conexion.prepareStatement(sqlConEstadoHandset);	
				
				pstm.setString(1, usuario.getEtiqueta());
				pstm.setString(2, usuario.getHandset().getIdHandset());
				pstm.setInt(3, Integer.parseInt(estadoHandset));
				pstm.setString(4, usuario.getIdUsuario());
				int i = pstm.executeUpdate();
				estado = i!=0?true:false;
				
				// Cambios para registro en la tabla de bloqueos (t_bloqueo)
				if (estado){
					pstm = conexion.prepareStatement(sqlBloqueo);
					pstm.setInt(1, Integer.parseInt(usuario.getIdUsuario()));
					pstm.setString(2, String.valueOf(estadoHandset));
					
					i = pstm.executeUpdate();
					estado = i!=0?true:false;
				}
				//Fin Cambios
				
				conexion.commit();
				conexion.setAutoCommit(true);
			
			}
			else
				conexion.setAutoCommit(false);
			
				//Se modifican los datos
				pstm = conexion.prepareStatement(sqlSinEstadoHandset);	
				
				pstm.setString(1, usuario.getEtiqueta());
				pstm.setString(2, usuario.getHandset().getIdHandset());
				pstm.setString(3, usuario.getIdUsuario());
				int i = pstm.executeUpdate();
				estado = i!=0?true:false;
				
				conexion.commit();
				conexion.setAutoCommit(true);
			
		} catch (SQLException e) {	
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		
		return estado;
	}

	@Override
	public Object actualizarTipo(String numero, String tipo) {
		String sql="UPDATE t_usuario SET tipo=? WHERE numero = ? AND estado = '1'";
		Connection conexion = null;
		PreparedStatement pstm = null;
		UsuarioDTO usuario = null;
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			
			pstm.setString(2, numero);
			pstm.setString(1, tipo);
			pstm.executeUpdate();


		} catch (SQLException e) {	
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		
		return usuario;
	}

	@Override
	public UsuarioDTO autenticarAdmin(String numero, String password) {
		String sql="SELECT idUsuario, numero, etiqueta, tipo, idCuenta FROM t_usuario WHERE numero = ? AND estado = '1'";
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		UsuarioDTO usuario = null;
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			
			pstm.setString(1, numero);
			rs = pstm.executeQuery();
			
			if(rs.next()){
				usuario=new UsuarioDTO();
				usuario.setIdUsuario(rs.getString(1));
				usuario.setNumero(rs.getString(2));
				usuario.setEtiqueta(rs.getString(3));
				usuario.setTipo(rs.getString(4));
				CuentaDTO cuenta = new CuentaDTO();
				cuenta.setIdCuenta(rs.getString(5));
				usuario.setCuenta(cuenta);
			}

		} catch (SQLException e) {	
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		if(usuario!=null){
			String tipo = usuario.getTipo();
			String pass;
			if(tipo.equals("0"))		pass=PropertyUtil.readProperty("PASS_NSN");
			else if(tipo.equals("3"))	pass=PropertyUtil.readProperty("PASS_CAL");
			else						return null;
			if(!pass.equals(password))	usuario=null;			
		}
		return usuario;
	}
	
	public String getTimestamp(String numero){
		String sql="SELECT timestamp FROM t_usuario WHERE numero = ?";
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String timestamp = null;
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			
			pstm.setString(1, numero);
			rs = pstm.executeQuery();
			
			if(rs.next()){
				timestamp=rs.getString(1);
			}
			
			if(timestamp==null){
				timestamp=ParserUtil.toSqlDateTimeFormat(new Date());
				timestamp=ParserUtil.glueDate(timestamp);
			}

		} catch (SQLException e) {	
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		
		return timestamp;
	}

	@Override
	public String getEtiqueta(String numero) {
		String sql="SELECT etiqueta FROM t_usuario WHERE numero = ? and estado=1";
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String etiqueta = null;
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			
			pstm.setString(1, numero);
			rs = pstm.executeQuery();
			
			if(rs.next()){
				etiqueta=rs.getString(1);
			}

		} catch (SQLException e) {	
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		
		return etiqueta;
	}

}
