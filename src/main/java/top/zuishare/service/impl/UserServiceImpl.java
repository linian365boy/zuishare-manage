package top.zuishare.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.zuishare.dao.RoleDao;
import top.zuishare.dao.UserDao;
import top.zuishare.model.User;
import top.zuishare.service.UserService;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.util.PageRainier;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserCache userCache;
	@Autowired
	private RoleDao roleDao;
	
	/**
	 * 根据用户名查询用户，用户名唯一
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		return this.loadUserByName(username);
	}
	/**
	 * 查询用户列表，根据Id排序，降序
	 * @param userId 
	 */
	@Override
	public PageRainier<User> findAllUser(RequestParam param, Integer userId) {
		PageRainier<User> page = null;
		long count = userDao.findAllCount(userId,param);
		page = new PageRainier<User>(count);
		page.setResult(userDao.findList(userId,param));
		return page;
	}
	
	@Override
	public User loadUserByName(String userid) {
		return userDao.findByName(userid);
	}
	
	/**
	 * 保存用户
	 */
	@Override
	public boolean saveUser(User model) {
		try{
			//新增用户
			model.setPassword(new Md5PasswordEncoder().encodePassword(model.getPassword(), null));
			userDao.save(model);
			logger.info("新增用户信息|{}",model);
			roleDao.insertUserRole(model.getId(), model.getRoles());
			return true;
		}catch(Exception e){
			logger.error("保存用户报错",e);
		}
		return false;
	}
	/**
	 * 删除用户
	 */
	@Override
	public void deleteUser(Integer userId) {
		try{
			userDao.delete(userId);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 根据Id删除用户
	 */
	@Override
	public void deleteUserById(Integer id) {
		try {
			userDao.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据Id删除密码
	 */
	@Override
	public String getPaswordById(Integer id) {
		return userDao.getPasswordById(id);
	}

	/**
	 * 模糊查询用户
	 */
	/*public PageRainier<User> findUserByLike(Specification<User> speci,String field,String condition,Integer pageNo,Integer pageSize){
		Page<User> tempPage = userDao.findAll(speci, 
				new PageRequest(pageNo-1,pageSize,new Sort(Direction.DESC,"id")));
		PageRainier<User> page = new PageRainier<User>(tempPage.getTotalElements(),pageNo,pageSize);
		page.setResult(tempPage.getContent());
		return page;
	}*/
	
	/*private Specification<User> findUserByRoleLikeSpeci(final String role){
		return new Specification<User>() {
			public Predicate toPredicate(Root<User> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<Role,String> join = root.joinList("roles");
				//root.fetch("name");
				return cb.like(join.<String>get("desc"), role);
			}
		};
	}*/
	/**
	 * 根据角色模糊查询用户
	 */
	/*public PageRainier<User> findUserByRoleLike(final String role,Integer pageNo,Integer pageSize){
		Specification<User> speci = findUserByRoleLikeSpeci(role);
		Page<User> tempPage = userDao.findAll(speci, new PageRequest(pageNo-1,pageSize));
		PageRainier<User> page = new PageRainier<User>(tempPage.getTotalElements(),pageNo,pageSize);
		page.setResult(tempPage.getContent());
		return page;
	}*/
	/**
	 * @FunName: changePassword
	 * @Description:  修改密码
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @param currentUser 当前用户
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	@Override
	public void changePassword(String oldPassword, String newPassword, Authentication currentUser){
		currentUser = SecurityContextHolder.getContext().getAuthentication();
		if(currentUser ==null){
			throw new AccessDeniedException("修改密码错误，不存在此用户！");
		}
		String username = currentUser.getName();
		userDao.changePassword(username,newPassword);
		SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(currentUser,newPassword));
		userCache.removeUserFromCache(username);
	}
	
	protected Authentication createNewAuthentication(Authentication currentAuth, String newPassword) {
        UserDetails user = loadUserByUsername(currentAuth.getName());
        UsernamePasswordAuthenticationToken newAuthentication =
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());
        return newAuthentication;
    }
	
	/**
	 * @FunName: resetPassword
	 * @Description:  重置密码
	 * @param username
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	@Override
	public void resetPassword(String username){
		userDao.changePassword(username, new Md5PasswordEncoder().encodePassword(username,null));
	}

	@Override
	public User loadUserById(Integer id) {
		return userDao.findOne(id);
	}
	/**
	 * 注销用户
	 */
	@Override
	public boolean unsubscribe(String username) {
		try{
			userDao.unsubscribe(username);
			return true;
		}catch(Exception e){
			logger.error("用户注销失败！",e);
		}
		return false;
	}
	
	@Override
	public boolean updateUser(User user) {
		try{
			userDao.updateUser(user);
			return true;
		}catch(Exception e){
			logger.error("修改用户失败！",e);
		}
		return false;
	}
	
	@Override
	public List<User> findUserByRole(String roleId) {
		return userDao.findUserByRole(roleId);
	}
	public UserCache getUserCache() {
		return userCache;
	}
	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}
}
