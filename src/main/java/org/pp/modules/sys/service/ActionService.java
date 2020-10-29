package org.pp.modules.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pp.core.BaseService;
import org.pp.modules.dev.model.Models;
import org.pp.modules.dev.service.ModelsService;
import org.pp.modules.sys.model.Action;

import com.jfinal.kit.Kv;

public class ActionService implements BaseService<Action>{
	
	public static Action dao = new Action().dao();
	private static Map<String,Action> maps;
	private String error = "";
	
	/**
	 * 扫描全部模型，生成默认的操作行为
	 */
	public void scan() {
		List<Models> list = ModelsService.dao.findAll();
		for(Models m:list) {
			models(m);
		}
	}
	
	
	/**
	 * 生成模型下的默认行为
	 * @param m Models 业务模型
	 */
	public void models(Models m) {
		String url = "/"+m.getModule().getCode().toLowerCase()+"/"+m.getCode().toLowerCase()+"/";
		Action data = findFirst(Kv.by("url = " , url+"lists"));
		if(data == null) {
			data = new Action();
			data.setModelId(m.getId().intValue());
			data.setModuleId(m.getModuleId().intValue());
			data.setTitle("查分页");
			data.setUrl(url + "lists");
			data.save();
		}
		
		data = findFirst(Kv.by("url = " , url+"slist"));
		if(data == null) {
			data = new Action();
			data.setModelId(m.getId().intValue());
			data.setModuleId(m.getModuleId().intValue());
			data.setTitle("查全部");
			data.setUrl(url + "slist");
			data.save();
		}
		
		data = findFirst(Kv.by("url = " , url+"save"));
		if(data == null) {
			data = new Action();
			data.setModelId(m.getId().intValue());
			data.setModuleId(m.getModuleId().intValue());
			data.setTitle("新增");
			data.setUrl(url + "save");
			data.save();
		}
		
		data = findFirst(Kv.by("url = " , url+"update"));
		if(data == null) {
			data = new Action();
			data.setModelId(m.getId().intValue());
			data.setModuleId(m.getModuleId().intValue());
			data.setTitle("修改");
			data.setUrl(url + "update");
			data.save();
		}
		
		data = findFirst(Kv.by("url = " , url+"delete"));
		if(data == null) {
			data = new Action();
			data.setModelId(m.getId().intValue());
			data.setModuleId(m.getModuleId().intValue());
			data.setTitle("删除");
			data.setUrl(url + "delete");
			data.save();
		}

		data = findFirst(Kv.by("url = " , url+"info"));
		if(data == null) {
			data = new Action();
			data.setModelId(m.getId().intValue());
			data.setModuleId(m.getModuleId().intValue());
			data.setTitle("查看");
			data.setUrl(url + "info");
			data.save();
		}

		data = findFirst(Kv.by("url = " , url+"imports"));
		if(data == null) {
			data = new Action();
			data.setModelId(m.getId().intValue());
			data.setModuleId(m.getModuleId().intValue());
			data.setTitle("导入");
			data.setUrl(url + "imports");
			data.save();
		}

		data = findFirst(Kv.by("url = " , url+"exports"));
		if(data == null) {
			data = new Action();
			data.setModelId(m.getId().intValue());
			data.setModuleId(m.getModuleId().intValue());
			data.setTitle("导出");
			data.setUrl(url + "exports");
			data.save();
		}
	}
	
	/**
	 * 将全部action映射成map，主键为ID或URL
	 * @return
	 */
	public static Map<String,Action> map(){
		if(maps == null || maps.size() == 0) {
			List<Action> list = dao.findAll();
			maps = new HashMap<>();
			for(Action a : list) {
				maps.put(a.getUrl(), a);
				maps.put(a.getId()+"", a);
			}
		}		
		return maps;
	}
	
	@Override
	public String getError() {
		return error;
	}

	@Override
	public void setError(String error) {
		this.error = error;
	}
	@Override
	public Action getModel() {
		return dao;
	}
}
