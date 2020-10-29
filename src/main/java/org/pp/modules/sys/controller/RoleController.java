package org.pp.modules.sys.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import java.util.List;
import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.sys.model.Role;
import org.pp.modules.sys.model.User;
import org.pp.modules.sys.service.MenuService;
import org.pp.modules.sys.service.RoleService;
import org.pp.modules.sys.service.UserService;
import org.pp.modules.sys.vo.Tree;
import org.pp.utils.HttpUtil;

public class RoleController extends AdminController<Role> {

	@Inject
	RoleService service;
	@Inject
	MenuService menuService;


	/**
	 * 保存角色菜单 
	 */
	public void menu() {
		if(isParaBlank("id")) {
			renderJson(RestObject.error("非法请求！"));
			return;
		}
		Long roleId = getParaToLong("id", 0l);
		String menus = getPara("menus", "");
		if(service.grant(roleId, menus.split(","))) {
			renderJson(RestObject.success("操作成功！"));
		}else {
			renderJson(RestObject.error("操作失败！"));
		}
	}

	
	/**
	 * 角色分配菜单树形
	 */
	public void menus() {
		if(isParaBlank("roleId")) {
			renderJson(RestObject.error("非法请求！"));
			return;
		}
		List<Tree> tree = service.menus(getParaToLong("roleId", 0l));
		renderJson(RestObject.success(tree.size(),tree));
	}
	
	/**
	 * 角色下的用户
	 */
	public void user() {		
		if(HttpUtil.isGet(getRequest())) {
			if(isParaBlank("id")) {
				error("404", "非法请求");
				return;
			}
			long roleId = getParaToLong("id");
			List<User> list1 = UserService.dao.find("SELECT * FROM sys_user WHERE id IN (SELECT user_id FROM sys_user_role WHERE role_id=?)", roleId);
			List<User> list2 = UserService.dao.find("SELECT * FROM sys_user WHERE id NOT IN (SELECT user_id FROM sys_user_role WHERE role_id=?)", roleId);
			
			setAttr("selected", list1);
			setAttr("noselect", list2);
			keepPara();
			parseRoute();
			setAttr("_action", "user");
			render("/view/sys/role/user.html");
		}else {
			if(isParaBlank("id")) {
				renderJson(RestObject.error("非法请求！"));
				return;
			}	
			List<String> values = getChecked("user");
			
			long[] rs = null;
			if(values.size() > 0) {
				rs = new long[values.size()];
				for(int i=0;i<rs.length;i++) {
					rs[i] = Long.valueOf(values.get(i));
				}
			}
			if(service.grantUser(getParaToLong("id"), rs)) {
				renderJson(RestObject.success("授权成功！"));
			}else {
				renderJson(RestObject.error("授权失败！"+service.getError()));
			}
		}
	}
	
	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("code")) {
			kv.put("code like ", "'%"+getPara("code")+"%'");
		}		
		if(!isParaBlank("title")) {
			kv.put("title like ", "'%"+getPara("title")+"%'");
		}		
		if(!isParaBlank("status")) {
			kv.put("status = ", getPara("status"));
		}			
		return kv;
	}
	
	@Override
	protected BaseService<Role> getService() {
		return service;
	}

}
