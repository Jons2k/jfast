package org.pp.modules.sys.model;

import org.pp.modules.dev.service.ModelsService;
import org.pp.modules.dev.service.ModuleService;
import org.pp.modules.dev.model.Models;
import org.pp.modules.dev.model.Module;
import org.pp.modules.sys.base.BaseAction;

import com.jfinal.aop.Aop;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Action extends BaseAction<Action> {
	private Module module;
	private Models model;

	public Models getModel() {
		if(model == null) {
			model = Aop.get(ModelsService.class).findCache(getModelId());
		}
		return model;
	}

	public void setModel(Models model) {
		this.model = model;
	}

	public Module getModule() {
		if(module == null && getModuleId() != null) {
			module = Aop.get(ModuleService.class).findCache(getModuleId());
		}
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

}