package cn.me.xdf.quartz;

import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.task.AttMainTask;
import org.apache.commons.lang3.StringUtils;
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


    @Autowired
    private AttMainTask attMainTask;


    public void executeTodo() {
        List<AttMain> attMainList = attMainService.findByCriteria(AttMain.class, Value.or(Value.isNull("fileUrl"), Value.ne("flag", 1)), Value.in("fdFileType", new String[]{"01", "04", "05"}));
        if (CollectionUtils.isEmpty(attMainList))
            return;
        for (AttMain attMain : attMainList) {
            if (attMain.getFlag() == -1) {
                attMainTask.run(attMain);
            } else if (StringUtils.isBlank(attMain.getFileUrl())) {
                attMain.setFileUrl(attMain.getFileUrl());
                attMainService.update(attMain);
            }
        }
    }
}
