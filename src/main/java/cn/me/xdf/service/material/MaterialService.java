package cn.me.xdf.service.material;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.service.BaseService;
import cn.me.xdf.utils.ShiroUtils;

@Service
@Transactional(readOnly = false)
public class MaterialService extends BaseService {

	@Autowired
	private MaterialAuthService materialAuthService;
	
	@SuppressWarnings("unchecked")
	@Override
	public  Class<MaterialInfo> getEntityClass() {
		return MaterialInfo.class;
	}
	
	/**
	 * 查看当前用户可用的资源
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<MaterialInfo> findCanUsed(){
		Finder finder = Finder
				.create("from MaterialAuth anth, MaterialInfo info ");
		finder.append("where (info.isPublish='true') or (anth.isReader='true' and anth.fdUser.fdId=:userId and info.fdId=anth.material.fdId )");
		finder.setParam("userId",ShiroUtils.getUser().getId());
		return super.find(finder);
	}

}
