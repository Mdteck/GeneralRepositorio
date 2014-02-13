package pe.com.nextel.service;

import java.util.List;

import pe.com.nextel.bean.TrackingDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.TrackingDAO;

public class TrackingServiceImpl implements TrackingService {
	
	DAOFactory fabrica = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	TrackingDAO trackingDAO=fabrica.getTrackingDAO();

	@Override
	public int registrar(TrackingDTO tracking) {
		return trackingDAO.registrar(tracking);
	}

	@Override
	public boolean cancelar(TrackingDTO tracking) {
		return trackingDAO.cancelar(tracking);
	}

	@Override
	public TrackingDTO mapa(int idTracking) {
		return trackingDAO.mapa(idTracking);
	}

	@Override
	public List<TrackingDTO> listar(int idMonitor) {
		return trackingDAO.listar(idMonitor);
	}
	
	@Override
	public TrackingDTO obtener(int idTracking) {
		return trackingDAO.obtener(idTracking);
	}
	
	@Override
	public TrackingDTO obtenerFiltros(int idTracking, String fechaInicial, String fechaFinal, String horaInicio, String horaFin, String modo){
		return trackingDAO.obtenerFiltros(idTracking, fechaInicial, fechaFinal, horaInicio, horaFin, modo);
	}
	
	@Override
	public List<TrackingDTO> historial(int idMonitor) {
		return trackingDAO.historial(idMonitor);
	}

	@Override
	public List<TrackingDTO> filtrar(int idUsuario, String modo, String fechaInicial, String fechaFin) {
		return trackingDAO.filtrar(idUsuario, modo, fechaInicial, fechaFin);
	}

}
