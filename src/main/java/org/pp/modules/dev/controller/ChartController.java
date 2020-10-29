package org.pp.modules.dev.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;

import java.util.Enumeration;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.dev.model.Chart;
import org.pp.modules.dev.model.Models;
import org.pp.modules.dev.service.ChartService;

public class ChartController extends AdminController<Chart> {
	@Inject
	ChartService service;
	
	@Override
	protected BaseService<Chart> getService() {
		return service;
	}

	/**
	 * 生成报表代码
	 */
	public void gen() {
		String id = getPara("id");
		if(StrKit.isBlank(id)) {
			renderJson(RestObject.error("非法请求!"));
			return;
		}
		
		Chart chart = service.find(Long.valueOf(id));
		if(chart == null) {
			renderJson(RestObject.error("报表不存在!"));
			return;
		}		

		if(service.genCode(chart)) {
			renderJson(RestObject.success("生成代码成功!"));
		}else {
			renderJson(RestObject.error("生成代码失败!"));
		}		
	}
	
	/**
	 * 查看报表
	 */
	public void view() {
		if(isParaBlank("id") && isParaBlank("code")) {
			renderError(404);
			return;
		}
		Chart chart;
		if(!isParaBlank("id")) {
			chart = service.find(getParaToLong("id"));
		}else {
			chart = service.findFirst(Kv.by("code = ", getPara("code")));
		}
		
		if(chart == null) {
			renderError(404);
			return;
		}		
		
		keepPara();
		render("/view/charts/"+chart.getCode().toLowerCase()+".html");
	}

	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("cateId")) {
			kv.put("cate_id = ", getParaToLong("cateId"));
		}			
		if(!isParaBlank("code")) {
			kv.put("code like ", "%"+getPara("code")+"%");
		}	
		if(!isParaBlank("type")) {
			kv.put("type = ", getPara("type"));
		}			
		if(!isParaBlank("status")) {
			kv.put("status = ", getPara("status"));
		}				
		return kv;
	}
}
