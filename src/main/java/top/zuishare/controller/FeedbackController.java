package top.zuishare.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.zuishare.service.FeedbackService;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.spi.model.Feedback;
import top.zuishare.util.Constant;
import top.zuishare.util.PageRainier;
import top.zuishare.vo.MessageVo;
import top.zuishare.vo.ReturnData;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/feedback")
@Scope("prototype")
public class FeedbackController {
	@Autowired
	private FeedbackService feedbackService;
	private final static Logger logger = LoggerFactory.getLogger(FeedbackController.class);
	
	@RequestMapping({"/feedbacks/list"})
	public String list(ModelMap map, HttpServletRequest request){
		map.put("ajaxListUrl", "admin/feedback/feedbacks/getJsonList");
		return "admin/feedback/list";
	}
	
	@ResponseBody
	@RequestMapping({"/feedbacks/getJsonList"})
	public ReturnData<Feedback> getJsonList(RequestParam param){
		PageRainier<Feedback> feedbacks = feedbackService.findAll(param);
		ReturnData<Feedback> datas = new ReturnData<Feedback>(feedbacks.getTotalRowNum(), feedbacks.getResult());
		return datas;
	}
	
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public String detail(@PathVariable Integer id, Model model){
		if(id!=null){
			model.addAttribute("model",feedbackService.loadOne(id));
		}
		return "admin_unless/feedback/detail";
	}
	
	@ResponseBody
	@RequestMapping(value="/{id}/del",method= RequestMethod.GET)
	public MessageVo delete(@PathVariable Integer id){
	    logger.info("delete feedback param => {}", id);
		MessageVo vo = null;
		if (id != null) {
			try{
				feedbackService.delete(id);
				logger.warn("delete feedback id => {} succeed.",id);
				vo = new MessageVo(Constant.SUCCESS_CODE,"删除评论成功！");
			}catch(Exception e){
				logger.error("delete feedback error.",e);
				vo = new MessageVo(Constant.ERROR_CODE,"删除评论失败！");
			}
		}
		logger.info("delete feedback return data => {}", vo);
		return vo;
	}
}
