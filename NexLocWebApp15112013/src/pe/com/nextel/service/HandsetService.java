package pe.com.nextel.service;

import java.util.List;

/**
 * @author Devos Inc.
 */
import pe.com.nextel.bean.HandsetDTO;

public interface HandsetService {
	
	public abstract boolean modificar(HandsetDTO handset);
	public abstract boolean eliminar(int idHandset);
	public abstract int registrar(HandsetDTO handset);
	public abstract List<HandsetDTO> listar();
	public abstract HandsetDTO obtener(int idHandset);

}
