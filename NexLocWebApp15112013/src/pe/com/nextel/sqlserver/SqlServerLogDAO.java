package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;
import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.iface.LogDAO;
import pe.com.nextel.util.DataSourceBD;
import pe.com.nextel.util.ParserUtil;

public class SqlServerLogDAO implements LogDAO{
	
	public LogDTO registrarLog(int operacion, int idUsuario, int id){
		String sql ="insert into t_log (idOperacion, idMonitor, id, fechaRegistro, flagExito) values(?,?,?,getdate(),0)";
		Connection conexion = null;
		PreparedStatement pstm = null;		
		LogDTO logDTO = new LogDTO();
		
		boolean estado=false;
		int idLog = 0;
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm.setInt(1, operacion);
			pstm.setInt(2, idUsuario);
			pstm.setInt(3, id);
			int i = pstm.executeUpdate();
			estado=i!=0?true:false;
			if(estado){
				ResultSet llaves = pstm.getGeneratedKeys();
				while (llaves.next()) {
					idLog = llaves.getInt(1);
				}
				llaves.close();
			}
			logDTO.setIdLog(idLog);
		} catch (SQLException e) {
			estado=false;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return logDTO;
	}
	
	public int registrarLogDetalle(int idLog, int idMensaje,int idComponenteEnvio, String descripcionEnvio,int idComponenteRespuesta, String descripcionRespuesta, int flagExito, String fechaEnvio, String fechaRespuesta){
		String sql ="insert into t_logDetalle(idLog, idMensaje,idComponenteEnvio, descripcionEnvio, idComponenteRespuesta, descripcionRespuesta, flagExito, fechaEnvio, fechaRespuesta) values (?,?,?,?,?,?,?,?,?)";
		Connection conexion = null;
		PreparedStatement pstm = null;		
		String fechaConvertida ="";
		boolean estado=false;
		int idLogDetalle = 0;
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm.setInt(1, idLog);
			pstm.setInt(2, idMensaje);
			pstm.setInt(3, idComponenteEnvio);
			pstm.setString(4, descripcionEnvio);
			pstm.setInt(5, idComponenteRespuesta);
			pstm.setString(6, descripcionRespuesta);
			pstm.setInt(7, flagExito);
			pstm.setString(8, ParserUtil.stringToStringFormat(fechaEnvio));
			
			if(fechaRespuesta!=""){
				fechaConvertida = ParserUtil.stringToStringFormat(fechaRespuesta);
				pstm.setString(9, fechaConvertida);
			}else{
				pstm.setNull(9, java.sql.Types.DATE);				
			}
						int i = pstm.executeUpdate();
			estado=i!=0?true:false;
			if(estado){
				ResultSet llaves = pstm.getGeneratedKeys();
				while (llaves.next()) {
					idLogDetalle = llaves.getInt(1);
				}
				llaves.close();
			}
		} catch (SQLException e) {
			estado=false;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return idLogDetalle;
	}
	
	public int registrarLogError(int idLog, String descripcion){
		String sql ="insert into t_logError(idLog,fechaRegistro, descripcion) values(?,getDate(),?)";
		Connection conexion = null;
		PreparedStatement pstm = null;		
		
		boolean estado=false;
		int idLogError = 0;
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm.setInt(1, idLog);
			pstm.setString(2, descripcion);
			int i = pstm.executeUpdate();
			estado=i!=0?true:false;
			if(estado){
				ResultSet llaves = pstm.getGeneratedKeys();
				while (llaves.next()) {
					idLogError = llaves.getInt(1);
				}
				llaves.close();
			}
		} catch (SQLException e) {
			estado=false;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return idLogError;
	}
	
