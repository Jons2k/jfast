package org.pp.interceptor;

import javax.servlet.http.HttpServletResponse;

import org.pp.core.RestObject;
import org.pp.modules.sys.model.User;
import org.pp.modules.sys.service.TokenService;
import org.pp.utils.HttpUtil;
import org.pp.utils.SecurityUtil;

import com.jfinal.aop.Aop;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
public class ExceptionInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		Controller c = inv.getController();
		try {
			//跨域
			String access = c.getRequest().getRequestURI().toLowerCase();
			if(!access.endsWith(".html")) {
				crossOrigin(c.getResponse());
			}
			
			//加载当前可能登录的用户
			Object user = c.getSessionAttr("user");
			if(user == null) {
				loadTokenUser(c);
			}else {
				SecurityUtil.setUser((User)user);
			}
			
			inv.invoke();
//			SecurityUtil.releaseUser();
		}catch (Exception e) {
			e.printStackTrace();
			SecurityUtil.releaseUser();
			if(HttpUtil.isAjax(c.getRequest())) {
				c.renderJson(RestObject.error("500", e.getMessage(),""));
			}else {
				c.renderError(500);
			}
		}		
	}

	/**
	 * 加载令牌中的用户信息
	 * @param c
	 */
	protected void loadTokenUser(Controller c) {
		String code = c.getHeader("xtoken");
		
		if(StrKit.isBlank(code)) {
			return;
		}
		
		User user = Aop.get(TokenService.class).getTokenUser(code);
		if(user != null) {
			SecurityUtil.setUser(user);
		}
	}
	
	/**
	 * 增加跨域响应头信息
	 * @param response
	 */
	private void crossOrigin(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers",
				"Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, XToken");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // 支持HTTP 1.1.   
	    response.setHeader("Pragma", "no-cache"); // 支持HTTP 1.0. response.setHeader("Expires", "0");   
	}
}
