package top.zuishare.dao;

import org.apache.ibatis.annotations.Param;
import top.zuishare.model.Advertisement;
import top.zuishare.spi.dto.request.RequestParam;

import java.util.List;

public interface AdvertisementDao {

	Advertisement loadAdvertisement(@Param("id") Integer id);

	void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

	List<Advertisement> findIndexAds(@Param("indexAdsSize") int indexAdsSize);
	
	void save(Advertisement temp);
	
	void delete(Integer id);
	
	long findAllCount(RequestParam param);
	
	List<Advertisement> findList(RequestParam param);
	
	void updateAdvertisement(Advertisement ad);
}
