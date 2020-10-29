package org.pp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.pp.modules.sys.model.Action;
import org.pp.modules.sys.model.Application;
import org.pp.modules.sys.model.Menu;
import org.pp.modules.sys.model.Role;
import org.pp.modules.sys.model.User;
import org.pp.modules.sys.service.ActionService;
import org.pp.modules.sys.service.ApplicationService;
import org.pp.modules.sys.service.MenuService;
import org.pp.modules.sys.vo.ClearInfo;
import org.pp.modules.sys.vo.HomeInfo;
import org.pp.modules.sys.vo.InitInfo;
import org.pp.modules.sys.vo.LogoInfo;
import org.pp.modules.sys.vo.MenuInfo;

import com.jfinal.aop.Aop;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class SecurityUtil {
	private static ThreadLocal<User> users = new ThreadLocal<>();
	private static Map<String,Set<Integer>> actions = new HashMap<>();
	private static boolean checkAction = false;
	
	static {
		checkAction = Boolean.parseBoolean(PropKit.get("security.action", "false"));
		
		if(checkAction) {
			List<Action> list = ActionService.dao.findAll();
			String sql = "SELECT DISTINCT ma.action_id, rm.role_id FROM sys_menu_action ma "
					+ " JOIN sys_role_menu rm ON rm.menu_id = ma.menu_id ";
			List<Record>  roles = Db.find(sql);
			for(Action a:list) {
				Set<Integer> rs = new HashSet<>();
				for(Record f:roles) {
					if(f.getInt("action_id").intValue() == a.getId().intValue()) {
						rs.add(f.getInt("role_id"));
					}
				}
				actions.put(a.getUrl().toLowerCase(), rs);
			}
		}		
	}
	
	/**
	 * 密码加密
	 * @param u User 用户
	 * @return 
	 */
	public static void password(User u) {
		u.setPassword(password(u.getPassword(),u.getSalt()));
	}
	
	/**
	 * 密码加密
	 * @param password String 密码
	 * @param salt String 盐值
	 * @return String
	 */
	public static String password(String password, String salt) {
		return HashKit.md5(password+"_"+salt);
	}
			
	/**
	 * 生成6位密码盐值
	 * @return
	 */
	public static String salt() {
		String source="0123456789abcdefghijklmnopqrstuvwxyz";
		String salt = "";
		Random r = new Random();
		for(int i=0;i<6;i++) {
			salt += source.charAt(r.nextInt(36));
		}
		return salt;
	}
	
	/**
	 * 判断用户是否有访问权限
	 * @param access String 请求的URL
	 * @return
	 */
	public static boolean checkAccess(String access) {
		return checkAccess(access, user());
	}
	
	/**
	 * 判断用户是否有访问权限
	 * @param access String 请求的URL
	 * @param user User 用户
	 * @return
	 */
	public static boolean checkAccess(String access, User user) {
		if(user == null) return false; //用户必须登录
		if(isSuper(user)) return true; //超级管理员拥有全部权限
		
		if(checkAction) {
			Set<Integer> rr = actions.get(access);
			if(rr == null) return true;
			
			List<Role> rs = user.getRoles();
			for(Role r:rs) {
				if(rr.contains(r.getId())) return true;
			}
		}else{
			return true;
		}
		return false;
	}
	
	/**
	 * 登录页面地址
	 * @return
	 */
	public static String loginUrl() {
		return PropKit.get("security.login");
	}
	
	/**
	 * 主页页面，建议如果没有前台为/admin，有前台则为前台/或个人中心/space
	 * @return
	 */
	public static String homeUrl() {
		return PropKit.get("security.home");
	}
	
	/**
	 * 获取当前用户信息，配合BackendInterceptor
	 * @return UserInfo
	 */
	public static User user() {
		return users.get();
	}
	
	/**
	 * 获取当前用户信息，配合BackendInterceptor
	 * @return UserInfo
	 */
	public static int userId() {
		User u = users.get();
		return u == null ? 0 : u.getId();
	}
	
	/**
	 * 设置能当用户信息，在BackendInterceptor调用
	 * @param user UserInfo
	 */
	public static void setUser(User user) {
		users.set(user);
	}
	
	/**
	 * 清空当前线程的User
	 * 
	 */
	public static void releaseUser() {
		users.remove();
	}
	
	public static boolean isSuper() {
		return isSuper(users.get());
	}
	public static boolean isSuper(User user) {
		return user != null && PropKit.get("security.super", "admin").equalsIgnoreCase(user.getAccount());
	}
	
	/**
	 * 初始化接口
	 * @param user
	 * @return
	 */
	public static InitInfo init(User user) {
		InitInfo init = new InitInfo();		
		init.setClearInfo(clearInfo());		
		init.setHomeInfo(homeInfo());		
		init.setLogoInfo(logoInfo());
		init.setMenuInfo(menuInfo(user));		
		return init;
	}
	
	public static ClearInfo clearInfo() {
		ClearInfo clear = new ClearInfo();
		clear.setClearUrl(PropKit.get("tpl.context")+"admin/clear");
		return clear;
	}
	
	public static LogoInfo logoInfo() {
		LogoInfo logo = new LogoInfo();
		logo.setImage(PropKit.get("tpl.statics")+"images/logo.png");
		logo.setTitle("ADMIN");
		return logo;
	}
	
	public static HomeInfo homeInfo() {
		HomeInfo home = new HomeInfo();
		home.setHref(PropKit.get("tpl.context")+"admin/welcome");
		home.setIcon("fa fa-home");
		home.setTitle("首页");
		return home;
	}
	
	public static Map<String,MenuInfo> menuInfo(User user) {
		Map<String,MenuInfo> menuInfo = new HashMap<>();
		
		ApplicationService applicationService = Aop.get(ApplicationService.class);
		List<Application> apps = null;
		if(isSuper(user)) {
			Kv cond = Kv.by("type = ", "i");
			cond.put("status = ", "1");
			apps = applicationService.all(cond, "list_sort ASC");
		}else {
			apps = applicationService.getUserApps(user.getId());
		}
		
		for(Application app:apps) {
			MenuInfo m = new MenuInfo();
			m.setTitle(app.getTitle());
			m.setIcon("fa fa-cubes"+app.getIcon("cubes"));
			m.setChild(menus(user, app));
			menuInfo.put("app_"+app.getListSort(), m);
		}
		return menuInfo;
	}
	
	public static List<MenuInfo> menus(User user,Application app){
		MenuService menuService = Aop.get(MenuService.class);
		List<Menu> list = null;
		if(isSuper(user)) {
			Kv cond = Kv.by("app_id = ", app.getId());
			cond.put("status = ", "1");
			list = menuService.all(cond, "list_sort ASC");
		}else {
			list = menuService.getUserAppMenus(user.getId(), app.getId());
		}
		if(list == null || list.size() == 0) {
			return null;
		}
		return menuTree(list, 0l);
	}
	
	private static List<MenuInfo> menuTree(List<Menu> list, long pid){
		List<MenuInfo> menus = new ArrayList<>();
		for(Menu m:list) {
			if(m.getPid().longValue() != pid) continue;
			
			MenuInfo i = new MenuInfo();
			i.setTitle(m.getTitle());
			i.setIcon("fa fa-"+m.getIcon("cube"));
			i.setHref(PropKit.get("tpl.context")+m.getUrl());
			i.setChild(menuTree(list, m.getId()));
			i.setTarget("_self");
			menus.add(i);
		}
		return menus.size() > 0 ? menus : null;
	}
}
