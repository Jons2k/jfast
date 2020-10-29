package org.pp.modules.sys.controller;

import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.modules.sys.model.File;
import org.pp.modules.sys.service.FileService;

public class FileController extends AdminController<File> {

	@Inject
	FileService service;
	
	public void down() {
		File f = null;
		if(!isParaBlank("fileId")) {
			f = service.findCache(getParaToLong("fileId"));
		}else if(!isParaBlank("id")) {
			f = service.findCache(getParaToLong("id"));
		}else if(!isParaBlank("code")) {
			f = service.findFirst(Kv.by("md5 = ", get("code")));
		}
		if(f == null) {
			error("404", "非法请求!");
			return;
		}
		
		renderFile(f.getPath(), f.getName());
	}
	
	@Clear
	public void view() {
		File f = null;
		if(!isParaBlank("fileId")) {
			f = service.findCache(getParaToLong("fileId"));
		}else if(!isParaBlank("id")) {
			f = service.findCache(getParaToLong("id"));
		}else if(!isParaBlank("code")) {
			f = service.findFirst(Kv.by("md5 = ", get("code")));
		}
		if(f == null) {
			error("404", "非法请求!");
			return;
		}
		set("vo", f);
		if(f.isImage()) {
			render("/view/sys/file/view_image.html");
		}else if(f.isPdf()) {
			render("/view/sys/file/view_pdf.html");
		}else if(f.getExts().equalsIgnoreCase("mp4")) {
			render("/view/sys/file/view_video.html");
		}else {
			render("/view/sys/file/view_noplay.html");
		}
	}
	
	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("uploadUserId")) {
			kv.put("upload_user_id = ", getParaToLong("uploadUserId"));
		}		
		if(!isParaBlank("exts")) {
			kv.put("exts = ", getParaToLong("exts"));
		}		
		if(!isParaBlank("status")) {
			kv.put("status = ", getParaToLong("status"));
		}	
		if(!isParaBlank("name")) {
			kv.put("name like ", "%"+getPara("name")+"%");
		}	
		if(!isParaBlank("startTime")) {
			kv.put("upload_time > ", getPara("startTime"));
		}	
		if(!isParaBlank("endTime")) {
			kv.put("upload_time > ", getPara("endTime"));
		}	
		return kv;
	}
	
	@Override
	protected BaseService<File> getService() {
		return service;
	}

}
