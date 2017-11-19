package top.zuishare.exceptions;

import org.springframework.security.core.AuthenticationException;

public class CaptchaException extends AuthenticationException {

	/** 
	 * serialVersionUID:序列化
	 * @since JDK 1.7 
	 */ 
	private static final long serialVersionUID = -3940526028793012557L;

	public CaptchaException(String msg) {
		super(msg);
	}
	
}
