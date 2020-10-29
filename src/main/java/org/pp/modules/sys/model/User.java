package org.pp.modules.sys.model;

import java.io.Serializable;
import java.util.List;

import org.pp.modules.sys.base.BaseUser;
import org.pp.modules.sys.service.DepartmentService;
import org.pp.modules.sys.service.RoleService;

import com.jfinal.aop.Aop;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class User extends BaseUser<User> implements Serializable {
	private Department depart;
	private List<Role> roles;

	public Department getDepart() {
		if(depart == null && getDepartId() != null) {
			depart = Aop.get(DepartmentService.class).findCache(getDepartId());
		}
		return depart;
	}

	public void setDepart(Department depart) {
		this.depart = depart;
	}

	public List<Role> getRoles() {
		if(roles == null) {
			roles = CacheKit.get("user_role", "user_"+getId());
			if(roles == null) {
				roles = Aop.get(RoleService.class).getUserRoles(getId());
				CacheKit.put("user_role", "user_"+getId(), roles);
			}
			
		}
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
}
