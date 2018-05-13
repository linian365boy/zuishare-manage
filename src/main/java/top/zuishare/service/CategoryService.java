package top.zuishare.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zuishare.dao.CategoryDao;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Category;
import top.zuishare.util.PageRainier;

import java.util.List;

@Service("categoryService")
public class CategoryService {
	@Autowired
	private CategoryDao categoryDao;
	private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

	public PageRainier<Category> findAll(RequestParam param) {
		long count = categoryDao.findAllCount(param);
		PageRainier<Category> page = new PageRainier<Category>(count);
		page.setResult(categoryDao.findList(param));
		return page;
	}

	public List<Category> findParentByAjax() {
		return this.categoryDao.findParentByAjax();
	}
	
	public List<Object[]> findChildrenByParentCateId(int parentCateId){
		return categoryDao.findChildrenByParentCateId(parentCateId);
	}

	public Category loadCategoryById(Integer categoryId) {
		return categoryDao.findOneById(categoryId);
	}

	public void saveCategory(Category temp) {
		categoryDao.save(temp);
	}

	public Category loadCategoryByName(String enName) {
		return categoryDao.loadCateByName(enName);
	}

	public void delCategory(Integer categoryId) {
		categoryDao.delete(categoryId);
	}

	public boolean checkHasChildren(Integer cateId) {
		return categoryDao.checkHasChildren(cateId)>0?true:false;
	}
	/**
	 * 根据英文名称查询分类是否存在
	 * @param enName
	 * @return
	 */
	public long countByCateEname(String enName) {
		return categoryDao.countByEname(enName);
	}

	/*private Specification<Category> countSpec(final String enName) {
		return new Specification<Category>(){
			@Override
			public Predicate toPredicate(Root<Category> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.<String>get("enName"), enName);
			}
		};
	}*/

	public Category loadCategoryByEname(String enName) {
		return categoryDao.findOneByEnName(enName);
	}

	public List<Category> findCateByColId(Integer id) {
		return categoryDao.findCate(id);
	}

	public List<Category> findList() {
		return categoryDao.findAll();
	}
	
	public List<Category> findAllParentCateList() {
		return categoryDao.findAllParentCateList();
	}

	public boolean updateCategory(Category category) {
		boolean flag = false;
		try{
			categoryDao.updateCategory(category);
			flag = true;
		}catch(Exception e){
			logger.info("修改分类报错！",e);
			flag = false;
		}
		return flag;
	}

}
