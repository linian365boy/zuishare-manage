package top.zuishare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zuishare.dao.ArticleCategoryDao;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Article;
import top.zuishare.spi.model.ArticleCategory;
import top.zuishare.util.Constant;
import top.zuishare.util.PageRainier;

import java.util.List;

/**
 * @author niange
 * @ClassName: ArticleCategoryService
 * @desp:
 * @date: 2017/12/2 下午11:08
 * @since JDK 1.7
 */
@Service
public class ArticleCategoryService {

    @Autowired
    private ArticleCategoryDao articleCategoryDao;

    public PageRainier<ArticleCategory> findAllList(RequestParam param){
        long count = articleCategoryDao.findAllCount(param);
        PageRainier<ArticleCategory> page = new PageRainier<>(count);
        page.setResult(articleCategoryDao.findList(param));
        return page;
    }

    public void save(ArticleCategory category){
        articleCategoryDao.save(category);
    }

    public ArticleCategory loadOne(int id){
        return articleCategoryDao.getById(id);
    }

    public void update(ArticleCategory category){
        articleCategoryDao.update(category);
    }

    public void delCategory(ArticleCategory category){
        category.setStatus(2);
        articleCategoryDao.update(category);
    }

    /**
     * 修改分类的状态
     * @param id 分类id
     * @param status 旧状态
     */
    public void updateStatus(int id, int status){
        int newStatus = status;
        if(status == Constant.C_ZERO){
            newStatus = Constant.C_ONE;
        }else{
            newStatus = Constant.C_ZERO;
        }
        articleCategoryDao.updateStatus(id, newStatus);
    }

    public List<ArticleCategory> findList(int status){
        return articleCategoryDao.findListByStatus(status);
    }
}
