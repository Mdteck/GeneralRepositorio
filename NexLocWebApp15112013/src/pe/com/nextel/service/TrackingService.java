package pe.com.nextel.service;

import java.util.List;

import pe.com.nextel.bean.TrackingDTO;

public interface TrackingService {
	
	public abstract int registrar(TrackingDTO tracking);
	public abstract boolean cancelar(TrackingDTO tracking);
	public abstract TrackingDTO mapa(int idTracking);
	public abstract List<TrackingDTO> listar(int idMonitor);
	public abstract List<TrackingDTO> historial(int idMonitor);
	public abstract TrackingDTO obtener(int idTracking);
	public abstract TrackingDTO obtenerFiltros(int idTracking, String fechaInicial, String fechaFinal, String horaInicio, String horaFin, String modo);
	public abstract List<TrackingDTO> filtrar(int idUsuario, String modo, String fechaInicial, String fechaFin);
}
