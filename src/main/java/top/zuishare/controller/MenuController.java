package top.zuishare.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import top.zuishare.model.Menu;
import top.zuishare.model.Resource;
import top.zuishare.model.User;
import top.zuishare.service.MenuService;
import top.zuishare.service.ResourceService;
import top.zuishare.spi.dto.LogType;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.util.Constant;
import top.zuishare.util.LogUtil;
import top.zuishare.util.PageRainier;
import top.zuishare.vo.MessageVo;
import top.zuishare.vo.ReturnData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/admin/sys/menu")
@SessionAttributes("menuJson")
@Scope("prototype")
public class MenuController {
	@Autowired
	private MenuService menuService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private LogUtil logUtil;
	private final static Logger logger = LoggerFactory.getLogger(MenuController.class);
	
	@RequestMapping({"/menus/list"})
	public String list(HttpServletRequest request,ModelMap map){
		map.put("ajaxListUrl","admin/sys/menu/menus/getJsonList");
		return "admin/sys/menu/list";
	}
	
	@ResponseBody
	@RequestMapping({"/menus/getJsonList"})
	public ReturnData<Menu> getJsonList(RequestParam param){
		PageRainier<Menu> menus = menuService.findAll(param);
		ReturnData<Menu> datas = new ReturnData<Menu>(menus.getTotalRowNum(),menus.getResult());
		return datas;
	}
	
	@RequestMapping(value="/add",method= RequestMethod.GET)
	public String addUI(ModelMap map){
		List<Menu> parentMenu = menuService.findParentByAjax();
		map.put("parentMenu", parentMenu);
		return "admin_unless/sys/menu/add";
	}
	
	@RequestMapping("/{menuId}/update")
	public String updateUI(@PathVariable("menuId") Integer menuId, ModelMap map){
		Menu menu = menuService.loadMenuById(menuId);
		List<Menu> parentMenu = menuService.findParentByAjax();
		map.put("model", menu);
		map.put("parentMenu", parentMenu);
		return "admin_unless/sys/menu/update";
	}

	@ResponseBody
	@RequestMapping(value="/{menuId}/update",method= RequestMethod.POST)
	public MessageVo doUpdate(@PathVariable("menuId") Integer menuId, Menu menu){
	    logger.info("doUpdate menu param => {}", menu);
		MessageVo vo = null;
		Menu tempMenu = menuService.loadMenuById(menuId);
		if(menu.getParentId() ==0 ) menu.setParentId(null);
		if(menuService.updateMenu(menu)){
			logger.info("old menu => {}, update to => {} succeed.", tempMenu, menu);
			vo = new MessageVo(Constant.SUCCESS_CODE,"修改后台菜单成功！");
		}else{
			logger.info("old menu => {}, update to => {} fail.", tempMenu, menu);
			vo = new MessageVo(Constant.ERROR_CODE,"修改后台菜单失败！");
		}
        logger.info("doUpdate return data => {}", vo);
		return vo;
	}
	
	@ResponseBody
	@RequestMapping(value="/add",method= RequestMethod.POST)
	public MessageVo add(Menu menu){
	    logger.info("add menu param menu => {}, resource => {}", menu);
		MessageVo vo = null;
		try{
			if(menu.getParentId() == 0) menu.setParentId(null);
			int updateCount = menuService.saveMenu(menu);
			if(updateCount>0){
				logUtil.log(LogType.ADD,"名称："+menu.getName());
				logger.info("add menu => {} succeed.",menu);
				vo = new MessageVo(Constant.SUCCESS_CODE);
			}else{
				logUtil.log(LogType.ADD,"名称："+menu.getName());
				vo = new MessageVo(Constant.ERROR_CODE,"新增菜单【"+menu.getName()+"】失败");
			}
		}catch(Exception e){
			vo = new MessageVo(Constant.ERROR_CODE,"新增菜单【"+menu.getName()+"】失败");
			logger.error("add menu error.",e);
		}
        logger.info("add menu return data => {}", vo);
		return vo;
	}
	
	@ResponseBody
	@RequestMapping(value="/{menuId}/del",method= RequestMethod.POST)
	public MessageVo del(@PathVariable("menuId") Integer menuId){
	    logger.info("delete menu menuId => {}", menuId);
		MessageVo vo = null;
		//1、先判断是否有子节点
		long count = menuService.findChildMenuCount(menuId);
		if(count==0){
			//2、删除
			if(menuService.delMenu(menuId)){
				vo = new MessageVo(Constant.SUCCESS_CODE,"删除菜单成功！");
			}else{
				vo = new MessageVo(Constant.ERROR_CODE,"删除菜单失败！");
			}
		}else{
			vo = new MessageVo(Constant.ERROR_CODE,"该菜单下有"+count+"个子菜单，请先删除子菜单！");
		}
        logger.info("delete menu return data => {}", vo);
		return vo;
	}
	
	private String generateJsonString(HttpServletRequest request,HttpSession session) {
		String output = null;
		String roleName = request.getParameter("name");
		String id = request.getParameter("id");
		String flag = request.getParameter("flag");
		if(id!=null && !("0".equals(id)) && StringUtils.isNotBlank(roleName)){
			logger.info("enter here id|{}, roleName|{}",id,roleName);
			output = generateTreeNodeJsonString(Integer.parseInt(id),roleName);
		}else{
			output = generateInitTreeString(session,roleName,Boolean.parseBoolean(flag));
		}
		return output;
	}
	
