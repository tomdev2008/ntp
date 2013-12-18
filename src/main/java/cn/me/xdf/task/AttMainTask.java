package cn.me.xdf.task;

import cn.me.xdf.aspect.SourceAspect;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.system.SysAppConfig;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.plugin.AttMainPlugin;
import cn.me.xdf.service.system.SysAppConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-9
 * Time: 下午12:47
 * To change this template use File | Settings | File Templates.
 */
public class AttMainTask {

    @Autowired
    private TaskExecutor taskExecutor;

    private static final Logger log = LoggerFactory.getLogger(SourceAspect.class);

    @Autowired
    private AttMainService attMainService;

    @Autowired
    private SysAppConfigService sysAppConfigService;


    private int getFlagByFileNetId(String fileNetId) {
        if (StringUtils.isBlank(fileNetId)) {
            return -1;
        }
        return 1;

    }

    public void run(final AttMain attMain) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if ("04".equals(attMain.getFdFileType()) || "05".equals(attMain.getFdFileType())) {
                    log.info("开始执行文档上传接口");
                    String fileNetId = AttMainPlugin.addDoc(attMain, "1");

                    attMain.setFileNetId(fileNetId);
                    attMain.setFlag(getFlagByFileNetId(fileNetId));
                    log.info("fileNameId======" + fileNetId);
                    attMainService.update(attMain);
                } else if ("01".equals(attMain.getFdFileType())) {
                    log.info("开始执行视频上传接口");
                    SysAppConfig sysAppConfig = sysAppConfigService.findByKeyAndParam("cn.me.xdf.model.base.AttMain", "CALL_BACK_URL");
                    String callback_url = "NTP";
                    if (sysAppConfig != null) {
                        callback_url = sysAppConfig.getFdValue();
                    }
                    String playCode = AttMainPlugin.addDocNtp(attMain,callback_url);
                    String fileNetId = AttMainPlugin.addDoc(attMain, "0");
                    if (StringUtils.isNotBlank(playCode)) {
                        if ("-1".equals(playCode)) {
                            attMain.setFlag(-1);
                            attMainService.update(attMain);
                        } else {
                            String playUrl = "http://union.bokecc.com/player?vid="
                                    + playCode
                                    + "&siteid=8B90641B41283EDC&autoStart=true&playerid=628A174866D77DB5&playertype=1";
                            attMain.setFileNetId(fileNetId);
                            attMain.setPlayCode(playCode);
                            attMain.setFlag(1);
                            attMain.setFileUrl(playUrl);
                            attMainService.update(attMain);
                        }
                    }
                }
            }
        });
    }
}
