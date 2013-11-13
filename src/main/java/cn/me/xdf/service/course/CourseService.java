package cn.me.xdf.service.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseAuth;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.organization.User;
import cn.me.xdf.service.BaseService;
import cn.me.xdf.utils.ShiroUtils;
/**
 * 
 * 课程service
 * 
 * @author zhaoq
 * 
 */
@Service
@Transactional(readOnly = false)
public class CourseService  extends BaseService{
	
	@Autowired
	private CourseParticipateAuthService courseParticipateAuthService;
	
	@Autowired
	private CourseAuthService courseAuthService;
	
	@SuppressWarnings("unchecked")
	@Override
	public  Class<CourseInfo> getEntityClass() {
		return CourseInfo.class;
	}
	
	/**
	 * 修改课程权限
	 */
	public void updateCourseAuth(String courseId,List<CourseAuth> courseAuths){
		//删除所有相关的权限信息
		courseAuthService.deleCourseAuthByCourseId(courseId);
		//插入权限信息
		for (CourseAuth courseAuth : courseAuths) {
			courseAuthService.save(courseAuth);
		}
	}
	/*
	 * 根据课程名称模糊搜索
	 * 
	 * 
	 * author hanhl
	 * */
	public  Pagination findCourseInfosByName(String fdName,String pageNo ,String orderbyStr,String seleType){

		String userId = ShiroUtils.getUser().getId();
		Finder finder = Finder.create("select course.*,scorestatis.fdaverage from IXDF_NTP_COURSE course ");
		finder.append(" left join IXDF_NTP_SCORE_STATISTICS scorestatis " );
		finder.append(" on course.fdid=scorestatis.fdmodelid  and scorestatis.fdmodelname='"+CourseInfo.class.getName()+"' ");
		//课程列表中有效的
		finder.append("where course.isavailable='1'");/*有效的*/
		if(Constant.COUSER_AUTH_MANAGE.equals(seleType)){//课程授权
		  //非公开  已发布  无密码
			finder.append(" and course.ispublish=0  and  course.fdstatus='01'  and course.fdpassword is null ");
		}
		//如果是管理员就显示所有有效的
		if(!ShiroUtils.isAdmin()){
			if(Constant.COUSER_TEMPLATE_MANAGE.equals(seleType)){//课程模版查询
			//已发布的课程
			finder.append("and ( course.fdstatus='01' or ");
			//当前登录用户自己创建的
			finder.append("  course.fdcreatorid=:createId  or");
			//有编辑权限的
			finder.append("	exists (select auth.fdid from ixdf_ntp_course_auth auth where auth.fdcourseid=course.fdid and auth.isediter=1 and fduserid=:userId)	)");
			finder.setParam("userId", userId);
			finder.setParam("createId", userId);
			}
			if(Constant.COUSER_AUTH_MANAGE.equals(seleType)){//课程授权
				finder.append(" and exists (select auth.fdid from ixdf_ntp_course_auth auth where auth.fdcourseid=course.fdid and auth.isauthstudy=1 and fduserid=:userId)");
				finder.setParam("userId", userId);
			}
		}
		//设置页码
		int pageNoI=0;
		if(StringUtil.isNotBlank(pageNo)&&StringUtil.isNotEmpty(pageNo)){
			pageNoI = NumberUtils.createInteger(pageNo);
		} else {
			pageNoI = 1;
		}
		//根据关键字搜索
		if(!("").equals(fdName)&&fdName!=null){
			finder.append("and  ( course.fdtitle like :ft  or course.fdsubtitle like :fs )");
			finder.setParam("ft", "%"+fdName+"%");
			finder.setParam("fs", "%"+fdName+"%");
		}
		//排序
		if(StringUtil.isNotBlank(orderbyStr)&&StringUtil.isNotEmpty(orderbyStr)){
	        if(orderbyStr.equalsIgnoreCase("fdtitle")){
	        	finder.append(" order by course.fdtitle desc ");
	        }else if(orderbyStr.equalsIgnoreCase("fdcreatetime")){
	        	finder.append(" order by course.fdcreatetime desc");
	        }else if(orderbyStr.equalsIgnoreCase("fdscorce")){
	        	finder.append(" order by scorestatis.fdaverage desc");
	        }
		}else{
			finder.append(" order by course.fdcreatetime desc");
		}
		Pagination pagination=getPageBySql(finder, pageNoI, SimplePage.DEF_COUNT);
		return pagination;
	}
	
	public List<Map> findAuthInfoByCourseId(String courseId){
		//获取课程ID
		List<CourseAuth> auths = courseAuthService.findByProperty("course.fdId", courseId);
		List<Map> list = new ArrayList<Map>();
		User user = null;
		for (int i=0;i<auths.size();i++) {
			CourseAuth courseAuth = auths.get(i);
			SysOrgPerson  person = courseAuth.getFdUser();
			Map map= new HashMap();
			map.put("id", person.getFdId());
			map.put("index", i);
			map.put("imgUrl",person.getPoto());
			map.put("name",person.getRealName());
			map.put("mail",person.getFdEmail());
			map.put("org","");
			map.put("department",person.getDeptName());
			map.put("tissuePreparation", courseAuth.getIsAuthStudy());
			map.put("editingCourse",courseAuth.getIsEditer());
			list.add(map);
		}
		return list;
	}

	/**
	 * 分页获取最新课程列表
	 * 
	 * @param userId 用户ID
	 * @param pageNo 当前页数
	 * @param pageSize 每页记录数
	 * @return Pagination
	 */
	@Transactional(readOnly = true)
	public Pagination discoverCourses(String userId, int pageNo, int pageSize) {
		Finder finder = Finder.create(" select c.* from ixdf_ntp_course c where c.isAvailable=1 and c.fdStatus=:fdStatus and ( ");
		finder.append(" c.isPublish=1 or (fdPassword is not null and fdPassword <> '') or exists ( ");
		finder.append(" select p.fdid from ixdf_ntp_course_partici_auth p where p.fdcourseid = c.fdid and p.fduserid = :userId  )) order by c.fdCreateTime desc ");
		finder.setParam("fdStatus", Constant.COURSE_TEMPLATE_STATUS_RELEASE);
		finder.setParam("userId", userId);
		Pagination page = getPageBySql(finder, pageNo, pageSize);
		return page;
	}
	
}
