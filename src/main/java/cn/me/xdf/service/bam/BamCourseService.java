package cn.me.xdf.service.bam;

import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.utils.MyBeanUtils;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.course.CourseParticipateAuth;
import cn.me.xdf.service.SimpleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-11-4
 * Time: 上午9:47
 * 备课老师对应课程存储的service
 *
 * 一定要小心操作BamCourse类，危险！
 *
 */
@Service
@Transactional(readOnly = true)
public class BamCourseService extends SimpleService {


    /**********************************************************************************************
     *writer
     **********************************************************************************************
     */

    /**
     * 1：对应授权课程
     *
     * @param course                 对应课程
     * @param courseParticipateAuths 课程参与人员
     */
    @Transactional(readOnly = false)
    public void saveBamCourse(CourseInfo course, CourseParticipateAuth... courseParticipateAuths) {
        //课程章节
        List<CourseCatalog> courseCatalogs = findByCriteria(CourseCatalog.class, Value.eq("courseInfo.fdId", course.getFdId()));
        List<Object> catalogId = getCatalogIds(courseCatalogs);
        //课程关系素材实体
        List<CourseContent> courseContents = findByCriteria(CourseContent.class, Value.in("catalog.fdId", catalogId));

        String courseJson = JsonUtils.writeObjectToJson(course);
        String courseCatalogJson = JsonUtils.writeObjectToJson(courseCatalogs);
        String courseContentJson = JsonUtils.writeObjectToJson(courseContents);

        BamCourse bamCourse;

        if (courseParticipateAuths != null && courseParticipateAuths.length > 0) {
            for (CourseParticipateAuth auth : courseParticipateAuths) {
                if (!auth.getCourse().getFdId().equals(course.getFdId())) {
                    continue;
                }
                bamCourse = new BamCourse(auth.getFdUser().getFdId(), auth.getFdTeacher().getFdId(), auth.getCourse().getFdId(),
                        courseJson, courseCatalogJson, courseContentJson);
                save(bamCourse);
            }
        }

    }


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


}
