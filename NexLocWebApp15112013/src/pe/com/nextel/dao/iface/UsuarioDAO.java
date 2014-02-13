package pe.com.nextel.dao.iface;

import java.util.List;

import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;

/**
 * @author Devos Inc.
 */

public interface UsuarioDAO {
	
	public abstract UsuarioDTO autenticar(String numero);
	public abstract boolean modificar(UsuarioDTO usuario);
	public abstract List<UsuarioDTO> listar(int idCuenta); 
	public abstract List<UsuarioDTO> listarMonitoreados(int idMonitor);
	public abstract boolean asignarMonitoreados(int idMonitor, List<UsuarioDTO> monitoreados);
	public abstract TransaccionDTO localizar(UsuarioDTO usuario, LogDTO idLog);
	public abstract UsuarioDTO obtener(int idUsuario);
	public abstract boolean perfil(UsuarioDTO usuario);
	public abstract String getOrigen(String numero);
	public abstract Object actualizarTipo(String numero, String tipo);
	public abstract UsuarioDTO autenticarAdmin(String numero, String password);
	public abstract String getTimestamp(String numero);
	public abstract String getEtiqueta(String numero);
	public abstract List<UsuarioDTO> listarNoMonitoreados(int idMonitor,
			List<UsuarioDTO> monitoreados);

}
