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
		Finder finder=Finder.create(" from CourseParticipateAuth cpa left join cpa.fdTeacher person");
//		Finder finder=Finder.create("from CourseParticipateAuth cpa ");//该方式会过滤掉无导师课程
		finder.append(" where cpa.course.fdId=:courseId  ");
		finder.setParam("courseId", courseId);
		if("mentor".equals(orderStr)){//按导师查询
			if(StringUtil.isNotBlank(keyword)){//搜索关键字是否存在
				finder.append(" and (cpa.fdTeacher.realName like :namestr  or cpa.fdTeacher.hbmParent.fdName like:deptstr)");
				finder.setParam("namestr", "%"+keyword+"%");
				finder.setParam("deptstr", "%"+keyword+"%");
			}
			finder.append(" order by nlssort(person.realName,'NLS_SORT=SCHINESE_PINYIN_M')");
		}else if("teacher".equals(orderStr)){
			if(StringUtil.isNotBlank(keyword)){//搜索关键字是否存在
				finder.append(" and (cpa.fdUser.realName like :namestr  or cpa.fdUser.hbmParent.fdName like:deptstr)" );
				finder.setParam("namestr", "%"+keyword+"%");
				finder.setParam("deptstr", "%"+keyword+"%");
			}
			finder.append(" order by nlssort(cpa.fdUser.realName,'NLS_SORT=SCHINESE_PINYIN_M')");
		}else if("createtime".equals(orderStr)){
			if(StringUtil.isNotBlank(keyword)){//搜索关键字是否存在
				finder.append("and ( cpa.fdUser.realName like :namestr  or cpa.fdUser.hbmParent.fdName like:deptstr)");
				finder.setParam("namestr", "%"+keyword+"%");
				finder.setParam("deptstr", "%"+keyword+"%");
			}
			finder.append(" order by cpa.fdCreateTime desc");
		}else{
			finder.append(" order by cpa.fdCreateTime desc");
		}
		
		return getPage(finder, pageNo, pageSize);
	}
	/**
	 * 某课程授权教师检查
	 */
	@Transactional(readOnly=false)
	public boolean findCouseParticipateAuthById(String courseId,String teacherId){
		Finder finder=Finder.create(" from CourseParticipateAuth cpa");
		finder.append(" where cpa.fdUser.fdId=:teacherId and cpa.course.fdId=:courseId");
		finder.setParam("teacherId", teacherId);
		finder.setParam("courseId", courseId);
		List list=find(finder);
		if(list!=null&&list.size()>0){
			return false;
		}else{
			return true;
		}
		
	}
}
