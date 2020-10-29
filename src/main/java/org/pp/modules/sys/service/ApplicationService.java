package org.pp.modules.sys.service;

import java.util.List;

import org.pp.core.BaseService;
import org.pp.modules.sys.model.Application;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;

public class ApplicationService implements BaseService<Application>{
	
	public static Application dao = new Application().dao();
	private String error = "";
	
	/**
	 * 查询用户授权的全部应用
	 * @param userId long 用户ID
	 * @return List<Application>
	 */
	public static List<Application> getUserApps(long userId){
		String sql = "select a.*\r\n" + 
				"from sys_application a\r\n" + 
				"join sys_menu m on a.id=m.app_id\r\n" + 
				"join sys_role_menu rm on rm.menu_id = m.id\r\n" + 
				"join sys_user_role ur on ur.role_id = rm.role_id\r\n" + 
				"where ur.user_id=? ORDER BY list_sort ASC";
		return dao.find(sql ,userId);
	}
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	@Override
	public Application getModel() {
		return dao;
	}
}
