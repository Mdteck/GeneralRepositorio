package pe.com.nextel.dao.iface;

import java.util.List;

import pe.com.nextel.bean.ExtentDTO;
import pe.com.nextel.bean.PuntoInteresDTO;
import pe.com.nextel.bean.UbigeoDTO;

/**
 * @author Devos Inc.
 */

public interface PuntoInteresDAO {
	
	public abstract List<PuntoInteresDTO> buscar(int idCategoria, String nombre, int idUsuario, String idDepartamento, String idProvincia, String idDistrito);
	public abstract List<PuntoInteresDTO> buscar(int idUsuario);
	public abstract List<PuntoInteresDTO> mapa(ExtentDTO limites, int idCuenta, int modo);
	public abstract boolean eliminar (int idPuntoInteres);
	public abstract boolean modificar(PuntoInteresDTO puntoInteres);
	public abstract int registrar(PuntoInteresDTO puntoInteres);
	public abstract List<PuntoInteresDTO> listar(int idCuenta, boolean showPrivados);
	public abstract PuntoInteresDTO obtener(int idPuntoInteres);
	public abstract List<PuntoInteresDTO> listar(int idCuenta, int idUsuario);
	public abstract List<PuntoInteresDTO> listar();
	public abstract List<UbigeoDTO> listarDepartamentos();
	public abstract List<UbigeoDTO> listarProvincias(String departamento);
	public abstract List<UbigeoDTO> listarDistritos(String departamento, String provincia);

}
