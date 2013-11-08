package cn.me.xdf.service.bam.aspect;

import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.process.SourceNote;
import org.apache.commons.lang3.BooleanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-11-8
 * Time: 下午12:00
 * To change this template use File | Settings | File Templates.
 */
@Aspect
public class SourceAspect {

    private static final Logger log = LoggerFactory.getLogger(SourceAspect.class);


    @After(value = "execution(* cn.me.xdf.service.bam.process.SourceNodeService.saveSourceNode(..))")
    public Object saveBamScore(JoinPoint joinPoint) {
        log.info("开始启动资源过滤----------------------");
        return joinPoint.getTarget();
    }
}