package top.zuishare.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.access.method.DelegatingMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//@Service
public class ResourceDetailsMonitor implements InitializingBean {
	private static Logger logger = LoggerFactory.getLogger(ResourceDetailsMonitor.class);
	@Autowired
	private FilterSecurityInterceptor filterSecurityInterceptor;
	@Autowired
	private AccessDecisionManager accessDecisionManager;
	@Autowired
	private DelegatingMethodSecurityMetadataSource delegatingMethodDefinitionSource;
	@Autowired
	private ResourceDetailsBuilder resourceDetailsBuilder;
	@Autowired
	private MethodSecurityInterceptor methodSecurityInterceptor;
	private Collection<ConfigAttribute> hasMethodAttribute;
	private Collection<ConfigAttribute> hasUrlAttribute;

	public void setFilterSecurityInterceptor(FilterSecurityInterceptor filterSecurityInterceptor) {
		this.filterSecurityInterceptor = filterSecurityInterceptor;
	}

	public void setDelegatingMethodDefinitionSource(
			DelegatingMethodSecurityMetadataSource delegatingMethodDefinitionSource) {
		this.delegatingMethodDefinitionSource = delegatingMethodDefinitionSource;
	}

	// spring 加载该bean后调用
	public void afterPropertiesSet() throws Exception {
		logger.info("-------- ResourceDetailsMonitor afterPropertiesSet start --------");
		// 修改默认的accessDecisionManager
		filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager);
		// 保存已经有的方法资源配置
		this.hasMethodAttribute = delegatingMethodDefinitionSource.getAllConfigAttributes();
		// 保存所有的url资源配置
		this.hasUrlAttribute = filterSecurityInterceptor.getSecurityMetadataSource().getAllConfigAttributes();
		// 从数据库读取其他资源
		refresh();
	}

	public void refresh() {
		if (filterSecurityInterceptor != null) {
			FilterInvocationSecurityMetadataSource source = resourceDetailsBuilder.createUrlSource();
			source.getAllConfigAttributes().addAll(hasUrlAttribute);
			filterSecurityInterceptor.setSecurityMetadataSource(source);
		}
		if (delegatingMethodDefinitionSource != null) {
			MethodSecurityMetadataSource source = resourceDetailsBuilder.createMethodSource();
			delegatingMethodDefinitionSource.getMethodSecurityMetadataSources().clear();
			delegatingMethodDefinitionSource.getMethodSecurityMetadataSources().add(source);
			List<MethodSecurityMetadataSource> list = new ArrayList<MethodSecurityMetadataSource>();
			source.getAllConfigAttributes().addAll(hasMethodAttribute);
			list.add(source);
			// 为何要new一个，而不是在原有基础上添加，
			// 因为DelegatingMethodSecurityMetadataSource存在缓存
			DelegatingMethodSecurityMetadataSource delegatingMethodDefinitionSource = new DelegatingMethodSecurityMetadataSource(list);
			methodSecurityInterceptor.setSecurityMetadataSource(delegatingMethodDefinitionSource);
		}
	}

	public ResourceDetailsBuilder getResourceDetailsBuilder() {
		return resourceDetailsBuilder;
	}

	public void setResourceDetailsBuilder(ResourceDetailsBuilder resourceDetailsBuilder) {
		this.resourceDetailsBuilder = resourceDetailsBuilder;
	}

	public MethodSecurityInterceptor getMethodSecurityInterceptor() {
		return methodSecurityInterceptor;
	}

	public void setMethodSecurityInterceptor(MethodSecurityInterceptor methodSecurityInterceptor) {
		this.methodSecurityInterceptor = methodSecurityInterceptor;
	}

	public Collection<? extends ConfigAttribute> getHasMethodAttribute() {
		return hasMethodAttribute;
	}

	public void setHasMethodAttribute(Collection<ConfigAttribute> hasMethodAttribute) {
		this.hasMethodAttribute = hasMethodAttribute;
	}

	public Collection<? extends ConfigAttribute> getHasUrlAttribute() {
		return hasUrlAttribute;
	}

	public void setHasUrlAttribute(Collection<ConfigAttribute> hasUrlAttribute) {
		this.hasUrlAttribute = hasUrlAttribute;
	}

	public FilterSecurityInterceptor getFilterSecurityInterceptor() {
		return filterSecurityInterceptor;
	}

	public DelegatingMethodSecurityMetadataSource getDelegatingMethodDefinitionSource() {
		return delegatingMethodDefinitionSource;
	}

	public AccessDecisionManager getAccessDecisionManager() {
		return accessDecisionManager;
	}

	public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
		this.accessDecisionManager = accessDecisionManager;
	}
}
