package org.pp.utils.db;

import java.util.ArrayList;
import java.util.List;

import org.pp.modules.dev.model.Field;
import org.pp.modules.dev.model.Models;
import org.pp.utils.ddl.Excels;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class Oracle extends Adapter {

	private String storage() {
		return "LOGGING\r\n" + "NOCOMPRESS\r\n" + "PCTFREE 10\r\n" + "INITRANS 1\r\n" + "STORAGE (\r\n"
				+ "  INITIAL 1048576 \r\n" + "  NEXT 8192 \r\n" + "  MINEXTENTS 1\r\n" + "  MAXEXTENTS 2147483645\r\n"
				+ "  BUFFER_POOL DEFAULT\r\n" + ")\r\n" + "PARALLEL 1\r\n" + "NOCACHE\r\n" + "DISABLE ROW MOVEMENT";
	}

	@Override
	public void filters(List<Field> list) {
		int id = -1;
		for (int i = 0; i < list.size(); i++) {
			Field f = list.get(i);
			if ("id".equalsIgnoreCase(f.getCode())) {
				id = i;
				continue;
			}
			switch (f.getType().toLowerCase()) {
			case "char":
				if (f.getLen() == 0)
					f.setLen(1);
				break;
			case "varchar":
			case "varchar2":
				f.setType("varchar2");
				if (f.getLen() == 0)
					f.setLen(1);
				break;
			case "text":
				f.setType("varchar2");
				if (f.getLen() == 0)
					f.setLen(2000);
				break;
			case "longtext":
				f.setType("clob");
				break;
			case "decimal":
			case "float":
			case "number":
				f.setType("number");
				if (f.getLen() == 0)
					f.setLen(10);
				trimNumber(f);
				break;
			case "long":
				f.setType("number");
				if (f.getLen() == 0)
					f.setLen(20);
				trimNumber(f);
				break;
			case "int":
			case "integer":
				f.setType("number");
				if (f.getLen() == 0)
					f.setLen(10);
				trimNumber(f);
				break;
			case "tinyint":
				f.setType("number");
				if (f.getLen() == 0)
					f.setLen(4);
				break;
			case "double":
				f.setType("number");
				if (f.getLen() == 0)
					f.setLen(40);
				break;
			case "date":
			case "datetime":
			case "timestamp":
				f.setType("date");
				break;
			default:
				f.setType("varchar2");
				if (f.getLen() == 0)
					f.setLen(10);
				break;
			}
		}
		if (id != -1)
			list.remove(id);
	}

	private String buildField(Field f) {
		String sql = "" + f.getCode();
		switch (f.getType().toLowerCase()) {
		case "char":
			sql += " char(" + f.getLen() + " CHAR)";
			if (f.getDefaults() != null && f.getDefaults().endsWith(".0"))
				f.setDefaults(f.getDefaults().substring(0, f.getDefaults().length() - 2));
			break;
		case "varchar2":
			sql += " varchar2(" + f.getLen() + " CHAR)";
			if (f.getDefaults() != null && f.getDefaults().endsWith(".0"))
				f.setDefaults(f.getDefaults().substring(0, f.getDefaults().length() - 2));
			break;
		case "clob":
			sql += " clob";
			break;
		case "number":
			sql += " number(" + f.getLen() + "," + f.getScale() + ")";
			break;
		case "date":
			sql += " date";
			break;
		default:
			sql += " varchar2(10 CHAR)";
			break;
		}
		if (!f.isNullable())
			sql += " NOT NULL";
		return sql;
	}

	@Override
	public List<Models> exportTables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void drop(Models m) {
		String tableName = m.getTables().toUpperCase();
		String sql = "SELECT * FROM user_tables WHERE table_name = '" + model.getTables().toUpperCase() + "'";
		List<Record> list = Db.find(sql);
		if(list.size() > 0) {
			Db.update("DROP TABLE " + tableName);
		}
	}

	@Override
	public List<String> create() {
		List<String> sqls = new ArrayList<>();
		List<Field> list = model.getFields();
		this.filters(list);
		String sql = "";
		String tableName = model.getTables().toUpperCase();
		String space = "";
		if(tableName.indexOf(".") > 0) {
			space = tableName.substring(0,tableName.indexOf("."));
			tableName = tableName.substring(tableName.indexOf("."));
			sql = "CREATE TABLE " + tableName + " (\r\n ID NUMBER NOT NULL";
			for (Field f : list) {
				sql += " ,\r\n" + buildField(f);
			}
			sql += "\r\n)\r\n";
			sql += "TABLESPACE " + space + "\r\n";
		}else {
			sql = "CREATE TABLE " + tableName + " (\r\n ID NUMBER NOT NULL";
			for (Field f : list) {
				sql += " ,\r\n" + buildField(f);
			}
			sql += "\r\n)\r\n";
		}
		sql += storage();
		sqls.add(sql);
		
		for (Field f : list) {

			// comment
			if (Excels.isNotEmpty(f.getTitle())) {
				sql = "COMMENT ON COLUMN " + tableName + "." + f.getCode() + " IS '" + f.getTitle() + "'";
			}
			sqls.add(sql);

			// default
			if (!f.isNullable() || Excels.isEmpty(f.getDefaults()))
				continue;
			if ("number".equalsIgnoreCase(f.getType())) {
				sql = "ALTER TABLE " + tableName + " MODIFY (" + f.getCode() + " DEFAULT " + f.getDefaults() + ")";
			} else {
				sql = "ALTER TABLE " + tableName + " MODIFY (" + f.getCode() + " DEFAULT '" + f.getDefaults() + "')";
			}
			sqls.add(sql);
		}

		// 主键
		sql = "ALTER TABLE " + model.getTables() + " ADD CONSTRAINT PK_" + tableName	+ " PRIMARY KEY (ID)";

		sqls.add(sql);

		//序列		
		sql = "CREATE SEQUENCE " + tableName
				+ "_SEQ \r\n MINVALUE 1\r\n MAXVALUE 999999999999999999999999999\r\n"
				+ "START WITH 1\r\n INCREMENT BY 1\r\n CACHE 20";

		sqls.add(sql);
		
		
		//触发器
		sql = "CREATE OR REPLACE TRIGGER " + tableName + "_ID\r\n";
		sql += "BEFORE INSERT ON "+ tableName + " FOR EACH ROW\r\n";
		sql += "BEGIN\r\n";
		sql += "SELECT " + tableName + "_SEQ.nextval into:New.id from dual;\r\n";
		sql += "END;";
		sqls.add(sql);
		
		return sqls;
	}

	@Override
	public List<Field> exportFields(String table) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public Models exportModel(String table) {
		// TODO Auto-generated method stub
		return null;
	}
}
