package pe.com.nextel.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MailUtil {
	
	public static boolean enviarMail1(String subject, String texto){
		Properties properties = new Properties();
		
		properties.put("mail.smtp.host", PropertyUtil.readProperty("SMTP_HOST"));  
	    properties.put("mail.smtp.starttls.enable", "true");  
	    properties.put("mail.smtp.port", PropertyUtil.readProperty("SMTP_PORT"));  
	    properties.put("mail.smtp.mail.sender", PropertyUtil.readProperty("SMTP_MAIL_USER"));  
	    properties.put("mail.smtp.user", PropertyUtil.readProperty("SMTP_MAIL_USER"));  
	    properties.put("mail.smtp.auth", "true");  
	    
	    Session session = Session.getDefaultInstance(properties);
	    MimeMessage message = new MimeMessage(session); 
        try {
			message.setFrom(new InternetAddress((String)properties.get("mail.smtp.mail.sender")));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(PropertyUtil.readProperty("MAIL_DESTINO")));  
	        message.setSubject(subject);  
	        message.setText(texto);  
	        Transport t = session.getTransport("smtp");  
	        t.connect((String)properties.get("mail.smtp.user"), PropertyUtil.readProperty("SMTP_MAIL_PASS"));  
	        t.sendMessage(message, message.getAllRecipients());  
	        t.close(); 
	        return true;
		} catch (AddressException e) {
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
         
	}
	
	
	private static Session getMailmyMailSession(){
        Context c;
		try {
			c = new InitialContext();
			return (Session) c.lookup("java:comp/env/email/NSN");
//			return (Session) c.lookup("email/NSN");
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
        
    }
	
	public static boolean enviarMailContext(String email, String subject, String body) {
       /* Session mailSession = getMailmyMailSession();
        if(mailSession!=null){
        	MimeMessage message = new MimeMessage(mailSession);
            try {
				message.setSubject(subject);
				message.setRecipients(RecipientType.TO, InternetAddress.parse(email, false));
	            message.setText(body);
	            Transport.send(message);
	            return true;
			} catch (MessagingException e) {
				e.printStackTrace();
				return false;
			}            
        }else*/
        	return true;
        
    }
	
	public static boolean enviarMailContext1(String subject, String body){
		return enviarMailContext(PropertyUtil.readProperty("MAIL_DESTINO"), subject, body );
	}
	

}
