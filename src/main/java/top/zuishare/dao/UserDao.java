package top.zuishare.dao;

import org.apache.ibatis.annotations.Param;
import top.zuishare.model.User;
import top.zuishare.spi.dto.request.RequestParam;
import java.util.List;

/**
 * @ModuleID:
 * @Comments: 
 * @JDK Version Used:<JDK1.6>		
 * @Author: ln
 * @Create Date: 2013-3-28
 * @Modified By: 
 * @Modified Date: 
 */
public interface UserDao {
	/**
	 * @FunName: findUserByRole
	 * @Description:  查询此角色下的所有用户
	 * @param roleId
	 * @return List<User>
	 * @Author: ln
	 * @CreateDate: 2013-3-28
	 */
	public List<User> findUserByRole(String roleId);
	/**
	 * @FunName: findByName
	 * @Description:  通过用户名获得User对象
	 * @param username
	 * @return User
	 * @Author: ln
	 * @CreateDate: 2013-3-28
	 */
	public User findByName(@Param("username") String username);
	/**
	 * @FunName: findUserByLike
	 * @Description:  模糊查询
	 * @param username
	 * @return List<User>
	 * @Author: ln
	 * @CreateDate: 2013-3-28
	 */
	public List<User> findUserByLike(@Param("un") String username);
	/**
	 * @FunName: getPasswordById
	 * @Description:  得到密码（已经加密）
	 * @param id
	 * @return String
	 * @Author: ln
	 * @CreateDate: 2013-3-28
	 */
	public String getPasswordById(Integer id);
	
	public void changePassword(@Param("username") String username, @Param("password") String password);
	/**
	 * @FunName: unsubscribe
	 * @Description:  通过username注销用户
	 * @param username
	 * @Author: ln
	 * @CreateDate: 2013-5-8
	 */
	public void unsubscribe(String username);
	/**
	 * @FunName: unsubscribe
	 * @Description:  通过User主键注销用户
	 * @param model
	 * @Author: ln
	 * @CreateDate: 2013-5-8
	 */
	public int save(User model);
	
	public void delete(Integer id);
	
	public User findOne(Integer id);
	/**
	 * findAllCount:排除当前登录者，统计用户数
	 * @author tanfan 
	 * @param userId
	 * @param param 
	 * @return 
	 * @since JDK 1.7
	 */
	public long findAllCount(@Param("loginId") Integer userId, @Param("param") RequestParam param);
	
	public List<User> findList(@Param("loginId") Integer userId, @Param("param") RequestParam param);
	/**
	 * updateUserRole:修改用户的角色
	 * @author tanfan 
	 * @param roleId 
	 * @since JDK 1.7
	 */
	public void updateUserRole(@Param("roleId") String roleId, @Param("newRoleId") String newRoleId);
	
	public void updateUser(User user);
}
