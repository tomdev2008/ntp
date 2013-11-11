package cn.me.xdf.service.bam.aspect;

import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.process.SourceNote;
import cn.me.xdf.service.bam.BamCourseService;
import org.apache.commons.lang3.BooleanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private BamCourseService bamCourseService;

    /**
     * 资源过滤
     * @param joinPoint
     * @return
     */
    @After(value = "execution(* cn.me.xdf.service.bam.process.SourceNodeService.saveSourceNode(..))")
    public Object saveBamScore(JoinPoint joinPoint) {
        log.info("开始启动资源过滤----------------------");
        Object[] args = joinPoint.getArgs();
        SourceNote note = (SourceNote) args[0];
        if (note == null || !BooleanUtils.toBoolean(note.getIsStudy()))
            return "";
        //更新素材
        BamCourse bamCourse = bamCourseService.getCourseByUserIdAndCourseId(note.getFdUserId(), note.getFdCourseId());
        // @todo 现在只做到设置素材进度，还没有更新章节进度
        bamCourse.toMateridThroughById(note.getFdMaterialId());
        bamCourseService.update(bamCourse);
        return joinPoint.getTarget();
    }
}