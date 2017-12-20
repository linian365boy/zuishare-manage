package top.zuishare.dao;

import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Article;

import java.util.List;

/**
 * @author niange
 * @ClassName: ArticleDao
 * @desp:
 * @date: 2017/12/2 下午10:18
 * @since JDK 1.7
 */
public interface ArticleDao {

    long findAllCount(RequestParam param);

    List<Article> findList(RequestParam param);

    void save(Article article);

    Article getById(long id);

    long countByCateId(int categoryId);

    void update(Article article);

    void updateStatus(long id, int newStatus);
}
