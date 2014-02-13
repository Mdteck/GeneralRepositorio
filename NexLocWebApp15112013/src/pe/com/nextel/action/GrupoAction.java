package pe.com.nextel.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import pe.com.nextel.bean.CoordenadaDTO;
import pe.com.nextel.bean.ExtentDTO;
import pe.com.nextel.bean.GrupoDTO;
import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.iface.AuditoriaDAO;
import pe.com.nextel.service.AuditoriaService;
import pe.com.nextel.service.BusinessDelegate;
import pe.com.nextel.service.GrupoService;
import pe.com.nextel.service.LogService;
import pe.com.nextel.service.UsuarioService;
import pe.com.nextel.util.LoggerUtil;
import pe.com.nextel.util.MailUtil;
import pe.com.nextel.util.MapUtil;
import pe.com.nextel.util.ParserUtil;
import pe.com.nextel.util.PropertyUtil;

/**
 * @author Devos Inc.
 * 
 * Clase que recepciona las solicitudes enviadas desde la interfaz web, para el manejo de datos de los grupos.
 * 
 */

public class GrupoAction {
	
	//********************** Variables *************************//
	
			private GrupoDTO grupo;
			private CoordenadaDTO centro;
			private List<GrupoDTO> grupos;
			private UsuarioDTO usuario;
			private List<PosicionDTO> posiciones;
			private String tipoRpta;														//Tipo de Mensaje que se le devuelve a la interfaz (OK o ERROR)
			private String mensaje;															//Mensaje que se le devuelve a la interfaz
			GrupoService grupoService=BusinessDelegate.getGrupoService();					//Instancia del servicio de Grupos para acceder a la BD
			AuditoriaService auditoriaService=BusinessDelegate.getAuditoriaService();
			UsuarioService usuarioService=BusinessDelegate.getUsuarioService();
			LogService logService=BusinessDelegate.getLogService();
			
			private List<UsuarioDTO> usuariosBloqueados;
			private List<PosicionDTO> usuariosErrorLocalizacion;
			private String filtroUsuario;
			Logger logger = LoggerUtil.getInstance();
			Logger loggerError = LoggerUtil.getInstanceError();
//			Logger logger = Logger.getLogger(GrupoAction.class.getName());
//			Logger loggerError = Logger.getLogger("LocHandler");
			
			//********************** Setter & Getter *************************//
			
			
			
			public String getMensaje() {
				return mensaje;
			}

			public CoordenadaDTO getCentro() {
				return centro;
			}

			public void setCentro(CoordenadaDTO centro) {
				this.centro = centro;
			}

			public UsuarioDTO getUsuario() {
				return usuario;
			}


			public void setUsuario(UsuarioDTO usuario) {
				this.usuario = usuario;
			}


			public List<PosicionDTO> getPosiciones() {
				return posiciones;
			}


			public void setPosiciones(List<PosicionDTO> posiciones) {
				this.posiciones = posiciones;
			}


			public List<UsuarioDTO> getUsuariosBloqueados() {
				return usuariosBloqueados;
			}

			public void setUsuariosBloqueados(List<UsuarioDTO> usuariosBloqueados) {
				this.usuariosBloqueados = usuariosBloqueados;
			}

			public List<PosicionDTO> getUsuariosErrorLocalizacion() {
				return usuariosErrorLocalizacion;
			}

			public void setUsuariosErrorLocalizacion(
					List<PosicionDTO> usuariosErrorLocalizacion) {
				this.usuariosErrorLocalizacion = usuariosErrorLocalizacion;
			}

			public GrupoDTO getGrupo() {
				return grupo;
			}



			public void setGrupo(GrupoDTO grupo) {
				this.grupo = grupo;
			}



			public List<GrupoDTO> getGrupos() {
				return grupos;
			}



