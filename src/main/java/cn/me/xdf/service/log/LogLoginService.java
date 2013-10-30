package cn.me.xdf.service.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.log.LogLogin;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.BaseService;


@Service
@Transactional(readOnly = false)
public class LogLoginService extends BaseService{

	@Autowired
	private LogOnlineService logOnlineService;
	
	@SuppressWarnings("unchecked")
	@Override
	public  Class<LogLogin> getEntityClass() {
		return LogLogin.class;
	}

	public SysOrgPerson getPersonBySessionId(String sesstionId){
	 	LogLogin login = this.findUniqueByProperty("sessionId", sesstionId);
	 	return login.getPerson();
	}
	
	public void saveAndUpdateOnine(LogLogin logLogin){
		save(logLogin);
		logOnlineService.saveOrUpdate(logLogin.getPerson(), logLogin.getTime(), logLogin.getIp(), true);
	}
}
