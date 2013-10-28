package cn.me.xdf.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.organization.SysOrgElement;
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
		finder.append("where (p.loginName like :key1  or  p.notifyEntity.realName like :key2) and p.loginName <> :admin");
		finder.setParam("key1", "%"+key+"%");
		finder.setParam("key2", "%"+key+"%");
		finder.setParam("admin", "admin");
		return (List<SysOrgPerson>)getPage(finder, 1,10).getList();
		
	}

}
