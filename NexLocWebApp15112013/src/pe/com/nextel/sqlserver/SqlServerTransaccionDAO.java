package pe.com.nextel.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

//import org.apache.log4j.Logger;

import pe.com.nextel.bean.ErrorDTO;
import pe.com.nextel.bean.PosicionDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.dao.iface.TransaccionDAO;
import pe.com.nextel.util.DataSourceBD;
import pe.com.nextel.util.LoggerUtil;
//import pe.com.nextel.util.MailUtil;
import pe.com.nextel.util.ParserUtil;

public class SqlServerTransaccionDAO implements TransaccionDAO {
	
//	Logger loggerError = Logger.getLogger("LocHandler");
//	Logger logger = Logger.getLogger(this.getClass().getName());
	Logger logger = LoggerUtil.getInstance();
	Logger loggerError = LoggerUtil.getInstanceError();

	@Override
	public boolean registrar(TransaccionDTO transaccion, int idUsuario, int tipoTransaccion) {
		String sqlTransaccion="INSERT INTO t_transaccion (idUsuario, tipo, resultado, origen, horaInicio, horaFin) VALUES (?, ?, ?, '0', ?, ?)";
		String sqlConsulta="INSERT INTO t_consulta (idTransaccion, resultado, tecnologia, msid, time, metodo, longitud, latitud, direccion, distrito, radius, levelConf, cc, ndc, vmscno) VALUES (?, ?, ?, ?, CAST(? as datetime), ?, ?, ?, ?, ?, ?, ? ,? ,? ,?)";
		String sqlSelectUsuario ="SELECT origen FROM t_usuario WHERE numero = ? AND estado = '1'"; 
		String sqlInsertUsuario = "UPDATE t_usuario SET longitud=?, latitud=?, time=CAST(? as datetime), metodo=?, timestamp=GETDATE() WHERE numero=?";
		String sqlInsertLogDetalleNumero ="insert into t_logDetalleNumero (idLog, numero, fechaRegistro, flagExito, idMetodo, fechaLocalizacion, idComponente, error, " +
				"descripcionError, origen, operacion) values (?,?,getdate(),?,?,CAST(? as datetime),?,?,?,?,?)";
				
		Connection conexion = null;
		boolean estado = false;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String origen="";
		if(transaccion==null){
			final String mensaje="(S) Se obtuvo un error de transaccion. No se ha podido comunicar con el servidor de localizacion";
			loggerError.info(mensaje);
			logger.info("No se guardara la transaccion en BD");	
			return false;
		}
		List<PosicionDTO> posiciones = transaccion.getPosiciones();
		int idTransaccion = -1;	
		transaccion.setIdTransaccion(idTransaccion);
		
		try{
			conexion = DataSourceBD.openConnection();
			
			if(transaccion.getError()==null){
						
				pstm = conexion.prepareStatement(sqlTransaccion, Statement.RETURN_GENERATED_KEYS);	
				pstm.setInt(1, idUsuario);
				pstm.setInt(2, tipoTransaccion);
				pstm.setInt(3, TransaccionDTO.OK);
				pstm.setString(4, transaccion.getHoraInicio());
				pstm.setString(5, transaccion.getHoraFin());
				
				logger.info(transaccion.getHoraInicio());
				logger.info(transaccion.getHoraFin());

				int i = pstm.executeUpdate();
				
				if(i!=0){	
					ResultSet llaves = pstm.getGeneratedKeys();
					while (llaves.next()) {
						idTransaccion = llaves.getInt(1);
						transaccion.setIdTransaccion(idTransaccion);
					}
					llaves.close();

					for(int x = 0; x<posiciones.size(); x++){
						PosicionDTO pos = posiciones.get(x);
						boolean isCacheError=false;
						int idConsulta=-1;
						/****/
						String numero = "";
						if(pos.getUsuario().getNumero().substring(0,2).equals("51"))
							numero = pos.getUsuario().getNumero().substring(2);
						else
							numero = pos.getUsuario().getNumero();
						
						/****************************************************************/
						/************** SE CONSULTa EN LA TABLA USUARIO *****************/
						/****************************************************************/
						pstm = conexion.prepareStatement(sqlSelectUsuario);
						pstm.setString(1, numero);
						rs=pstm.executeQuery();
						if(rs.next())	origen=rs.getString(1);
//						
						/****/
						if(pos.getError()==null){
							//Si no se recibe error
							if(!ParserUtil.validarTime(pos.getTime())){
								pos.setFlagExito(0);
								ErrorDTO e = new ErrorDTO();
								e.setResultado("Error de cache");
								e.setExtrainfo("Se obtuvo una posicion con un tiempo mayor al permitido");
								pos.setError(e);
								posiciones.add(x, pos);
								posiciones.remove(x+1);
//								loggerError.info("(C) Error se supero la diferencia de tiempo de localizacion del MLP y la permitida, para el numero : "+pos.getUsuario().getNumero());
								isCacheError=true;
								//transaccion.getLogDetalleDTO().get(x).setFlagExito(0);
							}
							
							String metodo = ParserUtil.calcularMetodo(pos.getRadio(), origen);
							int idMetodo = ParserUtil.devolverMetodo(metodo);
							
							
							try{
								/****************************************************************/
								/************** SE HACE UPDATE EN LA TABLA USUARIO***************/
								/****************************************************************/
								//Guardando en tabla t_usuario
								pstm = conexion.prepareStatement(sqlInsertUsuario);
								pstm.setString(1, pos.getCoordenada().getLongitud());
								pstm.setString(2, pos.getCoordenada().getLatitud());
								pstm.setString(3, ParserUtil.stringToStringFormat(pos.getTime()));
								pstm.setString(4, metodo);
								pstm.setString(5, numero);
								logger.info("El numero a ingresar es : "+numero);
								int u = pstm.executeUpdate();
								if(u!=0) logger.info("Se actualizo la informacion de localizacion para "+numero);
								else	 logger.info("No se pudo actualizar la informaci—n de localizacion para el usuario "+numero);
							
								/****************************************************************/
								/************** SE INSERTA EN LA TABLA CONSULTA *****************/
								/****************************************************************/
								
								//Guardando en tabla t_consulta
								pstm = conexion.prepareStatement(sqlConsulta, Statement.RETURN_GENERATED_KEYS);
								pstm.setInt(1, idTransaccion);
								if(pos.getError()==null)	pstm.setInt(2, TransaccionDTO.OK);
								else						pstm.setInt(2, TransaccionDTO.ERROR);
								
								pstm.setString(3, origen);
								pstm.setString(4, numero);
								pstm.setString(5, ParserUtil.stringToStringFormat(pos.getTime()));
								pstm.setString(6, metodo);
								pstm.setString(7, pos.getCoordenada().getLongitud());
								pstm.setString(8, pos.getCoordenada().getLatitud());
								
								if(pos.getError()==null){
									if(pos.getDireccion().getDireccion()!=null){
										pstm.setString(9, pos.getDireccion().getDireccion());
										pstm.setString(10, pos.getDireccion().getDistrito());
									}else{
										pstm.setNull(9, java.sql.Types.VARCHAR);
										pstm.setNull(10, java.sql.Types.VARCHAR);
									} 
								}else{
									pstm.setNull(9, java.sql.Types.VARCHAR);
									pstm.setNull(10, java.sql.Types.VARCHAR);
								}
								
									
								pstm.setString(11, pos.getRadio());
								
								if(pos.getLevelConf()!=null){
									//Si es 3G recibe estos valores
									pstm.setString(12, pos.getLevelConf());
									pstm.setString(13, pos.getCc());
									pstm.setString(14, pos.getNdc());
									pstm.setString(15, pos.getVmscno());
								}else{
									//Cuando no es 3G no recibe ningœn valor de los indicados, se ponen en null
									pstm.setNull(12, java.sql.Types.VARCHAR);
									pstm.setNull(13, java.sql.Types.VARCHAR);
									pstm.setNull(14, java.sql.Types.VARCHAR);
									pstm.setNull(15, java.sql.Types.VARCHAR);
								}
								int n = pstm.executeUpdate();
								if(n!=0)	estado = true;
								if(estado){
									llaves = pstm.getGeneratedKeys();
									while (llaves.next()) {
										idConsulta = llaves.getInt(1);
									}
									llaves.close();
									pos.setIdPosicion(idConsulta+"");
								}
							}catch (Exception ex) {
								ex.printStackTrace();
							}
							
							/****************************************************************/
							/************ SE INSERTA EN TABLA LOGDETALLENUMERO **************/
							/****************************************************************/
							pstm = conexion.prepareStatement(sqlInsertLogDetalleNumero);
							pstm.setInt(1, transaccion.getIdLog());
							pstm.setString(2, numero);
							pstm.setInt(3, pos.getFlagExito());
							pstm.setInt(4, idMetodo);
							pstm.setString(5, ParserUtil.stringToStringFormat(pos.getFechaRespuesta()));							
							pstm.setInt(6, pos.getIdComponente());							
							pstm.setNull(7, java.sql.Types.VARCHAR);
							pstm.setNull(8, java.sql.Types.VARCHAR);								
							pstm.setString(9, origen);
							pstm.setInt(10, (tipoTransaccion+1));
							int n = pstm.executeUpdate();
							if(n!=0)	estado = true;
						}else{
							//Si se recibe error en una posicion
							/****************************************************************/
							/************** SE CONSULTA EN LA TABLA USUARIO *****************/
							/****************************************************************/
							pstm = conexion.prepareStatement(sqlSelectUsuario);
							pstm.setString(1, numero);
							rs=pstm.executeQuery();
							if(rs.next())	origen=rs.getString(1);
							
							/****************************************************************/
							/************** SE INSERTA EN LA TABLA CONSULTA *****************/
							/****************************************************************/
							pstm = conexion.prepareStatement(sqlConsulta, Statement.RETURN_GENERATED_KEYS);
							pstm.setInt(1, idTransaccion);
							pstm.setInt(2, TransaccionDTO.ERROR);
							pstm.setString(3, origen);
							pstm.setString(4, numero);
							pstm.setString(5, ParserUtil.stringToStringFormat(pos.getTime()));
							pstm.setNull(6,  java.sql.Types.VARCHAR);
							pstm.setNull(7,  java.sql.Types.VARCHAR);
							pstm.setNull(8,  java.sql.Types.VARCHAR);
							pstm.setNull(9,  java.sql.Types.VARCHAR);
							pstm.setNull(10, java.sql.Types.VARCHAR);
							pstm.setNull(11, java.sql.Types.VARCHAR);
							pstm.setNull(12, java.sql.Types.VARCHAR);
							pstm.setNull(13, java.sql.Types.VARCHAR);
							pstm.setNull(14, java.sql.Types.VARCHAR);
							pstm.setNull(15, java.sql.Types.VARCHAR);
							int n = pstm.executeUpdate();
							if(n!=0) 	estado = true;
							if(estado){
								llaves = pstm.getGeneratedKeys();
								while (llaves.next()) {
									idConsulta = llaves.getInt(1);
								}
								llaves.close();
								pos.setIdPosicion(idConsulta+"");
							}
							
							/****************************************************************/
							/************ SE INSERTA EN TABLA LOGDETALLENUMERO **************/
							/****************************************************************/
							pstm = conexion.prepareStatement(sqlInsertLogDetalleNumero);
							pstm.setInt(1, transaccion.getIdLog());
							pstm.setString(2, numero);
							pstm.setInt(3, pos.getFlagExito());
							pstm.setNull(4, java.sql.Types.INTEGER);
							pstm.setString(5, ParserUtil.stringToStringFormat(pos.getFechaRespuesta()));
							pstm.setInt(6, pos.getIdComponente());							
							pstm.setString(7, pos.getError().getResultado());
							pstm.setString(8, pos.getError().getExtrainfo());								
							pstm.setString(9, origen);
							pstm.setInt(10, (tipoTransaccion+1));														
							n = pstm.executeUpdate();
							if(n!=0)	estado = true;
						}
						//transaccion.getLogDetalleDTO().get(x).setOrigen(origen);
						if(isCacheError)
							loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, idTransaccion+"", idConsulta+"","CACHE", ParserUtil.formatMSID(pos.getUsuario().getNumero()), "Error se supero la diferencia de tiempo de localizacion del MLP y la permitida."));
					}
				}
			}else{				
				pstm = conexion.prepareStatement(sqlTransaccion, Statement.RETURN_GENERATED_KEYS);	
				pstm.setInt(1, idUsuario);
				pstm.setInt(2, tipoTransaccion);
				pstm.setInt(3, TransaccionDTO.ERROR);
				pstm.setString(4, transaccion.getHoraInicio());
				pstm.setString(5, transaccion.getHoraFin());
				int i = pstm.executeUpdate();
				if(i!=0)	estado = true;
				
				ResultSet llaves = pstm.getGeneratedKeys();
				while (llaves.next()) {
					idTransaccion = llaves.getInt(1);
					transaccion.setIdTransaccion(idTransaccion);
				}
				llaves.close();
				
				ErrorDTO e = transaccion.getError();
//				loggerError.info("(S) Se obtuvo un error en el MLP : "+e.getResultado());
//				final String mensaje="(S) Se obtuvo un error de transaccion, la siguiente informacion fue devuelta por el servidor: \n"+e.getResultado()+"\\n"+e.getExtrainfo();
//				loggerError.info(mensaje);
				loggerError.info(String.format(ParserUtil.LOGGER_FORMAT, idTransaccion+"", "-1", e.getResultado(), "NONE", e.getExtrainfo()));
//				logger.info(mensaje);
				/*new Thread(){
					public void run(){
						boolean estado = MailUtil.enviarMailContext("Error de localizacion [Location Enhancement]", mensaje);
						if(estado){
							logger.info("Se envio el mail de Error en localizacion correctamente");
//							logger.info("Se envio el mail de Error en localizacion correctamente");

						}
						else{
//							loggerError.error("Hubo un error enviando el mail de Error en localizacion");
							logger.info("Hubo un error enviando el mail de Error en localizacion");
						}
					}
				}.start();*/
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			DataSourceBD.closeConnection(conexion);
		}
		return estado;
	}

}

