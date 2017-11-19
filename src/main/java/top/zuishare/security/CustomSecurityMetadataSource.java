package top.zuishare.security;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;
import top.zuishare.dao.ResourceDao;
import top.zuishare.model.Resource;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	private static Logger logger = LoggerFactory.getLogger(CustomSecurityMetadataSource.class);
	@Autowired
	private ResourceDao resourceDao;
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return new ArrayList<ConfigAttribute>();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	/**
	 * @PostConstruct是Java EE 5引入的注解， Spring允许开发者在受管Bean中使用它。当DI容器实例化当前受管Bean时，
	 * @PostConstruct注解的方法会被自动触发，从而完成一些初始化工作，
	 * 加载所有资源与权限的关系
	 */
	@PostConstruct
	private void loadResourceDefine() {
		logger.info("-----------CustomSecurityMetadataSource loadResourceDefine ----------- ");
		if (resourceMap == null) {
			resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
			List<Resource> resources = resourceDao.getAllResource();
			logger.debug("loadResourceDefine get all {} resource from DB", resources!=null ? resources.size() : 0);
			for (Resource resource : resources) {
				Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
				//通过资源名称来表示具体的权限 注意：必须"ROLE_"开头
				ConfigAttribute configAttribute = new SecurityConfig(resource.getRoleName());
				configAttributes.add(configAttribute);
				resourceMap.put(resource.getResString(), configAttributes);
				logger.debug("resource|{} add resource map", resource);
			}
		}
	}

	// 返回所请求资源所需要的权限
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		logger.debug("-----------CustomSecurityMetadataSource getAttributes requestUrl is {} ----------- ", requestUrl);
		if (resourceMap == null) {
			loadResourceDefine();
		}
		if (requestUrl.indexOf("?") > -1) {
			requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
		}
		Collection<ConfigAttribute> configAttributes = resourceMap.get(requestUrl);
		logger.debug("CustomSecurityMetadataSource getAttributes configAttributes | {}",
				ToStringBuilder.reflectionToString(configAttributes, ToStringStyle.SHORT_PREFIX_STYLE));
		return configAttributes;
	}

	public ResourceDao getResourceDao() {
		return resourceDao;
	}

	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}

	public static Map<String, Collection<ConfigAttribute>> getResourceMap() {
		return resourceMap;
	}

	public static void setResourceMap(
			Map<String, Collection<ConfigAttribute>> resourceMap) {
		CustomSecurityMetadataSource.resourceMap = resourceMap;
	}
}
