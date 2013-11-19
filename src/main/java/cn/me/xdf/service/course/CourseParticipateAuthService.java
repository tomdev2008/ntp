package cn.me.xdf.service.course;

import java.util.List;

import jodd.util.StringUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.course.CourseParticipateAuth;
import cn.me.xdf.service.BaseService;
/**
 * 
 * 课程参与权限service
 * 
 * @author zhaoq
 * 
 */
@Service
@Transactional(readOnly = true)
public class CourseParticipateAuthService extends BaseService{

	@SuppressWarnings("unchecked")
	@Override
	public  Class<CourseParticipateAuth> getEntityClass() {
		return CourseParticipateAuth.class;
	}
	
	/**
	 * 添加或修改参与人
	 * 
	 * @return CourseParticipateAuth
	 */
	@Transactional(readOnly = false)
	public CourseParticipateAuth saveOrUpdateCourseParticipateAuth(CourseParticipateAuth CourseParticipateAuth){
		List<CourseParticipateAuth> auth = findByProperty("fdId",CourseParticipateAuth.getFdId());
		if(auth.size()<=0){
			return save(CourseParticipateAuth);
		}else{
			return update(auth.get(0));
		}
	}
	/**
	 * 某课程授权列表
	 */
	@Transactional(readOnly=false)
	public Pagination findSingleCourseAuthList(String courseId,String orderStr,Integer pageNo,Integer pageSize,String keyword){
		Finder finder=Finder.create(" select cpa.fdid as fdid,cpa.fdcreatetime as fdcreatetime,cpa.fdcourseid as fdcourseid," +
				"sop.fdid as tid,sop.realname as trealname,sop.fd_email as temail,sop.depatid as depatid,soe.fd_name as departname," +
				"sop1.fdid as mid,sop1.realname as mrealname,sop1.fd_email as memail,sop1.depatid as depatid,soe1.fd_name as departname " );
		finder.append(" from IXDF_NTP_COURSE_PARTICI_AUTH cpa ");
		finder.append(" left join SYS_ORG_PERSON sop on cpa.fdteacherid=sop.fdid ");//教师信息
		finder.append(" left join SYS_ORG_PERSON sop1 on cpa.fduserid=sop1.fdid ");//导师信息
		finder.append(" left join SYS_ORG_ELEMENT soe on sop.depatid=soe.fdid ");//教师部门信息
		finder.append(" left join SYS_ORG_ELEMENT soe1 on sop1.depatid=soe1.fdid ");//导师部门信息
		finder.append(" where cpa.fdcourseid=:courseId  ");
		finder.setParam("courseId", courseId);
		if(StringUtil.isNotBlank(orderStr)){
			if("mentor".equals(orderStr)){//按导师查询
				if(StringUtil.isNotBlank(keyword)){//搜索关键字是否存在
					finder.append(" and sop1.realname like :namestr");
					finder.setParam("namestr", keyword);
				}
				finder.append(" order by sop1.realname");
			}
			if("teacher".equals(orderStr)){
				if(StringUtil.isNotBlank(keyword)){//搜索关键字是否存在
					finder.append(" and sop.realname like :namestr");
					finder.setParam("namestr", keyword);
				}
				finder.append(" order by sop.realname");
			}
			if("createtime".equals(orderStr)){
				finder.append(" order by cpa.createtime desc");
			}
		}
		return getPageBySql(finder, pageNo, pageSize);
	}
	
}
