package cn.me.xdf.common.json;

import cn.me.xdf.JunitBaseTest;
import cn.me.xdf.common.json.hibernate4.Hibernate4Module;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-29
 * Time: 上午11:07
 * To change this template use File | Settings | File Templates.
 */
public class OneToManyTest extends JunitBaseTest {

    static final String EXPECTED_JSON = "{\"m\":{\"A\":\"A\"}}";

    static final class X {
        @OneToMany
        public final Map<String, String> m = new LinkedHashMap<String, String>();
    }

    static final class Y {
        public final Map<String, String> m = new LinkedHashMap<String, String>();
    }

    public void testMap() throws Exception {
        Y object = new Y();
        object.m.put("A", "A");

        assertEquals(EXPECTED_JSON, mapWithoutHibernateModule(object));
        assertEquals(EXPECTED_JSON, mapWithHibernateModule(object));
    }

    public void testMapWithOneToMany() throws Exception {
        X object = new X();
        object.m.put("A", "A");

        assertEquals(EXPECTED_JSON, mapWithoutHibernateModule(object));
        assertEquals(EXPECTED_JSON, mapWithHibernateModule(object));
    }

    private String mapWithHibernateModule(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate4Module());
        return objectMapper.writeValueAsString(object);
    }

    private String mapWithoutHibernateModule(Object object) throws Exception {
        return new ObjectMapper().writeValueAsString(object);
    }

}
