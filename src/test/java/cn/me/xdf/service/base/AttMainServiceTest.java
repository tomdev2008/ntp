package cn.me.xdf.service.base;

import cn.me.xdf.BaseTest;
import cn.me.xdf.service.plugin.AttMainPlugin;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-2
 * Time: 下午3:03
 * To change this template use File | Settings | File Templates.
 */
public class AttMainServiceTest extends BaseTest {

    @Autowired
    private AttMainService attMainService;

    @Test
    public void testAddAttMain() {
        //attMainService.save(null);
        System.out.println(AttMainPlugin.getSwfPath(attMainService.get("1429d050d954ab9842240c146a2a8189")));
    }
}
