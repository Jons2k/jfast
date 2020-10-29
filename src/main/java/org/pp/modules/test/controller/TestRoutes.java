package org.pp.modules.test.controller;

import com.jfinal.config.Routes;

public class TestRoutes extends Routes{

	@Override
	public void config() {	
		add("/test/product", ProductController.class);
	}

}