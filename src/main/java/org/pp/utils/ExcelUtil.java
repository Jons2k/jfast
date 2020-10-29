package org.pp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel读写工具
 * 
 * @author Administrator
 *
 */
public class ExcelUtil {

	/**
	 * 读取指定的Excel文件
	 * @param file String 文件路径
	 * @param skip int 路过的行数
	 * @return List<String[]>
	 */
	public static List<String[]> read(String file, int skip) {
		File f = new File(file);
		if (!f.exists()) {
			return null;
		}
		return read(f, skip);
	}
	
	/**
	 * 读取指定的Excel文件
	 * @param file File 文件对象
	 * @param skip int 路过的行数
	 * @return List<String[]>
	 */
	public static List<String[]> read(File file, int skip) {
		Workbook book = open(file);
		if (book == null)
			return null;

		Sheet sheet = book.getSheetAt(0);
		if (sheet == null || sheet.getPhysicalNumberOfRows() <= skip)
			return null;

		int rowCount = sheet.getPhysicalNumberOfRows();
		int colCount = sheet.getRow(skip).getPhysicalNumberOfCells();
		List<String[]> list = new ArrayList<>();
		while (skip < rowCount) {
			Row row = sheet.getRow(skip);
			if("".equals(row.getCell(0).getStringCellValue().trim())) {
				break;
			}
			String[] line = new String[colCount];
			for (int i = 0; i < colCount; i++) {
				line[i] = getVal(row.getCell(i)).trim();
			}
			list.add(line);
			skip++;
		}
		return list;
	}

	
	
	/**
	 * 导出Excel到输出流,XLSX格式
	 * 
	 * @param out    OutputStream 输出流
	 * @param title String 内容名称
	 * @param headers String[] 标题
	 * @param lines  List<String[]> 数据
	 * @throws IOException
	 */
	public static void write(OutputStream out, String title, String[] headers, List<String[]> lines) throws IOException {
		Workbook book = new XSSFWorkbook();
		Sheet sheet = book.createSheet(title);
		Row row = sheet.createRow(0);
		Cell cell = null;
		for (int i = 0; i < headers.length; i++) {
			cell = row.createCell(i);

			CellStyle headCellStyle = book.createCellStyle();// 创建单元格样式对象
			Font font = book.createFont();
			font.setBold(true);// 是否加粗
			font.setFontHeightInPoints((short) 14);// 字体大小
			headCellStyle.setFont(font);
			headCellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
			headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
			cell.setCellStyle(headCellStyle);// 把设置好的样式对象给单元格
			cell.setCellValue(headers[i]);
		}
		if(lines != null && lines.size() > 0) {
			for (int r = 0; r < lines.size(); r++) {
				row = sheet.createRow(r + 1);
				String[] line = lines.get(r);
				for (int c = 0; c < line.length; c++) {
					row.createCell(c).setCellValue(line[c]);
				}
			}
		}
		book.write(out);
		book.close();
	}
	
	public static String getVal(Cell cell) {
		DecimalFormat df = new DecimalFormat("0"); // 格式化为整数
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 日期格式化
		if (cell == null) {
			return "";
		}
		
		String cellValue = null;
		switch (cell.getCellType()) {
			case NUMERIC: {
				cellValue = String.valueOf((int) cell.getNumericCellValue());
				break;
			}
			case FORMULA: {
				String dataFormat = cell.getCellStyle().getDataFormatString(); // 单元格格式
				boolean isDate = isCellDateFormatted(cell);
				if ("General".equals(dataFormat)) {
					cellValue = df.format(cell.getNumericCellValue());
				} else if (isDate) {
					cellValue = sdf.format(cell.getDateCellValue());
				} else {
					cellValue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			case STRING: {
				cellValue = cell.getRichStringCellValue().getString();
				break;
			}
			case BLANK: {
				cellValue = "";
				break;
			}
			default:
				cellValue = "";
		}
		return cellValue;
	}

	private static Workbook open(File excelFile) {
		Workbook workbook = null;
		String fileName = excelFile.getName();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		InputStream in = null;
		try {
			in = new FileInputStream(excelFile);
			if ("xls".equals(suffix)) {
				workbook = new HSSFWorkbook(in);
			} else if ("xlsx".equals(suffix)) {
				workbook = new XSSFWorkbook(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}

	public static boolean isCellDateFormatted(Cell cell) {
		if (cell == null)
			return false;
		boolean bDate = false;

		double d = cell.getNumericCellValue();
		if (isValidExcelDate(d)) {
			CellStyle style = cell.getCellStyle();
			if (style == null)
				return false;
			int i = style.getDataFormat();
			String f = style.getDataFormatString();
			bDate = isADateFormat(i, f);
		}
		return bDate;
	}

	public static boolean isADateFormat(int formatIndex, String formatString) {
		if (isInternalDateFormat(formatIndex)) {
			return true;
		}

		if ((formatString == null) || (formatString.length() == 0)) {
			return false;
		}

		String fs = formatString;
		// 下面这一行是自己手动添加的 以支持汉字格式wingzing
		fs = fs.replaceAll("[\"|\']", "").replaceAll("[年|月|日|时|分|秒|毫秒|微秒]", "");
		fs = fs.replaceAll("\\\\-", "-");
		fs = fs.replaceAll("\\\\,", ",");
		fs = fs.replaceAll("\\\\.", ".");
		fs = fs.replaceAll("\\\\ ", " ");
		fs = fs.replaceAll(";@", "");
		fs = fs.replaceAll("^\\[\\$\\-.*?\\]", "");
		fs = fs.replaceAll("^\\[[a-zA-Z]+\\]", "");
		return (fs.matches("^[yYmMdDhHsS\\-/,. :]+[ampAMP/]*$"));
	}

	public static boolean isInternalDateFormat(int format) {
		switch (format) {
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
		case 22:
		case 45:
		case 46:
		case 47:
			return true;
		case 23:
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:
		case 29:
		case 30:
		case 31:
		case 32:
		case 33:
		case 34:
		case 35:
		case 36:
		case 37:
		case 38:
		case 39:
		case 40:
		case 41:
		case 42:
		case 43:
		case 44:
		}
		return false;
	}

	public static boolean isValidExcelDate(double value) {
		return (value > -4.940656458412465E-324D);
	}
}
