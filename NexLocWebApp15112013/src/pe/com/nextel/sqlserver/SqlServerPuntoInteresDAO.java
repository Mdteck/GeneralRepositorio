package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import org.apache.log4j.Logger;
import java.util.logging.Logger;

import pe.com.nextel.bean.CategoriaDTO;
import pe.com.nextel.bean.CoordenadaDTO;
import pe.com.nextel.bean.ExtentDTO;
import pe.com.nextel.bean.PuntoInteresDTO;
import pe.com.nextel.bean.UbigeoDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.iface.PuntoInteresDAO;
import pe.com.nextel.util.DataSourceBD;
import pe.com.nextel.util.LoggerUtil;

/**
 * @author Devos Inc.
 * 
 * Clase que accede y maneja los datos del motos SqlServer para las funcionalidades de la intefaz PuntosInteresDAO.
 * 
 */

public class SqlServerPuntoInteresDAO implements PuntoInteresDAO {
	
	Logger logger = LoggerUtil.getInstance();
//	Logger logger = Logger.getLogger(SqlServerPuntoInteresDAO.class.getName());

	@Override
	/**
	 * MÃ©todo que elimina un punto de interÃ©s. Se actualizarÃ¡ el estado del punto de interÃ©s con estado = 0;
	 * @param idPuntoInteres: cadena que contiene el id del usuario a eliminar.
	 * @return boolean : si es true, se eliminÃ³ correctamente. Sino, ocurriÃ³ un error en la eliminaciÃ³n (retorna false)
	 */
	public boolean eliminar(int idPuntoInteres) {
		String sql="UPDATE t_puntoInteres SET estado = '0' WHERE idPuntoInteres = ?";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setInt(1, idPuntoInteres);		
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
	 * MÃ©todo que modofica un punto de interÃ©s.
	 * @param PuntoInteresDTO puntoInteres: objeto que contiene toda la informaciÃ³n para modificar el punto de interÃ©s
	 * @return boolean : si es true, se modificÃ³ correctamente. Sino, ocurriÃ³ un error en la modificaciÃ³n (retorna false)
	 */
	public boolean modificar(PuntoInteresDTO puntoInteres) {
		String sql="UPDATE t_puntoInteres SET nombre = ?, longitud = ?, latitud = ?, direccion = ?, idCategoria = ?, distrito = ?, rutaImagen = ? WHERE idPuntoInteres = ?";
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);
			pstm.setString(1, puntoInteres.getNombre());	
			CoordenadaDTO coordenada = puntoInteres.getCoordenada();
			pstm.setString(2, coordenada.getLongitud());
			pstm.setString(3, coordenada.getLatitud());
			pstm.setString(4, puntoInteres.getDireccion());
			CategoriaDTO categoria=puntoInteres.getCategoria();
			pstm.setString(5, categoria.getIdCategoria());
			pstm.setString(6, puntoInteres.getDistrito());
			pstm.setString(7, puntoInteres.getRutaImagen());
			pstm.setString(8, puntoInteres.getIdPuntoInteres());
			
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
	 * MÃ©todo que registra un punto de interÃ©s. 
	 * @param PuntoInteresDTO puntoInteres: objeto que contiene toda la informaciÃ³n para registrar el punto de interÃ©s
	 * @return boolean : si es true, se registrÃ³ correctamente. Sino, ocurriÃ³ un error en el registro (retorna false)
	 */
	public int registrar(PuntoInteresDTO puntoInteres) {
		String sql="INSERT INTO t_puntoInteres (nombre, longitud, latitud, direccion, idCategoria, idUsuario, visibilidad, idCuenta, distrito, rutaImagen, fechaRegistro) VALUES (?,?,?,?,?,?,?,?,?,?,GETDATE())";		
		boolean estado=false;
		Connection conexion = null;
		PreparedStatement pstm = null;
		int idPuntoInteres = 0;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm.setString(1, puntoInteres.getNombre());	
			CoordenadaDTO coordenada = puntoInteres.getCoordenada();
			pstm.setString(2, coordenada.getLongitud());
			pstm.setString(3, coordenada.getLatitud());
			pstm.setString(4, puntoInteres.getDireccion());
			CategoriaDTO categoria=puntoInteres.getCategoria();
			pstm.setString(5, categoria.getIdCategoria());
			if(puntoInteres.getUsuario()!=null){
				if(puntoInteres.getUsuario().getIdUsuario()!=null)
					pstm.setInt(6, Integer.parseInt(puntoInteres.getUsuario().getIdUsuario()));
				else
					pstm.setNull(6, java.sql.Types.INTEGER);
			}else
				pstm.setNull(6, java.sql.Types.INTEGER);
			pstm.setInt(7, Integer.parseInt(puntoInteres.getVisibilidad()));
			if(puntoInteres.getUsuario()!=null){
				if(puntoInteres.getUsuario().getCuenta()!=null)
					pstm.setInt(8, Integer.parseInt(puntoInteres.getUsuario().getCuenta().getIdCuenta()));
				else
					pstm.setNull(8, java.sql.Types.INTEGER);
			}else
				pstm.setNull(8, java.sql.Types.INTEGER);
				
			
			pstm.setString(9, puntoInteres.getDistrito());
			pstm.setString(10, puntoInteres.getRutaImagen());//---
			int i = pstm.executeUpdate();
			if(i!=0) estado=true;
			if(estado){
				ResultSet llaves = pstm.getGeneratedKeys();
				while (llaves.next()) {
					idPuntoInteres = llaves.getInt(1);
				}
				llaves.close();
			}
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return idPuntoInteres;
	}

	@Override
	/**
	 * MÃ©todo que obtiene una lista de Puntos de InterÃ©s de la BD.
	 * @return List<PuntoInteresDTO> usuarios: Develve la lista de puntos de interÃ©s de la BD. Si ocurre un error, devuelve null. Si no existe ningun punto de interÃ©s, devolverÃ¡ la lista con tamaÃ±o = 0
	 */
	public List<PuntoInteresDTO> listar(int idCuenta, boolean showPrivados) {
		//Para puntos de interŽs de toda una cuenta
		String sqlPriv="";
		if(!showPrivados) sqlPriv="AND pi.visibilidad<>'2' ";
		String sql="SELECT pi.idPuntoInteres, pi.nombre,c.nombre ,pi.fechaRegistro, pi.visibilidad, pi.longitud, pi.latitud, pi.direccion,u.etiqueta,u.numero,pi.distrito, pi.rutaImagen " +
				"FROM t_puntoInteres pi LEFT JOIN t_categoria c ON  c.idCategoria=pi.idCategoria LEFT JOIN t_usuario u ON u.idUsuario = pi.idUsuario "+
				"WHERE pi.estado='1'  AND (pi.idCuenta = ?) "+sqlPriv+" ORDER BY 3, 2 ";
		List<PuntoInteresDTO> puntosInteres;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setInt(1, idCuenta);
			rs = pstm.executeQuery();
			puntosInteres=new ArrayList<PuntoInteresDTO>();
			while (rs.next()) {
				PuntoInteresDTO puntoInteres=new PuntoInteresDTO();
				puntoInteres.setIdPuntoInteres(rs.getString(1));
				puntoInteres.setNombre(rs.getString(2));
				CategoriaDTO categoria = new CategoriaDTO();
				String cat = rs.getString(3);
				if(cat==null)	cat="Sin Asignar";
				categoria.setCategoria(cat);
				puntoInteres.setCategoria(categoria);
				puntoInteres.setFechaRegistro(rs.getString(4));	
				puntoInteres.setVisibilidad(rs.getString(5));				
				CoordenadaDTO coordenada = new CoordenadaDTO();
				coordenada.setLongitud(rs.getString(6));
				coordenada.setLatitud(rs.getString(7));
				puntoInteres.setCoordenada(coordenada);
				puntoInteres.setDireccion(rs.getString(8));
				UsuarioDTO usuario = new UsuarioDTO();
				String eti = rs.getString(9);
				if(eti==null)	eti="Administrador";
				usuario.setEtiqueta(eti);
				String num = rs.getString(10);
				if(num==null)	num="";
				usuario.setNumero(num);
				puntoInteres.setUsuario(usuario);
				String distrito=rs.getString(11);
				if(distrito==null) puntoInteres.setDistrito("");
				else puntoInteres.setDistrito(distrito);		
				puntoInteres.setRutaImagen(rs.getString(12));
				puntosInteres.add(puntoInteres);
			}
		} catch (SQLException e) {		
			puntosInteres=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return puntosInteres;
	}
	
	@Override
	public List<PuntoInteresDTO> listar(int idCuenta, int idUsuario) {
		//MŽtodo para listar puntos de inter�s privados
		String sql="SELECT pi.idPuntoInteres, pi.nombre, (SELECT c.nombre FROM t_categoria c WHERE  c.idCategoria=pi.idCategoria), pi.fechaRegistro, pi.visibilidad, pi.longitud, pi.latitud, pi.direccion, pi.rutaImagen FROM t_puntoInteres pi " +
				"WHERE pi.estado='1' AND pi.idUsuario = ? AND pi.idCuenta = ? ORDER BY 3, 2";
		List<PuntoInteresDTO> puntosInteres;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setInt(1, idUsuario);
			pstm.setInt(2, idCuenta);
			rs = pstm.executeQuery();
			puntosInteres=new ArrayList<PuntoInteresDTO>();
			while (rs.next()) {
				PuntoInteresDTO puntoInteres=new PuntoInteresDTO();
				puntoInteres.setIdPuntoInteres(rs.getString(1));
				puntoInteres.setNombre(rs.getString(2));
				CategoriaDTO categoria = new CategoriaDTO();
				String cat = rs.getString(3);
				if(cat==null)	cat="Sin Asignar";
				categoria.setCategoria(cat);
				puntoInteres.setCategoria(categoria);
				puntoInteres.setFechaRegistro(rs.getString(4));	
				puntoInteres.setVisibilidad(rs.getString(5));
				CoordenadaDTO coordenada = new CoordenadaDTO();
				coordenada.setLongitud(rs.getString(6));
				coordenada.setLatitud(rs.getString(7));
				puntoInteres.setCoordenada(coordenada);
				puntoInteres.setDireccion(rs.getString(8));
				puntoInteres.setRutaImagen(rs.getString(9));
				puntosInteres.add(puntoInteres);
			}
		} catch (SQLException e) {		
			puntosInteres=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return puntosInteres;
	}
	
	@Override
	public List<PuntoInteresDTO> listar() {
		//MŽtodo para listar puntos de inter�s globales
		String sql="SELECT pi.idPuntoInteres, pi.nombre, (SELECT c.nombre FROM t_categoria c WHERE  c.idCategoria=pi.idCategoria), pi.fechaRegistro, pi.visibilidad, pi.longitud, pi.latitud, pi.direccion, pi.rutaImagen FROM t_puntoInteres pi " +
				"WHERE pi.estado='1' AND pi.idUsuario IS NOT NULL AND pi.idCuenta IS NULL ORDER BY 3, 2";
		List<PuntoInteresDTO> puntosInteres;
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			rs = pstm.executeQuery();
			puntosInteres=new ArrayList<PuntoInteresDTO>();
			while (rs.next()) {
				PuntoInteresDTO puntoInteres=new PuntoInteresDTO();
				puntoInteres.setIdPuntoInteres(rs.getString(1));
				puntoInteres.setNombre(rs.getString(2));
				CategoriaDTO categoria = new CategoriaDTO();
				String cat = rs.getString(3);
				if(cat==null)	cat="Sin Asignar";
				categoria.setCategoria(cat);
				puntoInteres.setCategoria(categoria);
				puntoInteres.setFechaRegistro(rs.getString(4));	
				puntoInteres.setVisibilidad(rs.getString(5));
				CoordenadaDTO coordenada = new CoordenadaDTO();
				coordenada.setLongitud(rs.getString(6));
				coordenada.setLatitud(rs.getString(7));
				puntoInteres.setCoordenada(coordenada);
				puntoInteres.setDireccion(rs.getString(8));
				puntoInteres.setRutaImagen(rs.getString(9));
				puntosInteres.add(puntoInteres);
			}
		} catch (SQLException e) {		
			puntosInteres=null;
			e.printStackTrace();
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return puntosInteres;
	}

	@Override
	public PuntoInteresDTO obtener(int idPuntoInteres) {
		String sql="SELECT pi.idPuntoInteres, pi.nombre, c.nombre, pi.fechaRegistro, pi.longitud, pi.latitud, pi.direccion, pi.rutaImagen, pi.visibilidad FROM t_puntoInteres pi left join t_categoria c on pi.idCategoria=c.idCategoria where pi.idPuntoInteres=?";
		PuntoInteresDTO puntoInteres=new PuntoInteresDTO();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setInt(1, idPuntoInteres);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				puntoInteres.setIdPuntoInteres(rs.getString(1));
				puntoInteres.setNombre(rs.getString(2));
				CategoriaDTO categoria = new CategoriaDTO();
				String cat = rs.getString(3);
				if(cat==null)	cat="Sin Asignar";
				categoria.setCategoria(cat);
				puntoInteres.setCategoria(categoria);
				puntoInteres.setFechaRegistro(rs.getString(4));
				CoordenadaDTO coordenada = new CoordenadaDTO();
				coordenada.setLongitud(rs.getString(5));
				coordenada.setLatitud(rs.getString(6));
				puntoInteres.setCoordenada(coordenada);
				puntoInteres.setDireccion(rs.getString(7));
				puntoInteres.setRutaImagen(rs.getString(8));
				puntoInteres.setVisibilidad(rs.getString(9));
			}
		} catch (SQLException e) {		
			puntoInteres=null;
			e.printStackTrace();
			
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return puntoInteres;
	}


