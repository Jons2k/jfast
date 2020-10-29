package org.pp.utils.db;

import java.util.ArrayList;
import java.util.List;

import org.pp.modules.dev.model.Field;
import org.pp.modules.dev.model.Models;
import org.pp.modules.dev.model.Module;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


public class MySQL extends Adapter{

	@Override
	public void drop(Models m) {
		String sql = "DROP TABLE IF EXISTS `"+m.getTables()+"`";
		Db.update(sql);
	}

	@Override
	public List<String> create() {
		List<String> sqls = new ArrayList<>();
		List<Field> list = model.getFields();
		this.filters(list);
		String sql = "CREATE TABLE `"+model.getTables()+"` (\r\n"; 
		sql += "  id int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID'";
		for (Field f : list) {
			sql += " ,\r\n" + buildField(f);
		}
		sql += ",\r\n  PRIMARY KEY (id)" ;
		sql += "\r\n) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='"+model.getTitle()+"' AUTO_INCREMENT=1 ;";
		sqls.add(sql);
		return sqls;
	}
	
	@Override
	public void filters(List<Field> list) {
		int id = -1;
		for (int i = 0; i < list.size(); i++) {
			Field f = list.get(i);
			if("id".equalsIgnoreCase(f.getCode())) {
				id = i;
				continue;
			}
			f.setCode(f.getCode());
			switch (f.getType().toLowerCase()) {
			case "varchar":
			case "char":
				if (f.getLen() == 0)
					f.setLen(1);
				break;
			case "number":	
			case "float":	
			case "double":	
			case "int":			
			case "decimal":				
				if (f.getScale() == 0) {
					trimNumber(f);
				}
				break;
			default:
				break;
			}
		}
		if(id != -1) list.remove(id);
	}
	
	private String buildField(Field f) {
		String sql = f.getCode();
		boolean isn = false;
		switch (f.getDbType().toLowerCase()) {
		case "number":		
			if (f.getScale() == 0) {
				if(f.getLen() > 12) {
					sql +=  " bigint (" + f.getLen() + ")";
				}else {
					sql +=  " int (" + f.getLen() + ")";
				}				
			}else {
				sql += " float (" + f.getLen() + "," + f.getScale() + ")";				
			}
			isn = true;
			break;
		case "date":
		case "datetime":
			sql += " " +f.getDbType();
			break;
		default:
			sql += " " +f.getDbType() + "("+f.getLen()+")";
			break;
		}
		if ("0".equals(f.getNullable())) {
			sql += " NOT NULL";
		}else {
			String d = f.getDefaults();
			if(d != null && !"".equals(d)) {
				if(d.endsWith(".0")) d = d.substring(0,d.length()-2);
				sql += " DEFAULT " + (isn ? d : "'"+d+"'");
			}	
		}
			
		return sql + " COMMENT '"+f.getTitle()+"'";
	}

	@Override
	public List<Models> exportTables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Field> exportFields(String table) {
		String sql = "select * from information_schema.COLUMNS where TABLE_SCHEMA = (select database()) and TABLE_NAME='"+table+"'";
		List<Record> rs = Db.find(sql);
		List<Field> list = new ArrayList<Field>();
		for(Record r:rs) {
			Field f = parseField(r);
			if(f != null) list.add(f);
		}
		return list;
	}
	
