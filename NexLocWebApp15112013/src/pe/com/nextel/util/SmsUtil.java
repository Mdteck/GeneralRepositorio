package pe.com.nextel.util;

import org.me.ws.SmsWSService;
import org.me.ws.SmsWSServiceService;

public class SmsUtil {
	
	public static boolean sendSMS(String numero, String mensaje){
		try { 
            SmsWSServiceService service = new org.me.ws.SmsWSServiceService();
            SmsWSService port = service.getSmsWSServicePort();
            String usuario = PropertyUtil.readProperty("SMS_USER");
            String password = PropertyUtil.readProperty("SMS_PASS");
            String result = port.envioMensajesRsrv(usuario, password, numero, mensaje);
            if(!result.equals("999"))	return true;
            else						return false;

        } catch (Exception ex) {
        	return false;
        }
	}


}