	/**
	 * generateTreeNodeXmlString:获取第三级资源
	 * @author tanfan 
	 * @param menuId
	 * @param roleName
	 * @return 
	 * @since JDK 1.7
	 */
	private String generateTreeNodeJsonString(int menuId, String roleName) {
		StringBuilder sb = new StringBuilder();
		List<Resource> resources = resourceService.findResourceByParentId(menuId);
		List<Resource> resourceNames = resourceService.findResourceByRole(roleName);
		sb.append("{\"id\": \""+menuId+"\",\"item\":[");
		Resource res = null;
		boolean flag = false;
		int itIndex = 0;
		for(Iterator<Resource>  resIt = resources.iterator();resIt.hasNext();){
			res = resIt.next();
			for(Iterator<Resource> it=resourceNames.iterator();it.hasNext();){
				Integer id = it.next().getId();
				if(res.getId()==id){
					flag = true;
					if(itIndex>0){
						sb.append(",");
					}
					sb.append("{\"id\": \"r_"+res.getId()+"\",\"text\":\""+res.getDescn()+"\",\"checked\":\"1\"}");
					itIndex ++;
					break;
				}
				flag = false;
			}
			if(!flag){
				if(itIndex>0){
					sb.append(",");
				}
				sb.append("{\"id\":\"r_"+res.getId()+"\",\"text\":\""+res.getDescn()+"\"}");
				itIndex ++;
			}
		}
		sb.append("]}");
		return sb.toString();
	}
	
	@ResponseBody
	@RequestMapping(value="/findAllMenu",method= RequestMethod.GET)
	public Object findAllMenu(HttpServletRequest request, HttpSession session){
		return this.generateJsonString(request,session);
	}

	@ResponseBody
	@RequestMapping(value="/findMenuByRole",method= RequestMethod.GET)
	public Object findMenuByRole(HttpServletRequest request, HttpSession session){
		Object responseStr = session.getAttribute("menuJson");
		if(responseStr==null){
			responseStr = this.generateJsonString(request,session);
		}
		return responseStr;
	}
	
	/**
	 * @FunName: generateInitTreeString
	 * @Description:  获取第一级，第二级菜单
	 * @param flag
	 * @return
	 * @Author: 李年
	 * @CreateDate: 2013-5-9
	 */
	private String generateInitTreeString(HttpSession session, String name, boolean flag){
		Iterator<Menu> it = null;
		List<Menu> children = null;
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StringBuilder jsonStr = new StringBuilder();
		List<Menu> menus = menuService.findParentMenuByRole(user.getRoles(),flag);
		List<Resource> roleResources = null;	//角色能访问的资源
		List<Resource> resources = null;		//二级菜单下能访问的可显示的资源
		if(name!=null){
			roleResources = resourceService.findResourceByRole(name);
		}
		String displayName;
		boolean flags = false;
		jsonStr.append("{\"id\": \"0\",\"item\":[");
		int menusIndex = 0;
		for(Menu menu : menus){
			if(flag){
				displayName = menu.getMark();
			}else{
				displayName = menu.getName();
			}
			if(menusIndex>0){
				jsonStr.append(",");
			}
			jsonStr.append("{\"child\":\"1\",\"text\": \""+displayName+"\",\"id\": \""+menu.getId()+"\",\"url\": \""+menu.getUrl()+"\",\"item\":[");
			menusIndex++;
			if(menu.getChildren().size()>0&&menu.getChildren()!=null){
				children = menu.getChildren();
				it = children.iterator();
				Menu subMenu = null;
				int itIndex = 0;
				while(it.hasNext()){
					subMenu = it.next();
					if(flag){
						displayName = subMenu.getMark();
					}else{
						displayName = subMenu.getName();
					}
					resources = resourceService.findResourceByParentId(subMenu.getId());
					if(resources.size()==0&&flag){
						if(itIndex>0){
							jsonStr.append(",");
						}
						for(Iterator<Resource> itRes = roleResources.iterator();itRes.hasNext();){
							if(itRes.next().getMenuId() == subMenu.getId()){
								flags = true;
								jsonStr.append("{\"checked\":\"1\",\"child\":\"0\",\"text\":\""+displayName+"\",\"id\":\""+subMenu.getId()+"\",\"url\":\""+subMenu.getUrl()+"\"}");
								itIndex++;
								break;
							}
							flags = false;
						}
						if(!flags){
							jsonStr.append("{\"child\":\"0\",\"text\":\""+displayName+"\",\"id\":'"+subMenu.getId()+"',\"url\":\""+subMenu.getUrl()+"\"}");
							itIndex++;
						}
					}else{
						if(itIndex>0){
							jsonStr.append(",");
						}
						jsonStr.append("{\"child\":\"1\",\"text\":\""+displayName+"\",\"id\":\""+subMenu.getId()+"\",\"url\":\""+subMenu.getUrl()+"\"}");
						itIndex++;
					}
				}
			}
			jsonStr.append("]}");
		}
		jsonStr.append("]}");
		logger.info("generateInitTreeString menu json => {}",jsonStr.toString());
		session.setAttribute("menuJson", jsonStr.toString());
		return jsonStr.toString();
	}
}