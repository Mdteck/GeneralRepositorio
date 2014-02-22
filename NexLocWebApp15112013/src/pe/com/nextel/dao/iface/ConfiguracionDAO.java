package pe.com.nextel.dao.iface;

import pe.com.nextel.bean.ConfiguracionDTO;

public interface  ConfiguracionDAO {
	
	public abstract int registrar(ConfiguracionDTO config);
	public abstract ConfiguracionDTO obtener();
	public abstract String obtenerParametro(String param);

}