	private Field parseField(Record r) {
		String fieldName = r.get("COLUMN_NAME").toString().toLowerCase();
		
		if("id".equalsIgnoreCase(fieldName)) return null;

		Field field = new Field();
		field.setCode(fieldName);
		field.setType(r.get("DATA_TYPE").toString());
		if(r.get("COLUMN_COMMENT") != null) field.setTitle(r.get("COLUMN_COMMENT").toString());
		else {
			field.setTitle(field.getCode());
		}
		field.setNullable("0");
		List<String> valids = new ArrayList<String>();
		
		if("Y".equalsIgnoreCase(r.get("NULLABLE").toString())) field.setNullable("1");
		else {
			valids.add("required");
		}
		
		if(fieldName.equals("email")) {
			valids.add("email");
			field.setType("email");
		}
		if("status".equals(fieldName) ||fieldName.endsWith("_status") || fieldName.endsWith("_zt")) {
			field.setType("dict");
			field.setParam("status");
		}
		if(fieldName.startsWith("is_") || fieldName.startsWith("can_") || fieldName.startsWith("has_")) {
			field.setType("boolean");
		}
		if(fieldName.equals("phone") || fieldName.endsWith("_phone")) valids.add("phone");
		if(fieldName.equals("url") || fieldName.endsWith("_url")) valids.add("url");
		if(fieldName.endsWith("_num") || fieldName.endsWith("_count")) {
			valids.add("number");
		}
		if(fieldName.endsWith("_time") || fieldName.endsWith("_date")) {
			valids.add("date");
		}
		if(fieldName.indexOf("identity") != -1) valids.add("identity");
		if(fieldName.indexOf("_ip") != -1) {
			valids.add("ip");
		}
		if(fieldName.equals("account")) valids.add("username");
		if(fieldName.equals("password")) valids.add("password");
		if(fieldName.endsWith("_file"))field.setType("file");
		if(fieldName.endsWith("_image"))field.setType("image");
		if(fieldName.endsWith("_video"))field.setType("video");
		/*
		if(fieldName.endsWith("_id")) {
			field.setType("relation");
		}
		*/
		if("number".equalsIgnoreCase(field.getType()) || "long".equalsIgnoreCase(field.getType()) || "int".equalsIgnoreCase(field.getType())
				|| "float".equalsIgnoreCase(field.getType()) || "decimal".equalsIgnoreCase(field.getType())){
			valids.add("number");
			field.setType("number");
			field.setLen(Integer.valueOf(r.get("NUMERIC_PRECISION").toString()));
			field.setScale(Integer.valueOf(r.get("NUMERIC_SCALE").toString()));
		}else if("char".equalsIgnoreCase(field.getType())) {
			field.setType("char");
			field.setLen(Integer.valueOf(r.get("CHARACTER_MAXIMUM_LENGTH").toString()));
		}else if("varchar2".equalsIgnoreCase(field.getType())) {
			field.setType("varchar2");
			field.setLen(Integer.valueOf(r.get("CHAR_LENGTH").toString()));
			if(field.getLen() > 200) {
				field.setType("text");
			}
		}else if("date".equalsIgnoreCase(field.getType())){
			field.setType("date");
		}
		
		Object od = r.get("DATA_DEFAULT");
		if(od != null) field.setDefaults(od.toString());
		
		if(!valids.isEmpty()) {
			String[] vs ;
			//field.setValidation(StrKit.join((String[])valids.toArray(), ","));
		}
		return field;
	}

	
	@Override
	public Models exportModel(String table) {
		Models model = new Models();
		String module = table.substring(0, table.indexOf('_'));
		String code = table.substring(module.length()+1).replace("_", "");

		Module m = new Module();
		m.setCode(module);
		m.setTitle(module);		
		
		String sql = "select * from dev_model where tables ='"+table+"' LIMIT 1";
		List<Record> rs = Db.find(sql);
		if(rs == null || rs.size() == 0) {
			sql = "select table_name from information_schema.tables where table_schema=(select database()) AND talb_name='"+table+"'";
			rs = Db.find(sql);
			if(rs == null || rs.size() == 0) {
				return null;
			}else {
				Record data = rs.get(0);
				model.setCode(code);
				model.setTitle(data.get("table_comment").toString());
				model.setType("1");
			}
		}else {
			Record data = rs.get(0);
			model.setCode(data.get("code").toString());
			model.setTitle(data.get("title").toString());
			model.setType(data.get("type").toString());
			model.setId(Long.valueOf(data.get("id").toString()));	

			sql = "select * from dev_module where id ='"+data.get("id").toString()+"' LIMIT 1";
			rs = Db.find(sql);
			if(rs != null && rs.size() >= 0) {
				m.setTitle(rs.get(0).get("title").toString());
			}
		}	
		model.setFields(exportFields(table));
		model.setModule(m);

		return model;
	}

}
