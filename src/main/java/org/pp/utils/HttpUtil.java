package org.pp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {
	// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
	// 字符串在编译时会被转码一次,所以是 "\\b"
	// \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
	private static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i" + "|windows (phone|ce)|blackberry"
			+ "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp" + "|laystation portable)|nokia|fennec|htc[-_]"
			+ "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	private static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

	// 移动设备正则匹配：手机端、平板
	private static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
	private static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

	/**
	 * 检测是否是移动设备访问
	 * 
	 * @Title: isMobile
	 * @param request HttpServletRequest 当前请求
	 * @return true:移动设备接入，false:pc端接入
	 */
	public static boolean isMobile(HttpServletRequest request) {
		String userAgent = request.getHeader("USER-AGENT").toLowerCase();
		if (null == userAgent) {
			userAgent = "";
		}
		// 匹配
		Matcher matcherPhone = phonePat.matcher(userAgent);
		Matcher matcherTable = tablePat.matcher(userAgent);
		if (matcherPhone.find() || matcherTable.find()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMethod(HttpServletRequest request, String method) {
		return request.getMethod().equalsIgnoreCase(method);
	}

	public static boolean isGet(HttpServletRequest request) {
		return "get".equalsIgnoreCase(request.getMethod());
	}

	public static boolean isPost(HttpServletRequest request) {
		return "post".equalsIgnoreCase(request.getMethod());
	}

	/***
	 * 判断一个请求是否为AJAX请求,是则返回true
	 * 
	 * @param request HttpServletRequest
	 * @return
	 */
	public static boolean isAjax(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		if (requestType == null) {
			return false;
		}
		return true;
	}
}
