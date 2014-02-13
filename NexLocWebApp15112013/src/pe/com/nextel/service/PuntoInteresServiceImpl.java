package pe.com.nextel.service;

import java.util.List;

import pe.com.nextel.bean.ExtentDTO;
import pe.com.nextel.bean.PuntoInteresDTO;
import pe.com.nextel.bean.UbigeoDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.PuntoInteresDAO;

/**
 * @author Devos Inc.
 */

public class PuntoInteresServiceImpl implements PuntoInteresService {
	
	DAOFactory fabrica = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	PuntoInteresDAO puntoInteresDAO=fabrica.getPuntoInteresDAO();

	@Override
	public boolean modificar(PuntoInteresDTO puntoInteres) {
		return puntoInteresDAO.modificar(puntoInteres);
	}

	@Override
	public boolean eliminar(int idPuntoInteres) {
		return puntoInteresDAO.eliminar(idPuntoInteres);
	}

	@Override
	public int registrar(PuntoInteresDTO puntoInteres) {
		return puntoInteresDAO.registrar(puntoInteres);
	}

	@Override
	public List<PuntoInteresDTO> listar(int idCuenta, boolean showPrivados) {
		return puntoInteresDAO.listar(idCuenta, showPrivados);
	}

	@Override
	public PuntoInteresDTO obtener(int idPuntoInteres) {
		return puntoInteresDAO.obtener(idPuntoInteres);
	}


	@Override
	public List<PuntoInteresDTO> buscar(int idCategoria, String nombre, int idUsuario, String idDepartamento, String idProvincia, String idDistrito) {
		return puntoInteresDAO.buscar(idCategoria, nombre, idUsuario, idDepartamento, idProvincia, idDistrito);
	}
	@Override
	public List<PuntoInteresDTO> mapa(ExtentDTO limites, int idCuenta, int modo) {
		return puntoInteresDAO.mapa(limites, idCuenta, modo);
	}

	@Override
	public List<PuntoInteresDTO> buscar(int idUsuario) {
		return puntoInteresDAO.buscar(idUsuario);
	}

	@Override
	public List<PuntoInteresDTO> listar() {
		return puntoInteresDAO.listar();
	}

	@Override
	public List<PuntoInteresDTO> listar(int idCuenta, int idUsuario) {
		return puntoInteresDAO.listar(idCuenta, idUsuario);
	}

	@Override
	public List<UbigeoDTO> listarDepartamentos() {
		return puntoInteresDAO.listarDepartamentos();
	}

	@Override
	public List<UbigeoDTO> listarProvincias(String departamento) {
		return puntoInteresDAO.listarProvincias(departamento);
	}

	@Override
	public List<UbigeoDTO> listarDistritos(String departamento, String provincia) {
		return puntoInteresDAO.listarDistritos(departamento, provincia);
	}


}