			public void setGrupos(List<GrupoDTO> grupos) {
				this.grupos = grupos;
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
			
			public String getFiltroUsuario() {
				return filtroUsuario;
			}

			public void setFiltroUsuario(String filtroUsuario) {
				this.filtroUsuario = filtroUsuario;
			}

			
			//********************** Metodos *************************//

			/**
			 * Metodo que obtiene una lista de todos las categorÃ­as
			 */
			public String listar(){
				logger.info("INICIO ListarGrupo");
				if (getFiltroUsuario() == null) {
					int idMonitor = Integer.parseInt(getUsuario().getIdUsuario());
					setGrupos(grupoService.listar(idMonitor));
					logger.info("OK se listo por idMonitor");
				}else {
					int idCuenta = Integer.parseInt(getUsuario().getCuenta().getIdCuenta());
					setGrupos(grupoService.listarPorCuenta(idCuenta));
					logger.info("OK se listo por idCuenta");
				}
				if(getGrupos()==null){
					setTipoRpta("ERROR");
					setMensaje("Hubo un error obteniendo la lista de grupos. Por favor, reintente.");
					logger.info("Error Obteniendo lista de grupos de BD");
//					logger.error("Error Obteniendo lista de grupos de BD");
				}else{
					setTipoRpta("OK");
					logger.info("OK se obtuvo lista de grupos de BD");
				}
				logger.info("FIN ListarGrupo");
				return "listar";
			}
			
			/**
			 * Metodo que registra la categorÃ­a dada
			 */
			public String registrar(){
				logger.info("INICIO RegistrarGrupo");
				HttpServletRequest request =  ServletActionContext.getRequest();
		        HttpSession session = request.getSession(true); 
				int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
				GrupoDTO grupo = getGrupo();
				// validaciones
				if (grupo.getNombre().isEmpty() || grupo.getUsuarios() == null) {
					setTipoRpta("ERROR");
					setMensaje("Debe completar todos los campos.");
//					logger.warn("Campos de registro incompletos");
					logger.info("Campos de registro incompletos");
					logger.info("FIN RegistrarGrupo");
					return "registrar";					
				}
				if (grupo.getNombre().length() > 20) {
					setTipoRpta("ERROR");
					setMensaje("El nombre del grupo no debe ser mayor a 20 caracteres.");
					logger.info("FIN RegistrarGrupo");
					return "registrar";					
				}
				
				int cantidadUsuarios = grupo.getUsuarios().size();
				int maxUsuarioxGrupo=Integer.parseInt(PropertyUtil.readProperty("MAX_USERSXGRUPO"));
				if(cantidadUsuarios>maxUsuarioxGrupo){
					setTipoRpta("ERROR");
					setMensaje("Solo se pueden asignar un maximo de "+maxUsuarioxGrupo+ " para este grupo.");
					logger.info("Los usuarios asignados exceden el maximo permitido");
//					logger.warn("Los usuarios asignados exceden el maximo permitido");
					logger.info("FIN RegistrarGrupo");
					return "registrar";
				}
				int id=grupoService.registrar(grupo);
				logger.info("OK Se registro grupo en BD");
				boolean estado = grupoService.asignarGrupo(id, grupo.getUsuarios());
				if(estado){
					setTipoRpta("OK");
					auditoriaService.registrar("t_grupo", idUsuario, id, AuditoriaDAO.REGISTRAR);
					logger.info("OK se asignaron usuarios al grupo en BD");
				}
				else{
					setTipoRpta("ERROR");
					setMensaje("Hubo un error registrado el grupo, por favor reintente.");
					logger.info("Error registrando grupo en BD");
//					logger.error("Error registrando grupo en BD");
				}
				logger.info("FIN RegistrarGrupo");
				return "registrar";
			}
			
			
			/**
			 * Metodo que modifica la categorÃ­a dada.
			 */
			public String modificar(){
				logger.info("INICIO ModificarGrupo");
				HttpServletRequest request =  ServletActionContext.getRequest();
		        HttpSession session = request.getSession(true); 
				int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
				GrupoDTO grupo = getGrupo();
				if(grupo.getUsuarios().size()==0){
					setTipoRpta("ERROR");
					setMensaje("Por favor, asigne por lo menos un usuario.");
//					logger.warn("No se recibieron usuarios para asignar");
					logger.info("No se recibieron usuarios para asignar");
					logger.info("FIN ModificarGrupo");
					return "modificar";	
				}
				if (grupo.getNombre().length() > 20) {
					setTipoRpta("ERROR");
					setMensaje("El nombre del grupo no debe ser mayor a 20 caracteres.");
//					logger.warn("El nombre del grupo excede los 20 caracteres");
					logger.info("El nombre del grupo excede los 20 caracteres");
					logger.info("FIN ModificarGrupo");
					return "modificar";					
				}

				boolean estado=grupoService.modificar(grupo);
				int id = Integer.parseInt(grupo.getIdGrupo());
				estado = grupoService.asignarGrupo(id, grupo.getUsuarios());
				if(estado){
					setTipoRpta("OK");
					auditoriaService.registrar("t_grupo", idUsuario, id, AuditoriaDAO.ACTUALIZAR);
					logger.info("OK Se modifico Grupo en BD");
				}
				else{
					setTipoRpta("ERROR");
					setMensaje("Hubo un error modificando el grupo, por favor reintente.");
					logger.info("Error modificando Grupo en BD");
//					logger.error("Error modificando Grupo en BD");
				}
				logger.info("FIN ModificarGrupo");
				return "modificar";
			}
			
			/**
			 * Metodo que elimina la categorÃ­a dada
			 */
			public String eliminar(){
				logger.info("OK EliminarGrupo");
				HttpServletRequest request =  ServletActionContext.getRequest();
		        HttpSession session = request.getSession(true); 
				int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
				int id = Integer.parseInt(getGrupo().getIdGrupo());
				boolean resultado = grupoService.eliminar(id);
				if(resultado){
					setTipoRpta("OK");
					setMensaje("Se elimina el grupo correctamente");
					auditoriaService.registrar("t_grupo", idUsuario, id, AuditoriaDAO.ELIMINAR);
					logger.info("OK SE elimino grupo correctamente de BD");
				}else{
					setTipoRpta("ERROR");
					setMensaje("Hubo un error accediendo a la BD. Por favor, reintente.");
//					logger.error("Error eliminando grupo en BD");
					logger.info("Error eliminando grupo en BD");
				}
				logger.info("FIN EliminarGrupo");
				return "eliminar";
			}
			
			/**
			 * 
			 * Metodo que obtiene un grupo dado
			 */
			public String obtener(){
				logger.info("INICIO ObtenerGRupo");
				int id = Integer.parseInt(getGrupo().getIdGrupo());
				setGrupo(grupoService.obtener(id));
				if(getGrupo()==null){
					setTipoRpta("ERROR");
					setMensaje("Hubo un error obteniendo la informacian del grupo. Por favor, reintente.");
					logger.info("Error obteniendo informacion de grupo desde la BD");
//					logger.error("Error obteniendo informacion de grupo desde la BD");

				}else{
					setTipoRpta("OK");
					logger.info("OK se obtuvo informacion de Grupo");
				}
				logger.info("FIN ObtenerGrupo");
				return "obtener";

			}
			
			public String localizar(){
				logger.info("INICIO LocalizarGrupo");
				HttpServletRequest request =  ServletActionContext.getRequest();
		        HttpSession session = request.getSession(true); 
				int idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString()); 
				int id = Integer.parseInt(getGrupo().getIdGrupo());
				String fechaEnvio = ParserUtil.glueDate(ParserUtil.toSqlDateTimeFormat(new Date()));
				
				int maxUsuarioxLocalizacion=Integer.parseInt(PropertyUtil.readProperty("MAX_USERSXLOCALIZACION"));
				int nroGrupos = 0;
				
				LogDTO logDTO = logService.registrarLog(LogDTO.OPERACION_LOCALIZACION_GRUPAL, idUsuario, id);
				GrupoDTO grupo = grupoService.obtener(id);
				List<UsuarioDTO> usuarios = grupo.getUsuarios();
				
				if(usuarios==null){
					setTipoRpta("ERROR");
					setMensaje("Hubo un error al comunicarse con la BD");
					
					logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, "Hubo un error al comunicarse con la BD.",0, "", 1, fechaEnvio,"");
//					loggerError.info("Error comunicandose con la BD para obtener Cache");
					loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, "-1", "-1", "CACHE",grupo.getNombre(), "Error comunicandose con la BD para obtener Cache"));
