package org.pp.utils.db;

import java.util.List;

import org.pp.modules.dev.model.Field;
import org.pp.modules.dev.model.Models;

import com.jfinal.plugin.activerecord.Db;

public abstract class Adapter {

	
	protected Models model;
	
	
	
	/**
	 * 删除原表
	 * @return
	 */
	public abstract void drop(Models m);
	
	/**
	 * 创建新表
	 * @return
	 */
	public boolean create(Models m) {
		this.model = m;
		before_create();
		List<String> sqls = create();
		for(String sql:sqls)Db.update(sql);
		after_create();
		return true;
	}

	public abstract List<String> create() ;
	public void before_create() {}
	public void after_create() {}

	/**
	 * 清除默认数据中的小数点
	 * @param Field f 字段
	 */
	public void trimNumber(Field f) {
		String df = f.getDefaults();
		if(df != null && df.endsWith(".0")) f.setDefaults(df.substring(0, df.indexOf('.')));
	}
	
	public void setModel(Models model) {
		this.model = model;
	}
	
	/**
	 * 字段清洗
	 * @param List<Field> list 字段列表
	 */
	public void filters(List<Field> list) {	}
	

	public abstract List<Field> exportFields(String table);
	public abstract Models exportModel(String table);

	public abstract List<Models> exportTables();
}
