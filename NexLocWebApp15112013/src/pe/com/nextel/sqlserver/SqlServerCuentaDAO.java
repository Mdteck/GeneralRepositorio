package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pe.com.nextel.bean.CuentaDTO;
import pe.com.nextel.bean.HorarioDTO;
import pe.com.nextel.dao.iface.CuentaDAO;
import pe.com.nextel.util.DataSourceBD;

public class SqlServerCuentaDAO implements CuentaDAO {

	@Override
	public HorarioDTO obtenerHorario(int idCuenta) {
		String sql="SELECT horaInicio, horaFin FROM t_cuenta WHERE idCuenta = ?";
		HorarioDTO horario=null;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setInt(1,idCuenta);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				horario=new HorarioDTO();
				horario.setHoraInicio(rs.getString(1));
				horario.setHoraFin(rs.getString(2));
			}
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return horario;
	}

	@Override
	public boolean registrarHorario(CuentaDTO cuenta) {
		String sql="UPDATE t_cuenta set horaInicio = ?, horaFin = ? WHERE idCuenta = ?";
		Connection conexion = null;
		PreparedStatement pstm = null;
		boolean estado = false;
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setString(1, cuenta.getHorario().getHoraInicio());
			pstm.setString(2, cuenta.getHorario().getHoraFin());
			pstm.setInt(3, Integer.parseInt(cuenta.getIdCuenta()));
			int i = pstm.executeUpdate();
			if(i!=0) estado = true;
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return estado;
	}

}
