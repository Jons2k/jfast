package org.pp.modules.sys.controller;

import org.pp.interceptor.BackendInterceptor;

import com.jfinal.config.Routes;

public class SysRoutes extends Routes{

	@Override
	public void config() {	
		
		setBaseViewPath("/view");
		
	    addInterceptor(new BackendInterceptor());
	    
		add("/sys/action", ActionController.class);
		add("/sys/application", ApplicationController.class);
		add("/sys/category", CategoryController.class);
		add("/sys/config", ConfigController.class);
		add("/sys/department", DepartmentController.class);
		add("/sys/dict", DictController.class);
		add("/sys/file", FileController.class);
		add("/sys/log", LogController.class);
		add("/sys/menu", MenuController.class);
		add("/sys/role", RoleController.class);
		add("/sys/token", TokenController.class);
		add("/sys/user", UserController.class);
		add("/sys/userlogin", UserLoginController.class);
		add("/sys/notice", NoticeController.class);
	}

}