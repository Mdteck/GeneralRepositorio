package pe.com.nextel.localizacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.com.nextel.bean.ErrorDTO;
import pe.com.nextel.bean.GrupoDTO;
import pe.com.nextel.bean.LogDTO;
import pe.com.nextel.bean.TransaccionDTO;
import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.dao.iface.GrupoDAO;
import pe.com.nextel.util.ServicioLocalizacionConnectionUtil;
import pe.com.nextel.util.XMLUtil;

public class LocalizacionGrupoDAO implements GrupoDAO {

	@Override
	public List<GrupoDTO> listar(int idMonitor) {
		return null; // no se implementa en esta clase
	}

	@Override
	public List<GrupoDTO> listarPorCuenta(int idCuenta) {
		return null; // no se implementa en esta clase
	}

	@Override
	public int registrar(GrupoDTO grupo) {
		return 0; // No se implementa en esta clase
	}

	@Override
	public boolean modificar(GrupoDTO grupo) {
		return false; // no se implementa en esta clase
	}

	@Override
	public boolean eliminar(int idGrupo) {
		return false; // No se implementa en esta clase
	}

	@Override
	public boolean asignarGrupo(int idGrupo, List<UsuarioDTO> usuarios) {
		return false; // no se implementa en esta clase
	}

	@Override
	public GrupoDTO obtener(int idGrupo) {
		return null; // no se implementa en esta clase
	}

	@Override
	public TransaccionDTO localizar(List<UsuarioDTO> usuarios, LogDTO logDTO) {
		long timestampInicio = new Date().getTime();
//		String xmlResponse=timestampInicio+""+timestampInicio+"6<?xml version=\"1.0\" ?><!DOCTYPE svc_result SYSTEM \"MLP_SVC_RESULT_300.DTD\"><svc_result ver=\"3.0.0\"><slia ver=\"3.0.0\"><pos><msid enc=\"ASC\" type=\"MIN\">51946198304</msid><poserr><result resid=\"5\">ABSENT SUBSCRIBER</result><add_info>Target not reachable</add_info><time utc_off=\"+0000\">20130820041400</time></poserr></pos><pos><msid enc=\"ASC\" type=\"MIN\">51946576640</msid><pd><time utc_off=\"+0000\">20131116042200</time><shape><CircularArea srsName=\"www.epsg.org#4326\"><coord><X>11 59 12.000046S</X><Y>77 03 42.994995W</Y><Z>NaN</Z></coord><radius>592</radius></CircularArea></shape><lev_conf>100</lev_conf></pd><gsm_net_param><neid><vmscid><cc>51</cc><ndc>1</ndc><vmscno>981500005</vmscno></vmscid></neid></gsm_net_param></pos></slia></svc_result>";
		String xmlResponse = ServicioLocalizacionConnectionUtil.llamarAplicacionConexionLocalizacion(usuarios,logDTO.getIdLog());
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

}
