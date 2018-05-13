package top.zuishare.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zuishare.dao.AdvertisementDao;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Advertisement;
import top.zuishare.util.PageRainier;

import java.util.List;

@Service("advertisementService")
public class AdvertisementService {
	@Autowired
	private AdvertisementDao advertisementDao;
	private static final Logger logger = LoggerFactory.getLogger(AdvertisementService.class);
	
	public void saveAdvertisement(Advertisement temp) {
		advertisementDao.save(temp);
	}
	
	public Advertisement loadAdvertisement(Integer id) {
		return advertisementDao.loadAdvertisement(id);
	}
	
	public void delAdvertisement(Integer id) {
		advertisementDao.delete(id);
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
