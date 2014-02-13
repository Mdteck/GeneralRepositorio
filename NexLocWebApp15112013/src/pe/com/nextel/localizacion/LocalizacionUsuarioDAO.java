package pe.com.nextel.localizacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.com.nextel.bean.ErrorDTO;
import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.factory.DAOFactory;
import pe.com.nextel.dao.iface.LogDAO;
import pe.com.nextel.dao.iface.UsuarioDAO;
import pe.com.nextel.util.ServicioLocalizacionConnectionUtil;
import pe.com.nextel.util.XMLUtil;

/**
 * Clase que implementa la localización para un Usuario Los demás métodos no
 * tendrán código porque esta clase no los implementa
 * 
 * @author DevosInc
 * 
 */
public class LocalizacionUsuarioDAO implements UsuarioDAO {

	static DAOFactory fabricaSqlServer = DAOFactory
			.getDAOFactory(DAOFactory.SQLSERVER);
	static LogDAO sqlServerLogDAO = fabricaSqlServer.getLogDAO();

	@Override
	public boolean modificar(UsuarioDTO usuario) {
		return false;
	}

	@Override
	public List<UsuarioDTO> listar(int idCuenta) {
		return null;
	}

	/**
	 * Método que obtendrá la posición de un usuario de acuerdo a su número
	 * 
	 * @param UsuarioDTO
	 *            que contiene la información del usuario para ser localizado
	 * @return PosicionDTO con la información de la posición del usuario
	 *         enviado, podría contener el objeto ErrorDTO detallando si algo
	 *         salió mal
	 */
	@Override
	public TransaccionDTO localizar(UsuarioDTO usuario, LogDTO logDTO) {
		String numero = usuario.getNumero();
		long timestampInicio = new Date().getTime();
//		String xmlResponse=timestampInicio+""+timestampInicio+"5<?xml version=\"1.0\"?><!DOCTYPE svc_result SYSTEM \"MLP_SVC_RESULT_300.DTD\"><svc_result ver=\"3.0.0\"><slia ver=\"3.0.0\"><pos><msid type=\"MIN\">51946198304</msid><pd><time utc_off=\"+0000\">20130820035300</time><shape><CircularArea><coord><X>12 03 32.968S</X><Y>76 57 05.025W</Y></coord><radius>4999</radius></CircularArea></shape></pd></pos></slia></svc_result>";
//		String xmlResponse=timestampInicio+""+timestampInicio+"5<?xml version=\"1.0\"?><!DOCTYPE svc_result SYSTEM \"MLP_SVC_RESULT_300.DTD\"><svc_result ver=\"3.0.0\"><slia ver=\"3.0.0\"><pos><msid type=\"MIN\">51998178466</msid><poserr><result resid=\"5\">ABSENT SUBSCRIBER</result><add_info>Target not reachable</add_info><time utc_off=\"+0000\">20130820041400</time></poserr></pos></slia></svc_result>";
//		String xmlResponse=timestampInicio+""+timestampInicio+"5<?xml version='1.0' encoding='UTF-8'?> <!DOCTYPE svc_init SYSTEM 'MLP_SVC_INIT_300.DTD'> <svc_init ver='3.0.0'> <hdr ver='3.0.0'> <client> <id>Locper</id> <pwd>junin07</pwd> </client> </hdr> <slir ver='3.0.0' res_type='SYNC'> <msids> <msid type='MIN'>51981243062</msid> <msid type='MIN'>51946038572</msid> <msid type='MIN'>51994158647</msid> <msid type='MIN'>51994158994</msid> <msid type='MIN'>51994136956</msid> <msid type='MIN'>51994131888</msid> </msids> <eqop> <resp_req type='DELAY_TOL'/> <resp_timer>60</resp_timer> <hor_acc>25</hor_acc> </eqop> <loc_type type='CURRENT' /> </slir> </svc_init>"; 
//		String xmlResponse=timestampInicio+""+timestampInicio+"5<?xml version='1.0' encoding='UTF-8'?> <!DOCTYPE svc_init SYSTEM 'MLP_SVC_INIT_300.DTD'> <svc_init ver='3.0.0'> <hdr ver='3.0.0'> <client> <id>Izidor@NII</id> <pwd>n11v4s</pwd> </client> </hdr> <slir ver='3.0.0' res_type='SYNC'> <msids> <msid type='MIN'>51981082269</msid> </msids> <eqop> <resp_req type='DELAY_TOL'/> <resp_timer>60</resp_timer> <hor_acc>25</hor_acc> </eqop> <loc_type type='CURRENT' /> </slir> </svc_init> "; 
		String xmlResponse = ServicioLocalizacionConnectionUtil.llamarAplicacionConexionLocalizacion(numero, logDTO.getIdLog());
		TransaccionDTO transaccion = new TransaccionDTO();
		List<LogDTO> logDetalle = new ArrayList<LogDTO>();

		if (xmlResponse.isEmpty()) {
			return null;
		}

		String horaInicio = xmlResponse.substring(0, 13);
		String horaFin = xmlResponse.substring(13, 26);
		int idComponenteRespuesta = Integer.parseInt(xmlResponse.substring(26,27));
		xmlResponse = xmlResponse.substring(27);

		TransaccionDTO transaccionXml = XMLUtil.readXMLLocalizationServer(xmlResponse, logDTO.getIdLog(), idComponenteRespuesta);
		if (transaccionXml != null) {
			transaccion = transaccionXml;
			transaccion.setFlagExito(logDTO.FLAG_EXITO_RESPONDIO);
		} else {
			logDTO.setError("Formato de Respuesta no esperado");
			logDetalle.add(logDTO);
			ErrorDTO error = new ErrorDTO();
			error.setResultado("Formato de Respuesta no esperado");
			transaccion.setError(error);
			transaccion.setFlagExito(logDTO.FLAG_EXITO_INVALIDO);
		}
		transaccion.setHoraInicio(horaInicio);
		transaccion.setHoraFin(horaFin);

		return transaccion;
	}

	@Override
	public UsuarioDTO obtener(int idUsuario) {
		return null; // no se implementa en esta clase
	}

	@Override
	public List<UsuarioDTO> listarMonitoreados(int idMonitor) {
		return null; // No se implementa en esta clase
	}

	@Override
	public boolean asignarMonitoreados(int idMonitor,
			List<UsuarioDTO> monitoreados) {
		return false; // no se implementa en esta clase
	}

	@Override
	public List<UsuarioDTO> listarNoMonitoreados(int idMonitor,
			List<UsuarioDTO> monitoreados) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public UsuarioDTO autenticar(String numero) {
		return null; // no se implementa en esta clase
	}

	@Override
	public boolean perfil(UsuarioDTO usuario) {
		return false; // No se implementa en esta clase
	}

	@Override
	public String getOrigen(String numero) {
		return null;
	}

	@Override
	public Object actualizarTipo(String numero, String tipo) {
		return null; // No se implementa
	}

	@Override
	public UsuarioDTO autenticarAdmin(String numero, String password) {
		return null; // No se implementa
	}

	@Override
	public String getTimestamp(String numero) {
		return null;
	}

	@Override
	public String getEtiqueta(String numero) {
		return null;
	}

}
