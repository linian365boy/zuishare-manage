package top.zuishare.service;

import top.zuishare.model.Menu;
import top.zuishare.model.Role;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.util.PageRainier;

import java.util.List;

public interface MenuService {
	
	public boolean delMenu(Integer id);
	
	public int saveMenu(Menu m);
	
	public Menu loadMenuById(Integer id);
	
	public List<Menu> findParentByAjax();

	/**
	 * @FunName: loadMenuByResourceId
	 * @Description:  查找权限资源所属的二级菜单
	 * @param resourceId 资源Id
	 * @return 所属的二级菜单
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public Menu loadMenuByResourceId(Integer resourceId);
	/**
	 * findAll:分页查询菜单 
	 * @author tanfan 
	 * @param param
	 * @return 
	 * @since JDK 1.7
	 */
	public PageRainier<Menu> findAll(RequestParam param);
	
	/**
	 * findParentMenuByRole:根据角色查第一级菜单
	 * @author tanfan 
	 * @param roles
	 * @param flag
	 * @return 
	 * @since JDK 1.7
	 */
	public List<Menu> findParentMenuByRole(List<Role> roles, boolean flag);
	/**
	 * updateMenu:修改菜单 
	 * @author tanfan 
	 * @param menu
	 * @return 
	 * @since JDK 1.7
	 */
	public boolean updateMenu(Menu menu);
	/**
	 * updateRoleMenu:更新角色菜单
	 * @author tanfan 
	 * @param roleName
	 * @param menus
	 * @return 
	 * @since JDK 1.7
	 */
	public boolean updateRoleMenu(String roleName, List<Menu> menus);
	/**
	 * findMenuByRole:根据角色查菜单
	 * @author tanfan 
	 * @param roleName
	 * @return 
	 * @since JDK 1.7
	 */
	public List<Menu> findMenuByRole(String roleName);
	
	/**
	 * findChildMenuCount:获取子节点个数
	 * @author tanfan 
	 * @param menuId
	 * @return 
	 * @since JDK 1.7
	 */
	public long findChildMenuCount(Integer menuId);
	
}
