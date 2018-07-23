package top.zuishare.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.zuishare.service.ColumnService;
import top.zuishare.service.NewsService;
import top.zuishare.service.SystemConfig;
import top.zuishare.spi.dto.ConstantVariable;
import top.zuishare.spi.dto.LogType;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Column;
import top.zuishare.spi.model.News;
import top.zuishare.spi.util.Tools;
import top.zuishare.util.*;
import top.zuishare.vo.MessageVo;
import top.zuishare.vo.ReturnData;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/news")
@Scope("prototype")
public class NewsController {
	@Autowired
	private NewsService newsService;
	@Autowired
	private ColumnService columnService;
	@Autowired
	private SystemConfig systemConfig;
	@Autowired
	private LogUtil logUtil;
	private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
	
	@RequestMapping({"/news/list"})
	public String list(HttpServletRequest request,ModelMap map){
		map.put("ajaxListUrl", "admin/news/news/getJsonList");
		return "admin/news/list";
	}
	
	@ResponseBody
	@RequestMapping({"/news/getJsonList"})
	public ReturnData<News> getJsonList(RequestParam param){
		PageRainier<News> news = newsService.findAll(param);
		ReturnData<News> datas = new ReturnData<News>(news.getTotalRowNum(), news.getResult());
		return datas;
	}
	
	@RequestMapping(value="/add",method= RequestMethod.GET)
	public String add(ModelMap map){
		return "admin/news/add";
	}
	
	@ResponseBody
	@RequestMapping(value="/add",method= RequestMethod.POST)
	public MessageVo add(MultipartFile photo, News news, Integer firstColId, Integer secondColId, Integer thirdColId) {
		logger.info("add news firstColId => {}, secondColId => {}, thirdColId => {}, photo => {}", firstColId, secondColId, thirdColId, photo);
		MessageVo vo = null;
		try{
			if (photo != null && !photo.isEmpty()) {
				//picPath前面的file:不需要
				String realPath = systemConfig.getPicPath().substring(5) + Constant.ARTICLE_PIC_TITLE_PRE;
				String newFileName = realPath + File.separator + Tools.getRndFilename() + Tools.getExtname(photo.getOriginalFilename());
				FileUtils.copyInputStreamToFile(photo.getInputStream(), new File(newFileName));
				String url = newFileName.substring(realPath.lastIndexOf(Constant.UPLOAD_PRE));
				news.setImgTitlePath(url);
			}
		}catch (Exception e){
			logger.error("upload news image title error.", e);
			news.setImgTitlePath(Constant.DEFAULT_NEWS_PIC_TITLE);
		}
		news.setClicks(0);
		news.setCreateDate(new Date());
		news.setUrl(Tools.getRndFilename()+".htm");
		if(thirdColId != null && thirdColId !=0 ){
			news.setColumnId(thirdColId);
			news.setDepth(firstColId+"-"+secondColId+"-"+thirdColId);
		}else if(secondColId !=null && secondColId !=0 ){
			news.setColumnId(secondColId);
			news.setDepth(firstColId+"-"+secondColId);
		}else{
			news.setColumnId(firstColId);
			news.setDepth(String.valueOf(firstColId));
		}
		String bref = Jsoup.parse(news.getContent()).text();
        if(StringUtils.isNotBlank(bref)) {
            if(bref.length() > systemConfig.getLimitSize()) {
            	news.setBref(bref.substring(0, systemConfig.getLimitSize()));
            }else{
            	news.setBref(bref);
            }
        }else{
        	news.setBref(news.getContent());
        }
		if(newsService.saveNews(news)){
			logUtil.log(LogType.ADD,"标题："+news.getTitle());
			logger.info("add news => {} succeed.",news);
			vo = new MessageVo(Constant.SUCCESS_CODE,"添加新闻"+news.getTitle()+"成功");
		}else{
			vo = new MessageVo(Constant.ERROR_CODE,"添加新闻"+news.getTitle()+"失败");
		}
		logger.info("add news return data => {}", vo);
		return vo;
	}
	
	@RequestMapping(value="/{newsId}/update",method= RequestMethod.GET)
	public String update(@PathVariable Integer newsId, ModelMap map){
		News news = newsService.loadNews(newsId);
		if (news.getImgTitlePath() == null ){
			news.setImgTitlePath("/resources/images/defaultImgTitle.jpg");
		}
		map.put("news", news);
		return "admin/news/update";
	}
	
