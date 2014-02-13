package pe.com.nextel.service;

import java.util.List;

import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;

/**
 * @author Devos Inc.
 */

public interface UsuarioService {
	
	public abstract UsuarioDTO autenticar(String numero);
	public abstract UsuarioDTO autenticarAdmins(String numero, String password);
	public abstract boolean modificar(UsuarioDTO usuario);
	public abstract boolean perfil(UsuarioDTO usuario);
	public abstract List<UsuarioDTO> listar(int idCuenta);
	public abstract List<UsuarioDTO> listarMonitoreados(int idMonitor);
	public abstract boolean asignarMonitoreados(int idMonitor, List<UsuarioDTO> monitoreados);
	public abstract List<UsuarioDTO> listarNoMonitoreados(int idMonitor, List<UsuarioDTO> monitoreados);
	public abstract TransaccionDTO localizar (UsuarioDTO usuario, int idUsuario, LogDTO logDTO);
	public abstract UsuarioDTO obtener(int idUsuario);
	public abstract String getOrigen(String numero);
	public abstract void actualizarTipo(String numero, String tipo);
	public abstract String getTimestamp(String numero);
	public abstract String getEtiqueta(String numero);

}
