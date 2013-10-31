package cn.me.xdf.service.log;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.log.LogApp;
import cn.me.xdf.service.BaseService;

@Service
@Transactional(readOnly = false)
public class LogAppService extends BaseService{

	@SuppressWarnings("unchecked")
	@Override
	public  Class<LogApp> getEntityClass() {
		return LogApp.class;
	}

}
