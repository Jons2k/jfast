package org.pp.utils.ddl;

import java.util.ArrayList;
import java.util.List;

import org.pp.modules.dev.model.Field;
import org.pp.modules.dev.model.Models;
import org.pp.modules.dev.model.Module;
import org.pp.modules.dev.model.Validation;
import org.pp.modules.dev.service.FieldService;
import org.pp.modules.dev.service.ModelsService;
import org.pp.modules.dev.service.ModuleService;
import org.pp.modules.dev.service.ValidationService;
import org.pp.utils.db.Adapter;
import org.pp.utils.db.DbFactory;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;

public class ImportDb {
	private Adapter db;
	private static boolean createTable = true;
	private static boolean saveModel = true;
	private static boolean saveMenu = false;

	public static void main(String[] args) {

		PropKit.use("config.txt");
		DruidPlugin dp = new DruidPlugin(PropKit.get("db.url"), PropKit.get("db.username"), PropKit.get("db.password"));
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		arp.setDevMode(true);
		arp.setShowSql(true);
		org.pp.modules.dev.model._MappingKit.mapping(arp);
		dp.start();
		arp.start();


		String file = "D:\\java\\work-my\\jmooc\\docs\\设计文档\\数据库设计.xls";

		ImportDb idb = new ImportDb();
		idb.db = DbFactory.create("mysql");
		int total = idb.import2Db(file, 0);

		System.out.println("成功导入" + total + "张数据表");
	}

