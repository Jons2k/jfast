package org.pp.job;

import java.io.File;

import org.pp.modules.sys.service.FileService;
import org.pp.utils.FileUtil;

/**
 * 定时删除上传的Excel文件
 * @author motiandashao
 *
 */
public class Excel implements Runnable{
	private int expire = 30;  //删除多少天之前的excel文件
	
	@Override
	public void run() {
		
		String basePath = FileService.basePath(FileService.basePath());
		String path = FileService.uploadPath();
		File f = new File(basePath + path);
		
		if(!f.exists()) return;
		
		long et = System.currentTimeMillis() - expire * 24*3600*1000;
		
		for(File ff:f.listFiles()) {
			String ext = FileUtil.getExt(ff.getName());
			if(!"xls".equalsIgnoreCase(ext) && !"xlsx".equalsIgnoreCase(ext)) {
				continue;
			}
			if(ff.lastModified() > et) {
				continue;
			}
			if(null == FileService.dao.findFirst("SELECT * FROM sys_file WHERE path=?", path + ff.getName())) {
				ff.delete();
			}
		}
	}	
	
}
