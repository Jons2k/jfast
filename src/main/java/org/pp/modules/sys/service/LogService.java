package org.pp.modules.sys.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.pp.core.BaseService;
import org.pp.modules.sys.model.Action;
import org.pp.modules.sys.model.Log;
import org.pp.utils.SecurityUtil;

public class LogService implements BaseService<Log>{
	
	public static Log dao = new Log().dao();
	private String error = "";
	
	public static void log(HttpServletRequest request) {
		String uri = request.getRequestURI();
		Action a = ActionService.map().get(uri);
		if(a == null) {
			a = ActionService.map().get(request.getMethod().toLowerCase()+":"+uri);
		}
		if(a == null) return ;
		
		Log l = new Log();
		l.setActionId(a.getId());
		l.setActionTime(new Date());
		l.setIp(request.getRemoteAddr());
		l.parseParam(request);
		l.setUserId(SecurityUtil.userId());
		l.save();
	}
	
	
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	@Override
	public Log getModel() {
		return dao;
	}
}
