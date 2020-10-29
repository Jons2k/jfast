package org.pp.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.jfinal.plugin.druid.DruidPlugin;

public class JFinalUtil {

	public static boolean BUILD_CONTROLLER = true;
	public static boolean BUILD_ROUTE = true;
	public static boolean BUILD_SERVICE = true;
	public static boolean BUILD_MENU = false;
	
	public static void main(String[] args) {
		String module= "mooc";
		
		new JFinalUtil().genModule(module);
		//new JFinalUtil().genTable("sys_user_login");
	}
	
	public void genModule(String module) {
		String baseModelPkg = "org.pp.modules."+module+".base";
		String basePath = PathKit.getWebRootPath() + "/..";
		// base model 文件保存路径
		String baseModelDir = basePath + "/src/org/pp/modules/"+module+"/base";
		 
		// model 所使用的包名
		String modelPkg = "org.pp.modules."+module+".model";
		// model 文件保存路径
		String modelDir = basePath + "/src/org/pp/modules/"+module+"/model";
		PropKit.use("config.txt");
		DruidPlugin dp = new DruidPlugin(PropKit.get("db.url"), PropKit.get("db.username"), PropKit.get("db.password"));
		dp.start();
		Generator gernerator = new Generator(dp.getDataSource(), baseModelPkg, baseModelDir, modelPkg, modelDir);
		// 在 getter、setter 方法上生成字段备注内容
		gernerator.setGenerateRemarks(true);
		PrefixMetaBuilder pm = new PrefixMetaBuilder(dp.getDataSource());
		pm.setPrefix(module);
		gernerator.setMetaBuilder(pm);
		gernerator.setGenerateRemarks(true);
		gernerator.setRemovedTableNamePrefixes(module+"_");
		gernerator.generate();
	}
	
	public void genTable(String table) {
		String module = table.substring(0,table.indexOf("_"));
		String baseModelPkg = "org.pp.modules."+module+".base";
		String basePath = PathKit.getWebRootPath() + "/..";
		// base model 文件保存路径
		String baseModelDir = basePath + "/src/org/pp/modules/"+module+"/base";
		 
		// model 所使用的包名
		String modelPkg = "org.pp.modules."+module+".model";
		// model 文件保存路径
		String modelDir = basePath + "/src/org/pp/modules/"+module+"/model";
		PropKit.use("config.txt");
		DruidPlugin dp = new DruidPlugin(PropKit.get("db.url"), PropKit.get("db.username"), PropKit.get("db.password"));
		dp.start();
		Generator gernerator = new Generator(dp.getDataSource(), baseModelPkg, baseModelDir, modelPkg, modelDir);
		// 在 getter、setter 方法上生成字段备注内容
		gernerator.setGenerateRemarks(true);
		TableMetaBuilder pm = new TableMetaBuilder(dp.getDataSource());
		pm.setPrefix(module);
		pm.setTable(table);
		gernerator.setMetaBuilder(pm);
		gernerator.setGenerateRemarks(true);
		gernerator.setRemovedTableNamePrefixes(module+"_");
		//gernerator.setMappingKitGenerator(mappingKitGenerator);
		gernerator.setMappingKitOutputDir(modelDir+"/temp");
		gernerator.generate();
	}
		
	class PrefixMetaBuilder extends MetaBuilder {
	    protected Set<String> containTables = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	    private String prefix = "";
	    public PrefixMetaBuilder(DataSource dataSource) {
	        super(dataSource);
	    }
	 
	    @Override
	    protected void buildTableNames(List<TableMeta> ret) throws SQLException {
	        ResultSet rs = getTablesResultSet();
	        while (rs.next()) {
	            String tableName = rs.getString("TABLE_Name").toLowerCase();
	            
	            if (!tableName.startsWith(prefix+"_")) {
	                System.out.println("Skip table :" + tableName);
	                continue;
	            }
	            TableMeta tableMeta = new TableMeta();
	            tableMeta.name = tableName;
	            tableMeta.remarks = rs.getString("REMARKS");
	 
	            tableMeta.modelName = buildModelName(tableName);
	            tableMeta.baseModelName = buildBaseModelName(tableMeta.modelName);
	            ret.add(tableMeta);
	            if(JFinalUtil.BUILD_CONTROLLER) api(tableMeta);
	            if(JFinalUtil.BUILD_SERVICE) service(tableMeta);
	        }
	        rs.close();
	        if(JFinalUtil.BUILD_ROUTE) routes(ret);
	    }

