package org.pp.utils.db;

public class DbFactory {
	public static String[] TYPES = {"mysql", "oracle", "sqlserver"};
	public static Adapter create(String dbType) {
		Adapter o;
		if("oracle".equalsIgnoreCase(dbType)) {
			o = new Oracle();
		}else if("sqlserver".equalsIgnoreCase(dbType)) {
			o = new SqlServer();
		}else {
			o = new MySQL();
		}
		return o;
	}
}
