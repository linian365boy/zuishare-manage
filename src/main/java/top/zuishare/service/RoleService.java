package top.zuishare.service;

import top.zuishare.model.Resource;
import top.zuishare.model.Role;
import top.zuishare.model.User;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.util.PageRainier;

import java.util.List;

public interface RoleService {
	/**
	 * @FunName: loadRoleByName
	 * @Description:  根据角色名获取角色信息
	 * @param roleName
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public Role loadRoleByName(String roleName);
	/**
	 * @FunName: saveRole
	 * @Description:  保存角色
	 * @param role
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public boolean saveRole(Role role);
	/**
	 * @FunName: delRole
	 * @Description:  删除角色
	 * @param roleId
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public boolean delRole(String roleId);
	/**
	 * @FunName: findAllSpecification
	 * @Description:  自定义的Specification
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	/*public Specification<Role> findAllSpecification(){
		return new Specification<Role>(){
			public Predicate toPredicate(Root<Role> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("defaultOrNo"), false);
			}};
	}*/
	/**
	 * @FunName: findAll
	 * @Description:  查询全部角色，角色列表
	 * @param pageNo
	 * @param pageSize
	 * @param flag
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	/*public PageRainier<Role> findAll(Integer pageNo,Integer pageSize,boolean flag){
		PageRainier<Role> page = null;
		if(flag){
			Specification<Role> rs = findAllSpecification();
			Page<Role> tempPage = roleDao.findAll(rs,new PageRequest(pageNo-1,pageSize,new Sort(Direction.DESC, "createDate")));
			List<Role> roles = tempPage.getContent();
			page = new PageRainier<Role>(tempPage.getTotalElements(),pageNo,pageSize);
			page.setResult(roles);
			return page;
		}else{
			page = new PageRainier<Role>();
			page.setResult(roleDao.findAll(new Sort(Direction.DESC, "createDate")));
			return page;
		}
	}*/
	/**
	 * @FunName: findRoleByUser
	 * @Description:  查看此用户的所有角色
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-3-28
	 */
	public List<Role> findRoleByUser(User u);
	
	/**
	 * @FunName: findRoleByUser
	 * @Description:  查看此用户的所有角色，排除默认角色
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-3-28
	 */
	public List<Role> findNoDefaultRoleByUser(Integer userId);
	
	/**
	 * @FunName: findDefault
	 * @Description:  查询默认的角色，只有一个！
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public Role findDefault();
	
	public List<Role> findAllByAjax();
	
	public PageRainier<Role> findAll(RequestParam param);
	
	public boolean updateRole(Role temp);
	/**
	 * findResourceById:通过角色查询权限
	 * @author tanfan 
	 * @param roleId
	 * @return 
	 * @since JDK 1.7
	 */
	public List<Resource> findResourceById(String roleId);
	/**
	 * 根据角色name查询中文描述
	 * @author tanfan 
	 * @param roleId
	 * @return 
	 * @since JDK 1.7
	 */
	public String findRoleDesc(String roleId);
	/**
	 * updateUserRole:修改用户角色
	 * @author tanfan 
	 * @param id
	 * @param roles
	 * @return 
	 * @since JDK 1.7
	 */
	public boolean updateUserRole(Integer id, List<Role> roles);
}
