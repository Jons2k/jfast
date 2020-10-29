package org.pp.handler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pp.modules.dev.service.ModuleService;
import org.pp.utils.SecurityUtil;
import org.pp.modules.dev.model.Module;

import com.jfinal.handler.Handler;

public class HtmlHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		
		List<Module> list = ModuleService.dao.findAll();
		for(Module m : list) {
			if(target.startsWith("/"+m.getCode().toLowerCase()+"/") && target.endsWith(".html")) {
				request.setAttribute("_target", target);
				next.handle("/html/", request, response, isHandled);
				SecurityUtil.releaseUser();
				return;
			}
		}
		
		next.handle(target, request, response, isHandled);
		SecurityUtil.releaseUser();
	}
}