//					loggerError.error("(C) Error comunicandose con la BD para obtener Cache");
					logger.info("FIN LocalizarGrupo");
					return "localizar";
				}else if(usuarios.size()==0){
					setPosiciones(new ArrayList<PosicionDTO>());
					setTipoRpta("OK");
					setMensaje("El grupo elegido no tiene ningun usuario asignado.");
					logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, "El grupo elegido no tiene ningun usuario asignado.",0, "", 1,fechaEnvio,"");
//					logger.warn("El grupo elegido no tiene ningun usuario asignado");
					logger.info("El grupo elegido no tiene ningun usuario asignado");
					logger.info("FIN LocalizarGrupo");
					return "localizar";
				}				
				
				logger.info("Se recibieron "+usuarios.size()+" usuarios");
				
				if(usuarios.size()>maxUsuarioxLocalizacion){
					logger.info("Se hace localización por subgrupos");
					nroGrupos = usuarios.size()/maxUsuarioxLocalizacion;
					int residuo = usuarios.size()%maxUsuarioxLocalizacion;
					
					usuariosBloqueados = new ArrayList<UsuarioDTO>();
					usuariosErrorLocalizacion = new ArrayList<PosicionDTO>();
					
					List<UsuarioDTO> usuariosBloqueadosxGrupo;
					List<UsuarioDTO> usuariosDesbloqueadosxGrupo;
					List<PosicionDTO> usuariosErrorLocalizacionxGrupo;
					List<UsuarioDTO> usuariosxGrupo;
					List<Integer> flagsExitoTransaccion = new ArrayList<Integer>();
					List<PosicionDTO> posicionesSG = new ArrayList<PosicionDTO>();
					
					boolean flagLocExito = false;
					int flagTransaccion = 0;
					
					for(int j=0; (j<nroGrupos&&residuo==0)||(j<=nroGrupos&&residuo>0); j++){
						usuariosBloqueadosxGrupo = new ArrayList<UsuarioDTO>();
						usuariosDesbloqueadosxGrupo = new ArrayList<UsuarioDTO>();
						usuariosErrorLocalizacionxGrupo = new ArrayList<PosicionDTO>();
						
						if(j==nroGrupos&&residuo>0){
							
							usuariosxGrupo = usuarios.subList(j*maxUsuarioxLocalizacion, (j*maxUsuarioxLocalizacion)+residuo);
							
						}else{
						
							usuariosxGrupo = usuarios.subList(j*maxUsuarioxLocalizacion, (j+1)*maxUsuarioxLocalizacion);
							
						}
						
						for(UsuarioDTO u : usuariosxGrupo){
							if(u.getEstadoHandset().equals(UsuarioDTO.BLOQUEADO))	usuariosBloqueadosxGrupo.add(u);
							else													usuariosDesbloqueadosxGrupo.add(u);
						}
						
						logger.info("Usuarios Bloqueados para el grupo "+(j+1)+" :"+usuariosBloqueadosxGrupo.size());
						usuariosxGrupo=usuariosDesbloqueadosxGrupo;
						
						if(usuariosxGrupo.size()!=0){

							
							
							TransaccionDTO transaccion = grupoService.localizar(usuariosxGrupo, idUsuario, logDTO);			
							
							/*05/08/2012 MTECK Sandy Huanco*/
							if(transaccion==null){
								//setTipoRpta("ERROR");
								//setMensaje("No se pudo localizar al grupo.");
//								loggerError.error("Error en transaccion de localizacion para el numero "+getUsuario().getNumero());
//								loggerError.info("Error guardando la transaccion en BD de localizacion para el numero "+getUsuario().getNumero());
								loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, "-1", "-1", "ERROR BD", grupo.getIdGrupo(), "Error guardando la transaccion en BD de localizacion"));
//								logService.registrarLogDetalleNumero(transaccion.getLogDetalleDTO(), 2);
								logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, "No se pudo localizar al grupo.",0, "", 1, fechaEnvio, "");
								//logService.updateLogExito(logDTO.getIdLog(), transaccion.getFlagExito());
								//return "localizar";
							}else
							
							if(transaccion.getError()!=null){
//								loggerError.error("(S) Ocurrio un error en la transaccion de localizacion "+transaccion.getError().getResultado());
								//loggerError.info("(S) Ocurrio un error en la transaccion de localizacion "+transaccion.getError().getResultado());
								loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, transaccion.getIdTransaccion()+"", "-1", transaccion.getError().getResultado()+"",grupo.getNombre() +"(GRUPO)", transaccion.getError().getExtrainfo()+""));

								final String mensaje= "Ocurrio un error durante una transaccion de localizacion."+transaccion.getError().getResultado();
								/*new Thread(){
									public void run(){
										MailUtil.enviarMailContext("Error de Transaccion de Localizacion", mensaje);

									}
								}.start();*/
								//setTipoRpta("ERROR");
								//setMensaje("No se encontr— al grupo. ( S ) ");
