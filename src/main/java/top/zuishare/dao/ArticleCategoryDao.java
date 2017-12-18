package top.zuishare.dao;

import org.apache.ibatis.annotations.Param;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.ArticleCategory;

import java.util.List;

/**
 * @author niange
 * @ClassName: ArticleCategoryDao
 * @desp:
 * @date: 2017/12/2 下午11:11
 * @since JDK 1.7
 */
public interface ArticleCategoryDao {
    long findAllCount(RequestParam parma);
    List<ArticleCategory> findList(RequestParam param);
    List<ArticleCategory> findListByStatus(int status);
    void save(ArticleCategory category);
    ArticleCategory getById(int id);
    void update(ArticleCategory category);
    void updateStatus(@Param("id") int id,@Param("newStatus") int newStatus);
}
