package cn.me.xdf.plugin;

import cn.me.xdf.JunitBaseTest;
import cn.me.xdf.service.plugin.AttMainPlugin;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-9
 * Time: 上午10:08
 * To change this template use File | Settings | File Templates.
 */
public class AttMainPluginTest extends JunitBaseTest {

    public void testAddDoc() {
        AttMainPlugin.addDocNtp(null,null);
    }
}
