package cn.me.xdf.service.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.me.xdf.model.material.MaterialEnum;
import cn.me.xdf.service.material.source.ISourceService;
import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.material.MaterialAuth;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.organization.User;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.BaseService;
import cn.me.xdf.utils.ShiroUtils;
/**
 * 
 * 资源service
 * 
 * @author 
 * 
 */
@Transactional(readOnly = true)
public class MaterialService extends BaseService {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MaterialAuthService materialAuthService;

    private Map<String,ISourceService> sourceMap;

    public Map<String,ISourceService> getSourceMap() {
        return sourceMap;
    }

    public void setSourceMap(Map<String,ISourceService> sourceMap) {
        this.sourceMap = sourceMap;
    }

    @SuppressWarnings("unchecked")
	@Override
	public  Class<MaterialInfo> getEntityClass() {
		return MaterialInfo.class;
	}

    public void find_test(String value){
        sourceMap.get(MaterialEnum.valueOf(value));
    }
	
	/**
	 * 根据素材id获取素材权限信息
	 * @param MaterialId yuhz
	 * @return
	 */
	public List<Map> findAuthInfoByMaterialId(String MaterialId){
		//获取课程ID
		List<MaterialAuth> auths = materialAuthService.findByProperty("material.fdId", MaterialId);
		List<Map> list = new ArrayList<Map>();
		User user = null;
		for (int i=0;i<auths.size();i++) {
			MaterialAuth materialAuth = auths.get(i);
			SysOrgPerson  person = materialAuth.getFdUser();
			Map map= new HashMap();
			map.put("id", person.getFdId());
			map.put("index", i);
			map.put("imgUrl",person.getPoto());
			map.put("name",person.getRealName());
			map.put("mail",person.getFdEmail());
			map.put("org","");
			map.put("department",person.getDeptName());
			map.put("tissuePreparation", materialAuth.getIsReader());
			map.put("editingCourse",materialAuth.getIsEditer());
			list.add(map);
		}
		return list;
	}
	/**
	 * 编辑视频素材
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
		info.setAuthList(material.getAuthList());
		info.setQuestions(material.getQuestions());
		this.update(info);
	}
	/**
	 * 保存素材的相关权限
	 * @param kingUser
	 * @param materialId
	 */
	@Transactional(readOnly = false)
	public void saveMaterAuth(String kingUser,String materialId){
	  if(StringUtil.isNotBlank(kingUser)){
		  List<Map> list =  JsonUtils.readObjectByJson(kingUser, List.class);
			List<MaterialAuth> auths = new ArrayList<MaterialAuth>();
			MaterialInfo info = this.get(materialId);
			//删除素材的权限
			if(StringUtil.isNotBlank(materialId)&&StringUtil.isNotEmpty(materialId)){
				materialAuthService.deleMaterialAuthByMaterialId(materialId);
			}
			for (Map map : list) {
				MaterialAuth auth = new MaterialAuth();
				auth.setMaterial(info);
				SysOrgPerson fdUser = accountService.load((String)map.get("id"));
				auth.setFdUser(fdUser);
				auth.setIsReader((Boolean)map.get("tissuePreparation"));
				auth.setIsEditer((Boolean)map.get("editingCourse"));
				auths.add(auth);
			}
			//插入权限信息
			for(MaterialAuth auth:auths){
				materialAuthService.save(auth);
			}
	  }
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
	@Transactional(readOnly = false)
	public Pagination findMaterialList(String fdType,Integer pageNo, Integer pageSize,String fdName,String order){
		Finder finder = Finder.create("select info.*,score.fdaverage from IXDF_NTP_MATERIAL info left join IXDF_NTP_MATERIAL_AUTH auth ");
		finder.append("on info.FDID=auth.FDMATERIALID ");
		finder.append(" left join IXDF_NTP_SCORE_STATISTICS score on info.FDID = score.fdModelId and score.fdmodelname = 'cn.me.xdf.model.material.MaterialInfo' ");
		finder.append(" where info.FDTYPE=:fdType and info.isAvailable=1 ");
		finder.append(" and ( ( auth.isEditer=1 and auth.FDUSERID='"+ShiroUtils.getUser().getId()+"' ");
		finder.append(" ) or info.fdCreatorId='"+ShiroUtils.getUser().getId()+"') ");
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
				finder.append(" order by info.FDCREATETIME desc ");
			}
			if(order.equalsIgnoreCase("FDSCORE")){
				finder.append(" order by score.fdaverage desc ");
			}
			
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
	
	/**
	 * 模糊查询资源
	 */
	public List<Map> getMaterialsTop10Bykey(String key,String type){
		
		Finder finder = Finder
				.create("select info.FDID as id , info.FDNAME as name from IXDF_NTP_MATERIAL info left join IXDF_NTP_MATERIAL_AUTH auth ");
		finder.append(" on info.FDID=auth.FDMATERIALID ");
		finder.append(" where info.FDTYPE=:fdType and info.ISAVAILABLE=1 and lower(info.FDNAME) like :key ");
		finder.append(" and ( (auth.FDUSERID='"+ShiroUtils.getUser().getId()+"' and auth.ISREADER=1 ) ");
		finder.append("  or info.ISPUBLISH=1) ");
		finder.setParam("fdType", type);
		finder.setParam("key", "%"+key+"%");
		List<Map> list =(List<Map>)(getPageBySql(finder, 1,10).getList());
		if(list==null){
			return null;
		}
		List<Map> maps = new ArrayList<Map>();
		for (Map map1 : list) {
			Map map = new HashMap();
			map.put("id", map1.get("ID"));
			map.put("name",  map1.get("NAME"));
			maps.add(map);
		}
		return maps;
	}

}
