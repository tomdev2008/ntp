package cn.me.xdf.service.material;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.material.MaterialAuth;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.service.BaseService;
import cn.me.xdf.utils.ShiroUtils;
/**
 * 
 * 资源service
 * 
 * @author 
 * 
 */
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
	 * 查看可使用的资源（分页操作）
	 * @param fdType 资源类型
	 * @param pageNo 页码
	 * @param pageSize 每页几条数据
	 * @return
	 */
	public Pagination findMaterialList(String fdType,Integer pageNo, Integer pageSize){
		Finder finder = Finder.create("select distinct * from MaterialInfo info right join MaterialAuth auth");
		finder.append("where info.fdId=auth.material.fdId and info.fdType=:fdType");
		finder.setParam("fdType", fdType);
		finder.append(" and ( (info.isPublish=:isPublish ) or ( auth.isReader=:isReader and auth.fdUser.fdId=:userId ) )");
		finder.setParam("isPublish",true);
		finder.setParam("isReader",true);
		finder.setParam("userId",ShiroUtils.getUser().getId());
		Pagination page = this.getPage(finder, pageNo, pageSize);
		return page;
	}
	/**
	 * 在list页面进行搜索的方法
	 * @param fdName 页面传来的资源名字
	 * @param fdType 资源类型
	 * @param pageNo 页码
	 * @param pageSize 每页几条数据
	 * @return Pagination
	 */
	public Pagination serachMaterialList(String fdType, String fdName, Integer pageNo, Integer pageSize){
		Finder finder = Finder.create("select distinct * from MaterialInfo info right join MaterialAuth auth");
		finder.append("where info.fdId=auth.material.fdId and info.fdType=:fdType and info.fdName like '%:fdName%' ");
		finder.setParam("fdType", fdType);
		finder.setParam("fdName", fdName);
		finder.append(" and ( (info.isPublish=:isPublish ) or ( auth.isReader=:isReader and auth.fdUser.fdId=:userId ) )");
		finder.setParam("isPublish",true);
		finder.setParam("isReader",true);
		finder.setParam("userId",ShiroUtils.getUser().getId());
		Pagination page = this.getPage(finder, pageNo, pageSize);
		return page;
	}
	
	/**
	 * 查看当前用户可用的资源
	 */
	@SuppressWarnings("unchecked")
	public List<MaterialInfo> findCanUsed(){
		Finder finder = Finder
				.create("from MaterialAuth anth, MaterialInfo info ");
		finder.append("where (info.isPublish='true') or (anth.isReader='true' and anth.fdUser.fdId=:userId and info.fdId=anth.material.fdId )");
		finder.setParam("userId",ShiroUtils.getUser().getId());
		return super.find(finder);
	}
	
	/**
	 * 修改资源权限
	 */
	public void updateMaterialAuth(String materialAuthId,List<MaterialAuth> materialAuths){
		//删除所有相关的权限信息
		materialAuthService.deleMaterialAuthByMaterialId(materialAuthId);
		//插入权限信息
		for (MaterialAuth materialAuth : materialAuths) {
			materialAuthService.save(materialAuth);
		}
	}

}
