package top.zuishare.util;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.zuishare.dao.ArticleDao;
import top.zuishare.service.ArticleViewNumToDB;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author niange
 * @ClassName: ScheduleUtil
 * @desp:  定时执行任务
 * @date: 2018/1/4 下午11:10
 * @since JDK 1.7
 */
public class ScheduleUtil {

    private final static Logger logger = LoggerFactory.getLogger(ScheduleUtil.class);
    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);
    private static ArticleViewNumToDB articleViewNumToDB;

    /**
     * 以固定周期频率执行任务
     */
    public static void executeFixedRate(StringRedisTemplate stringRedisTemplate, ArticleDao articleDao, Gson gson) {
        logger.info("start fixed rate job...");
        scheduledExecutorService.scheduleAtFixedRate(
                new ArticleViewNumToDB(stringRedisTemplate,articleDao,gson),
                1,
                12,
                TimeUnit.HOURS);
    }

    /**
     * 以固定延迟时间进行执行
     * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
     */
    public static void executeFixedDelay(StringRedisTemplate stringRedisTemplate, ArticleDao articleDao, Gson gson) {
        logger.info("start fixed delay job...");
        scheduledExecutorService.scheduleWithFixedDelay(
                new ArticleViewNumToDB(stringRedisTemplate,articleDao,gson),
                5,
                10,
                TimeUnit.SECONDS);
    }

    /**
     * 应用启动，执行消费redis队列任务
     * @param stringRedisTemplate
     * @param articleDao
     * @param gson
     */
    public static void executeArticleViewNumTask(StringRedisTemplate stringRedisTemplate, ArticleDao articleDao, Gson gson){
        articleViewNumToDB = new ArticleViewNumToDB(stringRedisTemplate,articleDao,gson);
        executorService.execute(articleViewNumToDB);
    }

    public static void closeResource(){
        if(articleViewNumToDB != null){
            articleViewNumToDB.setRunning(false);
        }
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
        }
        if(executorService != null){
            executorService.shutdown();
        }

    }

}
