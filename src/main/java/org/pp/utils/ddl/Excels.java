package org.pp.utils.ddl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.pp.modules.dev.model.Field;
import org.pp.modules.dev.model.Models;
import org.pp.utils.ExcelUtil;

public class Excels {
	private int line = 0;
	
	/**
	 * 解析一个Excel文件
	 * @param filePath String 文件路径
	 * @Param s int Sheet编号，从0开始
	 * @return
	 */
	public List<Models> readModels(String filePath, int s){
		File f = new File(filePath);
		if(!f.exists()) return null;		
		
		List<Models> list = new ArrayList<>();
		try {
			HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(f));
			if(book.getNumberOfSheets() < 1) {
				book.close();
				return null;
			}
			HSSFSheet sheet = book.getSheetAt(s);
			int rowCount = sheet.getLastRowNum();
			for(line = 0; line < rowCount;line++){				
				Models t = readModel(sheet);
				if(t != null && t.getFields().size() > 0) list.add(t);
			}			
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	/**
	 * 解析一个Excel文件
	 * @param filePath String 文件路径
	 * @Param sheetIndex int Sheet编号，从0开始
	 * @Param skip int 行起始编号，从0开始
	 * @return List<String[]>
	 */
	public List<String[]> readAll(String filePath, int sheetIndex, int skip){
		File f = new File(filePath);
		if(!f.exists()) return null;		
		
		List<String[]> list = new ArrayList<>();
		try {
			HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(f));
			if(book.getNumberOfSheets() < 1) {
				book.close();
				return null;
			}
			HSSFSheet sheet = book.getSheetAt(sheetIndex);
			int rowCount = sheet.getLastRowNum();
			for(; skip <= rowCount;skip++){	
				HSSFRow row = sheet.getRow(skip);
				int cellCount = row.getLastCellNum();
				String[] rs = new String[cellCount];
				for(int c=0;c<cellCount;c++){
					rs[c] = ExcelUtil.getVal(row.getCell(c));
				}
				list.add(rs);
			}			
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	public Models readModel(HSSFSheet sheet) {
		HSSFRow row = sheet.getRow(line);
		if(row == null) return null;
		
		if(row.getCell(0) == null) return null;
		
		String table =  ExcelUtil.getVal(row.getCell(0));
		Models t = new Models();
		t.setType("c");
		t.setStatus("1");
		t.setTables(Excels.humpToLine(table.trim()));
		t.setTitle(ExcelUtil.getVal(row.getCell(3)).trim());
		t.setFields(new ArrayList<Field>());
		//忽略一行标题
		line ++;
		for(int i=0;i<100;i++) {
			line ++;
			row = sheet.getRow(line);
			if(row == null) break;
			
			String code =  ExcelUtil.getVal(row.getCell(1));
			if(isEmpty(code)) break;
			
			Field f = new Field();
			f.setListSort(Integer.valueOf(ExcelUtil.getVal(row.getCell(0))));
			f.setCode(Excels.humpToLine(code.trim()));
			if("pid".equalsIgnoreCase(f.getCode())) {
				t.setType("t");
			}
			f.setDbType(ExcelUtil.getVal(row.getCell(2)).trim());
			parseFieldType(f);
			String length = ExcelUtil.getVal(row.getCell(3));
			if(isNotEmpty(length)) {
				String[] ls = length.split(",");
				f.setLen(parseInt(ls[0]));
				if(ls.length>1)f.setScale(parseInt(ls[1]));
				else f.setScale(0);
			}else {
				f.setLen(0);
				f.setScale(0);
			}
			f.setNullable("Y".equals(ExcelUtil.getVal(row.getCell(4))) ? "1" : "0");
			f.setDefaults(ExcelUtil.getVal(row.getCell(5)));
			f.setTitle(ExcelUtil.getVal(row.getCell(6)));
			String attr = ExcelUtil.getVal(row.getCell(7));
			f.setParam(attr);
			
			t.getFields().add(f);
		}
		return t;
	}		
	
	private void parseFieldType(Field f) {
		switch(f.getDbType().toLowerCase()) {
			case "varhcar2":
			case "varhcar":
			case "char":
				if(f.getParam() != null) {
					f.setType("dict");
				}else {
					f.setType("string");
				}
				f.setDbType("varchar");
				break;
			case "number":
			case "int":
			case "long":
			case "float":
			case "double":
			case "decimal":
				f.setType("number");
				f.setDbType("number");
				break;
			case "timestamp":
				f.setType("datetime");
				f.setDbType("datetime");
			default:
				f.setType(f.getDbType());
				break;
		}
		if(f.getCode().indexOf("file") > 0 || f.getCode().indexOf("image") > 0) {
			f.setType("file");
		}
	}
	
	/**
	 * 解以.0结束的整数
	 * @param intVal
	 * @return
	 */
	public int parseInt(String intVal){
		if(intVal.endsWith(".0")) intVal = intVal.substring(0, intVal.length()-2);
		return Integer.parseInt(intVal);
	}
	
	public String trimNumber(String strVal){
		if(strVal.endsWith(".0")) return strVal.substring(0, strVal.length()-2);
		return strVal;
	}
	
	public static boolean isEmpty(String val) {
		return val == null || "".equals(val.trim());
	}
	
	public static boolean isNotEmpty(String val) {
		return val != null && !"".equals(val.trim());
	}
	
	public static String lineToHump(String str) {
		str = str.toLowerCase();
		Pattern linePattern = Pattern.compile("_(\\w)");
		Matcher matcher = linePattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	public static String humpToLine(String str) {
		return str.replaceAll("[A-Z]", "_$0").toLowerCase();
	}
}