	/**
	 * 将excel文件中的数据表定义导入到数据库
	 * 
	 * @param String excelFile xls文件路径
	 * @param        int sheet 工作簿
	 * @return int 导入成功的表个数
	 */
	public int import2Db(String excelFile, int sheet) {

		List<Models> list = new Excels().readModels(excelFile, sheet);
		if (list.size() < 1) {
			System.out.println("未发现有效数据表!");
			return 0;
		}

		int n = 0;
		try {
			// System.setOut(new PrintStream("D:/sql.txt"));
			for (Models model : list) {
				n++;

				String table = model.getTables();
				String module = table.substring(0, table.indexOf('_'));
				Module m = new Module();
				m.setCode(module);
				m.setTitle(module);
				model.setCode(Excels.lineToHump(table.substring(module.length() + 1)));
				model.setModule(m);
				
				if(ImportDb.createTable) {
					db.drop(model);
					db.create(model);
				}
				
				if (ImportDb.saveModel) {
					saveModel(model);
				}
				
				if (ImportDb.saveMenu) {
					saveMenu(model);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}

	public void saveModel(Models m) {
		String sql = "SELECT * from dev_models where tables='" + m.getTables() + "'";
		Models om = ModelsService.dao.findFirst(sql);
		if (om == null) {
			sql = "SELECT * from dev_module where code='" + m.getModule().getCode() + "'";
			Module oom = ModuleService.dao.findFirst(sql);
			if (oom == null) {
				oom = m.getModule();
				oom.setTitle(oom.getCode());
				oom.setStatus("1");
				oom.save();				
			}
			m.setModuleId(oom.getId());
			m.save();
		}else {
			m.setId(om.getId());
		}		

		for (Field f : m.getFields()) {
			f.setModelId(m.getId());
			saveField(f);
		}
	}

	public void saveField(Field field) {
		String fieldName = field.getCode();
		List<Validation> valids = new ArrayList<>();
				
		if(fieldName.equals("email") || fieldName.endsWith("_email")) {
			Validation vd = new Validation();
			vd.setType("regex");
			vd.setRule("email");
			vd.setTitle(field.getTitle()+"必须是邮箱格式:XXX@XX.com");
			valids.add(vd);
			field.setType("email");
		}
		if("status".equals(fieldName) ||fieldName.endsWith("_status") || fieldName.endsWith("_zt")) {
			field.setType("dict");
			field.setParam("status");
		}
		if(fieldName.startsWith("is_") || fieldName.startsWith("can_") || fieldName.startsWith("has_")) {
			field.setType("dict");
			field.setParam("boolean");
		}
		if(fieldName.equals("phone") || fieldName.endsWith("_phone")) {
			Validation vd = new Validation();
			vd.setType("regex");
			vd.setRule("phone");
			vd.setTitle(field.getTitle()+"必须是手机号码格式:1xxxxxxxxxx");
			valids.add(vd);
		}
		if(fieldName.equals("url") || fieldName.endsWith("_url")) {
			Validation vd = new Validation();
			vd.setType("regex");
			vd.setRule("url");
			vd.setTitle(field.getTitle()+"必须是URL地址格式:http://xxx");
			valids.add(vd);
		}
		if(fieldName.endsWith("_num") || fieldName.endsWith("_count")) {
			Validation vd = new Validation();
			vd.setType("regex");
			vd.setRule("number");
			vd.setTitle(field.getTitle()+"必须是数字格式");
			valids.add(vd);
		}
		if(fieldName.endsWith("_time")) {
			Validation vd = new Validation();
			vd.setType("regex");
			vd.setRule("datetime");
			vd.setTitle(field.getTitle()+"必须是日期时间格式:2019-03-06 12:24:00");
			valids.add(vd);
			field.setType("datetime");
		}
		if(fieldName.endsWith("_date")) {
			Validation vd = new Validation();
			vd.setType("regex");
			vd.setRule("date");
			vd.setTitle(field.getTitle()+"必须是日期格式:2019-03-06");
			valids.add(vd);
			field.setType("date");
		}
		if(fieldName.indexOf("identity") != -1) {
			Validation vd = new Validation();
			vd.setType("regex");
			vd.setRule("identity");
			vd.setTitle(field.getTitle()+"必须是18位身份证号");
			valids.add(vd);
		}
		if(fieldName.indexOf("_ip") != -1) {
			Validation vd = new Validation();
			vd.setType("regex");
			vd.setRule("ip");
			vd.setTitle(field.getTitle()+"必须是有效IP地址");
			valids.add(vd);
		}
		if(fieldName.endsWith("_file")) field.setType("file");
		if(fieldName.endsWith("_image")) field.setType("file");
		if(fieldName.endsWith("_video")) field.setType("file");
		if("string".equalsIgnoreCase(field.getType()) || "char".equalsIgnoreCase(field.getType())) {
			Validation vd = new Validation();
			vd.setType("length");
			vd.setRule(""+field.getLen());
			vd.setTitle(field.getTitle()+"长度不能大于"+field.getLen());
			valids.add(vd);
		}
		if("datetime".equalsIgnoreCase(field.getType())) {
			field.setLen(20);
		}
		if("date".equalsIgnoreCase(field.getType())) {
			field.setLen(10);
		}
		Field of = FieldService.dao.findFirst("SELECT * FROM dev_field WHERE model_id=? AND code=? ", field.getModelId(), field.getCode());
		if(of == null){
			field.save();
		}else {
			field.setId(of.getId());
			field.update();
		}
		
		for(Validation v:valids) {
			Validation ov = ValidationService.dao.findFirst("SELECT * FROM dev_validation WHERE field_id=? AND type=? and rule=?", field.getId(), v.getType(), v.getRule());
			if(ov != null) continue;
			v.setFieldId(field.getId());
			v.setModelId(field.getModelId());
			v.save();
		}
	}

	public void saveMenu(Models m) {
		String mc = m.getModule().getCode();
    	Record app = Db.findFirst("SELECT * FROM sys_application WHERE url=?", mc);
    	if(app == null) {
    		app = new Record();
    		app.set("url", mc);
    		app.set("title", mc);
    		app.set("type", "i");
    		app.set("list_sort", 9);
    		Db.save("sys_application", app);
    	}
    	String url = mc+"/"+m.getCode().toLowerCase()+"/list.html";
    	Record menu = Db.findFirst("SELECT * FROM sys_menu WHERE app_id=? AND url=?", app.getInt("id"), url);
    	if(menu == null) {
    		menu = new Record();
    		menu.set("pid", 0);
    		menu.set("title", m.getTitle());
    		menu.set("url", url);
    		menu.set("app_id", app.getInt("id"));
    		menu.set("list_sort", 9);
    		menu.set("type", "1");
    		Db.save("sys_menu", menu);
    	}
    }
	
	public static List<Field> getDefaults() {
		List<Field> list = new ArrayList<Field>();
		/*
		 * Field f1 = new Field(); f1.setCode("year"); f1.setLen(11);
		 * f1.setNullable(false); f1.setTitle("年度"); f1.setType("number");
		 * f1.setScale(0); list.add(f1); return list;
		 */
		Field f1 = new Field();
		f1.setCode("org_id");
		f1.setLen(11);
		f1.setNullable("0");
		f1.setTitle("学校");
		f1.setType("number");
		f1.setScale(0);
		list.add(f1);

		Field f2 = new Field();
		f2.setCode("task_id");
		f2.setLen(11);
		f2.setNullable("0");
		f2.setTitle("任务");
		f2.setType("number");
		f2.setScale(0);
		list.add(f2);

		return list;
	}
}
