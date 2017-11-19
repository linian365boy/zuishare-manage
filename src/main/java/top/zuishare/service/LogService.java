package top.zuishare.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.zuishare.dao.LogDao;
import top.zuishare.model.Log;
import top.zuishare.model.User;
import top.zuishare.spi.dto.LogType;
import top.zuishare.spi.dto.request.RequestParam;
import top.zuishare.util.PageRainier;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Service("logService")
public class LogService {
	@Autowired
	private LogDao logDao;

	public void saveLog(Log log){
		logDao.save(log);
	}

	public PageRainier<Log> findAll(RequestParam param) {
		long count = logDao.findAllCount(param);
		PageRainier<Log> page = new PageRainier<Log>(count);
		page.setResult(logDao.findList(param));
		return page;
	}


	public void log(LogType type, String content) {
		//内容不为空，插入DB
		if(StringUtils.isNotBlank(content)){
			Log log = new Log();
			User u = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			log.setType(type.getName());
			log.setContent(content);
			log.setCreateTime(new Date());
			log.setOperator(u.getUsername());
			log.setOperatorRealName(u.getRealName());
			logDao.save(log);
		}
	}

	public void log(LogType type, 
			Map<String, Object> logMap) {
		Log log = new Log();
		User u = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		log.setCreateTime(new Date());
		//log.setMenu(module);
		log.setOperatorRealName(u.getRealName());
		log.setOperator(u.getUsername());
		log.setType(type.getName());
		Set<String> keys = logMap.keySet();
		StringBuilder sb = new StringBuilder();
		for(Iterator<String> it = keys.iterator();it.hasNext();){
			String key = it.next();
			Object obj = logMap.get(key);
			sb.append(key+"="+obj.toString()+" ");
		}
		log.setContent(sb.toString());
	}
}
