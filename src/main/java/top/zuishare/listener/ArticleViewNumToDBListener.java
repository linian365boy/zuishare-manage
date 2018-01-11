package top.zuishare.listener;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import top.zuishare.dao.ArticleDao;
import top.zuishare.util.ScheduleUtil;

/**
 * @author niange
 * @ClassName: ArticleViewNumToDBListener
 * @desp:   消费redis 队列中的数据，文章点击量数据落地
 * @date: 2018/1/4 下午11:25
 * @since JDK 1.7
 */
@Component
public class ArticleViewNumToDBListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ArticleViewNumToDBListener.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private Gson gson;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        final ApplicationContext app = contextRefreshedEvent.getApplicationContext();
        if (null == app.getParent() || null == app.getParent().getParent()){
            //启动执行任务执行
            logger.info("start article viewNum write to db task...");
            ScheduleUtil.executeArticleViewNumTask(stringRedisTemplate,articleDao,gson);
        }
    }
}
