package org.pp.utils;

import java.io.File;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pp.modules.dev.model.Field;
import org.pp.modules.dev.model.Models;
import org.pp.modules.dev.model.Validation;
import org.pp.modules.dev.service.FieldService;
import org.pp.modules.dev.service.ValidationService;
import org.pp.modules.sys.service.FileService;
import org.pp.utils.db.Adapter;
import org.pp.utils.db.DbFactory;

import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
public class XDataUtil {
	private String error;
	
	/**
	 * 更新数据
	 * @param Models model 模型
	 * @param Record data 数据
	 * @return boolean
	 */
	public boolean update(Models model, Record data) {
		attr(model);
		return Db.tx(new IAtom() {			
			@Override
			public boolean run() throws SQLException {
				if(!validate(data, model)) {
					return false;
				}
				data.set("mflag", "1");
				data.set("sflag", "0");
				data.set("flag", "U");
				if(!Db.update(model.getTables(), data)) {
					setError("保存数据失败");
					return false;
				}
				calculate(data, model);		
				backup(model, data.getLong("id"), "U");		
				return true;
			}
		});
	}
	
	/**
	 * 保存新数据
	 * @param Models model 模型
	 * @param Record data 数据
	 * @return boolean
	 */
	public boolean save(Models model, Record data) {
		attr(model);
		defaults(data, model);
		return Db.tx(new IAtom() {			
			@Override
			public boolean run() throws SQLException {
				if(!validate(data, model)) {
					return false;
				}
				data.set("mflag", "1");
				data.set("sflag", "0");
				data.set("flag", "I");
				if(!Db.save(model.getTables(), data)) {
					setError("保存数据失败");
					return false;
				}
				calculate(data, model);		
				backup(model, data.getLong("id"), "I");		
				return true;
			}
		});
	}
	
	/**
	 * 保存新数据
	 * @param Models model 模型
	 * @param Record data 数据
	 * @return boolean
	 */
	public boolean delete(Models model, String ids) {		
		String[] ida = ids.split(",");
		return Db.tx(() -> {
			for(String id:ida) {
				Record data = new Record();
				data.set("mflag", "1");
				data.set("sflag", "0");
				data.set("flag", "D");
				data.set("id", Long.valueOf(id));
				if(!Db.update(model.getTables(),data)) {
					return false;
				}
				backup(model, data.getLong("id"), "D");
			}
			return true;
		});
	}
	
	/**
	 * 记录数据变更
	 * @param Models model
	 * @param long id
	 * @param String type I/U/D
	 * @return int
	 */
	public int backup(Models model, long id, String type) {
		String fields = "";
		for(Field f:model.getFields()) {
			fields += ","+f.getCode();
		}
		String sql = "INSERT INTO "+model.getTables()+"_history(hid"+fields+",log_user,log_time,log_type) ";
		sql += "SELECT id"+fields+","+SecurityUtil.userId()+","+PropKit.get("ddl.now", "now()")+",'"+type+"' ";
		sql	+= "FROM " + model.getTables()+ " t WHERE t.id="+id;
		return Db.update(sql);
	}
	
