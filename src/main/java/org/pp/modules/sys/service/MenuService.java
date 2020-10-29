package org.pp.modules.sys.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.pp.core.BaseService;
import org.pp.modules.sys.model.Action;
import org.pp.modules.sys.model.Menu;
import org.pp.modules.sys.vo.Tree;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

public class MenuService implements BaseService<Menu>{
	
	public static Menu dao = new Menu().dao();
	private String error = "";
	
	/**
	 * 菜单关联行为
	 * @param menuId long 菜单ID
	 * @param actions List<String> 行为列表
	 * @return boolean
	 */
	public boolean addAction(long menuId, List<String> actions) {
		return Db.tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				Db.delete("DELETE FROM sys_menu_action WHERE menu_id=?", menuId);
				
				for(String id:actions) {
					Record data = new Record();
					data.set("menu_id", menuId);
					data.set("action_id", Long.valueOf(id));
					if(!Db.save("sys_menu_action", data)) {
						return false;
					}
				}
				return true;
			}
		});
	}
	
	/**
	 * 查询菜单关联的动作
	 * @param menuId long 菜单ID
	 * @param appId long 应用ID
	 * @return List<Menu>
	 */
	public static List<Action> getMenuAction(long menuId){
		String sql = "select a.* from sys_action a \r\n" + 
				"join sys_menu_action ma on ma.action_id = a.id\r\n" + 
				"where ma.menu_id=?";
		return ActionService.dao.find(sql, menuId);
	}
	
	/**
	 * 查询用户授权的应用下全部菜单
	 * @param userId long 用户ID
	 * @param appId long 应用ID
	 * @return List<Menu>
	 */
	public static List<Menu> getUserAppMenus(long userId, long appId){
		String sql = "select m.* from sys_menu m \r\n" + 
				"join sys_role_menu rm on rm.menu_id = m.id\r\n" + 
				"join sys_user_role ur on ur.role_id = rm.role_id\r\n" + 
				"where ur.user_id=? AND m.app_id=? ORDER BY list_sort ASC";
		return dao.find(sql, userId, appId);
	}
	
		
	/**
	 * 生成树形菜单
	 * @param pid long 上级ID
	 * @return List<Tree>
	 */
	public List<Tree> tree(long pid){
		List<Tree> tree = new ArrayList<>();
		List<Menu> list = all(Kv.by("pid = ", pid), "list_sort ASC");
		if(list== null || list.size() == 0) return null;
		
		for(Menu d:list) {
			Tree t = new  Tree();
			t.setId(d.getId()+"");
			t.setField(d.getType());
			t.setTitle(d.getTitle());
			
			List<Tree> s = tree(d.getId());
			if(s != null && s.size() >0) {
				t.setChildren(s);
			}
			t.setSpread(true);
			tree.add(t);
		}
		return tree;
	}
	
	/**
	 * 生成菜单树形分组
	 * @param pid long 上级ID
	 * @return List<Tree>
	 */
	public List<Tree> groups(Kv cond){
		List<Tree> tree = new ArrayList<>();
		List<Menu> list = all(cond, "list_sort ASC");
		if(list== null || list.size() == 0) return null;
		
		for(Menu d:list) {
			Tree t = new  Tree();
			t.setId(d.getId()+"");
			t.setField(d.getType());
			t.setTitle(d.getTitle());
			
			cond.put("pid = ",d.getId());
			List<Tree> s = groups(cond);
			if(s != null && s.size() >0) {
				t.setChildren(s);
			}
			t.setSpread(true);
			tree.add(t);
		}
		return tree;
	}	
		
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	@Override
	public Menu getModel() {
		return dao;
	}
}
