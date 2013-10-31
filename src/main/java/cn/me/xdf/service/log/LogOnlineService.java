package cn.me.xdf.service.log;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.log.LogOnline;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.BaseService;


@Service
@Transactional(readOnly = false)
public class LogOnlineService extends BaseService{

	@SuppressWarnings("unchecked")
	@Override
	public  Class<LogOnline> getEntityClass() {
		return LogOnline.class;
	}
	
	public void saveOrUpdate(SysOrgPerson person,Date date,String ip,Boolean isOnline){
		LogOnline logOnline = findUniqueByProperty("person.fdId", person.getFdId());
		if(logOnline==null){
			LogOnline online = new LogOnline();
			online.setIp(ip);
			online.setLoginTime(date);
			online.setIsOnline(isOnline);
			online.setPerson(person);
			online.setLoginNum(1);
			save(online);
		}else{
			logOnline.setIsOnline(isOnline);
			logOnline.setIp(ip);
			logOnline.setLoginTime(date);
			update(logOnline);
			if(isOnline){
				logOnline.setLoginNum(logOnline.getLoginNum()+1);
			}else{
				logOnline.setLoginNum(logOnline.getLoginNum());
			}
		}
	}

}
