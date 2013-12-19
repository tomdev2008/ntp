package cn.me.xdf.plugin;

import cn.me.xdf.JunitBaseTest;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.service.plugin.AttMainPlugin;
import org.apache.http.HttpException;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-9
 * Time: 上午10:08
 * To change this template use File | Settings | File Templates.
 */
public class AttMainPluginTest extends JunitBaseTest {

    public void testAddDoc() {
        AttMain attMain = new AttMain();
        attMain.setFileNetId("{0F042B78-B8AD-417C-8B50-9860BAA6ED0E}");
        attMain.setFdFilePath("D:\\temp\\resources\\www\\201312\\19104409qkfo.docx");
        InputStream inputStream = AttMainPlugin.getDocByAttId(attMain);
        try {
            String path = "D:\\19104409qkfo.docx";
            BufferedOutputStream bof = new BufferedOutputStream(
                    new FileOutputStream(path));
            int read = 0;
            while ((read = inputStream.read()) != -1) {
                bof.write(read);
            }
            bof.flush();
            bof.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
