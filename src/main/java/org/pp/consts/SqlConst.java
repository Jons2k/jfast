package org.pp.consts;

public class SqlConst {
	/**
	 * 默认查询条件
	 */
	public static String TPL_WHERE = " #for(x : cond) #(for.first ? \"WHERE\": \"AND\") #(x.key) #para(x.value)  #end";
	
	/**
	 * 默认排序条件
	 */
	public static String TPL_ORDER = " ORDER BY #if(order != null) #(order) #else id DESC #end";
	
	/**
	 * 默认返回条数限制
	 */
	public static String TPL_LIMIT = " LIMIT #(limit)";
	
	/**
	 * 分页
	 */
	public static String TPL_PAGE = " LIMIT #(offset),#(limit)";
}
