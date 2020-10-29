package org.pp.modules.dev.controller;

import java.util.Enumeration;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.dev.service.ModelsService;
import org.pp.modules.dev.service.ModuleService;
import org.pp.modules.sys.service.ActionService;
import org.pp.utils.XDataUtil;
import org.pp.modules.dev.model.Models;
import org.pp.modules.dev.model.Module;

import com.jfinal.aop.Inject;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;

public class ModelsController extends AdminController<Models> {
	@Inject
	ModelsService service;
	@Inject
	ModuleService mservice;
	@Inject
	ActionService action;
	
	/**
	 * 生成模型代码
	 */
	public void gen() {
		if(!JFinal.me().getConstants().getDevMode()) {
			renderJson(RestObject.error("非开发模式，仅供参考!"));
			return;
		}
		String id = getPara("id");
		if(StrKit.isBlank(id)) {
			renderJson(RestObject.error("非法请求!"));
			return;
		}
		
		Models m = service.find(Long.valueOf(id));
		if(m == null) {
			renderJson(RestObject.error("模型不存在!"));
			return;
		}
		
		Enumeration<String> names = getParaNames();
		String[] contents = new String[10];
		String[] menus = new String[5];
		int ci = 0, mi=0;
		while(names.hasMoreElements()) {
			String name = names.nextElement();
			if(!"on".equalsIgnoreCase(getPara(name))) continue;
			
			if(name.startsWith("content[")) {
				name = name.substring(8);
				name = name.substring(0,name.length()-1);
				contents[ci++] = name;
			}else if(name.startsWith("menu[")) {
				name = name.substring(5);
				name = name.substring(0,name.length()-1);
				menus[mi++] = name;
			}else if("action".equalsIgnoreCase(name)) {
				action.models(m);
			}
		}

		if(service.genCode(m, contents, menus)) {
			renderJson(RestObject.success("生成代码成功!"));
		}else {
			renderJson(RestObject.error("生成代码失败!"));
		}
		
	}

	/**
	 * 根据数据表生成模型
	 */
	public void table() {
		Models  bean = getBean(Models.class, "");
		if(bean == null) {
			renderJson(RestObject.error("请检查数据！"));
			return;
		}
		if(service.createModel(bean)) {
			renderJson(RestObject.success("操作成功！"));
			return;
		}else {
			renderJson(RestObject.error("操作失败！"));
			return;
		}
	}
	
	/**
	 * 刷新表结构
	 */
	public void refresh() {
		String id = getPara("id");
		if(StrKit.isBlank(id)) {
			renderJson(RestObject.error("非法请求!"));
			return;
		}
		
		Models m = service.find(Long.valueOf(id));
		if(m == null) {
			renderJson(RestObject.error("模型不存在!"));
			return;
		}
		
		if(service.loadField(m)) {
			renderJson(RestObject.success("操作成功！"));
			return;
		}else {
			renderJson(RestObject.error("操作失败！"));
			return;
		}
	}
	
	/**
	 * 模型的SQL
	 */
	public void sqls() {		
		String id = getPara("id");
		if(StrKit.isBlank(id)) {
			renderJson(RestObject.error("非法请求!"));
			return;
		}
		
		Models m = service.find(Long.valueOf(id));
		if(m == null) {
			renderJson(RestObject.error("模型不存在!"));
			return;
		}
		XDataUtil util = new XDataUtil();
		set("sqls", util.sql(m));
		render("/view/dev/models/sqls.html");
	}
	
	@Override
	protected BaseService<Models> getService() {
		return service;
	}

	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("moduleId")) {
			kv.put("module_id = ", getParaToLong("moduleId"));
		}			
		if(!isParaBlank("moduleCode")) {
			Module  m = mservice.findFirst(Kv.by("code = ", getPara("moduleCode")));
			if(m != null) kv.put("module_id = ", m.getId());
		}			
		if(!isParaBlank("title")) {
			kv.put("title like ", "%"+getPara("title")+"%");
		}		
		return kv;
	}
}
