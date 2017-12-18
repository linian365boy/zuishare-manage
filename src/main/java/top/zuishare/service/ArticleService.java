package top.zuishare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zuishare.dao.ArticleDao;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Article;
import top.zuishare.util.PageRainier;

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
    }

}
