package org.pp.modules.dev.controller;

import java.util.ArrayList;
import java.util.List;

import org.pp.core.BaseController;
import org.pp.core.RestObject;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class DatabaseController extends BaseController {
	
	public void tables() {
		List<Record> list = Db.find("SELECT * FROM  information_schema.`TABLES`  WHERE TABLE_SCHEMA=(SELECT DATABASE());");
		List<Record> res = new ArrayList<>();
		for(Record t:list) {
			Record r = new Record();
			r.set("name", t.getStr("TABLE_NAME"));
			r.set("comment", t.getStr("TABLE_COMMENT")+"("+t.getStr("TABLE_NAME")+")");
			res.add(r);
		}
		renderJson(RestObject.success(res.size(), res));
	}

	public void query() {
		if(!isParaBlank("sql")) {
			String sql = getPara("sql");
			List<Record> list = Db.find(sql);
			if(list.size() > 0) {
				keepPara();
				set("data", JsonKit.toJson(list));
				set("cols", list.get(0).getColumnNames());
			}
		}		
		render("query.html");		
	}
}
