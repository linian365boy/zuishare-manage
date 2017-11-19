package top.zuishare.util;

import java.util.UUID;

/**
 * @ModuleID:
 * @Comments: 生成UUID
 * @Create Date: 2013-5-24
 */
public class UUIDGenerator {
	
	public UUIDGenerator(){}
	/**
	 * @FunName: getUUID
	 * @Description:  生成UUID，把生成的"-"换成了""
	 * @return UUID
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public static String getUUID(){
		String s = UUID.randomUUID().toString();
		return s.replaceAll("-", "");
	}
	
	public static void main(String[] args) {
		System.out.println(UUIDGenerator.getUUID());
	}
	
}
