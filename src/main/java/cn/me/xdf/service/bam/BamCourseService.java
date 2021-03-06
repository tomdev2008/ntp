package cn.me.xdf.service.bam;

import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.utils.MyBeanUtils;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.bam.CourseLogic;
import cn.me.xdf.model.bam.CourseReLoad;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.course.CourseParticipateAuth;
import cn.me.xdf.service.SimpleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import jodd.util.StringUtil;


/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-11-4
 * Time: 上午9:47
 * 备课老师对应课程存储的service
 * <p/>
 * 一定要小心操作BamCourse类，危险！
 */
@Service
@Transactional(readOnly = true)
public class BamCourseService extends SimpleService {

    /**
     * 设置进入此课程的开始时间
     */
    public void updateCourseStartTime(String fdId) {
        BamCourse bamCourse = get(BamCourse.class, fdId);
        if (bamCourse.getStartDate() == null) {
            bamCourse.setStartDate(new Date());
            update(bamCourse);
        }
    }

    public void updateCatalogThrough(BamCourse bamCourse, String catalogId) {

    }

    /**
     * 更新某一节的开始时间
     *
     * @param bamId
     * @param catalogId
     */
    public void updateCourseCatalogStartTime(String bamId, String catalogId) {
        BamCourse bamCourse = get(BamCourse.class, bamId);
        List<CourseCatalog> catalogs = bamCourse.getCatalogs();
        boolean isNotEnableStartDate = false;
        for (CourseCatalog catalog : catalogs) {
            if (catalog.getFdId().equals(catalogId)) {
                if (catalog.getStartDate() == null) {
                    isNotEnableStartDate = true;
                    catalog.setStartDate(new Date());
                }
            }
        }
        if (isNotEnableStartDate) {
            bamCourse.setCatalogJson(JsonUtils.writeObjectToJson(catalogs));
            update(bamCourse);
        }
    }

    /**
     * 根据备课老师和课程的ID获取此次备课的信息
     *
     * @param userId
     * @param courseId
     * @return
     */
    public BamCourse getCourseByUserIdAndCourseId(String userId, String courseId) {
        return findUniqueByProperty(BamCourse.class, Value.eq("preTeachId", userId), Value.eq("courseId", courseId));
    }

    /**
     * *******************************************************************************************
     * writer
     * *********************************************************************************************
     */
    /**
     * 对备课老师授权课程
     *
     * @param course 课程信息
     * @param userId 备课老师ID
     */
    @Transactional(readOnly = false)
    public void saveBamCourse(CourseInfo course, String userId) {
        //课程章节
        List<CourseCatalog> courseCatalogs = findByCriteria(CourseCatalog.class, Value.eq("courseInfo.fdId", course.getFdId()));
        List<Object> catalogId = getCatalogIds(courseCatalogs);
        //课程关系素材实体
        List<CourseContent> courseContents = findByCriteria(CourseContent.class, Value.in("catalog.fdId", catalogId));

        String courseJson = JsonUtils.writeObjectToJson(course);
        String courseCatalogJson = JsonUtils.writeObjectToJson(courseCatalogs);
        String courseContentJson = JsonUtils.writeObjectToJson(courseContents);

        BamCourse bamCourse;
        //公开课  或者 非公开 加密的
        if (course.getIsPublish()||
        		(!course.getIsPublish()&&StringUtil.isNotBlank(course.getFdPassword()))) {
            bamCourse = new BamCourse(userId, null, course.getFdId(),
                    courseJson, courseCatalogJson, courseContentJson);
        } else {
            CourseParticipateAuth auth = findUniqueByProperty(CourseParticipateAuth.class, Value.eq("course.fdId", course.getFdId()), Value.eq("fdUser.fdId", userId));
            bamCourse = new BamCourse(auth.getFdUser().getFdId(), auth.getFdTeacher()==null?null:auth.getFdTeacher().getFdId(), course.getFdId(),
                    courseJson, courseCatalogJson, courseContentJson);
        }
        save(bamCourse);
    }
    
    /**
     * 重新根据课程模板更新进程
     *
     * @param course 课程信息
     * @param userId 备课老师ID
     */
    public BamCourse updateBamCourse(CourseInfo course, String userId) {
        //课程章节
        List<CourseCatalog> courseCatalogs = findByCriteria(CourseCatalog.class, Value.eq("courseInfo.fdId", course.getFdId()));
        List<Object> catalogId = getCatalogIds(courseCatalogs);
        //课程关系素材实体
        List<CourseContent> courseContents = findByCriteria(CourseContent.class, Value.in("catalog.fdId", catalogId));

        String courseJson = JsonUtils.writeObjectToJson(course);
        String courseCatalogJson = JsonUtils.writeObjectToJson(courseCatalogs);
        String courseContentJson = JsonUtils.writeObjectToJson(courseContents);

        BamCourse newBamCourse;
        //公开课  或者 非公开 加密的
        if (course.getIsPublish()||
        		(!course.getIsPublish()&&StringUtil.isNotBlank(course.getFdPassword()))) {
        	newBamCourse = new BamCourse(userId, null, course.getFdId(),
                    courseJson, courseCatalogJson, courseContentJson);
        } else {
            CourseParticipateAuth auth = findUniqueByProperty(CourseParticipateAuth.class, Value.eq("course.fdId", course.getFdId()), Value.eq("fdUser.fdId", userId));
            newBamCourse = new BamCourse(auth.getFdUser().getFdId(), auth.getFdTeacher()==null?null:auth.getFdTeacher().getFdId(), course.getFdId(),
                    courseJson, courseCatalogJson, courseContentJson);
        }
        
        BamCourse hisBamCourse = getCourseByUserIdAndCourseId(userId,course.getFdId());
        CourseReLoad courseReLoad = new CourseReLoad(hisBamCourse,newBamCourse);
        return save(courseReLoad.getBamCourse());
    }

    /**
     * 根据课程节获取所有课程节的ID
     *
     * @param courseCatalogs
     * @return
     */
    private List<Object> getCatalogIds(List<CourseCatalog> courseCatalogs) {
        try {
            return MyBeanUtils.getPropertyByList(courseCatalogs, "fdId");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据课程ID将该课程所有进程记录设置为需要更新
     *
     * @param courseId
     * 
     */
	public void setCourseIsUpdate(String courseId) {
		// TODO Auto-generated method stub
		String sql = "update ixdf_ntp_bam_score set isUpdate=? where courseId=?";
		super.executeSql(sql, "Y",courseId);
	}


}
