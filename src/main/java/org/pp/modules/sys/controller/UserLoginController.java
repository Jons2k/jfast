package org.pp.modules.sys.controller;

import com.jfinal.aop.Inject;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.modules.sys.model.UserLogin;
import org.pp.modules.sys.service.UserLoginService;

public class UserLoginController extends AdminController<UserLogin> {

	@Inject
	UserLoginService service;


	@Override
	protected BaseService<UserLogin> getService() {
		return service;
	}

}
