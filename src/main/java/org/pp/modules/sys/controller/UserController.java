package org.pp.modules.sys.controller;

import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;

import java.util.List;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.sys.model.User;
import org.pp.modules.sys.service.RoleService;
import org.pp.modules.sys.service.UserService;
import org.pp.utils.HttpUtil;
import org.pp.utils.SecurityUtil;

public class UserController extends AdminController<User> {

	@Inject
	UserService service;
	
	@Override
	protected BaseService<User> getService() {
		return service;
	}
	
	/**
	 * 重置密码
	 */
	public void repass() {
		String ids = getPara("id");
		if(StrKit.isBlank(ids)) {
			renderJson(RestObject.error("至少选择一个用户!"));
			return;
		}
		
		if(service.repass(ids.split(","))) {
			renderJson(RestObject.success("重置密码成功!"));
		}else {
			renderJson(RestObject.error("操作失败!"+service.getError()));
		}	
	}
	
	/**
	 * 用户授权
	 */
	public void grant() {		
		if(HttpUtil.isGet(getRequest())) {
			if(isParaBlank("id")) {
				error("404", "非法请求");
				return;
			}
			RoleService rs = Aop.get(RoleService.class);
			setAttr("roles", rs.all(Kv.by("status = ", "1")));
			setAttr("selects", rs.getUserRoles(getParaToLong("id")));
			keepPara();
			parseRoute();
			setAttr("_action", "grant");
			render("/view/sys/user/grant.html");
		}else {
			if(isParaBlank("id")) {
				renderJson(RestObject.error("非法请求！"));
				return;
			}	
			List<String> values = getChecked("role");
			long[] rs = null;
			if(values.size() > 0) {
				rs = new long[values.size()];
				for(int i=0;i<rs.length;i++) {
					rs[i] = Long.valueOf(values.get(i));
				}
			}
			if(service.grant(getParaToLong("id"), rs)) {
				renderJson(RestObject.success("授权成功！"));
			}else {
				renderJson(RestObject.error("授权失败！"+service.getError()));
			}
		}
	}
	
	/**
	 * 修改个人信息
	 */
	public void setting() {
		if(HttpUtil.isGet(getRequest())) {
			keepPara();
			parseRoute();
			setAttr("_action", "setting");
			render("/view/sys/user/setting.html");
		}else {
			User u = SecurityUtil.user();
			User bean = getBean(service.getModel().getClass(), "");
			u.setEmail(bean.getEmail());
			u.setNickname(bean.getNickname());
			u.setPhone(bean.getPhone());
			if(u.update()) {
				renderJson(RestObject.success("修改信息成功！"));
			}else {
				renderJson(RestObject.error("修改信息失败！"));
			}
		}
	}
	
	/**
	 * 修改密码
	 */
	public void password() {
		if(HttpUtil.isGet(getRequest())) {
			keepPara();
			parseRoute();
			setAttr("_action", "password");
			render("/view/sys/user/password.html");
		}else {
			
			if(isParaBlank("oldpass")) {
				renderJson(RestObject.error("原始密码不能为空！"));
				return;
			}
			if(isParaBlank("newpass")) {
				renderJson(RestObject.error("新密码不能为空！"));
				return;
			}
			User u = SecurityUtil.user();
			if(!u.getPassword().equals(SecurityUtil.password(getPara("oldpass"), u.getSalt()))) {
				renderJson(RestObject.error("原始密码不正确！"));
				return;
			}
			String password = SecurityUtil.password(getPara("newpass"), u.getSalt());
			u.setPassword(password);
			if(u.update()) {
				renderJson(RestObject.success("修改密码成功！"));
			}else {
				renderJson(RestObject.error("修改密码失败！"));
			}
		}
	}
	
	@Override
	public void info() {
		String ids = getPara("id");
		if(StrKit.isBlank(ids)) {
			renderJson(RestObject.error("非法请求!"));
			return;
		}
		User  bean = service.find(Long.valueOf(ids));
		if(bean == null) {
			renderJson(RestObject.error("对象不存在!"+service.getError()));
		}else {
			bean.setPassword(null);
			renderJson(RestObject.success(bean));
		}		
	}
	
	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("departId")) {
			kv.put("depart_id = ", getParaToLong("departId"));
		}	
		if(!isParaBlank("account")) {
			kv.put("account like ", "%"+getPara("account")+"%");
		}	
		if(!isParaBlank("email")) {
			kv.put("email like ", "%"+getPara("email")+"%");
		}	
		if(!isParaBlank("phone")) {
			kv.put("phone like ", "%"+getPara("phone")+"%");
		}		
		if(!isParaBlank("nickname")) {
			kv.put("nickname like ", "%"+getPara("nickname")+"%");
		}		
		if(!isParaBlank("status")) {
			kv.put("status = ", getPara("status"));
		}			
		return kv;
	}
}
