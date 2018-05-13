package top.zuishare.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.zuishare.service.SystemConfig;
import top.zuishare.service.WebConfigService;
import top.zuishare.spi.dto.LogType;
import top.zuishare.spi.model.WebConfig;
import top.zuishare.util.Constant;
import top.zuishare.util.LogUtil;
import top.zuishare.vo.MessageVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: SystemController  
 * @Description: 网站系统设置一些配置，如关键字，底部内容等 
 * @date: 2016年7月22日 下午2:32:41 
 * 
 * @author tanfan 
 * @version  
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/admin/sys/webconfig")
public class WebConfigController {
	@Autowired
	private WebConfigService webConfigService;
	@Autowired
	private SystemConfig systemConfig;
	@Autowired
	private LogUtil logUtil;
	private static final Logger logger = LoggerFactory.getLogger(WebConfigController.class);
	
	@RequestMapping(value={"/detail","/",""},method= RequestMethod.POST)
	public String detail(Model model) {
		model.addAttribute("model",webConfigService.loadSystemConfig(systemConfig.getWebConfigPath()));
		return "admin/sys/webconfig/detail";
	}
	
	@ResponseBody
	@RequestMapping(value={"/update"},method= RequestMethod.POST)
	public MessageVo update(WebConfig config, HttpServletRequest request){
		logger.info("update webConfig param => {}", config);
		WebConfig webConfig = webConfigService.loadSystemConfig(systemConfig.getWebConfigPath());
		MessageVo vo = null;
		try{
			boolean flag = webConfigService.saveOrUpdateSystem(systemConfig.getWebConfigPath(),config);
			if(flag){
				StringBuilder content = new StringBuilder();
				if(!webConfig.getKeyword().equals(config.getKeyword())){
					content.append("网站关键字由\""+webConfig.getKeyword()+"\"修改为\""+config.getKeyword()+"\"");
				}
                logUtil.log(LogType.EDIT, content.toString());
				vo = new MessageVo(Constant.SUCCESS_CODE,"修改网站设置成功！");
			}else{
				logger.error("update webConfig fail.");
				vo = new MessageVo(Constant.ERROR_CODE,"修改网站设置失败！");
			}
		}catch(Exception e){
			logger.error("update webConfig error",e);
			vo = new MessageVo(Constant.ERROR_CODE,"修改网站设置失败！");
		}
        logger.info("update webConfig return data => {}", config);
		return vo;
	}
}
