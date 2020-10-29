package org.pp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String DATE_FORMAT = "yyyy-MM-dd";
	public static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static String date(Date d) {
		return new SimpleDateFormat(DATE_FORMAT).format(d);
	}
	public static String date(Date d, String format) {
		return new SimpleDateFormat(format).format(d);
	}
	public static String datetime(Date d) {
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}
	
	public static Date from(String ds) {
		try {
			return new SimpleDateFormat(DATE_FORMAT).parse(ds);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Date from(String ds, String format) {
		try {
			return new SimpleDateFormat(format).parse(ds);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
