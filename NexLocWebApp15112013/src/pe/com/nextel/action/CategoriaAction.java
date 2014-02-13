package pe.com.nextel.action;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import pe.com.nextel.bean.CategoriaDTO;
import pe.com.nextel.dao.iface.AuditoriaDAO;
import pe.com.nextel.service.AuditoriaService;
import pe.com.nextel.service.BusinessDelegate;
import pe.com.nextel.service.CategoriaService;
import pe.com.nextel.util.LoggerUtil;
//import org.apache.log4j.Logger;

/**
 * @author Devos Inc.
 * 
 * Clase que recepciona las solicitudes enviadas desde la interfaz web, para el manejo de datos de las categorias.
 * 
 */

public class CategoriaAction {
	
	//********************** Variables *************************//
	
		private CategoriaDTO categoria;
		private List<CategoriaDTO> categorias;
		private String tipoRpta;														//Tipo de Mensaje que se le devuelve a la interfaz (OK o info)
		private String mensaje;															//Mensaje que se le devuelve a la interfaz
		CategoriaService categoriaService=BusinessDelegate.getCategoriaService();		//Instancia del servicio de Categorias para acceder a la BD
		AuditoriaService auditoriaService=BusinessDelegate.getAuditoriaService();
		Logger logger = LoggerUtil.getInstance();
//		Logger logger = Logger.getLogger(CategoriaAction.class.getName());
		
		//********************** Setter & Getter *************************//
		
		
		
		public String getMensaje() {
			return mensaje;
		}


		public CategoriaDTO getCategoria() {
			return categoria;
		}


		public void setCategoria(CategoriaDTO categoria) {
			this.categoria = categoria;
		}


		public List<CategoriaDTO> getCategorias() {
			return categorias;
		}


