package top.zuishare.dao;

import org.apache.ibatis.annotations.Param;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Product;

import java.util.List;

public interface ProductDao {
	long countByColId(@Param("id") Integer id);

	long countByCateId(@Param("cateId") int cateId);

	List<Product> findAllListByCateId(@Param("cateId") Integer catId);

	void updateStatus(@Param("id") Integer id, @Param("status") boolean status);
	
	Product findOne(Integer productId);
	
	void save(Product product);
	
	void delete(Integer productId);
	
	List<Product> findIndexPic(int pageSize);
	
	void save(List<Product> productList);
	
	List<Product> findList(RequestParam param);
	
	List<Product> findAllReleaseProductByLikeKeyWordList(RequestParam param);
	
	long countAllReleaseProductByLikeKeyword(RequestParam param);
	
	List<Product> findListByColId(@Param("colId") Integer colId, @Param("start") int start, @Param("pageSize") Integer pageSize);
	
	long findAllCount(RequestParam param);
	
	List<Product> findAllListByCateId(@Param("cateId") Integer cateId, @Param("start") int start, @Param("pageSize") Integer pageSize);
	
	void updateProduct(Product product);
	
}
