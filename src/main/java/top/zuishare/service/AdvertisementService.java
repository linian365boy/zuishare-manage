package top.zuishare.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import top.zuishare.dao.AdvertisementDao;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Advertisement;
import top.zuishare.spi.util.RedisUtil;
import top.zuishare.util.PageRainier;

@Service("advertisementService")
public class AdvertisementService {
	private static final Logger logger = LoggerFactory.getLogger(AdvertisementService.class);
	@Autowired
	private AdvertisementDao advertisementDao;
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	
	public void saveAdvertisement(Advertisement temp) {
		advertisementDao.save(temp);
		// delete to redis
		stringRedisTemplate.delete(RedisUtil.getAdsKey());
	}
	
	public Advertisement loadAdvertisement(Integer id) {
		return advertisementDao.loadAdvertisement(id);
	}
	
	public void delAdvertisement(Integer id) {
		advertisementDao.delete(id);
		// delete to redis
		stringRedisTemplate.delete(RedisUtil.getAdsKey());
	}

	public PageRainier<Advertisement> findAll(RequestParam param) {
		long count = advertisementDao.findAllCount(param);
		PageRainier<Advertisement> page = new PageRainier<Advertisement>(count);
		page.setResult(advertisementDao.findList(param));
		return page;
	}

	public void updateStatus(Integer id, Integer status) {
		if(status==0){
			status = 1;
		}else{
			status = 0;
		}
		advertisementDao.updateStatus(id,status);
		// delete to redis
		stringRedisTemplate.delete(RedisUtil.getAdsKey());
	}

	public List<Advertisement> getIndexAds(int indexAdsSize) {
		logger.info("getIndexAds param => {}", indexAdsSize);
		return advertisementDao.findIndexAds(indexAdsSize);
	}

	public boolean updateAdvertisement(Advertisement ad) {
		boolean flag = false;
		try{
			advertisementDao.updateAdvertisement(ad);
			flag = true;
			// delete to redis
			stringRedisTemplate.delete(RedisUtil.getAdsKey());
		}catch(Exception e){
			logger.error("修改广告信息失败",e);
		}
		return flag;
	}

	/*private Specification<Advertisement> indexAdsSpec() {
		return new Specification<Advertisement>(){
			@Override
			public Predicate toPredicate(Root<Advertisement> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("status"), 1);
			}
		};
	}*/
	
}
