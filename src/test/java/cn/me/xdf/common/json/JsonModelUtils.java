package cn.me.xdf.common.json;

import cn.me.xdf.BaseTest;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-28
 * Time: 下午4:24
 * To change this template use File | Settings | File Templates.
 */
public class JsonModelUtils extends BaseTest {

    @Test
    public void jsonToModelTest() {
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setFdId("courseInfo_id");
        courseInfo.setFdTitle("courseInfo_title");

        List<CourseCatalog> items = new ArrayList<CourseCatalog>();

        CourseCatalog catalog1 = new CourseCatalog();
        catalog1.setFdId("catelog1_id");
        catalog1.setFdName("第一节");
        catalog1.setCourseInfo(courseInfo);
        catalog1.setFdDescription("说明");
        catalog1.setHbmParent(null);

        items.add(catalog1);
        courseInfo.setCatalogList(items);

        String jsonStr = JsonUtils.writeObjectToJson(courseInfo);
        System.out.println("json===" + jsonStr);
        CourseInfo courseJson = JsonUtils.readObjectByJson(jsonStr, CourseInfo.class);
        System.out.println("courseJson.fdTitle===" + courseJson.getFdTitle());
    }
}
