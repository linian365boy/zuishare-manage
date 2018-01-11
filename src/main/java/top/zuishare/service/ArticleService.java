package top.zuishare.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.zuishare.dao.ArticleDao;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Article;
import top.zuishare.util.Constant;
import top.zuishare.util.PageRainier;
import top.zuishare.util.RedisUtil;
import top.zuishare.vo.ArticleQueryVo;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author niange
 * @ClassName: ArticleService
 * @desp:
 * @date: 2017/12/2 下午10:03
 * @since JDK 1.7
 */
@Service
public class ArticleService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private Gson gson;

    private final static Logger logger = LoggerFactory.getLogger(ArticleService.class);

    public PageRainier<Article> findAll(RequestParam param){
        long count = articleDao.findAllCount(param);
        PageRainier<Article> page = new PageRainier<>(count);
        page.setResult(articleDao.findList(param));
        return page;
    }

    public void save(Article article){
        articleDao.save(article);
    }


    public Article loadOne(long id){
        return articleDao.getById(id);
    }

    public long countByCateId(int categoryId){
        return articleDao.countByCateId(categoryId);
    }

    public void update(Article article){
        articleDao.update(article);
        if(article.getStatus() == Constant.C_ZERO && article.getPublishTime() > 0){
            //状态为锁定并且发布过的主题文章修改，删除缓存
            stringRedisTemplate.delete(RedisUtil.getAllPublishArticlesKey());
            stringRedisTemplate.delete(RedisUtil.getHotArticlesKey());
            stringRedisTemplate.delete(RedisUtil.getArticleKey(article.getId()));
            logger.warn("delete articles and hot article and article key " +
                    "and article viewNum key and article => {} redis key.", article.getId());
        }
    }

    public void updateStatus(long id, int status){
        int newStatus = status;
        Article tempArticle = articleDao.getById(id);
        if(status == Constant.C_ZERO){
            newStatus = Constant.C_ONE;
            //加入缓存
            stringRedisTemplate.opsForValue().set(RedisUtil.getArticleKey(id), gson.toJson(tempArticle));
        }else{
            newStatus = Constant.C_ZERO;
            //正常状态变为锁定时，删除缓存
            stringRedisTemplate.delete(RedisUtil.getAllPublishArticlesKey());
            stringRedisTemplate.delete(RedisUtil.getHotArticlesKey());
            //删除缓存
            stringRedisTemplate.delete(RedisUtil.getArticleKey(id));
            logger.warn("delete articles and hot article and article and article viewNum redis key.");
        }
        articleDao.updateStatus(id, newStatus);
    }

    public void publishArticle(Article article){
        articleDao.publishArticle(article);
        //发布成功之后，删除缓存
        stringRedisTemplate.delete(RedisUtil.getAllPublishArticlesKey());
        //发布成功之后，加入redis
        stringRedisTemplate.opsForValue().set(RedisUtil.getArticleKey(article.getId()), gson.toJson(article));
        logger.warn("delete articles redis key.");
    }

    /**
     * 查询正常状态 + 是否发布过的文章
     * @param publishOrNo 是否发布过的文章  1：查询发布过的文章，0：未发布过的文章
     * @return
     */
    public List<Article> findAllNormal(int publishOrNo){
        return articleDao.findAllNormal(publishOrNo);
    }

    public void saveToRedis(List<Article> articles){
        stringRedisTemplate.opsForValue().set(RedisUtil.getAllPublishArticlesKey(), gson.toJson(articles),
                Constant.TIMEOUTDAYS, TimeUnit.DAYS);
    }

    public List<Article> findArticles(ArticleQueryVo vo){
        return articleDao.findArticles(vo);
    }
}