	public boolean registrarLogDetalleNumero(TransaccionDTO transaccion){
		boolean estado=false;
		String sql ="insert into t_logDetalleNumero " +
		"(idLog, numero, fechaRegistro, flagExito, idMetodo, fechaLocalizacion, idComponente, error, " +
		"descripcionError, origen, operacion) values (?,?,getdate(),?,?,CAST(? as datetime),?,?,?,?,?)";
		
		Connection conexion = null;
		PreparedStatement pstm = null;	
		int idLogDetalle;
		try{
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			if (transaccion.getError() == null) {
				List<PosicionDTO> posiciones = transaccion.getPosiciones();						
				for (PosicionDTO posicion : posiciones) {
					try{
						if(posicion.getIdComponente()==LogDTO.COMPONENTE_BD){
							if(posicion.getFlagExito()==1){
								pstm.setInt(1, transaccion.getIdLog());
								pstm.setString(2, posicion.getUsuario().getNumero());
								pstm.setInt(3, posicion.getFlagExito());
								pstm.setInt(4, posicion.getIdMetodo());
								pstm.setString(5, ParserUtil.stringToStringFormat(posicion.getFechaRespuesta()));
								pstm.setInt(6, posicion.getIdComponente());							
								pstm.setNull(7, java.sql.Types.VARCHAR);
								pstm.setNull(8, java.sql.Types.VARCHAR);								
								pstm.setString(9, posicion.getTecnologia());
								pstm.setInt(10, posicion.getIdTipoTransaccion());
								int i = pstm.executeUpdate();
								estado=i!=0?true:false;
								if(estado){
									ResultSet llaves = pstm.getGeneratedKeys();
									while (llaves.next()) {
										idLogDetalle = llaves.getInt(1);
									}
									llaves.close();
								}
							}
						}
					}catch(Exception e){
						String mensaje = e.getMessage();
					}
				}
			}
		} catch (SQLException e) {
			estado=false;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return estado;
	}
	
//	public boolean registrarLogDetalleNumero(TransaccionDTO transaccion){
//		String sql ="insert into t_logDetalleNumero " +
//		"(idLog, numero, fechaRegistro, flagExito, idMetodo, fechaLocalizacion, idComponente, error, " +
//		"descripcionError, origen, operacion) values (?,?,getdate(),?,?,CAST(? as datetime),?,?,?,?,?)";
//		String sqlSelectUsuario ="SELECT origen FROM t_usuario WHERE numero = ? AND estado = '1'";
//		
//		Connection conexion = null;
//		PreparedStatement pstm = null;	
//		ResultSet rs = null;
//		boolean estado=false;
//		int idLogDetalle = 0;
//		try {
//			conexion = DataSourceBD.openConnection();
//			pstm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//			
//			for(LogDTO log : logDetalle){				
//				if(log.getOrigen().equalsIgnoreCase("") || log.getOrigen()==null){
//					pstm = conexion.prepareStatement(sqlSelectUsuario);
//					pstm.setString(1, log.getNumero());
//					rs=pstm.executeQuery();
//					if(rs.next())	log.setOrigen(rs.getString(1));
//				}	
//				pstm.setInt(1, log.getIdLog());
//				pstm.setString(2, log.getNumero());
//				pstm.setInt(3, log.getFlagExito());
//				if(log.getIdMetodo()!=0){
//					pstm.setInt(4, log.getIdMetodo());
//				}else{
//					pstm.setNull(4, java.sql.Types.INTEGER);
//				}
//				if(log.getFechaRespuesta()!=null){
//					pstm.setString(5, ParserUtil.stringToStringFormat(log.getFechaRespuesta()));
//				}else{
//					pstm.setNull(5, java.sql.Types.DATE);
//				}				
//				pstm.setInt(6, log.getIdComponente());
//				pstm.setString(7, log.getError());
//				pstm.setString(8, log.getDescripcionError());
//				pstm.setString(9, log.getOrigen());
//				pstm.setInt(10, tipoTransaccion);
//				int i = pstm.executeUpdate();
//				estado=i!=0?true:false;
//				if(estado){
//					ResultSet llaves = pstm.getGeneratedKeys();
//					while (llaves.next()) {
//						idLogDetalle = llaves.getInt(1);
//					}
//					llaves.close();
//				}
//			}			
//			
//		} catch (SQLException e) {
//			estado=false;
//			e.printStackTrace();
//		} finally{
//			DataSourceBD.closeConnection(conexion);
//		}
//		return estado;
//	}

	public int updateLogExito(int idLog, int flagExito){
		String sql ="update t_log set flagExito	=? where idLog=?";
		Connection conexion = null;
		PreparedStatement pstm = null;		
		
		boolean estado=false;
		int idLogError = 0;
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm.setInt(1, flagExito);
			pstm.setInt(2, idLog);
			int i = pstm.executeUpdate();
			estado=i!=0?true:false;
			if(estado){
				ResultSet llaves = pstm.getGeneratedKeys();
				while (llaves.next()) {
					idLogError = llaves.getInt(1);
				}
				llaves.close();
			}
		} catch (SQLException e) {
			estado=false;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return idLogError;
	}
	

}
