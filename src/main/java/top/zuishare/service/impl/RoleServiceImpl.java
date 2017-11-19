package top.zuishare.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zuishare.dao.RoleDao;
import top.zuishare.dao.UserDao;
import top.zuishare.model.Resource;
import top.zuishare.model.Role;
import top.zuishare.model.User;
import top.zuishare.service.RoleService;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.util.PageRainier;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
	private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserDao userDao;
	
	/**
	 * @FunName: findAllByAjax
	 * @Description:  ajax异步拿到所有角色的name与desc属性
	 * @return
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	@Override
	public List<Role> findAllByAjax() {
		return roleDao.finAllByAjax();
	}
	/**
	 * @FunName: exportToCSV
	 * @Description:  角色导出csv文件，包括了所有的三级资源（显示与不显示的）。
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	/*public void exportToCSV(List<Role> roles, String fileName,
			String[] headers, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			this.getOutCSV().exportCSV(headers, roles, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
	}*/
	
	@Override
	public PageRainier<Role> findAll(RequestParam param) {
		long count = roleDao.findAllCount(param);
		PageRainier<Role> page = new PageRainier<Role>(count);
		page.setResult(roleDao.findAll(param));
		return page;
	}
	
	@Override
	public Role loadRoleByName(String roleName) {
		return roleDao.findOne(roleName);
	}
	@Override
	public boolean saveRole(Role role) {
		try {
			roleDao.save(role);
			return true;
		} catch (Exception e) {
			logger.error("新增角色报错",e);
		}
		return false;
	}
	@Override
	public boolean delRole(String roleId) {
		boolean flag = false;
		try {
			//修改次角色下的用户的角色为null
			userDao.updateUserRole(roleId,null);
			roleDao.delete(roleId);
			flag = true;
		} catch (Exception e) {
			logger.error("删除角色报错",e);
		}
		return flag;
	}
	@Override
	public boolean updateUserRole(Integer userId, List<Role> roles) {
		boolean flag = false;
		try {
			//先修改表数据
			roleDao.deleteByUserId(userId);
			//再插入数据
			roleDao.insertUserRole(userId,roles);
			flag = true;
		} catch (Exception e) {
			logger.error("删除角色报错",e);
		}
		return flag;
	}
	@Override
	public List<Role> findRoleByUser(User u) {
		return roleDao.findRoleByUser(u.getId());
	}
	@Override
	public Role findDefault() {
		return roleDao.findDefaultRole();
	}
	
	@Override
	public List<Resource> findResourceById(String roleId) {
		return roleDao.findResourceByRole(roleId);
	}
	
	@Override
	public boolean updateRole(Role temp) {
		boolean flag = false;
		try {
			roleDao.updateRole(temp);
			flag = true;
		} catch (Exception e) {
			logger.error("删除角色报错",e);
		}
		return flag;
	}
	@Override
	public String findRoleDesc(String roleId) {
		return roleDao.findRoleDesc(roleId);
	}
	
	@Override
	public List<Role> findNoDefaultRoleByUser(Integer userId) {
		return roleDao.findNoDefaultRoleByUser(userId);
	}
	/**
	 * @FunName: exportToCSVExNoDisplay
	 * @Description:  角色导出csv文件，只导出能显示的三级资源。
	 * @param roles
	 * @param fileName
	 * @param headers
	 * @param response
	 * @Author: ln
	 * @CreateDate: 2013-6-6
	 */
	/*public void exportToCSVExNoDisplay(List<Role> roles, String fileName,
			String[] headers, HttpServletResponse response){
		PrintWriter out = null;
		try{
			out = response.getWriter();
			this.getOutCSV().exportCSVExNoDisplay(headers, roles, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
	}*/
	
	/**
	 * @FunName: exportToCSV
	 * @Description:  角色导出csv文件
	 * @param roles 导出的角色列表
	 * @param fileName 导出的文件名
	 * @param headers 列名
	 * @param response
	 * @param l 特殊对待的二级菜单Id，如果此Id不为空，则（三级菜单即资源会导出！否则不导出）
	 * @Author: ln
	 * @CreateDate: 2013-5-24
	 */
	/*public void exportToCSV(List<Role> roles, String fileName,
			String[] headers, HttpServletResponse response, Long l) {
		if(l==null){
			exportToCSV(roles,fileName,headers,response);
		}else{
			PrintWriter out = null;
			try {
				out = response.getWriter();
				this.getOutCSV().exportCSV(headers, roles, out,l);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(out!=null){
					out.close();
				}
			}
		}
	}*/
}
