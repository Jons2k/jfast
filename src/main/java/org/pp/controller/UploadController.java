package org.pp.controller;

import org.pp.core.RestObject;
import org.pp.interceptor.BackendInterceptor;
import org.pp.modules.sys.model.File;
import org.pp.modules.sys.service.FileService;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;

@Before(BackendInterceptor.class)
public class UploadController extends Controller {

	@Inject
	FileService service;
	
	public void excel() {
		UploadFile upload = getFile("file","tmp");
		File file = service.rename(upload, false);
		if(file == null) {
			upload.getFile().delete();
			renderJson(RestObject.error(service.getError()));
		}else {
			renderJson(RestObject.success(file));
		}		
	}
	
	public void image() {
		UploadFile upload = getFile("file","tmp");
		File file = service.rename(upload, true);
		if(file == null) {
			upload.getFile().delete();
			renderJson(RestObject.error(service.getError()));
		}else {
			renderJson(RestObject.success(file));
		}		
	}	
	
	public void file() {
		UploadFile upload = getFile("file","tmp");
		File file = service.rename(upload, true);
		if(file == null) {
			upload.getFile().delete();
			renderJson(RestObject.error(service.getError()));
		}else {
			renderJson(RestObject.success(file));
		}		
	}
	
	public void ueditor() {
		String action = getPara("action");
		if ("config".equals(action)) {
			setAttr("basePth", service.basePath());
			render("/view/public/ueditor_config.json");
			return;
		}else if(action.startsWith("upload")){
			UploadFile upload = getFile("upfile","tmp");
			File file = service.rename(upload, true);
			Ret ret = Ret.create();
				
			if(file == null) {
				upload.getFile().delete();
				ret.set("state", "FAILED");
			}else {
				ret.set("state", "SUCCESS")
						.set("url", PropKit.get("tpl.upload", "/upload/") + file.getPath())//文件上传地址
						.set("title", file.getName())
						.set("original", file.getName())
						.set("type", "." + file.getExts()) // 这里根据实际扩展名去写
						.set("size", file.getSize());
			}	
			renderJson(ret);
			return;
		}else if(action.startsWith("list")){//暂不支持
			renderJson(Ret.create("state", "FAILED"));
			return;	
		}else {
			renderJson(Ret.create("state", "FAILED"));
			return;			
		}		
	}	
}
