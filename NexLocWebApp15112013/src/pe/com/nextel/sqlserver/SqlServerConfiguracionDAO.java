package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pe.com.nextel.bean.ConfiguracionDTO;
import pe.com.nextel.dao.iface.ConfiguracionDAO;
import pe.com.nextel.util.DataSourceBD;
import pe.com.nextel.util.ParserUtil;

public class SqlServerConfiguracionDAO implements ConfiguracionDAO{
	
	@Override
	public int registrar(ConfiguracionDTO config) {
		
		String sqlSelect = "SELECT parametro FROM t_configuracion";
		String sql = "UPDATE t_configuracion SET valor = ? WHERE parametro = ?";
		
		List<String> parametros = new ArrayList<String>();
		
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		int resp = 0;
		
		try {
			conexion = DataSourceBD.openConnection();
			
			if(!config.getEstadoAprov().equals("1")){
				pstm.setString(2, "ESTADO_APROV");
				pstm.setString(1, "0");
				resp = pstm.executeUpdate();
			}else{
				pstm = conexion.prepareStatement(sqlSelect);
				rs = pstm.executeQuery();
				
				while(rs.next()){
					parametros.add(rs.getString(1));
				}
				
				pstm = conexion.prepareStatement(sql);
				
				for (String param : parametros) {
					pstm.setString(2, param);
					pstm.setString(1, ParserUtil.unmapParametro(param, config));
					resp = pstm.executeUpdate();
				}
			}
			
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		
		return resp;
	}

	@Override
	public ConfiguracionDTO obtener() {
		// TODO Auto-generated method stub
		
		String sql = "SELECT * FROM t_configuracion";
		
		ConfiguracionDTO config = new ConfiguracionDTO();
		
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			rs = pstm.executeQuery();
			
			while(rs.next()){
				ParserUtil.mapParametro(rs.getString(1), rs.getString(2), config);
			}
			
		} catch (SQLException e) {		
			config=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		
		return config;
	}

	@Override
	public String obtenerParametro(String param) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT valor FROM t_configuracion WHERE parametro = ?";
		String valor = null;
		
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setString(1, param);
			rs = pstm.executeQuery();
		
			while(rs.next()){
				valor = rs.getString(1);
			}
			
		} catch (SQLException e) {		
			valor=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}	
			
		return valor;
	}

	


}
