package top.zuishare.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.zuishare.dao.ColumnDao;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Column;
import top.zuishare.spi.util.RedisUtil;
import top.zuishare.util.PageRainier;

import java.util.List;

@Service("columnService")
public class ColumnService {
	@Autowired
	private ColumnDao columnDao;
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	private static Logger logger = LoggerFactory.getLogger(ColumnService.class);
	
	public Column getById(Integer id){
		return columnDao.findOne(id);
	}
	
	public boolean save(Column column){
		boolean flag = false;
		try{
			columnDao.save(column);
			flag = true;
			// delete redis
			stringRedisTemplate.delete(RedisUtil.getColumnsKey());
		}catch(Exception e){
			logger.error("新增栏目报错",e);
		}
		return flag;
	}
	
	public PageRainier<Column> findAll(RequestParam param){
		long count = columnDao.findAllCount(param);
		PageRainier<Column> page = new PageRainier<Column>(count);
		page.setResult(columnDao.findList(param));
		return page;
	}

	public void delete(Integer id) {
		columnDao.delete(id);
		// delete redis
		stringRedisTemplate.delete(RedisUtil.getColumnsKey());
	}

	public List<Column> findParentByAjax() {
		return this.columnDao.findParentByAjax();
	}

	/**
	 * 根据栏目代码查询是否存在该栏目
	 * @param code　栏目代码
	 * @return
	 */
	public Long countColumnByCode(String code) {
		return columnDao.countColumnByCode(code);
	}

	public Column loadColumnByCode(String code) {
		return columnDao.loadColumnByCode(code);
	}

	public List<Column> findChildrenByParentId(Integer pId) {
		return this.columnDao.findChildrenByParentId(pId);
	}

	public List<Column> findColumnsByDepth() {
		List<Column> colList = columnDao.findParentByAjax();
		return colList;
	}

	public void updateColumnPublishContent(Integer id, Column column) {
		columnDao.updateColumnPublishContent(id,column.getType(),column.isHasNeedForm());
		// delete redis
		stringRedisTemplate.delete(RedisUtil.getColumnsKey());
	}

	public List<Column> findList() {
		return columnDao.findAll();
	}

	public void updateColumn(Column column) {
		columnDao.updateColumn(column);
		// delete redis
		stringRedisTemplate.delete(RedisUtil.getColumnsKey());
	}

}
