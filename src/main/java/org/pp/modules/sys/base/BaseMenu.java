package org.pp.modules.sys.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseMenu<M extends BaseMenu<M>> extends Model<M> implements IBean {

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
	 * 所属应用
	 */
	public void setAppId(java.lang.Integer appId) {
		set("app_id", appId);
	}
	
	/**
	 * 所属应用
	 */
	public java.lang.Integer getAppId() {
		return getInt("app_id");
	}

	/**
	 * 上级菜单
	 */
	public void setPid(java.lang.Integer pid) {
		set("pid", pid);
	}
	
	/**
	 * 上级菜单
	 */
	public java.lang.Integer getPid() {
		return getInt("pid");
	}

	/**
	 * 菜单类型
	 */
	public void setType(java.lang.String type) {
		set("type", type);
	}
	
	/**
	 * 菜单类型
	 */
	public java.lang.String getType() {
		return getStr("type");
	}

	/**
	 * 操作地址
	 */
	public void setUrl(java.lang.String url) {
		set("url", url);
	}
	
	/**
	 * 操作地址
	 */
	public java.lang.String getUrl() {
		return getStr("url");
	}

	/**
	 * 菜单名称
	 */
	public void setTitle(java.lang.String title) {
		set("title", title);
	}
	
	/**
	 * 菜单名称
	 */
	public java.lang.String getTitle() {
		return getStr("title");
	}

	/**
	 * 排序
	 */
	public void setListSort(java.lang.Integer listSort) {
		set("list_sort", listSort);
	}
	
	/**
	 * 排序
	 */
	public java.lang.Integer getListSort() {
		return getInt("list_sort");
	}

	/**
	 * 图标
	 */
	public void setIcon(java.lang.String icon) {
		set("icon", icon);
	}
	
	/**
	 * 图标
	 */
	public java.lang.String getIcon() {
		return getStr("icon");
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
