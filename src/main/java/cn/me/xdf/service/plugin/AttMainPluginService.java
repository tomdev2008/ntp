package cn.me.xdf.service.plugin;

import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.service.SimpleService;
import cn.me.xdf.utils.ComUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-31
 * Time: 上午9:16
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = true)
public class AttMainPluginService extends SimpleService {


    @Transactional(readOnly = false)
    public void addToXDFAttMain(AttMain attMain) {
        Map<String, Object> params;
        Map<String, Object> queryParams;
        if (StringUtils.isBlank(attMain.getFdFilePath())) {
            return;
        }
        params = new HashMap<String, Object>();
        queryParams = new HashMap<String, Object>();
        queryParams.put("FD_ID", attMain.getFdId());
        List<Map> lists = findByNamedQuery("selectXdfDB", queryParams,
                Map.class);
        if (lists.isEmpty()) {
            params.put("FD_ID", attMain.getFdId());
            params.put("FD_ID", attMain.getFdId());
            params.put("FD_ATT_ID", attMain.getFdId());
            params.put("FD_FILE_PATH", "");
            params.put("FD_FILE_NAME", attMain.getFdFileName());
            params.put("FD_ATT_SEQUENCE", 0);
            params.put("FD_SAVE_TYPE", "http");
            params.put("FD_TYPE", "otp");
            params.put("FD_SAVE_PARAM", ComUtils.HTTP_URL
                    + "/common/file/download/" + attMain.getFdId());
            params.put("FD_ATT_SWF_STATUS", 0);
            params.put("FD_ENABLED", 1);
            updateByNamedQuery("insertToXdfDB", params);
        } else {
            params.put("FD_FILE_NAME", attMain.getFdFileName());
            params.put("FD_ID", attMain.getFdId());
            updateByNamedQuery("updateXdfDB", params);
        }
    }

}