//								logService.registrarLogDetalleNumero(transaccion.getLogDetalleDTO(), 2);
								logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, "No se encontr— al grupo. ( S ) ",0, "", 1, fechaEnvio, "");
								//logService.updateLogExito(logDTO.getIdLog(), transaccion.getFlagExito());
								//logger.info("FIN LocalizarGrupo");
								//return "localizar";
							}else{
							
								flagLocExito = true;
								
							List<PosicionDTO> posicionesxGrupo = MapUtil.coordenadasValidas(transaccion.getPosiciones());
							usuariosErrorLocalizacionxGrupo=MapUtil.coordenadasInvalidas(transaccion.getPosiciones());
							
							for(int i = 0; i<usuariosErrorLocalizacionxGrupo.size(); i++){
								String numero = "";
								if(usuariosErrorLocalizacionxGrupo.get(i).getUsuario().getNumero().substring(0,2).equals("51"))
									numero = usuariosErrorLocalizacionxGrupo.get(i).getUsuario().getNumero().substring(2);
								else
									numero = usuariosErrorLocalizacionxGrupo.get(i).getUsuario().getNumero();
								UsuarioDTO usuario = new UsuarioDTO();
								usuario.setNumero(numero);
								usuario.setEtiqueta(usuarioService.getEtiqueta(numero));
								usuariosErrorLocalizacionxGrupo.get(i).setUsuario(usuario);
							}
							
							logger.info("Usuarios localizados correctamente para el sub grupo"+posicionesxGrupo.size());
							logger.info("Usuarios con error de localizacion para el sub grupo"+usuariosErrorLocalizacionxGrupo.size());
//							logger.warn("Usuarios con error de localizacion "+usuariosErrorLocalizacion.size());
							
							posicionesSG.addAll(posicionesxGrupo);
							
							}
							
							flagsExitoTransaccion.add(transaccion.getFlagExito());
						}
						
						usuariosBloqueados.addAll(usuariosBloqueadosxGrupo);
						usuariosErrorLocalizacion.addAll(usuariosErrorLocalizacionxGrupo);
						
					}
					
					if(flagsExitoTransaccion.contains(1)){
						List<CoordenadaDTO> coordenadas = new ArrayList<CoordenadaDTO>();
						for(PosicionDTO pos : posicionesSG){
							coordenadas.add(pos.getCoordenada());						
						}
						ExtentDTO limites = MapUtil.getLimites(coordenadas);
						setCentro(MapUtil.calcularCentro(limites));
						for(PosicionDTO p : posicionesSG){
							String numero = "";
							if(p.getUsuario().getNumero().substring(0,2).equals("51"))
								numero = p.getUsuario().getNumero().substring(2);
							else
								numero = p.getUsuario().getNumero();
							p.setTimestamp(usuarioService.getTimestamp(numero));
							UsuarioDTO usuario = new UsuarioDTO();
							usuario.setNumero(numero);
							usuario.setEtiqueta(usuarioService.getEtiqueta(numero));
							p.setUsuario(usuario);
						}
						setPosiciones(posicionesSG);
//						logService.registrarLogDetalleNumero(transaccion.getLogDetalleDTO(), 2);
						logService.updateLogExito(logDTO.getIdLog(), 1);
					}else{
						setTipoRpta("ERROR");
						
						logService.updateLogExito(logDTO.getIdLog(), 0);
						setMensaje("No se pudo localizar al grupo.");
						if(flagsExitoTransaccion.contains(3)){
							logService.updateLogExito(logDTO.getIdLog(), 2);
							setMensaje("No se encontr— al grupo. ( S ) ");
						}
						logger.info("FIN LocalizarGrupo");
						return "localizar";
					}
					
				}else{
				
				usuariosBloqueados = new ArrayList<UsuarioDTO>();
				List<UsuarioDTO> usuariosDesbloqueados = new ArrayList<UsuarioDTO>();
				usuariosErrorLocalizacion = new ArrayList<PosicionDTO>();
				for(UsuarioDTO u : usuarios){
					if(u.getEstadoHandset().equals(UsuarioDTO.BLOQUEADO))	usuariosBloqueados.add(u);
					else													usuariosDesbloqueados.add(u);
				}
				logger.info("Usuarios Bloqueados :"+usuariosBloqueados.size());
				usuarios=usuariosDesbloqueados;
				if(usuarios.size()!=0){
					
					
					TransaccionDTO transaccion = grupoService.localizar(usuarios, idUsuario, logDTO);			
					
					/*05/08/2012 MTECK Sandy Huanco*/
					if(transaccion==null){
						setTipoRpta("ERROR");
						setMensaje("No se pudo localizar al grupo.");
//						loggerError.error("Error en transaccion de localizacion para el numero "+getUsuario().getNumero());
//						loggerError.info("Error guardando la transaccion en BD de localizacion para el numero "+getUsuario().getNumero());
						loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, "-1", "-1", "ERROR BD", grupo.getIdGrupo(), "Error guardando la transaccion en BD de localizacion"));
//						logService.registrarLogDetalleNumero(transaccion.getLogDetalleDTO(), 2);
						logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, "No se pudo localizar al grupo.",0, "", 1, fechaEnvio, "");
						logService.updateLogExito(logDTO.getIdLog(), transaccion.getFlagExito());
						return "localizar";
					}
					
					if(transaccion.getError()!=null){
//						loggerError.error("(S) Ocurrio un error en la transaccion de localizacion "+transaccion.getError().getResultado());
						//loggerError.info("(S) Ocurrio un error en la transaccion de localizacion "+transaccion.getError().getResultado());
						loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, transaccion.getIdTransaccion()+"", "-1", transaccion.getError().getResultado()+"",grupo.getNombre() +"(GRUPO)", transaccion.getError().getExtrainfo()+""));

						final String mensaje= "Ocurrio un error durante una transaccion de localizacion."+transaccion.getError().getResultado();
						/*new Thread(){
							public void run(){
								MailUtil.enviarMailContext("Error de Transaccion de Localizacion", mensaje);

							}
						}.start();*/
						setTipoRpta("ERROR");
						setMensaje("No se encontr— al grupo. ( S ) ");
