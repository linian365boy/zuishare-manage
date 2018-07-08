package top.zuishare.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.zuishare.model.User;
import top.zuishare.service.CategoryService;
import top.zuishare.service.ColumnService;
import top.zuishare.service.ProductService;
import top.zuishare.spi.dto.LogType;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Category;
import top.zuishare.spi.model.Column;
import top.zuishare.util.Constant;
import top.zuishare.util.LogUtil;
import top.zuishare.util.PageRainier;
import top.zuishare.vo.MessageVo;
import top.zuishare.vo.ReturnData;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/goods/category")
@Scope("prototype")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ColumnService columnService;
	@Autowired
	private ProductService productService;
    @Autowired
    private LogUtil logUtil;
	private final static Logger logger = LoggerFactory.getLogger(CategoryController.class);
	
	/**
	 * list:如果需要定位用户进入到哪个栏目菜单，需要传入HttpServletRequest、ModelMap两个参数，顺序不定
	 * @author tanfan 
	 * @param request
	 * @param map
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping("/categorys/list")
	public String list(HttpServletRequest request, ModelMap map){
		map.put("ajaxListUrl", "admin/goods/category/categorys/getJsonList");
		return "admin/goods/category/list";
	}
	
	@ResponseBody
	@RequestMapping("/categorys/getJsonList")
	public ReturnData<Category> getJsonList(RequestParam param){
		PageRainier<Category> categorys = categoryService.findAll(param);
		ReturnData<Category> datas = new ReturnData<Category>(categorys.getTotalRowNum(), categorys.getResult());
		return datas;
	}
	
	@RequestMapping(value="/add",method= RequestMethod.GET)
	public String add(ModelMap map) {
		return "admin_unless/goods/category/add";
	}
	
	@ResponseBody
	@RequestMapping(value="/add",method= RequestMethod.POST)
	public MessageVo add(Category category, HttpServletRequest request) {
		User u = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MessageVo vo = null;
		try {
			if(Integer.parseInt(request.getParameter("parentC"))==0){
				category.setParentId(null);
			}else{
				category.setParentId(Integer.parseInt(request.getParameter("parentC")));
			}
			category.setCreateDate(new Date());
			category.setCreateUserId(u.getId());
			categoryService.saveCategory(category);
			logUtil.log(LogType.ADD,"名称："+category.getEnName());
			logger.info("添加产品分类{}成功！",category);
			vo = new MessageVo(Constant.SUCCESS_CODE, "添加产品分类【"+category.getEnName()+"】成功！");
		} catch (Exception e) {
			logger.error("添加产品分类失败！",e);
			vo = new MessageVo(Constant.ERROR_CODE, "添加产品分类【"+category.getEnName()+"】失败！");
		}
		return vo;
	}
	
	@RequestMapping(value="/getParentByAjax/{flag}",method= RequestMethod.GET)
	@ResponseBody
	public List<Category> getParentByAjax(@PathVariable Integer flag){
		List<Category> parentsByAjax = categoryService.findParentByAjax();
		if(flag!=0){
			parentsByAjax.add(0, new Category(0,"根节点","root note"));
		}
		return parentsByAjax;
	}
	
	@RequestMapping(value="/{categoryId}/update",method= RequestMethod.GET)
	public String update(@PathVariable Integer categoryId, Model model) {
		if (categoryId != null) {
			Category category = categoryService.loadCategoryById(categoryId);
			model.addAttribute("model",category);
			List<Category> parentsByAjax = categoryService.findParentByAjax();
			parentsByAjax.add(0, new Category(0,"根节点","root note"));
			model.addAttribute("parents", parentsByAjax);
		}
		return "admin_unless/goods/category/update";
	}
	
	@ResponseBody
	@RequestMapping(value="/{categoryId}/update",method= RequestMethod.POST)
	public MessageVo update(HttpServletRequest request, @PathVariable Integer categoryId, Category category) {
		logger.info("update category param category => {}, request param => {}", category, request.getParameterMap());
		StringBuilder content = new StringBuilder();
		MessageVo vo = null;
		try {
			if(categoryId!=null){
				Category temp = categoryService.loadCategoryById(categoryId);
				String parentIds = (String)request.getParameter("parents");
				if(parentIds!=null){
					category.setParentId(Integer.parseInt(parentIds));
				}
				if(!temp.getEnName().equals(category.getEnName())){
					content.append("名称由\""+temp.getEnName()+"\"修改为\""+category.getEnName()+"\"");
				}else{
					content.append("名称："+temp.getEnName());
				}
				if(temp.getParentId()!=null&&!temp.getParentId().equals(category.getParentId())){
					content.append("一级分类由\""+temp.getParentName()+"\"修改为\""+category.getParentName()+"\"");
				}else if(temp.getParentId()==null&&parentIds!=null){
					content.append("一级分类由\"无\"修改为\""+category.getParentName()+"\"");
				}else{
					if(temp.getParentId()!=null){
						content.append("一级分类："+temp.getParentName());
					}
				}
				category.setCreateUserId(temp.getCreateUserId());
				categoryService.updateCategory(category);
				logUtil.log(LogType.EDIT,content.toString());
				logger.info("update category => {} success.",category);
				vo = new MessageVo(Constant.SUCCESS_CODE,"修改商品分类【"+category.getEnName()+"】成功！");
			}
		}catch(Exception e){
			logger.error("update category => {} error.", category, e);
			vo = new MessageVo(Constant.ERROR_CODE,"修改商品分类【"+category.getEnName()+"】失败！");
		}
		logger.info("update category return data => {}", vo);
		return vo;
	}
	
	@ResponseBody
	@RequestMapping(value="/existCategory",method= RequestMethod.POST)
	public boolean existCategory(String enName, String en){
		//en为空表示添加，否则为编辑
		boolean result = false;
		try {
			if(enName!=null){
				//如果没有修改name
				if(enName.equals(en)){
					result = true;	//true表示可用
				}else{
					Category ca = categoryService.loadCategoryByName(enName);
					if(ca==null){
						result = true;	//true表示可用，分类不存在
					}
				}
			}
		} catch (Exception e) {
			logger.error("查询是否存在相同分类发生错误",e);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/{categoryId}/del",method= RequestMethod.POST)
	public MessageVo del(@PathVariable Integer categoryId){
		MessageVo vo = null;
		if(categoryId!=null){
			Category temp = categoryService.loadCategoryById(categoryId);
			if(categoryService.checkHasChildren(categoryId)){
				vo = new MessageVo(Constant.ERROR_CODE,"请先删除该分类下的子分类");
			}else{
				//判断分类下是否有产品
				long count = productService.countByCateId(categoryId);
				if(count>0){
					vo = new MessageVo(Constant.ERROR_CODE,"请先删除该分类下的"+count+"个产品！");
				}else{
					logger.info("删除分类|{}",temp);
					categoryService.delCategory(categoryId);
					//日志记录
					logUtil.log(LogType.DEL, temp.getEnName()+"删除了");
					vo = new MessageVo(Constant.SUCCESS_CODE,"删除分类【"+temp.getEnName()+"】成功！");
				}
			}
		}
		return vo;
	}

	@RequestMapping(value="/getChildrenCate/{parentCateId}",method= RequestMethod.POST)
	@ResponseBody
	public List<Object[]> getChildrenCate(@PathVariable Integer parentCateId){
		List<Object[]> childrenCateArr = categoryService.findChildrenByParentCateId(parentCateId);
		return childrenCateArr;
	}
}
