package top.zuishare.security.custom;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;

import java.util.Calendar;

public class CustomWebSecurityExpressionRoot extends WebSecurityExpressionRoot {
	public CustomWebSecurityExpressionRoot(Authentication a, FilterInvocation fi) {
		super(a, fi);
	}

	public boolean isEvenMinute() {
		return (Calendar.getInstance().get(Calendar.MINUTE) % 2) == 0;
	}
}
