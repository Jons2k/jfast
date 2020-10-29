package org.pp.modules.sys.service;

import java.util.Date;

import org.pp.core.BaseService;
import org.pp.modules.sys.model.User;
import org.pp.utils.SecurityUtil;

import com.jfinal.kit.Kv;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

public class UserService implements BaseService<User>{
	
	public static User dao = new User().dao();
	private String error = "";
	
	public User login(String account, String password) {
		User user = findFirst(Kv.by("account = ", account));
		if(user == null || !checkPassword(user, password)) {
			error = "账号不存在或密码错误";
			return null;
		}
		return user;
	}
	
	/**
	 * 验证密码
	 * @param u User 用户
	 * @param password String 密码
	 * @return boolean
	 */
	public boolean checkPassword(User u, String password) {
		password = SecurityUtil.password(password, u.getSalt());
		return u.getPassword().equalsIgnoreCase(password);
	}
	
	/**
	 * 重置密码
	 * @param ids
	 * @return
	 */
	public boolean repass(String[] ids) {
		String password = PropKit.get("security.password");
		return Db.tx(() -> {
			for(String id : ids) {
				User u = find(Long.valueOf(id));
				if(u != null) {
					u.setPassword(password);
				}
				SecurityUtil.password(u);
				u.update();
			}
			return true;
		});		
	}

	/**
	 * 用户授权（分配角色）
	 * @param userId String 用户ID
	 * @param roles long[] 角色ID数组
	 * @return boolean
	 */
	public boolean grant(long userId,long[] roles) {
		return Db.tx(() -> {
			Db.delete("DELETE FROM sys_user_role WHERE user_id=?", userId);
			
			if(roles == null) return true;
			for(long roleId : roles) {
				Record r = new Record();
				r.set("user_id", userId);
				r.set("role_id", roleId);
				if(!Db.save("sys_user_role", r)) {
					return false;
				}
			}
			CacheKit.remove("user_role", "user_"+userId);
			return true;
		});
	}
		
	@Override
	public boolean insert(User bean) {
		bean.setCreateTime(new Date());
		bean.setSalt(SecurityUtil.salt());
		if(StrKit.isBlank(bean.getPassword())) {
			bean.setPassword(PropKit.get("security.password"));
		}
		SecurityUtil.password(bean);
		return bean.save();
	}
	
	@Override
	public boolean delete(long id) {
		User u = dao.findById(id);
		if(u == null) {
			return false;
		}
		if("1".equals(u.getStatus())){
			error = "启用的账号无法删除!";
			return false;
		}
		return Db.tx(() -> {
			Db.delete("DELETE FROM sys_role_menu WHERE userId=?",id);
			dao.deleteById(id);
			return true;
		});
	}
		
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	@Override
	public User getModel() {
		return dao;
	}
}
