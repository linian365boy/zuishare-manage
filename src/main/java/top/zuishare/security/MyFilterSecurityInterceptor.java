package top.zuishare.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

@Service
public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
	@Autowired
	private CustomSecurityMetadataSource securityMetadataSource;
	@Autowired
	private CustomAccessDecisionManager accessDecisionManager;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostConstruct
	public void initPre(){
		super.setAuthenticationManager(authenticationManager);
		super.setAccessDecisionManager(accessDecisionManager); 
	}
	
	@Override 
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		invoke(fi);
	}

	private void invoke(FilterInvocation fi) throws IOException,
            ServletException {
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}

	@Override
	public Class<? extends Object> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	
}
