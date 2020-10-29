package org.pp.modules.dev.controller;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.modules.dev.service.FieldService;
import org.pp.modules.dev.model.Field;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

public class FieldController extends AdminController<Field> {

	@Inject
	FieldService service;
	@Override
	protected BaseService<Field> getService() {
		return service;
	}
	
	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("modelId")) {
			kv.put("model_id = ", getParaToLong("modelId"));
		}		
		if(!isParaBlank("code")) {
			kv.put("code like ", "%"+getPara("code")+"%");
		}	
		if(!isParaBlank("status")) {
			kv.put("status = ", getPara("status"));
		}		
		if(!isParaBlank("type")) {
			kv.put("type = ", getPara("type"));
		}			
		return kv;
	}
}
