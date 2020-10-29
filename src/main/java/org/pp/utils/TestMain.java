package org.pp.utils;

import java.util.regex.Pattern;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String reg = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
		
		String str = "2020-04-24 00:00:00";
		Pattern p = Pattern.compile(reg);
		if(p.matcher(str).matches()) {
			System.out.println("match");
		}else {
			System.err.println("no match");
		}
	}

}
