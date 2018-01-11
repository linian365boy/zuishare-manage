package top.zuishare.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
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
import top.zuishare.spi.model.ArticleCategory;
import top.zuishare.spi.util.Tools;
import top.zuishare.util.*;
import top.zuishare.vo.ArticleQueryVo;
import top.zuishare.vo.MessageVo;
import top.zuishare.vo.ReturnData;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

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
        logger.info("add article param => {}", article.getTitle());
        StringBuilder sb = new StringBuilder();
        MessageVo vo = null;
        try {
            if(article.getContentType() == Constant.ARTICLE_CONTENT_ONE_TYPE){
                article.setImgTitlePath(null);
                photo = null;
            }
            if(photo != null && !photo.isEmpty()){
                //picPath前面的file:不需要
                String realPath = systemConfig.getPicPath().substring(5)+Constant.ARTICLE_PIC_TITLE_PRE;
                String newFileName = realPath+File.separator+ Tools.getRndFilename()+Tools.getExtname(photo.getOriginalFilename());
                FileUtils.copyInputStreamToFile(photo.getInputStream(), new File(newFileName));
                String url = newFileName.substring(realPath.lastIndexOf(Constant.UPLOAD_PRE));
                article.setImgTitlePath(url);
            }
            String introduce = Jsoup.parse(article.getContent()).text();
            if(StringUtils.isNotBlank(introduce)) {
                if(introduce.length() > systemConfig.getLimitSize()) {
                    article.setIntroduce(introduce.substring(0, systemConfig.getLimitSize()));
                }else{
                    article.setIntroduce(introduce);
                }
            }else{
                article.setIntroduce(article.getContent());
            }
            article.setAuthor(Constant.AUTHOR);
            article.setCreateTime(TimeUtils.getTime());
            article.setUpdateTime(TimeUtils.getTime());
            article.setViewNum(0);
            article.setStatus(0);
            article.setCategoryId(Integer.parseInt(article.getCategoryName().split("_")[0]));
            article.setCategoryName(article.getCategoryName().split("_")[1]);
            articleService.save(article);
            sb.append("文章名称："+article.getTitle());
            logUtil.log(LogType.ADD, sb.toString());
            logger.info("add article => {} succeed",article.getTitle());
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
        map.put("articleCategorys", articleCategoryService.findList(Constant.C_ONE));
        return "admin/article/update";
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public MessageVo update(MultipartFile photo, @PathVariable("id") long id, Article article){
        logger.info("update article param => {}", article.getTitle());
        MessageVo vo = null;
        try{
            String realPath = systemConfig.getPicPath().substring(5)+Constant.ARTICLE_PIC_TITLE_PRE;
            Article temp = articleService.loadOne(id);
            if(article.getContentType() == Constant.ARTICLE_CONTENT_ONE_TYPE){
                article.setImgTitlePath(null);
                photo = null;
                //如果原类型为图文类型，把原图片删除
                if(temp.getContentType() != Constant.ARTICLE_CONTENT_ONE_TYPE) {
                    FileUtil.delFile(realPath  + temp.getImgTitlePath().substring(Constant.UPLOAD_ARTICLE_PIC_TITLE.length()));
                }
            }
            if(photo != null && !photo.isEmpty()){
                //picPath前面的file:不需要
                String newFileName = realPath+File.separator+ Tools.getRndFilename()+Tools.getExtname(photo.getOriginalFilename());
                FileUtils.copyInputStreamToFile(photo.getInputStream(), new File(newFileName));
                String url = newFileName.substring(realPath.lastIndexOf(Constant.UPLOAD_PRE));
                article.setImgTitlePath(url);
                //如果原类型为图文类型，把原图片删除
                if(temp.getContentType() != Constant.ARTICLE_CONTENT_ONE_TYPE
                        && StringUtils.isNotBlank(temp.getImgTitlePath())) {
                    FileUtil.delFile(realPath + temp.getImgTitlePath().substring(Constant.UPLOAD_ARTICLE_PIC_TITLE.length()));
                }
            }else if(article.getContentType() != Constant.ARTICLE_CONTENT_ONE_TYPE){
                article.setImgTitlePath(temp.getImgTitlePath());
            }
            String introduce = Jsoup.parse(article.getContent()).text();
            if(StringUtils.isNotBlank(introduce)) {
                if(introduce.length() > systemConfig.getLimitSize()) {
                    article.setIntroduce(introduce.substring(0, systemConfig.getLimitSize()));
                }else{
                    article.setIntroduce(introduce);
                }
            }else{
                article.setIntroduce(article.getContent());
            }
            article.setCreateTime(temp.getCreateTime());
            article.setViewNum(temp.getViewNum());
            article.setAuthor(temp.getAuthor());
            article.setCategoryId(Integer.parseInt(article.getCategoryName().split("_")[0]));
            article.setCategoryName(article.getCategoryName().split("_")[1]);
            //修改之后，状态修改为锁定，等待下一次的发布才会出现在前端
            article.setStatus(0);
            article.setUpdateTime(TimeUtils.getTime());
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
    public MessageVo delete(@PathVariable("id") long id){
        logger.info("delete article param => {}", id);
        MessageVo vo = null;
        Article temp = articleService.loadOne(id);
        temp.setStatus(2);
        temp.setUpdateTime(TimeUtils.getTime());
        articleService.update(temp);
        vo = new MessageVo(Constant.SUCCESS_CODE, "删除主题文章【"+temp.getTitle()+"】成功！");
        logger.info("delete product return data => {}", vo);
        return vo;
    }

    @ResponseBody
    @RequestMapping("/{id}/changeStatus")
    public MessageVo changeStatus(@PathVariable("id") long id, int status){
        logger.info("article  => {}, old status => {} changeStatus.", id, status);
        MessageVo vo = null;
        try{
            articleService.updateStatus(id, status);
            vo = new MessageVo(Constant.SUCCESS_CODE, "修改状态成功！");
            logger.error("article => {}, old status => {} changeStatus success.", id, status);
        }catch (Exception e){
            logger.error("article => {}, old status => {} changeStatus fail.", id, status, e);
            vo = new MessageVo(Constant.ERROR_CODE, "修改状态失败！");
        }
        logger.info("article => {} change status return data => {}", id, vo);
        return vo;
    }

    /**
     * 发布主题文章
     * 1、先判断文章的状态是否为正常，如果正常才继续往下处理
     * 2、修改发布时间、更新时间为当前时间
     * @param id 主题文章id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{id}/release", method = RequestMethod.POST)
    public MessageVo releaseArticle(@PathVariable("id") long id){
        long start = System.currentTimeMillis();
        MessageVo vo = null;
        try{
            Article article = articleService.loadOne(id);
            if(article.getStatus() != Constant.C_ONE){
                vo = new MessageVo(Constant.ERROR_CODE, "该主题文章当前状态不支持发布！");
                logger.info("release article return data => {}", vo);
                return vo;
            }
            article.setUpdateTime(TimeUtils.getTime());
            article.setPublishTime(TimeUtils.getTime());
            articleService.publishArticle(article);
            vo = new MessageVo(Constant.SUCCESS_CODE, "发布主题文章成功！");
        }catch (Exception e){
            logger.error("release article id => {} fail.", id, e);
            vo = new MessageVo(Constant.ERROR_CODE, "发布主题文章失败！");
        }
        logger.info("release article return data => {}, cost {} ms.", vo, System.currentTimeMillis() - start);
        return vo;
    }

    @ResponseBody
    @RequestMapping(value = "/releaseAll", method = RequestMethod.POST)
    public MessageVo releaseAll(){
        long start = System.currentTimeMillis();
        MessageVo vo = null;
        try{
            List<Article> articles = articleService.findAllNormal(0);
            if(CollectionUtils.isEmpty(articles)){
                vo = new MessageVo(Constant.ERROR_CODE, "未查到正常状态的主题文章！");
                logger.info("release all normal status articles return data => {}", vo);
                return vo;
            }
            for(Article article : articles){
                article.setUpdateTime(TimeUtils.getTime());
                article.setPublishTime(TimeUtils.getTime());
                articleService.publishArticle(article);
            }
            articles = articleService.findAllNormal(1);
            if(!CollectionUtils.isEmpty(articles)){
                articleService.saveToRedis(articles);
            }
            vo = new MessageVo(Constant.SUCCESS_CODE, "一键发布主题文章成功！");
        }catch (Exception e){
            logger.error("release all normal status articles fail.", e);
            vo = new MessageVo(Constant.ERROR_CODE, "一键发布主题文章失败！");
        }
        logger.info("release all normal status articles return data => {}, cost {} ms.", vo, System.currentTimeMillis() - start);
        return vo;
    }

    @ResponseBody
    @RequestMapping(value = "/reflush", method = RequestMethod.POST)
    public MessageVo reflush(){
        long start = System.currentTimeMillis();
        MessageVo vo = null;
        try{
            List<Article> articles = articleService.findAllNormal(1);
            if(!CollectionUtils.isEmpty(articles)){
                articleService.saveToRedis(articles);
                //每个分类下的文章列表写入redis
                List<ArticleCategory> categorys=  articleCategoryService.findList(1);
                for(ArticleCategory category : categorys){
                    articleService.findArticles(new ArticleQueryVo(1, 1, category.getId()));
                }
            }else{
                logger.warn("reflush articles redis, but not found normal articles.");
            }
            vo = new MessageVo(Constant.SUCCESS_CODE, "重新刷入redis成功！");
        }catch(Exception e){
            logger.error("reflush articles redis fail.", e);
            vo = new MessageVo(Constant.ERROR_CODE, "重新刷入redis失败！");
        }
        logger.info("refulsh articles redis return data => {}, cost {} ms", vo, System.currentTimeMillis() - start);
        return vo;
    }

}