	@Override
	public List<PuntoInteresDTO> buscar(int idCategoria, String nombre, int idUsuario, String idDepartamento, String idProvincia, String idDistrito) {
		String operador="";
		if(idCategoria==0) 	operador="OR";
		else				operador="AND";
		String sqlSelectCuenta = "SELECT idCuenta FROM t_usuario WHERE idUsuario = ?";
		String sqlSelectMonitores = "SELECT idMonitor FROM t_monitorxmonitoreado WHERE idMonitoreado = ?";
		String sql = "SELECT p.idPuntoInteres, p.nombre, p.longitud, p.latitud, p.rutaImagen, p.direccion, (SELECT c.nombre FROM t_categoria c WHERE c.idCategoria = p.idCategoria), p.rutaImagen, p.visibilidad FROM t_puntoInteres p " +
				"WHERE (p.idCategoria = ? "+ operador +" p.nombre LIKE ?) AND p.estado = '1' AND ubigeo = ? ";
		String sqlGlobalYCuentaYUsuario=" AND ( (p.visibilidad = '1' AND p.idCuenta = ?) OR (p.visibilidad='2' AND p.idUsuario=?) OR (p.visibilidad='0') )";
		String sqlUsuario = " AND  p.idUsuario = ? ";
		List<PuntoInteresDTO> puntosInteres = new ArrayList<PuntoInteresDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs=null;
		List<Integer> idsPuntosInteres = new ArrayList<Integer>();
		String ubigeo = idDepartamento+idProvincia+idDistrito;
		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sqlSelectCuenta);
			pstm.setInt(1, idUsuario);
			rs=pstm.executeQuery();
			int idCuenta = 0;
			if(rs.next()) idCuenta = rs.getInt(1);
			pstm = conexion.prepareStatement(sql+sqlGlobalYCuentaYUsuario);					
			pstm.setInt(1, idCategoria);
			pstm.setString(2, "%"+nombre+"%");
			pstm.setString(3, ubigeo);
			pstm.setInt(4, idCuenta);
			pstm.setInt(5, idUsuario);
			rs = pstm.executeQuery();	
			
