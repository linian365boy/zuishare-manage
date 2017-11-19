package top.zuishare.dao;

import org.apache.ibatis.annotations.Param;
import top.zuishare.model.Resource;
import top.zuishare.model.Role;
import top.zuishare.spi.dto.request.RequestParam;
import java.util.List;

/**
 * @ModuleID:
 * @Comments: 
 * @Author: ln
 * @Create Date: 2013-3-28
 * @Modified By: 
 * @Modified Date: 
 */
public interface RoleDao {
		/**
		 * @FunName: finAllByAjax
		 * @Description:  ajax异步拿到所有角色的name与desc属性
		 * @return List<Object[]>
		 * @Author: ln
		 * @CreateDate: 2013-3-28
		 */
		public List<Role> finAllByAjax();
		/**
		 * @FunName: findRoleByUser
		 * @Description:  查找此用户所在的角色
		 * @param userId
		 * @return
		 * @Author: ln
		 * @CreateDate: 2013-3-28
		 */
		public List<Role> findRoleByUser(Integer userId);
		/**
		 * @FunName: findDefaultRole
		 * @Description:  查找默认的角色l，默认的角色只有一个！
		 * @return
		 * @Author: ln
		 * @CreateDate: 2013-4-7
		 */
		public Role findDefaultRole();
		
		public Role findOne(String roleName);
		
		public void save(Role role);
		
		public void delete(String roleId);
		/**
		 * findAllCount:查询所有除默认角色的角色数量
		 * @author tanfan 
		 * @return 
		 * @since JDK 1.7
		 */
		public long findAllCount(RequestParam param);
		/**
		 * findAllCount:查询所有除默认角色的角色
		 * @author tanfan 
		 * @return 
		 * @since JDK 1.7
		 */
		public List<Role> findAll(RequestParam param);
		/**
		 * updateRole:修改角色 
		 * @author tanfan 
		 * @param role 
		 * @since JDK 1.7
		 */
		public void updateRole(Role role);
		
		public List<Resource> findResourceByRole(String roleId);
		
		public String findRoleDesc(String roleId);
		
		public List<Role> findNoDefaultRoleByUser(Integer userId);
		/**
		 * deleteByUserId:根据用户id删除角色
		 * @author tanfan 
		 * @param userId 
		 * @since JDK 1.7
		 */
		public void deleteByUserId(Integer userId);
		/**
		 * insertUserRole:插入用户角色数据
		 * @author tanfan 
		 * @param userId
		 * @param roles 
		 * @since JDK 1.7
		 */
		public void insertUserRole(@Param("userId") Integer userId, @Param("roles") List<Role> roles);
}
