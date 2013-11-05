package cn.me.xdf.service.bam;

import cn.me.xdf.BaseTest;
import cn.me.xdf.model.course.CourseInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-11-4
 * Time: 上午11:08
 * To change this template use File | Settings | File Templates.
 */
public class BamCourseServiceTest extends BaseTest {

    @Autowired
    private BamCourseService bamCourseService;

    @Test
    public void testSaveBamCourse() {
        //测试
        CourseInfo courseInfo = bamCourseService.get(CourseInfo.class, "142228b652bcd770db6af914aeb8952f");
        bamCourseService.saveBamCourse(courseInfo, null);
    }
}