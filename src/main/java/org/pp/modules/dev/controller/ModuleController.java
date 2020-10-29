package org.pp.modules.dev.controller;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.dev.service.ModuleService;
import org.pp.modules.dev.model.Module;

import com.jfinal.aop.Inject;
import com.jfinal.core.JFinal;
import com.jfinal.kit.StrKit;

public class ModuleController extends AdminController<Module> {
	@Inject
	ModuleService service;
	
	/**
	 * 生成模块结构和代码
	 */
	public void gen() {
		if(!JFinal.me().getConstants().getDevMode()) {
			renderJson(RestObject.error("非开发模式，仅供参考!"));
			return;
		}
		String ids = getPara("id");
		if(StrKit.isBlank(ids)) {
			renderJson(RestObject.error("非法请求!"));
			return;
		}
		Module  bean = service.find(Long.valueOf(ids));
		if(bean == null) {
			renderJson(RestObject.error("非法请求!"));
			return;
		}
		
		if(service.gen(bean)) {
			renderJson(RestObject.success("生成代码成功!"));
		}else {
			renderJson(RestObject.error("生成代码失败!"+service.getError()));
		}
	}

	@Override
	protected BaseService<Module> getService() {
		return service;
	}
}