	@ResponseBody
	@RequestMapping(value="/{newsId}/update",method= RequestMethod.POST)
	public MessageVo update(MultipartFile photo, HttpServletRequest request,
                            @PathVariable Integer newsId, News news){
        logger.info("update news data => {}, photo => {}", news, photo);
		MessageVo vo = null;
		News temp = newsService.loadNews(newsId);
		try{
			if (photo != null && !photo.isEmpty()) {
				//picPath前面的file:不需要
				String realPath = systemConfig.getPicPath().substring(5) + Constant.ARTICLE_PIC_TITLE_PRE;
				String newFileName = realPath + File.separator + Tools.getRndFilename() + Tools.getExtname(photo.getOriginalFilename());
				FileUtils.copyInputStreamToFile(photo.getInputStream(), new File(newFileName));
				String url = newFileName.substring(realPath.lastIndexOf(Constant.UPLOAD_PRE));
				news.setImgTitlePath(url);
				//old image delete
				if(temp.getImgTitlePath()!= null && !temp.getImgTitlePath().equals(Constant.DEFAULT_NEWS_PIC_TITLE)) {
					FileUtil.delFile(realPath + temp.getImgTitlePath().substring(Constant.UPLOAD_ARTICLE_PIC_TITLE.length()));
				}
			}else {
				news.setImgTitlePath(temp.getImgTitlePath());
			}
		}catch (Exception e){
			logger.error("upload news image title error.", e);
			news.setImgTitlePath(Constant.DEFAULT_NEWS_PIC_TITLE);
		}

		StringBuilder content = new StringBuilder();
		news.setCreateDate(temp.getCreateDate());
		news.setClicks(temp.getClicks());
		news.setUrl(temp.getUrl());
		news.setPublishDate(temp.getPublishDate());

		String bref = Jsoup.parse(news.getContent()).text();
		if(StringUtils.isNotBlank(bref)) {
			if(bref.length() > systemConfig.getLimitSize()) {
				news.setBref(bref.substring(0, systemConfig.getLimitSize()));
			}else{
				news.setBref(bref);
			}
		}else{
			news.setBref(news.getContent());
		}
		if(newsService.updateNews(news)){
			logger.info("update from news => {}, to => {}", temp, news);
			if(!temp.getTitle().equals(news.getTitle())){
				content.append("标题由\""+temp.getTitle()+"\"修改为\""+news.getTitle()+"\"");
			}
			if(temp.getPriority() != news.getPriority()){
				content.append("优先值由\""+temp.getPriority()+"\"修改为\""+news.getPriority()+"\"");
			}
			if("".equals(content.toString().trim())){
				content.append("修改了标题为"+news.getTitle()+"新闻");
			}
			logUtil.log(LogType.EDIT, content.toString());
			//删除页面
			String path = request.getSession().getServletContext().getRealPath("/");
			Tools.delFile(path + Constant.NEWSPATH + File.separator+news.getUrl());
			Tools.delFile(path + Constant.NEWSPRE + File.separator+news.getId()+".htm");
			vo = new MessageVo(Constant.SUCCESS_CODE,"修改新闻【"+news.getTitle()+"】成功");
		}else{
			vo = new MessageVo(Constant.ERROR_CODE,"修改新闻【"+news.getTitle()+"】失败");
		}
        logger.info("update news return data => {}", vo);
		return vo;
	}
	
	@RequestMapping(value="/{newsId}",method= RequestMethod.GET)
	public String view(@PathVariable Integer newsId, Model model){
		if(newsId!=null){
			News tempNews = newsService.loadNews(newsId);
			//点击率
			newsService.updateClicks(tempNews);
			if(tempNews!=null){
				model.addAttribute("news", tempNews);
			}
		}
		return "admin_unless/news/details";
	}
	
	@ResponseBody
	@RequestMapping(value="/{newsId}/del",method= RequestMethod.GET)
	public MessageVo del(@PathVariable Integer newsId, HttpServletRequest request, News news){
	    logger.info("delete news param => {}", news);
		MessageVo vo = null;
		if(newsId!=null){
			news = newsService.loadNews(newsId);
			String htmlUrl = news.getUrl();
			if(newsService.delNews(newsId)){
				if(htmlUrl!=null){
					String path = request.getSession().getServletContext().getRealPath("/"+htmlUrl);
					Tools.delFile(path);
				}
				logger.warn("删除了新闻|{}",news);
				vo = new MessageVo(Constant.SUCCESS_CODE,"删除新闻【"+news.getTitle()+"】成功！");
			}else{
				vo = new MessageVo(Constant.ERROR_CODE,"删除新闻【"+news.getTitle()+"】失败！");
			}
			logUtil.log(LogType.DEL, "标题："+news.getTitle());
		}
        logger.info("delete news return data => {}", vo);
		return vo;
	}
	
	@RequestMapping(value="/{newsId}/checkPub",method= RequestMethod.GET)
	@ResponseBody
	public String checkPub(@PathVariable Integer newsId){
		if(newsId!=null){
			News tempNews = newsService.loadNews(newsId);
			if(tempNews!=null){
				if(tempNews.getPublishDate()!=null){
					return "1";
				}else{
					return "-1";
				}
			}else{
				return "0";
			}
		}else{
			return "0";
		}
	}
	
	@RequestMapping(value="/{newsId}/release",method= RequestMethod.GET)
	@ResponseBody
	public MessageVo releaseNews(@PathVariable Integer newsId, HttpServletRequest request, ModelMap map){
	    logger.info("release news newsId => {}", newsId);
		MessageVo vo = new MessageVo();
		News tempNews = newsService.loadNews(newsId);
		tempNews.setPublishDate(new Date());
		if(StringUtils.isBlank(tempNews.getUrl())){
			tempNews.setUrl(Tools.getRndFilename()+".htm");
		}
		logUtil.log(LogType.PUBLISH, "标题："+tempNews.getTitle());
		map.put("model", tempNews);
		if(newsService.updateNews(tempNews)) {
			vo.setCode(Constant.SUCCESS_CODE);
			vo.setData(DateFormatUtils.format(new Date(), ConstantVariable.DFSTR));
			logger.info("release news => {} succeed.", tempNews);
		}
        logger.info("release news return data => {}", vo);
		return vo;
	}

	/**
	 * 一键把所有发布且正常的新闻放入detail redis，并且也放入zset，按照顺序
	 * @return
	 */
	@RequestMapping(value = "/newsToRedis", method = RequestMethod.GET)
	@ResponseBody
	public String productToRedis() {
		long start = System.currentTimeMillis();
		try {
			newsService.newsToRedis();
		}catch (Exception e){
			return "Fail";
		}
		logger.info("news to redis cost time => {} ms", System.currentTimeMillis() - start);
		return "Success";
	}

}
