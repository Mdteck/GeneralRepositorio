package pe.com.nextel.dao.iface;

import java.util.List;

import pe.com.nextel.bean.CategoriaDTO;

public interface CategoriaDAO {
	
	public abstract List<CategoriaDTO> listar();
	public abstract int registrar(CategoriaDTO categoria);
	public abstract boolean modificar(CategoriaDTO categoria);
	public abstract boolean eliminar(int idCategoria);
	public abstract CategoriaDTO obtener(int idCategoria);

}
