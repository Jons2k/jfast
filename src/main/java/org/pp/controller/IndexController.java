package org.pp.controller;

import org.pp.core.RestObject;
import org.pp.interceptor.BackendInterceptor;
import org.pp.modules.sys.model.User;
import org.pp.modules.sys.service.ConfigService;
import org.pp.modules.sys.service.NoticeService;
import org.pp.utils.SecurityUtil;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

@Before(BackendInterceptor.class)
public class IndexController extends Controller {
	@Inject
	NoticeService noticeService;
	
	public void index() {
		setAttr("_config", ConfigService.map());
		this.render("/view/public/admin.html");
	}
	
	public void welcome() {
		setAttr("notices", noticeService.findTop(null, 6));
		render("/view/public/welcome.html");
	}
	
	public void clear() {
		renderJson(RestObject.success("服务端清理缓存成功"));
	}
	
	public void init() {		
		Object o = this.getSession().getAttribute("user");		
		renderJson(SecurityUtil.init((User)o));
	}
}
