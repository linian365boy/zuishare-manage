package top.zuishare.controller;

import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.zuishare.model.Category;
import top.zuishare.service.ArticleCategoryService;
import top.zuishare.service.ArticleService;
import top.zuishare.spi.dto.LogType;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.ArticleCategory;
import top.zuishare.util.Constant;
import top.zuishare.util.LogUtil;
import top.zuishare.util.PageRainier;
import top.zuishare.util.TimeUtils;
import top.zuishare.vo.MessageVo;
import top.zuishare.vo.ReturnData;

/**
 * @author niange
 * @ClassName: ArticleCategoryController
 * @desp:
 * @date: 2017/12/2 下午10:20
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/admin/article/category")
public class ArticleCategoryController  {
    @Autowired
    private LogUtil logUtil;
    private final static Logger logger = LoggerFactory.getLogger(ArticleCategoryController.class);

    @Autowired
    private ArticleCategoryService articleCategoryService;
    @Autowired
    private ArticleService articleService;

    @RequestMapping(value={"/list"}, method = RequestMethod.POST)
    public String list( ModelMap map){
        map.put("ajaxListUrl", "admin/article/category/getJsonList");
        return "admin/article/category/list";
    }

    @ResponseBody
    @RequestMapping("/getJsonList")
    public ReturnData<ArticleCategory> list(RequestParam param){
        PageRainier<ArticleCategory> page = articleCategoryService.findAllList(param);
        ReturnData<ArticleCategory> datas = new ReturnData<>(page.getTotalRowNum(), page.getResult());
        return datas;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addUI(){
        return "admin_unless/article/category/add";
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public MessageVo add(ArticleCategory category){
        logger.info("add article category param => {}", category);
        MessageVo vo = null;
        try {
            category.setCreateTime(TimeUtils.getTime());
            articleCategoryService.save(category);
            vo = new MessageVo(Constant.SUCCESS_CODE, "新增主题分类【"+category.getName()+"】成功！");
        }catch(Exception e){
            logger.error("add article category error.", e);
            vo = new MessageVo(Constant.ERROR_CODE, "新增主题分类【"+category.getName()+"】失败！");
        }
        logger.info("add article return data => {}", vo);
        return vo;
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String updateUI(@PathVariable("id")int id, ModelMap map){
        map.put("model", articleCategoryService.loadOne(id));
        return "admin_unless/article/category/update";
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public MessageVo update(@PathVariable("id") int id, ArticleCategory category){
        logger.info("update article category param category => {}", category);
        MessageVo vo = null;
        try{
            ArticleCategory tempCcategory = articleCategoryService.loadOne(id);
            category.setCreateTime(tempCcategory.getCreateTime());
            //修改分类之后，状态重置回锁定
            category.setStatus(Constant.C_ZERO);
            articleCategoryService.update(category);
            vo = new MessageVo(Constant.SUCCESS_CODE, "修改商品分类【"+category.getName()+"】成功！");
        }catch(Exception e){
            logger.error("update article category error.", e);
            vo = new MessageVo(Constant.ERROR_CODE, "修改商品分类【"+category.getName()+"】失败！");
        }
        logger.info("update article category return data => {}", vo);
        return vo;
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/changeStatus", method = RequestMethod.POST)
    public MessageVo changeStatus(@PathVariable("id") int id, int status){
        logger.info("article category => {}, old status => {} changeStatus.", id, status);
        MessageVo vo = null;
        try{
            articleCategoryService.updateStatus(id, status);
            vo = new MessageVo(Constant.SUCCESS_CODE, "修改状态成功！");
            logger.error("article category => {}, old status => {} changeStatus success.", id, status);
        }catch (Exception e){
            logger.error("article category => {}, old status => {} changeStatus fail.", id, status, e);
            vo = new MessageVo(Constant.ERROR_CODE, "修改状态失败！");
        }
        logger.info("article category => {} change status return data => {}", id, vo);
        return vo;
    }

    @ResponseBody
    @RequestMapping(value="/{id}/delete",method= RequestMethod.GET)
    public MessageVo del(@PathVariable int id){
        logger.info("delete article category param => {}", id);
        MessageVo vo = null;
        //判断分类下是否有产品
        long count = articleService.countByCateId(id);
        if(count>0){
            vo = new MessageVo(Constant.ERROR_CODE,"请先删除该分类下的"+count+"个主题文章！");
        }else{
            ArticleCategory temp = articleCategoryService.loadOne(id);
            logger.info("删除主题分类|{}",temp);
            articleCategoryService.delCategory(temp);
            //日志记录
            logUtil.log(LogType.DEL, temp.getName());
            vo = new MessageVo(Constant.SUCCESS_CODE,"删除主题分类【"+temp.getName()+"】成功！");
        }
        logger.info("delete article category return data => {}", vo);
        return vo;
    }



}
