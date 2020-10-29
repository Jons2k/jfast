package org.pp.modules.dev.controller;

import org.pp.interceptor.BackendInterceptor;

import com.jfinal.config.Routes;

public class DevRoutes extends Routes{

	@Override
	public void config() {
	    addInterceptor(new BackendInterceptor());
	    setBaseViewPath("/view/");
		add("/dev/module", ModuleController.class);
		add("/dev/models", ModelsController.class);
		add("/dev/field", FieldController.class);	
		add("/dev/validation", ValidationController.class);	
		add("/dev/database", DatabaseController.class);		
		add("/dev/chart", ChartController.class);
		add("/dev/dataset", DatasetController.class);
		
	}

}
