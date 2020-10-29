package org.pp.modules.sys.controller;

import com.jfinal.aop.Inject;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.modules.sys.model.Token;
import org.pp.modules.sys.service.TokenService;

public class TokenController extends AdminController<Token> {

	@Inject
	TokenService service;
	@Override
	protected BaseService<Token> getService() {
		return service;
	}

}
