package org.pp.modules.sys.service;

import java.util.Date;

import org.pp.core.BaseService;
import org.pp.modules.sys.model.User;
import org.pp.modules.sys.model.UserLogin;

import com.jfinal.plugin.activerecord.Db;

public class UserLoginService implements BaseService<UserLogin>{	
	public static UserLogin dao = new UserLogin().dao();
	private String error = "";

	/**
	 * 记录未成功的登录信息
	 * @param account String 账号
	 * @param ip String IP
	 */
	public void record(String account, String ip) {
		UserLogin data = new UserLogin();
		data.setAccount(account);
		data.setIp(ip);
		data.setLoginTime(new Date());
		data.save();
	}
	
	/**
	 * 记录成功的登录 信息
	 * @param user User 登录成功的用户
	 * @param ip String IP
	 * @param session String SessionID
	 */
	public void record(User user, String ip, String session) {
		UserLogin data = new UserLogin();
		data.setAccount(user.getAccount());
		data.setUserId(user.getId());
		data.setSessionId(session);
		data.setIp(ip);
		data.setLoginTime(new Date());
		data.save();
	}
	
	/**
	 * 记录成功的登录 信息
	 * @param user User 登录成功的用户
	 * @param ip String IP
	 * @param session String SessionID
	 */
	public void record(User user, String ip) {
		UserLogin data = new UserLogin();
		data.setAccount(user.getAccount());
		data.setUserId(user.getId());
		data.setIp(ip);
		data.setLoginTime(new Date());
		data.save();
	}
	
	public void logout(User user, String session) {
		Db.update("UPDATE sys_user_login set logout_time=? WHERE user_id=? and session_id=?", new Date(), user.getId(), session);
	}
	
	public void logout(String ip, String session) {
		Db.update("UPDATE sys_user_login set logout_time=? WHERE ip=? and session_id=?", new Date(), ip, session);
	}
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	@Override
	public UserLogin getModel() {
		return dao;
	}
}
