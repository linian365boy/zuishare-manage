package top.zuishare.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zuishare.dao.MenuDao;
import top.zuishare.model.Menu;
import top.zuishare.model.Role;
import top.zuishare.service.MenuService;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.util.PageRainier;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
	private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
	@Autowired
	private MenuDao menuDao;
	/**
	 * @FunName: findParentMenuByRole
	 * @Description:  根据所属角色查找能访问的一级菜单
	 * @param roles
	 * @param flag
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	@Override
	public List<Menu> findParentMenuByRole(List<Role> roles, boolean flag) {
		List<Menu> parentM = null;
		if(!flag){//flag=false
			parentM =  menuDao.findParentMenuByRole(roles);
			for(Menu pare : parentM){
				pare.setChildren(menuDao.getChildldByParentAndRoles(pare.getId(),roles));
			}
		}else{//flag=true
			parentM = menuDao.findParentMenu();
		}
		return parentM;
	}
	/**
	 * @FunName: loadMenuByResourceId
	 * @Description:  查询资源所属的二级菜单
	 * @param resourceId
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	@Override
	public Menu loadMenuByResourceId(Integer resourceId) {
		return menuDao.loadMenuByResourceId(resourceId);
	}
	/**
	 * @Description:菜单列表
	 * @param param
	 * @return
	 */
	@Override
	public PageRainier<Menu> findAll(RequestParam param) {
		long count = menuDao.findAllCount(param);
		PageRainier<Menu> page = new PageRainier<Menu>(count);
		page.setResult(menuDao.findList(param));
		return page;
	}
	@Override
	public boolean delMenu(Integer id) {
		boolean flag = false;
		try{
			menuDao.delete(id);
			flag = true;
		}catch(Exception e){
			logger.error("删除菜单报错！");
		}
		return flag;
	}
	@Override
	public int saveMenu(Menu m) {
		return menuDao.save(m);
	}
	@Override
	public boolean updateMenu(Menu menu) {
		try{
			menuDao.update(menu);
			return true;
		}catch(Exception e){
			logger.error("修改菜单报错",e);
		}
		return false;
	}
	@Override
	public Menu loadMenuById(Integer id) {
		return menuDao.findOne(id);
	}
	@Override
	public List<Menu> findParentByAjax() {
		return menuDao.findParentByAjax();
	}
	@Override
	public boolean updateRoleMenu(String roleName, List<Menu> menus) {
		try{
			//先删除表数据
			menuDao.deleteRoleMenu(roleName);
			//再插入表数据
			menuDao.insertRoleMenu(roleName,menus);
			return true;
		}catch(Exception e){
			logger.error("更新角色菜单报错！",e);
		}
		return false;
	}
	
	@Override
	public long findChildMenuCount(Integer menuId) {
		return menuDao.findChildMenuCount(menuId);
	}
	
	@Override
	public List<Menu> findMenuByRole(String roleName) {
		return menuDao.findMenuByRole(roleName);
	}
}
