package cn.me.xdf.service.studyTack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.utils.array.ArrayUtils;
import cn.me.xdf.common.utils.array.SortType;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.message.Message;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.message.MessageService;
import cn.me.xdf.utils.ShiroUtils;
import cn.me.xdf.view.model.VStudyTrack;

/**
 * 
 * 学习跟踪service
 * 
 * @author zhaoq
 * 
 */
@Service
@Transactional(readOnly = true)
public class StudyTrackService {
	
	@Autowired
	private BamCourseService bamCourseService;
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private CourseService courseService;

	
	/**
	 * 得到学习跟踪分页列表
	 * 
	 * @param selectType
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getStudyTrack(String selectType,String userId,int pageNo,int pageSize,String orderType,String key){
		Pagination pagination=null;
		if(selectType.equals("myGuidance")){//我指导的备课ok
			pagination=bamCourseService.getPageBySql(getStudyTrackByMyGuidance(orderType,key), pageNo, pageSize);
		}else if(selectType.equals("myOrganized")){//我组织的备课ok
			pagination=bamCourseService.getPageBySql(getStudyTrackByMyOrganized(orderType,key), pageNo, pageSize);
			
		}else if(selectType.equals("myDepart")){//我所在部门的备课
			pagination=bamCourseService.getPageBySql(getStudyTrackByMyDepart(orderType,key), pageNo, pageSize);
		}else if(selectType.equals("myOrg")){//我所在机构的备课
			pagination=bamCourseService.getPageBySql(getStudyTrackByMyOrg(orderType,key), pageNo, pageSize);
			
		}else if(selectType.equals("myManaged")){//我所管理的备课ok
			pagination=bamCourseService.getPageBySql(getStudyTrackByMyManaged(orderType,key), pageNo, pageSize);
		}
		return pagination;
	}
	
	
	/**
	 * 得到学习跟踪
	 * 
	 * @param selectType
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Map> getStudyTrackAll(String selectType,String orderType,String key,int pageNo){
		List<Map> bamCourses=null;
		if(selectType.equals("myGuidance")){//我指导的备课ok
			bamCourses= (List<Map>) bamCourseService.getPageBySql(getStudyTrackByMyGuidance(orderType,key), pageNo, 20000).getList();
		}else if(selectType.equals("myOrganized")){//我组织的备课ok
			bamCourses= (List<Map>) bamCourseService.getPageBySql(getStudyTrackByMyGuidance(orderType,key), pageNo, 20000).getList();
		}else if(selectType.equals("myDepart")){//我所在部门的备课
			bamCourses= (List<Map>) bamCourseService.getPageBySql(getStudyTrackByMyGuidance(orderType,key), pageNo, 20000).getList();
			
		}else if(selectType.equals("myOrg")){//我所在机构的备课
			bamCourses= (List<Map>) bamCourseService.getPageBySql(getStudyTrackByMyGuidance(orderType,key), pageNo, 20000).getList();
			
		}else if(selectType.equals("myManaged")){//我所管理的备课ok
			bamCourses= (List<Map>) bamCourseService.getPageBySql(getStudyTrackByMyGuidance(orderType,key), pageNo, 20000).getList();
		}
		return bamCourses;
	}
	
	/**
	 * 我组织的备课
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Finder getStudyTrackByMyOrganized(String orderType,String key){

		Finder finder = Finder.create("");		
		finder.append(" SELECT b.FDID bamId,c.FDID courseId,b.PRETEACHID preId,o1.fdid guiId from");
		finder.append(" (");
		finder.append(" select * from IXDF_NTP_BAM_SCORE bamc ");
		finder.append(" where bamc.COURSEID in ");
		finder.append(" (select course.FDID from IXDF_NTP_COURSE course where course.FDCREATORID='"+ShiroUtils.getUser().getId()+"') ");
		finder.append(" or ");
		finder.append(" bamc.COURSEID in (select courseAuth.FDCOURSEID from IXDF_NTP_COURSE_AUTH courseAuth where courseAuth.ISAUTHSTUDY=1 and courseAuth.FDUSERID='"+ShiroUtils.getUser().getId()+"')");
		finder.append(" )  b ");
		finder.append("  left join IXDF_NTP_COURSE c on b.COURSEID = c.FDID left join SYS_ORG_PERSON o1 on b.GUIDETEACHID = o1.fdid left join SYS_ORG_PERSON o2 on b.PRETEACHID = o2.fdid");
		finder.append("    where o2.realname like '%"+key+"%'");
		finder = addOrder(finder, orderType);
		return finder;//bamCourseService.getPageBySql(finder, pageNo, pageSize);
		
	}
	
	/**
	 * 我指导的备课
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Finder getStudyTrackByMyGuidance(String orderType,String key){
		Finder finder = Finder
				.create("");		
		finder.append(" SELECT b.FDID bamId,c.FDID courseId,b.PRETEACHID preId,o1.fdid guiId from");
		finder.append(" (");
		finder.append(" select * from IXDF_NTP_BAM_SCORE  bamc ");
		finder.append(" where bamc.GUIDETEACHID = '"+ShiroUtils.getUser().getId()+"'");
		finder.append(" )  b ");
		finder.append("  left join IXDF_NTP_COURSE c on b.COURSEID = c.FDID left join SYS_ORG_PERSON o1 on b.GUIDETEACHID = o1.fdid left join SYS_ORG_PERSON o2 on b.PRETEACHID = o2.fdid");
		finder.append("    where o2.realname like '%"+key+"%'");
		finder = addOrder(finder, orderType);
		return finder;//bamCourseService.getPageBySql(finder, pageNo, pageSize);
	}
	
	/**
	 * 我所在部门的备课
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Finder getStudyTrackByMyDepart(String orderType,String key){
		Finder finder = Finder
				.create("");
		finder.append(" SELECT b.FDID bamId,c.FDID courseId,b.PRETEACHID preId,o1.fdid guiId from ");
		finder.append("  ( ");
		finder.append(" select * from IXDF_NTP_BAM_SCORE  bamc  ");
		finder.append(" where bamc.PRETEACHID in  ");
		finder.append(" (select person.FDID from SYS_ORG_PERSON person where person.depatid='"+((SysOrgPerson)accountService.get(ShiroUtils.getUser().getId())).getDepatId()+"') ");
		finder.append("  )  b  ");
		finder.append("   left join IXDF_NTP_COURSE c on b.COURSEID = c.FDID left join SYS_ORG_PERSON o1 on b.GUIDETEACHID = o1.fdid left join SYS_ORG_PERSON o2 on b.PRETEACHID = o2.fdid");
		finder.append("    where o2.realname like '%"+key+"%'");
		finder = addOrder(finder, orderType);
		return finder;//bamCourseService.getPageBySql(finder, pageNo, pageSize);
	}
	
	/**
	 * 我所在机构的备课
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Finder getStudyTrackByMyOrg(String orderType,String key){
		SysOrgPerson orgPerson = (SysOrgPerson)accountService.get(ShiroUtils.getUser().getId());
		Finder finder = Finder
				.create("");
		finder.append(" SELECT b.FDID bamId,c.FDID courseId,b.PRETEACHID preId,o1.fdid guiId from ");
		finder.append("  ( ");
		finder.append(" select * from IXDF_NTP_BAM_SCORE  bamc  ");
		finder.append(" where bamc.PRETEACHID in ");
		finder.append(" (select person.FDID from SYS_ORG_PERSON person ,SYS_ORG_ELEMENT dep,SYS_ORG_ELEMENT org");
		finder.append("  where dep.fd_parentid = org.fdid and person.depatid=dep.fdid and org.fdid='"+(orgPerson.getHbmParent()==null?"":orgPerson.getHbmParent().getFdParentId())+"' )");
		finder.append("  )  b ");
		finder.append("   left join IXDF_NTP_COURSE c on b.COURSEID = c.FDID left join SYS_ORG_PERSON o1 on b.GUIDETEACHID = o1.fdid left join SYS_ORG_PERSON o2 on b.PRETEACHID = o2.fdid");		
		finder.append("    where o2.realname like '%"+key+"%'");
		finder = addOrder(finder, orderType);
		return finder;//bamCourseService.getPageBySql(finder, pageNo, pageSize);
	}
	
	/**
	 * 我所管理的备课
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Finder getStudyTrackByMyManaged(String orderType,String key){
		Finder finder = Finder
				.create("");
		finder.append(" SELECT b.FDID bamId,c.FDID courseId,b.PRETEACHID preId,o1.fdid guiId from");
		finder.append("  (");
		finder.append(" select * from IXDF_NTP_BAM_SCORE  bamc ");
		finder.append(" where (bamc.COURSEID in ");
		finder.append(" (select course.fdId from IXDF_NTP_COURSE course where course.FDCREATORID='"+ShiroUtils.getUser().getId()+"'))");
		finder.append("  or ");
		finder.append(" (bamc.COURSEID in (select courseAuth.FDCOURSEID from IXDF_NTP_COURSE_AUTH courseAuth where courseAuth.Isediter=1 and courseAuth.Fduserid='"+ShiroUtils.getUser().getId()+"'))"); 
		finder.append("  )  b ");
		finder.append("   left join IXDF_NTP_COURSE c on b.COURSEID = c.FDID left join SYS_ORG_PERSON o1 on b.GUIDETEACHID = o1.fdid left join SYS_ORG_PERSON o2 on b.PRETEACHID = o2.fdid");
		finder.append("    where o2.realname like '%"+key+"%'");
		finder = addOrder(finder, orderType);
		return finder;//bamCourseService.getPageBySql(finder, pageNo, pageSize);
	}
	
	/**
	 * 
	 * 添加查询排序sql
	 * 
	 * @param finder
	 * @param orderType
	 * @return
	 */
	private Finder addOrder(Finder finder ,String orderType){
		
		if(orderType==null){
			finder.append(" order by b.STARTDATE desc ");
			return finder;
		}else if(orderType.equals("course")){
			finder.append(" order by c.FDTITLE desc ");
			return finder;
		}else if(orderType.equals("newTeacher")){
			finder.append(" order by o2.REALNAME desc ");
			finder.append("");
			return finder;
		}else if(orderType.equals("teacher")){
			finder.append(" order by o1.REALNAME desc ");
			finder.append("");
			return finder;
		}else if(orderType.equals("time")){
			finder.append(" order by b.STARTDATE desc ");
			return finder;
		}else{
			return finder;
		}
	}
	
