package cn.me.xdf;

import cn.me.xdf.common.json.hibernate4.Hibernate4Module;
import junit.framework.TestCase;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-29
 * Time: 上午11:05
 * To change this template use File | Settings | File Templates.
 */
public class JunitBaseTest extends TestCase {

    protected JunitBaseTest() {
    }

    protected ObjectMapper mapperWithModule(boolean forceLazyLoading) {
        ObjectMapper mapper = new ObjectMapper();
        Hibernate4Module mod = new Hibernate4Module();
        mod.configure(Hibernate4Module.Feature.FORCE_LAZY_LOADING, forceLazyLoading);
        mapper.registerModule(mod);
        return mapper;
    }

    protected void verifyException(Throwable e, String... matches) {
        String msg = e.getMessage();
        String lmsg = (msg == null) ? "" : msg.toLowerCase();
        for (String match : matches) {
            String lmatch = match.toLowerCase();
            if (lmsg.indexOf(lmatch) >= 0) {
                return;
            }
        }
        fail("Expected an exception with one of substrings (" + Arrays.asList(matches) + "): got one with message \"" + msg + "\"");
    }

}
