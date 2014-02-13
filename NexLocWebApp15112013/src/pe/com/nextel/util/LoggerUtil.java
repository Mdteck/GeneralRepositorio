package pe.com.nextel.util;

import java.io.IOException;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerUtil {
	private static Logger logger = null;
	private static Logger loggerError = null;
	
	public static Logger getInstance() {
		
	      if(logger == null) {
	    	  try {
	    	      boolean append = true;
	    	      int limit = 2000000; // 2 Mb
	    	      int numLogFiles = 5;	    	      
	    	      FileHandler fh = new FileHandler("C:\\Temp\\NexLocWebApp.log", limit, numLogFiles, append);
//	    	      FileHandler fh = new FileHandler(System.getProperty("com.sun.aas.instanceRoot")+"\\logs\\NexLocWebApp.log", limit, numLogFiles, append);
	    	      
	    	      fh.setFormatter(new Formatter(){
	    	         public String format(LogRecord rec) {
	    	        	long thread = Thread.currentThread().getId();
	    	            StringBuffer buf = new StringBuffer(1000);
	    	            buf.append(new java.util.Date());
	    	            buf.append('\t');
	    	            buf.append(thread);
	    	            buf.append('\t');
	    	            buf.append(formatMessage(rec));
	    	            buf.append('\n');
	    	            return buf.toString();
	    	            }
	    	          });
	    	      logger = Logger.getLogger("NexLocWebApp");
	    	      logger.addHandler(fh);
	    	      Handler[] handlers = logger.getHandlers();
	    	      if (handlers[0] instanceof ConsoleHandler) {
	    	          logger.removeHandler(handlers[0]);
	    	      }
	    	    }
	    	    catch (IOException e) {
	    	      e.printStackTrace();
	    	    }
	      }
	      return logger;
	   }	
	
	public static Logger getInstanceError() {
	      if(loggerError == null) {
	    	  try {
	    	      boolean append = true;

//	    	      FileHandler fh = new FileHandler("/tmp/NexLocWebAppErrores.log", append);
	    	      FileHandler fh = new FileHandler("C:\\Temp\\NexLocWebAppErrores.log", append);
//	    	      FileHandler fh = new FileHandler(System.getProperty("com.sun.aas.instanceRoot")+"\\logs\\NexLocWebAppErrores.log", append);
	    	      fh.setFormatter(new Formatter(){
	    	         public String format(LogRecord rec) {
	    	            StringBuffer buf = new StringBuffer(1000);
	    	            buf.append(ParserUtil.toLoggerDate(new Date()));
	    	            buf.append('\t');
	    	            buf.append(formatMessage(rec));
	    	            buf.append('\n');
	    	            return buf.toString();
	    	            }
	    	          });
	    	      loggerError = Logger.getLogger("NexLocWebAppErrores");
	    	      loggerError.addHandler(fh);
	    	      Handler[] handlers = loggerError.getHandlers();
	    	      if (handlers[0] instanceof ConsoleHandler) {
	    	    	  loggerError.removeHandler(handlers[0]);
	    	      }
	    	    }
	    	    catch (IOException e) {
	    	      e.printStackTrace();
	    	    }
	      }
	      return loggerError;
	   }


}
