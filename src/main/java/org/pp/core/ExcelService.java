package org.pp.core;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.pp.modules.sys.service.FileService;
import org.pp.utils.ExcelUtil;

public interface ExcelService {

	public String getError();
	public void setError(String error);
	
	/**
	 * 导入数据
	 * @param excelFile String excel文件路径
	 * @param error String 错误处理方式：1=退出，2=忽略
	 * @param repeat String 重复数据处理方式：1=报错，2=忽略，3=更新
	 * @return boolean
	 */
	default boolean imports(String excelFile, String error, String repeat) {
		String savePath = FileService.basePath()+excelFile;
		File f = new File(savePath);
		if(!f.exists()) {
			setError("上传的文件不存在");
			return false;
		}
		List<String[]> list = ExcelUtil.read(f, skip());
		if(list == null || list.size() == 0) {
			setError("没有解析到有效数据");
			return false;
		}
		
		for(int i=0;i<list.size();i++) {
			String[] line = list.get(i);
			if(!importOne(line, repeat) && "1".equalsIgnoreCase(error)) {
				setError("第"+(i+1)+"行数据发生错误:"+getError());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 导出数据
	 * @param os OutputStream 输出流
	 * @param list List 数据列表
	 * @return boolean
	 */
	default boolean exports(OutputStream os, List list) {
		List<String[]> lines = new ArrayList<>();
		for(Object row:list) {
			String[] line = exportOne(row);
			lines.add(line);
		}
		try {
			ExcelUtil.write(os, title(), header(), lines);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 导出文件名称
	 * @return String
	 */
	default String fileName() {
		return title()+"_" +FileService.uploadName()+".xlsx";
	}
	

	/**
	 * 导入时，跳过的模板标题行数
	 * @return int
	 */
	default int skip() {
		return 1;
	}
	
	/**
	 * 导入一行数据的业务逻辑
	 * @param line String[] 单条数据
	 * @param repeat String 重复数据处理方式：1=报错，2=忽略，3=更新
	 * @return boolean
	 */
	public boolean importOne(String[] line, String repeat);
	
	/**
	 * 导出一行数据的业务逻辑，主要是当前对象转换成字符串数组
	 * @param m Object 数据对象
	 * @return String[]
	 */
	public String[] exportOne(Object m);
	
	/**
	 * 导出文件的名称
	 * @return String
	 */
	public String title();
	
	/**
	 * 导出的表头
	 * @return String[]
	 */
	public String[] header();
	
}