	/**
	 * 导入数据
	 * @param m Models model 模型定义
	 * @param excelFile String excel文件路径
	 * @param error String 错误处理方式：1=退出，2=忽略
	 * @param repeat String 重复数据处理方式：1=报错，2=忽略，3=更新
	 * @return boolean
	 */
	public boolean imports(Models model, String excelFile, String error, String repeat) {
		String savePath = FileService.basePath()+excelFile;
		File file = new File(savePath);
		if(!file.exists()) {
			setError("上传的文件不存在");
			return false;
		}
		List<String[]> list = ExcelUtil.read(file, 1);
		if(list == null || list.size() == 0) {
			setError("没有解析到有效数据");
			return false;
		}

		attr(model);

		List<Field> fields = model.getFields();
		for(int i=0;i<list.size();i++) {
			String[] line = list.get(i);
			Record data = new Record();
			
			for(int j=0;j<fields.size();j++) {
				if(StrKit.isBlank(line[j])) {
					continue;
				}
				Field f = fields.get(j);
				String code = f.getCode();				
				if("date".equalsIgnoreCase(f.getType())) {
					data.set(code, DateUtil.from(line[j]));
				}else if("datetime".equalsIgnoreCase(f.getType())) {
					data.set(code, DateUtil.from(line[j], DateUtil.DATETIME_FORMAT));
				}else {
					data.set(code, line[j]);
				}
			}		
			if(!save(model, data) && "1".equalsIgnoreCase(error)){
				setError("第"+(i+1)+"行数据发生错误:"+getError());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 导出数据
	 * @param m Models model 模型定义
	 * @param os OutputStream 输出流
	 * @param list List<Record> 数据列表
	 * @return boolean
	 */
	public boolean exports(Models model, OutputStream os, List<Record> list) {
		attr(model);
		List<Field> fields = model.getFields();
		String[] headers = new String[fields.size()];
		for(int j=0;j<fields.size();j++) {
			headers[j] = fields.get(j).getTitle();
		}
		
		List<String[]> lines = new ArrayList<>();
		for(Record row:list) {
			String[] line = new String[fields.size()];			
			for(int j=0;j<fields.size();j++) {
				Field f = fields.get(j);
				String code = f.getCode();	
				if(row.getObject(code) == null) {
					line[j] = "";
					continue;
				}
				if("date".equalsIgnoreCase(f.getType())) {
					line[j] = DateUtil.date(row.getDate(code));
				}else if("datetime".equalsIgnoreCase(f.getType())) {
					line[j] = DateUtil.datetime(row.getDate(code));
				}else {
					line[j] = row.get(code);
				}
			}
			lines.add(line);
		}
		try {
			ExcelUtil.write(os, model.getTitle(), headers, lines);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 生成导入模板
	 * @param m Models model 模型定义
	 * @param os OutputStream 输出流
	 * @return boolean
	 */
	public boolean excel(Models model, OutputStream os) {
		attr(model);
		List<Field> fields = model.getFields();
		String[] headers = new String[fields.size()];
		for(int j=0;j<fields.size();j++) {
			headers[j] = fields.get(j).getTitle();
		}
		
		try {
			ExcelUtil.write(os, model.getTitle(), headers, null);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 加载模型的字段和验证规则
	 * @param m Models m 模型定义
	 */
	public void attr(Models m) {
		List<Field> fields= m.getFields();
		if(fields == null || fields.size() == 0) {
			fields = FieldService.dao.find("SELECT * FROM dev_field WHERE model_id=? ORDER BY list_sort ASC", m.getId());
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
	 * @param Record data 数据
	 * @param model Models 模型定义
	 */
	public void defaults(Record data, Models model) {	
		List<Field> fields= model.getFields();	
		for(Field f:fields) {
			if(!StrKit.isBlank(data.getStr(f.getCode()))) continue;
			if(!StrKit.isBlank(f.getDefaults())){
				data.set(f.getCode(), f.calcDefault());
			}else if(f.getCalculate() != null){
				data.set(f.getCode(), 0);
			}
		}
		
	}
	
	/**
	 * 验证数据
	 * @param Record data 数据
	 * @param m Models 模型定义
	 * @return boolean
	 */
	public boolean validate(Record data, Models m) {		
		List<Field> fields= m.getFields();
		List<Validation> valids= m.getValids();	
		Map<String,String> map = new HashMap<>();
		if(data.get("id") != null) {
			map.put("id", data.getStr("id"));
		}
		
		//把数据转换成Map,方便验证时使用
		for(Field f:fields) {
			if(null == data.get(f.getCode())) { 
				continue;
			}
			String val = "";
			if(Field.TYPE_DATE.equalsIgnoreCase(f.getType())) {
				val = DateUtil.date(data.getDate(f.getCode()));
			}else if(Field.TYPE_DATETIME.equalsIgnoreCase(f.getType())) {
				val = DateUtil.datetime(data.getDate(f.getCode()));
			}else {
				val = data.getStr(f.getCode());
			}		
			map.put(f.getCode(), val);
		}

		//逐个字段验证
		for(Field f:fields) {
			//先验证字段空值
			if(null == data.get(f.getCode())) {
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
	 * @param Record data 数据
	 * @param Models m  模型定义
	 */
	public void calculate(Record data, Models m) {
		List<Field> fields= m.getFields();
		
		for(Field f:fields) {
			if(!StrKit.isBlank(f.getCalculate())){
				Db.update("UPDATE " + m.getTables() + " SET " + f.getCode() +" = " + f.getCalculate() + " WHERE id = ", data.getLong("id"));
			}
		}
	}
	
	/**
	 * 模型可能用到的SQL
	 * @param Models model 模型
	 * @return Map<String,String>
	 */
	public Map<String,String> sql(Models model){
		attr(model);
		String sql = "";
		Map<String,String> map = new HashMap<>();
		for(String type : DbFactory.TYPES) {	
			sql = "";
			Adapter d = DbFactory.create(type);
			d.setModel(model);	
			List<String> sqls = d.create();
			for(String s:sqls) {
				sql += s +"\r\n";
			}
			sql += "//mflag tinyint(4) DEFAULT '0',\r\n"
					+ "//sflag tinyint(4) DEFAULT '0',\r\n"
					+ "//flag char(1) DEFAULT NULL,";
			map.put(type, sql);
		}
				
		sql = map.get("mysql");
		String str = "";
		sql = sql.replace("PRIMARY KEY (id)", str);
		
		map.put("history", sql);

		sql  = "//读数据-------------开始---------------\r\n"
				+ "SELECT ";
		for(Field f:model.getFields()) {
			str += ","+f.getCode();
		}
		sql += str.substring(1);
		sql += " FROM "+model.getTables() +" WHERE sflag > mflag;\r\n";
		sql += "//读数据-------------结束---------------\r\n \r\n";
		sql += "//写数据-------------开始---------------\r\n";
		sql += "//用主键判断是否存在对应数据\r\n"
				+ "set ct = SELECT count(id) FROM "+model.getTables() +" WHERE code = #(code) ;\r\n"
				+ "//无数据则插入\r\n" + 
				"if ct = 0 OR #flag = ’I’ then:\r\n" + 
				"insert into "+model.getTables()+"(";
		for(Field f:model.getFields()) {
			str += ","+f.getCode();
		}
		sql += str.substring(1);
		sql += ") values ( ";
		str = "";
		for(Field f:model.getFields()) {
			str += ",#("+f.getCode()+") ";
		}
		sql += str.substring(1);
		sql += " ) ;\r\n" + 
				"elseif #flag = ’U’ then:\r\n" + 
				"update "+model.getTables()+" set ";
		str = "";
		for(Field f:model.getFields()) {
			str += ","+f.getCode() +" = #("+f.getCode()+") ";
		}
		sql += str.substring(1);
		sql +=" where code = #(code) ;\r\n" + 
				"elseif #flag = ’D’ then:\r\n" + 
				"delete "+model.getTables()+" where code = #(code) ;\r\n" + 
				"end\r\n";
		sql += "//写数据-------------结束---------------\r\n";
		map.put("pipe", sql);
		return map;
	}
	
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}	
}
