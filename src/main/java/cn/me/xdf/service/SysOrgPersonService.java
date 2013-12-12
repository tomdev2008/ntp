package cn.me.xdf.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.organization.SysOrgDepart;
import cn.me.xdf.model.organization.SysOrgPerson;

@Service
@Transactional(readOnly = true)
public class SysOrgPersonService extends BaseService{

	@SuppressWarnings("unchecked")
	@Override
	public Class<SysOrgPerson> getEntityClass() {
		return SysOrgPerson.class; 
	}
	
	/**
	 * 根据登录名称和姓名模糊查询用户(前10条)
	 * 
	 * @param key
	 * @return
	 */
	public List<SysOrgPerson> findUserByLinkLoginAndRealNameTop10(String key){
		Finder finder = Finder
				.create("from SysOrgPerson p ");
		finder.append("where (lower(p.loginName) like :key1  or lower(p.notifyEntity.realName) like :key2 or lower(p.hbmParent.fdName) like :key3 ) and p.loginName <> :admin");
		finder.setParam("key1", "%"+key+"%");
		finder.setParam("key2", "%"+key+"%");
		finder.setParam("key3", "%"+key+"%");
		finder.setParam("admin", "admin");
		return (List<SysOrgPerson>)getPage(finder, 1,10).getList();
		
	}
	
	public Map getUserInfo(String userId){
		Map map = new HashMap();
		Finder finder = Finder
				.create("select p.fdBloodType from SysOrgPersonTemp p ");
		finder.append("where p.fdId=:userId ");
		finder.setParam("userId", userId);
		String fdBloodType = findUnique(finder);
		map.put("fdBloodType",fdBloodType==null?"不详":fdBloodType);
		Finder finder1 = Finder
				.create("select p.selfIntroduction from SysOrgPersonTemp p ");
		finder1.append("where p.fdId=:userId ");
		finder1.setParam("userId", userId);
		String selfIntroduction = findUnique(finder);
		map.put("selfIntroduction", selfIntroduction==null?"":selfIntroduction);
		return map;
	}
	

}
