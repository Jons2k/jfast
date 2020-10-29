package org.pp.modules.sys.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

import java.util.List;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.sys.model.Config;
import org.pp.modules.sys.service.ConfigService;
import org.pp.utils.HttpUtil;

public class ConfigController extends AdminController<Config> {

	@Inject
	ConfigService service;
	
	/**
	 * 上级分组
	 */
	public void groups() {
		List<Config> list = service.groups();
		renderJson(RestObject.success(list.size(),list));
	}
	
	public void set() {
		if(HttpUtil.isGet(getRequest())) {
			setAttr("list", service.all(null, "pid ASC,list_sort ASC"));
			parseRoute();
			render("/view/sys/config/set.html");
		}else {
			if(service.set(getParaMap())) {
				renderJson(RestObject.success("保存成功", ""));
			}else {
				renderJson(RestObject.error("保存失败!"+service.getError()));
			}
		}
	}
	
	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("pid")) {
			kv.put("pid = ", getParaToLong("pid"));
		}		
		if(!isParaBlank("code")) {
			kv.put("code like ", "%"+getPara("code")+"%");
		}
		if(!isParaBlank("title")) {
			kv.put("title like ", "%"+getPara("title")+"%");
		}		
		if(!isParaBlank("type")) {
			kv.put("type = ", getPara("type"));
		}			
		return kv;
	}
	
	@Override
	protected BaseService<Config> getService() {
		return service;
	}

}
