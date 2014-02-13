package pe.com.nextel.interceptor;

import java.io.IOException;
 
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
public class LoginFilter implements Filter {

 
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
 
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);  
        
        if(session!=null){
        	if(session.getAttribute("usuario")==null) 	response.sendRedirect("../loginSessionTimeout.jsp");
	        else{
	        	chain.doFilter(req, res);	
	        }
        }else{
        	response.sendRedirect("../loginSessionTimeout.jsp");
        }
    }
    
    public void init(FilterConfig config) throws ServletException {
    	
    }
    
    public void destroy() {
        //add code to release any resource
    }
}