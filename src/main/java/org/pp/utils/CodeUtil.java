package org.pp.utils;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.pp.modules.dev.model.Chart;
import org.pp.modules.dev.model.Field;
import org.pp.modules.dev.model.Models;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PathKit;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;

/**
 * 代码生成器
 * @author Administrator
 *
 */
public class CodeUtil {
	protected static  Engine engine = null;
	public static String BASE_PACKAGE="org.pp.modules";
	
	/**
	 * 生成模板代码，主要是模板，java代码由代码生成器完成
	 * @param model
	 * @param contents
	 * @param fields
	 * @return
	 */
	public static boolean model(Models model, String[] contents) {
		CodeUtil code = new CodeUtil();
		code.init();
		String module = model.getModule().getCode().toLowerCase();		
		String baseView = PathKit.getWebRootPath()+"/view/";
		String viewPath = baseView +module+"/"+model.getCode().toLowerCase();
		if(!FileUtil.mkdirs(viewPath)) {
			return false;
		}
		String savePath = "";
		Kv data = Kv.by("model", model);
		data.put("fields", model.getFields());
		for(String content:contents) {
			if("list".equalsIgnoreCase(content)) {
				savePath = viewPath+"/list.html";
				if(!FileUtil.exists(savePath)) {
					code.render("list.html", savePath, data);
				}
				savePath = viewPath+"/filter.html";
				if(!FileUtil.exists(savePath)) {
					code.render("filter.html", savePath, data);
				}
			}else if("form".equalsIgnoreCase(content)) {
				savePath = viewPath+"/form.html";
				if(!FileUtil.exists(savePath)) {
					code.render("form.html", savePath, data);
				}
				FileUtil.copy(baseView+"dev/build/add.html", viewPath+"/add.html");
				FileUtil.copy(baseView+"dev/build/edit.html", viewPath+"/edit.html");

			}else if("excel".equalsIgnoreCase(content)) {
				excel(model);
			}
		}
		return true;
	}
	
	/**
	 * 生成模块目录和初始代码
	 * @param  module String 模块代码
	 * @return boolean
	 */
	public static boolean module(String module) {
		String basePath = PathKit.getWebRootPath();
		if(!FileUtil.mkdirs(basePath + "/view/"+module)) {
			return false;
		}
		
		String javaPath = basePath+"/../src/"+BASE_PACKAGE.replace('.', '/')+ "/"+module;
		if(!FileUtil.mkdirs(javaPath + "/controller")) {
			return false;
		}
		if(!FileUtil.mkdirs(javaPath + "/base")) {
			return false;
		}
		if(!FileUtil.mkdirs(javaPath + "/model")) {
			return false;
		}
		new JFinalUtil().genModule(module);	
		
		return true;
	}
	
	/**
	 * 生成报表页面
	 * @param chart Chart
	 * @return boolean
	 */
	public static boolean chart(Chart chart) {
		CodeUtil code = new CodeUtil();
		code.init();			
		String savePath = PathKit.getWebRootPath()+"/view/charts/"+chart.getCode().toLowerCase()+".html";
		if(!FileUtil.exists(savePath)) {
			code.render("chart.html", savePath, Kv.by("vo", chart));
		}
		return FileUtil.exists(savePath);
	}
	
	/**
	 * 生成Excel模板
	 * @param Models model模型
	 * @return boolean
	 */
	public static boolean excel(Models model) {	
		List<Field> fields = model.getFields();
		String[] headers = new String[fields.size()];
		for(int j=0;j<fields.size();j++) {
			headers[j] = fields.get(j).getTitle();
		}
		
		try {
			String savePath = PathKit.getWebRootPath()+"/statics/excel/";
			savePath += model.getModule().getCode().toLowerCase()+"/";
			FileUtil.mkdirs(savePath);
			savePath += model.getCode().toLowerCase()+".xlsx";
			ExcelUtil.write(new FileOutputStream(savePath), model.getTitle(), headers, null);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void init() {
		if(engine != null) return;
		
		engine = Engine.create("code");
		engine.setDevMode(true);
		engine.setBaseTemplatePath(PathKit.getWebRootPath());
		engine.addSharedMethod(new com.jfinal.kit.StrKit());
		engine.addSharedObject("SHARP", "#");
	}
	
	
	public boolean render(String tpl,String savePath, Map<String,String> data) {		
		Template template = engine.getTemplate("/view/dev/build/"+tpl);
		return FileUtil.write(savePath, template.renderToString(data));
	}
}
