package org.pp.modules.sys.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.pp.core.BaseService;
import org.pp.modules.sys.model.File;
import org.pp.modules.sys.model.User;
import org.pp.utils.FileUtil;
import org.pp.utils.SecurityUtil;

import com.jfinal.kit.Kv;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;

public class FileService implements BaseService<File>{
	
	public static File dao = new File().dao();
	private String error = "";
	
	
	@Override
	public String getError() {
		return error;
	}

	@Override
	public void setError(String error) {
		this.error = error;
	}
	@Override
	public File getModel() {
		return dao;
	}
	
	/**
	 * 转移文件到正式目录
	 * @param upload UploadFile 上传的文件信息
	 * @param save boolean 是否保存入库
	 * @return File对象
	 */
	public File rename(UploadFile upload, boolean save) {
		String uploadBase = basePath();
		String md5 = FileUtil.md5(upload.getFile());
		Kv cond = Kv.by("md5 = ", md5);
		cond.put("status = ", "1");
		File f = findFirst(cond);
		if(f != null) {
			if(FileUtil.exists(uploadBase+f.getPath())) {
				return f;
			}else {
				f.setStatus("0");
				f.update();
			}
		}		
		
		User u = SecurityUtil.user();
		f = new File();
		f.setSize(upload.getFile().length());
		f.setName(upload.getFileName());
		f.setMd5(md5);
		f.setStatus("1");
		f.setExts(FileUtil.getExt(upload.getFileName()));
		String saveName = u.getId()+"_"+uploadName()+"."+f.getExts();
		String path = uploadPath();
		String savePath = basePath(uploadBase)+path;
		FileUtil.mkdirs(savePath);
		if(!upload.getFile().renameTo(new java.io.File(savePath + saveName))) {
			error = "保存文件失败";
			return null;
		}
		f.setPath(path+ saveName);
		f.setUploadTime(new Date());
		f.setUploadUserId(u.getId());
		if(save && !f.save()) {
			error = "保存文件信息失败";
			return null;
		}
		
		return f;
	}
	
	/**
	 * 获取文件的绝对路径
	 * @param fileId long 文件ID
	 * @return String
	 */
	public String fullPath(long fileId) {
		File f = findCache(fileId);
		if(f == null) return null;
		
		return basePath()+f.getPath();
	}
	
	/**
	 * 生成上传目录的绝对路径
	 * @param basePath String 指定参数
	 * @return String
	 */
	public static String basePath() {
		return basePath(PropKit.get("upload.base", "./upload/"));
	}
	
	/**
	 * 生成上传目录的绝对路径
	 * @param basePath String 指定参数
	 * @return String
	 */
	public static String basePath(String basePath) {
		if(basePath.startsWith("/")) return basePath; //linux

		if(basePath.indexOf(":") > 0) return basePath; //windows
		
		return PathKit.getWebRootPath() + "/" + basePath; //auto
	}
	
	/**
	 * 判断上传的文件是否存在 
	 * @param savePath String 保存路径
	 * @return boolean
	 */
	public static boolean uploadFileExist(String savePath) {
		savePath = basePath()+savePath;
		return FileUtil.exists(savePath);
	}
	
	/**
	 * 生成上传路径
	 * @return String
	 */
	public static String uploadPath() {
		return new SimpleDateFormat("yyyy/MM/").format(new Date());
	}
	
	/**
	 * 生成上传路径
	 * @return String
	 */
	public static String uploadName() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
}
