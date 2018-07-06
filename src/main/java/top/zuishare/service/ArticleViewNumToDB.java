package top.zuishare.service;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.gson.Gson;

import top.zuishare.dao.ArticleDao;
import top.zuishare.spi.model.Article;
import top.zuishare.spi.util.RedisUtil;
import top.zuishare.util.Constant;

/**
 * @author niange
 * @ClassName: ArticleViewNumToDB
 * @desp:
 * @date: 2018/1/4 下午11:14
 * @since JDK 1.7
 */
public class ArticleViewNumToDB implements Runnable {

    private StringRedisTemplate stringRedisTemplate;
    private ArticleDao articleDao;
    private Gson gson;
    private volatile boolean isRunning = true;

    private static final Logger logger = LoggerFactory.getLogger(ArticleViewNumToDB.class);

    public ArticleViewNumToDB(StringRedisTemplate stringRedisTemplate, ArticleDao articleDao, Gson gson){
        this.stringRedisTemplate = stringRedisTemplate;
        this.articleDao = articleDao;
        this.gson = gson;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        while(isRunning){
            try {
                String idAndViewNums = stringRedisTemplate.opsForList().rightPop(RedisUtil.getArticleViewNumKey());
                if(StringUtils.isBlank(idAndViewNums)){
                    TimeUnit.SECONDS.sleep(5);
                    continue;
                }
                String[] idAndViewNumArr = idAndViewNums.split(Constant.KEYDELIMITER);
                long articleId = Long.parseLong(idAndViewNumArr[0]);
                int viewNum = Integer.parseInt(idAndViewNumArr[1]);
                articleDao.updateViewNum(articleId, viewNum);
                //修改缓存中的点击量
                String gsonStr = stringRedisTemplate.opsForValue().get(RedisUtil.getArticleKey(articleId));
                Article article = gson.fromJson(gsonStr, Article.class);
                article.setViewNum(viewNum);
                stringRedisTemplate.opsForValue().set(RedisUtil.getArticleKey(articleId), gson.toJson(article));
                logger.info("from redis list pop viewNum => {} success.", idAndViewNums);
            }catch (Exception e){
                logger.error("from redis list pop viewNum write to db error.", e);
            }
        }
    }
}
