package pe.com.nextel.interceptor;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class ApplicationInterceptor implements Interceptor {

	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> session = ActionContext.getContext().getSession();
	    Object idUsuario = session.get("idUsuario");
	    if (idUsuario == null) {
	    	System.out.println(invocation.getAction().getClass().getName());
	    	if(invocation.getAction().getClass().getName().equals("pe.com.nextel.action.SeguridadAction"))
	    		return invocation.invoke();
	    	else
	    		return "login";
	    } else {
	        return invocation.invoke ();
	    }
	}

}
