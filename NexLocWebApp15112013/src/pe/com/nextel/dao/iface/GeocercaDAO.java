package pe.com.nextel.dao.iface;

import java.util.List;

import pe.com.nextel.bean.GeocercaDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.UsuarioDTO;

/**
 * @author Devos Inc.
 */

public interface GeocercaDAO {
	public abstract boolean eliminar (int idGeocerca);
	public abstract int registrar(GeocercaDTO geocerca);
	public abstract List<GeocercaDTO> listar(int idUsuario, int tipo);
	public abstract boolean asignarGeocerca(int idGeocerca, List<UsuarioDTO> usuarios);
	public abstract GeocercaDTO obtener(int idGeocerca);
	public abstract List<PosicionDTO> detalle(int idGeocerca);
	public abstract boolean modificar(GeocercaDTO geocerca);

}
