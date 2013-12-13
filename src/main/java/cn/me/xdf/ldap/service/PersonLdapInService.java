package cn.me.xdf.ldap.service;

import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.common.utils.Identities;
import cn.me.xdf.ldap.BaseLdapDao;
import cn.me.xdf.ldap.LdapUtils;
import cn.me.xdf.ldap.model.PeronLdap;
import cn.me.xdf.model.organization.SysOrgConstant;
import cn.me.xdf.model.organization.SysOrgElement;
import cn.me.xdf.service.SysOrgPersonService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ldap.control.PagedResult;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.control.PagedResultsRequestControl;
import org.springframework.ldap.core.*;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-12
 * Time: 下午6:01
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PersonLdapInService extends BaseLdapDao<PeronLdap> {


    @Autowired
    private SysOrgPersonService sysOrgPersonService;


    public void initData(List<Map<String, Object>> values) {
        updateOrg(values);
    }


    private String updateOrg(List<Map<String, Object>> values) {
        int insertSize = 0;
        int updateSize = 0;
        for (Map<String, Object> map : values) {
            List<Map> lists = sysOrgPersonService.findByNamedQuery("person.selectElementByKey", map, Map.class);
            List<Map> departList = sysOrgPersonService.findByNamedQuery("person.selectElementByFdNo", map, Map.class);
            if (CollectionUtils.isEmpty(lists)) {
                Map departMap = departList.get(0);
                Object v = departMap.get("FDID");
                if(v!=null){
                    departMap.put("FD_PARENTID",v);
                }
            }
            if (CollectionUtils.isEmpty(lists)) {
                sysOrgPersonService.updateByNamedQuery("saveElement", map);
                System.out.println("INSERT INTO SYS_ORG_PERSON(FDID,FD_LOGIN_NAME,FD_PASSWORD,FDEMAIL,FDMOBILENO,FD_WORK_PHONE,FD_IDENTITY_CARD,FD_IS_EMP,FD_SEX)\n" +
                        "\t\tVALUES('"+map.get("FDID")+"','"+map.get("FD_LOGIN_NAME")+"','"+map.get("FD_PASSWORD")+"','"+map.get("FD_EMAIL")+"','"+map.get("FDMOBILENO")+"','"+map.get("FD_WORK_PHONE")+"','"+map.get("FD_IDENTITY_CARD")+"','"+map.get("FD_IS_EMP")+"','"+map.get("FD_SEX")+"')");
                sysOrgPersonService.updateByNamedQuery("person.saveElement", map);
                insertSize++;
            } else {
                //sysOrgPersonService.updateByNamedQuery("person.updateElement", map);
                updateSize++;
            }
        }


        return "人员：本次新增" + insertSize + ",更新:" + updateSize;
    }


}
