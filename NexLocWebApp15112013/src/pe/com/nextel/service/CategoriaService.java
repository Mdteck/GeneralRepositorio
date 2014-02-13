package pe.com.nextel.service;

import java.util.List;

import pe.com.nextel.bean.CategoriaDTO;

/**
 * @author Devos Inc.
 */

public interface CategoriaService {
	
	public abstract boolean modificar(CategoriaDTO categoria);
	public abstract boolean eliminar(int idCategoria);
	public abstract int registrar(CategoriaDTO categoria);
	public abstract List<CategoriaDTO> listar();
	public abstract CategoriaDTO obtener(int idCategoria);
}
