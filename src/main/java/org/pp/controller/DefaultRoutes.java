package org.pp.controller;

import com.jfinal.config.Routes;

public class DefaultRoutes extends Routes{

	@Override
	public void config() {
		setBaseViewPath("/view/");
		// 公共页面和接口
		add("/public", PublicController.class);

		// 配合HtmlHandler进行模板渲染
		add("/html", HtmlController.class);
		// 后台首页
		add("/admin", IndexController.class);
		// 文件上传
		add("/upload", UploadController.class);		

	}

}
