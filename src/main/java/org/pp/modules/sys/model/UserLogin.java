package org.pp.modules.sys.model;

import org.pp.modules.sys.base.BaseUserLogin;
import org.pp.modules.sys.service.UserService;

import com.jfinal.aop.Aop;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class UserLogin extends BaseUserLogin<UserLogin> {
	private User user;
	
	public User getUser() {
		if(user == null && getUserId() != null) {
			user = Aop.get(UserService.class).findCache(getUserId());
		}
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}