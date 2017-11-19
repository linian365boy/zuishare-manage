package top.zuishare.service;

import top.zuishare.model.Resource;

import java.util.List;

public interface ResourceService {
	/**
	 * @FunName: findResourceByParentId
	 * @Description:  根据二级菜单查询资源,只获取能够显示的资源
	 * @param menuId
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public List<Resource> findResourceByParentId(Integer menuId);
	/**
	 * @FunName: findResourceByParentId
	 * @Description:  根据二级菜单查询全部资源
	 * @param menuId
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public List<Resource> findAllResourceByParentId(Integer menuId);
	/**
	 * @FunName: findResourceByRole
	 * @Description:  根据角色查看能访问的资源
	 * @param name
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public List<Resource> findResourceByRole(String name);
	
	/**
	 * @FunName: loadResourceByResource
	 * @Description:  根据资源Id查询资源对象
	 * @param id
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public Resource loadResourceByResource(Integer id);
	/**
	 * updateRoleResources:更新角色资源
	 * @author tanfan 
	 * @param roleName
	 * @param ress
	 * @return 
	 * @since JDK 1.7
	 */
	public boolean updateRoleResources(String roleName, List<Resource> ress);
	/**
	 * saveResource:插入一条方法Method资源
	 * @author tanfan 
	 * @param resource
	 * @return 
	 * @since JDK 1.7
	 */
	public boolean saveResource(Resource resource);
}
