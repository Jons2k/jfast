package org.pp.utils.ddl;

import java.util.List;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;

public class ImportDict {
	
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
		
		ImportDict idb = new ImportDict();
		int total = idb.import2Db(file, 1);
				
		System.out.println("成功导入"+total+"条字典目录");		
	}
	
	/**
	 * 将excel文件中的数据表定义导入到数据库
	 * @param String excelFile xls文件路径
	 * @param int sheet 工作簿
	 * @return int 导入成功的表个数 
	 */
	public int import2Db(String excelFile, int sheet) {

		List<String[]> list = new Excels().readAll(excelFile, sheet, 1);
		if(list.size() < 1) {
			System.out.println("未发现有效数据字典!");
			return 0;
		}

		int n=0;	
		try {
			for(String[] rs: list) {
				saveDict(rs);
				n++;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}

	
	
	public boolean saveDict(String[] dict) {
		if(dict.length < 2) return false;
		
		Record d = new Record();
		d.set("code", dict[0].trim());
		d.set("title", dict[1].trim());
		d.set("list_sort", 99);
		d.set("type", "d");
		d.set("pid", 0);
		
		if(!Db.save("sys_dict", d)) return false;
		
		for(int i=2;i<dict.length;i++) {
			if("".contentEquals(dict[i].trim())) continue;
			

			Record r = new Record();
			r.set("list_sort", i-1);
			r.set("type", "v");
			r.set("pid", d.getInt("id"));
			
			String[] v = dict[i].split("=");
			r.set("code", v[0].trim());
			if(v.length == 1) {
				r.set("title", v[0].trim());
			}else {
				r.set("title", v[1].trim());				
			}
			if(!Db.save("sys_dict", r)) return false;
		}
		return true;
	}

}
