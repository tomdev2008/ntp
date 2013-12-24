package cn.me.xdf.service;import java.awt.image.BufferedImage;import java.io.ByteArrayOutputStream;import java.io.File;import java.io.IOException;import java.net.URL;import java.util.List;import java.util.Map;import javax.imageio.ImageIO;import javax.xml.namespace.QName;import cn.me.xdf.common.httpclient.FilePart2;import cn.me.xdf.common.httpclient.StringPart2;import cn.me.xdf.common.json.JsonUtils;import cn.me.xdf.model.base.DocInterfaceModel;import org.apache.commons.httpclient.HttpClient;import org.apache.commons.httpclient.HttpStatus;import org.apache.commons.httpclient.methods.PostMethod;import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;import org.apache.commons.httpclient.methods.multipart.Part;import org.apache.commons.httpclient.params.HttpMethodParams;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import cn.me.xdf.common.hibernate4.Finder;import cn.me.xdf.common.page.Pagination;import cn.me.xdf.model.organization.RoleEnum;import cn.me.xdf.model.organization.SysOrgDepart;import cn.me.xdf.model.organization.SysOrgPerson;import cn.me.xdf.model.organization.SysOrgPersonTemp;import cn.me.xdf.utils.MD5Util;import cn.me.xdf.webservice.client.ad.ADApi;import cn.me.xdf.webservice.client.ad.ADApiSoap;import cn.me.xdf.webservice.client.ad.ExtensionDataObject;import cn.me.xdf.webservice.client.ad.UserInfo;@Service@Transactional(readOnly = true)public class RegisterService extends BaseService {    private static final Logger log = LoggerFactory            .getLogger(RegisterService.class);    public static final String DB_SCHEMA = "DB_IXDF";    @Autowired    private SysOrgDepartService SysOrgDepartService;    @Autowired    private UserRoleService userRoleService;    @Autowired    private AccountService accountService;    /**     * 分页查询     *     * @param finder 查询构造器     * @param pageNo 页码     * @return     */    @Transactional(readOnly = true)    public Pagination getPage(Finder finder, Integer pageNo) {        if (pageNo == null)            pageNo = 1;        return getBaseDao().find(finder, pageNo);    }    /**     * 用户注册用户     *     * @param     * @param     * @return     */    @Transactional(readOnly = false)    public SysOrgPerson registerPerson(SysOrgPerson person) {        person.setFdIsEmp("0");// 未入职        accountService.save(person);        // 添加角色        userRoleService.addUserRole(person.getFdId(), RoleEnum.trainee);        return person;    }    /**     * 对外提供方法可以调用接口远程     *     * @param fdFilePath     * @param id     */    @Transactional(readOnly = false)    public void updatePerToDBIXDF(String loginName, String fdFilePath, String id) {        updatePersonToAd(loginName, fdFilePath);// 调用接口远程    }    private void updatePersonToAd(String loginName, String fdFilePath) {        File file = new File(fdFilePath);        try {            FilePart2 fp = new FilePart2("file", file);            HttpClient client = new HttpClient();            client.getParams().setParameter(                    HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");            PostMethod filePost = new PostMethod(DocInterfaceModel.user_poto_upload_url);            filePost.getParams().setParameter(                    HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");            StringPart2 loginNamePart = new StringPart2("loginName",                    loginName, "utf-8");            Part[] parts = {loginNamePart,fp};            MultipartRequestEntity mre = new MultipartRequestEntity(                    parts, filePost.getParams());            filePost.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");            filePost.setRequestEntity(mre);            int status = 0;            status = client.executeMethod(filePost);            if (status == HttpStatus.SC_OK) {                String json = filePost.getResponseBodyAsString();                Map<String, Object> map = JsonUtils.readObjectByJson(json, Map.class);                if (!"1".equals(map.get("state").toString())) {                    throw new RuntimeException(map.get("errorMsg").toString());                }            } else {                log.error("连接失败");            }        } catch (IOException e) {            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.        }    }    private void updatePersonToDBIXDF(String loginName, String fdFilePath, String sysId) {        // 保存到SYS_ORG_PERSON        try {            BufferedImage buffed = ImageIO.read(new File(fdFilePath));            ByteArrayOutputStream toByte = new ByteArrayOutputStream();            ImageIO.write(buffed, "jpg", toByte);            byte[] bytes = toByte.toByteArray();            final QName SERVICE_NAME = new QName("http://tempuri.org/", "ADApi");            URL wsdlURL = ADApi.WSDL_LOCATION;            ADApi ss = new ADApi(wsdlURL, SERVICE_NAME);            ADApiSoap port = ss.getADApiSoap12();            StringBuilder sign = new StringBuilder();            UserInfo user = new UserInfo();            ExtensionDataObject _getUserThumbnailphoto_userInfoExtensionData = new ExtensionDataObject();            user.setExtensionData(_getUserThumbnailphoto_userInfoExtensionData);            user.setAccountID(loginName);            user.setSystemTag("KM");            user.setCompany("");            user.setDepartment("");            user.setInBeijing(1);            user.setLevel(0);            user.setExtensionData(_getUserThumbnailphoto_userInfoExtensionData);            user.setName("");            user.setPWD("");            user.setThumbnailphoto(bytes);            user.setTelphonenumber("");            user.setTitle("");            sign.append("accountid=").append(user.getAccountID());            sign.append("&company=").append(user.getCompany());            sign.append("&department=").append(user.getDepartment());            sign.append("&inbeijing=").append(user.getInBeijing());            sign.append("&level=").append(user.getLevel());            sign.append("&name=").append(user.getName());            sign.append("&pwd=").append(user.getPWD());            sign.append("&securitykey=neworiental.123456");            sign.append("&systemtag=").append("KM");            sign.append("&telphonenumber=").append(user.getTelphonenumber());            sign.append("&thumbnailphoto=").append(MD5Util.getMD5String(bytes));            sign.append("&title=").append(user.getTitle());            user.setSign(MD5Util.getMD5String(sign.toString()));            cn.me.xdf.webservice.client.ad.StateInfo stateInfo = port.updateUserthumbnailphoto(user);            int state = stateInfo.getState();            String errorMsg = stateInfo.getErrMessage();            log.info("state===" + state);            if (state != 1) {                log.error("errorMsg===" + errorMsg);                throw new RuntimeException("修改头像出现错误，信息：" + errorMsg);            }        } catch (IOException e) {            log.error("修改人员头像出现错误：" + e.getMessage());            throw new RuntimeException(e);        }    }    /**     * 验证身份证     *     * @param str     * @return     */    @SuppressWarnings("unchecked")    public int checkIdentityCard(String str) {        Finder finder = Finder                .create("from SysOrgPerson p where p.fdIdentityCard = :str");        finder.setParam("str", str);        List<SysOrgPersonTemp> list = find(finder);        if (list == null) {            return 0;        }        return list.size();    }    /**     * 验证     *     * @param str     * @return     */    @SuppressWarnings("unchecked")    public int checkIdentitymail(String str) {        Finder finder = Finder                .create("from SysOrgPerson p where p.fdEmail = :str");        finder.setParam("str", str);        List<SysOrgPersonTemp> list = find(finder);        if (list == null) {            return 0;        }        return list.size();    }    @Transactional(readOnly = false)    public List<SysOrgDepart> getDepartsByParent(String id) {        return SysOrgDepartService.findByProperty("hbmParent.fdId", id);    }    @Override    public <T> Class<T> getEntityClass() {        // TODO Auto-generated method stub        return null;    }}