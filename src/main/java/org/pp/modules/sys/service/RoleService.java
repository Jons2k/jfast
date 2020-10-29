package org.pp.modules.sys.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pp.core.BaseService;
import org.pp.modules.sys.model.Menu;
import org.pp.modules.sys.model.Role;
import org.pp.modules.sys.vo.Tree;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class RoleService implements BaseService<Role>{
	
	public static Role dao = new Role().dao();
	private String error = "";
	
	/**
	 * 角色授权菜单
	 * @param roleId long 角色ID
	 * @param menus String[] 菜单数组
	 * @return
	 */
	public boolean grant(long roleId, String menus[]) {
		return Db.tx(() -> {
			Db.delete("DELETE FROM sys_role_menu WHERE role_id=?",roleId);
			
			if(menus == null || menus.length == 0) return true;
			
			for(String m:menus) {
				Record r = new Record();
				r.set("role_id", roleId);
				r.set("menu_id", Long.valueOf(m));
				if(!Db.save("sys_role_menu", r)) {
					return false;
				}
			}
			return true;
		});
	}
	
	/**
	 * 角色分配用户
	 * @param roleId long 角色ID
	 * @param users long[] 用户数组
	 * @return
	 */
	public boolean grantUser(long roleId, long users[]) {
		return Db.tx(() -> {
			Db.delete("DELETE FROM sys_user_role WHERE role_id=?",roleId);
			
			if(users == null || users.length == 0) return true;
			
			for(long uid:users) {
				Record r = new Record();
				r.set("role_id", roleId);
				r.set("user_id", uid);
				if(!Db.save("sys_user_role", r)) {
					return false;
				}
			}
			return true;
		});
	}
	
	
	/**
	 * 查询用户的角色
	 * @param userId long 用户ID
	 * @return
	 */
	public List<Role> getUserRoles(long userId){
		return dao.find("SELECT * FROM sys_role WHERE status=? and id in (SELECT role_id FROM sys_user_role WHERE user_id=?)","1", userId);
	}
	
	/**
	 * 查询角色的菜单
	 * @param roleId long 角色ID
	 * @return
	 */
	public List<Tree> menus(long roleId){
		List<Menu> list = MenuService.dao.find("SELECT * FROM sys_menu WHERE status=? ORDER BY pid ASC,list_sort ASC","1");
		List<Long> grants = Db.query("SELECT menu_id menuId FROM sys_role_menu WHERE role_id=?", roleId);
		return grantTree(list, new HashSet<Long>(grants), 0l);
	}
	
	private List<Tree> grantTree(List<Menu> list,Set<Long> grants,long pid){
		List<Tree> tree = new ArrayList<>();
		for(Menu m:list) {
			if(m.getPid().longValue() != pid) continue;
			
			Tree t = new  Tree();
			t.setId(m.getId()+"");
			t.setField(m.getType());
			t.setTitle(m.getTitle());
			if(!"0".equals(m.getType()) &&grants.contains(m.getId())) {
				t.setChecked(true);
			}
			
			List<Tree> s = grantTree(list, grants, m.getId());
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
	public Role getModel() {
		return dao;
	}
}
