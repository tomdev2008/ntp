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
 * Created with IntelliJ IDEA. User: xiaobin268 Date: 13-12-2 Time: 上午10:36 To
 * change this template use File | Settings | File Templates.
 */
public class AttMainPlugin {

	private static final Logger log = LoggerFactory
			.getLogger(AttMainPlugin.class);

	/**
	 * 传输文档
	 * 
	 * @param attMain
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String addDoc(AttMain attMain) {

		try {
			DocInterfaceModel model = new DocInterfaceModel(attMain,
					DocInterfaceModel.addDoc);
			HttpClient client = new HttpClient();
			client.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			PostMethod filePost = new PostMethod(DocInterfaceModel.url);
			filePost.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

			MultipartRequestEntity mre = new MultipartRequestEntity(
					model.getCCToAddModel(), filePost.getParams());
			filePost.setRequestEntity(mre);
			int status = client.executeMethod(filePost);
			if (status == HttpStatus.SC_OK) {
				String json = filePost.getResponseBodyAsString();
				log.info(json);
				Map<String, Object> map = JsonUtils.readObjectByJson(json,
						Map.class);
				if ("1".equals(map.get("Status").toString())) {// 返回成功
					List<Map<String, String>> lists = (List<Map<String, String>>) map
							.get("ResponseData");
					return lists.get(0).get("filenetId");
				} else {
					log.error("addDoc:" + map.get("Error").toString());
					throw new RuntimeException("addDoc:"
							+ map.get("Error").toString());
				}
			} else {
				log.error("addDoc：连接失败,httpCode="+status);
				throw new RuntimeException("addDoc：连接失败,httpCode="+status);
				// e.printStackTrace();
			}
		} catch (Exception e) {
			log.error("addDoc:" + e.getCause());
			// throw new RuntimeException("addDoc:" + e.getCause());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取文档
	 * 
	 * @param attMain
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getSwfPath(AttMain attMain, String type) {

		try {
			DocInterfaceModel model = new DocInterfaceModel(attMain, type);
			HttpClient client = new HttpClient();
			client.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			PostMethod post = new PostMethod(DocInterfaceModel.url);
			post.addParameters(model.getCCToReturnPlayUrlModel());
			int status = client.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				String json = post.getResponseBodyAsString();
				Map<String, Object> map = JsonUtils.readObjectByJson(json,
						Map.class);
				if ("1".equals(map.get("Status").toString())) {
					List<Map<String, String>> lists = (List<Map<String, String>>) map
							.get("ResponseData");
					return lists.get(0).values()
							.toArray(new String[lists.get(0).values().size()])[0];

				} else {
					log.error("getSwfPath:" + map.get("Error").toString());
					throw new RuntimeException("getSwfPath:"
							+ map.get("Error").toString());
				}
			} else {
				throw new RuntimeException("getSwfPath:连接失败");
			}
		} catch (Exception e) {
			log.error("getSwfPath:" + e.getMessage());
			throw new RuntimeException("getSwfPath:" + e.getCause());
			// e.printStackTrace();
		}
	}

}
