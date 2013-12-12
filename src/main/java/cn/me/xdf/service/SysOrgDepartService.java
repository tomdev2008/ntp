package cn.me.xdf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.model.organization.SysOrgDepart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class SysOrgDepartService extends BaseService {

	/**
	 * 根据机构编码获取机构下的所有部门，包括本机构
	 * 
	 * @param orgId
	 * @return
	 */
	public List<SysOrgDepart> findAllChildrenById(String orgId) {
		List<SysOrgDepart> elements = new ArrayList<SysOrgDepart>();
		SysOrgDepart element = load(orgId);
		if (element != null) {
			elements.add(element);
			loadAllChildren(element.getFdChildren(), elements);
		}
		return elements;
	}
	
	/**
	 * 根据类型查询部门信息
	 * @param type
	 * @return
	 */
	public List<SysOrgDepart> findAllOrgByType(int type){

        return findByCriteria(SysOrgDepart.class, Value.eq("fdOrgType", type));
	}
	
	@SuppressWarnings("unchecked")
	public List<SysOrgDepart> findTypeis1(){
		Finder finder = Finder
				.create("from SysOrgDepart e where (e.hbmParent.fdId = :fdId or e.fdNo= :fdNo)");
		finder.setParam("fdId", "13ca02c2af70a0072c65c3644b28f42d");
		finder.setParam("fdNo", "105");
		return find(finder);
	}
	
	public List<SysOrgDepart> findElement(String fdId){
		
		return null;
	}



	private void loadAllChildren(List<SysOrgDepart> elements,
			List<SysOrgDepart> targetEelements) {
		for (SysOrgDepart e : elements) {
			targetEelements.add(e);
			if (e.getHbmChildren() != null) {
				loadAllChildren(e.getFdChildren(), targetEelements);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<SysOrgDepart> getEntityClass() {
		return SysOrgDepart.class;
	}

}
