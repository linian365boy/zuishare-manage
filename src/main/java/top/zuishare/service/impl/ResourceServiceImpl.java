package top.zuishare.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zuishare.dao.ResourceDao;
import top.zuishare.model.Resource;
import top.zuishare.service.ResourceService;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
	private static Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);
	@Autowired
	private ResourceDao resourceDao;
	/**
	 * 通过父菜单得到资源
	 * （通过二级菜单得到三级资源）
	 * 只获取可以显示的资源
	 */
	@Override
	public List<Resource> findResourceByParentId(Integer menuId){
		return resourceDao.findResourceByParentId(menuId);
	}
	/**
	 * @FunName: findAllResourceByParentId
	 * @Description:  获取全部子资源
	 * @param menuId
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-6-5
	 */
	@Override
	public List<Resource> findAllResourceByParentId(Integer menuId){
		return resourceDao.findAllResourceByParentId(menuId);
	}
	/**
	 * 通过角色拿到所有能访问的资源
	 */
	@Override
	public List<Resource> findResourceByRole(String name) {
		return resourceDao.findResourceByRole(name);
	}
	@Override
	public Resource loadResourceByResource(Integer id) {
		return resourceDao.loadResourceByResourceId(id);
	}
	@Override
	public boolean updateRoleResources(String roleName, List<Resource> ress) {
		boolean flag = false;
		try{
			//先删除表数据
			resourceDao.delRoleResources(roleName);
			//再插入
			resourceDao.insertRoleResources(roleName,ress);
			flag = true;
		}catch(Exception e){
			logger.error("更新角色资源报错！",e);
		}
		return flag;
	}
	
	@Override
	public boolean saveResource(Resource resource) {
		boolean flag = false;
		try{
			//保存资源
			resourceDao.saveResource(resource);
			//资源权限加入到超级管理员角色里面
			resourceDao.insertSuperRoleResource(resource);
			flag = true;
		}catch(Exception e){
			logger.error("保存资源报错！",e);
		}
		return flag;
	}
}
