package org.pp.core;

public class RestObject {
	private String code;
	private String msg;
	private String url;
	private Object data;
	private int count;
	
	
	
	public static RestObject error(String msg) {
		RestObject ro = new RestObject();
		ro.code = "1";
		ro.msg = msg;
		return ro;
	}
	
	public static RestObject error(String code, String url) {
		RestObject ro = new RestObject();
		ro.code = code;
		ro.msg = "操作失败";
		ro.url = url;
		return ro;
	}
	public static RestObject error(String code, String msg, String url) {
		RestObject ro = new RestObject();
		ro.code = code;
		ro.msg = msg;
		ro.url = url;
		return ro;
	}
	
	public static RestObject success(String msg) {
		RestObject ro = new RestObject();
		ro.code = "0";
		ro.msg = msg;
		return ro;
	}
	public static RestObject success(Object data) {
		RestObject ro = new RestObject();
		ro.code = "0";
		ro.msg = "操作成功";
		ro.data = data;
		return ro;
	}
	public static RestObject success(int count,Object data) {
		RestObject ro = new RestObject();
		ro.code = "0";
		ro.msg = "操作成功";
		ro.count = count;
		ro.data = data;
		return ro;
	}
	public static RestObject success(String msg, String url) {
		RestObject ro = new RestObject();
		ro.code = "0";
		ro.msg = msg;
		ro.url = url;
		return ro;
	}
	public static RestObject success(String msg, Object data) {
		RestObject ro = new RestObject();
		ro.code = "0";
		ro.msg = "操作成功";
		ro.data = data;
		return ro;
	}
	public static RestObject success(String msg, Object data, String url) {
		RestObject ro = new RestObject();
		ro.code = "0";
		ro.msg = "操作成功";
		ro.data = data;
		ro.url = url;
		return ro;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}