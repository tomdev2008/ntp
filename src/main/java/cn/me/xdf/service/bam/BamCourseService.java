package cn.me.xdf.service.bam;

import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.course.CourseParticipateAuth;
import cn.me.xdf.service.SimpleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-11-4
 * Time: 上午9:47
 * 备课老师对应课程存储的service
 */
@Service
@Transactional(readOnly = true)
public class BamCourseService extends SimpleService {

    /**
     * @param course                 对应课程
     * @param courseParticipateAuths 课程参与人员
     */
    public void saveBamCourse(CourseInfo course, CourseParticipateAuth courseParticipateAuths) {
        //课程章节
        List<CourseCatalog> courseCatalogs = findByCriteria(CourseCatalog.class, Value.eq("courseInfo.fdId", course.getFdId()));
        //课程关系素材实体
        List<CourseContent> courseContents = findByCriteria(CourseContent.class, Value.eq("catalog.courseInfo.fdId", course.getFdAuthor()));

        String courseJson = JsonUtils.writeObjectToJsonWithHibernate(course);
        String courseCatalogJson = JsonUtils.writeObjectToJson(courseCatalogs);
        String courseContentJson = JsonUtils.writeObjectToJson(courseContents);


    }
}
