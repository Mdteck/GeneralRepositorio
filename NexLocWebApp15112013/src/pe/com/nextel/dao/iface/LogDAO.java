package pe.com.nextel.dao.iface;

import java.util.List;

import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.TransaccionDTO;

public interface LogDAO {

	public abstract LogDTO registrarLog(int operacion, int idUsuario, int id);
	
	public abstract int registrarLogDetalle(int idLog, int idMensaje,int idComponenteEnvio, String descripcionEnvio,int idComponenteRespuesta, String descripcionRespuesta, int flagExito, String fechaEnvio, String fechaRespuesta);
	
	public abstract int registrarLogError(int idLog,  String descripcion);
	
	public abstract boolean registrarLogDetalleNumero(TransaccionDTO transaccion);
	
	public abstract int updateLogExito(int idLog, int flagExito);
}
