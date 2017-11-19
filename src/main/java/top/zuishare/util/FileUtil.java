package top.zuishare.util;

import java.io.File;

public class FileUtil {
	public synchronized static String getFileExtName(String filename){
		int p = filename.lastIndexOf(".");
		return filename.substring(p+1);
	}
	
	public static boolean delFile(String fpath){
		boolean flag = false;
		File file = new File(fpath);
		if(!file.exists()){
			return flag;
		}
		if(file.isDirectory()){
			return flag;
		}
		if(file.delete()){
			flag = true;
			return flag;
		}
		return flag;
	}
}
