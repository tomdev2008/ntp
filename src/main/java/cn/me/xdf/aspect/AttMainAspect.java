package cn.me.xdf.aspect;


import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.service.bam.aspect.SourceAspect;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.plugin.AttMainPlugin;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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


        String fileNetId = AttMainPlugin.addDoc(attMain);

        attMain.setFileNetId(fileNetId);
        attMainService.update(attMain);
        log.info("fileNameId======" + fileNetId);
        return joinPoint.getTarget();
    }
}
