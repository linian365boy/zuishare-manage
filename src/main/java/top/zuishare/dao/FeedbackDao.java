package top.zuishare.dao;

import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Feedback;

import java.util.List;

public interface FeedbackDao {

	Feedback findOne(Integer id);

	void delete(Integer id);

	void save(Feedback feedback);

	long findAllCount(RequestParam param);

	List<Feedback> findList(RequestParam param);

}
