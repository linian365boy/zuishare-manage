package top.zuishare.dao;

import org.apache.ibatis.annotations.Param;
import top.zuishare.model.Info;
import top.zuishare.spi.dto.request.RequestParam;

import java.util.List;

public interface InfoDao {
	//@Query("select info from Info info where info.code = :codec")
	Info loadByCode(@Param("codec") String code);

	Info findOne(Integer id);

	void delete(Integer id);

	void save(Info info);

	List<Info> findAll();

	void delete(Info info);

	List<Info> findList(RequestParam param);

	long findAllCount(RequestParam param);

	void updateInfo(Info info);
}
