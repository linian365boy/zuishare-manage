package top.zuishare.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNameExistException extends AuthenticationException{
	/** 
	 * @since JDK 1.7 
	 */ 
	private static final long serialVersionUID = 7000938891871272398L;

	public UserNameExistException(String msg) {
		super(msg);
	}
	
	public UserNameExistException(String msg, Throwable t) {
		super(msg,t);
	}
	
}
