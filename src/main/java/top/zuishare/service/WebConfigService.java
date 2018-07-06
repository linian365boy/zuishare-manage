package top.zuishare.service;

import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import top.zuishare.spi.constants.Constants;
import top.zuishare.spi.dto.ConstantVariable;
import top.zuishare.spi.model.WebConfig;
import top.zuishare.spi.util.RedisUtil;
import top.zuishare.spi.util.Tools;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class WebConfigService {
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	private static final Logger logger = LoggerFactory.getLogger(WebConfigService.class);
	
	public boolean saveOrUpdateSystem(String path, WebConfig system){
		system.setUpdateTime(new Date());
		String jsonStr = new GsonBuilder().setDateFormat(ConstantVariable.DFTSTR).create().toJson(system);
		logger.info("saveOrUpdateSystem param => {}, webConfig => {}, translate to jsonStr => {}",path, system, jsonStr);
		boolean flag = false;
		if(Tools.saveOrUpdateWebConfig(path,jsonStr)){
			flag = true;
			logger.info("设置网站配置成功！");
			// set to redis
			stringRedisTemplate.opsForValue().set(RedisUtil.getWebConfigKey(), jsonStr, Constants.TIMEOUTDAYS, TimeUnit.DAYS);
		}else{
			logger.error("设置网站配置失败");
		}
		logger.info("saveOrUpdateSystem return data => {}", flag);
		return flag;
	}

	public WebConfig loadSystemConfig(String path) {
		String jsonStr = Tools.getJsonStrFromPath(path);
		logger.info("从文件|{}解析的json串为|{}",path,jsonStr);
		return new GsonBuilder().setDateFormat(ConstantVariable.DFTSTR).create().fromJson(jsonStr, WebConfig.class);
	}
	
}
