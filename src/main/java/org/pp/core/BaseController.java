package org.pp.core;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.jfinal.core.Controller;

public  class BaseController extends Controller {
	
	/**
	 * 渲染错误页面
	 * @param code
	 * @param error
	 */
	protected void error(String code, String error) {
		set("code", code);
		set("error", error);
		render("/view/public/error.html");
	}
	
	protected void parseRoute(String target) {		
		String[] path = target.split("/");
		String module = path[1];
		String controller = "";
		String action = "";
		if(path.length == 3 || "".equals(path[3])) {
			controller = path[2].substring(0,path[2].indexOf('.'));
			action = "list";
		}else {
			controller = path[2];
			int pos = path[3].indexOf('.');
			action = pos >0 ? path[3].substring(0,pos) : path[3];
		}
		this.setAttr("_action", action);
		this.setAttr("_module", module);
		this.setAttr("_controller", controller);
	}
	
	protected void parseRoute() {
		parseRoute(getRequest().getServletPath());	
	}
	
	/**
	 * 查询通过LayUI checkbox传递的值 name[value]=on
	 * @param name String 参数名
	 * @return List<String>
	 */
	protected List<String> getChecked(String name) {
		Enumeration<String> names = getParaNames();
		List<String> values = new ArrayList<>();
		while(names.hasMoreElements()) {
			String n = names.nextElement();
			if(n.startsWith(name+"[") && "on".equalsIgnoreCase(getPara(n))) {
				n = n.substring(name.length()+1);
				n = n.substring(0, n.length()-1);
				values.add(n);
			}
		}
		return values;
	}
}
