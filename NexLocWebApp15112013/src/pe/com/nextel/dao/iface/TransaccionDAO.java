package pe.com.nextel.dao.iface;

import pe.com.nextel.bean.TransaccionDTO;

public interface TransaccionDAO {
	
	public abstract boolean registrar(TransaccionDTO transaccion, int idUsuario, int tipoTransaccion);

}
