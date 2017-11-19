package top.zuishare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.zuishare.model.Log;
import top.zuishare.service.LogService;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.util.PageRainier;
import top.zuishare.vo.ReturnData;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/sys/log")
@Scope("prototype")
public class LogController {
	@Autowired
	private LogService logService;
	@RequestMapping({"/logs/list"})
	public String list(HttpServletRequest request,ModelMap map){
		map.put("ajaxListUrl", "admin/sys/log/logs/getJsonList");
		return "admin/sys/log/list";
	}
	
	@ResponseBody
	@RequestMapping({"/logs/getJsonList"})
	public ReturnData<Log> getJsonList(RequestParam param){
		PageRainier<Log> logs = logService.findAll(param);
		ReturnData<Log> datas = new ReturnData<Log>(logs.getTotalRowNum(), logs.getResult());
		return datas;
	}
}
