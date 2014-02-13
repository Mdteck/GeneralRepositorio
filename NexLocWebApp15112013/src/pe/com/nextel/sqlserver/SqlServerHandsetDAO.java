package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pe.com.nextel.bean.HandsetDTO;
import pe.com.nextel.dao.iface.HandsetDAO;
import pe.com.nextel.util.DataSourceBD;

/**
 * @author Devos Inc.
 */

public class SqlServerHandsetDAO implements HandsetDAO{

	@Override
	/**
	 * Método que elimina un handset. Se actualizará el estado del handset con valor = 0;
	 * @param idHandset: cadena que contiene el id del handset a eliminar.
	 * @return boolean : si es true, se eliminó correctamente. Sino, ocurrió un error en la eliminación (retorna false)
	 */
	public boolean eliminar(int idHandset) {
		String sql="UPDATE t_handset SET estado = '0' WHERE idHandset = ?";
		String sqlUPDATEUSUARIO = "UPDATE t_usuario SET idHandset = NULL WHERE idHandset = ?";
		String sqlCOUNT= "SELECT COUNT(1) FROM t_usuario WHERE idHandset= ?";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs=null;

		try {
			conexion = DataSourceBD.openConnection();
			conexion.setAutoCommit(false);
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idHandset);		
			int i = pstm.executeUpdate();
			pstm = conexion.prepareStatement(sqlCOUNT);
			pstm.setInt(1, idHandset);
			rs = pstm.executeQuery();
			int count=0;
			if(rs.next())	count = rs.getInt(1);
			if(count!=0){
				pstm = conexion.prepareStatement(sqlUPDATEUSUARIO);
				pstm.setInt(1, idHandset);
				i=pstm.executeUpdate();
				if(i!=0) estado=true;
				else	estado=false;
			}else
				estado=true;
			
			if(estado)	conexion.commit();
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
	/**
	 * 
	 * Método que modifica un handset
	 * @param HandsetDTO handset: objeto que contiene la información de un handset.
	 * @return boolean : true si se actualiza correctamente. Si ocurre un error, retornará false.
	 * 
	 */
	public boolean modificar(HandsetDTO handset) {
		String sql="UPDATE t_handset SET modelo=?, plataforma=?, versionAplicacion = ? , imagen = ? WHERE idHandset = ?";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setString(1, handset.getModelo());
			pstm.setString(2, handset.getPlataforma());
			pstm.setString(3, handset.getVersionAplicacion());
			pstm.setString(4, handset.getImagen());
			pstm.setInt(5, Integer.parseInt(handset.getIdHandset()));
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
	 * Método que registra en la BD un nuevo handset
	 * @param HandsetDTO handset: bean que contiene la información del handset
	 * @return boolean : retorna true si se registró correctamente. Si ocurrió algún error registrando, retornará false.
	 */
	public int registrar(HandsetDTO handset) {
		String sql="INSERT INTO t_handset (modelo, plataforma, versionAplicacion, imagen) VALUES (?,?,?,?)";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		int idHandset = 0;
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm.setString(1, handset.getModelo());
			pstm.setString(2, handset.getPlataforma());
			pstm.setString(3, handset.getVersionAplicacion());
			pstm.setString(4, handset.getImagen());
			int i = pstm.executeUpdate();
			estado=i!=0?true:false;
			if(estado){
				ResultSet llaves = pstm.getGeneratedKeys();
				while (llaves.next()) {
					idHandset = llaves.getInt(1);
				}
				llaves.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return idHandset;
	}

	@Override
	/**
	 * Método que obtiene una lista de Categorías de la BD.
	 * @return List<HandsetDTO> handset: Develve la lista de handsets de la BD. Si ocurre un error, devuelve null. Si no existe ningún handset, devolverá la lista con tamaño = 0
	 */
	public List<HandsetDTO> listar() {
		String sql="SELECT idHandset, modelo, plataforma, versionAplicacion, imagen FROM t_handset WHERE estado='1'";
		List<HandsetDTO> handsets=null;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			rs = pstm.executeQuery();
			handsets=new ArrayList<HandsetDTO>();
			while (rs.next()) {
				HandsetDTO handset=new HandsetDTO();
				handset.setIdHandset(rs.getString(1));
				handset.setModelo(rs.getString(2));
				String plataforma=rs.getString(3);
				if(plataforma.equals("1"))			plataforma="IDEN";
				else if(plataforma.equals("2"))		plataforma="J2ME";
				else if(plataforma.equals("3"))		plataforma="Blackberry";
				else if(plataforma.equals("4"))		plataforma="Android";
				else								plataforma="Sin asignar";
				handset.setPlataforma(plataforma);
				String versionAplicacion=rs.getString(4);
				if(versionAplicacion.equals("0"))		versionAplicacion="Solo SMS";
				else if(versionAplicacion.equals("1"))	versionAplicacion="SMS e Imagen";
				else									versionAplicacion="Sin asignar";
				String imagen = rs.getString(5);
				if(imagen.equals("1"))	imagen="Si soporta";
				else					imagen="No soporta";
				handset.setImagen(imagen);
				handset.setVersionAplicacion(versionAplicacion);
				handsets.add(handset);
			}
		} catch (SQLException e) {		
			handsets=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return handsets;
	}

	@Override
	/**
	 * Método que obtiene la información de un Handset
	 * @param idHandset: el id del cual se obtendrá la información
	 * @return HandsetDTO objeto con la información cargada obtenida de la BD, si no encuentra al handset, devolverá null
	 */
	public HandsetDTO obtener(int idHandset) {
		String sql="SELECT idHandset, modelo, plataforma, versionAplicacion, imagen FROM t_handset WHERE idHandset=?";
		HandsetDTO handset=null;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setInt(1, idHandset);
			rs = pstm.executeQuery();
			if (rs.next()) {
				handset=new HandsetDTO();
				handset.setIdHandset(rs.getString(1));
				handset.setModelo(rs.getString(2));
				handset.setPlataforma(rs.getString(3));
				handset.setVersionAplicacion(rs.getString(4));
				handset.setImagen(rs.getString(5));
			}
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return handset;
	}

}
