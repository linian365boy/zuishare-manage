package top.zuishare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.zuishare.dao.ResourceDao;
import top.zuishare.model.Resource;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceDetailsBuilder {
	@Autowired
	private ResourceDao resourceDao;
    
    public FilterInvocationSecurityMetadataSource createUrlSource() {
        return new DefaultFilterInvocationSecurityMetadataSource(this.buildRequestMap());
    }

    public MethodSecurityMetadataSource createMethodSource() {
        return new MapBasedMethodSecurityMetadataSource(this.buildMethodMap());
    }

	protected LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> buildRequestMap() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = null;
        requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
        Map<String, String> resourceMaps = new LinkedHashMap<String,String>();
        Map<String, String> resourceMap = this.getAllResource("URL");
        resourceMaps.putAll(resourceMap);
        
        for (Map.Entry<String, String> entry : resourceMaps.entrySet()) {
        	RequestMatcher key = new AntPathRequestMatcher(entry.getKey());
            List<ConfigAttribute> attributes = 
        			SecurityConfig.createList(StringUtils.commaDelimitedListToStringArray(entry.getValue()));
            requestMap.put(key, attributes);
        }

        return requestMap;
    }

	protected Map<String, List<ConfigAttribute>> buildMethodMap() {
        Map<String, List<ConfigAttribute>> methodMap = null;
        methodMap = new LinkedHashMap<String, List<ConfigAttribute>>();
        Map<String, String> resourceMaps = new LinkedHashMap<String,String>();
        Map<String, String> resourceMap = this.getAllResource("METHOD");
        resourceMaps.putAll(resourceMap);
        
        for (Map.Entry<String, String> entry : resourceMaps.entrySet()) {
        	List<ConfigAttribute> attributes = 
        			SecurityConfig.createList(StringUtils.commaDelimitedListToStringArray(entry.getValue()));
            methodMap.put(entry.getKey(), attributes);
        }

        return methodMap;
    }

    protected Map<String, String> getAllResource(String type) {
        Map<String, String> resourceMap = new LinkedHashMap<String, String>();
        List<Resource> resources = resourceDao.getAllTypeResource(type);
        for (Resource resource : resources) {
            String guName = resource.getRoleName();
            String res_string = resource.getResString();

            if (resourceMap.containsKey(res_string)) {
                String value = resourceMap.get(res_string);
                resourceMap.put(res_string, value + "," + guName);
            } else {
                resourceMap.put(res_string, guName);
            }
        }
        return resourceMap;
    }

	public ResourceDao getResourceDao() {
		return resourceDao;
	}

	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}
}
