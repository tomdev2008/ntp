package cn.me.xdf.common.json;

import cn.me.xdf.JunitBaseTest;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-2
 * Time: 上午11:56
 * To change this template use File | Settings | File Templates.
 */
public class AttMainJsonTest extends JunitBaseTest {

    public void testJsonToObject() {
        String json = "{\"Status\":1,\"ResponseData\":[{\"filenetId\":\"{BB2BD57A-9BFA-40DF-911F-77F029AC785F}\"}],\"Error\":null}\n";
        Map<String, Object> map = JsonUtils.readObjectByJson(json, Map.class);
        System.out.println(map.get("ResponseData").getClass());
        List<Map<String, String>> lists = (List<Map<String, String>>) map.get("ResponseData");
        for (Map<String, String> m : lists) {
            System.out.println(m.get("filenetId"));
        }

    }


}
