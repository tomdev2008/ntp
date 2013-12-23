package cn.me.xdf.service.course;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import cn.me.xdf.model.course.CourseParticipateAuth;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.organization.User;
import cn.me.xdf.service.BaseService;
import cn.me.xdf.utils.ShiroUtils;
import cn.me.xdf.view.model.VCourseAuth;
import cn.me.xdf.view.model.VCourseAuthList;
import cn.me.xdf.view.model.VPersonAuth;
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
	
	public List<VCourseAuthList> findAllCourseInfoAuth(List list){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd h:m:s a");
		List<VCourseAuthList> authLsit = new ArrayList<VCourseAuthList>();
		for (Object obj : list) {
			VCourseAuthList auth = new VCourseAuthList();
			Map map = (Map) obj;
			CourseInfo info = this.get((String)map.get("FDID"));
			auth.setFdTitle(info.getFdTitle());
			List<CourseParticipateAuth> participateAuth = 
					courseParticipateAuthService.findByProperty("course.fdId", info.getFdId());
			List<VPersonAuth> teacher = new ArrayList<VPersonAuth>();
			for (CourseParticipateAuth pAuth : participateAuth) {
				VPersonAuth teacherAuth = new VPersonAuth();
				teacherAuth.setFdName(pAuth.getFdUser().getFdName());
				teacherAuth.setFdDept(pAuth.getFdUser().getDeptName());
				teacherAuth.setFdCreateTime(sdf.format(pAuth.getFdCreateTime()));
				
				if(pAuth.getFdTeacher()!=null){
					teacherAuth.setAdviserName(pAuth.getFdTeacher().getFdName());
					teacherAuth.setAdviserDept(pAuth.getFdTeacher().getDeptName());
				}else{
					teacherAuth.setAdviserName("无导师");
					teacherAuth.setAdviserDept("");
				}
				teacher.add(teacherAuth);
			}
			auth.setPersonAuth(teacher);
			authLsit.add(auth);
		}
		return authLsit;
	}
	/**
	 * 封装导出授权列表的list
	 * @param pagination
	 * @return
	 */
	public List<VCourseAuth> findCourseInfoAuth(List list){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd h:m:s a");
		List<VCourseAuth> authLsit = new ArrayList<VCourseAuth>();
		VCourseAuth auth = new VCourseAuth();
		for(int i=0;i<list.size();i++){
			Object [] obj=(Object[]) list.get(i);
			CourseParticipateAuth cpa =(CourseParticipateAuth)obj[0] ;
			SysOrgPerson teacher = cpa.getFdUser();
		    auth.setTeacherDept(teacher.getDeptName());
		    auth.setTeacherName(teacher.getFdName());
		    SysOrgPerson adviser = cpa.getFdTeacher();
		    if(adviser!=null){
				auth.setAdviserName(adviser.getFdName());
				auth.setAdviserDept(adviser.getDeptName());	
		    }else{
		    	auth.setAdviserName("无导师");
				auth.setAdviserDept("");	
		    }
			auth.setFdCreateTime(sdf.format(cpa.getFdCreateTime()));
			authLsit.add(auth);
		}
		return authLsit;
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
		Finder finder = Finder.create("select course.*,scorestatis.fdaverage,");
		//课程权限
		if(ShiroUtils.isAdmin()){
			finder.append(" '1'");
		} else {
			finder.append(" case when  course.fdcreatorid = '"+ShiroUtils.getUser().getId()+"' then '1'");
			finder.append(" else (case when temp.fdcourseid is null then '0' else '1' end ) end");
			
		}
		finder.append(" as authflag,");
		//创建者
		finder.append(" sysperson.fd_name as creatorName");
		
		finder.append(" from IXDF_NTP_COURSE course ");
		finder.append(" left join IXDF_NTP_SCORE_STATISTICS scorestatis " );
		finder.append(" on course.fdid=scorestatis.fdmodelid  and scorestatis.fdmodelname='"+CourseInfo.class.getName()+"' ");
		
		//可编辑的
		finder.append(" left join (select ma.fdcourseid from Ixdf_Ntp_Course_Auth ma");
		finder.append(" where ma.isediter='Y' and ma.fduserid='"+ShiroUtils.getUser().getId()+"') temp");
		finder.append(" on course.fdid = temp.fdcourseid");
		//找出创建者
		finder.append(" left join (select person.fdid,person.fd_name from Sys_Org_Element person ) sysperson on sysperson.fdid = course.fdcreatorid");
		 
		
		//课程列表中有效的
		finder.append("where course.isavailable='Y'");/*有效的*/
		
		
		if(Constant.COUSER_AUTH_MANAGE.equals(seleType)){//课程授权
		  //非公开  已发布  无密码
			finder.append(" and course.ispublish='N'  and  course.fdstatus='01'  and course.fdpassword is null ");
		}
		//如果是管理员就显示所有有效的
		if(!ShiroUtils.isAdmin()){
			if(Constant.COUSER_TEMPLATE_MANAGE.equals(seleType)){//课程模版查询
			//当前登录用户自己创建的
			finder.append(" and ( course.fdcreatorid=:createId  or ");
			finder.setParam("createId", userId);
			//已发布的课程
			finder.append("  course.fdstatus='01' and (course.ispublish='Y' or course.fdpassword is  not null or ");
			//有编辑权限的
			finder.append("	exists (select auth.fdid from ixdf_ntp_course_auth auth where auth.fdcourseid=course.fdid and (auth.isauthstudy='Y' or auth.isediter='Y') and fduserid=:userId)	) )");
			finder.setParam("userId", userId);
			}
			if(Constant.COUSER_AUTH_MANAGE.equals(seleType)){//课程授权
				//当前登录用户自己创建的
				finder.append(" and ( course.fdcreatorid=:createId  or ");
				finder.setParam("createId", userId);
				finder.append("  exists (select auth.fdid from ixdf_ntp_course_auth auth where auth.fdcourseid=course.fdid and auth.isauthstudy='Y' and fduserid=:userId) )");
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
	        	finder.append(" order by nvl(scorestatis.fdaverage,0) desc");
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
	public Pagination discoverCourses(int pageNo, int pageSize) {
		Finder finder = Finder.create(" select c.fdid,c.fdtitle,c.fdauthor from ixdf_ntp_course c where c.isAvailable='Y' and c.fdStatus=:fdStatus order by c.fdCreateTime desc ");
		finder.setParam("fdStatus", Constant.COURSE_TEMPLATE_STATUS_RELEASE);
		Pagination page = getPageBySql(finder, pageNo, pageSize);
		return page;
	}
	/**
	 * 查找课程
	 */
	public List<CourseInfo> findCourseInfoByCouseNameTop10(String key) {
		Finder finder = Finder.create(" from  CourseInfo c ");
		finder.append("where  c.isAvailable='Y'  and (c.fdTitle like :key1  or c.fdSubTitle like :key2)");
		finder.setParam("key1", "%"+key+"%");
		finder.setParam("key2", "%"+key+"%");
		if(!ShiroUtils.isAdmin()){
			//已发布的课程
			finder.append("and  c.fdStatus='01'  ");
			//当前登录用户自己创建的
			finder.append(" and  ( c.creator.fdId=:createId  or c.isPublish='Y' or c.fdPassword is not null or ");
			//有编辑权限的
			finder.append("	exists (select auth.fdId from CourseAuth auth where auth.course.fdId=c.fdId and (auth.isAuthStudy='Y' or auth.isEditer='Y') and auth.fdUser.fdId=:userId))	");
			finder.setParam("userId", ShiroUtils.getUser().getId());
			finder.setParam("createId", ShiroUtils.getUser().getId());
		}
		return (List<CourseInfo>)getPage(finder, 1,10).getList();
	}
	
}