	    /**
	     * 生成接口
	     * @param table
	     */
	    public void api(TableMeta table) {
	    	String basePath = PathKit.getWebRootPath()+"/../src/org/pp/modules/"+prefix+"/controller/";
	    	FileUtil.mkdirs(basePath);
	    	String saveFile = basePath+table.modelName+"Controller.java";
	    	FileUtil.move(saveFile, saveFile+".back");
	    	String content = "package org.pp.modules."+prefix+".controller;\r\n" + 
	    			"\r\n" + 
	    			"import com.jfinal.aop.Inject;\r\n\r\n" + 
	    			"import org.pp.core.AdminController;\r\n" + 
	    			"import org.pp.core.BaseService;\r\n" + 
	    			"import org.pp.modules."+prefix+".model."+table.modelName+";\r\n" + 
	    			"import org.pp.modules."+prefix+".service."+table.modelName+"Service;\r\n" + 
	    			"\r\n" + 
	    			"public class "+table.modelName+"Controller extends AdminController<"+table.modelName+"> {\r\n" + 
	    			"\r\n" +
	    			"	@Inject\r\n" + 
	    			"	"+table.modelName+"Service service;\r\n\r\n\r\n" + 
	    			"	@Override\r\n" + 
	    			"	protected BaseService<"+table.modelName+"> getService() {\r\n" + 
	    			"		return service;\r\n" + 
	    			"	}\r\n" + 
	    			"\r\n" + 
	    			"}\r\n" + 
	    			"";
	    	FileUtil.write(saveFile, content);
	    }
	    
	    /**
	     * 生成服务类
	     * @param table
	     */
	    public void service(TableMeta table) {
	    	String basePath = PathKit.getWebRootPath()+"/../src/org/pp/modules/"+prefix+"/service/";
	    	FileUtil.mkdirs(basePath);
	    	String saveFile = basePath+table.modelName+"Service.java";
	    	FileUtil.move(saveFile, saveFile+".back");
	    	String content = "package org.pp.modules."+prefix+".service;\r\n" + 
	    			"\r\n" + 
	    			"import org.pp.core.BaseService;\r\n" + 
	    			"import org.pp.modules."+prefix+".model."+table.modelName+";\r\n" + 
	    			"\r\n" + 
	    			"public class "+table.modelName+"Service implements BaseService<"+table.modelName+">{" + 
	    			"	\r\n" + 
	    			"	public static "+table.modelName+" dao = new "+table.modelName+"().dao();\r\n" + 
	    					"	private String error = \"\";\r\n\r\n" + 
	    					"	@Override\r\n" + 
	    					"	public String getError() {\r\n" + 
	    					"		return error;\r\n" + 
	    					"	}\r\n" + 
	    					"\r\n" + 
	    					"	public void setError(String error) {\r\n" + 
	    					"		this.error = error;\r\n" + 
	    					"	}\r\n" + 
	    			"	\r\n" + 
	    			"	@Override\r\n" + 
	    			"	public "+table.modelName+" getModel() {\r\n" + 
	    			"		return dao;\r\n" + 
	    			"	}\r\n" + 
	    			"}\r\n" + 
	    			"";
	    	FileUtil.write(saveFile, content);
	    }
	    
