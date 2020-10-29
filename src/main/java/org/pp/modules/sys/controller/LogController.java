package org.pp.modules.sys.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.sys.model.Log;
import org.pp.modules.sys.service.LogService;

public class LogController extends AdminController<Log> {

	@Inject
	LogService service;
	@Override
	protected BaseService<Log> getService() {
		return service;
	}
	
	/**
	 * 按条件删除
	 */
	public void dels() {
		Kv kv = search();
		if(kv.isEmpty()) {
			renderJson(RestObject.error("请至少选择一个条件!"));
			return;
		}
		int ret = service.delete(kv);
		renderJson(RestObject.success("删除"+ret+"条记录!"));
	}

	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("actionId")) {
			kv.put("action_id = ", getParaToLong("actionId"));
		}	
		if(!isParaBlank("userId")) {
			kv.put("user_id = ", getParaToLong("userId"));
		}	
		if(!isParaBlank("startTime")) {
			kv.put("action_time > ", getPara("startTime"));
		}	
		if(!isParaBlank("endTime")) {
			kv.put("action_time > ", getPara("endTime"));
		}	
		return kv;
	}
}
