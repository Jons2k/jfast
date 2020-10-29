package org.pp.modules.sys.vo;

import java.util.Map;

public class InitInfo {
	private ClearInfo clearInfo;
	private HomeInfo homeInfo;
	private LogoInfo logoInfo;
	private Map<String,MenuInfo> menuInfo;
	public ClearInfo getClearInfo() {
		return clearInfo;
	}
	public void setClearInfo(ClearInfo clearInfo) {
		this.clearInfo = clearInfo;
	}
	public HomeInfo getHomeInfo() {
		return homeInfo;
	}
	public void setHomeInfo(HomeInfo homeInfo) {
		this.homeInfo = homeInfo;
	}
	public Map<String,MenuInfo> getMenuInfo() {
		return menuInfo;
	}
	public void setMenuInfo(Map<String,MenuInfo> menuInfo) {
		this.menuInfo = menuInfo;
	}
	public LogoInfo getLogoInfo() {
		return logoInfo;
	}
	public void setLogoInfo(LogoInfo logoInfo) {
		this.logoInfo = logoInfo;
	}
}
