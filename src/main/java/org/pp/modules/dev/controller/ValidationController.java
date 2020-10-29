package org.pp.modules.dev.controller;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.modules.dev.service.ValidationService;
import org.pp.modules.dev.model.Validation;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

public class ValidationController extends AdminController<Validation> {
	@Inject
	ValidationService service;
	
	
	@Override
	protected BaseService<Validation> getService() {
		return service;
	}
	
	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("modelId")) {
			kv.put("model_id = ", getParaToLong("modelId"));
		}		
		if(!isParaBlank("fieldId")) {
			kv.put("field_id = ", getParaToLong("fieldId"));
		}		
		if(!isParaBlank("title")) {
			kv.put("title like ", "%"+getPara("title")+"%");
		}	
		if(!isParaBlank("rule")) {
			kv.put("rule = ", getPara("rule"));
		}		
		if(!isParaBlank("type")) {
			kv.put("type = ", getPara("type"));
		}			
		return kv;
	}
}
