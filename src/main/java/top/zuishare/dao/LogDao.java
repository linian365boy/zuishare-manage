package top.zuishare.dao;

import top.zuishare.model.Log;
import top.zuishare.spi.dto.request.RequestParam;

import java.util.List;

public interface LogDao {

	void save(Log log);

	long findAllCount(RequestParam param);

	List<Log> findList(RequestParam param);

}
