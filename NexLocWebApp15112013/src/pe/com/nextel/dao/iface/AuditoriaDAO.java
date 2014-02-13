package pe.com.nextel.dao.iface;

public interface AuditoriaDAO {
	
	public static final String REGISTRAR = "0";
	public static final String ACTUALIZAR = "1";
	public static final String ELIMINAR = "2"; 
	
	public abstract boolean registrar (String tabla, int idUsuario, int idRegistro, String accion);

}
