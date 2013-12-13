package cn.me.xdf.ldap.service;

import cn.me.xdf.common.utils.Identities;
import cn.me.xdf.ldap.LdapUtils;
import cn.me.xdf.utils.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-12
 * Time: 下午5:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DepartLdapInService extends LdapInService {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private LdapLogService ldapLogService;

    @Override
    public void initData() {
        List<Map<String, Object>> list = ldapTemplate.search(
                "ou=部门", "(&(objectClass=xdf-org))",
                new DepartContextMapper());
        String msg = updateOrg(list);
        ldapLogService.saveLog(msg);
    }

    @Override
    public String executeUpdateData(int day) {
        String date = DateUtil.convertDateToString(DateUtils.addDays(new Date(), 0 - day), "yyyyMMddHHmmss.ssssss");
        List<Map<String, Object>> list = ldapTemplate.search(
                "ou=部门", "(&(objectClass=xdf-org)(modifyTimeStamp>=" + date + "))",
                new DepartContextMapper());
        String msg = updateOrg(list);
        ldapLogService.saveLog(msg);
        return msg;
    }


    private String updateOrg(List<Map<String, Object>> values) {
        Map<String, String> fdIds = new ConcurrentHashMap<String, String>();
        int insertSize = 0;
        int updateSize = 0;
        for (Map<String, Object> map : values) {
            List<Map> lists = findByNamedQuery("org.selectElementByKey", map, Map.class);
            if (CollectionUtils.isEmpty(lists)) {
                fdIds.put(map.get("FD_NO").toString(), map.get("FDID").toString());
                updateByNamedQuery("saveElement", map);
                updateByNamedQuery("org.saveElement", map);
                insertSize++;
            } else {
                fdIds.put(map.get("FD_NO").toString(), lists.get(0).get("FDID").toString());
                updateByNamedQuery("updateElement", map);
                updateSize++;
            }
        }

        for (Map<String, Object> map : values) {
            if (map.get("FD_PARENTID") != null) {
                map.put("FD_PARENTID", fdIds.get(map.get("FD_PARENTID").toString()).toString());
            }
            updateByNamedQuery("updateElementParent", map);
        }
        return "部门：本次新增" + insertSize + ",更新:" + updateSize;
    }


    private static class DepartContextMapper implements ContextMapper {
        public Object mapFromContext(Object ctx) {
            DirContextAdapter context = (DirContextAdapter) ctx;
            Map<String, Object> map = new HashMap<String, Object>();
            //FD_ID,AVAILABLE,CREATETIME,FD_NAME,FD_NO,FD_ORG_TYPE,LDAPDN,FD_PARENTID
            map.put("FDID", Identities.generateID());
            map.put("AVAILABLE", true);
            map.put("CREATETIME", new Date());
            map.put("FD_NAME", context.getStringAttribute("displayName"));
            map.put("FD_NO", context.getStringAttribute("departmentNumber"));
            map.put("FD_ORG_TYPE", LdapUtils.getOrgType(context.getStringAttribute("departmentNumber")));
            map.put("LDAPDN", context.getStringAttribute("dn"));
            if ("0".equals(context.getStringAttribute("parentId"))) {
                map.put("FD_PARENTID", null);
            } else {
                map.put("FD_PARENTID", context.getStringAttribute("parentId"));
            }

            return map;
        }
    }
}
