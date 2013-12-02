package cn.me.xdf.service.plugin;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.DocInterfaceModel;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-2
 * Time: 上午10:36
 * To change this template use File | Settings | File Templates.
 */
public class AttMainPlugin {

    private static final Logger log = LoggerFactory.getLogger(AttMainPlugin.class);


    public static String addDoc(AttMain attMain) {

        try {
            DocInterfaceModel model = new DocInterfaceModel(attMain, DocInterfaceModel.addDoc);
            HttpClient client = new HttpClient();
            client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
                    "utf-8");
            PostMethod filePost = new PostMethod(DocInterfaceModel.url);
            filePost.getParams().setParameter(
                    HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

            MultipartRequestEntity mre = new MultipartRequestEntity(model.getCCToAddModel(),
                    filePost.getParams());
            filePost.setRequestEntity(mre);
            int status = client.executeMethod(filePost);
            if (status == HttpStatus.SC_OK) {
                String json = filePost.getResponseBodyAsString();
                log.info(json);
                Map<String, Object> map = JsonUtils.readObjectByJson(json, Map.class);
                if ("1".equals(map.get("Status").toString())) {//返回成功
                    List<Map<String, String>> lists = (List<Map<String, String>>) map.get("ResponseData");
                    return lists.get(0).get("filenetId");
                } else {
                    log.error(map.get("Error").toString());
                    throw new RuntimeException(map.get("Error").toString());
                }
            } else {
                throw new RuntimeException("连接失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getCause());
        }
    }

    public static String getSwfPath(AttMain attMain) {

        try {
            DocInterfaceModel model = new DocInterfaceModel(attMain, DocInterfaceModel.getSwfPath);
            HttpClient client = new HttpClient();
            client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
                    "utf-8");
            PostMethod post = new PostMethod(DocInterfaceModel.url);
            post.addParameters(model.getCCToReturnPlayUrlModel());
            int status = client.executeMethod(post);
            if (status == HttpStatus.SC_OK) {
                String json = post.getResponseBodyAsString();
                Map<String, Object> map = JsonUtils.readObjectByJson(json, Map.class);
                if ("1".equals(map.get("Status").toString())) {
                    List<Map<String, String>> lists = (List<Map<String, String>>) map.get("ResponseData");
                    return lists.get(0).get("swf地址");
                } else {
                    log.error(map.get("Error").toString());
                    throw new RuntimeException(map.get("Error").toString());
                }
            } else {
                throw new RuntimeException("连接失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getCause());
            //e.printStackTrace();
        }
    }


}
