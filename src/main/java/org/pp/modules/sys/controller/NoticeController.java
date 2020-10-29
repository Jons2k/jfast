package org.pp.modules.sys.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.sys.model.Notice;
import org.pp.modules.sys.service.NoticeService;
import org.pp.utils.SecurityUtil;

public class NoticeController extends AdminController<Notice> {

	@Inject
	NoticeService service;

	/**
	 * 记录阅读
	 */
	public void read() {
		if(isParaBlank("id")) {
			renderJson(RestObject.error("非法请求!"));
			return ;
		}
		
		if(service.read(getParaToLong("id"), SecurityUtil.user().getId())) {
			renderJson(RestObject.error("操作成功!"));
			return ;
		}else {
			renderJson(RestObject.error("操作失败!"+service.getError()));
			return ;
		}
	}

	@Override
	protected BaseService<Notice> getService() {
		return service;
	}
	
	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("addUserId")) {
			kv.put("add_user_id = ", getParaToLong("addUserId"));
		}	
		if(!isParaBlank("title")) {
			kv.put("title like ", "%"+getPara("title")+"%");
		}	
		if(!isParaBlank("startTime")) {
			kv.put("add_time > ", getPara("startTime"));
		}	
		if(!isParaBlank("endTime")) {
			kv.put("add_time < ", getPara("endTime"));
		}	
		return kv;
	}
}
