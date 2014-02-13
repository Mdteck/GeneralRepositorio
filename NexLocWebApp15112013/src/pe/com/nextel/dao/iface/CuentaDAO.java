package pe.com.nextel.dao.iface;

import pe.com.nextel.bean.CuentaDTO;
import pe.com.nextel.bean.HorarioDTO;

public interface CuentaDAO {
	
	public abstract HorarioDTO obtenerHorario(int idCuenta);
	public abstract boolean registrarHorario(CuentaDTO cuenta);

}
