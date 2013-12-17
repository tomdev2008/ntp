package cn.me.xdf.service.log;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.log.LogLogin;
import cn.me.xdf.model.log.LogOnline;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.BaseService;
import cn.me.xdf.utils.DateUtil;
import cn.me.xdf.utils.ShiroUtils;


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
	 	if(login==null){
	 		return null;
	 	}
	 	return login.getPerson();
	}
	
	public void saveAndUpdateOnine(LogLogin logLogin){
		save(logLogin);
		LogOnline logOnline = logOnlineService.loginToSaveOrUpdate(logLogin.getPerson(), logLogin.getTime(), logLogin.getIp(), true);
	}
	
	/**
	 * 获取用户最后一次登录时间
	 * 
	 * @return
	 */
	public String getNewLoginDate(){
		Finder finder = Finder.create("");		
		finder.append("from LogLogin l where l.person.fdId=:userid order by l.time desc");
		finder.setParam("userid", ShiroUtils.getUser().getId());
		List<LogLogin> logins = (List<LogLogin>)getPage(finder, 1,10).getList();
		if(logins==null||logins.size()<=1){
			return "0";
		}else{
			Date date = logins.get(1).getTime();
			return ""+ DateUtil.getIntervalDate(DateUtil.convertDateToString(date), "yyyy/MM/dd hh:mm aa");
		}
		
	}
	
//	/**
//	 * 统计在线天数
//	 * 
//	 * @return
//	 */
//	private int getOnlineDay(){
//		Finder finder = Finder.create("");		
//		finder.append("select count( distinct to_char(l.time,'yyyy-mm-dd')) from IXDF_NTP_LOGLOGIN l ");
//		finder.append("where l.fdpersonid='"+ShiroUtils.getUser().getId()+"' ");
//		List<Object> list = findBySQL(finder.getOrigHql(), null, null);
//		return new Integer(list.get(0).toString());
//	}


}
	

