package top.zuishare.dao;

import org.apache.ibatis.annotations.Param;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Category;

import java.util.List;

public interface CategoryDao {
	
	List<Category> findParentByAjax();

	Category loadCateByName(@Param("enName") String enName);

	long checkHasChildren(@Param("categoryId") Integer categoryId);

	List<Category> findCate(@Param("colId") Integer colId);

	List<Category> findAllParentCateList();

	List<Object[]> findChildrenByParentCateId(@Param("parentCateId") int parentCateId);

	long findAllCount(RequestParam param);
	
	List<Category> findList(RequestParam param);
	
	Category findOneById(Integer categoryId);
	
	void save(Category temp);
	
	void delete(Integer categoryId);
	
	long countByEname(String enName);
	
	Category findOneByEnName(String enName);
	
	List<Category> findAll();
	
	void updateCategory(Category category);
}
