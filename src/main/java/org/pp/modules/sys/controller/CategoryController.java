package org.pp.modules.sys.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

import java.util.List;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.sys.model.Category;
import org.pp.modules.sys.service.CategoryService;
import org.pp.modules.sys.vo.Tree;

public class CategoryController extends AdminController<Category> {

	@Inject
	CategoryService service;
	
	/**
	 * 下拉选择
	 */
	public void select() {
		List<Tree> tree = service.tree(0l);
		if(tree == null) renderJson(RestObject.success(null));
		else renderJson(RestObject.success(tree.size(),tree));
	}
	
	
	@Override
	protected BaseService<Category> getService() {
		return service;
	}
	
	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("status")) {
			kv.put("status = ", getParaToLong("status"));
		}	
		if(!isParaBlank("title")) {
			kv.put("title like ", "%"+getPara("title")+"%");
		}	
		if(!isParaBlank("code")) {
			kv.put("code like ", "%"+getPara("code")+"%");
		}	
		return kv;
	}
}
