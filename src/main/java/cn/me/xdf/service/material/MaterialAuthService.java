package cn.me.xdf.service.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.course.CourseAuth;
import cn.me.xdf.model.material.MaterialAuth;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.organization.User;
import cn.me.xdf.service.BaseService;
import cn.me.xdf.service.course.CourseAuthService;
/**
 * 
 * 资源权限service
 * 
 * @author
 * 
 */
@Service
@Transactional(readOnly = false)
public class MaterialAuthService extends BaseService{
	
	@Autowired
	private MaterialService materialService;

	@SuppressWarnings("unchecked")
	@Override
	public  Class<MaterialAuth> getEntityClass() {
		return MaterialAuth.class;
	}
	
	/**
	 * 添加或修改资源权限
	 * 
	 */
	@Transactional(readOnly = false)
	public MaterialAuth saveOrUpdateMaterialAuth(MaterialAuth materialAuth){
		MaterialAuth auth =findByMaterialIdAndUserId(materialAuth.getMaterial().getFdId(), materialAuth.getFdUser().getFdId());
		if(auth==null){
			return save(materialAuth);
		}else{
			return update(materialAuth);
		}
	}
	/**
	 * 删除资源权限
	 * 
	 */
	@Transactional(readOnly = false)
	public void deleMaterialAuth(MaterialAuth materialAuth){
		delete(materialAuth);
	}
	/**
	 * 查找资源权限
	 * 
	 */
	@Transactional(readOnly = true)
	public MaterialAuth findByMaterialIdAndUserId(String materialId,String userId){
		Finder finder = Finder
				.create("from MaterialAuth anth ");
		finder.append("where anth.material.fdId = :materialId and anth.fdUser.fdId = :userId");
		finder.setParam("materialId", materialId);
		finder.setParam("userId", userId);
		return (MaterialAuth) super.find(finder).get(0);
	}

	/**
	 * 根据资源Id删除所有资源权限
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public void deleMaterialAuthByMaterialId(String materialId){
		Finder finder = Finder
				.create("from MaterialAuth anth ");
		finder.append("where anth.material.fdId = :materialId");
		finder.setParam("materialId", materialId);
		List<MaterialAuth> list = super.find(finder);
		for (MaterialAuth materialAuth : list) {
			delete(materialAuth);
		}
	}
	/**
	 * 根据素材id获取素材权限信息
	 * @param MaterialId yuhz
	 * @return
	 */
	public List<Map> findAuthInfoByMaterialId(String MaterialId){
		//获取课程ID
		List<CourseAuth> auths = materialService.findByProperty("course.fdId", MaterialId);
		List<Map> list = new ArrayList<Map>();
		User user = null;
		for (int i=0;i<auths.size();i++) {
			CourseAuth courseAuth = auths.get(i);
			SysOrgPerson  person = courseAuth.getFdUser();
			Map map= new HashMap();
			map.put("id", person.getFdId());
			map.put("index", i);
			map.put("imgUrl",person.getPoto());
			map.put("name",person.getRealName());
			map.put("mail",person.getFdEmail());
			map.put("org","");
			map.put("department",person.getDeptName());
			map.put("tissuePreparation", courseAuth.getIsAuthStudy());
			map.put("editingCourse",courseAuth.getIsEditer());
			list.add(map);
		}
		return list;
	}

}
