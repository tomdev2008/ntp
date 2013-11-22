package cn.me.xdf.service.studyTack;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.utils.ShiroUtils;

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
			pagination=getStudyTrackByMyGuidance(pageNo, pageSize, orderType,key);
			
		}else if(selectType.equals("myOrganized")){//我组织的备课ok
			pagination=getStudyTrackByMyOrganized(pageNo, pageSize, orderType,key);
			
		}else if(selectType.equals("myDepart")){//我所在部门的备课
			pagination=getStudyTrackByMyDepart(pageNo, pageSize, orderType,key);
			
		}else if(selectType.equals("myOrg")){//我所在机构的备课
			pagination=getStudyTrackByMyOrg(pageNo, pageSize, orderType,key);
			
		}else if(selectType.equals("myManaged")){//我所管理的备课ok
			pagination=getStudyTrackByMyManaged(pageNo, pageSize, orderType,key);
			
		}
		return pagination;
	}
	
	/**
	 * 我组织的备课
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Pagination getStudyTrackByMyOrganized(int pageNo,int pageSize,String orderType,String key){

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
		return bamCourseService.getPageBySql(finder, pageNo, pageSize);
		
	}
	
	/**
	 * 我指导的备课
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Pagination getStudyTrackByMyGuidance(int pageNo,int pageSize,String orderType,String key){
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
		return bamCourseService.getPageBySql(finder, pageNo, pageSize);
	}
	
	/**
	 * 我所在部门的备课
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Pagination getStudyTrackByMyDepart(int pageNo,int pageSize,String orderType,String key){
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
		return bamCourseService.getPageBySql(finder, pageNo, pageSize);
	}
	
	/**
	 * 我所在机构的备课
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Pagination getStudyTrackByMyOrg(int pageNo,int pageSize,String orderType,String key){
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
		return bamCourseService.getPageBySql(finder, pageNo, pageSize);
	}
	
	/**
	 * 我所管理的备课
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private Pagination getStudyTrackByMyManaged(int pageNo,int pageSize,String orderType,String key){
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
		return bamCourseService.getPageBySql(finder, pageNo, pageSize);
	}
	
	private Finder addOrder(Finder finder ,String orderType){
		if(orderType.equals("course")){
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
