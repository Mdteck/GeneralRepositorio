package pe.com.nextel.dao.iface;

import java.util.List;

import pe.com.nextel.bean.HandsetDTO;

/**
 * @author Devos Inc.
 */

public interface HandsetDAO {
	
	public abstract boolean eliminar (int idHandset);
	public abstract boolean modificar(HandsetDTO handset);
	public abstract int registrar(HandsetDTO handset);
	public abstract List<HandsetDTO> listar();
	public abstract HandsetDTO obtener(int idHandset);

}
