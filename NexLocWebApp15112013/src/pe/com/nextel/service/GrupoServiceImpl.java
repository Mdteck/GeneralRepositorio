package pe.com.nextel.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import org.apache.log4j.Logger;
import java.util.logging.Logger;

import pe.com.nextel.bean.ErrorDTO;
import pe.com.nextel.bean.GrupoDTO;
import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.GrupoDAO;
import pe.com.nextel.dao.iface.TransaccionDAO;
import pe.com.nextel.dao.iface.UsuarioDAO;
import pe.com.nextel.util.LoggerUtil;
import pe.com.nextel.util.ParserUtil;

/**
 * @author Devos Inc.
 */

public class GrupoServiceImpl implements GrupoService {
	
	DAOFactory fabricaSqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
	GrupoDAO sqlServerGrupoDAO=fabricaSqlServer.getGrupoDAO();
	DAOFactory fabricaLocalizacion = DAOFactory.getDAOFactory(DAOFactory.LOCALIZACION);
	GrupoDAO localizacionGrupoDAO=fabricaLocalizacion.getGrupoDAO();
	UsuarioDAO sqlServerUsuarioDAO = fabricaSqlServer.getUsuarioDAO();
	TransaccionDAO sqlServerTransaccionDAO=fabricaSqlServer.getTransaccionDAO();
	LogService logService=BusinessDelegate.getLogService();
//	Logger logger = Logger.getLogger(GrupoServiceImpl.class.getName());
//	Logger loggerError = Logger.getLogger("LocHandler");
	Logger logger = LoggerUtil.getInstance();
	Logger loggerError = LoggerUtil.getInstanceError();

	@Override
	public boolean modificar(GrupoDTO grupo) {
		return sqlServerGrupoDAO.modificar(grupo);
	}

	@Override
	public boolean eliminar(int idGrupo) {
		return sqlServerGrupoDAO.eliminar(idGrupo);
	}

	@Override
	public int registrar(GrupoDTO grupo) {
		return sqlServerGrupoDAO.registrar(grupo);
	}

	@Override
	public List<GrupoDTO> listar(int idMonitor) {
		return sqlServerGrupoDAO.listar(idMonitor);
	}

	@Override
	public List<GrupoDTO> listarPorCuenta(int idCuenta) {
		return sqlServerGrupoDAO.listarPorCuenta(idCuenta);
	}

	@Override
	public boolean asignarGrupo(int idGrupo, List<UsuarioDTO> usuarios) {
		return sqlServerGrupoDAO.asignarGrupo(idGrupo, usuarios);
	}

	@Override
	public GrupoDTO obtener(int idGrupo) {
		return sqlServerGrupoDAO.obtener(idGrupo);
	}

	@Override
	public TransaccionDTO localizar(List<UsuarioDTO> usuarios, int idUsuario, LogDTO logDTO) {
		List<PosicionDTO> posiciones = new ArrayList<PosicionDTO>();
		List<PosicionDTO> posicionesCache = new ArrayList<PosicionDTO>();
		List<LogDTO> logCache = new ArrayList<LogDTO>();
		List<UsuarioDTO> usuariosLocalizacion = new ArrayList<UsuarioDTO>();
		TransaccionDTO transaccion = sqlServerGrupoDAO.localizar(usuarios, logDTO);
		TransaccionDTO transaccionBd = new TransaccionDTO();
		TransaccionDTO transaccionP = null;
		posiciones=transaccion.getPosiciones();
		int idMetodo = 0;
		try {
			if(posiciones!=null){
				for(PosicionDTO p : posiciones){
					if(!p.isCache()){
						usuariosLocalizacion.add(p.getUsuario());
					}else{
						posicionesCache.add(p);
//						LogDTO log = new LogDTO();
//						log.setIdLog(logDTO.getIdLog());
//						log.setNumero(p.getUsuario().getNumero());
//						log.setIdComponente(log.COMPONENTE_BD);
//						log.setIdMetodo(ParserUtil.devolverMetodo(p.getMetodo()));
//						log.setFechaRespuesta(p.getTime());
//						log.setFlagExito(1);
//						log.setOrigen(p.getTecnologia());
//						log.setIdTipoTransaccion(1);
//						logCache.add(log);
					}
				}
				if(posicionesCache.size()>0){
					transaccionBd.setIdLog(logDTO.getIdLog());
					transaccionBd.setPosiciones(posicionesCache);
					logService.registrarLogDetalleNumero(transaccionBd);
				}
				logger.info("Usuarios en Cache: "+posicionesCache.size());
				if(usuariosLocalizacion.size()!=0){
					logger.info("Usuarios enviados a localizacion: "+usuariosLocalizacion.size());
					transaccionP = localizacionGrupoDAO.localizar(usuariosLocalizacion, logDTO);					
					if(transaccionP!=null){
						transaccionP.setIdLog(logDTO.getIdLog());
						sqlServerTransaccionDAO.registrar(transaccionP, idUsuario, TransaccionDTO.GRUPAL);
						int cont = 0;
						if(transaccionP.getPosiciones()!=null){
							for(PosicionDTO p : transaccionP.getPosiciones()){
								if(p.getError()==null){
									String numero = "";
									if(p.getUsuario().getNumero().substring(0,2).equals("51"))
										numero = p.getUsuario().getNumero().substring(2);
									else
										numero = p.getUsuario().getNumero();
									p.setMetodo(ParserUtil.calcularMetodo(p.getRadio(), sqlServerUsuarioDAO.getOrigen(numero)));
									idMetodo = ParserUtil.devolverMetodo(p.getMetodo());									
									//logService.registrarLogMetodo(idLog, idMetodo);
								}else{
									//loggerError.info("(P) Error en posici—n para el nœmero "+p.getUsuario().getNumero()+" : "+p.getError().getResultado());
									loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, transaccion.getIdTransaccion()+"", p.getIdPosicion()+"", p.getError().getResultado(), ParserUtil.formatMSID(p.getUsuario().getNumero()), p.getError().getExtrainfo()));
								}
								posicionesCache.add(p);								
								cont++;
							}							
							posiciones = posicionesCache;
							transaccion.setPosiciones(posiciones);
						}else{
							transaccion.setError(transaccionP.getError());
							transaccion.setFlagExito(transaccionP.getFlagExito());
						}
					}else{
						ErrorDTO error = new ErrorDTO();
						error.setResultado("Error de Conexión con el componente Transaccion.");
						transaccion.setFlagExito(logDTO.FLAG_EXITO_NO_RESPONDIO);
						transaccion.setError(error);
					}
				}
				
				
			}else{
				String msj = "";
				
				transaccion = localizacionGrupoDAO.localizar(usuarios, logDTO);
				
				if(transaccion!=null){
					if(transaccion.getError()!=null){
						msj=transaccion.getError().getExtrainfo();
		//				loggerError.error("(S) Error en la transaccion recibida (MLP) :"+ msj);
		//				loggerError.info("(S) Error en la transacci—n recibida (MLP) : "+ msj);
						loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, transaccion.getIdTransaccion()+"", "-1",  transaccion.getError().getResultado()+"", "NONE", transaccion.getError().getExtrainfo()+""));
		
					}
					
					sqlServerTransaccionDAO.registrar(transaccion, idUsuario, TransaccionDTO.GRUPAL);
				}else{
					ErrorDTO error = new ErrorDTO();
					error.setResultado("Error de Conexión con el componente Transaccion.");
					transaccion.setFlagExito(logDTO.FLAG_EXITO_NO_RESPONDIO);
					transaccion.setError(error);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transaccion;
	}

}
