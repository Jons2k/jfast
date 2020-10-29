package org.pp.modules.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pp.core.BaseService;
import org.pp.modules.sys.model.Config;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.ehcache.CacheKit;

public class ConfigService implements BaseService<Config>{
	
	public static Config dao = new Config().dao();
	private String error = "";
	
	/**
	 * 查询系统配置
	 * @return Map<String,String>
	 */
	public static Map<String,String> map(){
		List<Config> list = dao.findByCache("sys_config", "all", "SELECT * FROM sys_config WHERE type != ?", "g");
		if(list == null || list.size() == 0) return null;
		
		Map<String,String> map = new HashMap<>();
		for(Config c : list) {
			if(c.getVals() == null) {
				map.put(c.getCode(), c.getVals());
			}else {
				map.put(c.getCode(), c.getDefaults());
			}
		}
		return map;
	}
	
	/**
	 * 生成配置分组
	 * @return List<Config>
	 */
	public List<Config> groups(){
		return all(Kv.by("type = ", "g"), "list_sort ASC");
	}	
	
	/**
	 * 批量保存
	 * @return boolean
	 */
	public boolean set(Map<String, String[]> sets) {
		List<Config> list = all(null, "list_sort ASC"); 
		return Db.tx(() -> {
			for(Config c : list) {
				if("g".equalsIgnoreCase(c.getType())) continue;
				
				String[] vs = sets.get(c.getCode());
				if(vs.length >0 && vs[0].equalsIgnoreCase(c.getVals())) continue;
				
				c.setVals(vs[0].trim());
				if(!c.update()) return false;
			}
			return true;
		});
	}
	
	@Override
	public boolean insert(Config bean) {
		Config d = findFirst(Kv.by("code = ", bean.getCode()));
		if(d != null) {
			error = "代码已经存在："+bean.getCode();
			return false;
		}
		if(bean.getPid() == null) bean.setPid(0);
		bean.setCode(bean.getCode().toLowerCase());
		CacheKit.remove("sys_config", "all");
		return bean.save();
	}
	
	
	@Override
	public boolean update(Config bean) {
		if(bean.getPid() == null) bean.setPid(0);

		CacheKit.remove("sys_config", "all");

		return bean.update();
	}
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	@Override
	public Config getModel() {
		return dao;
	}
}
