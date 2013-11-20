package cn.me.xdf.service.bam.aspect;

import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.bam.CourseLogic;
import cn.me.xdf.model.process.SourceNote;
import cn.me.xdf.service.bam.BamCourseService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
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
     *
     * @param joinPoint
     * @return
     */
    @After(value = "execution(* cn.me.xdf.service.bam.process.SourceNodeService.saveSourceNode(..))")
    public Object saveBamScoreBySourceNote(JoinPoint joinPoint) {
        log.info("开始启动资源过滤----------------------");
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length < 1) {
            throw new RuntimeException("无效的资源参数");
        }
        if (!(args[0] instanceof SourceNote)) {
            throw new RuntimeException("资源格式不正确");
        }
        SourceNote note = (SourceNote) args[0];
        if (note == null || !BooleanUtils.toBoolean(note.getIsStudy()))
            return "";
        //更新素材
        BamCourse bamCourse = bamCourseService.getCourseByUserIdAndCourseId(note.getFdUserId(), note.getFdCourseId());
        CourseLogic courseLogic = new CourseLogic(bamCourse, note);
        bamCourseService.update(courseLogic.getBamCourse());
        return joinPoint.getTarget();
    }

    @After(value = "execution(* cn.me.xdf.service.bam.BamCourseService.updateCatalogThrough(..))")
    public Object saveBamScoreByCatalog(JoinPoint joinPoint) {
        log.info("开始启动资源过滤----------------------");
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length < 2) {
            throw new RuntimeException("无效的资源参数");
        }
        if (!(args[0] instanceof SourceNote)) {
            throw new RuntimeException("资源格式不正确");
        }
        SourceNote note = (SourceNote) args[0];
        if (note == null || !BooleanUtils.toBoolean(note.getIsStudy()))
            return "";
        String catalogId = (String) args[1];
        //更新素材
        BamCourse bamCourse = bamCourseService.getCourseByUserIdAndCourseId(note.getFdUserId(), note.getFdCourseId());
        CourseLogic courseLogic = new CourseLogic(bamCourse, null, catalogId, CourseLogic.CATALOG_UPDATE);
        bamCourseService.update(courseLogic.getBamCourse());

        return joinPoint.getTarget();
    }
}