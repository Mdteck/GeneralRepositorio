package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pe.com.nextel.bean.CategoriaDTO;
import pe.com.nextel.dao.iface.CategoriaDAO;
import pe.com.nextel.util.DataSourceBD;

public class SqlServerCategoriaDAO implements CategoriaDAO {

	@Override
	/**
	 * Método que obtiene una lista de Categorías de la BD.
	 * @return List<CategoriaDTO> categoria: Develve la lista de categorias de la BD. Si ocurre un error, devuelve null. Si no existe ninguna categoaia, devolverá la lista con tamaño = 0
	 */
	public List<CategoriaDTO> listar() {
		String sql="SELECT c.idCategoria, c.nombre, (SELECT COUNT(1) FROM t_puntoInteres p WHERE  p.idCategoria = c.idCategoria) FROM t_categoria c";
		List<CategoriaDTO> categorias=null;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			rs = pstm.executeQuery();
			categorias=new ArrayList<CategoriaDTO>();
			
			while (rs.next()) {
				CategoriaDTO categoria=new CategoriaDTO();
				categoria.setIdCategoria(rs.getString(1));
				categoria.setCategoria(rs.getString(2));
				categoria.setCantidadPuntoInteres(rs.getInt(3));
				categorias.add(categoria);
			}
		} catch (SQLException e) {		
			categorias=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return categorias;
	}

	@Override
	/**
	 * Método que registra en la BD una nueva categoría.
	 * @param CategoriaDTO categoria: bean que contiene la información de la categoría (Solo es necesario el atributo categoria de dicha clase)
	 * @return boolean : retorna true si se registró correctamente. Si ocurrió algún error registrando, retornará false.
	 */
	public int registrar(CategoriaDTO categoria) {
		String sql="INSERT INTO t_categoria (nombre) VALUES (?)";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		int idCategoria=0;
		
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm.setString(1, categoria.getCategoria());
			int i = pstm.executeUpdate();
			estado=i!=0?true:false;
			if(estado){
				ResultSet llaves = pstm.getGeneratedKeys();
				while (llaves.next()) {
					idCategoria = llaves.getInt(1);
				}
				llaves.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return idCategoria;
	}

	@Override
	/**
	 * 
	 * Método que modifica una categoría 
	 * @param CategoriaDTO categoria: objeto que contiene la información de una categoria, deberá tener por lo menos idCategoria y nombre
	 * @return boolean : true si se actualiza correctamente. Si ocurre un error, retornará false.
	 * 
	 */
	public boolean modificar(CategoriaDTO categoria) {
		String sql="UPDATE t_categoria SET nombre = ? WHERE idCategoria = ?";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setString(1, categoria.getCategoria());
			pstm.setInt(2, Integer.parseInt(categoria.getIdCategoria()));
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
	 * Método que elimina una categoria. Se actualizará el estado de la categoria con valor = 0;
	 * @param idCategoria: cadena que contiene el id de la categoría a eliminar.
	 * @return boolean : si es true, se eliminó correctamente. Sino, ocurrió un error en la eliminación (retorna false)
	 */
	public boolean eliminar(int idCategoria) {
		String sql="DELETE FROM t_categoria WHERE idCategoria = ?";
		String sqlUPDATEPUNTOSINTERES = "UPDATE t_puntoInteres SET idCategoria = NULL WHERE idCategoria = ?";
		String sqlCOUNT = "SELECT COUNT(1) FROM t_puntoInteres WHERE idCategoria = ?";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			int i = 0;
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sqlCOUNT);
			pstm.setInt(1, idCategoria);
			rs = pstm.executeQuery();
			int count=0;
			if(rs.next())	count = rs.getInt(1);
			if(count!=0){
				pstm=conexion.prepareStatement(sqlUPDATEPUNTOSINTERES);
				pstm.setInt(1, idCategoria);
				i=pstm.executeUpdate();
				if(i!=0) estado=true;
			}else{
				estado=true;
			}
			
			conexion.setAutoCommit(false);
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idCategoria);		
			i = pstm.executeUpdate();
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
	public CategoriaDTO obtener(int idCategoria) {
		String sql="SELECT idCategoria, nombre FROM t_categoria WHERE idCategoria=?";
		CategoriaDTO categoria=null;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setInt(1, idCategoria);
			rs = pstm.executeQuery();
			if (rs.next()) {
				 categoria=new CategoriaDTO();
				categoria.setIdCategoria(rs.getString(1));
				categoria.setCategoria(rs.getString(2));
			}
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return categoria;
	}

}
