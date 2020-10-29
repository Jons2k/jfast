package org.pp.core;

import org.pp.handler.HtmlHandler;
import org.pp.controller.DefaultRoutes;
import org.pp.interceptor.ExceptionInterceptor;
import org.pp.modules.dev.controller.DevRoutes;
import org.pp.modules.sys.controller.SysRoutes;
import org.pp.tpl.AdminTpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;

public class Config extends JFinalConfig {
	public void configConstant(Constants me) {
		PropKit.use("config.txt");
		me.setDevMode(true);
		me.setToSlf4jLogFactory();
		me.setInjectDependency(true);

		JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
		me.setJsonFactory(new FastJsonFactory());

		// 配置 encoding，默认为 UTF8
		me.setEncoding("UTF8");

		// 配置 json 转换 Date 类型时使用的 data parttern
		me.setJsonDatePattern("yyyy-MM-dd HH:mm:ss");

		// 配置是否拒绝访问 JSP，是指直接访问 .jsp 文件，与 renderJsp(xxx.jsp) 无关
		me.setDenyAccessJsp(true);

		// 配置上传文件最大数据量，默认 10M
		me.setMaxPostSize(50 * 1024 * 1024);
		me.setBaseUploadPath(PropKit.get("upload.base"));
		me.setBaseDownloadPath(PropKit.get("upload.base"));

		me.setError401View("/view/public/error/401.html");
		me.setError403View("/view/public/error/403.html");
		me.setError404View("/view/public/error/404.html");
		me.setError500View("/view/public/error/500.html");
	}

	public void configRoute(Routes me) {
		// 如果要将控制器超类中的 public 方法映射为 action 配置成 true，一般不用配置
		me.setMappingSuperClass(true);

		// 配置 baseViewPath，可以让 render(...) 参数省去 baseViewPath 这部分前缀
		me.setBaseViewPath("/view");
		
		//默认路由
		me.add(new DefaultRoutes());

		//业务路由
		me.add(new DevRoutes());
		me.add(new SysRoutes());
	}

	public void configEngine(Engine me) {
		PropKit.use("config.txt");
		me.addSharedObject("CONTEXT", PropKit.get("tpl.context"));
		me.addSharedObject("UPLOAD", PropKit.get("tpl.upload"));
		me.addSharedObject("STATICS", PropKit.get("tpl.statics"));
		me.addSharedObject("VERSION", PropKit.get("tpl.version"));
		me.addSharedObject("SHARP", "#");
		me.addSharedObject("ADMIN", new AdminTpl());
	}

	public void configPlugin(Plugins me) {
		PropKit.use("config.txt");
		DruidPlugin dp = new DruidPlugin(PropKit.get("db.url"), PropKit.get("db.username"), PropKit.get("db.password"));
		me.add(dp);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		arp.setDevMode(true);
		arp.setShowSql(true);
		me.add(arp);

		org.pp.modules.dev.model._MappingKit.mapping(arp);
		org.pp.modules.sys.model._MappingKit.mapping(arp);

		me.add(new EhCachePlugin());

		me.add(new Cron4jPlugin(PropKit.getProp(), "cron4j"));
	}

	public void configInterceptor(Interceptors me) {
		me.add(new ExceptionInterceptor());
	}

	public void configHandler(Handlers me) {
		me.add(new HtmlHandler());
	}

	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 8000, "/", 5);
	}
}
