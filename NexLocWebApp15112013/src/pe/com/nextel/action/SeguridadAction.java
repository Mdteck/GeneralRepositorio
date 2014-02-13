package pe.com.nextel.action;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import pe.com.nextel.bean.UsuarioDTO;
import pe.com.nextel.service.BusinessDelegate;
import pe.com.nextel.service.UsuarioService;
import pe.com.nextel.util.LoggerUtil;
import pe.com.nextel.util.PropertyUtil;

import com.opensymphony.xwork2.ActionSupport;

public class SeguridadAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	//******************** Variables *************************
	private UsuarioDTO usuario;
	private String numero;
	private String tipo;
	private String tipoRpta;												//Tipo de Mensaje que se le devuelve a la interfaz (OK o ERROR)
	private String mensaje;		
	private String password;
	//Mensaje que se le devuelve a la interfaz
	UsuarioService usuarioService=BusinessDelegate.getUsuarioService();		//Instancia del servicio de Usuario para acceder a la BD
	Logger logger = LoggerUtil.getInstance();
//	Logger logger = Logger.getLogger(SeguridadAction.class.getName());
	
	//******************** Setter & Getter *********************
	
	
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getTipoRpta() {
		return tipoRpta;
	}
	public void setTipoRpta(String tipoRpta) {
		this.tipoRpta = tipoRpta;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
	//******************* Motodos **************************
	
	public String login(){	
		
		logger.info("INICIO Login");
		String numero = getNumero();
		String tipo=getTipo();
		if(numero==null || tipo == null){
//			logger.warn("Campos recibidos (usuario,tipo) invalidos");
			logger.info("Campos recibidos (usuario,tipo) invalidos");
			setMensaje("Por favor, ingrese todos los campos");
			logger.info("FIN Login");
			return ERROR;
		}else{
			logger.info("Tipo de Usuario logueado :"+tipo);
			logger.info("Usuario Logueado con numero: "+numero);
			if(tipo.equals("1"))		tipo="2";
			else if(tipo.equals("2"))	tipo="1";
			UsuarioDTO usuario;
			if(tipo.equals("0") || tipo.equals("3")){
				String password = getPassword();
				if(password==null){
//					logger.warn("No se ingreso ningun password valido");
					logger.info("No se ingreso ningun password valido");
					setMensaje("Por favor, ingrese todos los campos");
					logger.info("FIN Login");
					return ERROR;
				}
					usuario = usuarioService.autenticarAdmins(numero, password);
			}
			else	usuario = usuarioService.autenticar(numero);

			if(usuario==null){
				setMensaje("Usuario no encontrado.");
				logger.info("Usuario "+numero+" no encontrado");
//				logger.warn("Usuario "+numero+" no encontrado");
				logger.info("FIN Login");
				return ERROR;
			}else{
				if(!tipo.equals("0") && !tipo.equals("3"))	usuarioService.actualizarTipo(numero, tipo);
				HttpServletRequest request =  ServletActionContext.getRequest();
		        HttpSession session = request.getSession(true); 
			    session.setAttribute("idUsuario", usuario.getIdUsuario());
			    session.setAttribute("usuario", usuario.getEtiqueta());
			    session.setAttribute("numero", usuario.getNumero());
			    session.setAttribute("idCuenta", usuario.getCuenta().getIdCuenta());
			    session.setAttribute("tipo", tipo);
			    setTipoRpta("OK");
			    logger.info("OK Login exitoso");
			    logger.info("FIN Login");
				return SUCCESS;
			}
		}
	}
	
	public String logout(){
		logger.info("INICIO Logout");
		HttpServletRequest request =  ServletActionContext.getRequest();
		HttpSession session = request.getSession(true); 
	    session.removeAttribute("idUsuario");
	    session.removeAttribute("usuario");
	    session.removeAttribute("numero");
	    session.removeAttribute("idCuenta");
	    session.removeAttribute("tipo");
		session.invalidate();
		setMensaje("Ud. ha salido de su sesion.");
		logger.info("FIN Logout");
		return "logout";
	}
}
