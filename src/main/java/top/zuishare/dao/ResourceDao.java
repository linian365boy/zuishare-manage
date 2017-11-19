package top.zuishare.dao;

import org.apache.ibatis.annotations.Param;
import top.zuishare.model.Resource;

import java.util.List;

/**
 * @ModuleID:
 * @Comments: 
 * @Author: ln
 * @Create Date: 2013-3-28
 * @Modified By: 
 * @Modified Date: 
 */
public interface ResourceDao {
	/**
	 * @FunName: getAllResource
	 * @Description:  拿到所有资源
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-3-28
	 */
	public List<Resource> getAllResource();
	/**
	 * @FunName: findResourceByParentId
	 * @Description:  通过二级菜单获取三级可以显示的资源
	 * @param menuId
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-3-28
	 */
	public List<Resource> findResourceByParentId(Integer menuId);
	/**
	 * @FunName: findResourceByParentId
	 * @Description:  通过二级菜单获取全部三级资源
	 * @param menuId
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-3-28
	 */
	public List<Resource> findAllResourceByParentId(Integer menuId);
	/**
	 * @FunName: findResourceByRole
	 * @Description:  通过角色拿到所有资源
	 * @param name
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-3-28
	 */
	public List<Resource> findResourceByRole(String name);
	/**
	 * @FunName: loadResourceByResourceId
	 * @Description:  查找权限菜单所属的子菜单
	 * @param id
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-3-28
	 */
	public Resource loadResourceByResourceId(Integer id);
	
	/**
	 * @FunName: getAllResource
	 * @Description:  拿到所有资源
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-3-28
	 */
	public List<Resource> getAllTypeResource(String type);
	/**
	 * updateRoleResources:更新角色资源
	 * @author tanfan 
	 * @param roleName
	 * @param ress 
	 * @since JDK 1.7
	 */
	public void insertRoleResources(@Param("roleName") String roleName, @Param("resources") List<Resource> ress);
	
	public void delRoleResources(String roleName);
	/**
	 * saveResource: 保存资源
	 * @author tanfan 
	 * @param resource 
	 * @since JDK 1.7
	 */
	public void saveResource(Resource resource);
	/**
	 * insertSuperRoleResource:(这里用一句话描述这个方法的作用). 
	 * @author tanfan 
	 * @param resource 
	 * @since JDK 1.7
	 */
	public void insertSuperRoleResource(Resource resource);
	
}