		public void setCategorias(List<CategoriaDTO> categorias) {
			this.categorias = categorias;
		}


		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}

		public String getTipoRpta() {
			return tipoRpta;
		}

		public void setTipoRpta(String tipoRpta) {
			this.tipoRpta = tipoRpta;
		}

		
		//********************** Metodos *************************//
		
		/**
		 * Metodo que obtiene una lista de todos las categoris
		 */
		public String listar(){
			logger.info("INICIO ListarCategorias");
			setCategorias(categoriaService.listar());
			if(getCategorias()==null){
				setTipoRpta("ERROR");
				setMensaje("Hubo un error obteniendo la lista de categorias. Por favor, reintente.");
//				logger.error("Ocurrio un info devolviendo categorias.");
				logger.info("Ocurrio un info devolviendo categorias.");

			}else{
				setTipoRpta("OK");
				logger.info("OK Se devolvieron las categorias correctamente");
			}
			logger.info("FIN ListarCategorias");
			return "listar";
		}
		
		/**
		 * Metodo que registra la categori dada
		 */
		public String registrar(){
			logger.info("INICIO Se solicito registrarCategoria");
			HttpServletRequest request =  ServletActionContext.getRequest();
	        HttpSession session = request.getSession(true); 
			int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
			CategoriaDTO categoria = getCategoria();
			if(categoria.getCategoria()==null || categoria.getCategoria().isEmpty()){
				setTipoRpta("ERROR");
				setMensaje("Por favor, ingrese una categoria valida");
//				logger.warn("Se ingreso una categoria invalida");
				logger.info("Se ingreso una categoria invalida");

			} else if (categoria.getCategoria().length() > 50) {
				setTipoRpta("ERROR");
				setMensaje("El nombre de la categoria no debe ser mayor a 50 caracteres");
				logger.info("Se ingreso una categoria con mas de 50 caracteres");
//				logger.warn("Se ingreso una categoria con mas de 50 caracteres");

			}else{
				int idCategoria=categoriaService.registrar(categoria);
				
				if(idCategoria!=0){
					setTipoRpta("OK");
					auditoriaService.registrar("t_categoria", idUsuario, idCategoria, AuditoriaDAO.REGISTRAR);
					logger.info("OK Se registro correctamente la categoria en BD");
				}else{
					setTipoRpta("ERROR");
					setMensaje("Hubo un error registrado la categoria, por favor reintente.");
//					logger.error("Ocurrio un error registrando la categoria en BD");
					logger.info("Ocurrio un error registrando la categoria en BD");

				}
			}
			logger.info("FIN RegistrarCategoria");
			return "registrar";
		}
		
		
		/**
		 * Metodo que modifica la categori dada.
		 */
		public String modificar(){
			logger.info("INICIO se solicito ModificarCategoria");
			HttpServletRequest request =  ServletActionContext.getRequest();
	        HttpSession session = request.getSession(true); 
			int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
			CategoriaDTO categoria = getCategoria();
			if(categoria.getIdCategoria()==null || categoria.getCategoria()==null){
				setTipoRpta("ERROR");
				setMensaje("La informacion que se desea modificar no es correcta.");
//				logger.warn("El IDCategoria o la categoria recibida son invalidas");
				logger.info("El IDCategoria o la categoria recibida son invalidas");

			}else if (categoria.getCategoria().length() > 15) {
				setTipoRpta("ERROR");
				setMensaje("El nombre de la categoria no debe ser mayor a 15 caracteres");
				logger.info("El nombre de la categoria es mayor a 15 caracteres");
//				logger.warn("El nombre de la categoria es mayor a 15 caracteres");

			}else{
				boolean estado=categoriaService.modificar(categoria);
				if(estado){
					setTipoRpta("OK");
					auditoriaService.registrar("t_categoria", idUsuario, Integer.parseInt(categoria.getIdCategoria()), AuditoriaDAO.ACTUALIZAR);
					logger.info("OK Se registro la categoria en BD");
				}else{
					setTipoRpta("info");
					setMensaje("Hubo un error modificando la categoria, por favor reintente.");
					logger.info("Ocurrio un error registrando la categoria en BD");
//					logger.error("Ocurrio un error registrando la categoria en BD");

				}
			}
			logger.info("FIN ModificarCategoria");
			return "modificar";
		}
		
		/**
		 * Metodo que elimina la categori dada
		 */
		public String eliminar(){
			logger.info("INICIO EliminarCategoria");
			HttpServletRequest request =  ServletActionContext.getRequest();
	        HttpSession session = request.getSession(true); 
			int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
			int id = Integer.parseInt(getCategoria().getIdCategoria());
			boolean resultado = categoriaService.eliminar(id);
			if(resultado){
				setTipoRpta("OK");
				setMensaje("Se elimino la categoria correctamente");
				auditoriaService.registrar("t_categoria", idUsuario, id, AuditoriaDAO.ELIMINAR);
				logger.info("OK se elimino la categoria de la BD");
			}else{
				setTipoRpta("ERROR");
				setMensaje("Hubo un info accediendo a la BD. Por favor, reintente.");
				logger.info("Ocurrio un error eliminando la categoria de la BD");
//				logger.error("Ocurrio un error eliminando la categoria de la BD");

			}
			logger.info("FIN EliminarCategoria");
			return "eliminar";
		}
		
		/**
		 * Metodo que obtiene la informacion de una categori
		 * 
		 */
		public String obtener(){
			logger.info("INICIO ObtenerCategoria");
			int id = Integer.parseInt(getCategoria().getIdCategoria());
			setCategoria(categoriaService.obtener(id));
			if(getCategoria()==null){
				setTipoRpta("ERROR");
				setMensaje("Hubo un error obteniendo la informacion de la categoria. Por favor, reintente.");
				logger.info("Ocurrio un error devolviendo una categoria");
//				logger.error("Ocurrio un error devolviendo una categoria");

			}else{
				setTipoRpta("OK");
				logger.info("OK Se devolvio la categoria correctamente");
			}
			logger.info("FIN ObtenerCategoria");
			return "obtener";
		}

}
