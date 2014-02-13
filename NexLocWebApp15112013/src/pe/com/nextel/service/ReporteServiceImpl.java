package pe.com.nextel.service;

import java.util.List;

import pe.com.nextel.bean.BloqueadosDTO;
import pe.com.nextel.bean.GeocercaDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.ReporteTransaccionDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.ReporteDAO;

public class ReporteServiceImpl implements ReporteService {
	
	DAOFactory fabrica = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	ReporteDAO reporteDAO=fabrica.getReporteDAO();
	


	@Override
	public List<PosicionDTO> localizacion(String fechaInicio, String fechaFin, int id, int modo, int idCuenta) {
		return reporteDAO.localizacion(fechaInicio, fechaFin, id, modo, idCuenta);
	}


	@Override
	public List<GeocercaDTO> geocerca(int id, boolean porIdUsuario) {
		return reporteDAO.geocerca(id, porIdUsuario);
	}


	@Override
	public List<BloqueadosDTO> bloqueados(String idCuenta, String numero, String etiqueta) {
		return reporteDAO.bloqueados(idCuenta, numero, etiqueta);
	}


	@Override
	public ReporteTransaccionDTO transacciones(String tecnologia, String fechaInicial, String fechaFin, String origen) {
		return reporteDAO.transacciones(tecnologia, fechaInicial, fechaFin, origen);
	}


	@Override
	public ReporteTransaccionDTO transaccionesPorHora(String tecnologia, String date, String hora, String origen) {
		return reporteDAO.transaccionesPorHora(tecnologia, date, hora, origen);
	}


	@Override
	public List<ReporteTransaccionDTO> transaccionesPorDia(String tecnologia, String dia, String origen) {
		return reporteDAO.transaccionesPorDia(tecnologia, dia, origen);
	}


	@Override
	public List<ReporteTransaccionDTO> transaccionesPorMes(String tecnologia, String mes, String origen) {
		return reporteDAO.transaccionesPorMes(tecnologia, mes, origen);
	}


	@Override
	public List<ReporteTransaccionDTO> transaccionesPorAnio(String tecnologia, String anio, String origen) {
		return reporteDAO.transaccionesPorAnio(tecnologia, anio, origen);
	}


	@Override
	public List<ReporteTransaccionDTO> transaccionesPorMinutos(String tecnologia, String date, String hora, String origen) {
		return reporteDAO.transaccionesPorMinutos(tecnologia, date, hora, origen);
	}


	@Override
	public List<ReporteTransaccionDTO> transaccionesPorSegundos(String tecnologia, String date, String hora, String origen) {
		return reporteDAO.transaccionesPorSegundos(tecnologia, date, hora, origen);
	}

}
