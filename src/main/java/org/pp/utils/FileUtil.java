package org.pp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletResponse;

public class FileUtil {

	public static String getExt(String fileName) {
		String[] names = fileName.split("\\.");
		return names[names.length - 1];
	}

	/**
	 * 创建目录
	 * 
	 * @param path String 目录全名
	 * @return boolean
	 */
	public static boolean mkdirs(String path) {
		File f = new File(path);
		if (f.exists()) {
			return true;
		}
		return f.mkdirs();
	}
	
	/**
	 * 删除文件或目录 
	 * 
	 * @param path String 全名
	 * @return boolean
	 */
	public static boolean delete(String path) {
		File f = new File(path);
		if(!f.exists()) return true;
		if(f.isFile()) {
			return f.delete();
		}else {
			for(File ff:f.listFiles()) {
				delete(f.getAbsolutePath());
			}
		}
		return true;
	}

	/**
	 * 判断目录是否存在
	 * 
	 * @param path String 目录全名
	 * @return boolean
	 */
	public static boolean exists(String path) {
		File f = new File(path);
		return f.exists();
	}

	/**
	 * 判断目录是否存在
	 * 
	 * @param path String 目录全名
	 * @return boolean
	 */
	public static boolean move(String source, String dest) {
		File f = new File(source);
		if (f.exists()) {
			return f.renameTo(new File(dest));
		}
		return false;
	}

	/**
	 * 快速写入文件
	 * 
	 * @param file    String 文件路径
	 * @param content String 内容
	 * @return boolean
	 */
	public static boolean write(String file, String content) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(content.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 复制文件
	 * @param source String 源文件路径
	 * @param dest String 目标文件路径
	 * @return boolean
	 */
	public static boolean copy(String source, String dest) {
		File sf = new File(source);
		File df = new File(dest);
		try {
			Files.copy(sf.toPath(), df.toPath());
		}catch (Exception e) {
			return false;
		}
		return df.exists();
	}

	/**
	 * 获取文件的Md5值
	 * 
	 * @param file String 文件路径
	 * @return String
	 */
	public static String md5(String file) {
		return md5(new File(file));
	}

	/**
	 * 获取文件的Md5值
	 * 
	 * @param file File 目标文件
	 * @return String
	 */
	public static String md5(File file) {
		int bufferSize = 256 * 1024; // 缓冲区大小（这个可以抽出一个参数）
		FileInputStream fileInputStream = null;
		DigestInputStream digestInputStream = null;
		try {

			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			fileInputStream = new FileInputStream(file);
			digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
			byte[] buffer = new byte[bufferSize];

			while (digestInputStream.read(buffer) > 0)
				;
			messageDigest = digestInputStream.getMessageDigest();
			byte[] resultByteArray = messageDigest.digest();

			return byteArrayToHex(resultByteArray);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				digestInputStream.close();
			} catch (Exception e) {
			}
			try {
				fileInputStream.close();
			} catch (Exception e) {
			}

		}
	}

	public static String byteArrayToHex(byte[] byteArray) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] resultCharArray = new char[byteArray.length * 2];
		int index = 0;
		for (byte b : byteArray) {
			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b & 0xf];
		}
		return new String(resultCharArray);
	}
	
	/**
     * 预览pdf文件工具类
     * @param response
     * @param fileName
     */
    public static void showPdf( HttpServletResponse response, String fileName){
    	try {

            response.setContentType("application/pdf");
            FileInputStream in = new FileInputStream(new File(fileName));
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[1024];
            while ((in.read(b))!=-1) {
                out.write(b);
            }
            out.flush();
            in.close();
            out.close();
    	}catch (Exception e) {
			e.printStackTrace();
		}
     }
}
