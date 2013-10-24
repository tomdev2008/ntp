package cn.me.xdf.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
