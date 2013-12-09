package cn.me.xdf.model.base;

import cn.me.xdf.common.utils.ByteFileObjectUtils;
import cn.me.xdf.common.utils.sso.AES;
import cn.me.xdf.common.utils.sso.AESX3;
import cn.me.xdf.utils.DateUtil;
import jodd.io.FileNameUtil;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-2
 * Time: 上午10:53
 * To change this template use File | Settings | File Templates.
 */
public class DocInterfaceModel {

    //    public static final String url = "http://192.168.25.225:9080/ixdf/doc/docServlet";//testurl
    public static final String url = "http://me.xdf.cn/iportal/doc/docServlet";

    public static final String addDoc = "addDoc";
    public static final String getSwfPath = "getSwfPath";
    public static final String getPlayCode = "getPlayCode";


    private static final String pwdKey = "apidocumentsevel";
    private static final String userName = "kmsadmin";
    private static final String passWord = "filenet20_";
    private static final String appId = "0";
    private static final String appKey = "1";
    private static final String sysCode = "NTP";
    private String isConvert = "1";


    private String timeStrap;
    private String method;
    private String title;
    private String modelName;
    private String docId;
    private String author;
    private String filePath;
    private String fdFileName;

    private String attName;
    private String attId;
    private String sign;


    public DocInterfaceModel(AttMain attMain, String method, String isConvert) {
        this.timeStrap = DateUtil.convertDateToString(new Date());
        this.method = method;
        this.title = attMain.getFdFileName();
        this.docId = attMain.getFdId();
        if (StringUtils.isBlank(attMain.getFdModelName())) {
            this.modelName = "cn.me.xdf.model.base.AttMain";
        } else {
            this.modelName = attMain.getFdModelName();
        }
        this.author = attMain.getFdCreatorId();
        this.filePath = attMain.getFdFilePath();
        this.attName = FileNameUtil.getName(filePath);
        this.attId = attMain.getFileNetId();
        this.fdFileName = attMain.getFdFileName();
    }

    public Part[] getCCToAddModel() throws Exception {

        String signText = (method + appId + appKey + timeStrap + title + modelName + docId + author + sysCode + isConvert).toLowerCase();
        this.sign = AESX3.md5(signText); // 签名
        File file = new File(filePath);
        ByteArrayPartSource byteArrayPartSource = new ByteArrayPartSource(fdFileName, ByteFileObjectUtils.getBytesFromFile(file));
        FilePart fp = new FilePart("file", byteArrayPartSource);
        StringPart username = new StringPart("username",
                this.userName, "utf-8");
        StringPart password = new StringPart("password", AES.encode(
                this.passWord, pwdKey), "utf-8");
        StringPart appId = new StringPart("appId",
                this.appId, "utf-8");
        StringPart appKey = new StringPart("appKey",
                this.appKey, "utf-8");

        StringPart timestrap = new StringPart("timestamp",
                this.timeStrap, "utf-8");
        StringPart method = new StringPart("method",
                this.method, "utf-8");
        StringPart title = new StringPart("title",
                this.title, "utf-8");
        StringPart modelName = new StringPart("modelName",
                this.modelName, "utf-8");
        StringPart docId = new StringPart("docId",
                this.docId, "utf-8");

        StringPart author = new StringPart("author",
                this.author, "utf-8");


        StringPart sysCode = new StringPart("sysCode",
                this.sysCode);
        StringPart sign = new StringPart("sign", this.sign,
                "utf-8");
        StringPart isConver = new StringPart("isConver",
                this.isConvert, "utf-8");
        Part[] parts = {username, password, method, appId, appKey, timestrap,
                modelName, docId, author, sign, sysCode, title, fp, isConver};
        return parts;
    }


    public NameValuePair[] getCCToReturnPlayUrlModel() throws Exception {
        String signText = (this.method + appId + appKey + timeStrap + attId + sysCode + attName).toLowerCase();
        sign = AESX3.md5(signText); // 签名

        NameValuePair[] pairs = {
                new NameValuePair("attId", this.attId),
                new NameValuePair("username", userName),
                new NameValuePair("password", AES.encode(passWord, pwdKey)),
                new NameValuePair("appId", appId),
                new NameValuePair("appKey", appKey),
                new NameValuePair("timestamp", this.timeStrap),
                new NameValuePair("method", this.method),
                new NameValuePair("sysCode", this.sysCode),
                new NameValuePair("attName", attName),
                new NameValuePair("sign", this.sign)
        };
        return pairs;
    }


}
