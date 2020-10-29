package org.pp.utils;

import java.util.List;

public class ArrayUtil {
	/**
	 * 判断指定值是否在后边的数组内
	 * @param T val 值
	 * @param T[] arr 数组
	 * @return boolean
	 */
	public static<T> boolean in(T val, T[] arr) {
		for(T v:arr) {
			if(val.toString().equalsIgnoreCase(v.toString())) return true;
		}
		return false;
	}
		
	/**
	 * 列表转换成数据
	 * @param List<String> list 列表
	 * @return
	 */
	public static String[] toArray(List<String> list) {
		if(list == null || list.size() == 0) return null;
		return list.toArray(new String[list.size()]);		
	}
}
