package cn.me.xdf.service.material;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.material.MaterialAuth;
import cn.me.xdf.service.BaseService;

@Service
@Transactional(readOnly = false)
public class MaterialAuthService extends BaseService{

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
		finder.append("where anth.material.fdId like :materialId and anth.fdUser.fdId like :userId");
		finder.setParam("materialId", materialId);
		finder.setParam("userId", userId);
		return (MaterialAuth) super.find(finder).get(0);
	}


}
