package org.pp.modules.dev.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseValidation<M extends BaseValidation<M>> extends Model<M> implements IBean {

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
	 * 规则名称
	 */
	public void setTitle(java.lang.String title) {
		set("title", title);
	}
	
	/**
	 * 规则名称
	 */
	public java.lang.String getTitle() {
		return getStr("title");
	}

	/**
	 * 所属模型
	 */
	public void setModelId(java.lang.Long modelId) {
		set("model_id", modelId);
	}
	
	/**
	 * 所属模型
	 */
	public java.lang.Long getModelId() {
		return getLong("model_id");
	}

	/**
	 * 字段
	 */
	public void setFieldId(java.lang.Long fieldId) {
		set("field_id", fieldId);
	}
	
	/**
	 * 字段
	 */
	public java.lang.Long getFieldId() {
		return getLong("field_id");
	}

	/**
	 * 验证类型
	 */
	public void setType(java.lang.String type) {
		set("type", type);
	}
	
	/**
	 * 验证类型
	 */
	public java.lang.String getType() {
		return getStr("type");
	}

	/**
	 * 验证规则
	 */
	public void setRule(java.lang.String rule) {
		set("rule", rule);
	}
	
	/**
	 * 验证规则
	 */
	public java.lang.String getRule() {
		return getStr("rule");
	}

}