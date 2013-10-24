package cn.me.xdf.service.material;

import java.util.List;

import jodd.util.StringUtil;

import org.htmlparser.lexer.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.hibernate4.Finder.SearchType;
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
	 * @param fdName 不为空是表示搜索
	 * @author yuhuizhe
	 * @return
	 */
	public Pagination findMaterialList(String fdType,Integer pageNo, Integer pageSize,String fdName){
		Finder finder = Finder.create("select info.* from IXDF_NTP_MATERIAL info left join IXDF_NTP_MATERIAL_AUTH auth ");
		finder.append("on info.FDID=auth.FDMATERIALID  where info.FDTYPE=:fdType and info.isAvailable=1 ");
		finder.append(" and ( ( auth.isEditer=1 and auth.FDUSERID='"+ShiroUtils.getUser().getId()+"' ");
		finder.append(" ) or info.FDAUTHOR='"+ShiroUtils.getUser().getId()+"') ");
		finder.setParam("fdType", fdType);
		if(StringUtil.isNotBlank(fdName)&&StringUtil.isNotEmpty(fdName)){
			finder.append(" and info.FDNAME like : fdName");
			finder.setParam("fdName", '%' + fdName + '%');
		}
		Pagination page = getPageBySql(finder, pageNo, pageSize);
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
