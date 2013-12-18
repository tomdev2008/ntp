package cn.me.xdf.service.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
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
	
	public LogOnline logoutToSaveOrUpdate(String personId,Date date,String ip,Boolean isOnline){
		String sql = "update ixdf_ntp_logonline set isonline=?,ip=? where fdpersonid=?";
		executeSql(sql,isOnline,ip,personId);
		return null;
	}
	
	public LogOnline loginToSaveOrUpdate(SysOrgPerson person,Date date,String ip,Boolean isOnline){
		LogOnline logOnline = findUniqueByProperty("person.fdId", person.getFdId());
		if(logOnline==null){
			LogOnline online = new LogOnline();
			online.setIp(ip);
			online.setLoginTime(date);
			online.setIsOnline(isOnline);
			online.setPerson(person);
			online.setLoginNum(1);
			online.setLoginDay(1);
			save(online);
			return online;
		}else{
			logOnline.setIsOnline(isOnline);
			logOnline.setIp(ip);
			Date oldDate = logOnline.getLoginTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        	String dateold = formatter.format(oldDate);  
        	String datenow = formatter.format(date); 
        	if(!dateold.equals(datenow)){
        		logOnline.setLoginDay(logOnline.getLoginDay()+1);
        	}
			logOnline.setLoginTime(date);
			update(logOnline);
			if(isOnline){
				logOnline.setLoginNum(logOnline.getLoginNum()+1);
			}else{
				logOnline.setLoginNum(logOnline.getLoginNum());
			}
			return logOnline;
		}
	}
	
	public LogOnline getOnlineByUserId(String userId){
		return findUniqueByProperty("person.fdId", userId);
	}

}
