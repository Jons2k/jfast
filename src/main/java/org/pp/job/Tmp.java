package org.pp.job;

import java.io.File;

import org.pp.modules.sys.service.FileService;

/**
 * 定时删除上传的临时文件
 * @author motiandashao
 *
 */
public class Tmp implements Runnable{
	private int expire = 3;  //删除多少天之前的临时文件
	
	@Override
	public void run() {
		
		String basePath = FileService.basePath(FileService.basePath());
		File f = new File(basePath + "tmp");
		
		if(!f.exists()) return;
		
		long et = System.currentTimeMillis() - expire * 24*3600*1000;
		
		for(File ff:f.listFiles()) {
			if(ff.lastModified() > et) {
				continue;
			}
			ff.delete();
		}
	}	
	
}
