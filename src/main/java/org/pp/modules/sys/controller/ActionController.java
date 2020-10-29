package org.pp.modules.sys.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.sys.model.Action;
import org.pp.modules.sys.service.ActionService;

public class ActionController extends AdminController<Action> {
	@Inject
	ActionService service;
	
	/**
	 * 扫描生成操作行为
	 */
	public void scan() {
		service.scan();
		renderJson(RestObject.success("操作成功", ""));
	}
	
	@Override
	protected BaseService<Action> getService() {
		return service;
	}
	
	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("modelId")) {
			kv.put("model_id = ", getParaToLong("modelId"));
		}		
		if(!isParaBlank("appId")) {
			kv.put("app_id = ", getParaToLong("appId"));
		}	
		if(!isParaBlank("title")) {
			kv.put("title like ", "%"+getPara("title")+"%");
		}	
		return kv;
	}
}
