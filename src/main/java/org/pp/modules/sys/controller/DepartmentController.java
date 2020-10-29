package org.pp.modules.sys.controller;

import com.jfinal.aop.Inject;

import java.util.List;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.sys.model.Department;
import org.pp.modules.sys.service.DepartmentService;
import org.pp.modules.sys.vo.Tree;

public class DepartmentController extends AdminController<Department> {

	@Inject
	DepartmentService service;
	@Override
	protected BaseService<Department> getService() {
		return service;
	}

	/**
	 * 下拉选择
	 */
	public void select() {
		List<Tree> tree = service.tree(0l);
		if(tree == null) renderJson(RestObject.success(null));
		else renderJson(RestObject.success(tree.size(),tree));
	}
	

	@Override
	protected String getOrder() {
		return get("orderby" , "list_sort asc");
	}
}
