package org.pp.modules.sys.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

import java.util.List;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.sys.model.Dict;
import org.pp.modules.sys.service.DictService;
import org.pp.modules.sys.vo.Tree;

public class DictController extends AdminController<Dict> {

	@Inject
	DictService service;
	@Override
	protected BaseService<Dict> getService() {
		return service;
	}
	
	/**
	 * 下拉选择
	 */
	public void select() {
		if(isParaBlank("dict")) {
			renderJson(RestObject.error("非法请求!"));
			return;
		}
		Dict d = service.findFirst(Kv.by("code = ", getPara("dict")));
		if(d == null) {
			renderJson(RestObject.error("非法请求!"));
			return;
		}
		List<Dict> list = service.all(Kv.by("pid = ", d.getId()), "list_sort ASC");
		renderJson(RestObject.success(list.size(),list));
	}

	/**
	 * 左侧导航
	 */
	public void left() {
		List<Tree> tree = service.left(0l);
		renderJson(RestObject.success(tree.size(),tree));
	}
	
	/**
	 * 分类
	 */
	public void category() {
		List<Dict> list = service.category();
		renderJson(RestObject.success(list.size(),list));
	}
	
	/**
	 * 字典数据解析
	 */
	public void format() {
		setAttr("list", service.all(null));
		render("/view/public/dict.html");
	}
	
	/**
	 * 批量保存
	 */
	public void batch() {
		for(int i=0;i<5;i++) {
			if(isParaBlank("code_"+i) || isParaBlank("code_"+i) || isParaBlank("code_"+i)) {
				continue;
			}
			Dict d = new Dict();
			d.setCode(getPara("code_"+i));
			d.setListSort(getParaToInt("listSort_"+i));
			d.setPid(getParaToInt("pid"));
			d.setTitle(getPara("title_"+i));
			d.setType(getPara("type"));
			if(!service.insert(d)) {
				renderJson(RestObject.error("保存数据失败！"));
			}
		}
		renderJson(RestObject.success("保存数据成功！", ""));
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
}
