package cn.me.xdf.service.log;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.log.LogLogout;
import cn.me.xdf.service.BaseService;
import cn.me.xdf.service.course.CourseParticipateAuthService;

@Service
@Transactional(readOnly = false)
public class LogLogoutService extends BaseService{


	@Autowired
	private LogOnlineService logOnlineService;
	
	@SuppressWarnings("unchecked")
	@Override
	public  Class<LogLogout> getEntityClass() {
		return LogLogout.class;
	}

	public void saveAndUpdateOnine(LogLogout logLogout){
		save(logLogout);
		logOnlineService.saveOrUpdate(logLogout.getPerson(), null, logLogout.getIp(), false);

	}
	
}