package org.pp.modules.sys.service;

import java.util.Date;

import org.pp.core.BaseService;
import org.pp.modules.sys.model.Token;
import org.pp.modules.sys.model.User;

import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;

public class TokenService implements BaseService<Token>{
	public static int EXPIRE = 60 * 60 * 24; //令牌有效期为24小时
	public static Token dao = new Token().dao();
	
	private String error = "";

	/**
	 * 给用户生成令牌
	 * @param u User 用户
	 * @param ip String IP地址
	 * @return Token
	 */
	public Token grant(User u, String ip) {
		Token t = new Token();
		t.setCode(StrKit.getRandomUUID().substring(0,32));
		t.setClientId(0); //默认为0，可扩展为支持多个第三方应用
		t.setCreateTime(new Date());
		Date d = new Date();
		d.setTime(d.getTime() + EXPIRE * 1000);
		t.setExpireTime(d);
		t.setIp(ip);
		t.setUserId(u.getId());
		
		if(t.save()) return t;
		return null;
	}
	
	/**
	 * 获取令牌对应的用户信息
	 * @param code  String 令牌代码
	 * @return User
	 */
	public User getTokenUser(String code) {
		Token t = CacheKit.get("sys_token", code);
		if( t == null) {
			t = findFirst(Kv.by("code = ", code));
			if(t != null) {
				t.getUser().getRoles();
				CacheKit.put("sys_token", code, t);
			}			
		}
		
		if( t == null) {
			return null;
		}
		
		if(t.getExpireTime().getTime() < System.currentTimeMillis()) {
			CacheKit.remove("sys_token", code);
			return null;
		}
		return t.getUser();
	}
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	@Override
	public Token getModel() {
		return dao;
	}
}
