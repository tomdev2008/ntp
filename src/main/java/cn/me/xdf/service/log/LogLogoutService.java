package cn.me.xdf.service.log;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.utils.Identities;
import cn.me.xdf.model.log.LogLogout;
import cn.me.xdf.service.BaseService;

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
		String sql = "insert into ixdf_ntp_loglogout values(?,"+null+",?,?,?,?)";
	   	executeSql(sql, Identities.generateID(),logLogout.getIp(),logLogout.getSessionId(),new Date(),logLogout.getPerson().getFdId());
		logOnlineService.logoutToSaveOrUpdate(logLogout.getPerson().getFdId(), null, logLogout.getIp(), false);
	}
	
}
