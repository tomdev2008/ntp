package cn.me.xdf.service.bam.aspect;

import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.process.SourceNote;
import cn.me.xdf.service.SimpleService;
import cn.me.xdf.service.bam.BamCourseService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-11-7
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
 */
@Service
@Aspect
public class SourceAspect extends SimpleService {

    @Autowired
    private BamCourseService bamCourseService;

    @After(value = "execution(* cn.me.xdf.service.bam.process.SourceNodeService.saveSourceNode(sourceNote))")
    public void saveBamScore(JoinPoint joinPoint,SourceNote sourceNote) {
        Object[] args = joinPoint.getArgs();
        SourceNote note = (SourceNote) args[0];
        if (note == null || !note.getIsStudy())
            return;
        //更新素材
        BamCourse bamCourse = bamCourseService.getCourseByUserIdAndCourseId(note.getFdUserId(), note.getFdCourseId());
        bamCourse.toMateridThroughById(note.getFdMaterialId());
        bamCourseService.update(bamCourse);
    }

}
