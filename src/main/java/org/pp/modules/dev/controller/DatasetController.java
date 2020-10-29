package org.pp.modules.dev.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.core.RestObject;
import org.pp.modules.dev.model.Dataset;
import org.pp.modules.dev.service.DatasetService;

public class DatasetController extends AdminController<Dataset> {

	@Inject
	DatasetService service;
	
	@Override
	protected BaseService<Dataset> getService() {
		return service;
	}

	/**
	 * 查询数据
	 */
	public void data() {
		if(isParaBlank("id") && isParaBlank("code")) {
			renderJson(RestObject.error("非法请求"));
			return;
		}
		Dataset ds;
		if(!isParaBlank("id")) {
			ds = service.find(getParaToLong("id"));
		}else {
			ds = service.findFirst(Kv.by("code = ", getPara("code")));
		}
		
		if(ds == null) {
			renderJson(RestObject.error("数据不存在"));
			return;
		}
		
		Kv where = Kv.create();
		Enumeration<String> ps = getParaNames();
		while(ps.hasMoreElements()) {
			String p = ps.nextElement();
			if("id".equalsIgnoreCase(p) || "code".equalsIgnoreCase(p)) continue;
			
			where.put(p + " = "	, getPara(p));			
		}
		SqlPara sql = Db.getSqlParaByString(ds.getSqls(), Kv.by("cond", where));
		
		List<Record> list = Db.find(sql);
		renderJson(RestObject.success(list.size(), list));
	}
	
	/**
	 * 预览数据
	 */
	public void query() {
		if(isParaBlank("id")) {
			error("404", "非法请求");
			return;
		}
		Dataset ds = service.find(getParaToLong("id"));
		if(ds == null) {
			error("404", "非法请求");
			return;
		}
		String[] sps = {">", ">=", "<", "<=", "=", "like"};
		Kv where = Kv.create();
		for(int i=0;i<10;i++) {
			String p = "cond"+i;
			if(!isParaBlank(p)) {
				String v = getPara(p);
				String[] va;
				for(String sp:sps) {
					String s = " "+sp+" ";
					if(v.indexOf(s) > 0) {
						va = v.split(s);
						if("like".equalsIgnoreCase(sp)){
							where.put(va[0] + s, "%"+va[1]+"%");
						}else {
							where.put(va[0] + s, va[1]);
						}
					}
				}				
			}
		}
		SqlPara sql = Db.getSqlParaByString(ds.getSqls(), Kv.by("cond", where));
		
		List<Record> list = Db.find(sql);
		if(list.size() > 0) {
			keepPara();
			set("data", JsonKit.toJson(list));
			set("cols", list.get(0).getColumnNames());
		}	
		set("vo", ds);
		render("query.html");		
	}
	
	@Override
	protected Kv search() {
		Kv kv= Kv.create();
		if(!isParaBlank("cateId")) {
			kv.put("cate_id = ", getParaToLong("cateId"));
		}			
		if(!isParaBlank("code")) {
			kv.put("code like ", "%"+getPara("code")+"%");
		}		
		if(!isParaBlank("title")) {
			kv.put("title like ", "%"+getPara("title")+"%");
		}		
		if(!isParaBlank("status")) {
			kv.put("status = ", getPara("status"));
		}				
		return kv;
	}
}
