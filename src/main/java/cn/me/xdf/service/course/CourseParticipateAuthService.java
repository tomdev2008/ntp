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
		Finder finder=Finder.create(" from CourseParticipateAuth cpa ");
		finder.append(" where cpa.course.fdId=:courseId  ");
		finder.setParam("courseId", courseId);
		if(StringUtil.isNotBlank(orderStr)){
			if("mentor".equals(orderStr)){//按导师查询
				if(StringUtil.isNotBlank(keyword)){//搜索关键字是否存在
					finder.append(" and cpa.fdTeacher.notifyEntity.realName like :namestr");
					finder.setParam("namestr", keyword);
				}
				finder.append(" order by cpa.fdTeacher.notifyEntity.realName");
			}
			if("teacher".equals(orderStr)){
				if(StringUtil.isNotBlank(keyword)){//搜索关键字是否存在
					finder.append(" and cpa.fdUser.notifyEntity.realName like :namestr");
					finder.setParam("namestr", keyword);
				}
				finder.append(" order by cpa.fdUser.notifyEntity.realName");
			}
			if("createtime".equals(orderStr)){
				finder.append(" order by cpa.fdCreateTime desc");
			}
		}
		return getPage(finder, pageNo, pageSize);
	}
	
}
