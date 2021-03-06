package org.pp.modules.sys.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {

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
	 * 账号
	 */
	public void setAccount(java.lang.String account) {
		set("account", account);
	}
	
	/**
	 * 账号
	 */
	public java.lang.String getAccount() {
		return getStr("account");
	}

	/**
	 * 密码
	 */
	public void setPassword(java.lang.String password) {
		set("password", password);
	}
	
	/**
	 * 密码
	 */
	public java.lang.String getPassword() {
		return getStr("password");
	}

	/**
	 * 邮箱
	 */
	public void setEmail(java.lang.String email) {
		set("email", email);
	}
	
	/**
	 * 邮箱
	 */
	public java.lang.String getEmail() {
		return getStr("email");
	}

	/**
	 * 手机号
	 */
	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}
	
	/**
	 * 手机号
	 */
	public java.lang.String getPhone() {
		return getStr("phone");
	}

	/**
	 * 姓名
	 */
	public void setNickname(java.lang.String nickname) {
		set("nickname", nickname);
	}
	
	/**
	 * 姓名
	 */
	public java.lang.String getNickname() {
		return getStr("nickname");
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
	 * 所在部门
	 */
	public void setDepartId(java.lang.Integer departId) {
		set("depart_id", departId);
	}
	
	/**
	 * 所在部门
	 */
	public java.lang.Integer getDepartId() {
		return getInt("depart_id");
	}

	/**
	 * 盐值
	 */
	public void setSalt(java.lang.String salt) {
		set("salt", salt);
	}
	
	/**
	 * 盐值
	 */
	public java.lang.String getSalt() {
		return getStr("salt");
	}

	/**
	 * 状态
	 */
	public void setStatus(java.lang.String status) {
		set("status", status);
	}
	
	/**
	 * 状态
	 */
	public java.lang.String getStatus() {
		return getStr("status");
	}

}
