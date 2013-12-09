package cn.me.xdf.aspect;


import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.plugin.AttMainPlugin;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-2
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
@Aspect
public class AttMainAspect {

    private static final Logger log = LoggerFactory.getLogger(SourceAspect.class);

    @Autowired
    private AttMainService attMainService;

    @Autowired
    private TaskExecutor taskExecutor;

    /**
     * 資源接口
     *
     * @param joinPoint
     * @param result
     * @return
     */
    @AfterReturning(value = "execution(* cn.me.xdf.service.base.AttMainService.save(..))", returning = "result")
    public Object afterSaveAttMain(JoinPoint joinPoint, Object result) {

        log.info("开始启动资源过滤------------afterSaveAttMain----------");
        if (result == null) {
            return null;
        }
        if (!(result instanceof AttMain)) {
            throw new RuntimeException("不支持的格式类型");
        }
        AttMain attMain = (AttMain) result;
        run(attMain);
        return joinPoint.getTarget();
    }


    private void run(final AttMain attMain) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if ("04".equals(attMain.getFdFileType()) || "05".equals(attMain.getFdFileType())) {
                    log.info("开始执行文档上传接口");
                    String fileNetId = AttMainPlugin.addDoc(attMain);
                    attMain.setFileNetId(fileNetId);
                    log.info("fileNameId======" + fileNetId);
                } else if ("01".equals(attMain.getFdFileType())) {
                    log.info("开始执行视频上传接口");
                    String playCode = AttMainPlugin.addDocNtp(attMain);
                    if (StringUtils.isNotBlank(playCode) && !("-1".equals(playCode))) {
                        String playUrl = "http://union.bokecc.com/player?vid="
                                + playCode
                                + "&siteid=8B90641B41283EDC&autoStart=true&playerid=628A174866D77DB5&playertype=1";
                        attMain.setFileUrl(playUrl);
                    }
                }
                attMainService.update(attMain);
            }
        });
    }
}
