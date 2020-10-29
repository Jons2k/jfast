package org.pp.modules.sys.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseToken<M extends BaseToken<M>> extends Model<M> implements IBean {

	/**
	 * 自增长主键ID
	 */
	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	/**
	 * 自增长主键ID
	 */
	public java.lang.Integer getId() {
		return getInt("id");
	}

	/**
	 * 代码
	 */
	public void setCode(java.lang.String code) {
		set("code", code);
	}
	
	/**
	 * 代码
	 */
	public java.lang.String getCode() {
		return getStr("code");
	}

	/**
	 * 用户
	 */
	public void setUserId(java.lang.Integer userId) {
		set("user_id", userId);
	}
	
	/**
	 * 用户
	 */
	public java.lang.Integer getUserId() {
		return getInt("user_id");
	}

	/**
	 * 应用
	 */
	public void setClientId(java.lang.Integer clientId) {
		set("client_id", clientId);
	}
	
	/**
	 * 应用
	 */
	public java.lang.Integer getClientId() {
		return getInt("client_id");
	}

	/**
	 * IP地址
	 */
	public void setIp(java.lang.String ip) {
		set("ip", ip);
	}
	
	/**
	 * IP地址
	 */
	public java.lang.String getIp() {
		return getStr("ip");
	}

	/**
	 * 创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}
	
	/**
	 * 创建时间
	 */
	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	/**
	 * 过期时间
	 */
	public void setExpireTime(java.util.Date expireTime) {
		set("expire_time", expireTime);
	}
	
	/**
	 * 过期时间
	 */
	public java.util.Date getExpireTime() {
		return get("expire_time");
	}

}