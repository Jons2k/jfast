package org.pp.modules.dev.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.pp.core.BaseService;
import org.pp.modules.dev.model.Field;
import org.pp.modules.dev.model.Models;
import org.pp.modules.sys.model.Application;
import org.pp.modules.sys.model.Menu;
import org.pp.modules.sys.service.ApplicationService;
import org.pp.modules.sys.service.MenuService;
import org.pp.utils.ArrayUtil;
import org.pp.utils.CodeUtil;

import com.jfinal.aop.Aop;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

public class ModelsService implements BaseService<Models>{
	
	public static Models dao = new Models().dao();
	private static Map<String,Models> models;
	
	private String error = "";
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	@Override
	public Models getModel() {
		return dao;
	}
	
	/**
	 * 根据表名查询模型对象
	 * @param table string 表名
	 * @return Model
	 */
	public static Models getTableModel(String table) {
		return dao.findFirst("SELECT * FROM dev_models WHERE tables=?", table);
		// return dao.findFirstByCache("dev_models", table, "SELECT * FROM dev_models WHERE tables=?", table);
		
		/*
		if(models == null) {
			models = new HashMap<>();
			List<Models> list = dao.findAll();
			for(Models m:list) {
				models.put(m.getTables(), m);
			}
		}
		return models.get(table);
		*/
	}
	
	/**
	 * 生成模型的代码
	 * @param model Model 模型
	 * @param contents String[] 要生成 的内容 
	 * @param menus String[] 要生成 的菜单
	 * @return boolean
	 */
	public boolean genCode(Models model, String[] contents, String[] menus) {		
		FieldService fs = Aop.get(FieldService.class);		
		List<Field> fields = fs.modelField(model.getId());
		for(Field f:fields) {
			f.setCode(StrKit.toCamelCase(f.getCode()));
			if(Field.TYPE_RELATION.equalsIgnoreCase(f.getType())) {
				Models m = getTableModel(f.getParam().toLowerCase());
				f.setParam(m.getModule().getCode()+"/"+m.getCode().toLowerCase());
			}
		}
		model.setFields(fields);
		if(!CodeUtil.model(model, contents)){
			return false;
		}		

		for(String menu:menus) {
			if(StrKit.isBlank(menu)) continue;
			
			if(!createMenu(model, menu)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 创建模型的默认菜单
	 * @param model Model
	 * @return boolean
	 */
	public boolean createMenu(Models model,String menu) {
		String title = model.getTitle();
		if("list".equalsIgnoreCase(menu)) {
			if(title.length() < 3) title +="管理";
		}else{
			if(title.length() < 3) title +=menu;
		}
		String url = model.getModule().getCode().toLowerCase()+"/"+model.getCode().toLowerCase()+"/"+menu+".html";
		MenuService ms = Aop.get(MenuService.class);
		Menu m = ms.findFirst(Kv.by("url = ", url));
		if(m != null) {
			return true;
		}

		m = new Menu();
		m.setPid(0);
		m.setStatus("1");
		m.setTitle(title);
		m.setType("1");
		m.setUrl(url);
		m.setListSort(9);
		
		ApplicationService apps = Aop.get(ApplicationService.class);
		Application app = apps.findFirst(Kv.by("url = ", model.getModule().getCode()));
		if(app != null) {
			m.setAppId(app.getId());			
		}
		return m.save();
	}
	
	/**
	 * 根据数据表，生成模型信息
	 * @param m Model 模型
	 * @return boolean
	 */
	public boolean createModel(Models m) {
		return Db.tx(() -> {
			Kv cond = Kv.by("tables = ", m.getTables());
			Models o = findFirst(cond);
			
			if(o == null) {
				m.setCode(table2ModelName(m.getTables()));
				m.setStatus("1");
				if(!m.save()) {
					return false;
				}
				return loadField(m);
			}else {
				return loadField(o);
			}
		});
	}
	
	/**
	 * 从数据表生成模型的字段列表
	 * @param model Model模型对象
	 * @return boolean
	 */
	public boolean loadField(Models model) {
		FieldService fs = Aop.get(FieldService.class);
		Kv cond = null;
		
		List<Record> fields = Db.find("SELECT * FROM  information_schema.`COLUMNS`  WHERE TABLE_NAME='"+model.getTables()+"';");
		String[] ignores = PropKit.get("ddl.ignore", "id").split(",");
		for(Record r:fields) {
			String code = r.getStr("COLUMN_NAME");
			if(ArrayUtil.in(code, ignores)) {
				continue;
			}				

			cond = Kv.by("model_id = ", model.getId());
			cond.put("code = ", code);				
			
			Field f = fs.findFirst(cond);
			if(f != null) {
				continue;				
			}
			f=parseDbField(r);
			f.setModelId(model.getId());
			f.setListSort(9);
			if(!f.save()) {
				return false;
			}
		}
		return true;
	}
	
	public Field parseDbField(Record r) {
		Field f = new Field();
		f.setCode(r.getStr("COLUMN_NAME"));
		f.setDefaults(r.getStr("COLUMN_DEFAULT"));
		f.setNullable("yes".equalsIgnoreCase(r.getStr("IS_NULLABLE"))? "1":"0");
		f.setTitle(r.getStr("COLUMN_COMMENT") == null ? r.getStr("COLUMN_NAME"):r.getStr("COLUMN_COMMENT"));
		switch(r.getStr("DATA_TYPE").toLowerCase()) {
			case "int":
			case "smallint":
			case "bigint":
			case "tinyint":
			case "decimal":
			case "float":
			case "double":
				f.setLen(r.getInt("NUMERIC_PRECISION"));
				f.setScale(r.getInt("NUMERIC_SCALE"));
				f.setType("number");
				f.setDbType("number");
				break;
			case "text":
			case "tinytext":
			case "blob":
			case "tinyblob":
				f.setType("text");
				f.setDbType("text");
				f.setLen(r.getInt("CHARACTER_MAXIMUM_LENGTH"));
				break;
			case "date":
				f.setType("date");
				f.setDbType("date");
				f.setLen(10);
			case "datetime":
			case "timestamp":
				f.setType("datetime");
				f.setDbType("datetime");
				f.setLen(20);
				break;
			case "char":
			case "varchar":
			default:
				f.setType("string");
				f.setDbType("varchar");
				f.setLen(r.getInt("CHARACTER_MAXIMUM_LENGTH"));
				break;
		}
		return f;
	}
	
	@Override
	public boolean delete(long id) {
		return Db.tx(new IAtom() {			
			@Override
			public boolean run() throws SQLException {
				Db.delete("DELETE FROM dev_models WHERE id=?", id);
				Db.delete("DELETE FROM dev_field WHERE model_id=?", id);
				Db.delete("DELETE FROM dev_validation WHERE model_id=?", id);
				return true;
			}
		});
	}
	
	public String table2ModelName(String table) {
		String[] ns = table.split("_");
		String name="";
		for(int i =1;i<ns.length;i++) {
			name += StrKit.firstCharToUpperCase(ns[i]);
		}
		return name;
	}
		
}