//						logService.registrarLogDetalleNumero(transaccion.getLogDetalleDTO(), 2);
						logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, "No se encontr— al grupo. ( S ) ",0, "", 1, fechaEnvio, "");
						logService.updateLogExito(logDTO.getIdLog(), transaccion.getFlagExito());
						logger.info("FIN LocalizarGrupo");
						return "localizar";
					}
					
					List<PosicionDTO> posiciones = MapUtil.coordenadasValidas(transaccion.getPosiciones());
					usuariosErrorLocalizacion=MapUtil.coordenadasInvalidas(transaccion.getPosiciones());
					
					for(int i = 0; i<usuariosErrorLocalizacion.size(); i++){
						String numero = "";
						if(usuariosErrorLocalizacion.get(i).getUsuario().getNumero().substring(0,2).equals("51"))
							numero = usuariosErrorLocalizacion.get(i).getUsuario().getNumero().substring(2);
						else
							numero = usuariosErrorLocalizacion.get(i).getUsuario().getNumero();
						UsuarioDTO usuario = new UsuarioDTO();
						usuario.setNumero(numero);
						usuario.setEtiqueta(usuarioService.getEtiqueta(numero));
						usuariosErrorLocalizacion.get(i).setUsuario(usuario);
					}
					
					logger.info("Usuarios localizados correctamente "+posiciones.size());
					logger.info("Usuarios con error de localizacion "+usuariosErrorLocalizacion.size());
