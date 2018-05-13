package top.zuishare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zuishare.dao.FeedbackDao;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Feedback;
import top.zuishare.util.PageRainier;

import java.util.Date;

@Service("feedbackService")
public class FeedbackService {
	@Autowired
	private FeedbackDao feedbackDao;

	public PageRainier<Feedback> findAll(RequestParam param) {
		long count = feedbackDao.findAllCount(param);
		PageRainier<Feedback> page = new PageRainier<Feedback>(count);
		page.setResult(feedbackDao.findList(param));
		return page;
	}

	public Feedback loadOne(Integer id) {
		return feedbackDao.findOne(id);
	}

	public void delete(Integer id) {
		feedbackDao.delete(id);
	}

	public void addFeedback(Feedback feedback) {
		feedback.setCreateTime(new Date());
		feedbackDao.save(feedback);
	}
	
}
