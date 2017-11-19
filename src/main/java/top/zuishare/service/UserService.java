package top.zuishare.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import top.zuishare.model.User;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.util.PageRainier;

import java.util.List;

public interface UserService extends UserDetailsService {
	/**
	 * @FunName: findAllUser
	 * @Description:  查询所有用户。也是用户列表
	 * @param param
	 * @param userId 当前登录Id
	 * @return 
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public PageRainier<User> findAllUser(RequestParam param, Integer userId);
	/**
	 * @FunName: loadUserByName
	 * @Description:  根据用户名查询用户。用户名是唯一的
	 * @param username
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public User loadUserByName(String username);
	/**
	 * @FunName: saveUser
	 * @Description:  保存用户
	 * @param model
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public boolean saveUser(User model);
	/**
	 * @FunName: deleteUser
	 * @Description:  删除用户
	 * @param id
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public void deleteUser(Integer id);
	/**
	 * @FunName: getPaswordById
	 * @Description:  
	 * @param id
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public String getPaswordById(Integer id);
	/**
	 * @FunName: findUserByLike
	 * @Description:  用户模糊查询
	 * @param speci
	 * @param field
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	//public PageRainier<User> findUserByLike(Specification<User> speci, String field, String condition, Integer pageNo, Integer pageSize);
	/**
	 * @FunName: findUserByRoleLike
	 * @Description:  根据用户的角色模糊查询
	 * @param role
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	//public PageRainier<User> findUserByRoleLike(String role,Integer pageNo,Integer pageSize);
	/**
	 * @FunName: loadUserById
	 * @Description:  根据用户ID加载用户对象
	 * @param id
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public User loadUserById(Integer id);
	/**
	 * @FunName: unsubscribe
	 * @Description:  注销用户
	 * @param username
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public boolean unsubscribe(String username);
	/**
	 * @FunName: findUserByRole
	 * @Description:  查询某角色下的用户对象
	 * @param roleId
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	public List<User> findUserByRole(String roleId);
	
	public void resetPassword(String username);
	
	public void changePassword(String oldPassword, String password, Authentication authentication);
	
	public boolean updateUser(User user);
	
	public void deleteUserById(Integer id);
}