	/**
	 * 获取当前环节
	 * 
	 * @param bamId
	 * @return
	 */
	public Map passInfoByBamId(String bamId){
		Map map = new HashMap();
		BamCourse bamCourse = bamCourseService.get(BamCourse.class , bamId);
		List<CourseCatalog> catalogs =bamCourse.getCatalogs();
		ArrayUtils.sortListByProperty(catalogs, "fdTotalNo", SortType.HIGHT);
		for (int i=0;i< catalogs.size();i++) {
			CourseCatalog courseCatalog = catalogs.get(i);
			if(Constant.CATALOG_TYPE_CHAPTER == courseCatalog.getFdType()){
				continue;
			}
			List<MaterialInfo> infos = bamCourse.getMaterialByCatalog(courseCatalog.getFdId());
			for (int j=0; j<infos.size();j++) {
				MaterialInfo materialInfo = infos.get(j);
				if(materialInfo.getThrough()==true){
					map.put("courseCatalogNow", courseCatalog);
					map.put("materialInfoNow", materialInfo);
				}
			}
		}
		return map;
	}
	
	/**
	 * 获取备课跟踪
	 * 
	 * @param bamId
	 * @return
	 */
	public Map getMessageInfoByBamId(String bamId){
		Map map = new HashMap();
		Finder finder = Finder.create("");		
		finder.append(" from Message message");
		finder.append(" where message.fdType=:fdType and message.fdModelName=:fdModelName and message.fdModelId=:fdModelId ");
		finder.append(" order by message.fdCreateTime desc");
		finder.setParam("fdType", Constant.MESSAGE_TYPE_SYS);
		finder.setParam("fdModelName", BamCourse.class.getName());
		finder.setParam("fdModelId", bamId);
		Message message =messageService.find(finder)==null?null:(Message) messageService.find(finder).get(0);
		if(message!=null){
			map.put("cot", message.getFdContent());
			map.put("time", message.getFdCreateTime());
		}
		return map;
	}
	
	
	/**
	 * 根据bamId获取导出模板
	 * 
	 * @param ids
	 * @return
	 */
	public List<VStudyTrack> buildStudyTrackList(String[] ids){
		List<VStudyTrack> studyTrackList = new ArrayList<VStudyTrack>();
		for (String string : ids) {
			BamCourse bamCourse = bamCourseService.get(BamCourse.class, string);
			SysOrgPerson person = (SysOrgPerson)accountService.load(bamCourse.getPreTeachId());
			CourseInfo courseInfo = courseService.load(bamCourse.getCourseId());
			VStudyTrack vStudyTrack = new VStudyTrack();
			vStudyTrack.setUserName(person.getRealName());
			vStudyTrack.setUserDep(person.getDeptName());
			vStudyTrack.setUserTel(person.getFdWorkPhone());
			vStudyTrack.setUserEmai(person.getFdEmail());
			vStudyTrack.setCourseName(courseInfo.getFdTitle());
			if(StringUtil.isEmpty(bamCourse.getGuideTeachId())){
				vStudyTrack.setGuideTeachName("没有导师");
			}else{
				vStudyTrack.setGuideTeachName(((SysOrgPerson)accountService.load(bamCourse.getGuideTeachId())).getRealName());
			}
			Map passMap = passInfoByBamId(bamCourse.getFdId());
			String currLecture="";
			if(passMap.size()==0){
				currLecture="尚未开始学习";
			}else{
				CourseCatalog catalog = (CourseCatalog)passMap.get("courseCatalogNow");
				MaterialInfo materialInfo = (MaterialInfo) passMap.get("materialInfoNow");
				currLecture = catalog.getFdName()+"  ,  "+materialInfo.getFdName();
			}
			vStudyTrack.setLinkNow(currLecture);
			Map map2 = getMessageInfoByBamId(bamCourse.getFdId());
			if(map2.size()==0){
				vStudyTrack.setStudyInofNow("没有学习记录");
			}else{
				vStudyTrack.setStudyInofNow((String)map2.get("cot"));
			}
			studyTrackList.add(vStudyTrack);
		}
		return studyTrackList;
	}
	