//					logger.warn("Usuarios con error de localizacion "+usuariosErrorLocalizacion.size());
					List<CoordenadaDTO> coordenadas = new ArrayList<CoordenadaDTO>();
					for(PosicionDTO pos : posiciones){
						coordenadas.add(pos.getCoordenada());						
					}
					ExtentDTO limites = MapUtil.getLimites(coordenadas);
					setCentro(MapUtil.calcularCentro(limites));
					for(PosicionDTO p : posiciones){
						String numero = "";
						if(p.getUsuario().getNumero().substring(0,2).equals("51"))
							numero = p.getUsuario().getNumero().substring(2);
						else
							numero = p.getUsuario().getNumero();
						p.setTimestamp(usuarioService.getTimestamp(numero));
						UsuarioDTO usuario = new UsuarioDTO();
						usuario.setNumero(numero);
						usuario.setEtiqueta(usuarioService.getEtiqueta(numero));
						p.setUsuario(usuario);
					}
					setPosiciones(posiciones);
//					logService.registrarLogDetalleNumero(transaccion.getLogDetalleDTO(), 2);
					logService.updateLogExito(logDTO.getIdLog(), transaccion.getFlagExito());
				}
			}
				
				if(usuariosBloqueados.size()!=0 || usuariosErrorLocalizacion.size()!=0){
					setTipoRpta("WARN");
					String m="";
					if(usuariosErrorLocalizacion.size()!=0 || usuariosBloqueados.size()!=0){
						m+="Uno o mas usuarios no pudieron ser localizados o estan bloqueados.";
					}
					m += "Revise el detalle de las localizaciones.";
					setMensaje(m);
					logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, m,0, "", 1, fechaEnvio,"");
				}else if(getPosiciones()==null){
					setTipoRpta("ERROR");
					setMensaje("Hubo un error obteniendo la localizacion del grupo. Por favor, reintente.");
					logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, "Hubo un error obteniendo la localizacion del grupo. Por favor, reintente.",0, "", 1, fechaEnvio,"");
//					logger.error("ERROR Hubo un error obteniendo la localizacion del grupo");
					logger.info("ERROR Hubo un error obteniendo la localizacion del grupo");
				}else{
					setTipoRpta("OK");
					logger.info("OK se obtuvieron las posiciones del grupo");
					logService.registrarLogDetalle(logDTO.getIdLog(), LogDTO.MENSAJE_MOSTRAR_LOCALIZACION, LogDTO.COMPONENTE_WEB, "Localizacion de grupo ",0, "", 1, fechaEnvio, "");
				}
				
				logger.info("FIN LocalizarGrupo");
				return "localizar";
			}

}
