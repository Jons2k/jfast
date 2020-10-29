package org.pp.controller;

import org.pp.core.BaseController;
import org.pp.modules.sys.service.ConfigService;
import org.pp.utils.SecurityUtil;

public class HtmlController extends BaseController {

	public void index() {
		parseRoute(getAttrForStr("_target"));
		
		String module = getAttrForStr("_module");
		String controller = getAttrForStr("_controller");
		String action = getAttrForStr("_action");
		String view = "/view/"+module+"/"+controller+"/";
		if("imports".equalsIgnoreCase(action)) {
			view = "/view/public/imports.html";
		}else {
			view += action+".html";
		}
		this.setAttr("_config", ConfigService.map());
		this.setAttr("_user", SecurityUtil.user());
		keepPara();
		render(view);
	}

	public void add() {

	}

	public void edit() {

	}

	public void imports() {

	}
}
