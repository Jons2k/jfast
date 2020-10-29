package org.pp.modules.sys.model;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.pp.modules.sys.base.BaseLog;
import org.pp.modules.sys.service.ActionService;
import org.pp.modules.sys.service.UserService;

import com.jfinal.aop.Aop;
import com.jfinal.kit.JsonKit;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Log extends BaseLog<Log> {
	private Action action;
	private User user;

	public Action getAction() {
		if(action == null) {
			action = Aop.get(ActionService.class).findCache(getActionId());
		}
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	

	public User getUser() {
		if(user == null && getUserId() != null) {
			user = Aop.get(UserService.class).findCache(getUserId());
		}
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void parseParam(HttpServletRequest request) {
		Map<String,String> p = new HashMap<>();
		Enumeration<String> ps = request.getParameterNames();
		while(ps.hasMoreElements()) {
			String n = ps.nextElement();
			String v = request.getParameter(n);
			if(v.length() > 200) continue;
			p.put(n, v);
		}
		setParam(JsonKit.toJson(p));
	}
}