			while (rs.next()) {
				idsPuntosInteres.add(rs.getInt(1));
				PuntoInteresDTO puntoInteres = new PuntoInteresDTO();
				puntoInteres.setIdPuntoInteres(rs.getString(1));
				puntoInteres.setNombre(rs.getString(2));
				CoordenadaDTO coordenada = new CoordenadaDTO();
				coordenada.setLongitud(rs.getString(3));
				coordenada.setLatitud(rs.getString(4));
				puntoInteres.setCoordenada(coordenada);
				puntoInteres.setRutaImagen(rs.getString(5));
				puntoInteres.setDireccion(rs.getString(6));
				CategoriaDTO categoria = new CategoriaDTO();
				String cat = rs.getString(7);
				if(cat==null)	cat="Sin Asignar";
				categoria.setCategoria(cat);
				puntoInteres.setCategoria(categoria);
				puntoInteres.setRutaImagen(rs.getString(8));
				puntosInteres.add(puntoInteres);
			}
			pstm=conexion.prepareStatement(sqlSelectMonitores);
			pstm.setInt(1, idUsuario);
			List<Integer> idsMonitores = new ArrayList<Integer>();
			rs=pstm.executeQuery();
			while(rs.next()) idsMonitores.add(rs.getInt(1));
			for(int id : idsMonitores){
				pstm=conexion.prepareStatement(sql+sqlUsuario);
				pstm.setInt(1, idCategoria);
				pstm.setString(2, "%"+nombre+"%");
				pstm.setString(3, ubigeo);
				pstm.setInt(4, id);
				rs=pstm.executeQuery();
				while(rs.next()){
					int idPuntoInteres = rs.getInt(1);
					if(idsPuntosInteres.contains(idPuntoInteres))	continue;
					idsPuntosInteres.add(rs.getInt(1));
					PuntoInteresDTO puntoInteres = new PuntoInteresDTO();
					puntoInteres.setIdPuntoInteres(rs.getString(1));
					puntoInteres.setNombre(rs.getString(2));
					CoordenadaDTO coordenada = new CoordenadaDTO();
					coordenada.setLongitud(rs.getString(3));
					coordenada.setLatitud(rs.getString(4));
					puntoInteres.setCoordenada(coordenada);
					puntoInteres.setRutaImagen(rs.getString(5));
					puntoInteres.setDireccion(rs.getString(6));
					CategoriaDTO categoria = new CategoriaDTO();
					String cat = rs.getString(7);
					if(cat==null)	cat="Sin Asignar";
					puntoInteres.setCategoria(categoria);
					puntoInteres.setRutaImagen(rs.getString(8));
					puntoInteres.setVisibilidad(rs.getString(9));
					puntosInteres.add(puntoInteres);
					
				}
			}
			
		} catch (SQLException e) {	
			puntosInteres=null;
			e.printStackTrace();
		}finally{
			DataSourceBD.closeConnection(conexion);
		}
		return puntosInteres;
	}

	@Override
	public List<PuntoInteresDTO> buscar(int idUsuario) {
		String sqlSelectAdmin = "SELECT idCuenta FROM t_usuario WHERE idUsuario = ?";
		String sqlSelectMonitores = "SELECT idMonitor FROM t_monitorxmonitoreado WHERE idMonitoreado = ?";
		String sql = "SELECT p.idPuntoInteres, p.nombre, p.longitud, p.latitud, p.rutaImagen, p.direccion, (SELECT c.nombre FROM t_categoria c WHERE c.idCategoria = p.idCategoria), p.rutaImagen, p.visibilidad FROM t_puntoInteres p " +
				"WHERE p.estado = '1'";
		String sqlGlobalYCuentaYUsuario=" AND ( (p.visibilidad = '1' AND p.idCuenta = ?) OR (p.visibilidad='2' AND p.idUsuario=?) OR (p.visibilidad='0') )";
		String sqlUsuario = " AND  p.idUsuario = ? ";
		List<PuntoInteresDTO> puntosInteres = new ArrayList<PuntoInteresDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs=null;
		List<Integer> idsPuntosInteres = new ArrayList<Integer>();
		try {
			conexion = DataSourceBD.openConnection();
			//obtenemos id de cuenta
			pstm = conexion.prepareStatement(sqlSelectAdmin);
			pstm.setInt(1, idUsuario);
			rs=pstm.executeQuery();
			int idCuenta = 0;
			if(rs.next()) idCuenta = rs.getInt(1);
			
			//obtenemos los puntos de interes de la cuenta
			pstm = conexion.prepareStatement(sql+sqlGlobalYCuentaYUsuario);		
			pstm.setInt(1, idCuenta);
			pstm.setInt(2, idUsuario);
			rs = pstm.executeQuery();				
			while (rs.next()) {
				idsPuntosInteres.add(rs.getInt(1));
				PuntoInteresDTO puntoInteres = new PuntoInteresDTO();
				puntoInteres.setIdPuntoInteres(rs.getString(1));
				puntoInteres.setNombre(rs.getString(2));
				CoordenadaDTO coordenada = new CoordenadaDTO();
				coordenada.setLongitud(rs.getString(3));
				coordenada.setLatitud(rs.getString(4));
				puntoInteres.setCoordenada(coordenada);
				puntoInteres.setRutaImagen(rs.getString(5));
				puntoInteres.setDireccion(rs.getString(6));
				CategoriaDTO categoria = new CategoriaDTO();
				String cat = rs.getString(7);
				if(cat==null)	cat="Sin Asignar";
				categoria.setCategoria(cat);
				puntoInteres.setCategoria(categoria);
				puntoInteres.setRutaImagen(rs.getString(8));
				puntosInteres.add(puntoInteres);
			}
			
			//obtenemos puntos de interes de los monitoreados
			pstm=conexion.prepareStatement(sqlSelectMonitores);
			pstm.setInt(1, idUsuario);
			List<Integer> idsMonitores = new ArrayList<Integer>();
			rs=pstm.executeQuery();
			while(rs.next()) idsMonitores.add(rs.getInt(1));
			for(int id : idsMonitores){
				pstm=conexion.prepareStatement(sql+sqlUsuario);
				pstm.setInt(1, id);
				rs=pstm.executeQuery();
				while(rs.next()){
					int idPuntoInteres = rs.getInt(1);
					if(idsPuntosInteres.contains(idPuntoInteres))	continue;
					idsPuntosInteres.add(rs.getInt(1));
					PuntoInteresDTO puntoInteres = new PuntoInteresDTO();
					puntoInteres.setIdPuntoInteres(rs.getString(1));
					puntoInteres.setNombre(rs.getString(2));
					logger.info("Punto interes monitoreado:"+puntoInteres.getNombre());
					CoordenadaDTO coordenada = new CoordenadaDTO();
					coordenada.setLongitud(rs.getString(3));
					coordenada.setLatitud(rs.getString(4));
					puntoInteres.setCoordenada(coordenada);
					puntoInteres.setRutaImagen(rs.getString(5));
					puntoInteres.setDireccion(rs.getString(6));
					CategoriaDTO categoria = new CategoriaDTO();
					String cat = rs.getString(7);
					if(cat==null)	cat="Sin Asignar";
					categoria.setCategoria(cat);
					puntoInteres.setCategoria(categoria);
					puntoInteres.setRutaImagen(rs.getString(8));
					puntoInteres.setVisibilidad(rs.getString(9));
					puntosInteres.add(puntoInteres);
					
				}
			}
		} catch (SQLException e) {	
			puntosInteres=null;
			e.printStackTrace();
		}finally{
			DataSourceBD.closeConnection(conexion);
		}
		return puntosInteres;
	}

	@Override
	public List<PuntoInteresDTO> mapa(ExtentDTO limites, int id, int modo) {
		String sqlMONITORES = "SELECT idMonitor FROM t_monitorxmonitoreado WHERE idMonitoreado = ?";
		String sqlGLOBAL="SELECT p.idPuntoInteres, p.nombre, p.longitud, p.latitud, p.rutaImagen, (SELECT c.nombre FROM t_categoria c WHERE c.idCategoria=p.idCategoria), p.direccion, p.distrito, p.visibilidad FROM t_puntoInteres p " +
				"WHERE (p.longitud BETWEEN ? AND ?) AND (p.latitud BETWEEN ? AND ? ) AND p.estado = '1' AND ";
		String sqlCUENTA=" p.visibilidad = '1' AND p.idCuenta = ? ";
		String sqlGLOBALES=" p.visibilidad = '0' ";
		
		String sqlOBJETIVO="p.visibilidad = '2' AND p.idUsuario = ? ";
		List<PuntoInteresDTO> puntosInteres = new ArrayList<PuntoInteresDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs=null;
		
		
		try {
			conexion = DataSourceBD.openConnection();
			if(modo==0){//cuenta
				//Si se necesitan los puntos de interï¿½s de la cuenta
				pstm = conexion.prepareStatement(sqlGLOBAL + sqlCUENTA);	
				pstm.setString(1, limites.getXmax().toString()); // longitud mayor
				pstm.setString(2, limites.getXmin().toString()); // longitud menor
				pstm.setString(3, limites.getYmax().toString()); // latitud mayor
				pstm.setString(4, limites.getYmin().toString()); // latitud mayor
				pstm.setInt(5, id);
				rs=pstm.executeQuery();
				while (rs.next()) {
					PuntoInteresDTO puntoInteres = new PuntoInteresDTO();
					puntoInteres.setIdPuntoInteres(rs.getString(1));
					puntoInteres.setNombre(rs.getString(2));
					//logger.info("pi cuenta: "+puntoInteres.getNombre());
					CoordenadaDTO coordenada = new CoordenadaDTO();
					coordenada.setLongitud(rs.getString(3));
					coordenada.setLatitud(rs.getString(4));
					puntoInteres.setCoordenada(coordenada);
					puntoInteres.setRutaImagen(rs.getString(5));
					CategoriaDTO categoria = new CategoriaDTO();
					String cat = rs.getString(6);
					if(cat==null)	cat="Sin Asignar";
					categoria.setCategoria(cat);
					puntoInteres.setCategoria(categoria);
					puntoInteres.setDireccion(rs.getString(7));
					puntoInteres.setDistrito(rs.getString(8));
					puntoInteres.setVisibilidad(rs.getString(9));
					puntosInteres.add(puntoInteres);
				}
			}else if(modo==1){//usuario
				//Si se necesitan los puntos de interï¿½s del objetivo (no incluye globales ni privados a la cuenta)
				List<String> idMonitores = new ArrayList<String>();
				pstm = conexion.prepareStatement(sqlMONITORES);
				pstm.setInt(1, id);
				rs=pstm.executeQuery();
				while(rs.next()) idMonitores.add(rs.getString(1));
				
				for(String idMonitor : idMonitores){
					pstm = conexion.prepareStatement(sqlGLOBAL + sqlOBJETIVO);
					pstm.setString(1, limites.getXmax().toString()); // longitud mayor
					pstm.setString(2, limites.getXmin().toString()); // longitud menor
					pstm.setString(3, limites.getYmax().toString()); // latitud mayor
					pstm.setString(4, limites.getYmin().toString()); // latitud mayor
					pstm.setInt(5, Integer.parseInt(idMonitor));
					
					rs=pstm.executeQuery();
					while (rs.next()) {
						PuntoInteresDTO puntoInteres = new PuntoInteresDTO();
						puntoInteres.setIdPuntoInteres(rs.getString(1));
						puntoInteres.setNombre(rs.getString(2));
						//logger.info("pi usuario: "+puntoInteres.getNombre());
						CoordenadaDTO coordenada = new CoordenadaDTO();
						coordenada.setLongitud(rs.getString(3));
						coordenada.setLatitud(rs.getString(4));
						puntoInteres.setCoordenada(coordenada);
						puntoInteres.setRutaImagen(rs.getString(5));
						CategoriaDTO categoria = new CategoriaDTO();
						categoria.setCategoria(rs.getString(6));
						puntoInteres.setCategoria(categoria);
						puntoInteres.setDireccion(rs.getString(7));
						puntosInteres.add(puntoInteres);
					}
				}
				
			}
			else if(modo==2){//globales
				pstm = conexion.prepareStatement(sqlGLOBAL + sqlGLOBALES);	
				pstm.setString(1, limites.getXmax().toString()); // longitud mayor
				pstm.setString(2, limites.getXmin().toString()); // longitud menor
				pstm.setString(3, limites.getYmax().toString()); // latitud mayor
				pstm.setString(4, limites.getYmin().toString()); // latitud mayor
				rs=pstm.executeQuery();
				while (rs.next()) {
					PuntoInteresDTO puntoInteres = new PuntoInteresDTO();
					puntoInteres.setIdPuntoInteres(rs.getString(1));
					puntoInteres.setNombre(rs.getString(2));
					//logger.info("pi global: "+puntoInteres.getNombre());
					CoordenadaDTO coordenada = new CoordenadaDTO();
					coordenada.setLongitud(rs.getString(3));
					coordenada.setLatitud(rs.getString(4));
					puntoInteres.setCoordenada(coordenada);
					puntoInteres.setRutaImagen(rs.getString(5));
					CategoriaDTO categoria = new CategoriaDTO();
					String cat = rs.getString(6);
					if(cat==null)	cat="Sin Asignar";
					categoria.setCategoria(cat);
					puntoInteres.setCategoria(categoria);
					puntoInteres.setDireccion(rs.getString(7));
					puntoInteres.setDistrito(rs.getString(8));
					puntoInteres.setVisibilidad(rs.getString(9));
					puntosInteres.add(puntoInteres);
				}
			}

		} catch (SQLException e) {	
			puntosInteres=null;
			e.printStackTrace();
		}finally{
			DataSourceBD.closeConnection(conexion);
		}
		return puntosInteres;
	}

	@Override
	public List<UbigeoDTO> listarDepartamentos() {
		String sql = "SELECT idDepartamento, nombre FROM t_ubigeo WHERE idProvincia LIKE '00' AND idDistrito LIKE '00'";
		List<UbigeoDTO> departamentos = new ArrayList<UbigeoDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				UbigeoDTO u = new UbigeoDTO();
				u.setIdUbigeo(rs.getString(1));
				u.setNombre(rs.getString(2));
				departamentos.add(u);
			}
		} catch (SQLException e) {		
			departamentos=null;
			e.printStackTrace();
			
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return departamentos;
	}

	@Override
	public List<UbigeoDTO> listarProvincias(String departamento) {
		String sql = "SELECT idProvincia, nombre FROM t_ubigeo WHERE idDepartamento LIKE ? AND idDistrito LIKE '00' AND idProvincia <> '00'";
		List<UbigeoDTO> provincias = new ArrayList<UbigeoDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setString(1, departamento);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				UbigeoDTO u = new UbigeoDTO();
				u.setIdUbigeo(rs.getString(1));
				u.setNombre(rs.getString(2));
				provincias.add(u);
			}
		} catch (SQLException e) {		
			provincias=null;
			e.printStackTrace();
			
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return provincias;
	}

	@Override
	public List<UbigeoDTO> listarDistritos(String departamento, String provincia) {
		String sql = "SELECT idDistrito, nombre FROM t_ubigeo WHERE idDepartamento LIKE ? AND idProvincia LIKE ? ANd idDistrito <> '00'";
		List<UbigeoDTO> distritos = new ArrayList<UbigeoDTO>();
		Connection conexion = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexion = DataSourceBD.openConnection();
			pstm = conexion.prepareStatement(sql);	
			pstm.setString(1, departamento);
			pstm.setString(2, provincia);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				UbigeoDTO u = new UbigeoDTO();
				u.setIdUbigeo(rs.getString(1));
				u.setNombre(rs.getString(2));
				distritos.add(u);
			}
		} catch (SQLException e) {		
			distritos=null;
			e.printStackTrace();
			
		} finally{
			DataSourceBD.closeConnection(conexion);
		}
		return distritos;
	}


}
