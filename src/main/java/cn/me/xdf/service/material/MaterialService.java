package cn.me.xdf.service.material;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.material.MaterialAuth;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
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
	 * @param order 排序规则
	 * @author yuhuizhe
	 * @return
	 */
	public Pagination findMaterialList(String fdType,Integer pageNo, Integer pageSize,String fdName,String order){
		Finder finder = Finder.create("select info.*,score.fdscore as materialScore from IXDF_NTP_MATERIAL info left join IXDF_NTP_MATERIAL_AUTH auth ");
		finder.append("on info.FDID=auth.FDMATERIALID ");
		finder.append(" left join IXDF_NTP_SCORE score on info.FDID = score.fdModelId and cn.me.xdf.model.material.MaterialInfo=score.fdmodelname");
		finder.append(" where info.FDTYPE=:fdType and info.isAvailable=1 ");
		finder.append(" and ( ( auth.isEditer=1 and auth.FDUSERID='"+ShiroUtils.getUser().getId()+"' ");
		finder.append(" ) or info.FDAUTHOR='"+ShiroUtils.getUser().getId()+"') ");
		finder.setParam("fdType", fdType);
		if(StringUtil.isNotBlank(fdName)&&StringUtil.isNotEmpty(fdName)){
			finder.append(" and info.FDNAME like :fdName");
			finder.setParam("fdName", '%' + fdName + '%');
		}
		if(StringUtil.isNotBlank(order)&&StringUtil.isNotEmpty(order)){
			if(order.equalsIgnoreCase("fdName")){
				finder.append(" order by info.fdName ");
			}
			if(order.equalsIgnoreCase("FDCREATETIME")){
				finder.append(" order by info.FDCREATETIME ");
			}
			if(order.equalsIgnoreCase("FDSCORE")){
				finder.append(" order by score.FDSCORE ");
			}
			
		}
		Pagination page = getPageBySql(finder, pageNo, pageSize);
		return page;
	}
	/**
	 * 保存素材
	 * @param info
	 */
	@Transactional(readOnly = false)
	public void saveMaterial(MaterialInfo info){
		SysOrgPerson creator = new SysOrgPerson();
		creator.setFdId(ShiroUtils.getUser().getId());
		info.setCreator(creator);
		info.setFdCreateTime(new Date());
		info.setIsAvailable(true);
		super.save(info);
	}
	/**
	 * 编辑素材
	 * @param material
	 * @param fdId
	 */
	@Transactional(readOnly = false)
	public void updateMaterial(MaterialInfo material,String fdId){
		MaterialInfo info = this.get(fdId);
		info.setFdAuthorDescription(material.getFdAuthorDescription());
        info.setFdAuthor(material.getFdAuthor());
        info.setFdDescription(material.getFdDescription());
        info.setIsPublish(material.getIsPublish());
        info.setFdLink(material.getFdLink());
        info.setFdName(material.getFdName());
		info.setAttMains(material.getAttMains());
		info.setAuthList(material.getAuthList());
		info.setQuestions(material.getQuestions());
		this.update(info);
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
	
	/**
	 * 模糊查询资源
	 */
	public List<Map> getMaterialsTop10Bykey(String key,String type){
		Finder finder = Finder
				.create(" from MaterialInfo  info ");
		finder.append("where info.fdType = :type and info.fdName like :key");
		finder.setParam("type", type);
		finder.setParam("key", "%"+key+"%");
		List<MaterialInfo> list =(List<MaterialInfo>)(getPage(finder, 1,10).getList());
		if(list==null){
			return null;
		}
		List<Map> maps = new ArrayList<Map>();
		for (MaterialInfo materialInfo : list) {
			Map map = new HashMap();
			map.put("id", materialInfo.getFdId());
			map.put("name", materialInfo.getFdName());
			maps.add(map);
		}
		return maps;
	}

}
