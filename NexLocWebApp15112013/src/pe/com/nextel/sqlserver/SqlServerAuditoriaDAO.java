package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import pe.com.nextel.dao.iface.AuditoriaDAO;
import pe.com.nextel.util.DataSourceBD;

public class SqlServerAuditoriaDAO implements AuditoriaDAO {
	
	

	@Override
	public boolean registrar(String tabla, int idUsuario, int idRegistro, String accion) {
		String sql="INSERT INTO t_auditoria (idUsuario, tabla, accion, idRegistro, timestamp) VALUES (?, ?, ?, ?, GETDATE())";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idUsuario);
			pstm.setString(2, tabla);
			pstm.setString(3, accion);
			pstm.setInt(4, idRegistro);
			int i = pstm.executeUpdate();
			estado=i!=0?true:false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return estado;
	}

}
