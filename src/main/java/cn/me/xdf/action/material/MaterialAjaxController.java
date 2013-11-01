package cn.me.xdf.action.material;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseCatalogService;
import cn.me.xdf.service.course.CourseContentService;
import cn.me.xdf.service.material.MaterialAuthService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.utils.ShiroUtils;

@Controller
@RequestMapping(value = "/ajax/material")
@Scope("request")
public class MaterialAjaxController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private MaterialService materialService;

	@Autowired
	private MaterialAuthService materialAuthService;

	@Autowired
	private AttMainService attMainService;

	@Autowired
	private CourseContentService courseContentService;

	@Autowired
	private CourseCatalogService courseCatalogService;

	/**
	 * 删除素材相关信息==>素材==>课程内容==>节
	 * 
	 * @param request
	 */
	@RequestMapping(value = "deleteMaterial")
	@ResponseBody
	public void deleteMaterial(HttpServletRequest request) {
		String materialId = request.getParameter("materialId");
		// 删除素材
		deleteMaterialData(materialId);
	}

	/**
	 * 批量删除素材的方法
	 * 
	 * @param request
	 */
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	public void batchDelete(HttpServletRequest request) {
		String fdIds = request.getParameter("materialIds");
		if (StringUtil.isNotEmpty(fdIds)) {
			String[] materialId = fdIds.split(",");
			String fdId = "";
			for (int i = 0; i < materialId.length; i++) {
				fdId = materialId[i];
				deleteMaterialData(fdId);
			}
		}
	}

	@RequestMapping(value = "deleteAllMaterial")
	@ResponseBody
	public void deleteAllMaterial(HttpServletRequest request) {
		String fdType = request.getParameter("fdType");
		String fdName = request.getParameter("fdName");
		String order = request.getParameter("order");
		Pagination page = materialService.findMaterialList(fdType, 1,
				SimplePage.DEF_COUNT, fdName, order);
		int i = page.getTotalPage();
		if (i > 0) {
			for (int j = 0; j < i; j++) {
				page = materialService.findMaterialList(fdType, 1, 1, fdName,
						order);
				List list = page.getList();
				if (list != null && list.size() > 0) {
					for (Object obj : list) {
						Map map = (Map) obj;
						String materialId = (String) map.get("FDID");
						deleteMaterialData(materialId);
					}
				}
			}
		}

	}

	public void deleteMaterialData(String materialId) {
		List<CourseContent> courseList = courseContentService.findByProperty(
				"material.fdId", materialId);
		if (courseList != null && courseList.size() > 0) {
			for (CourseContent courseContent : courseList) {
				CourseCatalog catalog = courseContent.getCatalog();
				if (catalog != null && catalog.getFdTotalContent() != null) {
					catalog.setFdTotalContent(catalog.getFdTotalContent() - 1);
					courseCatalogService.save(catalog);
				}
				courseContentService.delete(courseContent);
			}
		}
		MaterialInfo material = materialService.load(materialId);
		material.setIsAvailable(false);
		materialService.update(material);
	}
	@RequestMapping(value = "updateDownloadNum")
	@ResponseBody
	public void updateDownloadNum(HttpServletRequest request){
		String fdId = request.getParameter("materialId");
		MaterialInfo materialInfo = materialService.load(fdId);
		if(materialInfo.getFdDownloads()==null){
			materialInfo.setFdDownloads(1);
		}else{
			materialInfo.setFdDownloads(materialInfo.getFdDownloads()+1);
		}
		materialService.update(materialInfo);
	}

	/**
	 * ajax找出素材列表
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "findList")
	@ResponseBody
	public ModelAndView findList(Model model, HttpServletRequest request) {
		String fdType = request.getParameter("fdType");
		String pageNoStr = request.getParameter("pageNo");
		String fdName = request.getParameter("fdName");
		String order = request.getParameter("order");
		int pageNo;
		if (StringUtil.isNotBlank(pageNoStr)
				&& StringUtil.isNotEmpty(pageNoStr)) {
			pageNo = Integer.parseInt(pageNoStr);
		} else {
			pageNo = 1;
		}
		if (StringUtil.isNotBlank(fdType) && StringUtil.isNotEmpty(fdType)) {
			Pagination page = materialService.findMaterialList(fdType, pageNo,
					SimplePage.DEF_COUNT, fdName, order);
			model.addAttribute("page", page);
		}
		if(Constant.MATERIAL_TYPE_TEST.equals(fdType)){
			return new ModelAndView(
					"forward:/WEB-INF/views/material/divMatQuestList.jsp");	
		}else if(Constant.MATERIAL_TYPE_JOBPACKAGE.equals(fdType)){
			return new ModelAndView(
					"forward:/WEB-INF/views/material/divMatTaskList.jsp");	
		}else{
			return new ModelAndView(
					"forward:/WEB-INF/views/material/divMatList.jsp");
		}
	}

	@RequestMapping(value = "getMaterialBykey")
	@ResponseBody
	public List<Map> getMaterialBykey(HttpServletRequest request) {
		String key = request.getParameter("q");
		String type = request.getParameter("type");
		return materialService.getMaterialsTop10Bykey(key, type);
	}

	@RequestMapping(value = "saveMaterial")
	@ResponseBody
	public Map saveMaterial(HttpServletRequest request) {
		String type = request.getParameter("type");
		String fileName = request.getParameter("fileName");
		String attId = request.getParameter("attId");
		MaterialInfo materialInfo = new MaterialInfo();
		materialInfo.setFdName(fileName);
		materialInfo.setFdType(type);
		materialInfo.setIsAvailable(true);
		materialInfo.setIsPublish(true);
		materialInfo.setIsDownload(true);
		SysOrgPerson creator = accountService
				.load(ShiroUtils.getUser().getId());
		materialInfo.setCreator(creator);
		List<AttMain> attMains = new ArrayList<AttMain>();
		AttMain attMain = new AttMain();
		attMain.setFdId(attId);
		attMains.add(attMain);
		materialInfo.setAttMains(attMains);
		materialService.save(materialInfo);
		Map map = new HashMap();
		map.put("id", materialInfo.getFdId());
		map.put("name", materialInfo.getFdName());
		return map;
	}

	/**
	 * 更新或保存素材(共用)
	 * 
	 * @param materialInfo
	 * @return
	 */
	@RequestMapping(value = "saveOrUpdateVideo")
	@ResponseBody
	public void saveOrUpdateVideo(HttpServletRequest request) {
		MaterialInfo info = new MaterialInfo();
		info.setFdAuthor(request.getParameter("author"));
		info.setFdAuthorDescription(request.getParameter("authorIntro"));
		info.setFdLink(request.getParameter("videoUrl"));
		info.setFdName(request.getParameter("videoName"));
		info.setFdDescription(request.getParameter("videoIntro"));
		String permission = request.getParameter("permission");
		String kingUser = request.getParameter("kingUser");
		String attId = request.getParameter("attId");
		String fdId = request.getParameter("fdId");
		if (StringUtil.isBlank(fdId)) {
			SysOrgPerson creator = accountService.load(ShiroUtils.getUser()
					.getId());
			info.setFdType(request.getParameter("fdType"));
			info.setCreator(creator);
			info.setFdCreateTime(new Date());
			info.setIsAvailable(true);
			if (permission.equals("open")) {
				info.setIsPublish(true);
			} else {
				info.setIsPublish(false);
				// 保存权限信息
				materialService.saveMaterAuth(kingUser, info.getFdId());
			}
			// 保存素材信息
			materialService.save(info);
			// 保存附件信息
			if (StringUtil.isNotBlank(attId)) {
				saveAtt(attId, info.getFdId());
			}
		} else {
			if (permission.equals("open")) {
				info.setIsPublish(true);
				// 删除素材的权限
				if (StringUtil.isNotBlank(fdId)
						&& StringUtil.isNotEmpty(fdId)) {
					materialAuthService.deleMaterialAuthByMaterialId(fdId);
				}
			} else {
				info.setIsPublish(false);
				materialService.saveMaterAuth(kingUser, fdId);
			}
			materialService.updateMaterial(info, fdId);
			if (StringUtil.isNotBlank(attId)) {
				attMainService.deleteAttMainByModelId(info.getFdId());
				saveAtt(attId, info.getFdId());
			}
		}
	}

	/**
	 * 保存附件关系
	 * 
	 * @param attId
	 * @param modelId
	 */
	public void saveAtt(String attId, String modelId) {
		AttMain att = attMainService.get(attId);
		att.setFdModelId(modelId);
		att.setFdModelName(MaterialInfo.class.getName());
		attMainService.update(att);
	}

	/**
	 * 得到指定素材的权限信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getAuthInfoByMaterId")
	@ResponseBody
	public String getAuthInfoByMaterId(HttpServletRequest request) {
		// 获取课程ID
		String MaterialId = request.getParameter("MaterialId");
		List<Map> list = materialService.findAuthInfoByMaterialId(MaterialId);
		Map map = new HashMap();
		map.put("user", list);
		return JsonUtils.writeObjectToJson(map);
	}

	
}
