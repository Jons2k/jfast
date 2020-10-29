package org.pp.modules.sys.vo;

import java.util.List;

public class MenuInfo {
	private String title;
	private String icon;
	private String href;
	private String target;
	private List<MenuInfo> child;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public List<MenuInfo> getChild() {
		return child;
	}
	public void setChild(List<MenuInfo> child) {
		this.child = child;
	}
}
