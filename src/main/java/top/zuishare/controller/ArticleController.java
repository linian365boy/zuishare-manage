package top.zuishare.controller;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.zuishare.service.ArticleCategoryService;
import top.zuishare.service.ArticleService;
import top.zuishare.service.SystemConfig;
import top.zuishare.spi.dto.LogType;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Article;
import top.zuishare.spi.util.Tools;
import top.zuishare.util.Constant;
import top.zuishare.util.LogUtil;
import top.zuishare.util.PageRainier;
import top.zuishare.vo.MessageVo;
import top.zuishare.vo.ReturnData;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author niange
 * @ClassName: ArticleController
 * @desp:
 * @date: 2017/12/2 下午10:01
 * @since JDK 1.7
 */

@Controller
@RequestMapping("/admin/article")
public class ArticleController {

    @Autowired
    private LogUtil logUtil;
    private final static Logger logger = LoggerFactory.getLogger(ArticleController.class);
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleCategoryService articleCategoryService;
    @Resource
    private SystemConfig systemConfig;

    @RequestMapping(value={"/list"}, method = RequestMethod.POST)
    public String list( ModelMap map){
        map.put("ajaxListUrl", "admin/article/getJsonList");
        return "admin/article/list";
    }

    @ResponseBody
    @RequestMapping("/getJsonList")
    public ReturnData<Article> list(RequestParam param){
        PageRainier<Article> page = articleService.findAll(param);
        ReturnData<Article> datas = new ReturnData<>(page.getTotalRowNum(), page.getResult());
        return datas;
    }

    @RequestMapping(value="/add", method = RequestMethod.GET)
    public String addUI(ModelMap modelMap){
        modelMap.put("articleCategorys", articleCategoryService.findList(Constant.C_ONE));
        return "admin/article/add";
    }

    @ResponseBody
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public MessageVo add(MultipartFile photo, Article article){
        logger.info("add article param => {}", article);
        StringBuilder sb = new StringBuilder();
        MessageVo vo = null;
        try {
            if(photo != null && !photo.isEmpty()){
                String realPath = systemConfig.getPicPath()+File.separator+"upload"+File.separator+"article";
                String newFileName = realPath+File.separator+ Tools.getRndFilename()+Tools.getExtname(photo.getOriginalFilename());
                FileUtils.copyInputStreamToFile(photo.getInputStream(), new File(newFileName));
                String url = newFileName.substring(realPath.lastIndexOf("upload"));
                article.setImgTitlePath(url);
            }
            article.setAuthor("TANFAN");
            article.setCreateTime(System.currentTimeMillis()/1000);
            article.setUpdateTime(System.currentTimeMillis()/1000);
            article.setViewNum(0);
            article.setStatus(0);
            article.setCategoryId(Integer.parseInt(article.getCategoryName().split("_")[0]));
            article.setCategoryName(article.getCategoryName().split("_")[1]);
            articleService.save(article);
            sb.append("文章名称："+article.getTitle());
            logUtil.log(LogType.ADD, sb.toString());
            logger.info("add article => {} succeed",article);
            vo = new MessageVo(Constant.SUCCESS_CODE,"新增主题文章【"+article.getTitle()+"】成功！");
        } catch (Exception e) {
            logger.error("add article error.",e);
            vo = new MessageVo(Constant.ERROR_CODE,"新增主题文章【"+article.getTitle()+"】失败！");
        }
        logger.info("add article return data => {}", vo);
        return vo;
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String updateUI(@PathVariable("id") long id, ModelMap map){
        map.put("model", articleService.loadOne(id));
        return "admin/article/update";
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public MessageVo update(MultipartFile photo, @PathVariable("id") long id, Article article){
        logger.info("update article param => {}", article);
        MessageVo vo = null;
        try{
            Article temp = articleService.loadOne(article.getId());
            if(photo != null && !photo.isEmpty()){
                String realPath = systemConfig.getPicPath()+File.separator+"upload"+File.separator+"article";
                String newFileName = realPath+File.separator+ Tools.getRndFilename()+Tools.getExtname(photo.getOriginalFilename());
                FileUtils.copyInputStreamToFile(photo.getInputStream(), new File(newFileName));
                String url = newFileName.substring(realPath.lastIndexOf("upload"));
                article.setImgTitlePath(url);
            }else{
                article.setImgTitlePath(temp.getImgTitlePath());
            }
            article.setCreateTime(temp.getCreateTime());
            article.setViewNum(temp.getViewNum());
            article.setAuthor(temp.getAuthor());
            article.setCategoryId(Integer.parseInt(article.getCategoryName().split("_")[0]));
            article.setCategoryName(article.getCategoryName().split("_")[1]);
            article.setStatus(0);
            article.setUpdateTime(System.currentTimeMillis()/1000);
            articleService.update(article);
            vo = new MessageVo(Constant.SUCCESS_CODE, "修改主题文章【"+article.getTitle()+"】成功！");
        }catch (Exception e){
            logger.error("update article error.", e);
            vo = new MessageVo(Constant.ERROR_CODE, "修改主题文章【"+article.getTitle()+"】失败！");
        }
        logger.info("update article return data => {}", vo);
        return vo;
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/delete")
    public MessageVo delete(@PathVariable("id") int id){
        logger.info("delete article param => {}", id);
        MessageVo vo = null;
        Article temp = articleService.loadOne(id);
        temp.setStatus(2);
        temp.setUpdateTime(System.currentTimeMillis()/1000);
        articleService.update(temp);
        vo = new MessageVo(Constant.SUCCESS_CODE, "删除主题文章【"+temp.getTitle()+"】成功！");
        logger.info("delete product return data => {}", vo);
        return vo;
    }

}
