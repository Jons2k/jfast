package org.pp.utils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pp.modules.dev.model.Field;
import org.pp.modules.dev.model.Models;
import org.pp.modules.dev.model.Validation;
import org.pp.modules.dev.service.FieldService;
import org.pp.modules.dev.service.ModelsService;
import org.pp.modules.dev.service.ValidationService;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * 模型数据处理工具，主要是加强保存和更新功能，支持自动填充默认值，数据验证和公式值计算
 * @author Administrator
 *
 */
public class ModelUtil {
	private String error;
	private static ThreadLocal<ModelUtil> locals = new ThreadLocal<>();
	
	/**
	 * 更新数据
	 * @param bean extend Model 模型数据
	 * @return boolean
	 */
	public boolean update(Model bean) {
		String table = TableMapping.me().getTable(bean.getClass()).getName();
		Models m = ModelsService.getTableModel(table);
		attr(m);
		defaults(bean, m);
		return Db.tx(new IAtom() {			
			@Override
			public boolean run() throws SQLException {
				if(!validate(bean, m)) {
					return false;
				}
				if(!bean.update()) {
					setError("保存数据失败");
					return false;
				}
				calculate(bean, m);				
				return true;
			}
		});
	}
	
	/**
	 * 保存新数据
	 * @param bean extend Model 模型数据
	 * @return boolean
	 */
	public boolean save(Model bean) {
		String table = TableMapping.me().getTable(bean.getClass()).getName();
		Models m = ModelsService.getTableModel(table);
		attr(m);
		defaults(bean, m);
		return Db.tx(new IAtom() {			
			@Override
			public boolean run() throws SQLException {
				if(!validate(bean, m)) {
					return false;
				}
				if(!bean.save()) {
					setError("保存数据失败");
					return false;
				}
				calculate(bean, m);				
				return true;
			}
		});
	}
	
	/**
	 * 加载模型的字段和验证规则
	 * @param m Models 模型定义
	 */
	public void attr(Models m) {
		List<Field> fields= m.getFields();
		if(fields == null || fields.size() == 0) {
			fields = FieldService.dao.find("SELECT * FROM dev_field WHERE model_id=?", m.getId());
			m.setFields(fields);
		}
		
		List<Validation> valids= m.getValids();
		if(valids == null || valids.size() == 0) {
			valids = ValidationService.dao.find("SELECT * FROM dev_validation WHERE model_id=? ORDER BY field_id ASC", m.getId());
			m.setValids(valids);
		}
	}
	
	/**
	 * 自动填充默认值,如果有计算公式则填充为0
	 * @param bean extend Model 模型数据
	 * @param m Models 模型定义
	 */
	public void defaults(Model bean, Models m) {	
		List<Field> fields= m.getFields();	
		for(Field f:fields) {
			if(!StrKit.isBlank(bean.getStr(f.getCode()))) continue;
			if(!StrKit.isBlank(f.getDefaults())){
				bean.set(f.getCode(), f.calcDefault());
			}else if(Field.TYPE_NUMBER.equals(f.getType()) && !StrKit.isBlank(f.getCalculate())){
				bean.set(f.getCode(), 0);
			}
		}		
	}
	
	/**
	 * 验证数据
	 * @param bean extend Model 模型数据
	 * @param m Models 模型定义
	 * @return boolean
	 */
	public boolean validate(Model bean, Models m) {		
		List<Field> fields= m.getFields();
		List<Validation> valids= m.getValids();		

		Map<String,String> map = new HashMap<>();
		if(bean.get("id") != null) {
			map.put("id", bean.getStr("id"));
		}
		
		//把数据转换成Map,方便验证时使用
		for(Field f:fields) {
			if(null == bean.get(f.getCode())) { 
				continue;
			}
			String val = "";
			if(Field.TYPE_DATE.equalsIgnoreCase(f.getType())) {
				val = DateUtil.date(bean.getDate(f.getCode()));
			}else if(Field.TYPE_DATETIME.equalsIgnoreCase(f.getType())) {
				val = DateUtil.datetime(bean.getDate(f.getCode()));
			}else {
				val = bean.getStr(f.getCode());
			}		
			map.put(f.getCode(), val);
		}
		
		//逐个字段验证
		for(Field f:fields) {
			//先验证字段空值
			if(null == bean.get(f.getCode())) {
				if(f.isNullable()) continue;
				else {
					setError(f.getTitle()+"不能为空");
					return false;
				}
			}	

			//再验证规则
			for(Validation v : valids) {
				if(v.getFieldId().equals(f.getId())) {
					v.setField(f);
					v.setModel(m);
					if(!v.valid(map)) {
						setError(v.getTitle());
						return false;
					}
				}
			}
		}
		return true;
	}
	/**
	 * 数据保存后，进行公式计算	
	 * @param bean extend Model 模型数据
	 * @param m Models 模型定义
	 */
	public void calculate(Model bean, Models m) {
		List<Field> fields= m.getFields();
		
		for(Field f:fields) {
			if(!StrKit.isBlank(f.getCalculate())){
				Db.update("UPDATE " + m.getTables() + " SET " + f.getCode() +" = " + f.getCalculate() + " WHERE id = ", bean.getLong("id"));
			}
		}
	}
	
	/**
	 * 获取实例
	 * @return ModelUtil
	 */
	public static ModelUtil get() {
		ModelUtil inst = locals.get();
		if(inst == null) {
			inst = new ModelUtil();
			locals.set(inst);
		}
		return inst;
	}
	
	/**
	 * 清除当前实例
	 */
	public static void release() {
		locals.remove();
	}

	private ModelUtil() {}
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}	
}
