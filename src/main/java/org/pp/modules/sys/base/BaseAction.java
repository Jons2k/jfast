package org.pp.modules.sys.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAction<M extends BaseAction<M>> extends Model<M> implements IBean {

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
	 * 名称
	 */
	public void setTitle(java.lang.String title) {
		set("title", title);
	}
	
	/**
	 * 名称
	 */
	public java.lang.String getTitle() {
		return getStr("title");
	}

	/**
	 * 所属模块
	 */
	public void setModuleId(java.lang.Integer moduleId) {
		set("module_id", moduleId);
	}
	
	/**
	 * 所属模块
	 */
	public java.lang.Integer getModuleId() {
		return getInt("module_id");
	}

	/**
	 * 所属模型
	 */
	public void setModelId(java.lang.Integer modelId) {
		set("model_id", modelId);
	}
	
	/**
	 * 所属模型
	 */
	public java.lang.Integer getModelId() {
		return getInt("model_id");
	}

}
