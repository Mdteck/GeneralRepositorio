package pe.com.nextel.service;

import pe.com.nextel.bean.CuentaDTO;
import pe.com.nextel.bean.HorarioDTO;

public interface CuentaService {
	
	public abstract HorarioDTO obtenerHorario(int idCuenta);
	public abstract boolean registrarHorario(CuentaDTO cuenta);

}
