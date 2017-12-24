package top.zuishare.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public synchronized static String getFileExtName(String filename){
		int p = filename.lastIndexOf(".");
		return filename.substring(p+1);
	}
	
	public static boolean delFile(String fpath){
		boolean flag = false;
		File file = new File(fpath);
		if(!file.exists()){
			logger.warn("delete file => {} is {}", fpath, flag);
			return flag;
		}
		if(file.isDirectory()){
			logger.warn("delete file => {} is {}", fpath, flag);
			return flag;
		}
		if(file.delete()){
			flag = true;
			logger.warn("delete file => {} is {}", fpath, flag);
			return flag;
		}
		logger.warn("delete file => {} is {}", fpath, flag);
		return flag;
	}
}
