package pe.com.nextel.dao.iface;

import java.util.List;

import pe.com.nextel.bean.BloqueadosDTO;
import pe.com.nextel.bean.GeocercaDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.ReporteTransaccionDTO;

public interface ReporteDAO {
	
	public static final int GRUPAL=2;
	public static final int INDIVIDUAL=1;
	public static final int TODOS=0;
	
	
	public abstract List<PosicionDTO> localizacion(String fechaInicio, String fechaFin, int id, int modo, int idCuenta);
	public abstract List<GeocercaDTO> geocerca(int id, boolean porIdUsuario);
	public abstract List<BloqueadosDTO> bloqueados(String idCuenta, String numero, String etiqueta);
	public abstract ReporteTransaccionDTO transacciones(String tecnologia, String fechaInicial, String fechaFin, String origen);
	public abstract ReporteTransaccionDTO transaccionesPorHora(String tecnologia, String date, String hora, String origen);
	public abstract List<ReporteTransaccionDTO> transaccionesPorDia(String tecnologia, String dia, String origen);
	public abstract List<ReporteTransaccionDTO> transaccionesPorMes(String tecnologia, String mes, String origen);
	public abstract List<ReporteTransaccionDTO> transaccionesPorAnio(String tecnologia, String anio, String origen);
	public abstract List<ReporteTransaccionDTO> transaccionesPorMinutos(String tecnologia, String date, String hora, String origen);
	public abstract List<ReporteTransaccionDTO> transaccionesPorSegundos(String tecnologia, String date, String hora, String origen);
}