	/**
	 * 根据bamList获取导出模板
	 * 
	 * @param list
	 * @return
	 */
	public List<VStudyTrack> buildStudyTrackList(List list){
		List<VStudyTrack> studyTrackList = new ArrayList<VStudyTrack>();
		for (Object obj : list) {
			Map map = (Map) obj;
			BamCourse bamCourse = bamCourseService.get(BamCourse.class, (String)map.get("BAMID"));
			SysOrgPerson person = (SysOrgPerson)accountService.load(bamCourse.getPreTeachId());
			CourseInfo courseInfo = courseService.load(bamCourse.getCourseId());
			VStudyTrack vStudyTrack = new VStudyTrack();
			vStudyTrack.setUserName(person.getRealName());
			vStudyTrack.setUserDep(person.getDeptName());
			vStudyTrack.setUserTel(person.getFdWorkPhone());
			vStudyTrack.setUserEmai(person.getFdEmail());
			vStudyTrack.setCourseName(courseInfo.getFdTitle());
			if(StringUtil.isEmpty(bamCourse.getGuideTeachId())){
				vStudyTrack.setGuideTeachName("没有导师");
			}else{
				vStudyTrack.setGuideTeachName(((SysOrgPerson)accountService.load(bamCourse.getGuideTeachId())).getRealName());
			}
			Map passMap = passInfoByBamId(bamCourse.getFdId());
			String currLecture="";
			if(passMap.size()==0){
				currLecture="尚未开始学习";
			}else{
				CourseCatalog catalog = (CourseCatalog)passMap.get("courseCatalogNow");
				MaterialInfo materialInfo = (MaterialInfo) passMap.get("materialInfoNow");
				currLecture = catalog.getFdName()+"  ,  "+materialInfo.getFdName();
			}
			vStudyTrack.setLinkNow(currLecture);
			Map map2 = getMessageInfoByBamId(bamCourse.getFdId());
			if(map2.size()==0){
				vStudyTrack.setStudyInofNow("没有学习记录");
			}else{
				vStudyTrack.setStudyInofNow((String)map2.get("cot"));
			}
			studyTrackList.add(vStudyTrack);
		}
		return studyTrackList;
	}
}
