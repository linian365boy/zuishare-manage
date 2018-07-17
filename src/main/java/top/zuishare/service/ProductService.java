package top.zuishare.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import top.zuishare.dao.ProductDao;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Product;
import top.zuishare.spi.util.RedisUtil;
import top.zuishare.util.PageRainier;
import top.zuishare.util.RedisHelper;

import java.util.List;

@Component("productService")
public class ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	@Autowired
    private Gson gson;
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	public PageRainier<Product> findAll(RequestParam param) {
		long count = productDao.findAllCount(param);
		PageRainier<Product> page = new PageRainier<Product>(count);
		page.setResult(productDao.findList(param));
		return page;
	}

	public Product loadProductById(Integer productId) {
		return productDao.findOne(productId);
	}

	public void saveProduct(Product product) {
		productDao.save(product);
		//set to redis
		stringRedisTemplate.opsForValue().set(RedisUtil.getProductDetailKey(product.getId()), gson.toJson(product));
		long autoId = stringRedisTemplate.opsForValue().increment(RedisUtil.getGenerateIncreaseKey(), 1);
		stringRedisTemplate.opsForZSet().add(RedisUtil.getProductsKey(), String.valueOf(product.getId()),
				RedisHelper.getZsetScore(product.getPriority(), autoId));
	}

	public boolean delProduct(Integer productId) {
		boolean flag = false;
		try{
			productDao.delete(productId);
			flag = true;
			//delete from redis
			stringRedisTemplate.delete(RedisUtil.getProductDetailKey(productId));
			stringRedisTemplate.opsForZSet().remove(RedisUtil.getProductsKey(), String.valueOf(productId));
		}catch(Exception e){
			logger.error("删除产品失败！",e);
		}
		return flag;
	}
	
	/**
	 * findIndexPic:首页展示热门产品
	 * @author tanfan 
	 * @param pageSize
	 * @return 
	 * @since JDK 1.7
	 */
	public List<Product> findIndexPic(int pageSize){
		return productDao.findIndexPic(pageSize);
	}
	
	/**
	 * 查询栏目下的已发布的，状态正常的产品
	 * @param id
	 * @return
	 */
	public long countByColId(Integer id) {
		return productDao.countByColId(id);
	}

	/**
	 * 供前台查询，分页查询产品
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageRainier<Product> findPageByColId(int pageNo, Integer pageSize,
			Integer colId) {
		long count = productDao.countByColId(colId);
		PageRainier<Product> page = new PageRainier<Product>(count,pageNo,pageSize);
		page.setResult(productDao.findListByColId(colId,(pageNo-1)*pageSize,pageSize));
		return page;
	}
	
	public long countIndexByColId(Integer id) {
		return productDao.countByColId(id);
	}

	public long countByCateId(int cateId) {
		return productDao.countByCateId(cateId);
	}

	/**
	 * 查询该分类下所有已发布且状态正常的产品
	 * @param pageNo
	 * @param pageSize
	 * @param cateId
	 * @return
	 */
	public PageRainier<Product> findAllByCateId(int pageNo, Integer pageSize,
			Integer cateId) {
		long count = productDao.countByCateId(cateId);
		PageRainier<Product> page = new PageRainier<Product>(count,pageNo,pageSize);
		page.setResult(productDao.findAllListByCateId(cateId,(pageNo-1)*pageSize,pageSize));
		return page;
	}

	public boolean updateStatus(Integer id, boolean status) {
		if(status){
			status = false;
		}else{
			status = true;
		}
		boolean flag = true;
		try{
			productDao.updateStatus(id,status);
			//set to redis
			stringRedisTemplate.opsForValue().set(RedisUtil.getProductDetailKey(id), gson.toJson(productDao.findOne(id)));
		}catch(Exception e){
			logger.error("修改产品的状态失败！",e);
			flag = false;
		}
		return flag;
	}

	public List<Product> findRelatedProducts(Integer id, String keyWords, int maxSize) {
		/*if(!StringUtils.isBlank(keyWords)){
			String[] kws = keyWords.split(";");
			List<Product> ps = new ArrayList<Product>();
			List<Product> tps = new ArrayList<Product>();
			List<Product> temp = null;
			for(String kw : kws){
				temp = productDao.findAll(findRelatedProductsSpec(id,kw),new Sort(Direction.DESC,"priority","hot","id"));
				if(CollectionUtils.isNotEmpty(temp)){
					if(temp.size()>=maxSize){
						ps.addAll(temp.subList(0, maxSize-1));
						break;
					}else{
						ps.addAll(temp);
					}
				}
			}
			Set<Integer> tpId = new HashSet<Integer>();
			for(Product p : ps){
				if(tpId.contains(p.getId())){
					continue;
				}
				tpId.add(p.getId());
				tps.add(p);
			}
			ps.clear();
			tpId.clear();
			return tps;
		}*/
		return null;
	}
	
	/*private Specification<Product> findRelatedProductsSpec(final Integer id, final String kw) {
		return new Specification<Product>(){
			@Override
			public Predicate toPredicate(Root<Product> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.like(root.<String>get("keyWords"), '%'+kw+'%'),
						cb.notEqual(root.<Integer>get("id"), id));
			}
		};
	}*/

	public PageRainier<Product> findAllReleaseProductByLikeKeyword(RequestParam param) {
		logger.info("findAllReleaseProductByLikeKeyword request param => {}", param);
		long count = productDao.countAllReleaseProductByLikeKeyword(param);
		PageRainier<Product> page = new PageRainier<Product>(count);
		page.setResult(productDao.findAllReleaseProductByLikeKeyWordList(param));
        logger.info("findAllReleaseProductByLikeKeyword return data => {}", count);
		return page;
	}

	public void updateProduct(Product product) {
		productDao.updateProduct(product);
		stringRedisTemplate.opsForValue().set(RedisUtil.getProductDetailKey(product.getId()), gson.toJson(product));
		
	}

	/*private Specification<Product> findAllReleaseProductByLikeKeywordSpec(final String keyword) {
		return new Specification<Product>(){
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(StringUtils.isNotBlank(keyword)){
					return cb.like(root.<String>get("enName"), '%'+keyword+'%');
				}else{
					return null;
				}
			}
		};
	}*/
}
