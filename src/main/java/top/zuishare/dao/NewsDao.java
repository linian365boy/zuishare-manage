package top.zuishare.dao;

import org.apache.ibatis.annotations.Param;
import top.zuishare.model.News;
import top.zuishare.spi.dto.request.RequestParam;

import java.util.List;

public interface NewsDao {
	//@Modifying
	//@Query("update News set clicks = ?2 where id = ?1")
	public void updateClicks(@Param("newsId") Integer newsId, @Param("clicks") Integer clicks);

	public void save(News news);

	public News findOne(Integer id);

	public void delete(Integer newsId);

	public void save(List<News> tempNewsList);

	public long findAllCount(RequestParam param);

	public List<News> findList(RequestParam param);

	public List<News> findIndexNews(int indexNewsSize);

	public long findAllCountByColId(@Param("colId") Integer colId, @Param("depth") int depth);

	public long countByColId(@Param("colId") Integer colId, @Param("depth") int depth);

	public void updateNews(News news);

}
