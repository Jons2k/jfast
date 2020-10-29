package org.pp.modules.dev.service;

import java.util.Date;

import org.pp.core.BaseService;
import org.pp.modules.dev.model.Module;
import org.pp.modules.sys.model.Application;
import org.pp.modules.sys.service.ApplicationService;
import org.pp.utils.CodeUtil;
import org.pp.utils.ModelUtil;

import com.jfinal.aop.Aop;
import com.jfinal.kit.Kv;

public class ModuleService implements BaseService<Module>{
	
	public static Module dao = new Module().dao();
	private String error = "";
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	@Override
	public Module getModel() {
		return dao;
	}
	
	/**
	 * 生成模型代码
	 * @param module Module模块
	 * @return boolean
	 */
	public boolean gen(Module module) {
		if(!createApp(module)) return false;
		
		return CodeUtil.module(module.getCode().toLowerCase());
	}
	
	/**
	 * 创建模型的默认菜单
	 * @param model Model
	 * @return boolean
	 */
	public boolean createApp(Module module) {
		String url = module.getCode();
		ApplicationService apps = Aop.get(ApplicationService.class);
		Application app = apps.findFirst(Kv.by("url = ", url));
		if(app != null) {
			return true;			
		}
		
		app = new Application();
		app.setAddTime(new Date());
		app.setStatus("1");
		app.setTitle(module.getTitle());
		app.setType("i");
		app.setUrl(url);
		
		ModelUtil util = ModelUtil.get();
		if(util.save(app)) {
			return true;
		}else {
			error = util.getError();
			return false;
		}
	}
}
