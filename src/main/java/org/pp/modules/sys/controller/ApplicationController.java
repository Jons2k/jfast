package org.pp.modules.sys.controller;

import com.jfinal.aop.Inject;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.modules.sys.model.Application;
import org.pp.modules.sys.service.ApplicationService;

public class ApplicationController extends AdminController<Application> {

	@Inject
	ApplicationService service;
	
	@Override
	protected BaseService<Application> getService() {
		return service;
	}

}
