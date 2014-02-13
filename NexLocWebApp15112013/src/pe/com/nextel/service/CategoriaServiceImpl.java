package pe.com.nextel.service;

import java.util.List;

import pe.com.nextel.bean.CategoriaDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.CategoriaDAO;

/**
 * @author Devos Inc.
 */

public class CategoriaServiceImpl implements CategoriaService {
	
	DAOFactory fabrica = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	CategoriaDAO categoriaDAO=fabrica.getCategoriaDAO();

	@Override
	public boolean modificar(CategoriaDTO categoria) {
		return categoriaDAO.modificar(categoria);
	}

	@Override
	public boolean eliminar(int idCategoria) {
		return categoriaDAO.eliminar(idCategoria);
	}

	@Override
	public int registrar(CategoriaDTO categoria) {
		return categoriaDAO.registrar(categoria);
	}

	@Override
	public List<CategoriaDTO> listar() {
		return categoriaDAO.listar();
	}

	@Override
	public CategoriaDTO obtener(int idCategoria) {
		return categoriaDAO.obtener(idCategoria);
	}

}
