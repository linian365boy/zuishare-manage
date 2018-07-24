package top.zuishare.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import top.zuishare.dao.NewsDao;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.News;
import top.zuishare.spi.util.RedisUtil;
import top.zuishare.util.PageRainier;

@Component("newsService")
public class NewsService {
	@Autowired
	private NewsDao newsDao;
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	@Autowired
    private Gson gson;
	private static Logger logger = LoggerFactory.getLogger(NewsService.class);
	
	public PageRainier<News> findAll(RequestParam param) {
		long count = newsDao.findAllCount(param);
		PageRainier<News> page = new PageRainier<News>(count);
		page.setResult(newsDao.findList(param));
		return page;
	}
	
	public boolean saveNews(News news) {
		boolean flag = false;
		try {
			newsDao.save(news);
			flag = true;
		} catch (Exception e) {
			logger.error("添加新闻报错",e);
		}
		return flag;
	}
	public News loadNews(Integer id){
		return newsDao.findOne(id);
	}

	public boolean delNews(Integer newsId) {
		try{
			newsDao.delete(newsId);
			//delete from redis
			stringRedisTemplate.delete(RedisUtil.getNewsDetailKey(newsId));
			stringRedisTemplate.opsForZSet().remove(RedisUtil.getNewsKey(), String.valueOf(newsId));
			return true;
		}catch(Exception e){
			logger.error("删除新闻报错",e);
		}
		return false;
	}

	public void updateClicks(News news) {
		try{
			newsDao.updateClicks(news.getId(),news.getClicks()+1);
			//set to redis
			stringRedisTemplate.opsForValue().set(RedisUtil.getNewsDetailKey(news.getId()), gson.toJson(news));
		}catch(Exception e){
			logger.error("update新闻报错",e);
		}
	}

	
	/**
	 * 根据栏目id，查询该栏目下所有的文章，包括子栏目下的文章
	 * @param colId
	 * @param depth
	 * @return
	 */
	public long countByColId(Integer colId, int depth) {
		return newsDao.countByColId(colId,depth);
	}

	public PageRainier<News> findAllByColId(RequestParam param,
											Integer colId, int depth) {
		long count = newsDao.findAllCountByColId(colId,depth);
		PageRainier<News> page = new PageRainier<News>(count);
		page.setResult(newsDao.findList(param));
		return page;
	}

	public List<News> findIndexPic(int indexNewsSize) {
		return newsDao.findIndexNews(indexNewsSize);
	}

	public boolean updateNews(News news) {
		try{
			newsDao.updateNews(news);
			//set to redis
			if(news.getPublishDate() != null) {
				stringRedisTemplate.opsForValue().set(RedisUtil.getNewsDetailKey(news.getId()), gson.toJson(news));
				stringRedisTemplate.opsForZSet().add(RedisUtil.getNewsKey(), String.valueOf(news.getId()), news.getPriority() * 1.0);
			}else{
				stringRedisTemplate.delete(RedisUtil.getNewsDetailKey(news.getId()));
				stringRedisTemplate.opsForZSet().remove(RedisUtil.getNewsKey(), String.valueOf(news.getId()));
			}
			return true;
		}catch(Exception e){
			logger.error("删除新闻报错",e);
		}
		return false;
	}

	public void newsToRedis(){
		List<News> news = newsDao.findAllPublishNews();
		Map<String, String> keyValues = new HashMap<>(news.size());
		Set<ZSetOperations.TypedTuple<String>> tuplesSet = new HashSet<>(news.size());
		for (News n : news) {
			if (n.getImgTitlePath() == null) {
				n.setImgTitlePath("images/defaultImgTitle.jpg");
			}
			keyValues.put(RedisUtil.getNewsDetailKey(n.getId()), gson.toJson(n));
			tuplesSet.add(new DefaultTypedTuple<>(String.valueOf(n.getId()), n.getPriority() * 1.0));
		}
		stringRedisTemplate.opsForValue().multiSet(keyValues);
		stringRedisTemplate.opsForZSet().add(RedisUtil.getNewsKey(), tuplesSet);
	}

}
