package pe.com.nextel.dao.iface;

import java.util.List;
import pe.com.nextel.bean.GrupoDTO;
import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;

/**
 * @author Devos Inc.
 */

public interface GrupoDAO {
	
	public abstract List<GrupoDTO> listar(int idMonitor);
	public abstract List<GrupoDTO> listarPorCuenta(int idCuenta);
	public abstract int registrar(GrupoDTO grupo);
	public abstract boolean modificar(GrupoDTO grupo);
	public abstract boolean eliminar(int idGrupo);
	public abstract boolean asignarGrupo(int idGrupo, List<UsuarioDTO> usuarios);
	public abstract GrupoDTO obtener(int idGrupo);
	public abstract TransaccionDTO localizar(List<UsuarioDTO> usuarios, LogDTO idLog);

}
