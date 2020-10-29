package org.pp.interceptor;

import org.pp.consts.RestConst;
import org.pp.core.RestObject;
import org.pp.modules.sys.model.User;
import org.pp.modules.sys.service.LogService;
import org.pp.utils.HttpUtil;
import org.pp.utils.SecurityUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * 后台拦截器
 * 1、强制用户登录，如果未登录，强制跳转到登录页面
 * 2、判断是否有访问权限，如果没有，强制跳转到登录页面
 * 3、记录操作日志
 * @author Administrator
 *
 */
public class BackendInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		Controller c = inv.getController();
		User user = SecurityUtil.user();
		if(user != null) {
			c.setAttr("_user", user);
			String access = c.getRequest().getRequestURI();
			if(SecurityUtil.checkAccess(access)) {
				LogService.log(inv.getController().getRequest());
				inv.invoke();
			}else {
				login(c, RestConst.DENY, "您没有权限");
			}
		}else {
			login(c, RestConst.NO_AUTH, "您没有没有登录");
		}		
	}
	
	protected void login(Controller c, String code, String msg) {
		boolean isAjax = HttpUtil.isAjax(c.getRequest());
		if(isAjax) {
			c.renderJson(RestObject.error(code, msg, c.getRequest().getContextPath()+SecurityUtil.loginUrl()));
		}else {
			String access = c.getRequest().getRequestURI();
			c.setSessionAttr("redirect", access);
			c.set("redirect", access);
			c.render("/view/public/login.html");
		}
	}
	
}
