package cn.me.xdf.common;

import cn.me.xdf.JunitBaseTest;
import jodd.io.FileNameUtil;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-2
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
public class FileUtilsTest extends JunitBaseTest {

    public void testGetFileName() {
        System.out.println(FileNameUtil.getName("D:\\temp\\resources\\www\\201311\\28124343yaz4.jpg"));
    }
}