	    /**
	     * 生成路由
	     * @param ret
	     */
	    public void routes(List<TableMeta> ret) {
	    	String basePath = PathKit.getWebRootPath()+"/../src/org/pp/modules/"+prefix+"/controller/";
	    	FileUtil.mkdirs(basePath);
	    	String module = StrKit.firstCharToUpperCase(prefix);
	    	String saveFile = basePath+module+"Routes.java";
	    	FileUtil.move(saveFile, saveFile+".back");
	    	String content = "package org.pp.modules."+prefix+".controller;\r\n" + 
	    			"\r\n" + 
	    			"import com.jfinal.config.Routes;\r\n" + 
	    			"\r\n" + 
	    			"public class "+module+"Routes extends Routes{\r\n" + 
	    			"\r\n" + 
	    			"	@Override\r\n" + 
	    			"	public void config() {	\r\n";
	    	for(TableMeta tm:ret) {
	    		content += "		add(\"/"+prefix+"/"+tm.modelName.toLowerCase()+"\", "+tm.modelName+"Controller.class);\r\n";
	    	}	    		
	    	content +=		"	}\r\n" + 
	    			"\r\n" + 
	    			"}";
	    	FileUtil.write(saveFile, content);
	    }
	    
		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix.toLowerCase();
		}
	}

	class TableMetaBuilder extends MetaBuilder {
	    protected Set<String> containTables = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	    private String table = "";
	    private String prefix = "";
	    public TableMetaBuilder(DataSource dataSource) {
	        super(dataSource);
	    }
	 
	    @Override
	    protected void buildTableNames(List<TableMeta> ret) throws SQLException {
	        ResultSet rs = getTablesResultSet();
	        while (rs.next()) {
	            String tableName = rs.getString("TABLE_Name").toLowerCase();
	            
	            if (!tableName.equalsIgnoreCase(table)) {
	                System.out.println("Skip table :" + tableName);
	                continue;
	            }
	            TableMeta tableMeta = new TableMeta();
	            tableMeta.name = tableName;
	            tableMeta.remarks = rs.getString("REMARKS");
	 
	            tableMeta.modelName = buildModelName(tableName);
	            tableMeta.baseModelName = buildBaseModelName(tableMeta.modelName);
	            ret.add(tableMeta);
	            if(JFinalUtil.BUILD_CONTROLLER) api(tableMeta);
	            if(JFinalUtil.BUILD_SERVICE) service(tableMeta);
	        }
	        rs.close();
	    }

	    /**
	     * 生成接口
	     * @param table
	     */
	    public void api(TableMeta table) {
	    	String basePath = PathKit.getWebRootPath()+"/../src/org/pp/modules/"+prefix+"/controller/";
	    	FileUtil.mkdirs(basePath);
	    	String saveFile = basePath+table.modelName+"Controller.java";
	    	FileUtil.move(saveFile, saveFile+".back");
	    	String content = "package org.pp.modules."+prefix+".controller;\r\n" + 
	    			"\r\n" + 
	    			"import com.jfinal.aop.Inject;\r\n\r\n" + 
	    			"import org.pp.core.AdminController;\r\n" + 
	    			"import org.pp.core.BaseService;\r\n" + 
	    			"import org.pp.modules."+prefix+".model."+table.modelName+";\r\n" + 
	    			"import org.pp.modules."+prefix+".service."+table.modelName+"Service;\r\n" + 
	    			"\r\n" + 
	    			"public class "+table.modelName+"Api extends AdminController<"+table.modelName+"> {\r\n" + 
	    			"\r\n" +
	    			"	@Inject\r\n" + 
	    			"	"+table.modelName+"Service service;\r\n\r\n\r\n" + 
	    			"	@Override\r\n" + 
	    			"	protected BaseService<"+table.modelName+"> getService() {\r\n" + 
	    			"		return service;\r\n" + 
	    			"	}\r\n" + 
	    			"\r\n" + 
	    			"}\r\n" + 
	    			"";
	    	FileUtil.write(saveFile, content);
	    }
	    
	    /**
	     * 生成服务类
	     * @param table
	     */
	    public void service(TableMeta table) {
	    	String basePath = PathKit.getWebRootPath()+"/../src/org/pp/modules/"+prefix+"/service/";
	    	FileUtil.mkdirs(basePath);
	    	String saveFile = basePath+table.modelName+"Service.java";
	    	FileUtil.move(saveFile, saveFile+".back");
	    	String content = "package org.pp.modules."+prefix+".service;\r\n" + 
	    			"\r\n" + 
	    			"import org.pp.core.BaseService;\r\n" + 
	    			"import org.pp.modules."+prefix+".model."+table.modelName+";\r\n" + 
	    			"\r\n" + 
	    			"public class "+table.modelName+"Service implements BaseService<"+table.modelName+">{" + 
	    			"	\r\n" + 
	    			"	public static "+table.modelName+" dao = new "+table.modelName+"().dao();\r\n" + 
	    					"	private String error = \"\";\r\n\r\n" + 
	    					"	@Override\r\n" + 
	    					"	public String getError() {\r\n" + 
	    					"		return error;\r\n" + 
	    					"	}\r\n" + 
	    					"\r\n" + 
	    					"	public void setError(String error) {\r\n" + 
	    					"		this.error = error;\r\n" + 
	    					"	}\r\n" + 
	    			"	\r\n" + 
	    			"	@Override\r\n" + 
	    			"	public "+table.modelName+" getModel() {\r\n" + 
	    			"		return dao;\r\n" + 
	    			"	}\r\n" + 
	    			"}\r\n" + 
	    			"";
	    	FileUtil.write(saveFile, content);
	    } 
	  
	    public void menu(TableMeta table) {
	    	Record app = Db.findFirst("SELECT * FROM sys_application WHERE url=?", prefix);
	    	if(app == null) {
	    		app = new Record();
	    		app.set("url", prefix);
	    		app.set("title", prefix);
	    		app.set("type", "i");
	    		app.set("list_sort", 9);
	    		Db.save("sys_application", app);
	    	}
	    	String url = prefix+"/"+table.modelName.toLowerCase()+"/list.html";
	    	Record menu = Db.findFirst("SELECT * FROM sys_menu WHERE app_id=? AND url=?", app.getInt("id"), url);
	    	if(menu == null) {
	    		menu = new Record();
	    		menu.set("pid", 0);
	    		menu.set("title", table.remarks == null ? table.modelName :table.remarks);
	    		menu.set("url", url);
	    		menu.set("app_id", app.getInt("id"));
	    		menu.set("list_sort", 9);
	    		menu.set("type", "1");
	    		Db.save("sys_menu", menu);
	    	}
	    }
	    
		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix.toLowerCase();
		}

		public String getTable() {
			return table;
		}

		public void setTable(String table) {
			this.table = table;
		}
	}
	
}
