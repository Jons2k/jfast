package org.pp.modules.sys.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

import java.util.List;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.dev.service.ModelsService;
import org.pp.modules.dev.service.ModuleService;
import org.pp.modules.sys.model.Menu;
import org.pp.modules.sys.service.ActionService;
import org.pp.modules.sys.service.MenuService;
import org.pp.modules.sys.vo.Tree;
import org.pp.utils.HttpUtil;

public class MenuController extends AdminController<Menu> {

	@Inject
	MenuService service;
	
	/**
	 * 上级分组
	 */
	public void groups() {
		Kv cond = Kv.by("type = ", "0");
		cond.put("pid = " , 0);
		if(!isParaBlank("appId")) {
			cond.put("app_id = ", getParaToInt("appId"));
		}
		List<Tree> tree = service.groups(cond);
		if(tree == null) renderJson(RestObject.success(null));
		else renderJson(RestObject.success(tree.size(),tree));
	}
		
	/**
	 * 菜单树形
	 */
	public void tree() {
		List<Tree> tree = service.tree(0l);
		renderJson(RestObject.success(tree.size(),tree));
	}
	
	/**
	 * 菜单关联行为
	 */
	public void action() {
		if(isParaBlank("id")) {
			renderJson(RestObject.error("非法请求！"));
			return;
		}
		Long menuId = getParaToLong("id", 0l);
		if(HttpUtil.isGet(getRequest())) {
			setAttr("actions", ActionService.dao.findAll());
			setAttr("modules", ModuleService.dao.findAll());
			setAttr("models", ModelsService.dao.findAll());
			setAttr("list", service.getMenuAction(menuId));
			keepPara();
			parseRoute();
			//setAttr("_action", "action");
			render("action.html");
		}else {
			if(service.addAction(menuId, getChecked("action"))) {
				renderJson(RestObject.success("操作成功！"));
			}else {
				renderJson(RestObject.error("操作失败！"));
			}
		}
		
	}
	
	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("pid")) {
			kv.put("pid = ", getParaToLong("pid"));
		}		
		if(!isParaBlank("appId")) {
			kv.put("app_id = ", getParaToLong("appId"));
		}	
		if(!isParaBlank("title")) {
			kv.put("title like ", "%"+getPara("title")+"%");
		}		
		if(!isParaBlank("type")) {
			kv.put("type = ", getPara("type"));
		}		
		if(!isParaBlank("status")) {
			kv.put("status = ", getPara("appId"));
		}			
		return kv;
	}
	
	@Override
	protected BaseService<Menu> getService() {
		return service;
	}
}
