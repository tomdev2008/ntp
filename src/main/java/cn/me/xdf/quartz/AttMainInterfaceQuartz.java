package cn.me.xdf.quartz;

import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.service.base.AttMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-6
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class AttMainInterfaceQuartz implements Serializable {

    @Autowired
    private AttMainService attMainService;


    public void executeTodo() {
        List<AttMain> attMainList = attMainService.findByCriteria(AttMain.class, Value.isNotNull("fileUrl"), Value.in("fdFileType", new String[]{"01", "04", "05"}));
        if (CollectionUtils.isEmpty(attMainList))
            return;
        for (AttMain attMain : attMainList) {
            attMain.setFileUrl(attMain.getFileUrl());
        }
    }
}
